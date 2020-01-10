package com.jin.demo.mytest;

import com.jin.demo.myspring.annotation.MyAutowired;
import com.jin.demo.myspring.annotation.MyService;
import com.jin.demo.myspring.annotation.MyTransactional;

@MyService
public class TransferService implements ITransferService{

    @MyAutowired
    private TransferDao transferDao;

    @Override
    @MyTransactional
    public String transfer(Bank userOut,Bank userIn){
        transferDao.transferOut(userOut);
        int i=0;
        System.out.println(10/i);
        transferDao.transferIn(userIn);
        transferDao.queryBank().forEach(System.out::println);
        return "success";
    }
}
