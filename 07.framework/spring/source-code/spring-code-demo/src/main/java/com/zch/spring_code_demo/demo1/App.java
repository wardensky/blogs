package com.zch.spring_code_demo.demo1;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {
	public static void main(String[] args) {
		IPersonBll pbll = new PersonBll();
		Person p = new Person();
		p.setName("chzhao");
		pbll.addPerson(p);
		ApplicationContext act = new ClassPathXmlApplicationContext("demo1.xml");
		IPersonBll pbll1 = (IPersonBll) act.getBean("PersonBll");
		pbll1.addPerson(p);
		((ClassPathXmlApplicationContext) act).close();
	}
}
