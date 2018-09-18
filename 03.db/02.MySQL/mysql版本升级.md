# mysql版本升级
 


## 环境
mysql安装在centos上，需要升级。
mysql的版本是
```
mysql> select version();
+-----------+
| version() |
+-----------+
| 5.1.73    |
+-----------+
1 row in set (0.00 sec)
```
centos的版本是
```
lsb_release -a
LSB Version:	:base-4.0-amd64:base-4.0-noarch:core-4.0-amd64:core-4.0-noarch
Distributor ID:	CentOS
Description:	CentOS release 6.9 (Final)
Release:	6.9
Codename:	Final
```

## 下载新版本
访问```https://dev.mysql.com/doc/mysql-yum-repo-quick-guide/en/```，下载跟centos版本相对应的rpm文件。
这个rpm会修改库文件，为了后续yum安装用。
需要登录。
我下载了```mysql57-community-release-el6-11.noarch.rpm```
## 安装过程
安装RPM
```
rpm -Uvh mysql57-community-release-el6-11.noarch.rpm
```

安装mysql
```
yum repolist enabled | grep mysql

#yum install -y mysql-community-server

#service mysqld start
```
安装成功，但是启动失败。

## 解决启动失败的问题


```
mysql.user table is damaged. Please run mysql_upgrade
```
查到的解决方案
```
删除/var/lib/mysql下的三个文件：ibdata1、ib_logfile0、ib_logfile1；
然后使用如下命令，重新初始化mysql
mysqld --initialize --user=mysql
```
执行最后一句命令时，出现：
```
2017-12-14T01:50:57.809614Z 0 [ERROR] --initialize specified but the data directory has files in it. Aborting.
2017-12-14T01:50:57.809639Z 0 [ERROR] Aborting
```
看起来是还有数据文件，要手动删掉。
mysql配置文件是```/etc/my.cnf```
里面定义了数据文件和日志文件的位置
```
datadir=/var/lib/mysql
socket=/var/lib/mysql/mysql.sock

symbolic-links=0

log-error=/var/log/mysqld.log
pid-file=/var/run/mysqld/mysqld.pid
```
删除数据目录下全部文件
```
#rm -rf /var/lib/mysql/*
```
重新初始化mysql
```
#mysqld --initialize --user=mysql
```
启动mysql，成功。
```
#service mysqld start
```
修改mysql配置文件```/etc/my.cnf```，增加
```
[client]
default-character-set = utf8mb4
[mysql]
default-character-set = utf8mb4
[mysqld]
character-set-client-handshake = FALSE
character-set-server = utf8mb4
collation-server = utf8mb4_unicode_ci
init_connect='SET NAMES utf8mb4'
```

重新启动mysql，成功。
```
#service mysqld restart
```

##java数据库连接
修改为
```
jdbc:mysql://1.1.1.1:3306/hhtd_wechat?zeroDateTimeBehavior=convertToNull&&autoReconnect=true
```
注意，不要
