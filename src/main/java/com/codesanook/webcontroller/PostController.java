package com.codesanook.webcontroller;

import com.codesanook.dto.TagDto;
import com.codesanook.dto.MetaTag;
import com.codesanook.dto.posts.*;
import com.codesanook.interceptor.Authorize;
import com.codesanook.model.*;
import com.codesanook.service.FileService;
import com.codesanook.service.PostService;
import com.codesanook.util.PaginationComponent;
import com.mangofactory.swagger.annotations.ApiIgnore;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.List;


@ApiIgnore
@Controller
@Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_UNCOMMITTED)
public class PostController {

    private Log log = LogFactory.getLog(PostController.class);

    @Autowired
    private Environment env;
    @Value("${post.item-per-page}")
    private Integer itemsPerPage;

    private PostService postService;
    private FileService fileService;


    @Value("${featured.tags}")
    private String[] featuredTags;

    @Autowired
    public PostController(PostService postService, FileService fileService) {
        this.postService = postService;
        this.fileService = fileService;
    }


    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String feedPost(Model model) {

        model.addAttribute("featuredTags", featuredTags);
        String postType = "knowledge";
        PostType postTypeObj = postService.getPostTypeByName(postType);
        PostStatus publishStatus = postService.getPostStatus(PostStatusEnum.PUBLISHED);
        PostDtoList response = postService.getPostsByType(postTypeObj, 0, itemsPerPage, "desc",
                publishStatus);
        List<TagDto> tagResponse = postService.getAllTags();
        model.addAttribute("postResponse", response);
        model.addAttribute("tags", tagResponse);

        String urlPattern = "/post/list/{0}/{1}/{2}";
        setPaginationAttribute(model, response.getTotalPagesCount(), urlPattern, 0,
                String.valueOf(itemsPerPage), "desc");

        return "home";
    }


    @RequestMapping(value = {"/post/list/{pageIndex}/{itemsPerPage}/{sort}"},
            method = RequestMethod.GET)
    public String showPostList(@PathVariable int pageIndex,
                               @PathVariable int itemsPerPage,
                               @PathVariable String sort,
                               Model model) {

        String postType = "knowledge";
        PostType postTypeObj = postService.getPostTypeByName(postType);
        PostStatus publishStatus = postService.getPostStatus(PostStatusEnum.PUBLISHED);
        PostDtoList response = postService.getPostsByType(postTypeObj, pageIndex, itemsPerPage, sort,
                publishStatus);
        List<TagDto> tagResponse = postService.getAllTags();
        model.addAttribute("postResponse", response);
        model.addAttribute("tags", tagResponse);

        String urlPattern = "/post/list/{0}/{1}/{2}";
        setPaginationAttribute(model, response.getTotalPagesCount(), urlPattern, pageIndex,
                String.valueOf(itemsPerPage), "desc");
        return "post/post-list";
    }

    private void setPaginationAttribute(Model model, int totalPagesCount,
                                        String urlPattern, int pageIndex,
                                        String... params) {
        PaginationComponent paginationComponent = new PaginationComponent(totalPagesCount, urlPattern, pageIndex, params);
        model.addAttribute("pagination", paginationComponent.render());
    }


    @RequestMapping("/post/details/{alias}/{postId}")
    public String details(@PathVariable int postId, @PathVariable String alias, Model model) {
        PostDto postDto = postService.getPost(postId);
        model.addAttribute("post", postDto);

        List<TagDto> tags = postService.getAllTags();
        model.addAttribute("tags", tags);
        model.addAttribute("title", postDto.getTitle());

        MultipartPostDto multipartPostDto = postService.getMultipartPostByPostId(postId);
        model.addAttribute("multipartPost", multipartPostDto);

        setMetaTag(postDto, model);
        return "post/post-details";
    }

    private void setMetaTag(PostDto postDto, Model model) {
        MetaTag metaTag = new MetaTag();
        metaTag.setOgTitle(postDto.getTitle());
        metaTag.setOgDescription(postDto.getShortIntroduction());

        String url = String.format("http://%s/post/details/%s/%d",
                env.getProperty("domain"), postDto.getAlias(), postDto.getId());
        metaTag.setOgUrl(url);
        metaTag.setOgImage(getFeatureImageUrl(postDto));
        model.addAttribute("metaTag", metaTag);
    }

    private String getFeatureImageUrl(PostDto postDto) {
        if (postDto.getFeaturedImage() != null) {
            return fileService.getAbsoluteUrl(postDto.getFeaturedImage().getRelativePath());
        }


        if (postDto.getPostType().getId() == PostTypeEnum.QUESTION.getId()) {
            return "https://s3-ap-southeast-1.amazonaws.com/codesanook-static/resources/featured-image-question.jpg";
        } else {
            return "https://s3-ap-southeast-1.amazonaws.com/codesanook-static/resources/featured-image-knowledge.jpg";
        }

    }


    @RequestMapping(value = {"/post/list/tag/{tagName}"}, method = RequestMethod.GET)
    public String showPostListByTagName(@PathVariable("tagName") String tagName, Model model) {
        int pageIndex = 0;
        String sort = "desc";
        return showPostListByTagName(tagName, pageIndex, itemsPerPage, sort, model);
    }


