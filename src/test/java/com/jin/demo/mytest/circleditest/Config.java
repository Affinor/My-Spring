package com.jin.demo.mytest.circleditest;

import com.jin.demo.myspring.annotation.MyBean;
import com.jin.demo.myspring.annotation.MyConfiguration;

@MyConfiguration
public class Config {
    @MyBean(value = "xxx")
    public A a(){
        return new A();
    }
}
