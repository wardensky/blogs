# countDownLatch

## 介绍

CountDownLatch类位于java.util.concurrent包下，利用它可以实现类似计数器的功能。比如有一个任务A，它要等待其他4个任务执行完毕之后才能执行，此时就可以利用CountDownLatch来实现这种功能了。

CountDownLatch类只提供了一个构造器：

```
public CountDownLatch(int count) {  };  //参数count为计数值
```

从下图也可以看出来

![](./images/countdownlatch.png)

## 源码

```
package com.zch.java36.lesson19;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

public class CountDownLatchDemo {
	public static SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");

	public static void main(String[] args) {
		CountDownLatch latch = new CountDownLatch(2);
		Task1 t1 = new Task1(latch);
		Task2 t2 = new Task2(latch);
		t1.start();
		t2.start();

		try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.println(df.format(new Date()) + " 所有任务完成");
	}

}

class Task1 extends Thread {
	private CountDownLatch latch;

	public Task1(CountDownLatch latch) {
		this.latch = latch;
	}

	@Override
	public void run() {
		try {
			Thread.sleep(1000);

		} catch (Exception ex) {
		} finally {
			System.out.println(CountDownLatchDemo.df.format(new Date()) + " task1完成");
			this.latch.countDown();
		}
	}

}

class Task2 extends Thread {
	private CountDownLatch latch;

	public Task2(CountDownLatch latch) {
		this.latch = latch;
	}

	@Override
	public void run() {
		try {
			Thread.sleep(3000);

		} catch (Exception ex) {
		} finally {
			System.out.println(CountDownLatchDemo.df.format(new Date()) + " task2完成");
			this.latch.countDown();
		}
	}
}
```
输出

```

15:46:17 task1完成
15:46:19 task2完成
15:46:19 所有任务完成

```
可以看出，所有任务完成在等待前两个的完成，即countdown要到0.

## 参考

- [并发工具类（一）等待多线程完成的CountDownLatch](http://ifeve.com/talk-concurrency-countdownlatch/)
- [什么时候使用CountDownLatch](http://www.importnew.com/15731.html)
- [Java并发编程：CountDownLatch、CyclicBarrier和Semaphore](http://www.cnblogs.com/dolphin0520/p/3920397.html)
- [我在github上的源码](https://github.com/wardensky/java36_study_notes/tree/master/java36/src/main/java/com/zch/java36/lesson19)
