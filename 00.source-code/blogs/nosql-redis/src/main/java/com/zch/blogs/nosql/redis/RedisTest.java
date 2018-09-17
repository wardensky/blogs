package com.zch.blogs.nosql.redis;

import redis.clients.jedis.JedisPubSub;

public class RedisTest {
	public static void main(String[] args) throws InterruptedException {
		Thread thread = new Thread(new TestThread());
		thread.start();
		System.out.println("start listener");
		for (int i = 0; i < 10; i++) {
			testPub(i);
			Thread.sleep(10);
		}

	}

	private static void testPub(int i) {

		PubClient client = new PubClient("127.0.0.1", "zch");

		client.pub("123", "adsfasfa" + i * 10000);
		System.out.println("publish");
	}
}

class TestThread implements Runnable {

	public void run() {
		TestThread.testSub();
	}

	private static void testSub() {
		SubClient subclient = new SubClient("127.0.0.1", "zch");
		JedisPubSub listener = new PrintListener();
		subclient.sub(listener, "123");
	}

}
