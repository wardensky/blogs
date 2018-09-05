package com.zch.spring_code_demo.demo4;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.zch.spring_code_demo.demo2.IPersonBll;

public class App {
	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("demo4.xml");
		IPersonBll bll = (PersonBll) context.getBean("PersonBll");
		bll.show();
		((ClassPathXmlApplicationContext) context).close();
	}
}
