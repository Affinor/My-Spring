package com.jin.demo.myspring.annotation;

import java.lang.annotation.*;

/**
 * 自动注入,数据访问层
 * @author wangjin
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyRepository {
	String value() default "";
}
