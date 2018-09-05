package com.zch.spring_code_demo.demo2;

import com.zch.spring_code_demo.demo1.Person;

public class PersonBll1 implements IPersonBll {
	private Person person;

	public PersonBll1(String name) {
		this.person = new Person();
		this.person.setName(name);
	}

	public void show() {
		if (this.person != null) {
			System.out.println(this.person.getName());
		} else {
			System.out.println("no person");
		}
	}
}
