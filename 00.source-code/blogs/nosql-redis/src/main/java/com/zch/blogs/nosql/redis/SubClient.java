package com.zch.blogs.nosql.redis;

import org.apache.commons.lang3.StringUtils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

public class SubClient {
	private Jedis jedis;

	public SubClient(String host, String pass) {
		jedis = new Jedis(host, GlobalConfig.REDIS_DEFAULT_PORT);
		if (StringUtils.isNotBlank(pass)) {
			jedis.auth(pass);
		}
	}

	public void sub(JedisPubSub listener, String channel) {
		jedis.subscribe(listener, channel);

	}

}
