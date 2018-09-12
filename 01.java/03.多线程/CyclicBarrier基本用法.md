# CyclicBarrier基本用法

## 解释

CyclicBarrier 的字面意思是可循环使用（Cyclic）的屏障（Barrier）。它要做的事情是，让一组线程到达一个屏障（也可以叫同步点）时被阻塞，**直到最后一个线程到达屏障时，屏障才会开门，所有被屏障拦截的线程才会继续干活**。CyclicBarrier默认的构造方法是CyclicBarrier(int parties)，其参数表示屏障拦截的线程数量，每个线程调用await方法告诉CyclicBarrier我已经到达了屏障，然后当前线程被阻塞。


CyclicBarrier的应用场景

CyclicBarrier可以用于多线程计算数据，最后合并计算结果的应用场景。比如我们用一个Excel保存了用户所有银行流水，每个Sheet保存一个帐户近一年的每笔银行流水，现在需要统计用户的日均银行流水，先用多线程处理每个sheet里的银行流水，都执行完之后，得到每个sheet的日均银行流水，最后，再用barrierAction用这些线程的计算结果，计算出整个Excel的日均银行流水。




## 源码

```
package com.zch.java36.lesson19;

import java.util.Date;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class CyclicBarrierDemo {
	private static final CyclicBarrier cb = new CyclicBarrier(3);

	public static void main(String[] args) {
		Task3 t3 = new Task3(cb);
		Task4 t4 = new Task4(cb);
		t3.start();
		t4.start();
		try {
			cb.await();
		} catch (InterruptedException | BrokenBarrierException e) {
			e.printStackTrace();
		}
		System.out.println(CountDownLatchDemo.df.format(new Date()) + " 所有任务完成");
	}
}

class Task3 extends Thread {
	private CyclicBarrier barrier;

	public Task3(CyclicBarrier latch) {
		this.barrier = latch;
	}

	@Override
	public void run() {
		try {
			Thread.sleep(1000);
			this.barrier.await();
		} catch (Exception ex) {
		} finally {
			System.out.println(CountDownLatchDemo.df.format(new Date()) + " task3完成");

		}
	}

}

class Task4 extends Thread {
	private CyclicBarrier barrier;

	public Task4(CyclicBarrier latch) {
		this.barrier = latch;
	}

	@Override
	public void run() {
		try {
			Thread.sleep(3000);
			this.barrier.await();
		} catch (Exception ex) {
		} finally {
			System.out.println(CountDownLatchDemo.df.format(new Date()) + " task4完成");

		}
	}
}
```


输出

```
15:33:29 task4完成
15:33:29 所有任务完成
15:33:29 task3完成
```
所有的一起输出。

## CyclicBarrier和CountDownLatch的区别

- CountDownLatch的计数器只能使用一次。而CyclicBarrier的计数器可以使用reset() 方法重置。所以CyclicBarrier能处理更为复杂的业务场景，比如如果计算发生错误，可以重置计数器，并让线程们重新执行一次。
- CyclicBarrier还提供其他有用的方法，比如getNumberWaiting方法可以获得CyclicBarrier阻塞的线程数量。isBroken方法用来知道阻塞的线程是否被中断。比如以下代码执行完之后会返回true。
- CountDownLatch 是不可以重置的，所以无法重用；而 CyclicBarrier 则没有这种限制，可以重用。
- CountDownLatch 的基本操作组合是 countDown/await。调用 await 的线程阻塞等待 countDown 足够的次数，不管你是在一个线程还是多个线程里 countDown，只要次数足够即可。所以就像 Brain Goetz 说过的，CountDownLatch 操作的是事件。
- CyclicBarrier 的基本操作组合，则就是 await，当所有的伙伴（parties）都调用了 await，才会继续进行任务，并自动进行重置。注意，正常情况下，CyclicBarrier 的重置都是自动发生的，如果我们调用 reset 方法，但还有线程在等待，就会导致等待线程被打扰，抛出 BrokenBarrierException 异常。CyclicBarrier 侧重点是线程，而不是调用事件，它的典型应用场景是用来等待并发线程结束。
## 参考

- [并发工具类（二）同步屏障CyclicBarrier](http://ifeve.com/concurrency-cyclicbarrier/)
- [我在github上的源码](https://github.com/wardensky/java36_study_notes/tree/master/java36/src/main/java/com/zch/java36/lesson19)
