package com.zch.blogs.java.multithreads.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadWriteLockDemo {
	static ReadWriteLock lock = new ReentrantReadWriteLock();

	public static void main(String[] args) {

	}

	static void writeDemo() {
		Lock writeLock = lock.writeLock();
		writeLock.lock();
		try {
			for (int i = 0; i < 100; i++) {
				System.out.println("write something");
			}
		} finally {
			writeLock.unlock();
		}
	}

	static void readDemo() {
		Lock readLock = lock.readLock();
		readLock.lock();
		try {
			for (int i = 0; i < 100; i++) {
				System.out.println("write something");
			}
		} finally {
			readLock.unlock();
		}
	}

}
