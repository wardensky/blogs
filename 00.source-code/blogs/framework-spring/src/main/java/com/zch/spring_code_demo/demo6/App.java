package com.zch.spring_code_demo.demo6;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {
	public static void main(String[] args) {
		ApplicationContext ac = new ClassPathXmlApplicationContext("demo6.xml");
		///这个地方如果写成Service2会出问题，“No bean named 'Service2' is defined”
		IService2 s2 = (IService2) ac.getBean("Service2");

		s2.sing();

		((ClassPathXmlApplicationContext) ac).close();
	}

}
