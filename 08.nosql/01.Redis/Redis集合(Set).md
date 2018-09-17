# Redis 集合(Set)

## 简介

Redis 的 Set 是 String 类型的无序集合。集合成员是唯一的，这就意味着集合中不能出现重复的数据。
Redis 中集合是通过哈希表实现的，所以添加，删除，查找的复杂度都是 O(1)。
集合中最大的成员数为 232 - 1 (4294967295, 每个集合可存储40多亿个成员)。

## 实例
```
127.0.0.1:6379> sadd kk girl
(integer) 1
127.0.0.1:6379> sadd kk 3 pretty smart
(integer) 3
127.0.0.1:6379> sadd kk girl
(integer) 0
127.0.0.1:6379> scard kk
(integer) 4
127.0.0.1:6379> smembers kk
1) "smart"
2) "pretty"
3) "3"
4) "girl"
```

## Redis 集合命令
下表列出了 Redis 集合基本命令：

|序号|   命令及描述|
|-|-|
|1|      SADD key member1 [member2]|
||向集合添加一个或多个成员|
|2|      SCARD key|
||获取集合的成员数|
|3|      SDIFF key1 [key2]|
||返回给定所有集合的差集|
|4|      SDIFFSTORE destination key1 [key2]|
||返回给定所有集合的差集并存储在 destination 中|
|5|      SINTER key1 [key2]|
||返回给定所有集合的交集|
|6|      SINTERSTORE destination key1 [key2]|
||返回给定所有集合的交集并存储在 destination 中|
|7  |    SISMEMBER key member|
||判断 member 元素是否是集合 key 的成员|
|8|      SMEMBERS key|
||返回集合中的所有成员|
|9  |    SMOVE source destination member|
||将 member 元素从 source 集合移动到 destination 集合|
|10|     SPOP key|
||移除并返回集合中的一个随机元素|
|11|     SRANDMEMBER key [count]|
||返回集合中一个或多个随机数|
|12|     SREM key member1 [member2]|
||移除集合中一个或多个成员|
|13|     SUNION key1 [key2]|
||返回所有给定集合的并集|
|14 |    SUNIONSTORE destination key1 [key2]|
||所有给定集合的并集存储在 destination 集合中|
|15|     SSCAN key cursor [MATCH pattern] [COUNT count]|
||迭代集合中的元素|
