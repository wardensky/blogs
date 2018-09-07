package com.zch.blogs.java.multithreads.synchronizeddemo;

public class SynchronizedDemo5 {

}

/**
 * @Description 两种写法等价。
 * @author zch
 * @time 2018年9月6日 上午9:44:01
 * 
 */
class C51 {
	public synchronized void run() {
		for (int i = 0; i < 5; i++) {
			try {
				System.out.println(Thread.currentThread().getName());
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void run1() {
		synchronized (this) {
			for (int i = 0; i < 5; i++) {
				try {
					System.out.println(Thread.currentThread().getName());
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}