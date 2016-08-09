package com.codesanook.apicontroller.v1;

import com.codesanook.dto.posts.CommentDto;
import com.codesanook.exception.UnauthorizedException;
import com.codesanook.interceptor.Authorize;
import com.codesanook.interceptor.LoggedInUser;
import com.codesanook.model.Comment;
import com.codesanook.model.RoleEnum;
import com.codesanook.service.MailService;
import com.codesanook.service.PostService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.WebContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

@Transactional( rollbackFor = Exception.class, isolation = Isolation.READ_UNCOMMITTED)
@RequestMapping(value = "/api/comments")
@RestController
public class CommentApiController {

    private Log log = LogFactory.getLog(CommentApiController.class);
    private PostService postService;
    private MailService mailService;

    @Autowired
    public CommentApiController(PostService postService) {
        this.postService = postService;
    }


    @RequestMapping(value = "/{commentId}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public CommentDto getComment(@PathVariable int commentId) {
        Comment comment = postService.getCommentById(commentId);
        CommentDto commentDto = postService.toCommentDto(comment);
        return commentDto;
    }


    @Authorize
    @RequestMapping(value = "", method = RequestMethod.POST)
    public int addComment(@RequestBody CommentDto commentDto,
                          HttpServletRequest request,
                          HttpServletResponse response,
                          Locale locale) {

        String userIp = request.getRemoteAddr();
        if (StringUtils.isEmpty(userIp)) {
            userIp = request.getHeader("X-FORWARDED-FOR");
        }
        commentDto.setUserIp(userIp);

        WebContext ctx = new WebContext(request, response, request.getServletContext(), locale);
        int postId = postService.addComment(commentDto, ctx);
        return postId;
    }


    @Authorize
    @RequestMapping(value = "/{commentId}", method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public void updateComment(@PathVariable int commentId,
                              @RequestBody CommentDto commentDto, HttpServletRequest httpRequest) {
        Comment comment = postService.getCommentById(commentId);
        LoggedInUser loggedInUser = LoggedInUser.getLogginUser(httpRequest);
        if (loggedInUser.owningResource(comment.getUser().getId()) || loggedInUser.havingRole(RoleEnum.ADMIN)) {

            if (!StringUtils.isEmpty(commentDto.getContent())) {
                comment.setContent(commentDto.getContent());
            }

            if (!StringUtils.isEmpty(commentDto.getHtmlContent())) {
                comment.setHtmlContent(postService.cleanHtmlContent(commentDto.getHtmlContent()));
            }

        } else {
            throw new UnauthorizedException("invalid permission");
        }
    }


    @Authorize
    @RequestMapping(value = "/{commentId}", method = RequestMethod.DELETE)
    public void deleteComment(@PathVariable int commentId, HttpServletRequest httpRequest) {

        Comment comment = postService.getCommentById(commentId);
        LoggedInUser loggedInUser = LoggedInUser.getLogginUser(httpRequest);
        if (loggedInUser.owningResource(comment.getUser().getId()) || loggedInUser.havingRole(RoleEnum.ADMIN)) {
            comment.delete();
        } else {
            throw new UnauthorizedException("invalid permission");
        }
    }

}
