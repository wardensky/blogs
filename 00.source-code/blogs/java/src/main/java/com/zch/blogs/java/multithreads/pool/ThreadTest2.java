package com.zch.blogs.java.multithreads.pool;

public class ThreadTest2 {
	public static void main(String[] args) {
		Thread t1 = new Thread(new RunnableClass("测试"));
		t1.start();
	}

}
