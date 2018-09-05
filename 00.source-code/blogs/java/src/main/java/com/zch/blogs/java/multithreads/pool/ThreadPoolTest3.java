package com.zch.blogs.java.multithreads.pool;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * @author zch <br/>
 *         定时执行任务的初始化线程池
 */
public class ThreadPoolTest3 {
	public static void main(String[] args) {
		ScheduledThreadPoolExecutor ex = new ScheduledThreadPoolExecutor(1, new ThreadFactory() {
			 
			public Thread newThread(Runnable r) {
				Thread t = new Thread(r);
				t.setDaemon(true);
				return t;
			}
		});

		ex.scheduleAtFixedRate(new RunnableClass("heart beat"), 0, 1, TimeUnit.SECONDS);
//		for (;;) {
//			try {
//				Thread.sleep(100);
//			} catch (Exception e1x) {
//			}
//		}
	}
}
