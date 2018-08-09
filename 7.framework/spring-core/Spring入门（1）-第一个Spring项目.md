### Spring入门（1）-第一个Spring项目

#### 1. 创建maven项目，maven相关配置如下：
```
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>

	<groupId>com.chzhao</groupId>
	<artifactId>springtest</artifactId>
	<version>0.0.1-SNAPSHOT</version>
	<packaging>jar</packaging>

	<name>springtest</name>
	<url>http://maven.apache.org</url>

	<properties>
		<project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
	</properties>

	<dependencies>
		<dependency>
			<groupId>junit</groupId>
			<artifactId>junit</artifactId>
			<version>3.8.1</version>
			<scope>test</scope>
		</dependency>
		<dependency>
			<groupId>org.springframework</groupId>
			<artifactId>spring-context</artifactId>
			<version>4.0.1.RELEASE</version>
		</dependency>
		<dependency>
			<groupId>org.springframework</groupId>
			<artifactId>spring-core</artifactId>
			<version>4.0.1.RELEASE</version>
		</dependency>
	</dependencies>
</project>

```

#### 2. Java的基本代码

```
package com.chzhao.springtest;

public class Person {
	private String name;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}

```


```
package com.chzhao.springtest;

public interface IPersonBll {
	void addPerson(Person p);
}

```

```
package com.chzhao.springtest;

public class PersonBll implements IPersonBll {

	public void addPerson(Person p) {
		System.out.println("add person: " + p.getName());
	}
}

```

#### 3. 添加Spring配置文件applicationContext.xml

内容如下，配置了一个bean
```
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">
	<bean  name="PersonBll" class="com.chzhao.springtest.PersonBll"/>
</beans>
```

#### 4. 主函数

```
package com.chzhao.springtest;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {
	public static void main(String[] args) {
		IPersonBll pbll = new PersonBll();
		Person p = new Person();
		p.setName("chzhao");
		pbll.addPerson(p);
		ApplicationContext act = new ClassPathXmlApplicationContext(
				"applicationContext.xml");
		IPersonBll pbll1 = (IPersonBll) act.getBean("PersonBll");
		pbll1.addPerson(p);
	}
}

```
可以看到，我通过两种方法实例化了PersonBll：一种是常见的方法；一种是通过spring。

#### 5. 输出

```
add person: chzhao
十二月 27, 2014 10:56:24 下午 org.springframework.context.support.AbstractApplicationContext prepareRefresh
信息: Refreshing org.springframework.context.support.ClassPathXmlApplicationContext@1cbfa42: startup date [Sat Dec 27 22:56:24 CST 2014]; root of context hierarchy
十二月 27, 2014 10:56:24 下午 org.springframework.beans.factory.xml.XmlBeanDefinitionReader loadBeanDefinitions
信息: Loading XML bean definitions from class path resource [applicationContext.xml]
add person: chzhao

```

两张方法的输出是一样的。

#### 6. 什么是IOC

![](http://docs.spring.io/spring/docs/current/spring-framework-reference/html/images/container-magic.png)
上图是Spring官方文档中队IOC的介绍，Spring有一个container，你把pojo和配置拿过来，直接输出对象。减少了对象实例化的过程，降低了系统的耦合度。


#### 7. 参考
[The IoC container](http://docs.spring.io/spring/docs/current/spring-framework-reference/html/beans.html)
