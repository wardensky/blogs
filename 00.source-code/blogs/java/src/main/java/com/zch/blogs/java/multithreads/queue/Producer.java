package com.zch.blogs.java.multithreads.queue;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Producer implements Runnable {

	public Producer(BlockingQueue<String> queue, String name) {
		this.queue = queue;
		this.name = name;
	}

	public void run() {
		String data = null;
		Random r = new Random();

		System.out.println("启动生产者线程！" + this.name);
		try {
			while (isRunning) {
				System.out.println(this.name + "正在生产数据...");
				Thread.sleep(r.nextInt(DEFAULT_RANGE_FOR_SLEEP));

				data = "data:" + count.incrementAndGet();
				System.out.println(this.name + "将数据：" + data + "放入队列...");
				if (!queue.offer(data, 2, TimeUnit.SECONDS)) {
					System.out.println(this.name + "放入数据失败：" + data);
				}
				System.out.println(this.name + "当前队列大小 = " + this.queue.size());
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
			Thread.currentThread().interrupt();
		} finally {
			System.out.println(this.name + "退出生产者线程！");
		}
	}

	public void stop() {
		isRunning = false;
	}

	private String name;
	private volatile boolean isRunning = true;
	private BlockingQueue<String> queue;
	private static AtomicInteger count = new AtomicInteger();
	private static final int DEFAULT_RANGE_FOR_SLEEP = 1000;

}