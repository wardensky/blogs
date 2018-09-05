package com.zch.blogs.java.innerclass;

public class DemoMain {
	public static void main(String[] args) {
		StaticDemo.InnerStatic a = new StaticDemo.InnerStatic();
		a.setName("abc");
		System.out.println(a.getName());
	}
}
