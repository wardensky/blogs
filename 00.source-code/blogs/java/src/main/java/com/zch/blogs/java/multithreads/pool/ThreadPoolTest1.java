package com.zch.blogs.java.multithreads.pool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author zch <br/>
 *         如果不手动调用，线程不会死掉。
 */
public class ThreadPoolTest1 {
	public static void main(String[] args) {
		ExecutorService executorService = Executors.newSingleThreadExecutor();
		executorService.execute(new ThreadClass("测试newSingleThreadExecutor"));
		// 如果不手动调用，线程不会死掉。
		executorService.shutdown();
	}
}
