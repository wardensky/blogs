# mac安装jdk

## 下载

## 安装

## 配置环境变量

```
JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk1.8.0_181.jdk/Contents/Home
export JAVA_HOME
CLASSPATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar
export CLASSPATH
PATH=$PATH:$JAVA_HOME/bin
export PATH
```
