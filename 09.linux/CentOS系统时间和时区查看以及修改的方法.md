# CentOS 系统时间和时区查看以及修改的方法

## 时区

### 查看时区

```
date -R
```


### 修改时区：

（将Asia/shanghai-上海时区写入当前时区）

```
cp -f /usr/share/zoneinfo/Asia/Shanghai     /etc/localtime
```

提示是否覆盖,输入Y回车,

然后#date
查看时区和时间（CST,中国时区）
