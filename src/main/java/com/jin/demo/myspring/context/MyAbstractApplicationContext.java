package com.jin.demo.myspring.context;

/**IOC容器实现的顶层设计
 * @author wangjin
 */
public abstract class MyAbstractApplicationContext {
    /**
     * 只提供给子类重写
     */
    protected void refresh() throws Exception {};
}
