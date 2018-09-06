package com.zch.blogs.java.multithreads.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockDemo2 {
	public static void main(String[] args) {
		ThreadClass2 tc1 = new ThreadClass2();
		Thread t1 = new Thread(tc1, "t1");
		Thread t2 = new Thread(tc1, "t2");
		t1.start();
		t2.start();
	}
}

class ThreadClass2 implements Runnable {

	@Override
	public void run() {
		ReentrantLockClass2.foo();
	}

}

/**
 * @Description 可以增加等待时间。如果锁在等待的时间内空闲，且线程没有被中断，则可以获取锁。
 * @author zch
 * @time 2018年9月6日 上午11:13:53
 * 
 */
class ReentrantLockClass2 {
	static void foo() {
		Lock lock = new ReentrantLock();
		try {
			if (lock.tryLock(10, TimeUnit.SECONDS)) {
				lock.lock();
				for (int i = 0; i < 10; i++) {
					System.out.println(Thread.currentThread().getName() + " " + i);
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}
}
