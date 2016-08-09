package com.codesanook.admincontroller;

import com.codesanook.dto.users.UserRoleDto;
import com.codesanook.interceptor.Authorize;
import com.codesanook.dto.UserAdminEditRequest;
import com.codesanook.dto.users.UserProfileDto;
import com.codesanook.model.Role;
import com.codesanook.model.RoleEnum;
import com.codesanook.model.User;
import com.codesanook.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import javax.validation.Valid;
import java.util.*;

@Controller
@RequestMapping("/admin/user")
@Transactional(readOnly = false, rollbackFor = Exception.class,
        isolation = Isolation.READ_COMMITTED)
public class UserAdminController {

    private UserService userService;

    @Autowired
    public UserAdminController(UserService userService) {
        this.userService = userService;
    }


    @Authorize(roles = {RoleEnum.ADMIN})
    @RequestMapping(value = "/edit/{userId}", method = RequestMethod.GET)
    public String manageUser(@PathVariable int userId,
                             UserAdminEditRequest userAdminEditRequest,
                             Model model) {
        List<Role> allRoles = userService.getAllRoles();

        User user = userService.getUserById(userId);
        userAdminEditRequest.setUserId(user.getId());
        userAdminEditRequest.setIsDeleted(user.getIsDeleted());

        List<Integer> userRoleIds = convertToRoleIdList(user.getRoles());
        userAdminEditRequest.setUserRoleIds(userRoleIds);

        model.addAttribute("allRoles", allRoles);
        model.addAttribute("userAdminEditRequest", userAdminEditRequest);

        return "admin/user/edit-user";
    }


    @Authorize(roles = {RoleEnum.ADMIN})
    @RequestMapping(value = "/edit/{userId}", method = RequestMethod.POST)
    public String manageUser(@PathVariable int userId,
                             @Valid UserAdminEditRequest userAdminEditRequest,
                             BindingResult bindingResult,
                             Model model) {

        if (bindingResult.hasErrors()) {
            userAdminEditRequest.setUserId(userId);
            List<Role> allRoles = userService.getAllRoles();
            model.addAttribute("allRoles", allRoles);
            model.addAttribute("userAdminEditRequest", userAdminEditRequest);
            return "admin/user/edit-user";
        }

        User user = userService.getUserById(userId);
        user.setIsDeleted(userAdminEditRequest.getIsDeleted());

        List<Role> allRoles = userService.getAllRoles();
        List<Integer> userRoleIds = convertToRoleIdList(user.getRoles());
        List<Integer> editRoleIds = userAdminEditRequest.getUserRoleIds();

        for (Role role : allRoles) {
            boolean isUserHasRole = userRoleIds.indexOf(role.getId()) != -1;
            boolean isEditHasRole = editRoleIds.indexOf(role.getId()) != -1;

            if (isUserHasRole == isEditHasRole) continue;

            if (isUserHasRole && !isEditHasRole) {
                user.removeRole(role);
            }
            if (!isUserHasRole && isEditHasRole) {
                user.addRole(role);
            }
        }

        return "redirect:/admin/user/list";
    }

    private List<Integer> convertToRoleIdList(List<Role> role) {

        List<Integer> userRoleIds = new ArrayList<>();
        for (Role userRole : role) {
            userRoleIds.add(userRole.getId());
        }
        return userRoleIds;
    }


    @Authorize(roles = {RoleEnum.ADMIN})
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String showAllUsers(Model model) {
        return showAllUsers(0, 20, model);
    }



    @Authorize(roles = {RoleEnum.ADMIN})
    @RequestMapping(value = "/list/{firstItemIndex}/{itemsPerPage}/", method = RequestMethod.GET)
    public String showAllUsers(@PathVariable int firstItemIndex, @PathVariable int itemsPerPage,
                               Model model) {
        List<User> users = userService.getUsers(firstItemIndex, itemsPerPage);
        List<UserProfileDto> userProfiles = new ArrayList<>();
        for (User user : users) {
            UserProfileDto profile = new UserProfileDto();
            profile.setId(user.getId());
            profile.setName(user.getName());
            profile.setEmail(user.getEmail());
            profile.setIsActivated(user.getIsActivated());
            profile.setIsDeleted(user.getIsDeleted());

            List<UserRoleDto> roleResponses = new ArrayList<>();
            for (Role role : user.getRoles()) {
                UserRoleDto roleResponse = new UserRoleDto();
                roleResponse.setName(role.getName());
                roleResponse.setId(role.getId());
                roleResponses.add(roleResponse);
            }

            profile.setRoles(roleResponses);
            userProfiles.add(profile);
        }

        model.addAttribute("profileList", userProfiles);
        return "admin/user/list";
    }
}

