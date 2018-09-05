package com.zch.blogs.java.multithreads.atomic;

import java.util.concurrent.atomic.AtomicInteger;

public class AtomicDemo {

	public static void main(String[] args) {
		AtomicDemo.run();
	}

	static AtomicInteger i = new AtomicInteger(0);

	static void run() {
		Thread t1 = new Thread() {
			public void run() {
				for (int j = 0; j < 10000; j++) {
					i.incrementAndGet();
				}
			}
		};
		t1.start();

		Thread t2 = new Thread() {
			public void run() {
				for (int j = 0; j < 10000; j++) {
					i.incrementAndGet();
				}
			}
		};
		t2.start();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(i);
	}
}
