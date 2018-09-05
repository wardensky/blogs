package com.zch.blogs.java.multithreads.simple;

public class Thread2 extends Thread {

	@Override
	public void run() {
		System.out.println("this thread will never die.");
		for (;;) {
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("I'm alive");
		}
	}
}
