package com.zch.blogs.java.multithreads.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockDemo {
	public static void main(String[] args) {
		demo1();
	}

	static void demo1() {
		Lock lock = new ReentrantLock();
		lock.lock();
		try {
			for (int i = 0; i < 100; i++) {
				System.out.println(i);

			}
		} finally {
			lock.unlock();
		}
	}

	/**
	 * trylock
	 */
	static void demo2() {
		Lock lock = new ReentrantLock();
		if (lock.tryLock()) {
			try {
				for (int i = 0; i < 100; i++) {
					System.out.println(i);
				}
			} finally {
				lock.unlock();
			}
		}
	}

	/**
	 * trylock加时间的
	 */
	static void demo3() {
		Lock lock = new ReentrantLock();
		try {
			if (lock.tryLock(10, TimeUnit.SECONDS)) {
				try {
					for (int i = 0; i < 100; i++) {
						System.out.println(i);

					}
				} finally {
					lock.unlock();
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
