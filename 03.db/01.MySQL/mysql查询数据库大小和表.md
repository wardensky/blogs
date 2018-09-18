# mysql查询数据库大小和表


每个mysql都有一个库information_schema，里面有一张表TABLES存储了所有数据库表的信息，因此，可以从这张表中查看数据库大小和表大小



查询数据库大小
```
select concat(round((sum(data_length)+sum(index_length))/1024/1024/1024,2),'GB') as data  from information_schema.tables where table_schema='esb';

```
查询数据库中表大小
```
select concat(round((sum(data_length)+sum(index_length))/1024/1024/1024,2),'GB') as data  from information_schema.tables where table_schema='esb' and table_name='perf_service';
```
