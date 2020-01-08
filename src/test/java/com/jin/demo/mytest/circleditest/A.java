package com.jin.demo.mytest.circleditest;

import com.jin.demo.myspring.annotation.MyAutowired;
import com.jin.demo.myspring.annotation.MyService;

@MyService
public class A {
    @MyAutowired
    private B b;
}
