# 使用Docker一步搞定ZooKeeper集群的搭建

## 背景
原来学习 ZK 时, 我是在本地搭建的伪集群, 虽然说使用起来没有什么问题, 但是总感觉部署起来有点麻烦. 刚好我发现了 ZK 已经有了 Docker 的镜像了, 于是就尝试了一下, 发现真是爽爆了, 几个命令就可以搭建一个完整的 ZK 集群. 下面我简单记录一下使用 Docker 搭建 ZK 集群的一些步骤.

## 镜像下载
hub.docker.com 上有不少 ZK 镜像, 不过为了稳定起见, 我们就使用官方的 ZK 镜像吧.
首先执行如下命令:
```
docker pull zookeeper
```
当出现如下结果时, 表示镜像已经下载完成了:
```
>>> docker pull zookeeper
Using default tag: latest
latest: Pulling from library/zookeeper

e110a4a17941: Pull complete
a696cba1f6e8: Pull complete
bc427bd93e95: Pull complete
c72391ae24f6: Pull complete
40ab409b6b34: Pull complete
d4bb8183b85d: Pull complete
0600755f1470: Pull complete
Digest: sha256:12458234bb9f01336df718b7470cabaf5c357052cbcb91f8e80be07635994464
Status: Downloaded newer image for zookeeper:latest
```
## ZK 镜像的基本使用
### 启动 ZK 镜像
```
>>> docker run --name my_zookeeper -d zookeeper:latest
```
这个命令会在后台运行一个 zookeeper 容器, 名字是 my_zookeeper, 并且它默认会导出 2181 端口.
接着我们使用:
```
docker logs -f my_zookeeper
```
这个命令查看 ZK 的运行情况, 输出类似如下内容时, 表示 ZK 已经成功启动了:

```
>>> docker logs -f my_zookeeper
ZooKeeper JMX enabled by default
Using config: /conf/zoo.cfg
...
2016-09-14 06:40:03,445 [myid:] - INFO  [main:NIOServerCnxnFactory@89] - binding to port 0.0.0.0/0.0.0.0:2181
```
### 使用 ZK 命令行客户端连接 ZK
因为刚才我们启动的那个 ZK 容器并没有绑定宿主机的端口, 因此我们不能直接访问它. 但是我们可以通过 Docker 的 link 机制来对这个 ZK 容器进行访问. 执行如下命令:
```
docker run -it --rm --link my_zookeeper:zookeeper zookeeper zkCli.sh -server zookeeper
```
如果对 Docker 有过了解的话, 那么对上面的命令一定不会陌生了.
这个命令的含义是:

启动一个 zookeeper 镜像, 并运行这个镜像内的 zkCli.sh 命令, 命令参数是 "-server zookeeper"
将我们先前启动的名为 my_zookeeper 的容器连接(link) 到我们新建的这个容器上, 并将其主机名命名为 zookeeper
当我们执行了这个命令后, 就可以像正常使用 ZK 命令行客户端一样操作 ZK 服务了.

ZK 集群的搭建
因为一个一个地启动 ZK 太麻烦了, 所以为了方便起见, 我直接使用 docker-compose 来启动 ZK 集群.
首先创建一个名为 docker-compose.yml 的文件, 其内容如下:
```
version: '2'
services:
    zoo1:
        image: zookeeper
        restart: always
        container_name: zoo1
        ports:
            - "2181:2181"
        environment:
            ZOO_MY_ID: 1
            ZOO_SERVERS: server.1=zoo1:2888:3888 server.2=zoo2:2888:3888 server.3=zoo3:2888:3888

    zoo2:
        image: zookeeper
        restart: always
        container_name: zoo2
        ports:
            - "2182:2181"
        environment:
            ZOO_MY_ID: 2
            ZOO_SERVERS: server.1=zoo1:2888:3888 server.2=zoo2:2888:3888 server.3=zoo3:2888:3888

    zoo3:
        image: zookeeper
        restart: always
        container_name: zoo3
        ports:
            - "2183:2181"
        environment:
            ZOO_MY_ID: 3
            ZOO_SERVERS: server.1=zoo1:2888:3888 server.2=zoo2:2888:3888 server.3=zoo3:2888:3888
```

