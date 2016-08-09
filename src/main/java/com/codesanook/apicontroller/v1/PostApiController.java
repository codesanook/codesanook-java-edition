package com.codesanook.apicontroller.v1;

import com.codesanook.dto.posts.*;
import com.codesanook.dto.users.UserDto;
import com.codesanook.interceptor.Authorize;
import com.codesanook.interceptor.LoggedInUser;
import com.codesanook.model.*;
import com.codesanook.service.FileService;
import com.codesanook.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Transactional( rollbackFor = Exception.class, isolation = Isolation.READ_UNCOMMITTED)
@RequestMapping("/api/posts")
@RestController
public class PostApiController {

    private PostService postService;
    private FileService fileService;

    @Autowired
    private Environment env;
    @Value("${post.item-per-page}")
    private int itemsPerPage;


    @Autowired
    public PostApiController(PostService postService, FileService fileService) {
        this.postService = postService;
        this.fileService = fileService;
    }

    @RequestMapping(value = "/{pageIndex}/{itemsPerPage}/{sort}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public PostDtoList getPosts(@PathVariable int pageIndex, @PathVariable int itemsPerPage,
                                @PathVariable String sort) {
        PostStatus publishStatus = postService.getPostStatus(PostStatusEnum.PUBLISHED);
        return postService.getPosts(pageIndex, itemsPerPage, sort, publishStatus);
    }


    @Authorize
    @RequestMapping(value = "", method = RequestMethod.POST)
    public int addPost(@RequestBody PostDto postDto, HttpServletRequest request) {
        LoggedInUser loggedInUser = LoggedInUser.getLogginUser(request);
        UserDto userDto = new UserDto();
        userDto.setId(loggedInUser.getId());
        postDto.setUser(userDto);
        return postService.addPost(postDto);
    }


    @Authorize
    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<PostMetaDataDto> searchPost(@RequestParam(required = true) String title) {
        return postService.searchPost(title);
    }


    @RequestMapping(value = "/{postId}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public PostDto getPost(@PathVariable int postId) {
        return postService.getPost(postId);
    }


    @RequestMapping(value = "/{postId}/content", method = RequestMethod.GET)
    public PostContentDto getPostContent(@PathVariable int postId) {
        PostDto post = postService.getPost(postId);
        PostContentDto postContentDto = new PostContentDto();
        postContentDto.setHtmlContent(post.getHtmlContent());
        postContentDto.setPostId(post.getId());
        return postContentDto;
    }


    @Authorize
    @RequestMapping(value = "/{postId}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public void updatePost(@PathVariable int postId, @RequestBody PostDto request) {
        postService.updatePost(postId, request);
    }


    @Authorize
    @RequestMapping(value = "/{postId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public void deletePost(@PathVariable int postId) {
        Post post = postService.getPostById(postId);
        PostStatus publishStatus = postService.getPostStatus(PostStatusEnum.UNPUBLISHED);
        post.setPostStatus(publishStatus);
    }


    @Authorize
    @RequestMapping(value = "/{postId}/publish", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public void publishPost(@PathVariable int postId) {
        Post post = postService.getPostById(postId);
        PostStatus publishStatus = postService.getPostStatus(PostStatusEnum.PUBLISHED);
        post.setPostStatus(publishStatus);
    }

    @Authorize
    @RequestMapping(value = "/{postId}/tags", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public void updateTag(@PathVariable int postId,
                          @RequestBody TagDto request) {
        Post existingPost = postService.getPostById(postId);
        for (String tagName : request.getTags()) {
            Tag tag = postService.getTagByName(tagName);
            if (tag == null) {
                tag = new Tag();
                tag.setName(tagName);
                postService.addTag(tag);
            }
            existingPost.addTagIfNotExist(tag);
        }

        existingPost.removeTagsIfMissing(request.getTags());
    }


    @Authorize
    @RequestMapping(value = "/{postId}/files/{fileId}", method = RequestMethod.DELETE)
    public void updateTag(@PathVariable int postId, @PathVariable int fileId) {
        Post post = postService.getPostById(postId);
        UploadedFile file = fileService.getUploadedFile(fileId);
        post.removeUploadedFile(file);

        //remove feature image if required
        if (post.getFeaturedImage() != null && file.getId() == post.getFeaturedImage().getId()) {
            post.removeFeaturedImage();
        }

        fileService.deleteUploadedFile(file.getId());
    }

}
