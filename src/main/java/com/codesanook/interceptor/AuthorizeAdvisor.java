package com.codesanook.interceptor;

import com.codesanook.exception.UnauthorizedException;
import com.codesanook.model.RoleEnum;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.Method;

@Component
@Aspect
@Order(0)
public class AuthorizeAdvisor {

    static Log log = LogFactory.getLog(AuthorizeAdvisor.class);

    @Around("execution(@com.codesanook.interceptor.Authorize * *(..))")
    public Object time(ProceedingJoinPoint pjp) throws Throwable {

        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                    .currentRequestAttributes()).getRequest();
            int[] roles = getRequiredRoles(pjp);
            validateCookieToken(request, roles);

            Object result = pjp.proceed();
            log.debug("end AuthorizeAdvisor");
            return result;
        } catch (Exception ex) {
            log.error("error in interceptor", ex);
            throw ex;
        }

    }

    private int[] getRequiredRoles(ProceedingJoinPoint pjp) {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        Authorize authorize = method.getAnnotation(Authorize.class);


        RoleEnum[] roleEnums = authorize.roles();
        if (roleEnums == null) return new int[0];

        int[] roleIds = new int[roleEnums.length];
        for (int index = 0; index < roleIds.length; index++) {
            roleIds[index] = roleEnums[index].getId();
        }
        return roleIds;
    }


    private void validateCookieToken(HttpServletRequest request, int[] requiredRoles) throws IOException {
        //set from LoggedInUserFilter
        LoggedInUser loggedInUser = LoggedInUser.getLogginUser(request);
        if (loggedInUser == null) throw new UnauthorizedException();

        DateTime utcNow = DateTime.now(DateTimeZone.UTC);
        if (loggedInUser.getExpired().isBefore(utcNow)) throw new UnauthorizedException();

        //no required role
        if (requiredRoles == null || requiredRoles.length == 0) {
            return;
        }

        //there is role
        for (int userRole : loggedInUser.getRoles()) {
            for (int requiredRole : requiredRoles) {
                if (requiredRole == userRole) {
                    return;
                }
            }
        }

        throw new UnauthorizedException();
    }
}
