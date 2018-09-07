package com.zch.blogs.java.multithreads.synchronizeddemo;

/**
 * @Description 最常见的用法之一，锁在对象上。
 * @author zch
 * @time 2018年9月6日 上午9:41:12
 * 
 */
public class SynchronizedDemo1 {
	public static void main(String[] args) {
		Class1 c1 = new Class1();
		Thread t1 = new Thread(c1, "t1");
		Thread t2 = new Thread(c1, "t2");
		t1.start();
		t2.start();
	}
}

class Class1 implements Runnable {

	@Override
	public void run() {
		synchronized (this) {
			for (int i = 0; i < 5; i++) {
				try {
					Thread.sleep(1000);
					System.out.println(Thread.currentThread().getName());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}