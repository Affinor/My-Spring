package com.jin.demo.myspring.context;

import com.jin.demo.myspring.annotation.MyAutowired;
import com.jin.demo.myspring.annotation.MyController;
import com.jin.demo.myspring.annotation.MyRepository;
import com.jin.demo.myspring.annotation.MyService;
import com.jin.demo.myspring.aop.MyAdvisedSupport;
import com.jin.demo.myspring.aop.MyAopProxy;
import com.jin.demo.myspring.aop.MyCglibAopProxy;
import com.jin.demo.myspring.aop.MyJdkDynamicAopProxy;
import com.jin.demo.myspring.aop.config.MyAopConfig;
import com.jin.demo.myspring.beans.MyBeanDefinitionReader;
import com.jin.demo.myspring.beans.MyBeanWrapper;
import com.jin.demo.myspring.beans.MyDefaultListableBeanFactory;
import com.jin.demo.myspring.beans.config.MyBeanDefinition;
import com.jin.demo.myspring.beans.config.MyBeanPostProcessor;
import com.jin.demo.myspring.core.MyBeanFactory;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**IOC容器，核心实现
 * @author wangjin
 */
public class MyApplicationContext extends MyDefaultListableBeanFactory implements MyBeanFactory {
    /**
     * 保存配置文件路径
     */
    private String [] configLoactions;
    /**
     * 对所有的bean进行读取
     */
    private MyBeanDefinitionReader reader;

    /**
     * 单例的IOC容器缓存（真正的IOC容器，存放初始完成的bean）===>>>一级缓存
     */
    private Map<String,MyBeanWrapper> factoryBeanObjectCache = new ConcurrentHashMap<String, MyBeanWrapper>();
    /**
     * 初始化中的bean===>>>二级缓存
     */
    private Map<String, MyBeanWrapper> factoryBeanInstanceCache = new ConcurrentHashMap<String, MyBeanWrapper>();
    /**
     * 刚实例化的bean===>>>三级缓存
     */
    private Map<String, MyBeanWrapper> factoryBeanIntiCache = new ConcurrentHashMap<String, MyBeanWrapper>();

