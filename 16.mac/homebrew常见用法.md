# homebrew常见用法

## 1. 安装
Homebrew是mac下安装软件的好帮手， 是使用 ruby 写的，采用 github 来存放信息库，很方便吧。

Ruby 已经内置，最好装上 Xcode，因为可能需要一些编译包。然后在终端执行以下命令。
```
$ ruby -e "$(curl -fsSLk https://gist.github.com/raw/323731/install_homebrew.rb)"
```
brew 安装的软件存放在 /usr/local/Cellar 中，同时会在 /usr/local/bin, /usr/local/sbin, /usr/local/lib 中创建链接。你可能需要将 /usr/local/sbin 添加到搜索路径中。
```
$ vim ~/.profile

PATH="$PATH:/usr/local/sbin"
export PATH
```

## 2. 使用
### (1) 更新库信息
```
$ brew update
```
### (2) 搜索软件
```
$ brew search redis
$ brew search jpg # 局部搜索
libjpg
$ brew search /^libjp[e]?g/ # 正则表达式搜索
```
### (3) 查看软件信息
```
$ brew info redis
```
### (4) 浏览软件主页
```
$ brew home redis
```
### (5) 安装软件
```
$ brew install redis
```
### (6) 删除软件
```
$ brew uninstall redis
```
### (7) 查看已经安装的软件
```
$ brew list
```
### (8) 查看某软件的安装文件
```
$ brew list redis
```
### (9) 查看某软件的 brew 安装脚本
```
$ brew cat redis
```
### (10) 删除所有软件升级后的遗留旧版本
```
$ brew cleanup
```
