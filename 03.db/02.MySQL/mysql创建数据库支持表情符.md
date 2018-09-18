# mysql创建数据库支持表情符


```
CREATE DATABASE IF NOT EXISTS hhtd_wechat DEFAULT CHARSET utf8 COLLATE utf8_general_ci;
```

下面这个支持表情符号的，但必须是5.5.3以上，我现在都是用5.7

```
CREATE DATABASE IF NOT EXISTS hhtd_wechat DEFAULT CHARSET utf8mb4 COLLATE utf8mb4_unicode_ci;
grant all privileges on *.* to 'root'@'localhost' identified by 'hhtdpwd' with grant option;
flush privileges;
```
