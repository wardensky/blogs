package com.zch.spring_code_demo.demo2;

import com.zch.spring_code_demo.demo1.Person;

public class PersonBll2 implements IPersonBll {
	private Person person;

	public PersonBll2(Person p) {
		this.person = p;
	}

	public void show() {
		if (this.person != null) {
			System.out.println(this.person.getName());
		} else {
			System.out.println("no person");
		}
	}
}
