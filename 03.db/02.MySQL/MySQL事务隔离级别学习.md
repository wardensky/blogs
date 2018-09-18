# MySQL事务隔离级别学习


## 事务的基本要素（ACID）

1. 原子性（Atomicity）：事务开始后所有操作，要么全部做完，要么全部不做，不可能停滞在中间环节。事务执行过程中出错，会回滚到事务开始前的状态，所有的操作就像没有发生一样。也就是说事务是一个不可分割的整体，就像化学中学过的原子，是物质构成的基本单位。
2. 一致性（Consistency）：事务开始前和结束后，数据库的完整性约束没有被破坏 。比如A向B转账，不可能A扣了钱，B却没收到。
3. 隔离性（Isolation）：同一时间，只允许一个事务请求同一数据，不同的事务之间彼此没有任何干扰。比如A正在从一张银行卡中取钱，在A取钱的过程结束前，B不能向这张卡转账。
4. 持久性（Durability）：事务完成后，事务对数据库的所有更新将被保存到数据库，不能回滚。


## 事务的并发问题


### 脏读

事务A读取了事务B更新的数据，然后B回滚操作，那么A读取到的数据是脏数据

### 不可重复读

事务 A 多次读取同一数据，事务 B 在事务A多次读取的过程中，对数据作了更新并提交，导致事务A多次读取同一数据时，结果不一致。

### 幻读

系统管理员A将数据库中所有学生的成绩从具体分数改为ABCDE等级，但是系统管理员B就在这个时候插入了一条具体分数的记录，当系统管理员A改结束后发现还有一条记录没有改过来，就好像发生了幻觉一样，这就叫幻读。

### 小结

不可重复读的和幻读很容易混淆，不可重复读侧重于修改，幻读侧重于新增或删除。解决不可重复读的问题只需锁住满足条件的行，解决幻读需要锁表




## MySQL事务隔离级别

|事务隔离级别 | 脏读| 不可重复读|幻读 |
| - | - | - | - |
| 读未提交（read-uncommitted） | 是 | 是 | 是 |
| 不可重复读（read-committed) | 否 | 是 | 是 |
| 可重复读（repeatable-read） |否  |否 |是  |
|串行化（serializable）  |  否| 否 | 否 |


总结如下:
- 未提交读(Read Uncommitted)：允许脏读，也就是可能读取到其他会话中未提交事务修改的数据
- 提交读(Read Committed)：只能读取到已经提交的数据。Oracle等多数数据库默认都是该级别 (不重复读)
- 可重复读(Repeated Read)：可重复读。在同一个事务内的查询都是事务开始时刻一致的，InnoDB默认级别。在SQL标准中，该隔离级别消除了不可重复读，但是还存在幻象读
- 串行读(Serializable)：完全串行化的读，每次读都需要获得表级共享锁，读写相互都会阻塞

mysql默认的事务隔离级别为repeatable-read

```
mysql> select @@tx_isolation;
+-----------------+
| @@tx_isolation  |
+-----------------+
| REPEATABLE-READ |
+-----------------+
1 row in set, 1 warning (0.00 sec)
```
## 实例

用例子说明各个隔离级别的情况

### 读未提交：

- 打开一个客户端A，并设置当前事务模式为read uncommitted（未提交读），查询表account的初始值：

```
mysql> set session transaction isolation level read uncommitted;
Query OK, 0 rows affected (0.00 sec)
```

```
mysql> select * from account;
+----+------+---------+
| id | name | balance |
+----+------+---------+
|  1 | zch  |     450 |
|  2 | keke |   16000 |
|  3 | ivy  |    2400 |
+----+------+---------+
3 rows in set (0.00 sec)
```
- 在客户端A的事务提交之前，打开另一个客户端B，更新表account：

```
mysql> start transaction;
Query OK, 0 rows affected (0.00 sec)

mysql> update account set balance = balance - 50 where id = 1;
Query OK, 1 row affected (0.02 sec)
Rows matched: 1  Changed: 1  Warnings: 0

mysql> select * from account;
+----+------+---------+
| id | name | balance |
+----+------+---------+
|  1 | zch  |     400 |
|  2 | keke |   16000 |
|  3 | ivy  |    2400 |
+----+------+---------+
3 rows in set (0.00 sec)
```

