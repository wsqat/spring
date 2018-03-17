package helloworld;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {

    public static void main(String[] args) {

        //��ʹ��Spring���֮ǰ�Ĳ���
        //1������HelloWorld����
        HelloWorld helloWorld = new HelloWorld();
        //2��Ϊʵ����������Ը�ֵ
        helloWorld.setName("sagewang");
        //3�����ö���ķ���
        helloWorld.printHello();


        //ʹ��Spring��ܵ��÷�
        //1������һ��Spring��IOC��������
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        //2����IOC�����л�ȡBeanʵ��
        HelloWorld obj = (HelloWorld) context.getBean("helloBean");
        //3�����ö���ķ���
        obj.printHello();
    }
}