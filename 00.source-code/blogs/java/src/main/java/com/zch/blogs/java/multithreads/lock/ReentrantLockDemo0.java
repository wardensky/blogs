package com.zch.blogs.java.multithreads.lock;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @Description 普通的ReentrantLock例子。
 * @author zch
 * @time 2018年9月6日 上午11:13:08
 * 
 */
public class ReentrantLockDemo0 {
	public final static ReentrantLock lock = new ReentrantLock();

	public static void main(String[] args) {
		ThreadClass0 tc1 = new ThreadClass0();
		Thread t1 = new Thread(tc1, "t1");
		Thread t2 = new Thread(tc1, "t2");
		Thread t3 = new Thread(tc1, "t3");
		Thread t4 = new Thread(tc1, "t4");
		Thread t5 = new Thread(tc1, "t5");

		t1.start();
		t2.start();
		t3.start();
		t4.start();
		t5.start();

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

		ReentrantLockDemo0.lock.lock();
		try {
			for (int i = 0; i < 3; i++) {
				Thread.sleep(10);
				System.out.println(Thread.currentThread().getName() + " " + i);
			}
			int count = ReentrantLockDemo0.lock.getHoldCount();
			int queuedLength = ReentrantLockDemo0.lock.getQueueLength();
			System.out.println(Thread.currentThread().getName() + " getHoldCount = " + count);
			System.out.println(Thread.currentThread().getName() + " getQueueLength = " + queuedLength);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			ReentrantLockDemo0.lock.unlock();
		}
	}
}
