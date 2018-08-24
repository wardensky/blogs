package com.zch.spring_code_demo.demo6;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Service2 implements IService2 {

	@Autowired
	private IService1 service1;

	public void sing() {
		System.out.println("I'm service2 and i sing a song");
		this.service1.say();
	}

}
