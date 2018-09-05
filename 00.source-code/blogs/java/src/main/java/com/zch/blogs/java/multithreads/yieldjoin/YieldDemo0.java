package com.zch.blogs.java.multithreads.yieldjoin;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public class YieldDemo0 {
	private ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();

	public static void main(String[] args) {

		Thread0_1 t1 = new Thread0_1("aaa");
		Thread0_2 t2 = new Thread0_2("bbb");
		t1.start();
		t2.start();
	}
}

class Thread0_1 extends Thread {

	public Thread0_1(String name) {
		this.setName(name);

	}

	@Override
	public void run() {

		for (int i = 1; i < 5; i++) {
			System.out.println("I'm " + this.getName() + ". I'm doing something " + i);
			Thread.yield();
		}

	}
}

class Thread0_2 extends Thread {

	public Thread0_2(String name) {
		this.setName(name);
	}

	@Override
	public void run() {

		for (int i = 1; i < 5; i++) {
			System.out.println("I'm " + this.getName() + ". I'm doing something " + i);
			// Thread.yield();
		}

	}
}
