package com.zch.spring_code_demo.demo1;

public class PersonBll implements IPersonBll {
	public void addPerson(Person p) {
		System.out.println("add person: " + p.getName());
	}
}
