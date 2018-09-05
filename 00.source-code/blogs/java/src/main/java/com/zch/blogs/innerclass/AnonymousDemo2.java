package com.zch.blogs.innerclass;

public class AnonymousDemo2 {
	public void foo(final String para) {
		Person2 p = new Person2() {
			public void eat( ) {
				System.out.println("eat " + para);
			}
		};
	 
		p.eat( );
	}

	public static void main(String[] args) {
		AnonymousDemo2 demo = new AnonymousDemo2();
		String abc = "abc";
		demo.foo(abc);
	}
}

interface Person2 {
	void eat();
}