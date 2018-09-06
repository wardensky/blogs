package com.zch.spring_code_demo.demo3;

import java.util.HashSet;
import java.util.Set;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.zch.spring_code_demo.demo1.IPersonBll;

public class App {
	public static void main(String[] args) {
		ApplicationContext act = new ClassPathXmlApplicationContext("demo3.xml");

		Set<IPersonBll> set = new HashSet<IPersonBll>();
		for (int i = 0; i < 10; i++) {
			IPersonBll pbll1 = (IPersonBll) act.getBean("PersonBll");
			set.add(pbll1);
		}
		///如果是singleton，打印的是1，如果是prototype，打印的是10
		System.out.println(set.size());
		((ClassPathXmlApplicationContext) act).close();
	}
}
