package com.jin.demo.myspring.jdbc;

import java.sql.SQLException;

/**事务管理器的实现
 * @author wangjin
 */
public class MyTransactionManager implements MyTransaction {
    /**
     * 获取当前线程的数据库链接
     */
    private MyConnectionUtil myConnectionUtil = MyConnectionUtil.getInstance();

    @Override
    public void begin() throws SQLException {
        myConnectionUtil.getCurrentThreadConn().setAutoCommit(false);
    }

    @Override
    public void commit() throws SQLException {
        myConnectionUtil.getCurrentThreadConn().commit();
    }

    @Override
    public void rollback() throws SQLException {
        myConnectionUtil.getCurrentThreadConn().rollback();
    }
}
