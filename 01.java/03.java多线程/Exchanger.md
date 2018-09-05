# Exchanger


## 简介

简介

Exchanger（交换者）是一个用于线程间协作的工具类。Exchanger用于进行线程间的数据交换。它提供一个同步点，在这个同步点两个线程可以交换彼此的数据。这两个线程通过exchange方法交换数据， 如果第一个线程先执行exchange方法，它会一直等待第二个线程也执行exchange，当两个线程都到达同步点时，这两个线程就可以交换数据，将本线程生产出来的数据传递给对方。

Exchanger的应用场景

Exchanger可以用于遗传算法，遗传算法里需要选出两个人作为交配对象，这时候会交换两人的数据，并使用交叉规则得出2个交配结果。
Exchanger也可以用于校对工作。比如我们需要将纸制银流通过人工的方式录入成电子银行流水，为了避免错误，采用AB岗两人进行录入，录入到Excel之后，系统需要加载这两个Excel，并对这两个Excel数据进行校对，看看是否录入的一致。

## 源码

```
package com.zch.java36.lesson19;

import java.util.concurrent.Exchanger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExchangerDemo {
	private static final Exchanger<String> exgr = new Exchanger<String>();
	private static ExecutorService threadPool = Executors.newFixedThreadPool(2);

	public static void main(String[] args) {
		threadPool.execute(new Task5(exgr));
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		threadPool.execute(new Task6(exgr));
		threadPool.shutdown();
	}
}

class Task5 extends Thread {
	private Exchanger<String> exchanger;

	public Task5(Exchanger<String> latch) {
		this.exchanger = latch;
	}

	@Override
	public void run() {
		try {
			String A = "我是task5";
			String value = exchanger.exchange(A);
			System.out.println("task5 value = " + value);
		} catch (Exception ex) {
		} finally {

		}
	}

}

class Task6 extends Thread {
	private Exchanger<String> exchanger;

	public Task6(Exchanger<String> latch) {
		this.exchanger = latch;
	}

	@Override
	public void run() {
		try {
			String A = "我是task6啊啊啊啊";

			String value = exchanger.exchange(A);

			System.out.println("task6 value = " + value);
		} catch (Exception ex) {
		} finally {

		}
	}
}
```

输出

```
task6 value = 我是task5
task5 value = 我是task6啊啊啊啊

```

可以看出，两个线程交换了值。

## 参考

- [并发工具类（四）两个线程进行数据交换的Exchanger](http://ifeve.com/concurrency-exchanger/)
- [我在github上的源码](https://github.com/wardensky/java36_study_notes/tree/master/java36/src/main/java/com/zch/java36/lesson19)
