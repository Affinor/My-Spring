package com.jin.demo.myspring.aop;

import com.jin.demo.myspring.annotation.MyTransactional;
import com.jin.demo.myspring.jdbc.MyTransaction;
import com.jin.demo.myspring.jdbc.MyTransactionManager;
import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**基于Cglib的动态代理
 * @author wangjin
 */
@Slf4j
public class MyCglibAopProxy implements MyAopProxy {
    /**
     * 增强处理类
     */
    private MyAdvisedSupport advised;
    /**
     * 事务管理器
     */
    private MyTransaction myTransaction = new MyTransactionManager();

    public MyCglibAopProxy(MyAdvisedSupport myAdvisedSupport) {
        this.advised = myAdvisedSupport;
    }

    @Override
    public Object getProxy() {
        return getProxy(null);
    }

    @Override
    public Object getProxy(ClassLoader classLoader) {
        return Enhancer.create(advised.getTarget().getClass(), new InnerMethodInterceptor());
    }
    class InnerMethodInterceptor implements MethodInterceptor {
        @Override
        public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
            log.info("create cglib proxy start...");
            boolean annotation = method.isAnnotationPresent(MyTransactional.class);
            //如果没有MyTransactional注解，直接返回
            if (!annotation){
                return method.invoke(advised.getTarget(),objects);
            }
            //开始执行事务管理
            Object result = null;
            try {
                //开启事务
                myTransaction.begin();
                //执行方法调用
                result = method.invoke(advised.getTarget(),objects);
                //提交事务
                myTransaction.commit();
            }catch (Throwable e){
                //异常回滚
                myTransaction.rollback();
                log.error("this transaction rollback, because execute cglib proxy Exception invoke method:{}#{}",advised.getTarget().getClass().getName(),method.getName(),e);
                throw e;
            }
            //返回结果
            return result;
        }
    }
}
