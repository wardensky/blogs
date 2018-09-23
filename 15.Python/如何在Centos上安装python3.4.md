# 如何在Centos上安装python3.4

Centos上面默认的Python版本是2.6，本文介绍如何安装3.4版本。

## 0.下载前准备
需要安装以下库，不然会有问题。
```
yum -y install zlib-devel bzip2-devel openssl-devel ncurses-devel sqlite-devel readline-devel tk-devel gcc make
```

## 1. 下载Python3.4源码
```
# wget http://mirrors.sohu.com/python/3.4.1/Python-3.4.1.tar.xz
```
## 2. 解压缩并安装
```
# xz -d Python-3.4.1.tar.xz
# tar xf Python-3.4.1.tar -C /usr/local/src/
# cd /usr/local/src/Python-3.4.1/
# ./configure --prefix=/usr/local/python34
# make -j8 && make install
```
## 3. 安装的目录
默认情况下，python会安装在
```
/usr/local/python34
```

## 4. 安装PyMySQL
PyMySQL是python的mysql库，安装方法如下：
```
/usr/local/python34/bin/pip3 install PyMySQL
```
