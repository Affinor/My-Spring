package com.jin.demo.myspring.annotation;

import java.lang.annotation.*;

/**
 * 自动注入,Controller层
 * @author wangjin
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyController {
    String value() default "";
}
