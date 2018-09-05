package com.zch.blogs.java.multithreads.waitnotify;

public class WaitNotifyDemo0 {

	/**
	 * 这个对象就是所有线程都用的锁。
	 */
	public static Object obj = new Object();

	public static void main(String[] args) throws InterruptedException {
		Thread0_1 t1 = new Thread0_1("Thread WaterMelon");
		Thread0_2 t2 = new Thread0_2("Thread Apple");
		t2.start();
		///这个sleep很重要。
		//Thread.sleep(1);
		//t1.start();
		System.out.println("完成");
	}
}

/**
 * 这个线程用来演示阻塞，等这个线程工作完，会通过notify方法告诉别人。<br/>
 * 
 * @author zch <br/>
 * 
 */
class Thread0_1 extends Thread {
	String name;

	public Thread0_1(String name) {
		this.name = name;
	}

	@Override
	public void run() {
		try {
			synchronized (WaitNotifyDemo0.obj) {
				for (int i = 1; i < 5; i++) {
					Thread.sleep(1000);
					System.out.println("I'm " + this.name + ". I'm doing something " + i);
				}
				WaitNotifyDemo0.obj.notify();
				System.out.println("I'm " + this.name + ".  The object notify");
			}

		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}
}

/**
 * 这个类用来表示被阻塞的线程，进入工作状态后，他可以先把锁还给别人，等别人通知他，他才继续工作。<br/>
 * 
 * @author zch
 *
 */
class Thread0_2 extends Thread {
	String name;

	public Thread0_2(String name) {
		this.name = name;
	}

	@Override
	public void run() {
		try {
			System.out.println("I'm " + this.name + " . I'm waiting");
			// WaitNotifyDemo0.obj.wait();
			synchronized (WaitNotifyDemo0.obj) {
			 	WaitNotifyDemo0.obj.wait();
				System.out.println(this.name + " Finish wait");
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
}