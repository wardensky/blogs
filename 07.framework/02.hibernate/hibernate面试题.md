# hibernate面试题

## 什么是Hibernate的并发机制？怎么去处理并发问题？

Hibernate并发机制：

a、Hibernate的Session对象是非线程安全的,对于单个请求,单个会话,单个的工作单元(即单个事务,单个线程),它通常只使用一次, 然后就丢弃。

  如果一个Session 实例允许共享的话，那些支持并发运行的,例如Http request,session beans将会导致出现资源争用。

 如果在Http Session中有hibernate的Session的话,就可能会出现同步访问Http Session。只要用户足够快的点击浏览器的“刷新”,  就会导致两个并发运行的线程使用同一个Session。  
1
2
3
b、多个事务并发访问同一块资源,可能会引发第一类丢失更新，脏读，幻读，不可重复读，第二类丢失更新一系列的问题。

解决方案：设置事务隔离级别。
Serializable：串行化。隔离级别最高
Repeatable Read：可重复读
Read Committed：已提交数据读
Read Uncommitted：未提交数据读。隔离级别最差
设置锁：乐观锁和悲观锁。
乐观锁：使用版本号或时间戳来检测更新丢失,在的映射中设置 optimistic-lock=”all”可以在没有版本或者时间戳属性映射的情况下实现 版本检查，此时Hibernate将比较一行记录的每个字段的状态 行级悲观锁：Hibernate总是使用数据库的锁定机制，从不在内存中锁定对象！只要为JDBC连接指定一下隔 离级别，然后让数据库去搞定一切就够了。类LockMode 定义了Hibernate所需的不同的锁定级别：LockMode.UPGRADE,LockMode.UPGRADE_NOWAIT,LockMode.READ;

### update和saveOrUpdate的区别？

update()和saveOrUpdate()是用来对跨Session的PO进行状态管理的。
update()方法操作的对象必须是持久化了的对象。也就是说，如果此对象在数据库中不存在的话，就不能使用update()方法。
saveOrUpdate()方法操作的对象既可以使持久化了的，也可以使没有持久化的对象。如果是持久化了的对象调用saveOrUpdate()则会 更新数据库中的对象；如果是未持久化的对象使用此方法,则save到数据库中。

## hibernate的三种状态之间如何转换

当对象由瞬时状态(Transient)一save()时，就变成了持久化状态；
当我们在Session里存储对象的时候，实际是在Session的Map里存了一份， 也就是它的缓存里放了一份，然后，又到数据库里存了一份，在缓存里这一份叫持久对象(Persistent)。 Session 一 Close()了,它的缓存也都关闭了,整个Session也就失效了,这个时候，这个对象变成了游离状态(Detached)，但数据库中还是存在的。
当游离状态(Detached)update()时，又变为了持久状态(Persistent)。
当持久状态(Persistent)delete()时，又变为了瞬时状态(Transient), 此时，数据库中没有与之对应的记录。

## 比较hibernate的三种检索策略优缺点

1立即检索；
优点： 对应用程序完全透明，不管对象处于持久化状态，还是游离状态，应用程序都可以方便的从一个对象导航到与它关联的对象；
缺点： 1.select语句太多；2.可能会加载应用程序不需要访问的对象白白浪费许多内存空间；
2延迟检索：
优点： 由应用程序决定需要加载哪些对象，可以避免可执行多余的select语句，以及避免加载应用程序不需要访问的对象。因此能提高检索性能，并且能节省内存空间；
缺点： 应用程序如果希望访问游离状态代理类实例，必须保证他在持久化状态时已经被初始化；
3 迫切左外连接检索
优点： 1对应用程序完全透明，不管对象处于持久化状态，还是游离状态，应用程序都可以方便地冲一个对象导航到与它关联的对象。2使用了外连接，select语句数目少；
缺点： 1 可能会加载应用程序不需要访问的对象，白白浪费许多内存空间；2复杂的数据库表连接也会影响检索性能；

## 如何在控制台看到hibernate生成并执行的sql

在定义数据库和数据库属性的文件applicationConfig.xml里面，把hibernate.show_sql 设置为true
这样生成的SQL就会在控制台出现了
注意：这样做会加重系统的负担，不利于性能调优

## hibernate都支持哪些缓存策略

Read-only: 这种策略适用于那些频繁读取却不会更新的数据，这是目前为止最简单和最有效的缓存策略
* Read/write:这种策略适用于需要被更新的数据，比read-only更耗费资源，在非JTA环境下，每个事务需要在session.close和session.disconnect()被调用
* Nonstrict read/write: 这种策略不保障两个同时进行的事务会修改同一块数据，这种策略适用于那些经常读取但是极少更新的数据
* Transactional: 这种策略是完全事务化得缓存策略，可以用在JTA环境下

## hibernate里面的sorted collection 和ordered collection有什么区别

sorted collection是在内存中通过Java比较器进行排序的
ordered collection是在数据库中通过order by进行排序的

## Hibernate工作原理及为什么要用？

1.读取并解析配置文件
2.读取并解析映射信息，创建SessionFactory
3.打开Sesssion
4.创建事务Transation
5.持久化操作
6.提交事务
7.关闭Session
8.关闭SesstionFactory

