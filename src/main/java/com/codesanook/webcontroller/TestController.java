package com.codesanook.webcontroller;

import com.codesanook.dto.emails.EmailDto;
import com.codesanook.interceptor.Authorize;
import com.codesanook.interceptor.LoggedInUser;
import com.codesanook.model.UploadedFile;
import com.codesanook.model.User;
import com.codesanook.service.MailService;
import com.codesanook.service.UserService;
import com.codesanook.util.PaginationComponent;
import com.codesanook.util.StringExtensionUtils;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.exception.FacebookOAuthException;
import com.restfb.types.FacebookType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.WebContext;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;

@Transactional( rollbackFor = Exception.class, isolation = Isolation.READ_UNCOMMITTED)
@RequestMapping("/test")
@Controller
public class TestController {

    private Log log = LogFactory.getLog(TestController.class);

    @Autowired
    private Environment env;


    private UserService userService;

    @Autowired
    private MailService mailService;

    @Autowired
    public TestController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/view")
    public String testView() {
        return "test";
    }


    @Authorize
    @RequestMapping(value = "/xml",
            method = RequestMethod.GET,
            produces = {"application/xml", "application/json"})
    public
    @ResponseBody
    UploadedFile testImageJson() throws IOException {

        return new UploadedFile();
    }


    @RequestMapping("/pagination")
    public String pagination(@RequestParam("page") int pageIndex, Model model) {

        int totalPageCount = 20;
        String urlPattern = "/test/pagination?page={0}&a={1}&b={2}";
        PaginationComponent paginationTemplage = new PaginationComponent(totalPageCount, urlPattern, pageIndex, "1", "2");
        model.addAttribute("pagination", paginationTemplage.render());

        return "test/pagination";
    }


    @RequestMapping("/regex")
    @ResponseBody
    public void testRegex() {

        String url = "/host/{0}/xx{1}/{0}";
        log.debug(StringExtensionUtils.format(url, "test", "sci"));
    }


    @RequestMapping("/sendpush")
    @ResponseBody
    public String sendPushnotification(HttpServletRequest request) {

        LoggedInUser loggedInUser = LoggedInUser.getLogginUser(request);
        User user = userService.getUserById(loggedInUser.getId());
        log.debug(String.format("user facebook id %d, access token %s",
                user.getFacebookAppScopeUserId(), user.getFacebookAccessToken()));

        String accessToken = "";
        FacebookClient facebookClient = new DefaultFacebookClient(accessToken, com.restfb.Version.VERSION_2_5);

        String message = "comment on your post";
        try {
            facebookClient.publish(user.getFacebookAppScopeUserId() + "/notifications", FacebookType.class,
                    Parameter.with("template", message));
        } catch (FacebookOAuthException e) {
            if (e.getErrorCode() == 200) {
                log.error("Not an app user");
            } else if (e.getErrorCode() == 100) {
                log.error("Message cannot be longer than 180 characters");
            }
        }
        return "sent";
    }


    @RequestMapping("/sort")
    public String sort() {


        return "test/sort";
    }

    @RequestMapping("/send-mail")
    public void sendMail(@RequestParam String to, HttpServletRequest request, HttpServletResponse response, Locale locale) throws MessagingException {

        EmailDto emailDto = new EmailDto();
        emailDto.setSubject("สวัสดีชาวโลก");
        emailDto.setFromName("โคดสนุก");
        emailDto.setFromEmail("admin@codesanook.com");
        emailDto.setToEmail(to);
        emailDto.setTemplate("email/hello-world-email");

        WebContext ctx = new WebContext(request, response, request.getServletContext(), locale);
        ctx.setVariable("email", to);

        mailService.sendMail(emailDto, ctx);
    }


}
