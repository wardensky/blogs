package com.zch.blogs.java.multithreads.simple;

public class Thread3 extends Thread {
	private Object obj;

	@Override
	public void run() {

		try {
			synchronized (this.obj) {
				System.out.println("开始------wait time = " + System.currentTimeMillis());
				obj.wait();
				System.out.println("结束------wait time = " + System.currentTimeMillis());
			}
			this.wait();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}
}