- 这时，虽然客户端B的事务还没提交，但是客户端A就可以查询到B已经更新的数据：

```
mysql> select * from account;
+----+------+---------+
| id | name | balance |
+----+------+---------+
|  1 | zch  |     450 |
|  2 | keke |   16000 |
|  3 | ivy  |    2400 |
+----+------+---------+
3 rows in set (0.00 sec)

mysql> select * from account;
+----+------+---------+
| id | name | balance |
+----+------+---------+
|  1 | zch  |     400 |
|  2 | keke |   16000 |
|  3 | ivy  |    2400 |
+----+------+---------+
3 rows in set (0.00 sec)

```

- 一旦客户端B的事务因为某种原因回滚，所有的操作都将会被撤销，那客户端A查询到的数据其实就是脏数据：

```
mysql> rollback;
Query OK, 0 rows affected (0.01 sec)

mysql> select * from account;
+----+------+---------+
| id | name | balance |
+----+------+---------+
|  1 | zch  |     450 |
|  2 | keke |   16000 |
|  3 | ivy  |    2400 |
+----+------+---------+
3 rows in set (0.00 sec)
```

- 在客户端A执行更新语句```update account set balance = balance - 50 where id =1```,zch的balance没有变成350，居然是400，是不是很奇怪，数据不一致啊，如果你这么想就太天真 了，在应用程序中，我们会用400-50=350，并不知道其他会话回滚了，要想解决这个问题可以采用读已提交的隔离级别

```
mysql> update account set balance = balance - 50 where id = 1;
Query OK, 1 row affected (0.01 sec)
Rows matched: 1  Changed: 1  Warnings: 0

mysql> select * from account;
+----+------+---------+
| id | name | balance |
+----+------+---------+
|  1 | zch  |     400 |
|  2 | keke |   16000 |
|  3 | ivy  |    2400 |
+----+------+---------+
3 rows in set (0.00 sec)
```



### 读已提交

- 打开一个客户端A，并设置当前事务模式为read committed（未提交读），查询表account的所有记录：

```
mysql> set session transaction isolation level read committed;
Query OK, 0 rows affected (0.00 sec)

mysql> start transaction;
Query OK, 0 rows affected (0.00 sec)

mysql> select * from account;
+----+------+---------+
| id | name | balance |
+----+------+---------+
|  1 | zch  |     450 |
|  2 | keke |   16000 |
|  3 | ivy  |    2400 |
+----+------+---------+
3 rows in set (0.00 sec)
```


- 在客户端A的事务提交之前，打开另一个客户端B，更新表account：

```
mysql> set session transaction isolation level read committed;
Query OK, 0 rows affected (0.00 sec)

mysql> start transaction;
Query OK, 0 rows affected (0.00 sec)

mysql> update account set balance = balance - 50 where id = 1;
Query OK, 1 row affected (0.01 sec)
Rows matched: 1  Changed: 1  Warnings: 0

mysql> select * from account;
+----+------+---------+
| id | name | balance |
+----+------+---------+
|  1 | zch  |     400 |
|  2 | keke |   16000 |
|  3 | ivy  |    2400 |
+----+------+---------+
3 rows in set (0.00 sec)
```


- 这时，客户端B的事务还没提交，客户端A不能查询到B已经更新的数据，解决了脏读问题：

```
mysql> select * from account;
+----+------+---------+
| id | name | balance |
+----+------+---------+
|  1 | zch  |     450 |
|  2 | keke |   16000 |
|  3 | ivy  |    2400 |
+----+------+---------+
3 rows in set (0.00 sec)

mysql> select * from account;
+----+------+---------+
| id | name | balance |
+----+------+---------+
|  1 | zch  |     450 |
|  2 | keke |   16000 |
|  3 | ivy  |    2400 |
+----+------+---------+
3 rows in set (0.00 sec)
```

