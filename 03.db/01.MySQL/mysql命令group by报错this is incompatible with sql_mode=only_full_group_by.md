# mysql命令group by报错this is incompatible with sql_mode=only_full_group_by


在mysql 工具 搜索或者插入数据时报下面错误：

> RROR 1055 (42000): Expression #1 of SELECT list is not in GROUP BY clause and contains nonaggregated column ‘database_tl.emp.id’ which is not functionally dependent on columns in GROUP BY clause; this is incompatible with sql_mode=only_full_group_by

临时解决，默认关掉ONLY_FULL_GROUP_BY：
```
set @@GLOBAL.sql_mode='';
set sql_mode ='STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION';
```

长期解决，修改my.cnf

```
sql_mode = NO_ENGINE_SUBSTITUTION,STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER
```

重启mysql
