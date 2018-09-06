#Hibernate入门（2）- 不用配置用注解
@(编程)

在上一个例子里面，我用的配置文件的方式，这次改成注解。

##pom.xml
增加了hibernate-commons-annotations和hibernate-annotations
```
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
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
				<dependency>
			<groupId>org.hibernate</groupId>
			<artifactId>hibernate-commons-annotations</artifactId>
			<version>3.2.0.Final</version>
		</dependency>
		<dependency>
			<groupId>org.hibernate</groupId>
			<artifactId>hibernate-annotations</artifactId>
			<version>3.2.0.ga</version>
		</dependency>
	</dependencies>
</project>

```
##hibernate.cfg.xml配置文件
把```mapping resource```改成```mapping class```
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
		<mapping class="com.wisdombud.HibernateTest.Dept" />
	</session-factory>
</hibernate-configuration>                                                                                                                                                            
```


##对象的修改
这个是重点，需要在类和属性上面增加注解。可以在属性上增加注解或者在get方法上增加注解。
```
package com.wisdombud.HibernateTest;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "dept")
public class Dept {

	private String id;

	private String DeptNo;

	private String DName;

	@Column(name = "id")
	@Id
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "DeptNo")
	public String getDeptNo() {
		return DeptNo;
	}

	public void setDeptNo(String deptNo) {
		DeptNo = deptNo;
	}

	@Column(name = "DName")
	public String getDName() {
		return DName;
	}

	public void setDName(String dName) {
		DName = dName;
	}
}
```
##删除hbm配置文件
把Dept.hbm.xml删除就可以了。
