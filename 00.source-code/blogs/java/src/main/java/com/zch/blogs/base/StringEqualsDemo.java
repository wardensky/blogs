package com.zch.blogs.base;

public class StringEqualsDemo {

	public static void main(String[] args) {
		String a = new String("abc");
		String b = "abc";
		String c = new String("This is a minimal Electron application based on the [Quick Start Guide](http://electron.atom.io/docs/tutorial/quick-start) within the Electron documentation.This is a minimal Electron application based on the [Quick Start Guide](http://electron.atom.io/docs/tutorial/quick-start) within the Electron documentation.");
		String d = "This is a minimal Electron application based on the [Quick Start Guide](http://electron.atom.io/docs/tutorial/quick-start) within the Electron documentation.This is a minimal Electron application based on the [Quick Start Guide](http://electron.atom.io/docs/tutorial/quick-start) within the Electron documentation.";
		System.out.println(a == b);
		System.out.println(a.equals(b));
		System.out.println(c == d);
		System.out.println(c.equals(d));
		Object o = new Object();
	}
}
