# MySQL SQL慢查询优化经历与处理方案

当MySQL服务器出现异常（慢），首先要考虑是否因，SQL语句引起数据库慢，如果情况比较紧急，我们就要立刻 SHOW FULL PROCESSLIST; 去查看，但我建议大家使用-e参数，采用非交互的方式，因为这样可以使用grep等命令，对结果进行过滤，更方便直观的看到结果

## 抓SQL慢查询语句的方法，有2种：

### 临时紧急抓取

```
SHOW FULL PROCESSLIST; #查看MySQL 在运行的线程；
```

多执行几次，有相同语句，就可能是SQL慢查询语句；

![MySQL SQL慢查询优化经历与处理方案](../images/SQL优化-1.png)



这个命令中最关键的就是state列，mysql列出的状态主要有以下几种：

- Checking table   #正在检查数据表（这是自动的）。
- Closing tables   #正在将表中修改的数据刷新到磁盘中，同时正在关闭已经用完的表。这是一个很快的操作，如果不是这样的话，就应该确认磁盘空间是否已经满了或者磁盘是否正处于重负中。
- Connect Out      #复制从服务器正在连接主服务器。
- Copying to tmp table on disk #由于临时结果集大于 tmp_table_size，正在将临时表从内存存储转为磁盘存储以此节省内存。
- Creating tmp table #正在创建临时表以存放部分查询结果。
- deleting from main table #服务器正在执行多表删除中的第一部分，刚删除第一个表。
- deleting from reference tables  #服务器正在执行多表删除中的第二部分，正在删除其他表的记录。
- Flushing tables  #正在执行 FLUSH TABLES，等待其他线程关闭数据表。
- Killed   #发送了一个kill请求给某线程，那么这个线程将会检查kill标志位，同时会放弃下一个kill请求。MySQL会在每次的主循环中检查kill标志位，不过有些情况下该线程可能会过一小段才能死掉。如果该线程程被其他线程锁住了，那么kill请求会在锁释放时马上生效。
- Locked    #被其他查询锁住了。
- Sending data  #正在处理 SELECT 查询的记录，同时正在把结果发送给客户端。
- Sorting for group #正在为 GROUP BY 做排序。
- Sorting for order  #正在为 ORDER BY 做排序。
- Opening tables  #这个过程应该会很快，除非受到其他因素的干扰。例如，在执 ALTER TABLE 或 LOCK TABLE 语句行完以前，数据表无法被其他线程打开。 正尝试打开一个表。
- Removing duplicates  #正在执行一个 SELECT DISTINCT 方式的查询，但是MySQL无法在前一个阶段优化掉那些重复的记录。因此，MySQL需要再次去掉重复的记录，然后再把结果发送给客户端。
- Reopen table #获得了对一个表的锁，但是必须在表结构修改之后才能获得这个锁。已经释放锁，关闭数据表，正尝试重新打开数据表。
- Repair by sorting #修复指令正在排序以创建索引。
- Repair with keycache #修复指令正在利用索引缓存一个一个地创建新索引。它会比 Repair by sorting 慢些。
- Searching rows for update  #正在讲符合条件的记录找出来以备更新。它必须在UPDATE要修改相关的记录之前就完成了。
- Sleeping #正在等待客户端发送新请求.
- System lock #正在等待取得一个外部的系统锁。如果当前没有运行多个mysqld服务器同时请求同一个表，那么可以通过增加 --skip-external-locking参数来禁止外部系统锁。
- Upgrading lock
- INSERT DELAYED #正在尝试取得一个锁表以插入新记录。
- Updating #正在搜索匹配的记录，并且修改它们。
- INSERT DELAYED #已经处理完了所有待处理的插入操作，正在等待新的请求。

总结：
一般简单查询都应该2秒内完成，如果超时可能就存在异常

另外，上面的状态 大多数都是出现问题后，为排错提供，类似错误码。

### 定期分析-记录慢查询日志的方式

我工作中，一般会在配置文件里，设置3个参数
```
log-slow-queries = /data/3306/slow.log          #慢查询日志路径， log文件在my.cnf定义
long_query_time = 2                     #记录SQL查询超过2s的语句
log-queries-not-using-indexes = 1            #记录没有使用索引的sql
```

通过上述3个参数，收集慢查询日志，通过写脚本 ，mysqladmin 进行日志切割，在使用msyqlsla工具进行分析，然后每天8点 通过定时任务去执行，定期将结果以邮件方式，发送给公司的DBA，或自己，核心开发，抄送CTO。

msyqlsla工具地址：http://www.linuxidc.com/Linux/2013-06/86447.htm

## 我们抓到慢查询了，如何进行优化？

### 创建索引

explian命令 # 查看sql语句使用索引情况
SQL-no-cache \G， #看看SQL语句 是不是走索引
注意其中有个key，真正显示是否走索引的，如果没有走索引的话，就要进行设置，那如何设置呢？？？

例如，我们查询当前系统所有用户，这个查询语句启用的是PRIMARY 主键索引（看key）
```
mysql> explain select  user,host from  mysql.user \G
*************************** 1. row ***************************
           id: 1
  select_type: SIMPLE
        table: user
         type: index
possible_keys: NULL
          key: PRIMARY
      key_len: 228
          ref: NULL
         rows: 6
        Extra: Using index
1 row in set (0.00 sec)
```
查看表结构（RPI主键索引）
```
mysql> desc mysql.user;
+------------------------+-----------------------------------+------+-----+-----------------------+-------+
| Field                  | Type                              | Null | Key | Default               | Extra |
+------------------------+-----------------------------------+------+-----+-----------------------+-------+
| Host                   | char(60)                          | NO   | PRI |                       |       |
| User                   | char(16)                          | NO   | PRI |                       |       |
```
## 如何创建索引？

我们可以针对 where 后 的条件 这种列做索引，尽量选唯一值多的大表上的列做索引，（例如男女性别列唯一值少，不是适合建立索引），如果条件列有好几列，唯一值有很少，我们可以建立联合索引来达到优化目的， 联合索引有前缀特性，查询频繁的列要放在前面，细节就不在说了，确认如何建立索引后，我们就开始创建索引

创建索引有2 种方法：
```
alter table student change id id int primary key auto_increment;  #增加自增主键索引
alter table student add index index_name(name)  #增加普通索引
create index index_dept on student(dept(8));  #创建指定字符数索引
```

数据量特别大的情况下，我们尽量选择数据库处于低谷或 选择晚上进行，以免影响站点访问，紧急情况除外；

## 更高级的优化

还可以使用select profile功能 ，对SQL语句的每一个细节，查看进行优化，这里我使用也不是很多，一般是公司专业DBA来进行处理。

当然也会遇到特别的长的sql语句，优化的余力也不大，我会sql语句发给核心开发，进行处理，比如1条语句很慢，我可以变为2条，分别走索引，有可能有会很高，也可以用过改善产品，改善架构的方式，例如这条语句没有优化的余地，我们可以放到内部的从库上进行查询。

## 参考

- [MySQL SQL慢查询优化经历与处理方案](https://www.linuxidc.com/Linux/2017-05/143727.htm)
