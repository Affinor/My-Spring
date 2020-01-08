package com.jin.demo.myspring.annotation;

import java.lang.annotation.*;

/**
 * 注解注入配置
 * @author wangjin
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyConfiguration {
    String value() default "";
}
