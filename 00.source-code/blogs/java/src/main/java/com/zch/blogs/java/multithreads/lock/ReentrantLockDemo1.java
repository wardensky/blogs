package com.zch.blogs.java.multithreads.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Description trylock的例子。
 * @author zch
 * @time 2018年9月6日 上午11:13:43
 * 
 */
public class ReentrantLockDemo1 {
	public static void main(String[] args) {
		ThreadClass1 tc1 = new ThreadClass1();
		Thread t1 = new Thread(tc1, "t1");
		Thread t2 = new Thread(tc1, "t2");
		t1.start();
		t2.start();
	}
}

class ThreadClass1 implements Runnable {

	@Override
	public void run() {
		ReentrantLockClass1.foo();
	}

}

/**
 * @Description 只是trylock没有用，不会加锁的，要lock一下。
 * @author zch
 * @time 2018年9月6日 上午11:13:53
 * 
 */
class ReentrantLockClass1 {
	static void foo() {
		Lock lock = new ReentrantLock();
		if (lock.tryLock()) {
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
}
