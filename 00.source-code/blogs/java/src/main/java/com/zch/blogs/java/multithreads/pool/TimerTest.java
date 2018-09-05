package com.zch.blogs.java.multithreads.pool;

import java.util.Timer;

/**
 * @author zch
 * Timer的缺点：多个timer是一个线程，这样的话多个任务不能保证执行成功。
 */
public class TimerTest {
	public static void main(String[] args) {
		Timer t1 = new Timer();
		t1.scheduleAtFixedRate(new TimerTaskClass("测试"), 0, 1000);

	}
}
 