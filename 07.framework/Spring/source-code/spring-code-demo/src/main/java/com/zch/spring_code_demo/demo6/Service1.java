package com.zch.spring_code_demo.demo6;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Service1 implements IService1 {
	@Autowired
	private BeanA a;

	public void say() {
		System.out.println("I'm service1 and I'm saying sth");
		a.jump();
	}
}
