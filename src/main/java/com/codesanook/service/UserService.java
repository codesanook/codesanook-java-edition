package com.codesanook.service;

import com.codesanook.dto.emails.EmailDto;
import com.codesanook.exception.*;
import com.codesanook.interceptor.LoggedInUser;
import com.codesanook.dto.users.*;
import com.codesanook.model.FacebookUser;
import com.codesanook.model.Role;
import com.codesanook.model.UploadedFile;
import com.codesanook.model.User;
import com.codesanook.repository.RoleRepository;
import com.codesanook.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.Version;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jasypt.util.text.StrongTextEncryptor;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.thymeleaf.context.WebContext;

import javax.mail.MessagingException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private final MailService mailService;
    private Log log = LogFactory.getLog(UserService.class);

    private static final String TOKEN_KEY_NAME = "cs-token";
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private FileService fileService;

//    @Autowired
//    private HttpServletRequest context;

    @Autowired
    private Environment env;

    @Value("${domain}")
    private String domain;

    @Value("${encryption-password}")
    private String ENCRYPTION_PASSWORD;

    @Autowired
    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       MailService mailService, FileService fileService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.mailService = mailService;
        this.fileService = fileService;
    }


    public LoggedInUser createLoggedInUser(User user) {
        LoggedInUser loggedInUser = new LoggedInUser();
        loggedInUser.setId(user.getId());
        loggedInUser.setName(user.getName());
        String profileUrl = User.DEFAULT_PROFILE_URL;
        if (user.getProfileImage() != null) {
            profileUrl = fileService.getAbsoluteUrl(user.getProfileImage().getRelativePath());
        }
        loggedInUser.setProfileImageUrl(profileUrl);
        List<Role> roles = user.getRoles();
        int[] userRoles;
        if (roles == null) {
            userRoles = new int[0];
        } else {
            userRoles = new int[roles.size()];
            for (int i = 0; i < userRoles.length; i++) {
                userRoles[i] = roles.get(i).getId();
            }
        }

        loggedInUser.setRoles(userRoles);
        DateTime utcNow = DateTime.now(DateTimeZone.UTC);
        int expiredInSeconds = getExpiredInSeconds();
        utcNow = utcNow.plusSeconds(expiredInSeconds);
        loggedInUser.setExpired(utcNow);
        return loggedInUser;
    }

    private int getExpiredInSeconds() {
        int expiredInSeconds = 30 * 24 * 60 * 60;//30 days
        return expiredInSeconds;
    }

    public void saveNewLoggedInUserObject(User user) {

        LoggedInUser loggedInUser = createLoggedInUser(user);
        String tokenString = serializeUserObject(loggedInUser);
        int expiredInSeconds = getExpiredInSeconds();
        saveTokenToCookie(tokenString, expiredInSeconds);
    }

    private String safeTrim(String input) {
        if (input == null) return input;
        return input.trim();
    }


    public void logInUser(UserLogInRequest request) {

        request.setEmail(safeTrim(request.getEmail()));
        request.setPassword(safeTrim(request.getPassword()));

        User user = userRepository.getUserByEmail(request.getEmail());
        if (user == null)
            throw new UnauthorizedException(String.format("no user with email %s",
                    request.getEmail()));

        if (!user.getIsActivated()) throw new InvalidActivationException();

        user.validatePassword(request.getPassword());
        saveNewLoggedInUserObject(user);
    }


    private String serializeUserObject(LoggedInUser loggedInUser) {
        //Convert object to JSON string
        if (StringUtils.isEmpty(ENCRYPTION_PASSWORD))
            throw new IllegalStateException("Please set encryption-password value in application.properties file");

        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JodaModule());

            //dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
            mapper.setDateFormat(dateFormat);
            String jsonInString = mapper.writeValueAsString(loggedInUser);


            StrongTextEncryptor textEncryptor = new StrongTextEncryptor();
            textEncryptor.setPassword(ENCRYPTION_PASSWORD);
            String encryptedText = textEncryptor.encrypt(jsonInString);

