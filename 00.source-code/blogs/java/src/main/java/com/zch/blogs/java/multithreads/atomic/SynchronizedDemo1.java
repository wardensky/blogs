package com.zch.blogs.java.multithreads.atomic;

public class SynchronizedDemo1 {
	public static void main(String[] args) {

		SynchronizedDemo1.run();

	}

	static int i = 0;

	static void run() {
		Thread t1 = new Thread() {
			public void run() {
				for (int j = 0; j < 10000; j++) {

					synchronized (this) {
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

				for (int j = 0; j < 10000; j++) {
					synchronized (this) {
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
