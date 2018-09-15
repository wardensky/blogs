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
