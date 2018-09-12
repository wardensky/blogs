# synchronized基本用法


## 简介

synchronized是Java中的关键字，是一种同步锁。它修饰的对象有以下几种：
- 修饰一个代码块，被修饰的代码块称为同步语句块，其作用的范围是大括号{}括起来的代码，作用的对象是调用这个代码块的对象；
- 修饰一个方法，被修饰的方法称为同步方法，其作用的范围是整个方法，作用的对象是调用这个方法的对象；
- 修改一个静态的方法，其作用的范围是整个静态方法，作用的对象是这个类的所有对象；
- 修改一个类，其作用的范围是synchronized后面括号括起来的部分，作用主的对象是这个类的所有对象。


## 源码

### 修饰一个方法

修饰一个方法用的特别多，很多java源码里面为了保证同步，都是采用这样的方法，简单方便。

```

package com.zch.blogs.java.multithreads.lock;

public class SynchronizedDemo5 {

}

/**
 * @Description 两种写法等价。
 * @author zch
 * @time 2018年9月6日 上午9:44:01
 *
 */
class C51 {
	public synchronized void run() {
		for (int i = 0; i < 5; i++) {
			try {
				System.out.println(Thread.currentThread().getName());
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void run1() {
		synchronized (this) {
			for (int i = 0; i < 5; i++) {
				try {
					System.out.println(Thread.currentThread().getName());
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}

```


### 修饰一个代码块

```
package com.zch.blogs.java.multithreads.lock;

/**
 * @Description 最常见的用法之一，锁在对象上。
 * @author zch
 * @time 2018年9月6日 上午9:41:12
 *
 */
public class SynchronizedDemo1 {
	public static void main(String[] args) {
		Class1 c1 = new Class1();
		Thread t1 = new Thread(c1, "t1");
		Thread t2 = new Thread(c1, "t2");
		t1.start();
		t2.start();
	}
}

class Class1 implements Runnable {

	@Override
	public void run() {
		synchronized (this) {
			for (int i = 0; i < 5; i++) {
				try {
					Thread.sleep(1000);
					System.out.println(Thread.currentThread().getName());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
```

比较好的写法：

```
package com.zch.blogs.java.multithreads.lock;

/**
 * @Description 零长度的byte数组对象创建起来将比任何对象都经济――查看编译后的字节码：生成零长度的byte[]对象只需3条操作码，而Object
 *              lock = new Object()则需要7行操作码。
 *
 *
 * @author zch
 * @time 2018年9月6日 上午9:40:18
 *
 */
public class SynchronizedDemo4 implements Runnable {
	private byte[] lock = new byte[0];

	public void method() {
		synchronized (lock) {

		}
	}

	public void run() {

	}

}

```


### 修饰一个静态方法

```
package com.zch.blogs.java.multithreads.lock;

import com.zch.blogs.java.base.TimeUtil;

/**
 * @Description 修饰一个静态的方法，即使是两个对象，也可以同步。
 * @author zch
 * @time 2018年9月6日 上午9:46:25
 *
 */
public class SynchronizedDemo6 {
	public static void main(String[] args) {
		Thread thread1 = new Thread(new C6(), "SyncThread1");
		Thread thread2 = new Thread(new C6(), "SyncThread2");
		thread1.start();
		thread2.start();
	}
}

class C6 implements Runnable {
	private static int count;

	public C6() {
		count = 0;
	}

	public synchronized static void method() {
		for (int i = 0; i < 5; i++) {
			try {
				System.out.println(TimeUtil.getShortTime() + Thread.currentThread().getName() + ":" + (count++));
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public synchronized void run() {
		method();
	}
}
```


### 修饰一个类

```
package com.zch.blogs.java.multithreads.lock;

/**
 * @Description 在类上加锁，跟demo6里面是一样的。
 * @author zch
 * @time 2018年9月6日 上午9:50:10
 *
 */
public class SynchronizedDemo7 {

}

class C7 implements Runnable {
	private static int count;

	public C7() {
		count = 0;
	}

	public static void method() {
		synchronized (C7.class) {
			for (int i = 0; i < 5; i++) {
				try {
					System.out.println(Thread.currentThread().getName() + ":" + (count++));
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public synchronized void run() {
		method();
	}
}
```



## 总结

- 无论synchronized关键字加在方法上还是对象上，如果它作用的对象是非静态的，则它取得的锁是对象；如果synchronized作用的对象是一个静态方法或一个类，则它取得的锁是对类，该类所有的对象同一把锁。
- 每个对象只有一个锁（lock）与之相关联，谁拿到这个锁谁就可以运行它所控制的那段代码。
- 实现同步是要很大的系统开销作为代价的，甚至可能造成死锁，所以尽量避免无谓的同步控制。

## 参考

- [Java中synchronized的用法](http://www.importnew.com/21866.html)
- [Java并发编程：synchronized](http://www.importnew.com/18523.html)
- [我在github的源码](https://github.com/wardensky/blogs/tree/master/00.source-code/blogs/java)
