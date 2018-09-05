package com.zch.blogs.java.multithreads.waitnotify;

public class WaitNotifyDemo2 {
	public static Object obj = new Object();

	public static void main(String[] args) {
		Thread2_1 t1 = new Thread2_1();
		Thread2_2 t2 = new Thread2_2("Thread Apple");
		Thread2_2 t3 = new Thread2_2("Thread Orange");

		t2.start();
		t3.start();
		try {
			// 必须sleep一下
			Thread.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		t1.start();
		System.out.println("完成");
	}
}

class Thread2_1 extends Thread {
	@Override
	public void run() {
		try {
			synchronized (WaitNotifyDemo2.obj) {
				for (int i = 1; i < 5; i++) {
					Thread.sleep(1000);
					System.out.println("I'm thread111. I'm doing something " + i);
				}
				WaitNotifyDemo2.obj.notifyAll();
				System.out.println("I'm thread111. The object notify");
			}

		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}
}

class Thread2_2 extends Thread {

	String name;

	public Thread2_2(String name) {
		this.name = name;
	}

	@Override
	public void run() {
		try {
			System.out.println("I'm " + this.name + " . I'm waiting");
			synchronized (WaitNotifyDemo2.obj) {
				WaitNotifyDemo2.obj.wait();
				System.out.println( this.name +" Finish wait");
				//如果不加这句话，就叫不醒别人了。
			 	//WaitNotifyDemo.obj.notify();
			}

		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}
}