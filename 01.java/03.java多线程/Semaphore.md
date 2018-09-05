# Semaphore

## 简介

Semaphore（信号量）是用来控制同时访问特定资源的线程数量，它通过协调各个线程，以保证合理的使用公共资源。很多年以来，我都觉得从字面上很难理解Semaphore所表达的含义，只能把它比作是控制流量的红绿灯，比如XX马路要限制流量，只允许同时有一百辆车在这条路上行使，其他的都必须在路口等待，所以前一百辆车会看到绿灯，可以开进这条马路，后面的车会看到红灯，不能驶入XX马路，但是如果前一百辆中有五辆车已经离开了XX马路，那么后面就允许有5辆车驶入马路，这个例子里说的车就是线程，驶入马路就表示线程在执行，离开马路就表示线程执行完成，看见红灯就表示线程被阻塞，不能执行。

## 应用场景

Semaphore可以用于做流量控制，特别公用资源有限的应用场景，比如数据库连接。假如有一个需求，要读取几万个文件的数据，因为都是IO密集型任务，我们可以启动几十个线程并发的读取，但是如果读到内存后，还需要存储到数据库中，而数据库的连接数只有10个，这时我们必须控制只有十个线程同时获取数据库连接保存数据，否则会报错无法获取数据库连接。这个时候，我们就可以使用Semaphore来做流控。

## 源码

```
package com.zch.java36.lesson19;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class SemaphoreDemo {

	private static final int COUNT = 10;
	private static Semaphore s = new Semaphore(5);

	private static ExecutorService es = Executors.newFixedThreadPool(COUNT);

	public static void main(String[] args) {
		for (int i = 0; i < COUNT; i++) {
			es.execute(new Runnable() {

				@Override
				public void run() {
					try {
						s.acquire();
						Thread.sleep(2000);
						System.out.println(CountDownLatchDemo.df.format(new Date()) + " save data");
						s.release();
					} catch (InterruptedException e) {

					}
				}
			});
		}
		es.shutdown();
	}
}


```


输出

```

15:40:46 save data
15:40:46 save data
15:40:46 save data
15:40:46 save data
15:40:46 save data
15:40:48 save data
15:40:48 save data
15:40:48 save data
15:40:48 save data
15:40:48 save data


```
可以看出，虽然指定了线程池的大小是10，但因为信号量控制，所以每次最多有5个可以进入线程池。


## 参考

- [并发工具类（三）控制并发线程数的Semaphore](http://ifeve.com/concurrency-semaphore/#more-14753)
- [我在github上的源码](https://github.com/wardensky/java36_study_notes/tree/master/java36/src/main/java/com/zch/java36/lesson19)
