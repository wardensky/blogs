# 应用Java操作Redis


应用Java语言操作Redis都是用Jedis，代码很简单。

下面代码是我项目中用的，用来封装部分操作的代码。


```
package com.zch.blogs.nosql.redis;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisClient {
	private Jedis jedis;
	private static RedisClient inst;
	private JedisPool jedisPool;

	private String ip = "127.0.0.1";
	private String pass = "";
	private int port = GlobalConfig.REDIS_DEFAULT_PORT;

	private RedisClient(String ip, String pass) {
		this.ip = ip;
		this.pass = pass;
		initialPool();
		jedis = jedisPool.getResource();
		if (StringUtils.isNotBlank(this.pass)) {
			jedis.auth(this.pass);
		}
	}

	private void initialPool() {
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxWaitMillis(1000L);
		config.setMaxIdle(5);
		config.setTestOnBorrow(false);
		System.out.println("this.ip = " + this.ip);
		this.jedisPool = new JedisPool(config, this.ip, this.port);
	}

	public static RedisClient getInst(String ip, String pass) {
		if (inst == null) {
			inst = new RedisClient(ip, pass);
		}
		return inst;
	}

	public void setEx(String key, int seconds, String value) {
		this.jedis.setex(key, seconds, value);
	}

	public void lpush(String key, String... values) {
		this.jedis.lpush(key, values);
	}

	public void lpush(String key, List<String> values) {
		for (String inst : values) {
			this.jedis.lpush(key, inst);
		}
	}

	public List<String> lrange(String key, long start, long end) {
		return this.jedis.lrange(key, start, end);
	}

	public void expire(String key, int seconds) {
		this.jedis.expire(key, seconds);
	}

	public void set(String key) {
		this.jedis.set(key, key);

	}

	public void set(String key, String value) {
		this.jedis.set(key, value);
	}

	public void selectDb(int index) {
		if (index < 16 && index > -1) {
			this.jedis.select(index);
		}
	}

	public void incrBy(String key, int amount) {
		this.jedis.incrBy(key, amount);
	}

	public void incr(String key) {
		this.jedis.incr(key);
	}

	public void decr(String key) {
		this.jedis.decr(key);
	}

	public void decrBy(String key, int amount) {
		this.jedis.decrBy(key, amount);
	}

	public String get(String key) {
		if (null == key) {
			return null;
		}
		return this.jedis.get(key);
	}

	public void del(String key) {
		this.jedis.del(key);
	}

	public boolean exists(String key) {
		return this.jedis.exists(key);
	}

	public Set<String> keys() {
		return this.jedis.keys("*");
	}

	public Set<String> keys(String data) {
		return this.jedis.keys("*" + data + "*");
	}

	public Set<String> matchKeys(String data) {
		return this.jedis.keys(data);
	}

	public void clearAll() {
		this.jedis.flushDB();
	}

	public Jedis getJedis() {
		return jedis;
	}

	public void setJedis(Jedis jedis) {
		this.jedis = jedis;
	}

	public void sadd(String key, String... values) {
		this.jedis.sadd(key, values);
	}

	public void sadd(String key, List<String> values) {
		for (String inst : values) {

			this.jedis.sadd(key, inst);
		}
	}

	public void hmset(String key, Map<String, String> hash) {
		this.jedis.hmset(key, hash);
	}

	public String hget(String key, String field) {
		return this.jedis.hget(key, field);
	}

	public Set<String> smembers(String key) {
		return this.jedis.smembers(key);
	}
}


```

pom文件如下：
```

<dependency>
  <groupId>redis.clients</groupId>
  <artifactId>jedis</artifactId>
  <version>2.9.0</version>
</dependency>

```

## 参考

- [Java中使用Jedis操作Redis](http://www.cnblogs.com/liuling/p/2014-4-19-04.html)
- [Intro to Jedis – the Java Redis Client Library](https://www.baeldung.com/jedis-java-redis-client-library)
