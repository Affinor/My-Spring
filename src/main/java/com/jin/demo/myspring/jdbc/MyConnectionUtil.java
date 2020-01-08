package com.jin.demo.myspring.jdbc;

import com.alibaba.druid.pool.DruidDataSource;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * 链接数据库的工具，简单实现，后续有时间再进行改造
 */
@Slf4j
public class MyConnectionUtil {

    private static DataSource dataSource;
    static {
        InputStream resourceAsSteam = MyResources.getResourceAsSteam("jdbc.properties");
        Properties properties = new Properties();
        try {
            properties.load(resourceAsSteam);
        } catch (IOException e) {
            log.error("load jdbc.properties error:{}",e.getMessage(),e);
        }
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setDriverClassName(properties.getProperty("jdbc.driver"));
        druidDataSource.setUrl(properties.getProperty("jdbc.url"));
        druidDataSource.setUsername(properties.getProperty("jdbc.username"));
        druidDataSource.setPassword(properties.getProperty("jdbc.password"));
        dataSource = druidDataSource;
    }
    /**
     * 存储当前线程的连接
     */
    private static ThreadLocal<Connection> threadLocal = new ThreadLocal<Connection>();

    /**
     * 从当前线程获取连接
     */
    public Connection getCurrentThreadConn() throws SQLException {
        /**
         * 判断当前线程中是否已经绑定连接，如果没有绑定，需要从连接池获取一个连接绑定到当前线程
         */
        Connection connection = threadLocal.get();
        if(connection == null) {
            // 从连接池拿连接并绑定到线程
            connection = dataSource.getConnection();
            // 绑定到当前线程
            threadLocal.set(connection);
        }
        return connection;
    }

    /**
     * 单例模式（静态内部类）
     */
    private MyConnectionUtil() {
    }
    private static class InnerClass{
        private static MyConnectionUtil myConnectionUtil = new MyConnectionUtil();
    }
    public static MyConnectionUtil getInstance(){
        return InnerClass.myConnectionUtil;
    }
}
