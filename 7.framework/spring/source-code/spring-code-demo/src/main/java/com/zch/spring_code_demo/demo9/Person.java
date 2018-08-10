package com.zch.spring_code_demo.demo9;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class Person
		implements BeanFactoryAware, BeanNameAware, InitializingBean, DisposableBean, ApplicationContextAware {
	private String name;
	private String address;
	private int phone;

	private BeanFactory beanFactory;
	private String beanName;

	private ApplicationContext applicationContext;

	public Person() {
		System.out.println("【构造器】调用Person的构造器实例化");
	}

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
		System.out.println("【ApplicationContextAware接口】调用ApplicationContextAware.setApplicationContext()");
		System.out.println(this.applicationContext.getDisplayName());
	}

	public void destroy() throws Exception {
		System.out.println("【DiposibleBean接口】调用DiposibleBean.destory()");
	}

	public void afterPropertiesSet() throws Exception {
		System.out.println("【InitializingBean接口】调用InitializingBean.afterPropertiesSet()");

	}

	public void setBeanName(String arg0) {
		System.out.println("【BeanNameAware接口】调用BeanNameAware.setBeanName()");
		this.beanName = arg0;

	}

	public void setBeanFactory(BeanFactory arg0) throws BeansException {
		System.out.println("【BeanFactoryAware接口】调用BeanFactoryAware.setBeanFactory()");
		this.beanFactory = arg0;

	}

	// 通过<bean>的init-method属性指定的初始化方法
	public void myInit() {
		System.out.println("【init-method】调用<bean>的init-method属性指定的初始化方法");
	}

	// 通过<bean>的destroy-method属性指定的初始化方法
	public void myDestory() {
		System.out.println("【destroy-method】调用<bean>的destroy-method属性指定的初始化方法");
	}

	public String toString() {
		return "Person [address=" + address + ", name=" + name + ", phone=" + phone + "]" + this.beanFactory.toString();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		System.out.println("【注入属性】注入属性name");
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		System.out.println("【注入属性】注入属性address");
		this.address = address;
	}

	public int getPhone() {
		return phone;
	}

	public void setPhone(int phone) {
		System.out.println("【注入属性】注入属性phone");
		this.phone = phone;
	}

	public BeanFactory getBeanFactory() {
		return beanFactory;
	}

	public String getBeanName() {
		return beanName;
	}

}
