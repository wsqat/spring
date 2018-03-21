## 1、什么是线程池

事先创建若干个线程放入一个容器中,需要的时候从池中获取线程,不用再创建了,使用完毕不需要销毁而是放入池中,减少创建和销毁的开销
如何设计一个动态大小的线程池

## 2、线程池的基本组成部分
- 线程管理器:用于创建并管理线程
- 工作线程:线程池中的线程,可以是就绪状态,也可以循环执行任务,也可以是阻塞
- 任务接口:每个任务必须实现的接口,以供工作线程调度任务的执行,规定了任务的入口,任务完成后的收尾工作,任务的执行状态
- 任务队列:用于存放没有在工作的线程

## 3、Java通过Executors提供四种线程池
分别为
- newCachedThreadPool创建一个可缓存线程池，如果线程池长度超过处理需要，可灵活回收空闲线程，若无可回收，则新建线程。
- newFixedThreadPool 创建一个定长线程池，可控制线程最大并发数，超出的线程会在队列中等待。
- newScheduledThreadPool 创建一个定长线程池，支持定时及周期性任务执行。
- newSingleThreadExecutor 创建一个单线程化的线程池，它只会用唯一的工作线程来执行任务，保证所有任务按照指定顺序(FIFO, LIFO, 优先级)执行。
 
## 4. 常用的几种线程池
### 4.1 newCachedThreadPool
创建一个可缓存线程池，如果线程池长度超过处理需要，可灵活回收空闲线程，若无可回收，则新建线程。

这种类型的线程池特点是：

- 工作线程的创建数量几乎没有限制(其实也有限制的,数目为Interger. MAX_VALUE), 这样可灵活的往线程池中添加线程。
- 如果长时间没有往线程池中提交任务，即如果工作线程空闲了指定的时间(默认为1分钟)，则该工作线程将自动终止。终止后，如果你又提交了新的任务，则线程池重新创建一个工作线程。
- 在使用CachedThreadPool时，一定要注意控制任务的数量，否则，由于大量线程同时运行，很有会造成系统瘫痪。
> 实现代码
```
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CachedThreadPoolExecutorTest {
    public static void main(String[] args) {
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
        for (int i = 0; i < 10; i++) {
            final int index = i;
            try {
                Thread.sleep(index * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            cachedThreadPool.execute(new Runnable() {
                public void run() {
                    System.out.println(index);
                }
            });
        }
    }
}
```
> 输出结果

```
0
1
2
3
4
5
6
7
8
9
```


### 4.2 newFixedThreadPool

创建一个指定工作线程数量的线程池。每当提交一个任务就创建一个工作线程，如果工作线程数量达到线程池初始的最大数，则将提交的任务存入到池队列中。

FixedThreadPool是一个典型且优秀的线程池，它具有线程池提高程序效率和节省创建线程时所耗的开销的优点。但是，在线程池空闲时，即线程池中没有可运行任务时，它不会释放工作线程，还会占用一定的系统资源。

示例代码如下：
```
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FixedThreadPoolExecutorTest {
    public static void main(String[] args) {
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(3);
        for (int i = 0; i < 10; i++) {
            final int index = i;
            fixedThreadPool.execute(new Runnable() {
                public void run() {
                    try {
                        System.out.println(index);
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
```


因为线程池大小为3，每个任务输出index后sleep 2秒，所以每两秒打印3个数字。
定长线程池的大小最好根据系统资源进行设置如Runtime.getRuntime().availableProcessors()。


### 4.3 newSingleThreadExecutor

创建一个单线程化的Executor，即只创建唯一的工作者线程来执行任务，它只会用唯一的工作线程来执行任务，保证所有任务按照指定顺序(FIFO, LIFO, 优先级)执行。如果这个线程异常结束，会有另一个取代它，保证顺序执行。单工作线程最大的特点是可保证顺序地执行各个任务，并且在任意给定的时间不会有多个线程是活动的。

示例代码如下
```
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SingleThreadExecutorTest {
    public static void main(String[] args) {
        ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
        for (int i = 0; i < 10; i++) {
            final int index = i;
            singleThreadExecutor.execute(new Runnable() {
                public void run() {
                    try {
                        System.out.println(index);
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
```


