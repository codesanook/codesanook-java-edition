package com.codesanook.interceptor;

import com.codesanook.dto.users.LoggedInUserDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tomcat.util.codec.binary.Base64;
import org.jasypt.util.text.StrongTextEncryptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class LoggedInUserRequestHandler extends HandlerInterceptorAdapter {

    private Log log = LogFactory.getLog(LoggedInUserRequestHandler.class);

    @Value("${encryption-password}")
    private String ENCRYPTION_PASSWORD;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {
        if (StringUtils.isEmpty(ENCRYPTION_PASSWORD))
            throw new IllegalStateException("Please set encryption-password value in application.properties file");

        LoggedInUser loggedInUser = getLoggedInUser(request, response);
        request.setAttribute(LoggedInUser.LOGGED_IN_USER_KEY, loggedInUser);

        LoggedInUserDto loggedInUserDto = getLoggedInUserDto(loggedInUser);
        request.setAttribute("loggedInUserDto", loggedInUserDto);

        int userId = 0;
        if (loggedInUser != null) userId = loggedInUser.getId();
        request.setAttribute("userId", userId);

        return true;
    }

    private LoggedInUser getLoggedInUser(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return null;

        Map cookieMap = new HashMap();
        for (Cookie cookie : cookies) {
            cookieMap.put(cookie.getName(), cookie);
        }

        String token = null;
        if (cookieMap.get(LoggedInUser.TOKEN_KEY) != null) {
            Cookie userTokenCookie = (Cookie) cookieMap.get(LoggedInUser.TOKEN_KEY);
            token = userTokenCookie.getValue();
        }

        if (token == null) return null;

        LoggedInUser loggedInUser = null;
        try {
            log.debug(String.format("raw token %s", token));
            String stringDecoded = URLDecoder.decode(token, "UTF-8");
            StrongTextEncryptor textEncryptor = new StrongTextEncryptor();
            textEncryptor.setPassword(ENCRYPTION_PASSWORD);
            String plainText = textEncryptor.decrypt(stringDecoded);

            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JodaModule());

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
            mapper.setDateFormat(dateFormat);
            loggedInUser = mapper.readValue(plainText, LoggedInUser.class);

        } catch (Exception ex) {
            log.error(ex);
            deleteCookie(response);
        }

        return loggedInUser;
    }

    private void deleteCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie(LoggedInUser.TOKEN_KEY, "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    private void decodeBase64(String token) {
        // Decode data on other side, by processing encoded data
        byte[] bytesEncoded = token.getBytes();
        byte[] valueDecoded = Base64.decodeBase64(bytesEncoded);
        String stringDecoded = new String(valueDecoded);
        log.debug("Decoded value is " + stringDecoded);

    }


    private LoggedInUserDto getLoggedInUserDto(LoggedInUser loggedInUser) {

        LoggedInUserDto loggedInUserDto = null;
        if (loggedInUser != null) {
            loggedInUserDto = new LoggedInUserDto();
            loggedInUserDto.setId(loggedInUser.getId());
            loggedInUserDto.setName(loggedInUser.getName());
            loggedInUserDto.setProfileUrl(loggedInUser.getProfileImageUrl());
        }
        return loggedInUserDto;
    }


}
