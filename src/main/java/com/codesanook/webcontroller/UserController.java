package com.codesanook.webcontroller;

import com.codesanook.dto.users.UserEditProfileRequest;
import com.codesanook.dto.users.UserProfileDto;
import com.codesanook.dto.users.UserTimeZone;
import com.codesanook.interceptor.Authorize;
import com.codesanook.interceptor.LoggedInUser;
import com.codesanook.model.UploadedFile;
import com.codesanook.model.User;
import com.codesanook.service.FileService;
import com.codesanook.service.UserService;
import com.mangofactory.swagger.annotations.ApiIgnore;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.security.GeneralSecurityException;

@Controller
@RequestMapping("/user")
@Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_UNCOMMITTED)
@ApiIgnore
public class UserController {

    private UserService userService;
    private FileService fileService;
    private Log log = LogFactory.getLog(UserController.class);

    @Autowired
    public UserController(UserService userService, FileService fileService) {
        this.userService = userService;
        this.fileService = fileService;
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        return "user/login";
    }


    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout() {
        userService.logoutLoggedInUser();
        return "redirect:/";
    }


    @Authorize
    @RequestMapping("/profile/{userId}")
    public String showUserProfile(@PathVariable int userId, Model model,
                                  HttpServletRequest httpRequest) {

        UserProfileDto userProfile = userService.getUserProfile(userId);
        model.addAttribute("userProfile", userProfile);
        return "user/profile";
    }


    @Authorize
    @RequestMapping(value = "/edit/{userId}", method = RequestMethod.GET)
    public String editProfile(@PathVariable int userId,
                              UserEditProfileRequest userEditProfileRequest,
                              Model model, HttpServletRequest httpRequest) {

        LoggedInUser loggedInUser = LoggedInUser.getLogginUser(httpRequest);
        loggedInUser.validateIfOwnsResource(userId);

        User user = userService.getUserById(userId);
        userEditProfileRequest.setId(user.getId());
        userEditProfileRequest.setName(user.getName());
        String profileImageUrl = User.DEFAULT_PROFILE_URL;
        if (user.getProfileImage() != null) {
            profileImageUrl = fileService.getAbsoluteUrl(user.getProfileImage().getRelativePath());
        }
        userEditProfileRequest.setProfileImageUrl(profileImageUrl);
        model.addAttribute("userEditProfileRequest", userEditProfileRequest);
        return "user/edit-profile";
    }

    @Authorize
    @RequestMapping(value = "/edit/{userId}", method = RequestMethod.POST)
    public String editProfile(@PathVariable int userId,
                              @Valid UserEditProfileRequest userEditProfileRequest,
                              BindingResult bindingResult,
                              @RequestParam("file") MultipartFile file,
                              Model model,
                              HttpServletRequest httpRequest) throws IOException, GeneralSecurityException, InterruptedException {

        LoggedInUser loggedInUser = LoggedInUser.getLogginUser(httpRequest);
        loggedInUser.validateIfOwnsResource(userId);

        userEditProfileRequest.setId(userId);
        UploadedFile uploadedFile = null;
        if (!file.isEmpty()) {
            uploadedFile = fileService.addUploadedFile(file, 500, 500);
            String absoluteUrl = fileService.getAbsoluteUrl(uploadedFile.getRelativePath());
            userEditProfileRequest.setProfileImageUrl(absoluteUrl);
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("userEditProfileRequest", userEditProfileRequest);
            return "user/edit-profile";
        }

        User user = userService.getUserById(userId);
        user.setName(userEditProfileRequest.getName());
        if (uploadedFile != null) {
            user.setProfileImage(uploadedFile);
        }

        //force new cookie
        userService.saveNewLoggedInUserObject(user);

        return String.format("redirect:/user/profile/%d", userId);
    }

    @RequestMapping(value = "/set-session-timezone", method = RequestMethod.PUT)
    @ResponseBody
    public void setSessionTimeZone(@RequestBody UserTimeZone userTimeZone,
                                   HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.setAttribute(UserTimeZone.KEY, userTimeZone.getTimeZoneMillisecond());

    }

}
