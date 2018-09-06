package com.zch.spring_code_demo.demo9;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {
	public static void main(String[] args) {
		System.out.println("现在开始初始化容器");

		ApplicationContext ac = new ClassPathXmlApplicationContext("demo9.xml");
		System.out.println("容器初始化成功");
	 
		Person person = ac.getBean("person", Person.class);
		System.out.println(person);

		System.out.println("现在开始关闭容器！");
		((ClassPathXmlApplicationContext) ac).registerShutdownHook();
		((ClassPathXmlApplicationContext) ac).close();
	}
}
