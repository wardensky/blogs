# mysql on docker


## 启动镜像
```
# docker run --name test-mysql -p 3308:3306  -e MYSQL_ROOT_PASSWORD=123456 -d mysql
```

## 进入mysql的终端
```
# docker exec -it some-mysql mysql -uroot -p123456
```

**进入之后，要对用户进行授权，否则用navicat连接不上。**

```
GRANT ALL PRIVILEGES ON *.* TO 'root'@'localhost' IDENTIFIED BY '123456';
GRANT ALL PRIVILEGES ON *.* TO 'root'@'%' IDENTIFIED BY '123456';
```

如果是MySQL 8以后的版本


1 问题:当使用 grant 权限列表 on 数据库 to '用户名'@'访问主机' identified by '密码'; 时会出现"......near 'identified by '密码'' at line 1"这个错误

2 原因:因为新版的的mysql版本已经将创建账户和赋予权限的方式分开了

3解决办法:
```
创建账户:
create user '用户名'@'访问主机' identified by '密码';

赋予权限:

grant 权限列表 on 数据库 to '用户名'@'访问主机' ;
(修改权限时在后面加with grant option)
```
