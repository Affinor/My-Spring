package com.jin.demo.myspring.aop;

/**AOP代理接口
 * @author wangjin
 */
public interface MyAopProxy {
    /**
     * 获取代理对象
     * @return
     */
    Object getProxy();

    /**
     * 根据类加载器获取代理对象
     * @param classLoader
     * @return
     */
    Object getProxy(ClassLoader classLoader);
}
