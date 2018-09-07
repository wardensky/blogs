package com.zch.blogs.java.multithreads.synchronizeddemo;

/**
 * @Description 不是一个对象，不能锁。
 * @author zch
 * @time 2018年9月6日 上午9:41:40
 * 
 */
public class SynchronizedDemo2 {
	public static void main(String[] args) {
		Thread t1 = new Thread(new Class2(), "t1");
		Thread t2 = new Thread(new Class2(), "t2");
		t1.start();
		t2.start();
	}
}

class Class2 implements Runnable {

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