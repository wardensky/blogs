package com.zch.blogs.java.innerclass;

/**
 * @Description 测试通过内部类实现多重继承
 * @author zch <br>
 *         注意，MultiExtendDemo不能同时继承FatherKlass1和FatherKlass2
 */
public class MultiExtendDemo {
	class Klass1 extends FatherKlass1 {
	}

	class Klass2 extends FatherKlass2 {
	}

	void realEat() {
		Klass1 k1 = new Klass1();
		k1.eat();
		Klass2 k2 = new Klass2();
		k2.eat();
	}

	public static void main(String[] args) {
		MultiExtendDemo demo = new MultiExtendDemo();
		demo.realEat();
	}
}

class FatherKlass1 {
	public void eat() {
		System.out.println("1 eat something");
	}
}

class FatherKlass2 {
	public void eat() {
		System.out.println("2 eat something");
	}
}
