package com.jin.demo.myspring.aop.aspect;

import com.jin.demo.myspring.aop.MyAdvice;
import com.jin.demo.myspring.aop.intercept.MyMethodInterceptor;
import com.jin.demo.myspring.aop.intercept.MyMethodInvocation;

import java.lang.reflect.Method;

/**异常增强
 * @author wangjin
 */
public class MyAfterThrowingAdviceInterceptor extends MyAbstractAspectAdvice implements MyAdvice, MyMethodInterceptor {

    private String throwingName;

    public MyAfterThrowingAdviceInterceptor(Method aspectMethod, Object aspectTarget) {
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
        try {
            return invocation.proceed();
        }catch (Throwable e){
            //发生异常时开始处理
            super.invokeAdviceMethod(invocation,null,e.getCause());
            throw e;
        }
    }
    public void setThrowName(String throwName){
        this.throwingName = throwName;
    }
}
