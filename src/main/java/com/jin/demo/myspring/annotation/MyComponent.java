package com.jin.demo.myspring.annotation;

import java.lang.annotation.*;

/**组件扫描注入
 * @author wangjin
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyComponent {
    String value() default "";
}
