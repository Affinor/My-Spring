package com.jin.demo.myspring.annotation;

import java.lang.annotation.*;

/**注解注入
 * @author wangjin
 */
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyBean {
    /**
     * bean的name，可以自定义
     * @return
     */
    String[] value() default "";
}
