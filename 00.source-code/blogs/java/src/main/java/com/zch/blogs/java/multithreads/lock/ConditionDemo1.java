package com.zch.blogs.java.multithreads.lock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ConditionDemo1 {
	public static void main(String[] args) {
		BoundedBuffer boundedBuffer = new BoundedBuffer();
		// 创建3个put线程，每个往Buffer里put 3次
		BufferThread put1 = new BufferThread(boundedBuffer, "PUT1");
		BufferThread put2 = new BufferThread(boundedBuffer, "PUT2");
		BufferThread put3 = new BufferThread(boundedBuffer, "PUT3");

		// 创建2个take线程，每个从Buffer里take 3次
		BufferThread take1 = new BufferThread(boundedBuffer, "TAKE1");
		BufferThread take2 = new BufferThread(boundedBuffer, "TAKE2");

		put1.start();
		put2.start();
		put3.start();
		take1.start();
		take2.start();
	}

}

class BoundedBuffer {
	final ReentrantLock lock = new ReentrantLock();
	// notFull 才能put
	final Condition notFull = lock.newCondition();
	// notEmpty 才能take
	final Condition notEmpty = lock.newCondition();

	final int[] items = new int[2];
	int putptr, takeptr, count;

	public void put(int x) throws InterruptedException {

		lock.lock();
		try {

			while (count == items.length) {
				System.out.printf("----FULL---- The buffer is full!  %s has to wait.\n",
						Thread.currentThread().getName());
				notFull.await();
			}

			// 每次只要put成功，则通知一下 notEmpty，如果存在等待take的线程，则唤醒一个让它取
			items[putptr] = x;
			if (++putptr == items.length) {
				putptr = 0;
			}
			++count;
			notEmpty.signal();
		} finally {
			lock.unlock();
		}
	}

	public int take() throws InterruptedException {
		lock.lock();
		try {
			while (count == 0) {
				System.out.printf("----EMPTY---- The buffer is empty!  %s has to wait.\n",
						Thread.currentThread().getName());
				notEmpty.await();
			}
			// 每次take成功，则通知 notFull，如果有等待put的线程，则让它放
			int x = items[takeptr];
			if (++takeptr == items.length)
				takeptr = 0;
			--count;
			notFull.signal();
			return x;
		} finally {
			lock.unlock();
		}
	}
}

class BufferThread extends Thread {

	private BoundedBuffer boundedBuffer = new BoundedBuffer();
	private String name;

	public BufferThread(BoundedBuffer boundedBuffer, String name) {
		super(name);
		this.boundedBuffer = boundedBuffer;
		this.name = name;
	}

	@Override
	public void run() {
		super.run();
		System.out.println(Thread.currentThread().getName() + " is running!");
		if (name.startsWith("PUT")) {
			for (int i = 1; i < 4; i++) {
				try {
					boundedBuffer.put(i);
					System.out.printf("--PUT-- %s has put %d into the buffer.\n", Thread.currentThread().getName(), i);
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} else if (name.startsWith("TAKE")) {
			for (int i = 1; i < 4; i++) {
				try {
					int value = boundedBuffer.take();
					System.out.printf("--TAK-- %s has took %d from the buffer.\n", Thread.currentThread().getName(),
							value);
					Thread.sleep(400);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		}

	}
}