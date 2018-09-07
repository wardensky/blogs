package com.zch.blogs.java.multithreads.synchronizeddemo;

public class SynchronizedDemo8 {
	public void method() {
		synchronized (this) {
			System.out.println("Method 1 start");
		}
	}
}
