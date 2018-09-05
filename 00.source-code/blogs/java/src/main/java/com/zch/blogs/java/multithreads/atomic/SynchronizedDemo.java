package com.zch.blogs.java.multithreads.atomic;

public class SynchronizedDemo {
	public static void main(String[] args) {
		SynchronizedDemo.run();
	}

	static volatile int i = 0;

	static void run() {
		Thread t1 = new Thread() {
			public void run() {
				synchronized (this) {
					for (int j = 0; j < 10000; j++) {
						i++;
					}
				}
			}
		};
		t1.start();
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Thread t2 = new Thread() {
			public void run() {
				synchronized (this) {
					for (int j = 0; j < 10000; j++) {
						i++;
					}
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
