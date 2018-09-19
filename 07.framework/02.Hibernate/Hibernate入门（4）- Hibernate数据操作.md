# Hibernate入门（4）- Hibernate数据操作


## Hibernate加载数据

### Session.get(Class clazz,  Serializable id)

* clazz:需要加载对象的类，例如：User.class

* id:查询条件(实现了序列化接口的对象)：

返回值： 此方法返回类型为Object，也就是对象，然后我们再强行转换为需要加载的对象就可以了。

如果数据不存在，则返回null;

注：执行此方法时立即发出查询SQL语句。加载User对象

加载数据库中存在的数据，代码如下
```
@Test
public void testLoad() {
    Session session = null;
    Transaction tx = null;

    try {
        session = HibernateUtil.getSession();
        tx = session.beginTransaction();

        User user = (User) session.get(User.class,"4028802c46d255740146d25576320000");

        // 数据加载完后的状态为persistent状态。数据将与数据库同步。
        System.out.println("user.name=" + user.getName());

        // 因为此的user为persistent状态，所以数据库进行同步为龙哥。
        user.setName("龙哥");
        session.getTransaction().commit();
    } catch (HibernateException e) {
        e.printStackTrace();
        session.getTransaction().rollback();
    } finally {
        if (session != null) {
            if (session.isOpen()) {
                session.close();
            }
        }
    }
}
```
### Object Session.load(Class clazz, Serializable id)

* clazz:需要加载对象的类，例如：User.class

* id:查询条件(实现了序列化接口的对象)：例"4028802c46d255740146d25576320000"字符串已经实现了序列化接口。

* 此方法返回类型为Object，但返回的是代理对象。

* 执行此方法时不会立即发出查询SQL语句。只有在使用对象时，它才发出查询SQL语句，加载对象。

* 因为load方法实现了lazy(称为延迟加载、赖加载)

* 延迟加载：只有真正使用这个对象的时候，才加载(才发出SQL语句)

* Hibernate延迟加载实现原理是代理方式。

* 采用load()方法加载数据，如果数据库中没有相应的记录，则会抛出异常对象不找到(org.Hibernate.ObjectNotFoundException)

复制代码
```
@Test
public void testLoad() {
    Session session = null;
    Transaction tx = null;
    try {
        session = HibernateUtil.getSession();
        tx = session.beginTransaction();

        User user = (User) session.load(User.class,
                "4028802c46d255740146d25576320000");
        // 只有在使用对象时，它才发出查询SQL语句，加载对象。
        System.out.println("user.name=" + user.getName());
        // 因为此的user为persistent状态，所以数据库进行同步为龙哥。
        user.setName("发哥");
        session.getTransaction().commit();
    } catch (HibernateException e) {
        e.printStackTrace();
        session.getTransaction().rollback();
    } finally {
        if (session != null) {
            if (session.isOpen()) {
                session.close();
            }
        }
    }
}
```

### Hibernate两种加载数据方式的区别

- get()方法默认不支持lazy(延迟加载)功能，而load支持延迟加载
- get()方法在查询不到数据时，返回null,而load因为支持延迟加载，只有在使用对象时才加载，所以如果数据库中不在数据load会抛出异常(org.Hibernate.ObjectNotFoundException)。
- get()和load()只根据主键查询，不能根据其它字段查询，如果想根据非主键查询，可以使用HQL

## Hibernate更新数据
建立使用Hibernate进行更新数据时，先加载数据，然后再修改后更新。
否则一些字段可以会被null替换。
```
@Test
public void testDelete() {
    Session session = null;
    Transaction tx = null
    try {
        session = HibernateUtil.getSession();
        tx = session.beginTransaction();
        User user = (User) session.load(User.class,
                "4028802c46d255740146d25576320000");
        session.delete(user);
        session.getTransaction().commit();
    } catch (HibernateException e) {
        e.printStackTrace();
        session.getTransaction().rollback();
    } finally {
        if (session != null) {
            if (session.isOpen()) {
                session.close();
            }
        }
    }
}
```
## query接口初步

Query session.createQuery(String hql)方法;
- Hibernate的session.createQuery()方法是使用HQL(Hibernate的查询语句)语句查询对象的。
- hql：是查询对象的，例如："from User"，其中from不区分大小写，而User是区分大小写，因为它是对象。是User类
- 返回Query对象。
-执行这条语句后，Hibernate会根据配置文件中所配置的数据库适配器自动生成相应数据库的SQL语句。如：

```
Hibernate: select user0_.id as id0_, user0_.name as name0_, user0_.password as password0_,
user0_.createTime as createTime0_, user0_.expireTime as expireTime0_ from User user0_
```
代码示例

```
public Iterator getSomeDept(String name) throws HibernateException {
    String queryString = "select u from Dept as u where u.name like :name";
    beginTransaction();
    Query query = session.createQuery(queryString);
    query.setString("name", "%" + name + "%");
    Iterator it = query.iterate();
    return it;
}
```
