package com.jin.demo.mytest.circleditest;

import com.jin.demo.myspring.context.MyApplicationContext;

/**
 * 循环依赖测试
 */
public class CircleDiTest {
    public static void main(String[] args) {
        MyApplicationContext myApplicationContext = new MyApplicationContext(new String[]{"applicationContext.properties"});
        try {
            //自定义beanId取值
            A a = (A) myApplicationContext.getBean("xxx");
            B b = (B) myApplicationContext.getBean("b");
            C c = (C) myApplicationContext.getBean("c");
            D d = (D) myApplicationContext.getBean("d");
            System.out.println(a);
            System.out.println(b);
            System.out.println(c);
            System.out.println(d);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