### 4.4 newScheduleThreadPool
创建一个定长的线程池，而且支持定时的以及周期性的任务执行，支持定时及周期性任务执行。

延迟3秒执行，延迟执行示例代码如下：
```
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ScheduleThreadPoolExecutorTest {
    public static void main(String[] args) {
        ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(5);
        scheduledThreadPool.schedule(new Runnable() {
            public void run() {
                System.out.println("delay 3 seconds");
            }
        }, 3, TimeUnit.SECONDS);
    }
}

```

表示延迟1秒后每3秒执行一次，定期执行示例代码如下：

```
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
public class ScheduleThreadPoolExecutorTest {
 public static void main(String[] args) {
  ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(5);
  scheduledThreadPool.scheduleAtFixedRate(new Runnable() {
   public void run() {
    System.out.println("delay 1 seconds, and excute every 3 seconds");
   }
  }, 1, 3, TimeUnit.SECONDS);
 }
}
```
> 输出结果
```
delay 1 seconds, and excute every 3 seconds
delay 1 seconds, and excute every 3 seconds
delay 1 seconds, and excute every 3 seconds
```


## 5、Executor 执行 Callable 任务的示例代码

> TestCallable.java
```
import java.util.concurrent.Callable;

public class TestCallable implements Callable<String> {

    private int id;
    public TestCallable(int id)
    {
        this.id = id;
    }

    /*
     * 任务会传递给ExecutorService的submit方法
     * 传递后该方法自动在一个线程上执行
     * **/

    @Override
    public String call() throws Exception{
        System.out.println("call方法已被调用!!!   " + Thread.currentThread().getName());

        StringBuilder sb = new StringBuilder("call()方法被自动调用,任务返回结果:");
        sb.append(id);
        sb.append("   ");
        sb.append(Thread.currentThread().getName());

        return sb.toString();
    }
}
```

> CallableExecutorTest.java

```
// Executor 执行 Callable 任务的示例代码

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CallableExecutorTest {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();

        // 当将一个 Callable 的对象传递给 ExecutorService 的 submit 方法，则该 call 方法自动在一个线程上执行，并且会返回执行结果 Future 对象。
        // Future并发异步,可以不等待返回结果,需要时可以查看
        ArrayList<Future<String>> resultList = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            Future<String> future = executorService.submit(new TestCallable(i));
            resultList.add(future);
        }

        for (Future<String> fs : resultList) {
            try {
                while (fs.isDone())  // 如果没完成则循环等待
                    System.out.println(fs.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } finally {
                executorService.shutdown();
            }
        }
    }
}

```


## 6、如何结束ExecutorService？
可以关闭 ExecutorService，这将导致其拒绝新任务。提供两个方法来关闭 ExecutorService。shutdown() 方法在终止前允许执行以前提交的任务，而 shutdownNow() 方法阻止等待任务启动并试图停止当前正在执行的任务。在终止时，执行程序没有任务在执行，也没有任务在等待执行，并且无法提交新任务。应该关闭未使用的 ExecutorService 以允许回收其资源。 

下列方法分两个阶段关闭 ExecutorService。
- 第一阶段调用 shutdown 拒绝传入任务，
- 第二阶段调用 shutdownNow（如有必要）取消所有遗留的任务： 

```
void shutdownAndAwaitTermination(ExecutorService pool) {  
  pool.shutdown(); // Disable new tasks from being submitted  
  try {  
    // Wait a while for existing tasks to terminate  
    if (!pool.awaitTermination(60, TimeUnit.SECONDS)) {  
      pool.shutdownNow(); // Cancel currently executing tasks  
      // Wait a while for tasks to respond to being cancelled  
      if (!pool.awaitTermination(60, TimeUnit.SECONDS))  
          System.err.println("Pool did not terminate");  
    }  
  } catch (InterruptedException ie) {  
    // (Re-)Cancel if current thread also interrupted  
    pool.shutdownNow();  
    // Preserve interrupt status  
    Thread.currentThread().interrupt();  
  }  
} 
```

两种方法
- shutdown调用后，不可以再submit新的task，已经submit的将继续执行。
- shutdownNow试图停止当前正执行的task，并返回尚未执行的task的list