package com.jin.demo.mytest;

import com.jin.demo.myspring.annotation.MyRepository;
import com.jin.demo.myspring.annotation.MyService;
import com.jin.demo.myspring.jdbc.MyConnectionUtil;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@MyRepository
public class TransferDao {

    private MyConnectionUtil myConnectionUtil = MyConnectionUtil.getInstance();
    /**
     * 转出
     * @return
     */
    public void transferOut(Bank userOut){
        String sql = "update bank set money = money-? where username = ?";
        trans(sql,userOut);
    }
    /**
     * 转入
     * @return
     */
    public void transferIn(Bank userIn){
        String sql = "update bank set money = money+? where username = ?";
        trans(sql,userIn);
    }
    public void trans(String sql,Bank bank){
        Connection conn;
        PreparedStatement ps;
        Integer rs;
        try {
            conn = myConnectionUtil.getCurrentThreadConn();
            ps = conn.prepareStatement(sql);
            ps.setInt(1,bank.getMoney());
            ps.setString(2,bank.getUsername());
            rs = ps.executeUpdate();
            if (rs>0){
                log.info("transfer success:{}",bank);
            }
        }catch (Exception e){
            log.error("transfer error:{}",e.getMessage(),e);
        }
    }
    public List<Bank> queryBank(){
        Connection conn;
        PreparedStatement ps;
        ResultSet rs;
        List<Bank> list = new ArrayList<Bank>();
        try {
            conn = myConnectionUtil.getCurrentThreadConn();
            ps = conn.prepareStatement("select * from bank");
            rs = ps.executeQuery();
            //遍历返回结果集
            while (rs.next()){
                Integer id = rs.getInt("id");
                Integer money = rs.getInt("money");
                String username = rs.getString("username");
                Bank bank = new Bank();
                bank.setId(id);
                bank.setMoney(money);
                bank.setUsername(username);
                list.add(bank);
            }
        }catch (Exception e){
            log.error("transfer error:{}",e.getMessage(),e);
        }
        return list;
    }
}
