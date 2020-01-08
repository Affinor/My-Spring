package com.jin.demo.myspring.jdbc;

import java.io.InputStream;

/**资源加载类
 * @author wangjin
 */
public class MyResources {
    public static InputStream getResourceAsSteam(String path){
        return MyResources.class.getClassLoader().getResourceAsStream(path);
    }
}
