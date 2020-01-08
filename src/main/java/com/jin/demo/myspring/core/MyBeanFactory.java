package com.jin.demo.myspring.core;

/**
 * 单例工厂的顶层设计
 * @author wangjin
 */
public interface MyBeanFactory {
    /**
     * 根据beanName(id)从IOC容器中获得一个实例Bean
     * @param beanName
     * @return
     */
    Object getBean(String beanName) throws Exception;
    /**
     * 根据beanClass(类型)从IOC容器中获得一个实例Bean
     * @param beanClass
     * @return
     */
    Object getBean(Class<?> beanClass) throws Exception;
}