//            // encode data on your side using BASE64
//            byte[] bytesEncoded = Base64.encodeBase64(encryptedText.getBytes());
//            String encodeString = new String(bytesEncoded);

            String encodeString = URLEncoder.encode(encryptedText, "UTF-8");

            return encodeString;

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        throw new IllegalStateException("error convert user object to JSON string");
    }

    private void saveTokenToCookie(String cookieValue, int expiredInSeconds) {
        Cookie cookie = new Cookie(TOKEN_KEY_NAME, cookieValue);
        cookie.setMaxAge(expiredInSeconds);
        cookie.setPath("/");
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder
                .currentRequestAttributes()).getResponse();
        response.addCookie(cookie);
    }

    public int registerUser(UserRegisterRequest request) {

        request.setEmail(safeTrim((request.getEmail())));
        request.setPassword(safeTrim(request.getPassword()));
        request.setConfirmPassword(safeTrim(request.getConfirmPassword()));
        request.setName(safeTrim(request.getName()));


        validateIfUserExist(request.getEmail());
        User user = new User();
        user.setEmail(request.getEmail());
        user.setName(request.getName());
        user.setPassword(request.getPassword());

        user.validateRequriedProperties();
        userRepository.addUser(user);
        log.info(String.format("new register user password %s", user.getPassword()));
        return user.getId();
    }

    public int registerUser(FacebookUserDto facebookUserDto) {
        User user = new User();
        user.setName(facebookUserDto.getName());
        user.setFacebookAppScopeUserId(facebookUserDto.getFacebookAppScopeUserId());
        String longLifeAccessToken = extendFacebookToken(facebookUserDto.getFacebookAccessToken());
        user.setFacebookAccessToken(longLifeAccessToken);
        user.setEmail(facebookUserDto.getEmail());//set to email from facebook
        DateTime utcNow = DateTime.now(DateTimeZone.UTC);
        user.setUtcCreateDate(utcNow);
        user.setUserAlreadyActivated();

        try {
            UploadedFile profileImage = fileService.addUploadedFile(facebookUserDto.getProfileUrl());
            user.setProfileImage(profileImage);
        } catch (IOException e) {
            log.error(e);
        } catch (GeneralSecurityException e) {
            log.error(e);
        } catch (InterruptedException e) {
            log.error(e);
        }
        userRepository.addUser(user);
        return user.getId();
    }


    private void validateIfUserExist(String email) {
        User user = userRepository.getUserByEmail(email);
        if (user != null)
            throw new IllegalStateException(String.format("user with email %s exists", email));
    }


    public UserProfileDto getUserProfile(int userId) {
        User user = userRepository.getUserById(userId);
        UserProfileDto response = new UserProfileDto();
        response.setId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setIsActivated(user.getIsActivated());
        response.setIsDeleted(user.getIsDeleted());

        String profileUrl = User.DEFAULT_PROFILE_URL;
        if (user.getProfileImage() != null) {
            profileUrl = fileService.getAbsoluteUrl(user.getProfileImage().getRelativePath());
        }
        response.setProfileImageUrl(profileUrl);

        List<UserRoleDto> roles = new ArrayList<>();
        for (Role role : user.getRoles()) {
            UserRoleDto roleResponse = new UserRoleDto();
            roleResponse.setId(role.getId());
            roleResponse.setName(role.getName());
            roles.add(roleResponse);
        }

        response.setRoles(roles);
        return response;
    }

    public void changePassword(int userId, UserChangePasswordRequest request) {
        User user = userRepository.getUserById(userId);
        user.changePassword(request.getCurrentPassword(), request.getNewPassword());
    }

    public void forgotPassword(UserForgotPasswordRequest userForgotPasswordRequest,
                               WebContext ctx) throws MessagingException {
        String email = userForgotPasswordRequest.getEmail();
        User user = userRepository.getUserByEmail(email);
        if (user == null) throw new NoUserWithEmailException(email);

        String newPassword = user.resetPassword();
        UserForgotPasswordResponse userForgotPasswordResponse = new UserForgotPasswordResponse();
        userForgotPasswordResponse.setName(user.getName());
        userForgotPasswordResponse.setNewPassword(newPassword);
        String logInUrl = String.format("http://%s/user/login", domain);
        userForgotPasswordResponse.setLogInUrl(logInUrl);
        ctx.setVariable("userForgotPasswordResponse", userForgotPasswordResponse);

        String fromEmail = "admin@codesanook.com";
        String fromName = "codesanook.com";
        String toEmail = user.getEmail();
        String template = "email/new-password";
        String subject = "your new password";

        EmailDto emailDto = new EmailDto();
        emailDto.setFromEmail(fromEmail);
        emailDto.setFromName(fromName);
        emailDto.setToEmail(toEmail);
        emailDto.setSubject(subject);
        emailDto.setTemplate(template);

        this.mailService.sendMail(emailDto, ctx);
    }

    public List<Role> getAllRoles() {
        List<Role> roles = roleRepository.getAllRoles();
        return roles;
    }

    public User getUserById(int userId) {
        User user = userRepository.getUserById(userId);
        if (user == null) throw new NoUserWithIdException(userId);
        return user;
    }

    public Role getRoleById(int roleId) {
        Role role = roleRepository.getRoleById(roleId);
        return role;
    }


    public List<User> getUsers(int firstItemIndex, int itemsPerPage) {
        return userRepository.getUsers(firstItemIndex, itemsPerPage);
    }

    public void logoutLoggedInUser() {

        Cookie cookie = new Cookie(TOKEN_KEY_NAME, "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        /*
        A negative value means that the cookie is not stored persistently and will be deleted when the Web browser exits.
        A zero value causes the cookie to be deleted.
         */
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder
                .currentRequestAttributes()).getResponse();
        response.addCookie(cookie);
    }

    public User getUserByEmail(String email) {
        User user = userRepository.getUserByEmail(email);
        if (user == null) throw new NoUserWithEmailException(email);
        return user;
    }

    public void sendActivation(User user, WebContext ctx) throws MessagingException {
        user.newActivationCode();

        String fromEmail = "admin@codesanook.com";
        String fromName = "codesanook.com";
        String toEmail = user.getEmail();
        String subject = "Please activate your user account.";
        String template = "email/activate-user";

        String url = String.format("http://%s/user/activate-with-code?userId=%d&code=%s",
                domain, user.getId(), user.getActivationCode());
        ctx.setVariable("activationUrl", url);
        ctx.setVariable("name", user.getName());

        EmailDto emailDto = new EmailDto();
        emailDto.setFromEmail(fromEmail);
        emailDto.setFromName(fromName);
        emailDto.setToEmail(toEmail);
        emailDto.setSubject(subject);
        emailDto.setTemplate(template);

        mailService.sendMail(emailDto, ctx);
    }

    public void activeUserWithCode(User user, String activationCode) {
        if (!activationCode.equals(user.getActivationCode()))
            throw new InvalidActivationException();
        user.setUserAlreadyActivated();
        saveNewLoggedInUserObject(user);
    }

    public String extendFacebookToken(String shortLifeToken) {
        FacebookClient facebookClient = new DefaultFacebookClient(shortLifeToken, Version.VERSION_2_5);
        FacebookClient.AccessToken newLongLifeAccessToken =
                facebookClient.obtainExtendedAccessToken(
                        env.getProperty("facebook.app-id"),
                        env.getProperty("facebook.app-secret",
                                shortLifeToken));
        return newLongLifeAccessToken.getAccessToken();

    }

    public boolean isUserExists(String email) {
        User user = userRepository.getUserByEmail(email);
        if (user != null) return true;
        return false;
    }

    public boolean isUserExists(long facebookAppScopeUserId) {
        log.debug(String.format("facebookAppScopeUserId %d", facebookAppScopeUserId));

        User user = userRepository.getByFacebookId(facebookAppScopeUserId);
        if (user != null) return true;
        return false;
    }

    public void validateFacebookToken(long facebookId, String longLifeToken) {
        log.debug(String.format("current request.getFacebookAccessToken %s", longLifeToken));
        FacebookClient facebookClient = new DefaultFacebookClient(longLifeToken, Version.VERSION_2_5);
        FacebookUser facebookUser = facebookClient.fetchObject("me", FacebookUser.class,
                Parameter.with("fields", "id"));
        if (facebookUser.getId() != facebookId) {
            throw new UnauthorizedException();
        }
    }

    public UserLogInResponse logInUserWithFacebook(UserLogInRequest request) {
        //validate facebook token by query to server
        String longLifeToken = extendFacebookToken(request.getFacebookAccessToken());
        validateFacebookToken(request.getFacebookAppScopeUserId(), longLifeToken);

        User user = userRepository.getByFacebookId(request.getFacebookAppScopeUserId());
        if (user == null) throw new NoUserWithFcebookIdException(request.getFacebookAppScopeUserId());

        user.setFacebookAccessToken(longLifeToken);
        LoggedInUser loggedInUser = createLoggedInUser(user);
        String token = serializeUserObject(loggedInUser);
        UserLogInResponse response = new UserLogInResponse();
        response.setApiToken(token);
        return response;
    }


    public void updateUserInfoWithFacebook(FacebookUserDto facebookUserDto) {
        User user = userRepository.getUserByEmail(facebookUserDto.getEmail());
        if (user == null) {
            throw new NoUserWithEmailException(facebookUserDto.getEmail());
        }

        user.setFacebookAppScopeUserId(facebookUserDto.getFacebookAppScopeUserId());
        String longLifeAccessToken = extendFacebookToken(facebookUserDto.getFacebookAccessToken());
        user.setFacebookAccessToken(longLifeAccessToken);
        //override email
        user.setEmail(facebookUserDto.getEmail());//set to email from facebook
        // https://fbcdn-profile-a.akamaihd.net/hprofile-ak-xpa1/v/t1.0-1/12373424_10206718846264154_4821069385078728963_n.jpg?oh=78b83ffce2ffbed9b04f7ec5c29fe1f9&oe=57120B3A&__gda__=1459399243_ade019283f46e8c473183c246e12ec83

        try {
            UploadedFile profileImage = fileService.addUploadedFile(facebookUserDto.getProfileUrl());
            user.setProfileImage(profileImage);
        } catch (IOException e) {
            log.error(e);
        } catch (GeneralSecurityException e) {
            log.error(e);
        } catch (InterruptedException e) {
            log.error(e);
        }
    }
}

