package com.jin.demo.myspring.jdbc;

import java.sql.SQLException;

/**事务控制接口
 * @author wangjin
 */
public interface MyTransaction {
    /**
     * 开启事务
     */
    void begin() throws SQLException;
    /**
     * 提交事务
     */
    void commit() throws SQLException;
    /**
     * 回滚事务
     */
    void rollback() throws SQLException;
}
