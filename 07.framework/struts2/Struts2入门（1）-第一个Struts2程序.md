#Struts2入门（1）-第一个Struts2程序
@(编程)

[TOC]


##目录结构
```
C:\WorkSpace\java\StrutsTest\src\main
C:\WorkSpace\java\StrutsTest\src\test
C:\WorkSpace\java\StrutsTest\src\main\java
C:\WorkSpace\java\StrutsTest\src\main\resources
C:\WorkSpace\java\StrutsTest\src\main\webapp
C:\WorkSpace\java\StrutsTest\src\main\java\com
C:\WorkSpace\java\StrutsTest\src\main\java\com\chzhao
C:\WorkSpace\java\StrutsTest\src\main\java\com\chzhao\strutstest
C:\WorkSpace\java\StrutsTest\src\main\java\com\chzhao\strutstest\HelloAction.java
C:\WorkSpace\java\StrutsTest\src\main\resources\struts.xml
C:\WorkSpace\java\StrutsTest\src\main\webapp\hello.jsp
C:\WorkSpace\java\StrutsTest\src\main\webapp\WEB-INF
C:\WorkSpace\java\StrutsTest\src\main\webapp\WEB-INF\web.xml
C:\WorkSpace\java\StrutsTest\src\test\java
C:\WorkSpace\java\StrutsTest\src\test\resources
```

##pom.xml
```
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>
	<groupId>com.zhao</groupId>
	<artifactId>StrutsTest</artifactId>
	<version>0.0.1-SNAPSHOT</version>
	<name>StrutsTest</name>
	<description>StrutsTest</description>

	<dependencies>
		<dependency>
			<groupId>junit</groupId>
			<artifactId>junit</artifactId>
			<version>3.8.1</version>
			<scope>test</scope>
		</dependency>
		<dependency>
			<groupId>org.apache.struts</groupId>
			<artifactId>struts2-core</artifactId>
			<version>2.3.16.1</version>
		</dependency>
		<dependency>
			<groupId>log4j</groupId>
			<artifactId>log4j</artifactId>
			<version>1.2.14</version>
		</dependency>
	</dependencies>
</project>
```

##struts.xml
```
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE struts PUBLIC
    "-//Apache Software Foundation//DTD Struts Configuration 2.0//EN"
    "http://struts.apache.org/dtds/struts-2.0.dtd">

<struts>
	<package name="test" namespace="/" extends="struts-default">
		<action name="hello" class="com.chzhao.strutstest.HelloAction" method="hello">
			<result name="success">/hello.jsp</result>
		</action>
	</package>
</struts>
```

##HelloAction.java
```
package com.chzhao.strutstest;

import com.opensymphony.xwork2.ActionSupport;

public class HelloAction extends ActionSupport {

	private static final long serialVersionUID = 1L;
	private String str;
    public String hello() {
       this.str = "zhaokeke!!!";
       return "success";
    }
    public String getStr() {
       return str;
    }
    public void setStr(String str) {
       this.str = str;
    }
}
```

##hello.jsp
```
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN""http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Test</title>
</head>
<body>
	<h1>
		<s:property value="str" />
	</h1>
</body>
</html>
```

##运行程序
修改Run Configurations，把context改为/，然后run as jetty
打开浏览器，输入http://localhost:8080/hello.action即可。
