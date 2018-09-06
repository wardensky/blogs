package com.zch.blogs.java.multithreads.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Description 普通的ReentrantLock例子。
 * @author zch
 * @time 2018年9月6日 上午11:13:08
 * 
 */
public class ReentrantLockDemo0 {
	public static void main(String[] args) {
		ThreadClass0 tc1 = new ThreadClass0();
		Thread t1 = new Thread(tc1, "t1");
		Thread t2 = new Thread(tc1, "t2");
		t1.start();
		t2.start();
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

class ThreadClass0 implements Runnable {

	@Override
	public void run() {
		ReentrantLockClass0.foo();
	}

}

class ReentrantLockClass0 {
	static void foo() {
		Lock lock = new ReentrantLock();
		lock.lock();
		try {
			for (int i = 0; i < 10; i++) {
				System.out.println(Thread.currentThread().getName() + " " + i);
			}
		} finally {
			lock.unlock();
		}
	}
}