为什么要用：
1. 对JDBC访问数据库的代码做了封装，大大简化了数据访问层繁琐的重复性代码。

Hibernate是一个基于JDBC的主流持久化框架，是一个优秀的ORM实现。他很大程度的简化DAO层的编码工作

hibernate使用Java反射机制，而不是字节码增强程序来实现透明性。

hibernate的性能非常好，因为它是个轻量级框架。映射的灵活性很出色。它支持各种关系数据库，从一对一到多对多的各种复杂关系。

## Hibernate是如何延迟加载?

当Hibernate在查询数据的时候，数据并没有存在与内存中，当程序真正对数据的操作时，对象才存在与内存中，就实现了延迟加载，他节省了服务器的内存开销，从而提高了服务器的性能。

## Hibernate中怎样实现类之间的关系?(如：一对多、多对多的关系)

类与类之间的关系主要体现在表与表之间的关系进行操作，它们都是对对象进行操作，我们程序中把所有的表与类都映射在一起，它们通过配置文件中的many-to-one、one-to-many、many-to-many、

## 说下Hibernate的缓存机制

内部缓存存在Hibernate中又叫一级缓存，属于应用事物级缓存

二级缓存：
a) 应用及缓存
b) 分布式缓存
条件：数据不会被第三方修改、数据大小在可接受范围、数据更新频率低、同一数据被系统频繁使用、非关键数据
c) 第三方缓存的实现

## Hibernate的查询方式

Sql、Criteria,objectcomposition
Hql：
1、 属性查询
2、 参数查询、命名参数查询
3、 关联查询
4、 分页查询
5、 统计函数

## 如何优化Hibernate？

1.使用双向一对多关联，不使用单向一对多
2.灵活使用单向一对多关联
3.不用一对一，用多对一取代
4.配置对象缓存，不使用集合缓存
5.一对多集合使用Bag,多对多集合使用Set
6. 继承类使用显式多态
7. 表字段要少，表关联不要怕多，有二级缓存撑腰

## Hibernate有哪几种查询数据的方式

3种：hql、条件查询QBC(QueryBy Criteria)、原生sql （通过createSQLQuery建立）

## 谈谈Hibernate中inverse的作用

inverse属性默认是false,就是说关系的两端都来维护关系。
比如Student和Teacher是多对多关系，用一个中间表TeacherStudent维护。Gp)i
如果Student这边inverse=”true”, 那么关系由另一端Teacher维护，就是说当插入Student时，不会操作TeacherStudent表（中间表）。只有Teacher插入或删除时才会触发对中间表的操作。所以两边都inverse=”true”是不对的，会导致任何操作都不触发对中间表的影响；当两边都inverse=”false”或默认时，会导致在中间表中插入两次关系。

## Detached Object（游离对象）有什么好处

Detached Object（游离对象）可以传递到任何层直到表现层而不是用任何DTO(DataTransfer Objects). 然后你还可以重新把游离对象赋给另外一个Session.

## JDBC hibernate 和 ibatis 的区别

jdbc:手动
手动写sql
delete、insert、update要将对象的值一个一个取出传到sql中,不能直接传入一个对象。
select:返回的是一个resultset，要从ResultSet中一行一行、一个字段一个字段的取出，然后封装到一个对象中，不直接返回一个对象。
ibatis的特点:半自动化
sql要手动写
delete、insert、update:直接传入一个对象
select:直接返回一个对象
hibernate:全自动
不写sql,自动封装
delete、insert、update:直接传入一个对象
select:直接返回一个对象

## 在数据库中条件查询速度很慢的时候,如何优化?

1.建索引
2.减少表之间的关联
3.优化sql，尽量让sql很快定位数据，不要让sql做全表查询，应该走索引,把数据量大的表排在前面
4.简化查询字段，没用的字段不要，已经对返回结果的控制，尽量返回少量数据

## 什么是SessionFactory,她是线程安全么？

SessionFactory 是Hibrenate单例数据存储和线程安全的，以至于可以多线程同时访问。一个SessionFactory 在启动的时候只能建立一次。SessionFactory应该包装各种单例以至于它能很简单的在一个应用代码中储存.

## Hibernate的五个核心接口

Configuration 接口：配置Hibernate，根据其启动hibernate，创建
SessionFactory 对象；
SessionFactory 接口：初始化Hibernate，充当数据存储源的代理，创建
session 对象，sessionFactory 是线程安全的，意味着它的同一个实例可以被应
用的多个线程共享，是重量级、二级缓存；
Session 接口：负责保存、更新、删除、加载和查询对象，是线程不安全的，
避免多个线程共享同一个session，是轻量级、一级缓存；
Transaction 接口：管理事务；
Query 和Criteria 接口：执行数据库的查询。

## 参考

- [hibernate面试题](https://blog.csdn.net/hi_kevin/article/details/7284155)
- [Hibernate常见面试题(转)](http://www.cnblogs.com/huajiezh/p/6415411.html)
- [java面试——Hibernate常见面试题](https://blog.csdn.net/qq1137623160/article/details/71194677)
