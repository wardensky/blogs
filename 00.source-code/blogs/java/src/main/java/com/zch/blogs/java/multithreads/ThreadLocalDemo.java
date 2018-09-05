package com.zch.blogs.java.multithreads;

public class ThreadLocalDemo {
	public static void main(String[] args) {
		MyThread1 t1 = new MyThread1();
		MyThread2 t2 = new MyThread2();

		Thread thread11 = new Thread(t1);
		thread11.start();
		Thread thread12 = new Thread(t1);
		thread12.start();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Thread thread21 = new Thread(t2);
		thread21.start();
		Thread thread22 = new Thread(t2);
		thread22.start();

	}

}

/**
 * @Description 用了ThreadLocal，值不会被覆盖掉。
 * @author zch
 * @time 2018年9月5日 下午5:30:35
 * 
 */
class MyThread1 implements Runnable {
	private ThreadLocal<String> threadLocalName = new ThreadLocal<String>();

	public void run() {
		try {
			this.threadLocalName.set((Math.random() * 100D) + "");
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(this.threadLocalName.get());
	}
}

/**
 * @Description 错误的例子，没用ThreadLocal，值被覆盖掉
 * @author zch
 * @time 2018年9月5日 下午5:30:14
 * 
 */
class MyThread2 implements Runnable {
	private String value = "";

	public void run() {
		try {
			this.value = (Math.random() * 100D) + "";
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(this.value);
	}
}