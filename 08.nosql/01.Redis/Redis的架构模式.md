# Redis 有哪些架构模式？讲讲各自的特点

## 单机版

![](../images/redis-4.jpg)


特点：简单

问题：
1. 内存容量有限
2. 处理能力有限
3. 无法高可用。

## 主从复制
![](../images/redis-5.jpg)


Redis 的复制（replication）功能允许用户根据一个 Redis 服务器来创建任意多个该服务器的复制品，其中被复制的服务器为主服务器（master），而通过复制创建出来的服务器复制品则为从服务器（slave）。 只要主从服务器之间的网络连接正常，主从服务器两者会具有相同的数据，主服务器就会一直将发生在自己身上的数据更新同步 给从服务器，从而一直保证主从服务器的数据相同。

特点：

- master/slave 角色
- master/slave 数据相同
- 降低 master 读压力在转交从库

问题：

- 无法保证高可用
- 没有解决 master 写的压力

## 哨兵

![](../images/redis-6.jpg)


Redis sentinel 是一个分布式系统中监控 redis 主从服务器，并在主服务器下线时自动进行故障转移。其中三个特性：
监控（Monitoring）： Sentinel 会不断地检查你的主服务器和从服务器是否运作正常。
提醒（Notification）： 当被监控的某个 Redis 服务器出现问题时， Sentinel 可以通过 API 向管理员或者其他应用程序发送通知。
自动故障迁移（Automatic failover）： 当一个主服务器不能正常工作时， Sentinel 会开始一次自动故障迁移操作。

**特点**
- 保证高可用
- 监控各个节点
- 自动故障迁移

**缺点**
- 主从模式，切换需要时间丢数据
- 没有解决 master 写的压力

## 集群（proxy 型）

![](../images/redis-7.jpg)

Twemproxy 是一个 Twitter 开源的一个 redis 和 memcache 快速/轻量级代理服务器； Twemproxy 是一个快速的单线程代理程序，支持 Memcached ASCII 协议和 redis 协议。

**特点**

- 多种 hash 算法：MD5、CRC16、CRC32、CRC32a、hsieh、murmur、Jenkins
- 支持失败节点自动删除
- 后端 Sharding 分片逻辑对业务透明，业务方的读写方式和操作单个 Redis 一致

**缺点**

- 增加了新的 proxy，需要维护其高可用。
- failover 逻辑需要自己实现，其本身不能支持故障的自动转移可扩展性差，进行扩缩容都需要手动干预

## 集群（直连型）

![](../images/redis-8.jpg)


从redis 3.0之后版本支持redis-cluster集群，Redis-Cluster采用无中心结构，每个节点保存数据和整个集群状态,每个节点都和其他所有节点连接。

**特点**

- 无中心架构（不存在哪个节点影响性能瓶颈），少了 proxy 层。
- 数据按照 slot 存储分布在多个节点，节点间数据共享，可动态调整数据分布。
- 可扩展性，可线性扩展到 1000 个节点，节点可动态添加或删除。
- 高可用性，部分节点不可用时，集群仍可用。通过增加 Slave 做备份数据副本
- 实现故障自动 failover，节点之间通过 gossip 协议交换状态信息，用投票机制完成 Slave到 Master的角色提升。

**缺点**
- 资源隔离性较差，容易出现相互影响的情况。
- 数据通过异步复制,不保证数据的强一致性
