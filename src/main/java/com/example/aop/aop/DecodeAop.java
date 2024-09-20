package com.example.aop.aop;

import com.example.aop.dto.User;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

public class DecodeAop {
    @Pointcut("execution(* com.example.aop.controller..*.*(..))")
    private void cut(){}

    @Pointcut("@annotation(com.example.aop.annotation.Decode)")
    private void enableDecode(){}

    @Before("cut() && enableDecode()")
    public void before(JoinPoint joinPoint) throws UnsupportedEncodingException {
        Object[] args = joinPoint.getArgs();

        for(Object arg : args){
            if(arg instanceof User){
                User user = User.class.cast(arg);
                String base64Email = user.getEmail();
                String email = new String(Base64.getDecoder().decode(base64Email), "UTF-8");

                user.setEmail(email);
            }
        }
    }

    @AfterReturning(value = "cut() && enableDecode()", returning = "returnObj")
    public void afterReturn(JoinPoint joinPoint, Object returnObj) throws UnsupportedEncodingException {
        if(returnObj instanceof User){
            User user = User.class.cast(returnObj);
            String base64Email = user.getEmail();
            String email = new String(Base64.getDecoder().decode(base64Email), "UTF-8");

            user.setEmail(email);
        }
    }
}
