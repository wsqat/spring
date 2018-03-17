## 一、教程目的
使用IntelliJ IDEA搭建Spring环境，比较传统的对象之间的调用和使用Spring容器后的对象之间的调用关系的变化，打印输出 “HelloWorld”的例子。

## 二、创建项目

- 1、打开IDEA，点击Create New Project
- 2、勾选Spring然后next 
 
![image.png](https://upload-images.jianshu.io/upload_images/688387-83008b1a5a9ac24b.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

- 3、设置你项目所想要存放的路径以及名字 
- 4、这时候IntelliJ IDEA就会自动下载Spring所需要的jars，只需要等待就好。 
- 5、下载好后，Spring的jars和配置文件都准备好了。 

![image.png](https://upload-images.jianshu.io/upload_images/688387-7e686f6cbb477479.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

>lib下为框架所需要的jar包，自动下载的；applicationContext.xml为Spring配置文件，并声明所有可用的Spring bean

## 三、HelloWord实现

### 1、HelloWorld类

> HelloWorld.java

```
package helloworld;

/**
 * Spring bean
 *
 */
public class HelloWorld {
    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public void printHello() {
        System.out.println("Spring 3 : Hello " + name + " !");
    }
}
```

### 2、传统的输出“HelloWorld”

不使用Spring框架之前的步骤
- 1、创建HelloWorld对象
- 2、为实例对象的属性赋值
- 3、调用对象的方法

```
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

    }
}
```

### 3、Spring Bean配置文件

> applicationContext.xml，创建Spring配置文件，并声明所有可用的Spring bean。

```
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
	http://www.springframework.org/schema/beans/spring-beans-3.0.xsd">

    <bean id="helloBean" class="helloworld.HelloWorld">
        <property name="name" value="sagewang" />
    </bean>

</beans>
```

### 4、Spring框架下的IOC容器

使用Spring框架的用法
- 1、创建一个Spring的IOC容器对象
- 2、从IOC容器中获取Bean实例
- 3、调用对象的方法

```
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
```

### 5、输出

```
Spring 3 : Hello sagewang !
三月 17, 2018 4:54:32 下午 org.springframework.context.support.ClassPathXmlApplicationContext prepareRefresh
信息: Refreshing org.springframework.context.support.ClassPathXmlApplicationContext@506c589e: startup date [Sat Mar 17 16:54:32 CST 2018]; root of context hierarchy
三月 17, 2018 4:54:33 下午 org.springframework.beans.factory.xml.XmlBeanDefinitionReader loadBeanDefinitions
信息: Loading XML bean definitions from class path resource [applicationContext.xml]
Spring 3 : Hello sagewang !
```

## 四、结论
对Spring的作用的初步了解
从上面的例子可以得出结论，Spring到底帮我们做了什么，当我们没有使用Spring的时候，调用printHello()方法需要3步： 
1. 创建一个HelloWorld的实例对象 
2. 设置实例对象的name属性 
3. 调用对象的sayHello()方法 

后来也需要3步： 
1. 创建一个Spring的IOC容器对象 
2. 从IOC容器中获取Bean实例 
3. 调用printHello()方法 

然后我们探究了什么时候new的helloWorld对象，我们可以看出，Spring帮我们完成了前2步，也就是创建实例对象以及设置对象的属性，也就是说我们可以把对象的创建和管理工作交给Spring去完成，不需要自己去new对象，也不要去设置对象的属性，只要写好Spring的配置文件，Spring就可以帮我们去做，当我们需要对象的时候，直接去找Spring去要就行。

> 这就是Spring框架的核心容器之一IoC 容器的作用。

### 什么是Ioc/DI？
IoC 容器：最主要是完成了完成对象的创建和依赖的管理注入等等。

先从我们自己设计这样一个视角来考虑：

所谓控制反转，就是把原先我们代码里面需要实现的对象创建、依赖的代码，反转给容器来帮忙实现。那么必然的我们需要创建一个容器，同时需要一种描述来让容器知道需要创建的对象与对象的关系。这个描述最具体表现就是我们可配置的文件。

- 对象和对象关系怎么表示？
可以用 xml ， properties 文件等语义化配置文件表示。

- 描述对象关系的文件存放在哪里？可能是 classpath ， filesystem ，或者是 URL 网络资源， servletContext 等。

回到正题，有了配置文件，还需要对配置文件解析。

- 不同的配置文件对对象的描述不一样，如标准的，自定义声明式的，如何统一？ 在内部需要有一个统一的关于对象的定义，所有外部的描述都必须转化成统一的描述定义。

- 如何对不同的配置文件进行解析？需要对不同的配置文件语法，采用不同的解析器。

[示例代码-github](https://github.com/wsqat/spring)


参考： 

[Spring hello world实例](https://www.yiibai.com/spring/spring-3-hello-world-example.html)

[Spring IntelliJ IDEA搭建Spring环境](http://blog.csdn.net/cflys/article/details/70598903)