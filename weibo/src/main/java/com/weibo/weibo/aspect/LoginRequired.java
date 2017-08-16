package com.weibo.weibo.aspect;

import java.lang.annotation.*;

/**
 * Created by jwc on 2017/8/1.
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LoginRequired {
    boolean loginRequired()default false;
}