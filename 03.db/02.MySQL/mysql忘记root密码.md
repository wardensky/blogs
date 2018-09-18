# mysql忘记root密码
@(mysql)

redhat用rpm安装完mysql后，找不到root默认密码，只好重置root密码
```
#service mysqld stop   
#/usr/sbin/mysqld --user=mysql --skip-grant-tables &
#mysql  
mysql>use mysql;
mysql>UPDATE user SET password=PASSWORD('123456') where User='root' and host='localhost';
mysql>FLUSH PRIVILEGES;
mysql>exit
```
出现You must SET PASSWORD before executing this statement
```
SET PASSWORD = PASSWORD('123456');

mysql> create user root@'%' identified by '123456';

mysql> grant all privileges on *.* to 'root'@'%' with grant option;

mysql> flush privileges;

```


上面是老版本的方法，在新版本中，要修改如下：

```
#service mysqld stop   
#/usr/sbin/mysqld --user=mysql --skip-grant-tables &
#mysql  
mysql>use mysql;
mysql>FLUSH PRIVILEGES;
mysql>alter user 'root'@'localhost' identified by 'password';
mysql>exit

```
