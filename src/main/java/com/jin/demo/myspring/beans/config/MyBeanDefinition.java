package com.jin.demo.myspring.beans.config;

import lombok.Data;

/**对所有bean的定义
 * @author wangjin
 */
@Data
public class MyBeanDefinition {
    /**
     * bean的全路径类名
     */
    private String beanClassName;
    /**
     * 是否懒加载（默认非懒加载）
     */
    private boolean lazyInit = false;
    /**
     * bean的名称（name/id）
     */
    private String factoryBeanName;
    /**
     * 是否为单例（默认为单例）
     */
    private boolean isSingleton = true;

}
