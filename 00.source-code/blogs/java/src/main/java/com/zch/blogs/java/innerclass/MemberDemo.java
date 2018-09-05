package com.zch.blogs.java.innerclass;

public class MemberDemo {
	public static void main(String[] args) {
		// 错误写法
		// MemberDemo.MemberInner i = new MemberInner();

		// 正确写法
		MemberDemo outer = new MemberDemo();
		MemberInner inner = outer.new MemberInner();
		inner.setName("abcd");
		inner.foo();
		inner.bar();
	}

	private void privateMethod() {
		System.out.println("this is a private method and city = " + this.city);
	}

	public void publicMethod() {
		System.out.println("this is a public methodand city = " + this.city);
	}

	// 既然是内部类，最好加private修饰
	private class MemberInner {

		public void foo() {
			city = name;
			System.out.println("inner call outer method");
			privateMethod();
			publicMethod();

		}

		public void bar() {
			MemberDemo.this.city = name;
			System.out.println("another inner call outer method");
			MemberDemo.this.privateMethod();
			MemberDemo.this.publicMethod();

		}

		private String name;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;

		}
	}

	private String city;

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}
}