这个配置文件会告诉 Docker 分别运行三个 zookeeper 镜像, 并分别将本地的 2181, 2182, 2183 端口绑定到对应的容器的2181端口上.
ZOO_MY_ID 和 ZOO_SERVERS 是搭建 ZK 集群需要设置的两个环境变量, 其中 ZOO_MY_ID 表示 ZK 服务的 id, 它是1-255 之间的整数, 必须在集群中唯一. ZOO_SERVERS 是ZK 集群的主机列表.

接着我们在 docker-compose.yml 当前目录下运行:
```
COMPOSE_PROJECT_NAME=zk_test docker-compose up
```
即可启动 ZK 集群了.
执行上述命令成功后, 接着在另一个终端中运行 docker-compose ps 命令可以查看启动的 ZK 容器:
```
>>> COMPOSE_PROJECT_NAME=zk_test docker-compose ps
Name              Command               State           Ports
----------------------------------------------------------------------
zoo1   /docker-entrypoint.sh zkSe ...   Up      0.0.0.0:2181->2181/tcp
zoo2   /docker-entrypoint.sh zkSe ...   Up      0.0.0.0:2182->2181/tcp
zoo3   /docker-entrypoint.sh zkSe ...   Up      0.0.0.0:2183->2181/tcp
```
注意, 我们在 "docker-compose up" 和 "docker-compose ps" 前都添加了 COMPOSE_PROJECT_NAME=zk_test 这个环境变量, 这是为我们的 compose 工程起一个名字, 以免与其他的 compose 混淆.
## 使用 Docker 命令行客户端连接 ZK 集群
通过 docker-compose ps 命令, 我们知道启动的 ZK 集群的三个主机名分别是 zoo1, zoo2, zoo3, 因此我们分别 link 它们即可:
```
docker run -it --rm \
        --link zoo1:zk1 \
        --link zoo2:zk2 \
        --link zoo3:zk3 \
        --net zktest_default \
        zookeeper zkCli.sh -server zk1:2181,zk2:2181,zk3:2181
```
### 通过本地主机连接 ZK 集群
因为我们分别将 zoo1, zoo2, zoo3 的 2181 端口映射到了 本地主机的2181, 2182, 2183 端口上, 因此我们使用如下命令即可连接 ZK 集群了:
```
zkCli -server localhost:2181,localhost:2182,localhost:2183
```

### 查看集群
我们可以通过 nc 命令连接到指定的 ZK 服务器, 然后发送 stat 可以查看 ZK 服务的状态, 例如:
```
>>> echo stat | nc 127.0.0.1 2181
Zookeeper version: 3.4.9-1757313, built on 08/23/2016 06:50 GMT
Clients:
 /172.18.0.1:49810[0](queued=0,recved=1,sent=0)

Latency min/avg/max: 5/39/74
Received: 4
Sent: 3
Connections: 1
Outstanding: 0
Zxid: 0x200000002
Mode: follower
Node count: 4
>>> echo stat | nc 127.0.0.1 2182
Zookeeper version: 3.4.9-1757313, built on 08/23/2016 06:50 GMT
Clients:
 /172.18.0.1:50870[0](queued=0,recved=1,sent=0)

Latency min/avg/max: 0/0/0
Received: 2
Sent: 1
Connections: 1
Outstanding: 0
Zxid: 0x200000002
Mode: follower
Node count: 4
>>> echo stat | nc 127.0.0.1 2183
Zookeeper version: 3.4.9-1757313, built on 08/23/2016 06:50 GMT
Clients:
 /172.18.0.1:51820[0](queued=0,recved=1,sent=0)

Latency min/avg/max: 0/0/0
Received: 2
Sent: 1
Connections: 1
Outstanding: 0
Zxid: 0x200000002
Mode: leader
Node count: 4
```

通过上面的输出, 我们可以看到, zoo1, zoo2 都是 follower, 而 zoo3 是 leader, 因此证明了我们的 ZK 集群确实是搭建起来了.

## 参考

- [使用 Docker 一步搞定 ZooKeeper 集群的搭建][11]

[11]:https://segmentfault.com/a/1190000006907443
