# JVM内存溢出


## 堆溢出

Java堆唯一的作用就是存储对象实例，只要保证不断创建对象并且对象不被回收，那么对象数量达到最大堆容量限制后就会产生内存溢出异常了。所以测试的时候把堆的大小固定住并且让堆不可扩展即可。测试代码如下


```
package com.zch.blogs.java.jvm;

import java.util.ArrayList;
import java.util.List;

public class HeapOverflowTest {
	public static void main(String[] args) {
		List<HeapOverflowTest> list = new ArrayList<HeapOverflowTest>();
		while (true) {
			list.add(new HeapOverflowTest());
		}
	}
}

```

输出

```

Exception in thread "main" java.lang.OutOfMemoryError: Java heap space
	at java.util.Arrays.copyOf(Arrays.java:3210)
	at java.util.Arrays.copyOf(Arrays.java:3181)
	at java.util.ArrayList.grow(ArrayList.java:261)
	at java.util.ArrayList.ensureExplicitCapacity(ArrayList.java:235)
	at java.util.ArrayList.ensureCapacityInternal(ArrayList.java:227)
	at java.util.ArrayList.add(ArrayList.java:458)
	at com.zch.blogs.java.jvm.HeapOverflowTest.main(HeapOverflowTest.java:10)

```

## 栈溢出

Java虚拟机规范中描述了如果线程请求的栈深度太深（换句话说方法调用的深度太深），就会产生栈溢出了。那么，我们只要写一个无限调用自己的方法，自然就会出现方法调用的深度太深的场景了。测试代码如下

```

package com.zch.blogs.java.jvm;

public class StackOverflowTest {
	private int stackLength = 1;

	public void stackLeak() {
		stackLength++;
		stackLeak();
	}

	public static void main(String[] args) throws Throwable {
		StackOverflowTest stackOverflow = new StackOverflowTest();
		try {
			stackOverflow.stackLeak();
		} catch (Throwable e) {
			System.out.println("stack length:" + stackOverflow.stackLength);
			throw e;
		}
	}
}

```


```
stack length:18622Exception in thread "main"
java.lang.StackOverflowError
	at com.zch.blogs.java.jvm.StackOverflowTest.stackLeak(StackOverflowTest.java:8)
	at com.zch.blogs.java.jvm.StackOverflowTest.stackLeak(StackOverflowTest.java:8)
	at com.zch.blogs.java.jvm.StackOverflowTest.stackLeak(StackOverflowTest.java:8)
	at com.zch.blogs.java.jvm.StackOverflowTest.stackLeak(StackOverflowTest.java:8)
	at com.zch.blogs.java.jvm.StackOverflowTest.stackLeak(StackOverflowTest.java:8)
	at com.zch.blogs.java.jvm.StackOverflowTest.stackLeak(StackOverflowTest.java:8)

```

## 方法区和运行时常量池溢出
