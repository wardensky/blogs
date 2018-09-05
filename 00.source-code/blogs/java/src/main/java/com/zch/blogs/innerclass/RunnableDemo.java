package com.zch.blogs.innerclass;

public class RunnableDemo {
	public static void main(String[] args) {
		Runnable r = new Runnable() {
			public void run() {
				System.out.print("runnable  ");
			}
		};
		Thread t = new Thread(r);
		t.start();
	}
}
