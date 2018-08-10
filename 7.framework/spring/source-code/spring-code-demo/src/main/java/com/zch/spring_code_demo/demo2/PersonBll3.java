package com.zch.spring_code_demo.demo2;

import com.zch.spring_code_demo.demo1.Person;

public class PersonBll3 implements IPersonBll {
	private Person person;

	public PersonBll3(String name) {
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