    @RequestMapping(value = {"/post/list/tag/{tagName}/{pageIndex}/{itemsPerPage}/{sort}"},
            method = RequestMethod.GET)
    public String showPostListByTagName(@PathVariable String tagName,
                                        @PathVariable int pageIndex,
                                        @PathVariable int itemsPerPage,
                                        @PathVariable String sort, Model model) {
        PostStatus publishStatus = postService.getPostStatus(PostStatusEnum.PUBLISHED);
        Tag tag = postService.getTagByName(tagName);
        PostDtoList response = postService.getPosts(tag, pageIndex, itemsPerPage, sort,
                publishStatus);
        List<TagDto> tagResponse = postService.getAllTags();
        model.addAttribute("postResponse", response);
        model.addAttribute("tags", tagResponse);

        //tag, index, item per page, sort
        String urlPattern = "/post/list/tag/{1}/{0}/{2}/{3}";
        setPaginationAttribute(model, response.getTotalPagesCount(), urlPattern, pageIndex,
                tagName, String.valueOf(itemsPerPage), "desc");
        return "post/post-list";
    }


    @Authorize
    @RequestMapping(value = "/post/create", method = RequestMethod.GET)
    public String createPost(PostDto postDto, Model model,
                             @RequestParam(value = "type", required = false, defaultValue = "0") int postType,
                             @RequestParam(value = "sub-type", required = false, defaultValue = "0") int postSubtype)
                             throws IOException, InterruptedException {

//        setPostTypeIfExist(postDto, postType);
//        setPostSubtypeIfExist(postDto, postSubtype);

        model.addAttribute("post", postDto);
        model.addAttribute("title", "create new post : codesanook.com");
        return "post/post-create-edit";
    }

    private void setPostTypeIfExist(PostDto postDto, int postType) {
        if (postType == 0) return;

        PostTypeDto postTypeDto = new PostTypeDto();
        postTypeDto.setId(postType);
        postDto.setPostType(postTypeDto);
    }

    private void setPostSubtypeIfExist(PostDto postDto, int postSubtype) {
        if (postSubtype == 0) return;

        PostSubtypeDto postSubtypeDto = new PostSubtypeDto();
        postSubtypeDto.setId(postSubtype);
        postDto.setPostSubtype(postSubtypeDto);
    }

    @Authorize
    @RequestMapping(value = "/post/edit/{postId}", method = RequestMethod.GET)
    public String editPost(@PathVariable int postId, Model model) throws IOException, InterruptedException {
        PostDto postDto = postService.getPost(postId);

        model.addAttribute("post", postDto);
        model.addAttribute("title", "edit post : codesanook.com");
        MultipartPostDto multipartPost = postService.getMultipartPostByPostId(postId);
        int multipartPostId = multipartPost != null ? multipartPost.getId() : 0;
        model.addAttribute("multipartPostId", multipartPostId);

        return "post/post-create-edit";
    }


    @RequestMapping(value = {"/post/list/type/{postType}"}, method = RequestMethod.GET)
    public String showPostListByType(@PathVariable("postType") String postType, Model model) {
        int pageIndex = 0;
        String sort = "desc";
        return showPostListByType(postType, pageIndex, itemsPerPage, sort, model);
    }


    @RequestMapping(value = {"/post/list/type/{postType}/{pageIndex}/{itemsPerPage}/{sort}"},
            method = RequestMethod.GET)
    public String showPostListByType(@PathVariable String postType,
                                     @PathVariable int pageIndex,
                                     @PathVariable int itemsPerPage,
                                     @PathVariable String sort, Model model) {

        PostType postTypeObj = postService.getPostTypeByName(postType);
        PostStatus publishStatus = postService.getPostStatus(PostStatusEnum.PUBLISHED);
        PostDtoList response = postService.getPostsByType(postTypeObj, pageIndex, itemsPerPage, sort,
                publishStatus);
        List<TagDto> tagResponse = postService.getAllTags();
        model.addAttribute("postResponse", response);
        model.addAttribute("tags", tagResponse);

        String urlPattern = "/post/list/type/{1}/{0}/{2}/{3}";
        setPaginationAttribute(model, response.getTotalPagesCount(), urlPattern, pageIndex,
                postType, String.valueOf(itemsPerPage), "desc");
        return "post/post-list";
    }


    @RequestMapping(value = {"/post/list/sub-type/{postSubtype}"}, method = RequestMethod.GET)
    public String showPostListBySubtype(@PathVariable String postSubtype, Model model) {
        int pageIndex = 0;
        String sort = "desc";
        return showPostListBySubtype(postSubtype, pageIndex, itemsPerPage, sort, model);
    }


    @RequestMapping(value = {"/post/list/sub-type/{postSubtype}/{pageIndex}/{itemsPerPage}/{sort}"},
            method = RequestMethod.GET)
    public String showPostListBySubtype(@PathVariable String postSubtype,
                                        @PathVariable int pageIndex,
                                        @PathVariable int itemsPerPage,
                                        @PathVariable String sort, Model model) {

        PostSubtype postSubtypeObj = postService.getPostSubtypeByName(postSubtype);
        PostStatus publishStatus = postService.getPostStatus(PostStatusEnum.PUBLISHED);
        PostDtoList response = postService.getPostsBySubtype(postSubtypeObj, pageIndex, itemsPerPage, sort,
                publishStatus);
        List<TagDto> tagResponse = postService.getAllTags();
        model.addAttribute("postResponse", response);
        model.addAttribute("tags", tagResponse);

        String urlPattern = "/post/list/sub-type/{1}/{0}/{2}/{3}";
        setPaginationAttribute(model, response.getTotalPagesCount(), urlPattern, pageIndex,
                postSubtype, String.valueOf(itemsPerPage), "desc");
        return "post/post-list";
    }


    @RequestMapping(value = "/post/all-tags", method = RequestMethod.GET)
    public String allTags(Model model) {
        List<TagDto> tags = postService.getAllTags();
        model.addAttribute("tags", tags);
        return "post/all-tags";
    }

}
