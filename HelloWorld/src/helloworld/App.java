package helloworld;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {

    public static void main(String[] args) {

        //不使用Spring框架之前的步骤
        //1、创建HelloWorld对象
        HelloWorld helloWorld = new HelloWorld();
        //2、为实例对象的属性赋值
        helloWorld.setName("sagewang");
        //3、调用对象的方法
        helloWorld.printHello();


        //使用Spring框架的用法
        //1、创建一个Spring的IOC容器对象
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        //2、从IOC容器中获取Bean实例
        HelloWorld obj = (HelloWorld) context.getBean("helloBean");
        //3、调用对象的方法
        obj.printHello();
    }
}