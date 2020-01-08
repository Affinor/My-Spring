package com.jin.demo.myspring.annotation;

import java.lang.annotation.*;

/**
 * 自动注入
 * @author wangjin
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyAutowired {
    String value() default "";
}
