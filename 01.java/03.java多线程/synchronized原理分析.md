# synchronized原理分析


## 反编译同步代码段


反编译下面一段代码的class文件，看一下synchronzied是怎么实现的。

```
package com.zch.blogs.java.multithreads.synchronizeddemo;

public class SynchronizedDemo8 {
	public void method() {
		synchronized (this) {
			System.out.println("Method 1 start");
		}
	}
}

```

通过下面命令反编译，反编译之前要生成class文件。

```
javap -c SynchronizedDemo8
```

反编译结果如下：

```

警告: 二进制文件SynchronizedDemo8包含com.zch.blogs.java.multithreads.synchronizeddemo.SynchronizedDemo8
Compiled from "SynchronizedDemo8.java"
public class com.zch.blogs.java.multithreads.synchronizeddemo.SynchronizedDemo8 {
  public com.zch.blogs.java.multithreads.synchronizeddemo.SynchronizedDemo8();
    Code:
       0: aload_0
       1: invokespecial #1                  // Method java/lang/Object."<init>":()V
       4: return

  public void method();
    Code:
       0: aload_0
       1: dup
       2: astore_1
       3: monitorenter
       4: getstatic     #2                  // Field java/lang/System.out:Ljava/io/PrintStream;
       7: ldc           #3                  // String Method 1 start
       9: invokevirtual #4                  // Method java/io/PrintStream.println:(Ljava/lang/String;)V
      12: aload_1
      13: monitorexit
      14: goto          22
      17: astore_2
      18: aload_1
      19: monitorexit
      20: aload_2
      21: athrow
      22: return
    Exception table:
       from    to  target type
           4    14    17   any
          17    20    17   any
}
```

可以看到有两条重要命令monitorenter和monitorexit




关于这两条指令的作用，我们直接参考JVM规范中描述：

**monitorenter ：**
>Each object is associated with a monitor. A monitor is locked if and only if it has an owner. The thread that executes monitorenter attempts to gain ownership of the monitor associated with objectref, as follows:

>• If the entry count of the monitor associated with objectref is zero, the thread enters the monitor and sets its entry count to one. The thread is then the owner of the monitor.

>• If the thread already owns the monitor associated with objectref, it reenters the monitor, incrementing its entry count.

>• If another thread already owns the monitor associated with objectref, the thread blocks until the monitor's entry count is zero, then tries again to gain ownership.

这段话的大概意思为：

每个对象有一个监视器锁（monitor）。当monitor被占用时就会处于锁定状态，线程执行monitorenter指令时尝试获取monitor的所有权，过程如下：

1、如果monitor的进入数为0，则该线程进入monitor，然后将进入数设置为1，该线程即为monitor的所有者。

2、如果线程已经占有该monitor，只是重新进入，则进入monitor的进入数加1.

3.如果其他线程已经占用了monitor，则该线程进入阻塞状态，直到monitor的进入数为0，再重新尝试获取monitor的所有权。

**monitorexit：**

>The thread that executes monitorexit must be the owner of the monitor associated with the instance referenced by objectref.

>The thread decrements the entry count of the monitor associated with objectref. If as a result the value of the entry count is zero, the thread exits the monitor and is no longer its owner. Other threads that are blocking to enter the monitor are allowed to attempt to do so.


这段话的大概意思为：

执行monitorexit的线程必须是objectref所对应的monitor的所有者。

指令执行时，monitor的进入数减1，如果减1后进入数为0，那线程退出monitor，不再是这个monitor的所有者。其他被这个monitor阻塞的线程可以尝试去获取这个 monitor 的所有权。


通过这两段描述，我们应该能很清楚的看出Synchronized的实现原理，Synchronized的语义底层是通过一个monitor的对象来完成，其实wait/notify等方法也依赖于monitor对象，这就是为什么只有在同步的块或者方法中才能调用wait/notify等方法，否则会抛出java.lang.IllegalMonitorStateException的异常的原因。


## 同步方法的反编译

下面这段代码，用上面的方法反编译。

```

package com.zch.blogs.java.multithreads.synchronizeddemo;

public class SynchronizedDemo9 {
	public synchronized void method() {
		System.out.println("Method 1 start");
	}
}


```

其结果是：

```

警告: 二进制文件SynchronizedDemo9包含com.zch.blogs.java.multithreads.synchronizeddemo.SynchronizedDemo9
Compiled from "SynchronizedDemo9.java"
public class com.zch.blogs.java.multithreads.synchronizeddemo.SynchronizedDemo9 {
  public com.zch.blogs.java.multithreads.synchronizeddemo.SynchronizedDemo9();
    Code:
       0: aload_0
       1: invokespecial #1                  // Method java/lang/Object."<init>":()V
       4: return

  public synchronized void method();
    Code:
       0: getstatic     #2                  // Field java/lang/System.out:Ljava/io/PrintStream;
       3: ldc           #3                  // String Method 1 start
       5: invokevirtual #4                  // Method java/io/PrintStream.println:(Ljava/lang/String;)V
       8: return
}

```


反编译的结果来看，方法的同步并没有通过指令monitorenter和monitorexit来完成。

但别人反编译出来有ACC_SYNCHRONIZED，我的没有，不知道为什么。

下面是别人的说法：

>不过相对于普通方法，其常量池中多了ACC_SYNCHRONIZED标示符。JVM就是根据该标示符来实现方法的同步的：当方法调用时，调用指令将会检查方法的 ACC_SYNCHRONIZED 访问标志是否被设置，如果设置了，执行线程将先获取monitor，获取成功之后才能执行方法体，方法执行完后再释放monitor。在方法执行期间，其他任何线程都无法再获得同一个monitor对象。 其实本质上没有区别，只是方法的同步是一种隐式的方式来实现，无需通过字节码来完成。

## 参考

- [Java并发编程：Synchronized及其实现原理](https://www.cnblogs.com/paddix/p/5367116.html)
