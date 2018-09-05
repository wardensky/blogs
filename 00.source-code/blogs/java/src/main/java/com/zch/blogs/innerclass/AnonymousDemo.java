package com.zch.blogs.innerclass;

public class AnonymousDemo {
	public void foo() {
		Person p = new Person() {
			public void eat() {
				System.out.println("eat some thing");
			}
		};
		p.eat();
	}

	public static void main(String[] args) {
		AnonymousDemo demo = new AnonymousDemo();
		demo.foo();
	}
}

interface Person {
	void eat();
}