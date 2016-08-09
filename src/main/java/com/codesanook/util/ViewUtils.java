package com.codesanook.util;

import com.codesanook.dto.UrlDto;
import com.codesanook.dto.comments.PartialComment;
import com.codesanook.dto.posts.PostDto;
import com.codesanook.dto.posts.PostSubtypeDto;
import com.codesanook.dto.users.UserDto;
import com.codesanook.dto.users.UserTimeZone;
import com.codesanook.interceptor.LoggedInUser;
import com.codesanook.model.PostSubtype;
import com.codesanook.model.PostTypeEnum;
import com.codesanook.model.RoleEnum;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Locale;
import java.util.TimeZone;

import static com.codesanook.model.PostTypeEnum.*;

@Component(value = "viewUtils")
public class ViewUtils {

    private Log log = LogFactory.getLog(ViewUtils.class);

    @Autowired
    private Environment env;

    //from
    //http://stackoverflow.com/questions/559155/how-do-i-get-a-httpservletrequest-in-my-spring-beans
//    @Autowired
//    private HttpServletRequest request;

    public String getPostClass(PostDto post) {
        PostTypeEnum postType = fromId(post.getPostType().getId());

        switch (postType) {

            case QUESTION:
                return "question";

            case KNOWLEDGE:
                return "knowledge";

            default:
                return "";
        }
    }

    private LoggedInUser getLoggedInUser() {
        HttpServletRequest request = getCurrentRequest();
        LoggedInUser loggedInUser = LoggedInUser.getLogginUser(request);
        return loggedInUser;
    }

    public boolean isAdmin() {
        LoggedInUser loggedInUser = getLoggedInUser();
        return isAdmin(loggedInUser);
    }


    public boolean isAdmin(LoggedInUser loggedInUser) {
        if (loggedInUser == null) return false;
        return loggedInUser.havingRole(RoleEnum.ADMIN);
    }


    public boolean isAuthor() {
        LoggedInUser loggedInUser = getLoggedInUser();
        return isAuthor(loggedInUser);
    }

    public boolean isAuthor(LoggedInUser loggedInUser) {
        if (loggedInUser == null) return false;
        return loggedInUser.havingRole(RoleEnum.AUTHOR);
    }

    public boolean isOwningResource(int userIdOfResource) {
        LoggedInUser loggedInUser = getLoggedInUser();
        return isOwningResource(loggedInUser, userIdOfResource);
    }


    public boolean isOwningResource(LoggedInUser loggedInUser, int userIdOfResource) {
        if (loggedInUser == null) return false;
        return loggedInUser.owningResource(userIdOfResource);
    }


    private HttpServletRequest getCurrentRequest() {
//        HttpServletRequest request =
//                ((ServletRequestAttributes) RequestContextHolder
//                        .getRequestAttributes()).getRequest();

        //from http://stackoverflow.com/questions/3320674/spring-how-do-i-inject-an-httpservletrequest-into-a-request-scoped-bean
        HttpServletRequest curRequest =
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                        .getRequest();
        return curRequest;
    }

    public int getUserTimeZone() {
        HttpServletRequest request = getCurrentRequest();
        Object timeZone = request.getSession().getAttribute(UserTimeZone.KEY);
        if (timeZone != null) return (int) timeZone;
        return -1;
    }

    public String formatDate(DateTime utcDateTime, String format) {
        if (utcDateTime == null) return "";

        HttpServletRequest request = getCurrentRequest();
        format = format != null ? format : "yyyy-MM-dd H:mm:ss Z";
        HttpSession httpSession = request.getSession();
        Object rawTimezone = httpSession.getAttribute(UserTimeZone.KEY);

        DateTimeZone toTimeZone;
        if (rawTimezone != null) {
            int userTimeZone = (int) httpSession.getAttribute("timeZoneMillisecond");
            toTimeZone = DateTimeZone.forOffsetMillis(userTimeZone);
            log.debug(String.format("from session toTimezone %s ,current date %s", toTimeZone.toString(),
                    utcDateTime.toString()));
        } else {
            TimeZone timezone = RequestContextUtils.getTimeZone(request);
            toTimeZone = DateTimeZone.forTimeZone(timezone);
        }


        DateTimeFormatter outputFormatter = DateTimeFormat.forPattern(format).withZone(toTimeZone);
        return outputFormatter.print(utcDateTime);
    }

    public String formatDate(DateTime utcDateTime) {
        return formatDate(utcDateTime, null);
    }

    public int getPostTypeId(PostDto postDto) {

        if (postDto.getId() == 0) {
            HttpServletRequest request = getCurrentRequest();
            return Integer.valueOf(request.getParameter("type"));
        } else {
            return postDto.getPostType().getId();
        }
    }

    public int getPostSubtypeId(PostDto postDto) {
        if (postDto.getId() == 0) {
            HttpServletRequest request = getCurrentRequest();
            String param = request.getParameter("sub-type");
            if (param != null) return Integer.valueOf(param);
            return 0;

        } else {
            PostSubtypeDto postSubtype = postDto.getPostSubtype();
            if (postSubtype != null) return postSubtype.getId();
            return 0;
        }
    }


    public String subStringIfLengthLongerThan(int maxStringLength, String htmlContent) {
        if (htmlContent.length() <= maxStringLength) return htmlContent;

        String contentWithoutHtmlTag = Jsoup.parse(htmlContent).text();

        int max = Math.min(contentWithoutHtmlTag.length(), maxStringLength);
        contentWithoutHtmlTag = contentWithoutHtmlTag.substring(0, max);
        return contentWithoutHtmlTag;
    }


    public PartialComment getPartialComment(int maxStringLength, String htmlContent) {
        PartialComment partialComment = new PartialComment();
        partialComment.setIsSubstring(false);

        if (htmlContent.length() <= maxStringLength) {
            partialComment.setContent(htmlContent);
            return partialComment;
        }

        partialComment.setContent(subStringIfLengthLongerThan(maxStringLength, htmlContent));
        partialComment.setIsSubstring(true);
        return partialComment;
    }


    public String getFeaturedImageUrl(PostDto postDto) {

        if (postDto.getFeaturedImage() != null) return postDto.getFeaturedImage().getAbsoluteUrl();

        int postTypeId = postDto.getPostType().getId();
        PostTypeEnum postTypeEnum = fromId(postTypeId);
        switch (postTypeEnum) {
            case QUESTION:
                return "/img/featured-image-question.jpg";
            case KNOWLEDGE:
                return "/img/featured-image-knowledge.jpg";
        }
        throw new IllegalStateException(String.format("no post type for post id %d", postDto.getId()));
    }

    public UrlDto getPostDetailsUrl(PostDto post) {
        String domain = env.getProperty("domain");
        String relativeUrl = String.format("/post/details/%s/%d", post.getAlias(), post.getId());
        UrlDto url = new UrlDto(domain, relativeUrl);
        return url;
    }


    public UrlDto getUserProfileUrl(UserDto userDto) {
        String domain = env.getProperty("domain");
        String relativeUrl = String.format("/user/profile/%d", userDto.getId());
        UrlDto url = new UrlDto(domain, relativeUrl);
        return url;
    }


    public String getCurrentLanguage() {
        Locale local = LocaleContextHolder.getLocale();
        String language = local.getLanguage();
        return language;
    }


}
