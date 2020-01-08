package com.jin.demo.myspring.annotation;

import java.lang.annotation.*;

/**
 * 自动注入,事务控制
 * @author wangjin
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyTransactional {
    String value() default "";
}
