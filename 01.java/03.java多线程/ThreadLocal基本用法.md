# ThreadLocal基本用法

## 简介

Java中的ThreadLocal类允许我们创建只能被同一个线程读写的变量。因此，如果一段代码含有一个ThreadLocal变量的引用，即使两个线程同时执行这段代码，它们也无法访问到对方的ThreadLocal变量。

Synchronized用于线程间的数据共享，而ThreadLocal则用于线程间的数据隔离。所以ThreadLocal的应用场合，最适合的是按线程多实例（每个线程对应一个实例）的对象的访问，并且这个对象很多地方都要用到。

ThreadLocal与线程同步机制不同，线程同步机制是多个线程共享同一个变量，而ThreadLocal是为每一个线程创建一个单独的变量副本，故而每个线程都可以独立地改变自己所拥有的变量副本，而不会影响其他线程所对应的副本。可以说ThreadLocal为多线程环境下变量问题提供了另外一种解决思路。

ThreadLocal定义了四个方法：

get()：返回此线程局部变量的当前线程副本中的值。
initialValue()：返回此线程局部变量的当前线程的“初始值”。
remove()：移除此线程局部变量当前线程的值。
set(T value)：将此线程局部变量的当前线程副本中的值设置为指定值。
除了这四个方法，ThreadLocal内部还有一个静态内部类ThreadLocalMap，该内部类才是实现线程隔离机制的关键，get()、set()、remove()都是基于该内部类操作。ThreadLocalMap提供了一种用键值对方式存储每一个线程的变量副本的方法，key为当前ThreadLocal对象，value则是对应线程的变量副本。

对于ThreadLocal需要注意的有两点：
1. ThreadLocal实例本身是不存储值，它只是提供了一个在当前线程中找到副本值得key。
2. 是ThreadLocal包含在Thread中，而不是Thread包含在ThreadLocal中，有些小伙伴会弄错他们的关系。

## 代码示例

```
package com.zch.blogs.java.multithreads;

public class ThreadLocalDemo {
	public static void main(String[] args) {
		MyThread1 t1 = new MyThread1();
		MyThread2 t2 = new MyThread2();

		Thread thread11 = new Thread(t1);
		thread11.start();
		Thread thread12 = new Thread(t1);
		thread12.start();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Thread thread21 = new Thread(t2);
		thread21.start();
		Thread thread22 = new Thread(t2);
		thread22.start();

	}

}

/**
 * @Description 用了ThreadLocal，值不会被覆盖掉。
 * @author zch
 * @time 2018年9月5日 下午5:30:35
 *
 */
class MyThread1 implements Runnable {
	private ThreadLocal<String> threadLocalName = new ThreadLocal<String>();

	public void run() {
		try {
			this.threadLocalName.set((Math.random() * 100D) + "");
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(this.threadLocalName.get());
	}
}

/**
 * @Description 错误的例子，没用ThreadLocal，值被覆盖掉
 * @author zch
 * @time 2018年9月5日 下午5:30:14
 *
 */
class MyThread2 implements Runnable {
	private String value = "";

	public void run() {
		try {
			this.value = (Math.random() * 100D) + "";
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(this.value);
	}
}
```
输出

```
92.80899644845726
78.18841987420362
19.53318872677794
19.53318872677794
```

上面的例子创建了一个MyRunnable实例，并将该实例作为参数传递给两个线程。两个线程分别执行run()方法，并且都在ThreadLocal实例上保存了不同的值。如果它们访问的不是ThreadLocal对象并且调用的set()方法被同步了，则第二个线程会覆盖掉第一个线程设置的值。但是，由于它们访问的是一个ThreadLocal对象，因此这两个线程都无法看到对方保存的值。也就是说，它们存取的是两个不同的值。

## 参考


- [Java ThreadLocal的使用](http://ifeve.com/java-threadlocal的使用/)
- [Java并发编程：深入剖析ThreadLocal](http://www.cnblogs.com/dolphin0520/p/3920407.html)
- [彻底理解ThreadLocal](https://blog.csdn.net/lufeng20/article/details/24314381)
- [原 荐 再看ThreadLocal](http://ju.outofmemory.cn/entry/368569)
