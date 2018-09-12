package com.zch.blogs.java.jvm;

public class StackOverflowTest {
	private int stackLength = 1;

	public void stackLeak() {
		stackLength++;
		stackLeak();
	}

	public static void main(String[] args) throws Throwable {
		StackOverflowTest stackOverflow = new StackOverflowTest();
		try {
			stackOverflow.stackLeak();
		} catch (Throwable e) {
			System.out.println("stack length:" + stackOverflow.stackLength);
			throw e;
		}
	}
}
