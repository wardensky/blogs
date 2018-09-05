package com.zch.blogs.java.multithreads.pool;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author zch <br/>
 *         手动初始化线程池
 */
public class ThreadPoolTest2 {
	public static void main(String[] args) {
		ThreadPoolExecutor ex = new ThreadPoolExecutor(2, 5, 0L, TimeUnit.MILLISECONDS,
				new LinkedBlockingQueue<Runnable>());
		ex.execute(new ThreadClass("测试newSingleThreadExecutor"));
		// 如果不手动调用，线程不会死掉。
		ex.shutdown();
	}
}
