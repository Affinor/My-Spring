package com.jin.demo.myspring.aop;

import com.jin.demo.myspring.aop.intercept.MyMethodInvocation;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

/**基于jdk的动态代理实现
 * @author wangjin
 */
public class MyJdkDynamicAopProxy implements MyAopProxy, InvocationHandler {
    private MyAdvisedSupport advised;

    public MyJdkDynamicAopProxy(MyAdvisedSupport config){
        this.advised = config;
    }
    @Override
    public Object getProxy() {
        return getProxy(this.advised.getTargetClass().getClassLoader());
    }

    @Override
    public Object getProxy(ClassLoader classLoader) {
        return Proxy.newProxyInstance(classLoader,this.advised.getTargetClass().getInterfaces(),this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        List<Object> interceptorsAndDynamicMethodMatchers = this.advised.getInterceptorsAndDynamicInterceptionAdvice(method,this.advised.getTargetClass());
        MyMethodInvocation invocation = new MyMethodInvocation(method,this.advised.getTarget(),args,interceptorsAndDynamicMethodMatchers);
        return invocation.proceed();
    }
}
