package com.zch.blogs.java.multithreads.lock;

/**
 * @Description 在类上加锁，跟demo6里面是一样的。
 * @author zch
 * @time 2018年9月6日 上午9:50:10
 * 
 */
public class SynchronizedDemo7 {

}

class C7 implements Runnable {
	private static int count;

	public C7() {
		count = 0;
	}

	public static void method() {
		synchronized (C7.class) {
			for (int i = 0; i < 5; i++) {
				try {
					System.out.println(Thread.currentThread().getName() + ":" + (count++));
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public synchronized void run() {
		method();
	}
}