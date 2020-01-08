package com.jin.demo.myspring.beans;

import lombok.Data;

/**对所有bean的包装
 * @author wangjin
 */
@Data
public class MyBeanWrapper {
    /**
     * bean的实例
     */
    private Object wrappedInstance;
    /**
     * bean的代理对象
     */
    private Object proxy;
    /**
     * bean的类型
     */
    private Class<?> wrappedClass;
    /**
     * bean的状态（默认为0，未实例化）
     * 0：还未实例化（还不能使用）
     * 1：刚实例化完成（还不能使用）
     * 2：初始化中（还不能使用）
     * 3：初始化完成（可以使用）
     */
    private String status = "0";
    /**
     * 构造传入
     * @param wrappedInstance
     */
    public MyBeanWrapper(Object wrappedInstance,Object proxy){
        this.wrappedInstance = wrappedInstance;
        this.wrappedClass = wrappedInstance.getClass();
        this.proxy = proxy;
    }

}