- 客户端B的事务提交

```
mysql> select * from account;
+----+------+---------+
| id | name | balance |
+----+------+---------+
|  1 | zch  |     400 |
|  2 | keke |   16000 |
|  3 | ivy  |    2400 |
+----+------+---------+
3 rows in set (0.00 sec)

mysql> commit;
Query OK, 0 rows affected (0.02 sec)

```

- 客户端A执行与上一步相同的查询，结果 与上一步不一致，即产生了不可重复读的问题

```
mysql> select * from account;
+----+------+---------+
| id | name | balance |
+----+------+---------+
|  1 | zch  |     450 |
|  2 | keke |   16000 |
|  3 | ivy  |    2400 |
+----+------+---------+
3 rows in set (0.00 sec)

mysql> select * from account;
+----+------+---------+
| id | name | balance |
+----+------+---------+
|  1 | zch  |     400 |
|  2 | keke |   16000 |
|  3 | ivy  |    2400 |
+----+------+---------+
3 rows in set (0.00 sec)
```



### 可重复读

- 打开一个客户端A，并设置当前事务模式为repeatable read，查询表account的所有记录

```
mysql> set session transaction isolation level repeatable read;
Query OK, 0 rows affected (0.00 sec)

mysql> start transaction;
Query OK, 0 rows affected (0.00 sec)

mysql> select * from account;
+----+------+---------+
| id | name | balance |
+----+------+---------+
|  1 | zch  |     400 |
|  2 | keke |   16000 |
|  3 | ivy  |    2400 |
+----+------+---------+
3 rows in set (0.00 sec)
```

- 在客户端A的事务提交之前，打开另一个客户端B，更新表account并提交


```
mysql> set session transaction isolation level repeatable read;
Query OK, 0 rows affected (0.00 sec)

mysql> start transaction;
Query OK, 0 rows affected (0.00 sec)

mysql> update account set balance = balance - 50 where id = 1;
Query OK, 1 row affected (0.00 sec)
Rows matched: 1  Changed: 1  Warnings: 0

mysql> select * from account;
+----+------+---------+
| id | name | balance |
+----+------+---------+
|  1 | zch  |     350 |
|  2 | keke |   16000 |
|  3 | ivy  |    2400 |
+----+------+---------+
3 rows in set (0.00 sec)

mysql> commit;
Query OK, 0 rows affected (0.00 sec)
```

- 在客户端A查询表account的所有记录，与步骤（1）查询结果一致，没有出现不可重复读的问题

```
mysql> select * from account;
+----+------+---------+
| id | name | balance |
+----+------+---------+
|  1 | zch  |     400 |
|  2 | keke |   16000 |
|  3 | ivy  |    2400 |
+----+------+---------+
3 rows in set (0.00 sec)

mysql> select * from account;
+----+------+---------+
| id | name | balance |
+----+------+---------+
|  1 | zch  |     400 |
|  2 | keke |   16000 |
|  3 | ivy  |    2400 |
+----+------+---------+
3 rows in set (0.00 sec)

```


- 在客户端A，接着执行```update balance = balance - 50 where id = 1```，balance没有变成400-50=350，zch的balance值用的是步骤（2）中的350来算的，所以是300，数据的一致性倒是没有被破坏。可重复读的隔离级别下使用了MVCC机制，select操作不会更新版本号，是快照读（历史版本）；insert、update和delete会更新版本号，是当前读（当前版本）。


```
mysql> select * from account;
+----+------+---------+
| id | name | balance |
+----+------+---------+
|  1 | zch  |     400 |
|  2 | keke |   16000 |
|  3 | ivy  |    2400 |
+----+------+---------+
3 rows in set (0.00 sec)

mysql>  update account set balance = balance - 50 where id = 1;
Query OK, 1 row affected (0.00 sec)
Rows matched: 1  Changed: 1  Warnings: 0

mysql> select * from account;
+----+------+---------+
| id | name | balance |
+----+------+---------+
|  1 | zch  |     300 |
|  2 | keke |   16000 |
|  3 | ivy  |    2400 |
+----+------+---------+
3 rows in set (0.00 sec)
```

