# 删除MySQL重复数据

## 项目背景
在最近做的一个linux性能采集项目中，发现线程的程序入库很慢，再仔细定位，发现数据库里面很多冗余数据。因为在采集中，对于同一台设备，同一个时间点应该只有一个数据，然而，数据库中存入了多个数据。对于如何造成了这个结果，一时没有想清楚，但为了解决入库慢的问题，首先要删除冗余数据。

## 问题描述
数据库的表结构很简单，如下：

**********
	+----------------+--------------+------+-----+---------+-------+
	| Field          | Type         | Null | Key | Default | Extra |
	+----------------+--------------+------+-----+---------+-------+
	| id             | varchar(255) | NO   | PRI | NULL    |       |
	| conf_id        | varchar(255) | NO   | MUL | NULL    |       |
	| insert_time    | datetime     | YES  |     | NULL    |       |
	| cpu_usage      | float(11,2)  | YES  |     | NULL    |       |
	| memory_usage   | float(11,2)  | YES  |     | NULL    |       |
	| io_usage_write | float(11,2)  | YES  |     | NULL    |       |
	| io_usage_read  | float(11,2)  | YES  |     | NULL    |       |
	+----------------+--------------+------+-----+---------+-------+
*************

查询所有数据量

```
select count(*) from perf_linux;
```

输出 427366

查询所有时间点不同设备的数据量

```
select count(distinct conf_id, insert_time) from perf_linux ;
```

输出42387

由上面的数据可以看出，数据冗余了10倍左右。

再按时间分组看一下:

```
select id, conf_id ,insert_time from perf_linux order by insert_time, conf_id;
```

输出：
```
	| 2a79f7cd-43a9-4c7b-adb2-316b6c04283e | 1       | 2014-12-09 15:09:14 |
	| 50d6f6c2-9c8b-45fd-98fd-2be211221cfd | 1       | 2014-12-09 15:09:14 |
	| 740b52e1-e868-4074-ba36-74e2634401b3 | 1       | 2014-12-09 15:09:14 |
	| 8b0096a4-9e85-417b-a131-e3505ca79a9c | 1       | 2014-12-09 15:09:14 |
	| 90a9e882-5220-4508-a56f-8d4ab4a7929b | 1       | 2014-12-09 15:09:14 |
	| d17403ed-24a4-45e8-b51b-2a95118383d9 | 1       | 2014-12-09 15:09:14 |
	| 0c2da917-579b-4080-857d-7159f38b44ac | 2       | 2014-12-09 15:09:14 |
	| 263083eb-8f63-4d2b-a03f-3320aa678735 | 2       | 2014-12-09 15:09:14 |
	| d6c57a38-080b-465a-a55a-beafd9daf32d | 2       | 2014-12-09 15:09:14 |
	| f672227b-1fb8-4b85-880d-2cc34b02880d | 2       | 2014-12-09 15:09:14 |
	| f80020fe-6cb5-48ec-beb0-4e8ebeb0ca57 | 2       | 2014-12-09 15:09:14 |
	| ff633a35-824d-49ba-b78c-5bcc5df8d1cc | 2       | 2014-12-09 15:09:14 |
	| 5c41e48a-abfc-4108-a00e-ca7def7d5a5a | 3       | 2014-12-09 15:09:14 |
	| 60b7ab9e-c91a-4020-a6d3-7bceb1dc47c5 | 3       | 2014-12-09 15:09:14 |
	| 7b6cd2b8-ac6d-43eb-8858-e15885e676c8 | 3       | 2014-12-09 15:09:14 |
	| d53a3df5-08c4-4604-8fac-cb51077935f6 | 3       | 2014-12-09 15:09:14 |
	| d9e4ba14-f98d-42a8-b3bc-2879d58aa797 | 3       | 2014-12-09 15:09:14 |
	| f56f82f6-32a7-47f7-ae07-b13168743884 | 3       | 2014-12-09 15:09:14 |
	| 076c4c1b-0028-4a9c-a8c4-de655bd6ab6b | 4       | 2014-12-09 15:09:14 |
	| 2a90ad9e-11a5-4707-95e8-78491da658ad | 4       | 2014-12-09 15:09:14 |
	| 3b17ad1d-e589-4b65-93a7-d61fc99b4071 | 4       | 2014-12-09 15:09:14 |
	| 6988d6cf-44ef-47f7-808d-09791caf2d90 | 4       | 2014-12-09 15:09:14 |
	| 8404d281-f9e5-4153-a47e-128c05386758 | 4       | 2014-12-09 15:09:14 |
	| e042e310-7ff2-4e4d-8c98-71e3e4d57828 | 4       | 2014-12-09 15:09:14 |
	+--------------------------------------+---------+---------------------+
```

由上图可见，同一个时间点的同一个设备的数据有冗余，现在我们要把这些冗余数据去掉。

## 解决方法

思路是这样的：首先应该按照conf_id和时间点来判断，进行分组（group by）查询，每组中再取一个就可以。分组是很简单，但是分组怎么取一个呢？我采用了中间表的形式。

### 创建中间表，并把数据导入中间表

```
create table perf_linux_t like perf_linux;
```

```
insert into perf_linux_t select * from perf_linux;
```
### 在中间表中增加一个字段，此字段是自增长的。
```
ALTER TABLE `perf_linux_t`
ADD COLUMN `auto_id` INT NOT NULL AUTO_INCREMENT ,
DROP PRIMARY KEY,
ADD PRIMARY KEY (`auto_id`);
```
### 删除无用数据

先查询一下

```
select min(auto_id) as auto_id from perf_linux_t group by insert_time ;
```

删除不对的数据

```
delete  from perf_linux_t where auto_id not in (select min(auto_id) as auto_id from perf_linux_t group by insert_time);
```

**慢着，输出错误：**
>You can't specify target table 'perf_linux_t' for update in FROM clause

不能删除啊，那只能再建一个中间表了。

### 再建中间表
```
create table tmp like perf_linux_t;
```

转变思路，不删除不符合的数据，而是把符合的数据存到这张新表中。

```
insert into tmp select * from perf_linux_t where auto_id in (select min(auto_id) as auto_id from perf_linux_t group by insert_time,conf_id );
```

把这张表中的无用列删除

```
ALTER TABLE `tmp`
DROP COLUMN `auto_id`,
DROP PRIMARY KEY;
```

### 导回数据

删除原来的数据

```
truncate table perf_linux;
```

插入数据

```
insert into perf_linux select * from tmp;
```

删除中间表

```
drop table tmp;
```

```
drop table perf_linux_t;
```


## 总结

通过这个方法，数据变为了42387条，删除了冗余的数据。但实际上程序的问题并没有完全定位，还需要观察才能定位问题。

而且我觉得我这个方法应该算比较土的方法，如果各位有更好的方式，希望不吝赐教。



参考：

[mysql删除重复记录语句的方法](http://www.jb51.net/article/23964.htm)
