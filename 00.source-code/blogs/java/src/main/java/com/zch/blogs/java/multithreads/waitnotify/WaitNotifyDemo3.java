package com.zch.blogs.java.multithreads.waitnotify;

/**
 * 这个例子就是加锁了，但没有加wait和notify
 * 
 * @author zch
 *
 */
public class WaitNotifyDemo3 {

	/**
	 * 这个对象就是所有线程都用的锁。
	 */
	public static Object obj = new Object();

	public static void main(String[] args) throws InterruptedException {
		Thread0_1 t1 = new Thread0_1("Thread WaterMelon");
		Thread0_2 t2 = new Thread0_2("Thread Apple");
		t2.start();
		Thread.sleep(1);
		t1.start();
		System.out.println("完成");
	}
}

/**
 * 这个线程用来演示阻塞，等这个线程工作完，不会告诉别人。<br/>
 * 
 * @author zch <br/>
 * 
 */
class Thread3_1 extends Thread {
	String name;

	public Thread3_1(String name) {
		this.name = name;
	}

	@Override
	public void run() {
		try {
			synchronized (WaitNotifyDemo3.obj) {
				for (int i = 1; i < 5; i++) {
					Thread.sleep(1000);
					System.out.println("I'm " + this.name + ". I'm doing something " + i);
				}
			}
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}
}

/**
 * 这个类用来表示被阻塞的线程，但这个线程并没有wait，所以他一直轮询这个锁，比较占资源。<br/>
 * 
 * @author zch
 *
 */
class Thread3_2 extends Thread {
	String name;

	public Thread3_2(String name) {
		this.name = name;
	}

	@Override
	public void run() {

		System.out.println("I'm " + this.name + " . I'm waiting");
		synchronized (WaitNotifyDemo3.obj) {

			System.out.println(this.name + " Finish wait");
		}

	}
}