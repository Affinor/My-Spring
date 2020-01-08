package com.jin.demo.mytest;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Bank {
    private Integer id;
    private Integer money;
    private String username;
}