    public MyApplicationContext(String... configLoactions){
        this.configLoactions = configLoactions;
        try {
            refresh();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void refresh() throws Exception {
        //1、定位，定位配置文件
        reader = new MyBeanDefinitionReader(this.configLoactions);

        //2、加载配置文件，扫描相关的类，把它们封装成BeanDefinition
        List<MyBeanDefinition> beanDefinitions = reader.loadBeanDefinitions();

        //3、注册，把配置信息放到容器里面(伪IOC容器)
        doRegisterBeanDefinition(beanDefinitions);

        //4、把不是延时加载的类，有提前初始化
        doAutowrited();
    }

    /**只处理非延时加载的情况
     * 把BeanWrapper存到IOC容器里面
     * 1、最初实例化
     * 2、再次初始化bean
     * 3、最后注入bean
     * 根据spring的三级缓存机制，分三步进行处理
     */
    private void doAutowrited() throws Exception {
        //1、最初实例化
        firstInit();
        //2、再次初始化bean
        nextInit();
        //3、最后注入bean
        endInit();
    }

    /**
     * 循环遍历beanDefinitionMap,
     * 实例化所有bean,然后放入三级缓存
     * @throws Exception
     */
    private void firstInit() throws Exception {
        Iterator<Map.Entry<String, MyBeanDefinition>> iterator = super.beanDefinitionMap.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<String, MyBeanDefinition> beanDefinitionEntry = iterator.next();
            MyBeanDefinition myBeanDefinition = beanDefinitionEntry.getValue();
            //非延时加载切未初始化完成的bean,进行初始化
            if(!myBeanDefinition.isLazyInit()) {
                //实例化这个bean
                instantiateBean(myBeanDefinition);
            }
        }
    }

    /**
     * 循环遍历三级缓存，
     * 把刚刚实例化的对象的所有属性（带有@MyAutowired注解的）进行赋值（值从当前三级缓存中取）
     * 然后把赋值好的bean，放入二级缓存
     * @throws Exception
     */
    private void nextInit() throws Exception {
        Iterator<Map.Entry<String, MyBeanWrapper>> iterator = this.factoryBeanIntiCache.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<String, MyBeanWrapper> beanDefinitionEntry = iterator.next();
            String beanName = beanDefinitionEntry.getKey();
            MyBeanWrapper myBeanWrapper = beanDefinitionEntry.getValue();
            //对这个bean的所有属性进行赋值
            initPopulateBean(beanName,myBeanWrapper);
        }
    }
    /**
     * 循环遍历二级缓存，
     * 把刚刚实例化的对象的所有属性（带有@MyAutowired注解的）进行赋值（值从二级缓存中取）
     * 然后把赋值好的bean，放入一级缓存
     * （备注：此时相当于，对该bean的属性的属性进行赋值操作）
     * @throws Exception
     */
    private void endInit() throws Exception {
        Iterator<Map.Entry<String, MyBeanWrapper>> iterator = this.factoryBeanInstanceCache.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<String, MyBeanWrapper> beanDefinitionEntry = iterator.next();
            String beanName = beanDefinitionEntry.getKey();
            MyBeanWrapper myBeanWrapper = beanDefinitionEntry.getValue();
            //对这个bean的所有属性的属性进行赋值
            endPopulateBean(beanName,myBeanWrapper);
        }
    }

    /**
     * 真正的注册，方法结束后，IOC容器初始化完毕
     * @param beanDefinitions
     * @throws Exception
     */
    private void doRegisterBeanDefinition(List<MyBeanDefinition> beanDefinitions) throws Exception {
        for (MyBeanDefinition beanDefinition: beanDefinitions) {
            //BeanName不允许重复！！！
            if(super.beanDefinitionMap.containsKey(beanDefinition.getFactoryBeanName())){
                throw new Exception("The “" + beanDefinition.getFactoryBeanName() + "” is exists!!");
            }
            super.beanDefinitionMap.put(beanDefinition.getFactoryBeanName(),beanDefinition);
        }
    }

    /**
     * 最终注入
     * @param beanName
     * @param myBeanWrapper
     * @throws IllegalAccessException
     */
    private void endPopulateBean(String beanName, MyBeanWrapper myBeanWrapper) throws IllegalAccessException {
        Class<?> clazz = myBeanWrapper.getWrappedClass();
        Object instance = myBeanWrapper.getWrappedInstance();
        //获得所有的fields
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if(!field.isAnnotationPresent(MyAutowired.class)){
                continue;
            }
            String autowiredBeanName = field.getAnnotation(MyAutowired.class).value().trim();
            //如果注解中值为空，就默认使用属性名
            if("".equals(autowiredBeanName)){
                autowiredBeanName = field.getType().getName();
            }
            field.setAccessible(true);
            MyBeanWrapper fieldBeanWrapper = this.factoryBeanInstanceCache.get(autowiredBeanName);
            //AOP的实现
            MyAdvisedSupport config = instantionAopConfig();
            config.setTargetClass(fieldBeanWrapper.getWrappedInstance().getClass());
            config.setTarget(fieldBeanWrapper.getWrappedInstance());
            Object proxy = null;
            //符合PointCut的规则的话，创建代理对象
            if(config.pointCutMatch()) {
                proxy = createProxy(config).getProxy();
            }
            //如果没有代理对象，就用当前实例，否则直接用代理对象
            if (null!=proxy){
                fieldBeanWrapper.setProxy(proxy);
                fieldBeanWrapper.setWrappedInstance(proxy);
            }
            field.set(instance,fieldBeanWrapper.getWrappedInstance());
        }
        myBeanWrapper.setStatus("3");
        //为了即可以通过beanName取值，又可以通过全类名取值，还可以通过自定义的beanID取值
        this.factoryBeanObjectCache.put(beanName,myBeanWrapper);//类型
        //此处暂时为多个，待后续优化
        super.getBeanNameByType(beanName).forEach(name->{
            this.factoryBeanObjectCache.put(name,myBeanWrapper);//名称
        });
        //SimpleName的首字母小写，如：com.jin.demo.TransferService ===>>> transferService
        /*char[] chars = myBeanWrapper.getWrappedInstance().getClass().getSimpleName().toCharArray();
        chars[0] += 32;
        this.factoryBeanObjectCache.put(String.valueOf(chars),myBeanWrapper);*/
    }

    /**
     * 再次对bean进行初始化
     * @param beanName
     * @param myBeanWrapper
     * @throws IllegalAccessException
     */
    private void initPopulateBean(String beanName, MyBeanWrapper myBeanWrapper) throws IllegalAccessException {
        Class<?> clazz = myBeanWrapper.getWrappedClass();
        Object instance = myBeanWrapper.getWrappedInstance();
        //获得所有的fields
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            //只有带MyAutowired注解的才进行注入
            if(!field.isAnnotationPresent(MyAutowired.class)){
                continue;
            }
            String autowiredBeanName = field.getAnnotation(MyAutowired.class).value().trim();
            //如果注解中值为空，就默认使用属性名
            if("".equals(autowiredBeanName)){
                autowiredBeanName = field.getType().getName();
            }
            //强制访问
            field.setAccessible(true);
            field.set(instance,this.factoryBeanIntiCache.get(autowiredBeanName).getWrappedInstance());
        }
        myBeanWrapper.setStatus("2");
        this.factoryBeanInstanceCache.put(beanName,myBeanWrapper);
    }

    /**
     * 根据beanName获取bean的实例
     * @param beanName
     * @return
     * @throws Exception
     */
    @Override
    public Object getBean(String beanName) throws Exception {
        MyBeanPostProcessor postProcessor = new MyBeanPostProcessor();
        //获取bean之前进行处理
        postProcessor.postProcessBeforeInitialization(null,beanName);
        MyBeanWrapper beanWrapper = this.factoryBeanObjectCache.get(beanName);
        //获取bean之后进行处理
        postProcessor.postProcessAfterInitialization(beanWrapper.getWrappedInstance(),beanName);
        //返回bean的实例
        return beanWrapper.getProxy()==null?beanWrapper.getWrappedInstance():beanWrapper.getProxy();
    }


    /**
     * 对该bean进行实例化
     * @param myBeanDefinition
     * @return
     */
    private void instantiateBean(MyBeanDefinition myBeanDefinition) throws Exception {
        //1、拿到要实例化的对象的类名
        String className = myBeanDefinition.getBeanClassName();
        //2、反射实例化，得到一个对象
        Class<?> clazz = Class.forName(className);
        if (clazz.isAnnotationPresent(MyController.class)||clazz.isAnnotationPresent(MyService.class)||clazz.isAnnotationPresent(MyRepository.class)){
            Object instance = clazz.newInstance();
            //此时bean才刚刚实例化完成
            MyBeanWrapper myBeanWrapper = new MyBeanWrapper(instance,null);
            myBeanWrapper.setStatus("1");
            this.factoryBeanIntiCache.put(className,myBeanWrapper);
        }
    }

    /**
     * 创建AOP代理对象
     * @param config
     * @return
     */
    private MyAopProxy createProxy(MyAdvisedSupport config) {
        Class targetClass = config.getTargetClass();
        //代理接口，用JDK的动态代理
        if(targetClass.getInterfaces().length > 0){
            return new MyJdkDynamicAopProxy(config);
        }
        //代理类，用Cglib的动态代理
        return new MyCglibAopProxy(config);
    }

    /**
     * 横切点的配置信息（待优化）
     * @return
     */
    private MyAdvisedSupport instantionAopConfig() {
        MyAopConfig config = new MyAopConfig();
        config.setPointCut(this.reader.getConfig().getProperty("point.cut"));
        config.setAspectClass(this.reader.getConfig().getProperty("aspect.class"));
        config.setAspectBefore(this.reader.getConfig().getProperty("aspect.before"));
        config.setAspectAfter(this.reader.getConfig().getProperty("aspect.after"));
        config.setAspectAfterThrow(this.reader.getConfig().getProperty("aspect.after.throw"));
        config.setAspectAfterThrowingName(this.reader.getConfig().getProperty("aspect.after.throwingName"));
        return new MyAdvisedSupport(config);
    }

    @Override
    public Object getBean(Class<?> beanClass) throws Exception {
        return getBean(beanClass.getName());
    }

    public String[] getBeanDefinitionNames() {
        return this.beanDefinitionMap.keySet().toArray(new  String[this.beanDefinitionMap.size()]);
    }

    public int getBeanDefinitionCount(){
        return this.beanDefinitionMap.size();
    }

    public Properties getConfig(){
        return this.reader.getConfig();
    }
}
