# Hibernate入门（1）-第一个Hibernate程序
Hibernate是最著名的ORM工具之一，本系列文章主要学习Hibernate的用法，不涉及Hibernate的原理。本文介绍第一个Hibernate例子，注意，这是一个独立的Hibernate，跟Spring和Struts没有任何关系。

## 目录结构
```
main
|--java
|----com
|------chzhao
|--------hibernatetest
|----------Dept.hbm.xml
|----------Dept.java
|----------DeptDao.java
|----------HibernateBase.java
|--resources
|----hibernate.cfg.xml
test
|----com
|------chzhao
|--------hibernatetest
|----------AppTest.java
|----------DeptDaoTest.java
```

## pom.xml
```
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>

	<groupId>com.wisdombud</groupId>
	<artifactId>HibernateTest</artifactId>
	<version>0.0.1-SNAPSHOT</version>
	<packaging>jar</packaging>

	<name>HibernateTest</name>
	<url>http://maven.apache.org</url>

	<properties>
		<project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
		<hibernate.version>4.2.0.Final</hibernate.version>
		<hibernate.search>4.2.0.Final</hibernate.search>
		<mysql-connector-java.version>5.1.29</mysql-connector-java.version>
	</properties>

	<dependencies>
		<dependency>
			<groupId>junit</groupId>
			<artifactId>junit</artifactId>
			<version>3.8.1</version>
			<scope>test</scope>
		</dependency>
		<dependency>
			<groupId>mysql</groupId>
			<artifactId>mysql-connector-java</artifactId>
			<version>${mysql-connector-java.version}</version>
		</dependency>
		<dependency>
			<groupId>org.hibernate</groupId>
			<artifactId>hibernate-core</artifactId>
			<version>${hibernate.version}</version>
		</dependency>
		<dependency>
			<groupId>org.hibernate</groupId>
			<artifactId>hibernate-c3p0</artifactId>
			<version>${hibernate.version}</version>
		</dependency>
		<dependency>
			<groupId>org.hibernate</groupId>
			<artifactId>hibernate-ehcache</artifactId>
			<version>${hibernate.version}</version>
		</dependency>
		<dependency>
			<groupId>org.hibernate</groupId>
			<artifactId>hibernate-search</artifactId>
			<version>${hibernate.search}</version>
		</dependency>
		<dependency>
			<groupId>org.hibernate</groupId>
			<artifactId>hibernate-entitymanager</artifactId>
			<version>${hibernate.version}</version>
		</dependency>
		<dependency>
			<groupId>org.hibernate</groupId>
			<artifactId>hibernate-validator</artifactId>
			<version>${hibernate.version}</version>
		</dependency>
	</dependencies>
</project>
```

## 实体类
```
package com.chzhao.hibernatetest;

public class Dept {
	private String id;
	private String DeptNo;
	private String DName;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDeptNo() {
		return DeptNo;
	}

	public void setDeptNo(String deptNo) {
		DeptNo = deptNo;
	}

	public String getDName() {
		return DName;
	}

	public void setDName(String dName) {
		DName = dName;
	}
}

```
## 映射文件
```
<!DOCTYPE hibernate-mapping PUBLIC  
	"-//Hibernate/Hibernate Mapping DTD 3.0//EN"  
	"http://hibernate.sourceforge.net/hibernate-mapping-3.0.dtd">

<hibernate-mapping package="com.chzhao.hibernatetest">
	<class name="Dept" table="dept">
		<id name="id" column="id" type="string"></id>
		<property name="DeptNo" />
		<property name="DName" />
	</class>
</hibernate-mapping>  
```
## Hibernate操作基础类
```
package com.chzhao.hibernatetest;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public abstract class HibernateBase {
	protected SessionFactory sessionFactory;// 会话工厂，用于创建会话
	protected Session session;// hibernate会话
	protected Transaction transaction; // hiberante事务

	public HibernateBase() throws HibernateException {
		this.initHibernate();
	}

	protected void initHibernate() throws HibernateException {
		sessionFactory = new Configuration().configure().buildSessionFactory();
	}

	protected void beginTransaction() throws HibernateException {
		session = sessionFactory.openSession();
		transaction = session.beginTransaction();
	}

	protected void endTransaction(boolean commit) throws HibernateException {

		if (commit) {
			transaction.commit();
		} else {
			transaction.rollback();
		}
		session.close();
	}
}
```
## 实体操作类
```
package com.chzhao.hibernatetest;

import java.util.Iterator;

import org.hibernate.HibernateException;
import org.hibernate.Query;

public class DeptDao extends HibernateBase {
	public DeptDao() throws HibernateException {
		super();
	}

	public void addDept(Dept Dept) throws HibernateException {
		beginTransaction();
		session.save(Dept);
		endTransaction(true);
	}

	public Iterator getAllDepts() throws HibernateException {
		String queryString = "select Depts from Dept as Dept";
		beginTransaction();
		Query query = session.createQuery(queryString);
		Iterator it = query.iterate();
		return it;
	}

	public void deleteDept(String id) throws HibernateException {
		beginTransaction();
		Dept Dept = (Dept) session.load(Dept.class, id);
		session.delete(Dept);
		endTransaction(true);
	}

	public Iterator getSomeDept(String name) throws HibernateException {
		String queryString = "select u from Dept as u where u.name like :name";
		beginTransaction();
		Query query = session.createQuery(queryString);
		query.setString("name", "%" + name + "%");
		Iterator it = query.iterate();
		return it;
	}
}

```
## hibernate配置文件
```
<?xml version='1.0' encoding='UTF-8'?>
<!DOCTYPE hibernate-configuration PUBLIC
          "-//Hibernate/Hibernate Configuration DTD 3.0//EN"
          "http://hibernate.sourceforge.net/hibernate-configuration-3.0.dtd">

<hibernate-configuration>
	<session-factory>
		<property name="dialect">org.hibernate.dialect.MySQLDialect</property>
		<property name="connection.url">
			jdbc:mysql://localhost:3306/tz?useUnicode=true&amp;characterEncoding=utf8
		</property>
		<property name="connection.username">tz</property>
		<property name="connection.password">tz</property>
		<property name="connection.driver_class">com.mysql.jdbc.Driver</property>
		<property name="show_sql">true</property>
		<mapping resource="com/chzhao/hibernatetest/Dept.hbm.xml" />
	</session-factory>
</hibernate-configuration>
```
## 测试类

```
package com.chzhao.hibernatetest;

import java.util.UUID;

import junit.framework.TestCase;

public class DeptDaoTest extends TestCase {
	public void testAddDept() {
		DeptDao dao = new DeptDao();
		Dept dept = new Dept();
		dept.setDeptNo("3");
		dept.setDName("c");
		dept.setId(UUID.randomUUID().toString());
		dao.addDept(dept);
	}
}

```