- 重新打开客户端B，插入一条新数据后提交


```
mysql> start transaction;
Query OK, 0 rows affected (0.00 sec)

mysql> insert into account values (4,'zyx', 700);
Query OK, 1 row affected (0.00 sec)

mysql> commit;
Query OK, 0 rows affected (0.01 sec)
```

- 在客户端A查询表account的所有记录，没有查出新增数据，所以没有出现幻读

```
mysql> select * from account;
+----+------+---------+
| id | name | balance |
+----+------+---------+
|  1 | zch  |     300 |
|  2 | keke |   16000 |
|  3 | ivy  |    2400 |
+----+------+---------+
3 rows in set (0.05 sec)
```


### 串行化
- 打开一个客户端A，并设置当前事务模式为serializable，查询表account的初始值：

```
mysql> set session transaction isolation level serializable;
Query OK, 0 rows affected (0.00 sec)

mysql> start transaction;
Query OK, 0 rows affected (0.00 sec)

mysql> select * from account;
```
如果另外一个事务没有提交，这个数据无法读出，会被阻塞。

只有当另外一个客户端提交事务之后，才能查询出结果。
```
mysql> commit;
Query OK, 0 rows affected (0.02 sec)

```

```
mysql> select * from account;
+----+------+---------+
| id | name | balance |
+----+------+---------+
|  1 | zch  |     300 |
|  2 | keke |   16000 |
|  3 | ivy  |    2400 |
|  4 | zyx  |     700 |
+----+------+---------+
4 rows in set (12.86 sec)
```

时间很长，是因为被阻塞了。

- 打开一个客户端B，并设置当前事务模式为serializable，插入一条记录报错，表被锁了插入失败，mysql中事务隔离级别为serializable时会锁表，因此不会出现幻读的情况，这种隔离级别并发性极低，开发中很少会用到。

```
mysql> set session transaction isolation level serializable;
Query OK, 0 rows affected (0.00 sec)

mysql> start transaction;
Query OK, 0 rows affected (0.00 sec)

mysql> insert into account values(5,'tom',0);
ERROR 1205 (HY000): Lock wait timeout exceeded; try restarting transaction
```

## 事务隔离是怎么实现的

是基于锁实现的.
有哪些锁？分别介绍下
在DBMS中，可以按照锁的粒度把数据库锁分为行级锁(INNODB引擎)、表级锁(MYISAM引擎)和页级锁(BDB引擎 )。

### 行级锁

行级锁是Mysql中锁定粒度最细的一种锁，表示只针对当前操作的行进行加锁。行级锁能大大减少数据库操作的冲突。其加锁粒度最小，但加锁的开销也最大。行级锁分为共享锁和排他锁。

**特点：**

开销大，加锁慢；会出现死锁；锁定粒度最小，发生锁冲突的概率最低，并发度也最高。

### 表级锁

表级锁是MySQL中锁定粒度最大的一种锁，表示对当前操作的整张表加锁，它实现简单，资源消耗较少，被大部分MySQL引擎支持。最常使用的MYISAM与INNODB都支持表级锁定。表级锁定分为表共享读锁（共享锁）与表独占写锁（排他锁）。

**特点**

开销小，加锁快；不会出现死锁；锁定粒度大，发出锁冲突的概率最高，并发度最低。

### 页级锁

页级锁是MySQL中锁定粒度介于行级锁和表级锁中间的一种锁。表级锁速度快，但冲突多，行级冲突少，但速度慢。所以取了折衷的页级，一次锁定相邻的一组记录。

**特点**

开销和加锁时间界于表锁和行锁之间；会出现死锁；锁定粒度界于表锁和行锁之间，并发度一般

## 参考

- [MySQL的四种事务隔离级别](https://www.cnblogs.com/huanongying/p/7021555.html)
- [MySQL 四种事务隔离级的说明](http://www.cnblogs.com/zhoujinyi/p/3437475.html)
- [MYSQL事务隔离级别](http://www.importnew.com/29321.html)
