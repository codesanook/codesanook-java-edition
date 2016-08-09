package com.codesanook.interceptor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.context.internal.ManagedSessionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

@Component
@Aspect
@Order(1)
public class HibernateTransactionalAdvisor {

    static Log log = LogFactory.getLog(HibernateTransactionalAdvisor.class);

//    @Autowired
//    SessionFactory sessionFactory;

    @Around("execution(@com.codesanook.interceptor.HibernateTransactional * *(..))")
    public Object executeInTransaction(ProceedingJoinPoint pjp) throws Throwable {

//        MethodSignature signature = (MethodSignature) pjp.getSignature();
//        Method method = signature.getMethod();
//
//        HibernateTransactional myAnnotation = method.getAnnotation(HibernateTransactional.class);
//        log.info(String.format("annotation value %s", myAnnotation.value()));
//
//        Session session;
//        session = sessionFactory.openSession();
//
//        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
//                .currentRequestAttributes()).getRequest();
//       request.setAttribute("currentSession",session);
//        try {
//
//            session.getTransaction().begin();
//
//            log.info("begin HibernateTransactionalAdvisor");
            Object result = pjp.proceed();
//            log.info("end HibernateTransactionalAdvisor");
//
//
//            session.getTransaction().commit();
            return result;
//        } catch (Exception ex) {
//            if (session.getTransaction().isActive()) {
//                session.getTransaction().rollback();
//            }
//            session.close();
//
//            log.error("this error in intercept", ex);
//            throw ex;
        }

    }
