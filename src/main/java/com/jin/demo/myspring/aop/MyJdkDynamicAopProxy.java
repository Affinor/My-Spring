package com.jin.demo.myspring.aop;

import com.jin.demo.myspring.annotation.MyTransactional;
import com.jin.demo.myspring.aop.intercept.MyMethodInvocation;
import com.jin.demo.myspring.jdbc.MyTransaction;
import com.jin.demo.myspring.jdbc.MyTransactionManager;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.stream.Stream;

/**基于jdk的动态代理实现
 * @author wangjin
 */
@Slf4j
public class MyJdkDynamicAopProxy implements MyAopProxy, InvocationHandler {
    private MyAdvisedSupport advised;
    /**
     * 事务管理器
     */
    private MyTransaction myTransaction = new MyTransactionManager();
    public MyJdkDynamicAopProxy(MyAdvisedSupport config){
        this.advised = config;
    }
    @Override
    public Object getProxy() {
        return getProxy(this.advised.getTargetClass().getClassLoader());
    }

    @Override
    public Object getProxy(ClassLoader classLoader) {
        return Proxy.newProxyInstance(classLoader, this.advised.getTargetClass().getInterfaces(), new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                log.info("create jdk proxy start...");
                //获取参数类型列表
                Class<?>[] classes = new Class<?>[args.length];
                for (int i = 0; i < args.length; i++) {
                    classes[i] = args[i].getClass();
                }
                //判断目标代理接口的实现类的方法是否有MyTransactional注解
                boolean annotation = advised.getTargetClass().getDeclaredMethod(method.getName(), classes).isAnnotationPresent(MyTransactional.class);
                //如果没有MyTransactional注解，直接返回
                if (!annotation){
                    return method.invoke(advised.getTarget(),args);
                }
                //开始执行事务管理
                Object result = null;
                try {
                    //开启事务
                    myTransaction.begin();
                    //执行方法调用
                    result = method.invoke(advised.getTarget(),args);
                    //提交事务
                    myTransaction.commit();
                }catch (Throwable e){
                    //异常回滚
                    myTransaction.rollback();
                    log.error("this transaction rollback, because execute jdk proxy Exception invoke method:{}#{}",advised.getTarget().getClass().getName(),method.getName(),e);
                    throw e;
                }
                //返回结果
                return result;
            }
        });
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        List<Object> interceptorsAndDynamicMethodMatchers = this.advised.getInterceptorsAndDynamicInterceptionAdvice(method,this.advised.getTargetClass());
        MyMethodInvocation invocation = new MyMethodInvocation(method,this.advised.getTarget(),args,interceptorsAndDynamicMethodMatchers);
        return invocation.proceed();
    }
}
