# Redis发布订阅


# 简介


Redis的发布订阅类似MQ里面的Topic

Redis 发布订阅(pub/sub)是一种消息通信模式：发送者(pub)发送消息，订阅者(sub)接收消息。
Redis 客户端可以订阅任意数量的频道。
下图展示了频道 channel1 ， 以及订阅这个频道的三个客户端 —— client2 、 client5 和 client1 之间的关系：

![pubsub1](../images/redis-pubsub1.png)

当有新消息通过 PUBLISH 命令发送给频道 channel1 时， 这个消息就会被发送给订阅它的三个客户端：

![pubsub1](../images/redis-pubsub2.png)

## 实例

以下实例演示了发布订阅是如何工作的。在我们实例中我们创建了订阅频道名为 redisChat:

```
127.0.0.1:6379> subscribe zkk
Reading messages... (press Ctrl-C to quit)
1) "subscribe"
2) "zkk"
3) (integer) 1
```


现在，我们先重新开启个 redis 客户端，然后在同一个频道 redisChat 发布两次消息，订阅者就能接收到消息。

```
127.0.0.1:6379> publish zkk 'hello zhao keke'
(integer) 1
127.0.0.1:6379> publish zkk 'how old are you'
(integer) 1
127.0.0.1:6379>

```




订阅者的客户端会显示如下消息

```
127.0.0.1:6379> subscribe zkk
Reading messages... (press Ctrl-C to quit)
1) "subscribe"
2) "zkk"
3) (integer) 1
1) "message"
2) "zkk"
3) "hello zhao keke"
1) "message"
2) "zkk"
3) "how old are you"

```

## Jedis代码实现

### PubClient

```
package com.hhtd.hhtd_utils.redis;

import org.apache.commons.lang3.StringUtils;

import com.hhtd.hhtd_constants.GlobalConfig;

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

```

### SubClient

```
package com.hhtd.hhtd_utils.redis;

import org.apache.commons.lang3.StringUtils;

import com.hhtd.hhtd_constants.GlobalConfig;

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

```

### 测试代码

```
package com.hhtd.hhtd_utils.redis;

import redis.clients.jedis.JedisPubSub;

public class PrintListener extends JedisPubSub {

	@Override
	public void onMessage(String channel, String message) {
		System.out.println("message receive:" + message + ",channel:" + channel + "...");
	}

}
```

```
package com.hhtd.hhtd_utils.redis;

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

	@Override
	public void run() {
		TestThread.testSub();
	}

	private static void testSub() {
		SubClient subclient = new SubClient("127.0.0.1", "zch");
		JedisPubSub listener = new PrintListener();
		subclient.sub(listener, "123");
	}

}

```

## Redis 发布订阅命令

下表列出了redis 发布订阅常用命令：

|序号|	命令及描述|
|-|-|
|1|	PSUBSCRIBE pattern [pattern ...]  订阅一个或多个符合给定模式的频道。|
|2|	PUBSUB subcommand [argument [argument ...]]查看订阅与发布系统状态。|
|3|	PUBLISH channel message将信息发送到指定的频道。|
|4|	PUNSUBSCRIBE [pattern [pattern ...]]退订所有给定模式的频道。|
|5|	SUBSCRIBE channel [channel ...]订阅给定的一个或多个频道的信息。|
|6|	UNSUBSCRIBE [channel [channel ...]]指退订给定的频道。|


# 参考

- [Redis 发布订阅](http://www.runoob.com/redis/redis-pub-sub.html)
