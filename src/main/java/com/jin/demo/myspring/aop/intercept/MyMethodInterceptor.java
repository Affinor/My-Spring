package com.jin.demo.myspring.aop.intercept;

/**方法拦截器接口
 * @author wangjin
 */
public interface MyMethodInterceptor {
    /**
     * 执行拦截方法
     * @param invocation
     * @return
     * @throws Throwable
     */
    Object invoke(MyMethodInvocation invocation) throws Throwable;
}
