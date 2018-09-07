package com.zch.blogs.java.multithreads.synchronizeddemo;

import com.zch.blogs.java.base.TimeUtil;

/**
 * @Description 修饰一个静态的方法，即使是两个对象，也可以同步。
 * @author zch
 * @time 2018年9月6日 上午9:46:25
 * 
 */
public class SynchronizedDemo6 {
	public static void main(String[] args) {
		Thread thread1 = new Thread(new C6(), "SyncThread1");
		Thread thread2 = new Thread(new C6(), "SyncThread2");
		thread1.start();
		thread2.start();
	}
}

class C6 implements Runnable {
	private static int count;

	public C6() {
		count = 0;
	}

	public synchronized static void method() {
		for (int i = 0; i < 5; i++) {
			try {
				System.out.println(TimeUtil.getShortTime() + Thread.currentThread().getName() + ":" + (count++));
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public synchronized void run() {
		method();
	}
}