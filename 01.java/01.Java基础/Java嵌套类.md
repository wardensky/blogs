# Java嵌套类
 

## 前言
本文介绍几种Java嵌套类的形式。

Java嵌套类有4种实现形式，分别是：

- 成员内部类 member inner class
- 局部内部类 local inner class
- 匿名内部类 anonymous inner class
- 静态内部类 static inner class

其中前三种也叫内部类。

## 为什么要引入内部类

可以将一个类的定义放在另一个类的定义内部，这就是内部类。

- 在《Think in java》中有这样一句话：使用内部类最吸引人的原因是：每个内部类都能独立地继承一个（接口的）实现，所以无论外围类是否已经继承了某个（接口的）实现，对于内部类都没有影响。
- 应用内部类可以实现多重继承
- 内部类可以用多个实例，每个实例都有自己的状态信息，并且与其他外围对象的信息相互独立。
- 在单个外围类中，可以让多个内部类以不同的方式实现同一个接口，或者继承同一个类。
- 创建内部类对象的时刻并不依赖于外围类对象的创建。
- 内部类并没有令人迷惑的“is-a”关系，他就是一个独立的实体。
- 内部类提供了更好的封装，除了该外围类，其他类都不能访问。


## 几种内部类的说明

### 成员内部类

- 是最常见的内部类，其写法见下面代码片段
- 和成员同一级别的内部类，可以访问外部类的所有成员（成员变量、方法）。
- 注意其初始化的方法，MemberInner inner = outer.new MemberInner();
- 可以随意引用外部类的属性或者方法

```
package com.wardensky.demo.innerclass;

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

```

### 局部内部类

有这样一种内部类，它是嵌套在方法和作用于内的，对于这个类的使用主要是应用与解决比较复杂的问题，想创建一个类来辅助我们的解决方案，到那时又不希望这个类是公共可用的，所以就产生了局部内部类，局部内部类和成员内部类一样被编译，只是它的作用域发生了改变，它只能在该方法和属性中被使用，出了该方法和属性就会失效。

跟成员内部类不同之处只是作用域不同。

其示例如下代码片段：

```
package com.wardensky.demo.innerclass;

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

```

### 匿名内部类

- 使用匿名内部类时，我们必须是继承一个类或者实现一个接口，但是两者不可兼得，同时也只能继承一个类或者实现一个接口。
- 匿名内部类中是不能定义构造函数的。
- 匿名内部类中不能存在任何的静态成员变量和静态方法。
-  匿名内部类为局部内部类，所以局部内部类的所有限制同样对匿名内部类生效。
-  匿名内部类不能是抽象的，它必须要实现继承的类或者实现的接口的所有抽象方法。

下面是匿名内部类的例子

```
package com.wardensky.demo.innerclass;

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
```

在使用多线程时，也经常使用匿名内部类，有两种实现形式，一个是继承Thread，另外一种是实现Runnable接口。

```
package com.wardensky.demo.innerclass;

public class ThreadDemo {
	public static void main(String[] args) {
		Thread t = new Thread() {
			public void run() {
				System.out.print("thread  ");
			}
		};
		t.start();
	}
}

```


```
package com.wardensky.demo.innerclass;

public class RunnableDemo {
	public static void main(String[] args) {
		Runnable r = new Runnable() {
			public void run() {
				System.out.print("runnable  ");
			}
		};
		Thread t = new Thread(r);
		t.start();
	}
}

```

不过，如果要给匿名类传参数，需要使用final关键字修饰。


### 静态内部类



- 静态内部类不持有外部类的引用
	- 在普通内部类中，我们可以直接访问外部类的属性、方法，即使是private类型也可以访问，这是因为内部类持有一个外部类的引用，可以自由访问。而静态内部类，则只可以访问外部类的静态方法和静态属性（如果是private权限也能访问，这是由其代码位置所决定的），其他则不能访问。

- 静态内部类不依赖外部类
	- 普通内部类与外部类之间是相互依赖的关系，内部类实例不能脱离外部类实例，也就是说它们会同生同死，一起声明，一起被垃圾回收器回收。而静态内部类是可以独立存在的，即使外部类消亡了，静态内部类还是可以存在的。

- 普通内部类不能声明static的方法和变量
	- 普通内部类不能声明static的方法和变量，注意这里说的是变量，常量（也就是final static修饰的属性）还是可以的，而静态内部类形似外部类，没有任何限制。

- 如果内部类不会引用到外部类的东西的话，强烈建议使用静态内部类，因为这样更节省资源，减少内部类指向外部类的引用——Effective java

	- 最好把静态内部类看成一个普通的类，他只是恰好被声明在另一个类里面。


静态内部类的例子

```
package com.wardensky.demo.innerclass;

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

```

如果在类外面引用静态类，是如下的写法
```

public class StaticDemo2 {
	public static void main(String[] args) {
		// 如果在类外面引用静态类，是如下的写法
		StaticDemo3.InnerStatic sta = new StaticDemo3.InnerStatic();
		sta.setName("keke");
		System.out.println(sta.getName());
	}
}
```

## 实现多重继承的例子

```
package com.wardensky.demo.innerclass;

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

```
