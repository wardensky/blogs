package com.zch.spring_code_demo.demo2;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {
	public static void main(String[] args) {
		//test1();
		//test2();
		test3();
	}

	static void test1() {
		ApplicationContext act = new ClassPathXmlApplicationContext("demo2-1.xml");
		IPersonBll pbll1 = (IPersonBll) act.getBean("PersonBll");
		pbll1.show();
		((ClassPathXmlApplicationContext) act).close();
	}
	
	static void test2() {
		ApplicationContext act = new ClassPathXmlApplicationContext("demo2-2.xml");
		IPersonBll pbll1 = (IPersonBll) act.getBean("PersonBll");
		pbll1.show();
		((ClassPathXmlApplicationContext) act).close();
	}
	
	static void test3() {
		ApplicationContext act = new ClassPathXmlApplicationContext("demo2-3.xml");
		IPersonBll pbll1 = (IPersonBll) act.getBean("PersonBll");
		pbll1.show();
		((ClassPathXmlApplicationContext) act).close();
	}
}
