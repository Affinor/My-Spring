package com.jin.demo.myspring.aop.config;

import lombok.Data;

/**AOP配置
 * @author wangjin
 */
@Data
public class MyAopConfig {
    /**
     * 横切点
     */
    private String pointCut;
    /**
     * 前置通知
     */
    private String aspectBefore;
    /**
     * 后置通知
     */
    private String aspectAfter;
    /**
     * 横切类
     */
    private String aspectClass;
    /**
     * 异常通知
     */
    private String aspectAfterThrow;
    /**
     * 哪种异常通知
     */
    private String aspectAfterThrowingName;
}
