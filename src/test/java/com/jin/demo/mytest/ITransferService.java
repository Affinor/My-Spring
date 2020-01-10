package com.jin.demo.mytest;

import com.jin.demo.myspring.annotation.MyComponent;

@MyComponent
public interface ITransferService {
    String transfer(Bank userOut,Bank userIn);
}
