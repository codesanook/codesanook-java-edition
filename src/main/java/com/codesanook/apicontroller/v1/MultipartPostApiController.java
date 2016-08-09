package com.codesanook.apicontroller.v1;

import com.codesanook.dto.posts.MultipartPostDto;
import com.codesanook.interceptor.Authorize;
import com.codesanook.interceptor.LoggedInUser;
import com.codesanook.model.User;
import com.codesanook.service.PostService;
import com.codesanook.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Transactional( rollbackFor = Exception.class, isolation = Isolation.READ_UNCOMMITTED)
@RequestMapping("/api/multipart-posts")
@RestController
public class MultipartPostApiController {

    private PostService postService;
    private UserService userService;

    @Autowired
    private Environment env;


    @Autowired
    public MultipartPostApiController(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }


    @Authorize
    @RequestMapping(value = "", method = RequestMethod.POST)
    public int addMultipartPost(@RequestBody MultipartPostDto multipartPostDto, HttpServletRequest request) {
        LoggedInUser loggedInUser = LoggedInUser.getLogginUser(request);
        User user = userService.getUserById(loggedInUser.getId());
        return postService.addMultipartPost(multipartPostDto, user);
    }


    @Authorize
    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<MultipartPostDto> searchMultipartPosts(@RequestParam(required = false) String title, HttpServletRequest request) {
        return postService.searchMultipartPosts(title);
    }

    @Authorize
    @RequestMapping(value = "/{multipartPostId}", method = RequestMethod.GET)
    public MultipartPostDto getMultipartPost(
            @PathVariable int multipartPostId, HttpServletRequest request) {
        return postService.getMultipartPostById(multipartPostId);
    }


    @Authorize
    @RequestMapping(value = "/{multipartPostId}", method = RequestMethod.PUT)
    public void updateMultipartPost(@PathVariable int multipartPostId,
                                    @RequestBody MultipartPostDto multipartPostDto, HttpServletRequest request) {

        LoggedInUser loggedInUser = LoggedInUser.getLogginUser(request);
        User user = userService.getUserById(loggedInUser.getId());
        multipartPostDto.setId(multipartPostId);
        postService.updateMultipartPost(multipartPostDto, user);
    }


}
