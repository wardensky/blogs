package com.zch.blogs.nosql.redis;

import redis.clients.jedis.JedisPubSub;

public class PrintListener extends JedisPubSub {

	@Override
	public void onMessage(String channel, String message) {
		System.out.println("message receive:" + message + ",channel:" + channel + "...");
	}

}