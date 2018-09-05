package com.zch.blogs.innerclass;

public class StaticDemo {

	static class InnerStatic {
		private String name;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
			// 不同于内部类，下面这种写法是错误的，因为静态内部类没有外部类的引用，所以看不到
			// StaticDemo.this.city = "";
		}
	}

	public static void main(String[] args) {
		// 这里不同于内部类，可以直接new
		InnerStatic sta = new InnerStatic();
		sta.setName("keke");
		System.out.println(sta.getName());
	}

	private String city;

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}
}
