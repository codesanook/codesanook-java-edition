package com.codesanook.apicontroller.v1;

import com.codesanook.dto.AddRoleRequest;
import com.codesanook.dto.AddUserToRoleRequest;
import com.codesanook.dto.users.*;
import com.codesanook.exception.NoUserWithEmailException;
import com.codesanook.interceptor.Authorize;
import com.codesanook.model.Role;
import com.codesanook.model.UploadedFile;
import com.codesanook.model.User;
import com.codesanook.repository.RoleRepository;
import com.codesanook.repository.UserRepository;
import com.codesanook.service.FileService;
import com.codesanook.service.UserService;
import com.mangofactory.swagger.annotations.ApiIgnore;
import com.wordnik.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;

@Transactional (rollbackFor = Exception.class, isolation = Isolation.READ_UNCOMMITTED)
@Api(basePath = "/users", value = "user", description = "Operations with Landlords",
        produces = "application/json", consumes = "application/json")
@RestController
@RequestMapping(value = "/api/users")
public class UserApiController {

    static Log log = LogFactory.getLog(UserApiController.class);

    private RoleRepository roleRepository;
    private UserRepository userRepository;
    private UserService userService;
    private FileService fileService;

    @Autowired
    public UserApiController(RoleRepository roleRepository,
                             UserRepository userRepository, UserService userService,
                             FileService fileService) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.fileService = fileService;
    }

    @RequestMapping(value = "/role", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public void addRole(@RequestBody AddRoleRequest request) {
        Role role = new Role();
        role.setName(request.getName());
        roleRepository.addRole(role);
    }

    @RequestMapping(value = "/{userId}/role", method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public void addUserToRole(@PathVariable int userId, @RequestBody AddUserToRoleRequest request) {
        roleRepository.getCurrentFlushMode();
        User user = userRepository.getUserById(userId);
        Role role = roleRepository.getRoleById(request.getRoleId());
        role.getUsers().add(user);
        user.getRoles().add(role);
    }

    @Authorize
    @RequestMapping(value = "/{userId}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public UserProfileDto getUserById(@PathVariable int userId) {
        return userService.getUserProfile(userId);
    }


    @RequestMapping(value = "/existing", method = RequestMethod.GET)
    public UserExistingResponse isUserExist(
            @RequestParam(required = false) Long facebookId,
            @RequestParam(required = false) String email) {
        UserExistingResponse response = new UserExistingResponse();

        if (facebookId != null) {
            boolean isExisting = userService.isUserExists(facebookId);
            response.setExistingUser(isExisting);
            return response;
        }

        if (!StringUtils.isEmpty(email)) {
            boolean isExisting = userService.isUserExists(email);
            response.setExistingUser(isExisting);
            return response;
        }

        throw new IllegalStateException("no facebookId or email");
    }


    @Authorize
    @RequestMapping(value = "/{userId}", method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public void edit(@PathVariable int userId, @RequestBody UserEditRequest request) {
        if (request == null) throw new IllegalArgumentException("request");

        User user = userRepository.getUserById(userId);
        user.setName(request.getName());
    }

    @RequestMapping(value = "/facebook", method = RequestMethod.PUT)
    public void updateFacebookInfo(@RequestBody FacebookUserDto facebookUserDto) {
        if (facebookUserDto == null) throw new IllegalArgumentException("request");
        userService.updateUserInfoWithFacebook(facebookUserDto);
    }


    @RequestMapping(value = "/facebook", method = RequestMethod.POST)
    public int addNewUserWithFacebookInfo(@RequestBody FacebookUserDto facebookUserDto) {
        if (facebookUserDto == null) throw new IllegalArgumentException("request");

        if (userService.isUserExists(facebookUserDto.getEmail())) {

            userService.updateUserInfoWithFacebook(facebookUserDto);
            User user = userService.getUserByEmail(facebookUserDto.getEmail());
            return user.getId();

        } else {
            return userService.registerUser(facebookUserDto);
        }
    }


    @RequestMapping(value = "/login/{facebookId}", method = RequestMethod.PUT)
    public UserLogInResponse logInWithFacebook(@PathVariable long facebookId,
                                               @RequestBody UserLogInRequest request) {
        request.setFacebookAppScopeUserId(facebookId);
        return userService.logInUserWithFacebook(request);
    }


    @ApiIgnore
    @RequestMapping("/greeting")
    public String greeting(@RequestParam(value = "name", required = false, defaultValue = "World")
                           String name, Model model) {
        log.debug("call greeting");

        model.addAttribute("name", name);
        return "greeting";
    }


    @RequestMapping(value = "/{userId}/role", method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public void removeUserFromRole(@PathVariable int userId, @RequestBody RemoveUserFromRoleRequest request) {
        User user = userRepository.getUserById(userId);
        Role role = roleRepository.getRoleById(request.getRoleId());
        user.removeRole(role);
    }


    public FileService getFileService() {
        return fileService;
    }

    public void setFileService(FileService fileService) {
        this.fileService = fileService;
    }
}
