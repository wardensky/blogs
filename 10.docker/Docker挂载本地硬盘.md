# Docker挂载本地硬盘
 


## 通过命令行挂载
```
docker run --name case1:3.0 -it -v /Users/zch/tmp:/data ubuntu /bin/bash
```

上面的命令，表示在Ubuntu上面，把目录```/Users/zch/tmp```挂载到```/data```，同时启动```bash```

## 通过Dockerfile挂载

Dockerfile内容
```
FROM ubuntu
VOLUME ["/tmp"]
```

编译成镜像

```

```

看一下镜像

```

```

输出

```

```
运行

```

```
