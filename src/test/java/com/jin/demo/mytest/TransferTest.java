package com.jin.demo.mytest;

import com.jin.demo.myspring.context.MyApplicationContext;

/**
 * 转账事务测试
 */
public class TransferTest {
    public static void main(String[] args) {
        try {
            MyApplicationContext myApplicationContext = new MyApplicationContext(new String[]{"applicationContext.properties"});
            TransferController transferController = (TransferController)myApplicationContext.getBean("transferController");
            Integer money = 100;
            Bank bankOut = new Bank();
            bankOut.setUsername("jack");
            bankOut.setMoney(money);
            Bank bankIn = new Bank();
            bankIn.setUsername("rose");
            bankIn.setMoney(money);
            transferController.transfer(bankOut,bankIn);
            System.out.println(transferController);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
