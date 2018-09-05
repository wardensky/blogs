package com.zch.blogs.java.multithreads.simple;

public class Thread1 extends Thread{

	@Override
	public void run() {
		System.out.println("this thread will die.");
	}
}
