package com.jin.demo.myspring.aop.aspect;

import com.jin.demo.myspring.aop.MyAdvice;
import com.jin.demo.myspring.aop.MyJoinPoint;
import com.jin.demo.myspring.aop.intercept.MyMethodInterceptor;
import com.jin.demo.myspring.aop.intercept.MyMethodInvocation;

import java.lang.reflect.Method;

/**后置增强
 * @author wangjin
 */
public class MyAfterReturningAdviceInterceptor extends MyAbstractAspectAdvice implements MyAdvice, MyMethodInterceptor {
    /**
     * 增强切点
     */
    private MyJoinPoint myJoinPoint;

    public MyAfterReturningAdviceInterceptor(Method aspectMethod, Object aspectTarget) {
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
        Object proceed = invocation.proceed();
        this.myJoinPoint = invocation;
        this.afterReturning(proceed);
        return proceed;
    }

    /**
     * 真正的后置处理
     * @param proceed
     * @throws Throwable
     */
    private void afterReturning(Object proceed) throws Throwable {
        super.invokeAdviceMethod(this.myJoinPoint,proceed,null);
    }
}
