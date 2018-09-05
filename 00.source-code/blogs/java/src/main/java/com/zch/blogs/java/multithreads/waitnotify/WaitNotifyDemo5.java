package com.zch.blogs.java.multithreads.waitnotify;

public class WaitNotifyDemo5 {

	/**
	 * 这个对象就是所有线程都用的锁。
	 */
	public static Object obj = new Object();

	public static void main(String[] args) throws InterruptedException {
		Thread4_1 t1 = new Thread4_1("Thread WaterMelon");
		Thread4_2 t2 = new Thread4_2("Thread Apple");
		t2.start();
		Thread.sleep(1);
		t1.start();
		System.out.println("完成");
	}
}

/**
 * 这个线程用来演示阻塞，等这个线程工作完，会通过notify方法告诉别人。<br/>
 * 
 * @author zch <br/>
 * 
 */
class Thread5_1 extends Thread {
	String name;

	public Thread5_1(String name) {
		this.name = name;
	}

	@Override
	public void run() {
		try {
			synchronized (WaitNotifyDemo5.obj) {
				for (int i = 1; i < 5; i++) {
					Thread.sleep(1000);
					System.out.println("I'm " + this.name + ". I'm doing something " + i);
				}
//				WaitNotifyDemo5.obj.notify();
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
class Thread5_2 extends Thread {
	String name;

	public Thread5_2(String name) {
		this.name = name;
	}

	@Override
	public void run() {
		System.out.println("I'm " + this.name + " . I'm waiting");
		try {
			synchronized (WaitNotifyDemo5.obj) {		
				WaitNotifyDemo5.obj.wait(1000 * 1);
				System.out.println(this.name + " Finish wait");
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
}