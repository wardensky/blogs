package com.zch.blogs.nosql.redis;

import org.apache.commons.lang3.StringUtils;

import redis.clients.jedis.Jedis;

public class PubClient {
	private Jedis jedis;//

	public PubClient(String host, String pass) {
		jedis = new Jedis(host, GlobalConfig.REDIS_DEFAULT_PORT);
		if (StringUtils.isNotBlank(pass)) {
			jedis.auth(pass);
		}
	}

	public void pub(String channel, String message) {
		jedis.publish(channel, message);
	}

	public void close(String channel) {
		jedis.publish(channel, "quit");
		jedis.del(channel);//
	}
}
