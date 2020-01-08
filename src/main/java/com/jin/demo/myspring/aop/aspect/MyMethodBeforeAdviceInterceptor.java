package com.jin.demo.myspring.aop.aspect;

import com.jin.demo.myspring.aop.MyAdvice;
import com.jin.demo.myspring.aop.MyJoinPoint;
import com.jin.demo.myspring.aop.intercept.MyMethodInterceptor;
import com.jin.demo.myspring.aop.intercept.MyMethodInvocation;

import java.lang.reflect.Method;

/**前置通知
 * @author wangjin
 */
public class MyMethodBeforeAdviceInterceptor extends MyAbstractAspectAdvice implements MyAdvice, MyMethodInterceptor {
    private MyJoinPoint myJoinPoint;
    public MyMethodBeforeAdviceInterceptor(Method aspectMethod, Object aspectTarget) {
        super(aspectMethod, aspectTarget);
    }

    /**
     * 方法调用
     * @param invocation
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(MyMethodInvocation invocation) throws Throwable {
        //从被织入的代码中才能拿到，JoinPoint
        this.myJoinPoint = invocation;
        before();
        return invocation.proceed();
    }

    /**
     * 方法调用之前的增强
     * @throws Throwable
     */
    private void before() throws Throwable {
        super.invokeAdviceMethod(this.myJoinPoint,null,null);
    }
}
