1、学员自定义@Controller、@Service、@Autowired、@Transactional注解类，完成基于注解的IOC容器（Bean对象创建及依赖注入维护）和声明式事务控制 IOC中的beanName注意考虑以下情况：
1）注解有无value属性值的情况
2）service层是否实现接口的情况

2、根据源码剖析，记录spring循环依赖处理机制中的调用关系，画出uml时序图
（UML时序图，实在不会画哈，请见谅，所以就画了个实现的简图，和spring的实现不太一样）