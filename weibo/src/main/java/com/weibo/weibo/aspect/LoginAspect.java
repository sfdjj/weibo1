package com.weibo.weibo.aspect;

import com.weibo.weibo.model.HostHolder;
import com.weibo.weibo.util.WeiboUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.reflect.Method;

/**
 * Created by jwc on 2017/8/1.
 */


@Aspect
@Component
public class LoginAspect {

    private static Logger logger = LoggerFactory.getLogger(LoginAspect.class);

    @Autowired
    HostHolder hostHolder;

    @Before("execution(* com.weibo.weibo.Controller.*Controller.*(..))")
    public void before(JoinPoint joinPoint) {
        StringBuilder stringBuilder = new StringBuilder();
        for(Object object:joinPoint.getArgs()) {
            stringBuilder.append(object.toString());
            stringBuilder.append("|");
        }
        logger.info(stringBuilder.toString());
    }

    @After("execution(* com.weibo.weibo.Controller.*Controller.*(..))")
    public void after(JoinPoint joinPoint) {

    }

    @Around("execution(* com.weibo.weibo.Controller.*Controller.*(..))")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable{
        Class<?> clazz = proceedingJoinPoint.getTarget().getClass();
        Class<?> []par = ((MethodSignature)proceedingJoinPoint.getSignature()).getParameterTypes();
        Method method = clazz.getMethod(proceedingJoinPoint.getSignature().getName(),par);
        LoginRequired required = method.getAnnotation(LoginRequired.class);
        if(required!=null&&required.loginRequired()&&hostHolder.getUser()==null) {
            ResponseBody responseBody = method.getAnnotation(ResponseBody.class);
            if(responseBody==null) {
                return "redirect:/loginPage";
            }else {
                return WeiboUtil.getJSONString(1,"你还没有登陆");
            }
        }
        return proceedingJoinPoint.proceed();
    }
}
