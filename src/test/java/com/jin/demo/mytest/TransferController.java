package com.jin.demo.mytest;

import com.jin.demo.myspring.annotation.MyAutowired;
import com.jin.demo.myspring.annotation.MyController;

@MyController
public class TransferController {
    @MyAutowired
    private TransferService transferService;
    public String transfer(Bank userOut,Bank userIn){
        return transferService.transfer(userOut,userIn);
    }
}
