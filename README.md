# blogs


## 一

这里是我这些年的一些博客的总结。

以前工作比较忙，难得有时间去总结，写博客。而且我写的博客大部分都是技术文章，技术文章就要有源代码。但不管哪个博客系统对源代码的管理都不友好。

github专门用来管理源代码，又可以直接在网页上显示markdown，所以逐步把之前写的一些博客拿过来。

现在翻看之前写的博客，很多知识的理解都是不对的，或者太片面，或者太肤浅，或者有些结论根本就是错误的。当然，我这个时间点写的东西，同样存在这样的问题。但进步本来就是不断发现自己错误的过程。

一方面自己水平有限，另外一方面时间也有限，所以博客的水平基本都比较低，大部分是sample code的级别。

## 二. 目录


### 01. java

#### 01. java基础知识

  - equals与hashcode
  - stringbuffer与Stringbuilder
  - map有哪些？其特点和使用场景？
  - 动态代理
  - 反射
  - session  cookie
  - nginx，apache 实际项目能做哪些？（鉴权，转发，缓存，反向代理等）和tomcat什么关系？最少了解
  - ajax跨域
  - jsonp
  - cache和buffer
  - 自动装箱与拆箱

#### 02.jvm基础

  - 内存模型
  - 内存管理调优
  - 内存参数设置
  - jstat
  - jmap
  - jstack
  - gc
  - class结构
  - classloader
  - Java虚拟机内存模型特点和作用
  - 程序计数器
  - Java虚拟机栈
  - 本地方法区
  - 堆
  - 方法区
  - 对象创建过程
  - 对象访问过程
  - 对象的内存结构
  - 垃圾收集算法
  - 如何判定哪些对象需要回收？
  - 对象内存分配策略
  - 分配担保机制
  - 垃圾收集器的比较
  - 类加载的时机
  - 类加载过程
  - 双亲委派模型
  - stackoverflowerror outofmemoryerror


#### 03.java多线程

  - 线程池
  - synchronized
  - ReentrantLock
  - volatile
  - Atomics
  - wait/notify/notifyAll
  - CountDownLatch
  - CyclicBarrier
  - Semaphore
  - Exchanger
  - Future/FutureTask
  - ThreadLocal
  - Java容器
  - Java并发容器


#### 04.java源码

  - ArrayList
  - HashMap
  - CopyOnWriteArrayList
  - concurrentHashMap
  - arrayblockingqueue
  - String
  - Integer


### 02. design pattern

### 03. db

 - 基础sql
 - mysql优化
 - 分库分表
 - 索引优化
 - 二阶段分布式事务
 - 自动扩容
 - 存储引擎
 - binlog
 - 半同步
 - mysql的事务是如何实现的
 - 为何要有事务隔离级别

### 04. distributed


 - 分布式事务
 - 缓存
 - 队列
 - 一致性hash
 - 分布式系统架构
 - rpc
 - 微服务
 - 服务发现
 - 降级
 - zookeeper
 - kafaka
 - 缓存架构与设计
 - redis数据结构
 - 缓存击穿
 - 任务管理
 - thrift
 - 布隆过滤器
 - 分布式锁

### 05. network


 - nio
 - netty
 - dubbo
 - https
 - http2
 - epoll
 - libuv
 - 三次握手，四次挥手

### 06. algorithm
 - 环形链表
 - 二叉树、平衡二叉树
 - B+树
 - 红黑树
 - LRU
 - 单链表复制
 - 归并排序、交换排序（冒泡、快排）、选择排序、插入排序

### 07. framework

#### 01.spring

- 01.[第一个Spring项目](https://github.com/wardensky/blogs/blob/master/07.framework/spring/01.第一个Spring项目.md)
- 02.[通过构造器注入Bean](https://github.com/wardensky/blogs/blob/master/07.framework/spring/02.通过构造器注入Bean.md)
- 03.[Spring命名空间与Bean作用域](https://github.com/wardensky/blogs/blob/master/07.framework/spring/03.Spring命名空间与Bean作用域.md)
- 04.[注入Bean属性](https://github.com/wardensky/blogs/blob/master/07.framework/spring/04.注入Bean属性.md)
- 05.[自动装配Bean属性](https://github.com/wardensky/blogs/blob/master/07.framework/spring/05.自动装配Bean属性.md)
- 06.[使用注解装配](https://github.com/wardensky/blogs/blob/master/07.framework/spring/06.使用注解装配.md)
- 07.[自动检测Bean](https://github.com/wardensky/blogs/blob/master/07.framework/spring/07.自动检测Bean.md)
- 08.[基于Java配置而不是XML](https://github.com/wardensky/blogs/blob/master/07.framework/spring/08.基于Java配置而不是XML.md)
- 09.[Bean的生命周期](https://github.com/wardensky/blogs/blob/master/07.framework/spring/09.Bean的生命周期.md)
- 10.[到底什么是IOC和DI](https://github.com/wardensky/blogs/blob/master/07.framework/spring/10.到底什么是IOC和DI.md)
- 11.[BeanFactory和ApplicationContext联系和区别](https://github.com/wardensky/blogs/blob/master/07.framework/spring/11.BeanFactory和ApplicationContext联系和区别.md)
- 12.[AOP初探](https://github.com/wardensky/blogs/blob/master/07.framework/spring/12.AOP初探.md)
- 13.[Spring JDBC](https://github.com/wardensky/blogs/blob/master/07.framework/spring/13.Spring-JDBC.md)
- 14.[spring事务](https://github.com/wardensky/blogs/blob/master/07.framework/spring/14.spring事务.md)


#### 02.hibernate



#### 03.jfinal

#### 04.struts2

#### 05.mybatis

### 08. nosql

#### 01.redis

 - 基础数据结构
 - 用法

#### 02.mongodb



### 09. linux

### 10. docker

### 11. maven
