package com.jin.demo.myspring.aop.aspect;

import com.jin.demo.myspring.aop.MyAdvice;
import com.jin.demo.myspring.aop.MyJoinPoint;

import java.lang.reflect.Method;

/**
 * 横切增强处理类
 * @author wangjin
 */
public class MyAbstractAspectAdvice implements MyAdvice {
    /**
     * 方法增强
     */
    private Method aspectMethod;
    /**
     * 类进行增强
     */
    private Object aspectTarget;

    public MyAbstractAspectAdvice(Method aspectMethod, Object aspectTarget) {
        this.aspectMethod = aspectMethod;
        this.aspectTarget = aspectTarget;
    }

    /**
     * 方法增强的实现
     * @param joinPoint
     * @param returnValue
     * @param tx
     * @return
     * @throws Throwable
     */
    public Object invokeAdviceMethod(MyJoinPoint joinPoint, Object returnValue, Throwable tx) throws Throwable{
        Class<?> [] paramTypes = this.aspectMethod.getParameterTypes();
        //如果没有参数，就直接增强调用
        if(null == paramTypes || paramTypes.length == 0){
            return this.aspectMethod.invoke(aspectTarget);
        }
        //如果有参数，先对参数进行增强替换，然后增强调用
        Object [] args = new Object[paramTypes.length];
        for (int i = 0; i < paramTypes.length; i ++) {
            if(paramTypes[i] == MyJoinPoint.class){
                args[i] = joinPoint;
            }else if(paramTypes[i] == Throwable.class){
                args[i] = tx;
            }else if(paramTypes[i] == Object.class){
                args[i] = returnValue;
            }
        }
        return this.aspectMethod.invoke(aspectTarget,args);
    }
}
