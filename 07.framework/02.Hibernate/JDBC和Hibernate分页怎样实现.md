# JDBC和Hibernate分页怎样实现

## Hibernate 的分页

```
Query query = session.createQuery(“from Student”);
query.setFirstResult(firstResult);//设置每页开始的记录号
query.setMaxResults(resultNumber);//设置每页显示的记录数
Collection students = query.list();

```

## JDBC 的分页

根据不同的数据库采用不同的sql 分页语句
例如: Oracle 中的sql 语句为:
```
SELECT * FROM (SELECT a.*, rownum r FROM
TB_STUDENT) WHERE r between 2 and 10″
//查询从记录号2 到记录号10 之间的所有记录
```

## 参考

- [JDBC，Hibernate 分页怎样实现?](https://www.jobui.com/mianshiti/it/hibernate/4786/)
