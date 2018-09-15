# No directory问题的处理


在ubuntu 14的版本中，安装了mysql 后，启动时，程序提示 “No directory, logging in with HOME=/”

```
 root@xxx:/etc/mysql# service mysql restart
 * Stopping MySQL Community Server 5.7.11
...
 * MySQL Community Server 5.7.11 is stopped
 * Re-starting MySQL Community Server 5.7.11
No directory, logging in with HOME=/
..
 * MySQL Community Server 5.7.11 is started
 ```

解决方法：

```
sudo service mysql stop

sudo usermod -d /var/lib/mysql/ mysql

sudo service mysql start
```
