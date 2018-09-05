package com.zch.blogs.java.multithreads.pool;

public class ThreadClass extends Thread {
	int count = 0;
	String name = "";

	public ThreadClass(String name) {
		this.name = name;
	}

	@Override
	public void run() {
		System.out.println(name + count++);
	}

}