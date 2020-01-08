package com.jin.demo.myspring.beans;

import com.jin.demo.myspring.annotation.MyBean;
import com.jin.demo.myspring.annotation.MyConfiguration;
import com.jin.demo.myspring.beans.config.MyBeanDefinition;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**对所有的bean进行读取
 * @author wangjin
 */
public class MyBeanDefinitionReader {
    /**
     * 保存bean的全路径
     */
    private List<String> registyBeanClasses = new ArrayList<String>();

    /**
     * properties配置文件
     */
    private Properties config = new Properties();

    /**
     * 固定配置文件中的key，相对于xml的规范
     */
    private final String SCAN_PACKAGE = "scan.package";

    /**
     * 构造传入资源路径，进行加载
     * @param locations
     */
    public MyBeanDefinitionReader(String... locations){
        //通过URL定位找到其所对应的文件，然后转换为文件流
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(locations[0].replace("classpath:",""));
        try {
            //加载
            config.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(null != is){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        //扫描所有的类
        doScanner(config.getProperty(SCAN_PACKAGE));
    }
    private void doScanner(String scanPackage) {
        //转换为文件路径，就是把.替换为/
        URL url = this.getClass().getResource("/" + scanPackage.replaceAll("\\.","/"));
        File classPath = new File(url.getFile());
        for (File file : classPath.listFiles()) {
            //如果是文件夹，就递归扫描
            if(file.isDirectory()){
                doScanner(scanPackage + "." + file.getName());
            }else{
                //如果是类，就直接装载
                if(!file.getName().endsWith(".class")){ continue;}
                String className = (scanPackage + "." + file.getName().replace(".class",""));
                registyBeanClasses.add(className);
            }
        }
    }

    /**
     * 把配置文件中扫描到的所有的配置类信息转换为MyBeanDefinition对象，方便之后IOC操作
     */
    public List<MyBeanDefinition> loadBeanDefinitions(){
        List<MyBeanDefinition> result = new ArrayList<MyBeanDefinition>();
        try {
            for (String className : registyBeanClasses) {
                Class<?> beanClass = Class.forName(className);

                //如果是一个接口，是不能实例化的
                //用它实现类来实例化
                if(beanClass.isInterface()) { continue; }

                //beanName有三种情况:
                //1、默认是类名首字母小写
                //2、自定义名字
                //3、接口注入
                result.add(doCreateBeanDefinition(toLowerFirstCase(beanClass.getSimpleName()),beanClass.getName()));

                Class<?> [] interfaces = beanClass.getInterfaces();
                for (Class<?> i : interfaces) {
                    //如果是多个实现类，就直接覆盖，暂时不做那么多考虑
                    //这个时候，可以自定义名字
                    result.add(doCreateBeanDefinition(i.getName(),beanClass.getName()));
                }
                //注解注入的配置加载
                if (beanClass.isAnnotationPresent(MyConfiguration.class)){
                    doCreateBeanDefinitionWithMyConfig(beanClass,result);
                }

            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 根据MyConfiguration注解注入的beanDefinition
     * @param beanClass
     * @param result
     */
    private void doCreateBeanDefinitionWithMyConfig(Class<?> beanClass, List<MyBeanDefinition> result) {
        for (Method declaredMethod : beanClass.getDeclaredMethods()) {
            if (declaredMethod.isAnnotationPresent(MyBean.class)){
                String[] value = declaredMethod.getDeclaredAnnotation(MyBean.class).value();
                if (value.length==0){
                    continue;
                }
                if (null == value[0] || "".equals(value[0])){
                    continue;
                }
                MyBeanDefinition myBeanDefinition = new MyBeanDefinition();
                //MyBean注解中的value为beanName
                myBeanDefinition.setFactoryBeanName(value[0]);
                myBeanDefinition.setBeanClassName(declaredMethod.getReturnType().getName());
                result.add(myBeanDefinition);
            }
        }
    }

    /**
     * 把每一个配信息解析成一个BeanDefinition
     */
    private MyBeanDefinition doCreateBeanDefinition(String factoryBeanName,String beanClassName){
        MyBeanDefinition beanDefinition = new MyBeanDefinition();
        beanDefinition.setBeanClassName(beanClassName);
        beanDefinition.setFactoryBeanName(factoryBeanName);
        return beanDefinition;
    }

    /**
     * 如果类名本身是小写字母，确实会出问题
     * 但是我要说明的是：这个方法是我自己用，private的
     * 传值也是自己传，类也都遵循了驼峰命名法
     * 默认传入的值，存在首字母小写的情况，也不可能出现非字母的情况
     * 为了简化程序逻辑，就不做其他判断了，
     * @param simpleName
     * @return
     */
    private String toLowerFirstCase(String simpleName) {
        char [] chars = simpleName.toCharArray();
        //之所以加，是因为大小写字母的ASCII码相差32，
        // 而且大写字母的ASCII码要小于小写字母的ASCII码
        //在Java中，对char做算学运算，实际上就是对ASCII码做算学运算
        chars[0] += 32;
        return String.valueOf(chars);
    }


    public Properties getConfig(){
        return this.config;
    }
}
