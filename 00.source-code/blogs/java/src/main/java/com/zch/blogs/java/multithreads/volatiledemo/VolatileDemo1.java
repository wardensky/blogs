package com.zch.blogs.java.multithreads.volatiledemo;

import java.util.concurrent.CountDownLatch;

public class VolatileDemo1 extends Thread {
	private static int count = 0;
	private static final int SIZE = 10000;
	private static CountDownLatch latch = new CountDownLatch(SIZE);

	public void run() {
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		synchronized (VolatileDemo1.class) {
			count++;
			latch.countDown();
		}
	}

	public static void main(String[] args) {

		Thread threads[] = new Thread[SIZE];
		for (int i = 0; i < threads.length; i++) {
			threads[i] = new VolatileDemo1();
			threads[i].start();
		}

		try {
			latch.countDown();
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(count);

	}
}
