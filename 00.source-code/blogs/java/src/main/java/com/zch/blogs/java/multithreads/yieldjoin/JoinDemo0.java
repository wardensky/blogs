package com.zch.blogs.java.multithreads.yieldjoin;

public class JoinDemo0 {
	public static void main(String[] args) throws InterruptedException {
		Thread1_1 t1 = new Thread1_1("aaa");
		Thread1_1 t2 = new Thread1_1("bbb");
		t1.start();
		t1.join();
		t2.start();	
	}
}

class Thread1_1 extends Thread {
	String name;

	public Thread1_1(String name) {
		this.name = name;
	}

	@Override
	public void run() {
		for (int i = 1; i < 5; i++) {
			System.out.println("I'm " + this.name + ". I'm doing something " + i);
		}

	}
}
