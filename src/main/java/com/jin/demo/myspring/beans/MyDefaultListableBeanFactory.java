package com.jin.demo.myspring.beans;

import com.jin.demo.myspring.beans.config.MyBeanDefinition;
import com.jin.demo.myspring.context.MyAbstractApplicationContext;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wangjin
 */
public class MyDefaultListableBeanFactory extends MyAbstractApplicationContext {
    /**
     * 存储注册信息的BeanDefinition,注意这个不是真正的IOC容器
     */
    protected final Map<String, MyBeanDefinition> beanDefinitionMap = new ConcurrentHashMap<String, MyBeanDefinition>();

    /**
     * 根据全类名，取beanName
     * @param beanType
     * @return
     */
    public Set<String> getBeanNameByType(String beanType){
        Set<String> beanNames = new HashSet<>();
        for (Map.Entry<String, MyBeanDefinition> beanDefinitionEntry : this.beanDefinitionMap.entrySet()) {
            if (beanType.equals(beanDefinitionEntry.getValue().getBeanClassName())){
                beanNames.add(beanDefinitionEntry.getKey());
            }
        }
        return beanNames;
    }
}
