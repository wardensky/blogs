package com.zch.spring_code_demo.demo9;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

public class MyBeanPostProcessor implements BeanPostProcessor {

	public MyBeanPostProcessor() {
		super();
		System.out.println("这是BeanPostProcessor实现类构造器！！");
	}

	public Object postProcessAfterInitialization(Object arg0, String arg1) throws BeansException {
		System.out.println("BeanPostProcessor接口方法postProcessAfterInitialization对属性进行更改！");
		return arg0;
	}

	public Object postProcessBeforeInitialization(Object arg0, String arg1) throws BeansException {
		System.out.println("BeanPostProcessor接口方法postProcessBeforeInitialization对属性进行更改！");
		return arg0;
	}

}
