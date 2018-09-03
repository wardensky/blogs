# Elastic Search操作入门

## 前言

Elastic Search是基于Lucene这个非常成熟的索引方案，另加上一些分布式的实现：集群，sharding，replication等。具体可以参考我同事写的[文章](https://www.zybuluo.com/big-bear/note/360909)。
本文主要介绍ES入门，包括最简单的操作和用C#代码操作ES。ES本身有很多复杂的功能，本文只是一个入门。


## 安装并启动ES

去```https://www.elastic.co/```下载zip文件，解压缩到本地硬盘。实现需要安装java环境。

双击elasticsearch.bat，启动ES。

打开浏览器，如果有类似如下输出，则启动成功。
```
{
  "name" : "Gorgeous George",
  "cluster_name" : "elasticsearch",
  "version" : {
    "number" : "2.3.5",
    "build_hash" : "90f439ff60a3c0f497f91663701e64ccd01edbb4",
    "build_timestamp" : "2016-07-27T10:36:52Z",
    "build_snapshot" : false,
    "lucene_version" : "5.5.0"
  },
  "tagline" : "You Know, for Search"
}
```
## 通过postman操作

postman是chrome的一个插件，可以作为http client模拟操作。

### 创建index
命令：
```
PUT http://localhost:9200/chzhao-index
```
返回
```
{
  "acknowledged": true
}
```
###存入数据
```
PUT http://localhost:9200/chzhao-index/employee/1

```
body内容
```
{
    "first_name" : "John",
    "last_name" :  "Smith",
    "age" :        25,
    "about" :      "I love to go rock climbing",
    "interests": [ "sports", "music" ]
}
```
返回
```
{
  "_index": "chzhao-index",
  "_type": "employee",
  "_id": "1",
  "_version": 2,
  "_shards": {
    "total": 2,
    "successful": 1,
    "failed": 0
  },
  "created": false
}
```

### 查询数据

#### 全部查询

查询全部数据
**命令**
```
http://localhost:9200/chzhao-index/employee/_search
```
返回
```
{
  "took": 13,
  "timed_out": false,
  "_shards": {
    "total": 5,
    "successful": 5,
    "failed": 0
  },
  "hits": {
    "total": 1,
    "max_score": 1,
    "hits": [
      {
        "_index": "chzhao-index",
        "_type": "employee",
        "_id": "1",
        "_score": 1,
        "_source": {
          "first_name": "chunhui",
          "last_name": "zhao",
          "age": 31,
          "about": "i love keke",
          "interests": [
            "go",
            "coding"
          ]
        }
      }
    ]
  }
}
```

#### 根据某一个字段查询

输入
```
http://localhost:9200/chzhao-index/employee/_search?q=last_name:Zhao
```

返回
```
{
  "took": 10,
  "timed_out": false,
  "_shards": {
    "total": 5,
    "successful": 5,
    "failed": 0
  },
  "hits": {
    "total": 1,
    "max_score": 0.30685282,
    "hits": [
      {
        "_index": "chzhao-index",
        "_type": "employee",
        "_id": "1",
        "_score": 0.30685282,
        "_source": {
          "first_name": "chunhui",
          "last_name": "zhao",
          "age": 31,
          "about": "i love keke",
          "interests": [
            "go",
            "coding"
          ]
        }
      }
    ]
  }
}
```

#### 使用DSL语句查询

**输入**
```
POST http://localhost:9200/chzhao-index/employee/_search
```
**参数**
参数要放在body里面。因为这样，不能用put方法。
```
{
    "query" : {
        "match" : {
            "last_name" : "Zhao"
        }
    }
}
```

**返回**
```
{
  "took": 12,
  "timed_out": false,
  "_shards": {
    "total": 5,
    "successful": 5,
    "failed": 0
  },
  "hits": {
    "total": 1,
    "max_score": 0.30685282,
    "hits": [
      {
        "_index": "chzhao-index",
        "_type": "employee",
        "_id": "1",
        "_score": 0.30685282,
        "_source": {
          "first_name": "chunhui",
          "last_name": "zhao",
          "age": 31,
          "about": "i love keke",
          "interests": [
            "go",
            "coding"
          ]
        }
      }
    ]
  }
}
```


## c#代码

c#代码应用了NEST等包，具体如下。
```
<?xml version="1.0" encoding="utf-8"?>
<packages>
  <package id="Elastic" version="1.0.3.0" targetFramework="net45" />
  <package id="Elasticsearch.Net" version="2.4.3" targetFramework="net45" />
  <package id="NEST" version="2.4.3" targetFramework="net45" />
  <package id="Newtonsoft.Json" version="9.0.1" targetFramework="net45" />
</packages>
```
核心代码
```
namespace ElasticSearchDemo
{

    public class Person
    {
        public string Id { get; set; }
        public string Firstname { get; set; }
        public string Lastname { get; set; }
        public string[] Chains { get; set; }
        public string Content { get; set; }
    }
}

```
```
using Nest;
using Newtonsoft.Json;
using System;
using System.Collections.Generic;

namespace ElasticSearchDemo
{
    class ESTool
    {
        private ElasticClient esClient = null;
        private string index = "test-index";
        public ESTool()
        {
            var node = new Uri("http://localhost:9200");
            ConnectionSettings settings = new ConnectionSettings(node);
            settings.DefaultIndex(index);
            this.esClient = new ElasticClient(settings);
        }
        public void Save()
        {
            IEnumerable<Person> persons = new List<Person>
            {
                new Person()
                {
                    Id = "4",
                    Firstname = "chunhui",
                    Lastname = "zhao",
                    Chains = new string[]{ "a","b","c" },
                    Content = "dad"
                },
                new Person()
                {
                    Id = "5",
                    Firstname = "keke",
                    Lastname = "zhao",
                    Chains = new string[]{ "x","y","z" },
                    Content = "daughter"
                }
            };
            this.esClient.IndexMany<Person>(persons, index);
        }

        public void Search()
        {
            var rs = this.esClient.Search<Person>(s => s.Index(index));
            Console.WriteLine(JsonConvert.SerializeObject(rs.Documents));

        }
    }
}

```

## 参考

[Elasticsearch 权威指南（中文版）](http://es.xiaoleilu.com/)
