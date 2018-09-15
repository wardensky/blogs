# Docker常用命令(1)

## docker images
列出所有docker的镜像

常见用法：
```
docker images
```


## docker ps
列出来所有的容器。


## docker run

常见用法：
```
docker run -d -p 8762:8762 service-add:latest

docker run -d -p 27017:27017 --name mongodb mongo
```


## docker rm

删除container。一个镜像必须运行在一个container里面。
常见用法：

```
docker rm -f 931a54761bec
```


## docker rmi

删除镜像。


```
docker rmi ad6c657858e
```


## docker build

用来编辑文件，需要一个dockerfile

常见用法：

```
docker build -t service-add:latest .
```

## docker pull
此命令从主服务器上拉取一个镜像到本地，其常见命令：
```
docker pull java8

```
这样会拉取下来最新的java8版本。也可以通过```:```制定版本。


## docker search

常见用法：
```
docker search java
```
输出：
选择里面有OFFICIAL的下载。



## docker kill


## docker inspect
常见用法

```
docker inspect f0558ad715ac
```


## docker attach
```
docker attach <id、container_name>
```

## docker exec

## docker start
```
docker start Name/ID  
```

## docker stop
```
docker stop Name/ID  
```
## docker kill
```
docker kill Name/ID  
```

## docker restart
```
docker restart Name/ID  
```


## docker commit

```
docker commit 614122c0aabb aoct/apache2
```
