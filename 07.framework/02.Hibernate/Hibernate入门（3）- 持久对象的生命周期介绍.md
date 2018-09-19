# Hibernate入门（3）- 持久对象的生命周期介绍


在hibernate中对象有三种状态：瞬时态(Transient)、 持久态(Persistent)、脱管态或游离态(Detached)。处于持久态的对象也称为PO(Persistence Object)，瞬时对象和脱管对象也称为VO（Value Object）。


## 瞬时状态
由new操作符创建，且尚未与Hibernate Session关联的对象被认定为瞬时（Transient）的。瞬时（Transient）对象不会被持久化到数据库中，也不会被赋予持久化标识（identifier）。如果瞬时（Transient）对象在程序中没有被引用，它会被垃圾回收器（garbage collector）销毁。使用Hibernate Session可以将其变为持久（Persistent）状态。（Hibernate会自动执行必要的SQL语句）。
瞬时状态的特点有：
1、 与数据库中的记录没有任何关联，也就是没有与其相关联的数据库记录。
2、 与Session没有任何关系，也就是没有通过Session对象的实例对其进行任何持久化的操作。

举例：
```
User user=new User(); //user是一个瞬时对象，在数据库的表中是没有记录和该对象相对应的。和session没有关系。
user.setName(“ddd”);
user.setBirthday(new Date());
session.save(user); //持久化状态
```


## 持久状态
持久（Persistent）是实例在数据库中有对应的记录，并拥有一个持久化标识（identifier）。持久（Persistent）的实例可能是刚被保存的，或刚被加载的，无论哪一种，按定义，它存在于相关联的Session作用范围内。Hibernate会检测到处于持久（Persistent）状态的对象的任何改动，在当前操作单元（unit of work）执行完毕时将对象数据（state）与数据库同步（synchronize）。开发者不需要手动执行Update。将对象从持久（Persistent）状态编程瞬时（Transient）状态同样也不需要手动执行delete语句。
持久对象具有如下特点：
1、 和session实例关联；
2、 在数据库中有与之关联的记录。
3、 Hibernate会根据持久态对象的属性的变化而改变数据库中的相应记录。
举例：
```
Session session = factory.openSession();    
Transaction tx = session.beginTransaction();    
session.save(stu); // persistent持久化状态    
System.out.println(stu);    
tx.commit();    
session.close();    //执行close()方法之后，就会由持久对象转换成脱管对象
System.out.println(stu); // 脱管对象
```
## 脱管状态
与持久（Persistent）状态对象关联的Session被关闭后，对象就变为脱管（Detached）的。对脱管（Detached）对象的引用依然有效，对象可继续被修改。脱管（Detached）对象如果重新关联到某个新的Session上，会再次转变为持久（Transistent）的（在脱管Detached其间的改动将被持久化到数据库）。这个功能使得一种编程模型，即中间会给用户思考时间（user think-time）的长时间运行的操作单元（unit of work）.
脱管对象拥有数据库的识别值，可通过update()、saveOrUpdate()等方法，转变成持久对象。
脱管对象具有如下特点：
1、 本质上与瞬时对象相同，在没有任何变量引用它时，JVM会在适当的时候将它回收；
2、 比瞬时对象多了一个数据库记录标识值。
3、 不在于Session相关联。
4、 脱管对象一定是由持久态对象转化而来。


## 状态之间的转换
![hibernate对象状态间转换]](../images/hibernate_states.png)
对以上图形的解析：
1、当一个对象被new了以后此对象处于瞬时态（Transient）；
2、然后对此对象执行session的save() 或者saveOrUpdate()方法后，此对象被放入session的一级缓存进入持久态．
2、当再对此对象执行evict()/close()/clear()的操作后此对象进入游离态（Detached）
4、游离态（Detached）和瞬时态（Transient）的对象由于没有被session管理会在适当的时机被java的垃圾回收站（garbage）回收．
5、执行session的get()/load()/find()/iternte()等方法从数据库里查询的到的对象,处于持久态（Persistent）.  
6、当对数据库中的纪录进行update()/saveOrUpdate()/lock()操作后游离态的对象就过渡到持久态　
7、处于持久态（Persistent）与游离态（Detached）的对象在数据库中都有对应的记录．
8、瞬时态（Transient）与游离态(Detached)的对象都可以被回收可是瞬时态的对象在数据库中没有对应的纪录,而游离态的对象在数据库中有对用的纪录。
