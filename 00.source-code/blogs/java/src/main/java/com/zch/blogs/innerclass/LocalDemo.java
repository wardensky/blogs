package com.zch.blogs.innerclass;

public class LocalDemo {

	public void foo() {
		class LocalInner {
			private String name;

			public String getName() {
				return name;
			}

			public void setName(String name) {
				this.name = name;
			}
		}

		LocalInner inner = new LocalInner();
		inner.setName("aaa");
		System.out.println(inner.getName());
	}

	public static void main(String[] args) {
		LocalDemo inst = new LocalDemo();
		inst.foo();
	}

	private String city;

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}
}
