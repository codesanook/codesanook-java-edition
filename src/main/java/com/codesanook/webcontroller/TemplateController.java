package com.codesanook.webcontroller;

import com.codesanook.dto.posts.CommentDto;
import com.codesanook.dto.posts.PostDto;
import com.codesanook.dto.posts.PostDtoList;
import com.codesanook.model.Comment;
import com.codesanook.model.Post;
import com.codesanook.service.PostService;
import com.mangofactory.swagger.annotations.ApiIgnore;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/dynamic-templates")
@Transactional( rollbackFor = Exception.class, isolation = Isolation.READ_UNCOMMITTED)
@ApiIgnore
public class TemplateController {

    private Log log = LogFactory.getLog(TemplateController.class);
    PostService postService;

    @Autowired
    public TemplateController(PostService postService) {
        this.postService = postService;
    }


    @RequestMapping(value = "/comment-item", method = RequestMethod.GET)
    public String getCommentItemTemplate(Model model, @RequestParam int commentId) {

        Comment comment = postService.getCommentById(commentId);
        CommentDto commentDto = postService.createCommentDto(comment);
        CommentDto[] comments = new CommentDto[]{
                commentDto
        };

        Post post = comment.getPost();

        model.addAttribute("comments", comments);
        model.addAttribute("post", post);
        return "dynamic-templates/comment-item-container";
    }


    @RequestMapping(value = "/post-item", method = RequestMethod.GET)
    public String getPostItemTemplate(Model model, @RequestParam int postId) {

        PostDtoList response = new PostDtoList();
        List<PostDto> postDtos = new ArrayList<>();
        PostDto postDto = postService.getPost(postId);
        postDtos.add(postDto);

        response.setPosts(postDtos);
        model.addAttribute("postResponse", response);
        return "dynamic-templates/post-item-container";


    }

}

