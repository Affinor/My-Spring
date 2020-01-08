package com.jin.demo.myspring.aop.intercept;

import com.jin.demo.myspring.aop.MyJoinPoint;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**拦截器
 * @author wangjin
 */
public class MyMethodInvocation implements MyJoinPoint {

    private Method method;
    private Object target;
    private Object [] arguments;
    private List<Object> interceptorsAndDynamicMethodMatchers;

    private Map<String, Object> userAttributes;

    /**
     * 定义一个索引，从-1开始来记录当前拦截器执行的位置
     */
    private int currentInterceptorIndex = -1;

    public MyMethodInvocation(Method method, Object target, Object[] arguments, List<Object> interceptorsAndDynamicMethodMatchers) {
        this.method = method;
        this.target = target;
        this.arguments = arguments;
        this.interceptorsAndDynamicMethodMatchers = interceptorsAndDynamicMethodMatchers;
    }
    public Object proceed() throws Throwable {
        //如果Interceptor执行完了，则执行joinPoint
        if (this.currentInterceptorIndex == this.interceptorsAndDynamicMethodMatchers.size() - 1) {
            return this.method.invoke(this.target,this.arguments);
        }
        //执行下一个Interceptor
        Object interceptorOrInterceptionAdvice = this.interceptorsAndDynamicMethodMatchers.get(++this.currentInterceptorIndex);
        //动态匹配joinPoint
        if (interceptorOrInterceptionAdvice instanceof MyMethodInterceptor) {
            MyMethodInterceptor mi = (MyMethodInterceptor) interceptorOrInterceptionAdvice;
            return mi.invoke(this);
        } else {
            //如果没有匹配上,就跳过当前Intercetpor,调用下一个Interceptor
            return proceed();
        }
    }
    @Override
    public Object getThis() {
        return this.target;
    }

    @Override
    public Object[] getArguments() {
        return this.arguments;
    }

    @Override
    public Method getMethod() {
        return this.method;
    }

    @Override
    public void setUserAttribute(String key, Object value) {
        if (value != null) {
            if (this.userAttributes == null) {
                this.userAttributes = new HashMap<String,Object>();
            }
            this.userAttributes.put(key, value);
        } else {
            if (this.userAttributes != null) {
                this.userAttributes.remove(key);
            }
        }
    }

    @Override
    public Object getUserAttribute(String key) {
        if (this.userAttributes != null){
            return this.userAttributes.get(key);
        }
        return null;
    }
}
