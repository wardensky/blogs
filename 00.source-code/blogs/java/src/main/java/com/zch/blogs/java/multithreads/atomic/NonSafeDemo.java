package com.zch.blogs.java.multithreads.atomic;

public class NonSafeDemo {
	static int i = 0;

	public static void main(String[] args) {
		NonSafeDemo.run();
	}

	static void run() {
		Thread t1 = new Thread() {
			public void run() {
				for (int j = 0; j < 10000; j++) {
					i++;
				}
			}
		};
		t1.start();

		Thread t2 = new Thread() {
			public void run() {
				for (int j = 0; j < 10000; j++) {
					i++;
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
