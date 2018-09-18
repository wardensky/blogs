# Hibernate的SessionFactory




## 什么是sessionfactory

SessionFactory接口负责初始化Hibernate。它充当数据存储源的代理，并负责创建Session对象。这里用到了工厂模式。需要注意的是SessionFactory并不是轻量级的，因为一般情况下，一个项目通常只需要一个SessionFactory就够，当需要操作多个数据库时，可以为每个数据库指定一个SessionFactory。----百度百科

我的理解是，其实顾名思义，sessionfactory，就是用来创建session会话（具体接下来讲）的工厂。想想有一个特别大的工厂，专门用来在你需要使用session的时候，让它帮你创建一个就好了。。但是需要注意一下就是，一般来讲，一个web工程（系统），如果涉及到的是一个数据库，那么本工程就只需要一个sessionfactory就够了。这个工程所涉及到的所有的session，都交由这个sessionfactory来管理。

接下来，创建一个sessionfactory，一般有两种创建方式，

1. 从XML文件读取配置信息构建SessionFactory，
2. 从Java属性文件读取配置信息构建SessionFactory

### 实例化Configuration对象，默认读取src目录下的Hibernate.cfg.xml，配置文件

```
Configuration config = new Configuration().configure();
```

或者在configure();中指明文件名称和路径

```
Configuration config = new Configuration().configure("Hibernate.cfg.xml");
```


### 现在这个config对象，已经包括所有Hibernate运行期的参数，通过Configuration实例的buildSessionFactory()方法可以构建一个惟一的SessionFactory：

```
SessionFactory sessionFactory = config.buildSessionFactory();
```

### 得到这个sessionfactory对象之后，就可以开始另外的话题了

## 什么是Session

提起来Session的话，首先想到的是http这个东东的session，想到了http这个无状态的协议，没办法保存任何访问对象的信息，所以就出现了session这个东东，用来记录访问者的一些信息。

在Hibernate中的session并不是http中所说的session，一般把HttpSession对象称为用户会话。

而Hibernate中的Session呢？是用来表示，应用程序和数据库的一次交互（会话）。在这个Session中，包含了一般的持久化方法（CRUD）。而且，Session是一个轻量级对象（线程不安全），通常将每个Session实例和一个数据库事务绑定，也就是每执行一个数据库事务，都应该先创建一个新的Session实例，在使用Session后，还需要关闭Session。

接下来，创建一个Session对象，依据第二步得到的sessionfactory对象。


```
Session session=sessionFactory.openSession();
```

然后得到这个session之后，就可以使用它来进行数据库的具体操作了，给一个简单的代码。多说一句，一般来说在使用session的时候，都会用到事务，尤其是需要对数据做修改的时候。上代码：


```
public void save(Student student) {
	Transaction transaction= getSession().beginTransaction();  //开启事务
	getSession().save(student);//只要传一个Student的对象实例		
	transaction.commit();  //事务提交
	getSession().close();//关闭session
}

```
