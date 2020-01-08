package com.jin.demo.myspring.aop;


import java.lang.reflect.Method;

/**切入点接口
 * @author wangjin
 */
public interface MyJoinPoint {
    /**
     *获取代理对象
     */
    Object getThis();
    /**
     *获取参数列表
     */
    Object[] getArguments();
    /**
     *获取方法
     */
    Method getMethod();
    /**
     *设置属性
     */
    void setUserAttribute(String key, Object value);
    /**
     *获取属性
     */
    Object getUserAttribute(String key);
}
