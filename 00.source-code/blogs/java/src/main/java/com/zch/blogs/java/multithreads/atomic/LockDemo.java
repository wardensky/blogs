package com.zch.blogs.java.multithreads.atomic;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockDemo {

	public static void main(String[] args) {
		LockDemo.run();
		Integer i = 5;
	}

	static int i = 0;
	static Lock lockObject = new ReentrantLock();

	static void run() {
		Thread t1 = new Thread() {
			public void run() {
				for (int j = 0; j < 10000; j++) {
					lockObject.lock();
					i++;
					lockObject.unlock();
				}
			}
		};
		t1.start();

		Thread t2 = new Thread() {
			public void run() {
				for (int j = 0; j < 10000; j++) {
					lockObject.lock();
					i++;
					lockObject.unlock();
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