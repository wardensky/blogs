package com.zch.blogs.java.multithreads.pool;

public class ThreadTest1 {
	public static void main(String[] args) {
		Thread t1 = new Thread(new ThreadClass("测试"));
		t1.start();
	}

} 