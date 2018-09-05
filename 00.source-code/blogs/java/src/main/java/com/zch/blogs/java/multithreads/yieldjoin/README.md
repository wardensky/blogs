# 本包里面的例子讲join和yield

## 前言

其实join和yield应该分开讲，因为他俩关系不大。

join的意思是等一个线程结束；yield的意思是一个线程可以暂时放弃资源，让给别人。

理解起来，join比yield简单。

## join

join的英文意思是参加、连接。在多线程里面，指的是等待一个线程执行完。下面是一个小例子。

```
	public static void main(String[] args) throws InterruptedException {
		Thread1_1 t1 = new Thread1_1("aaa");
		Thread1_1 t2 = new Thread1_1("bbb");
		t1.start();
		t1.join();
		t2.start();	
	}
```

输出

```
I'm aaa. I'm doing something 1
I'm aaa. I'm doing something 2
I'm aaa. I'm doing something 3
I'm aaa. I'm doing something 4
I'm bbb. I'm doing something 1
I'm bbb. I'm doing something 2
I'm bbb. I'm doing something 3
I'm bbb. I'm doing something 4

```
可以看到，先执行t1，t1执行完了才执行t2。

## yield

yield比join要复杂一点点。其英文愿意是屈服、投降。在多线程里面，指的是放弃某种资源。主要用在多线程竞争里面。

看下面的代码：

```
public class YieldDemo0 {
	public static void main(String[] args) {
		Thread0_1 t1 = new Thread0_1("aaa");
		Thread0_1 t2 = new Thread0_1("bbb");
		t1.start();
		t2.start();
	}
}
class Thread0_1 extends Thread {
	String name;

	public Thread0_1(String name) {
		this.name = name;
	}

	@Override
	public void run() {
		for (int i = 1; i < 5; i++) {
			System.out.println("I'm " + this.name + ". I'm doing something " + i);
		//	Thread.yield();
		}

	}
}
```
如上的代码，输出的一般都不固定，因为两个线程是竞争关系，不知道谁先运行。
输出类似：

```
I'm bbb. I'm doing something 1
I'm aaa. I'm doing something 1
I'm bbb. I'm doing something 2
I'm aaa. I'm doing something 2
I'm bbb. I'm doing something 3
I'm aaa. I'm doing something 3
I'm bbb. I'm doing something 4
I'm aaa. I'm doing something 4

```
