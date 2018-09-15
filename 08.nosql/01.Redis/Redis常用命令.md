# Redis常用命令

客户端常用命令keys、exists、del、expire、move、persist、randomkey、rename、type

服务器端常用命令ping、echo、select、quit、dbsize、info、monitor、config get、flushdb 、flushall


## 客户端常用命令

### keys

查找匹配中的key

```
127.0.0.1:6379> help keys

  KEYS pattern
  summary: Find all keys matching the given pattern
  since: 1.0.0
  group: generic

```

```
127.0.0.1:6379> keys *
1) "1111"
2) "1121"
3) "z"
4) "1221"
```

```
127.0.0.1:6379> keys 1*1
1) "1111"
2) "1121"
3) "1221"
```

### exists

判断一个key是否存在


```
127.0.0.1:6379> help exists

  EXISTS key [key ...]
  summary: Determine if a key exists
  since: 1.0.0
  group: generic
```

```
127.0.0.1:6379> exists 1*
(integer) 0
127.0.0.1:6379> exists 1111
(integer) 1
```

### del

删除一个key

```
127.0.0.1:6379> HELP DEL

  DEL key [key ...]
  summary: Delete a key
  since: 1.0.0
  group: generic
```

```
127.0.0.1:6379> keys *
1) "1111"
2) "1121"
3) "z"
4) "1221"
127.0.0.1:6379> del z
(integer) 1
127.0.0.1:6379> keys *
1) "1111"
2) "1121"
3) "1221"
```

### expire

设置超时时间

```
127.0.0.1:6379> HELP EXPIRE

  EXPIRE key seconds
  summary: Set a key's time to live in seconds
  since: 1.0.0
  group: generic
```

例子

```
127.0.0.1:6379> SET 222 BBB
OK
127.0.0.1:6379> GET 222
"BBB"
127.0.0.1:6379> EXPIRE 222 10
(integer) 1
127.0.0.1:6379> GET 222
"BBB"
//10秒之后
127.0.0.1:6379> GET 222
(nil)
```

### move


```
127.0.0.1:6379> help move

  MOVE key db
  summary: Move a key to another database
  since: 1.0.0
  group: generic
```

### persist


```
127.0.0.1:6379> HELP PERSIST

  PERSIST key
  summary: Remove the expiration from a key
  since: 2.2.0
  group: generic
```

### randomkey

```
127.0.0.1:6379> HELP randomkey

  RANDOMKEY -
  summary: Return a random key from the keyspace
  since: 1.0.0
  group: generic
```
```
127.0.0.1:6379> RANDOMKEY
"1221"
127.0.0.1:6379> RANDOMKEY
"1111"
```

### rename

```
127.0.0.1:6379> HELP RENAME

  RENAME key newkey
  summary: Rename a key
  since: 1.0.0
  group: generic

```

### type

```
127.0.0.1:6379> help type

  TYPE key
  summary: Determine the type stored at key
  since: 1.0.0
  group: generic
```

## 服务器端常用命令

###ping


```
127.0.0.1:6379> HELP PING

  PING [message]
  summary: Ping the server
  since: 1.0.0
  group: connection

```

### echo

```
127.0.0.1:6379> help echo

  ECHO message
  summary: Echo the given string
  since: 1.0.0
  group: connection
```

### select

```
127.0.0.1:6379> HELP SELECT

  SELECT index
  summary: Change the selected database for the current connection
  since: 1.0.0
  group: connection

```

### quit

```
127.0.0.1:6379> help quit

  QUIT -
  summary: Close the connection
  since: 1.0.0
  group: connection
```

### dbsize

```
127.0.0.1:6379> HELP DBSIZE

  DBSIZE -
  summary: Return the number of keys in the selected database
  since: 1.0.0
  group: server
```
### info

```
127.0.0.1:6379> HELP INFO

  INFO [section]
  summary: Get information and statistics about the server
  since: 1.0.0
  group: server
```

### monitor

```
127.0.0.1:6379> help monitor

  MONITOR -
  summary: Listen for all requests received by the server in real time
  since: 1.0.0
  group: server
```
### config get

```
127.0.0.1:6379> HELP CONFIG GET

  CONFIG GET parameter
  summary: Get the value of a configuration parameter
  since: 2.0.0
  group: server
```

### flushdb

```
127.0.0.1:6379> help flushdb

  FLUSHDB -
  summary: Remove all keys from the current database
  since: 1.0.0
  group: server

```

### flushall

```
127.0.0.1:6379> help flushall

  FLUSHALL -
  summary: Remove all keys from all databases
  since: 1.0.0
  group: server
```
## 参考

- [Redis常见命令-客户端，服务器--实例解析](https://blog.csdn.net/basycia/article/details/52176093)
