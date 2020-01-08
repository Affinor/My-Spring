package com.jin.demo.myspring.beans.config;

/**bean初始化之前/后，可以进行特殊处理
 * @author wangjin
 */
public class MyBeanPostProcessor {
    /**
     * bean初始化之前处理
     * @param bean
     * @param beanName
     * @return
     * @throws Exception
     */
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws Exception {
        return bean;
    }

    /**
     * bean初始化之后处理
     * @param bean
     * @param beanName
     * @return
     * @throws Exception
     */
    public Object postProcessAfterInitialization(Object bean, String beanName) throws Exception {
        return bean;
    }
}
