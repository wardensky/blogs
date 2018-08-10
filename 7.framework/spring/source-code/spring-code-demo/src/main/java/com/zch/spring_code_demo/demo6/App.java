package com.zch.spring_code_demo.demo6;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {
	public static void main(String[] args) {
		ApplicationContext ac = new ClassPathXmlApplicationContext("demo6.xml");
		IService2 s2 = (IService2)ac.getBean("service2");
		 
		s2.sing();

		((ClassPathXmlApplicationContext) ac).close();
	}

}
