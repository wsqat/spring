## 1、Java实现多线程的方式及区别
- 方式
    - 继承Thread类,实现run函数
    - 实现Runnable接口
    - 实现Callable接口
- 区别
    - 实现Runnable接口避免因为单继承而带来的局限性;增强程序健壮性
    - 继承了Thread与实现了Runnable后需要start来开启线程,此时JVM将此线程放到就绪队列,如果有处理机会,则执行run方法
    - Callable则是使用call方法,并且线程执行完毕后有返回,其他两种不会

## 2、多线程并发
### 2.1、继承Thread类
```
//线程实现
class MyThread extends Thread{

    private String name;

    public MyThread(String name){
        this.name = name;
    }

    @Override
    public void run(){
        Single s = Single.getSingle();
        System.out.println(s);
        System.out.println("name:"+name+" 子线程ID:"+Thread.currentThread().getId());
    }
}

//单例模式，双重校验锁
class Single{
    private Single(){};
    private static Single single = null;

    public static Single getSingle() {
        if (single==null){
            synchronized (Single.class){
                if (single == null){
                    single = new Single();
                }
            }
        }
        return single;
    }
}

//多线程并发访问单例模式
//https://www.cnblogs.com/yjd_hycf_space/p/7526608.html
//http://www.importnew.com/21136.html
public class MultiThread{

    public static void main(String[] args){
        System.out.println("主线程ID:"+Thread.currentThread().getId());
        MyThread thread1 = new MyThread("thread1");
        thread1.start();
        MyThread thread2 = new MyThread("thread2");
        thread2.run();
    }
}

```
> 输出结果
```
主线程ID:1
ThreadPool.Single@33557a1
ThreadPool.Single@33557a1
name:thread2 子线程ID:1
name:thread1 子线程ID:9
```

### 2.2、实现Runnable接口

> TestRunnable.java

```
public class TestRunnable implements Runnable{
    private String name;

    public TestRunnable(String name){
        this.name = name;
    }

    @Override
    public void run(){
        System.out.println("name:"+name+" 子线程ID:"+Thread.currentThread().getId());
    }
}
```

> MultiThreadTest.java

```
public class MultiThreadTest{

    public static void main(String[] args){
        System.out.println("主线程ID:"+Thread.currentThread().getId());
        TestRunnable thread1 = new TestRunnable("thread1");
        thread1.run();
        TestRunnable thread2 = new TestRunnable("thread2");
        thread2.run();
    }
}
```


### 2.3、实现Callable接口


```
package ThreadPool;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class MultiThreadTest{

    public static void main(String[] args){
//        System.out.println("主线程ID:"+Thread.currentThread().getId());
//        TestRunnable thread1 = new TestRunnable("thread1");
//        thread1.run();
//        TestRunnable thread2 = new TestRunnable("thread2");
//        thread2.run();

        /*
         * 一、创建执行线程的方式三：实现 Callable 接口。 相较于实现 Runnable 接口的方式，方法可以有返回值，并且可以抛出异常。
         *
         * 二、执行 Callable 方式，需要 FutureTask 实现类的支持，用于接收运算结果。  FutureTask 是  Future 接口的实现类
         */

        CallableDemo td = new CallableDemo();

        //1.执行 Callable 方式，需要 FutureTask 实现类的支持，用于接收运算结果。
        FutureTask<Integer> result = new FutureTask<>(td);

        new Thread(result).start();

        //2.接收线程运算后的结果
        try {
            Integer sum = result.get();  //FutureTask 可用于 闭锁 类似于CountDownLatch的作用，在所有的线程没有执行完成之后这里是不会执行的
            System.out.println(sum);
            System.out.println("------------------------------------");
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

}

class CallableDemo implements Callable<Integer> {

    @Override
    public Integer call() throws Exception {
        int sum = 0;

        for (int i = 0; i <= 100000; i++) {
            sum += i;
        }

        return sum;
    }
}

```
> 输出结果
```
705082704
------------------------------------
```


Callable 和 Runnable接口的区别

- （1）Callable规定的方法是call()，而Runnable规定的方法是run(). 
- （2）Callable的任务执行后可返回值，而Runnable的任务是不能返回值的。  
- （3）call()方法可抛出异常，而run()方法是不能抛出异常的。 
- （4）运行Callable任务可拿到一个Future对象， Future表示异步计算的结果。 
- （5）它提供了检查计算是否完成的方法，以等待计算的完成，并检索计算的结果。 
- （6）通过Future对象可了解任务执行情况，可取消任务的执行，还可获取任务执行的结果。 
- （7）Callable是类似于Runnable的接口，实现Callable接口的类和实现Runnable的类都是可被其它线程执行的任务。