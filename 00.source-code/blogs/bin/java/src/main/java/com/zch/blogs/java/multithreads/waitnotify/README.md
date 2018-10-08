# 这个包说明线程中wait/notify/notifyAll的用法

## 前言
多线程时，最关注的就是线程同步，线程间的同步一般用锁来实现，常见的锁就是synchronized和lock。用了synchronized，就不得不提到wait/notify/notifyAll。本文介绍这三者是什么东西。

## 举例说明

首先明确一点，所有的锁都是加在对象上面的。也就是说，只要是加了同步synchronized的代码，每个线程在运行到这的时候，都要去查一下这个对象上的锁有没有被人占用，如果被人占用了，就要等待。等待也分两种，一种是一直等着，一种是先做别的，别人提醒之后再找。

举个生活中的例子做对比。假设有一个屋子，屋子有一个门，门上面有一个锁。屋子同一个时间只能有一个人，这个人在里面干什么没人管。现在有多个人想进到这个屋子里面，每个进去的人都可以锁门。synchronized(object)里面的object就是门上这把锁，synchronized代码的意思是在执行代码之前，看看这把锁是不是锁着的，如果不是锁着的，就进去工作，同时把门锁上。执行完{}代码之后，就相当于从屋子里面出来，同时打开了锁。wait的意思就某个人甲进入了屋子，工作了一会，想临时先出去。所以他把锁打开，出去了，并把屋子让给了别人。那他什么时候回来呢？必须有别人告诉他，也就是notify。notify和notifyAll的区别在于notify随机告诉某一个人(注意这个人是wait的)，我不用锁了，你用吧；notifyAll是告诉所有等待这个锁的人。


我们下面用这个例子加代码说明这些用法。


### 情况1：不锁门，不加锁
如果不锁门，就会有多个人进入到这个屋子，这个屋子就是被撑爆。也就是说，如果代码里面不加synchronize，会有多个进程访问同一个资源，造成不可知的错误。这种情况就不举代码的例子了。

### 情况2：锁门，不加wait和notify

这种情况下，代码如WaitNotifyDemo3所示：

```
package com.wardensky.multithread.waitnotify;

/**
 * 这个例子就是加锁了，但没有加wait和notify
 * 
 * @author zch
 *
 */
public class WaitNotifyDemo3 {

	/**
	 * 这个对象就是所有线程都用的锁。
	 */
	public static Object obj = new Object();

	public static void main(String[] args) throws InterruptedException {
		Thread0_1 t1 = new Thread0_1("Thread WaterMelon");
		Thread0_2 t2 = new Thread0_2("Thread Apple");
		t2.start();
		Thread.sleep(1);
		t1.start();
		System.out.println("完成");
	}
}

/**
 * 这个线程用来演示阻塞，等这个线程工作完，不会告诉别人。<br/>
 * 
 * @author zch <br/>
 * 
 */
class Thread3_1 extends Thread {
	String name;

	public Thread3_1(String name) {
		this.name = name;
	}

	@Override
	public void run() {
		try {
			synchronized (WaitNotifyDemo3.obj) {
				for (int i = 1; i < 5; i++) {
					Thread.sleep(1000);
					System.out.println("I'm " + this.name + ". I'm doing something " + i);
				}
			}
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}
}

/**
 * 这个类用来表示被阻塞的线程，但这个线程并没有wait，所以他一直轮询这个锁，比较占资源。<br/>
 * 
 * @author zch
 *
 */
class Thread3_2 extends Thread {
	String name;

	public Thread3_2(String name) {
		this.name = name;
	}

	@Override
	public void run() {

		System.out.println("I'm " + this.name + " . I'm waiting");
		synchronized (WaitNotifyDemo3.obj) {

			System.out.println(this.name + " Finish wait");
		}

	}
}
```

输出如下：

```
I'm Thread Apple . I'm waiting
完成
I'm Thread WaterMelon. I'm doing something 1
I'm Thread WaterMelon. I'm doing something 2
I'm Thread WaterMelon. I'm doing something 3
I'm Thread WaterMelon. I'm doing something 4
I'm Thread WaterMelon.  The object notify
Thread Apple Finish wait

```
在上面这个例子里面，Apple线程一直在轮询这个锁的状态，等到线程WaterMelon用完之后，马上抢占过来。这种情况下，比较耗资源。

### 情况3:  加wait和notify

我们在情况2的基础上，让Apple线程增加一个wait，让线程WaterMelon增加一个notify，代码如下：

```
package com.wardensky.multithread.waitnotify;

public class WaitNotifyDemo0 {

	/**
	 * 这个对象就是所有线程都用的锁。
	 */
	public static Object obj = new Object();

	public static void main(String[] args) throws InterruptedException {
		Thread0_1 t1 = new Thread0_1("Thread WaterMelon");
		Thread0_2 t2 = new Thread0_2("Thread Apple");
		t2.start();
		///这个sleep很重要。
		Thread.sleep(1);
		t1.start();
		System.out.println("完成");
	}
}

/**
 * 这个线程用来演示阻塞，等这个线程工作完，会通过notify方法告诉别人。<br/>
 * 
 * @author zch <br/>
 * 
 */
class Thread0_1 extends Thread {
	String name;

	public Thread0_1(String name) {
		this.name = name;
	}

	@Override
	public void run() {
		try {
			synchronized (WaitNotifyDemo0.obj) {
				for (int i = 1; i < 5; i++) {
					Thread.sleep(1000);
					System.out.println("I'm " + this.name + ". I'm doing something " + i);
				}
				WaitNotifyDemo0.obj.notify();
				System.out.println("I'm " + this.name + ".  The object notify");
			}

		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}
}

/**
 * 这个类用来表示被阻塞的线程，进入工作状态后，他可以先把锁还给别人，等别人通知他，他才继续工作。<br/>
 * 
 * @author zch
 *
 */
class Thread0_2 extends Thread {
	String name;

	public Thread0_2(String name) {
		this.name = name;
	}

	@Override
	public void run() {
		try {
			System.out.println("I'm " + this.name + " . I'm waiting");
			synchronized (WaitNotifyDemo0.obj) {
				WaitNotifyDemo0.obj.wait();
				System.out.println(this.name + " Finish wait");
			}
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}
}
```
输出：

```
I'm Thread Apple . I'm waiting
完成
I'm Thread WaterMelon. I'm doing something 1
I'm Thread WaterMelon. I'm doing something 2
I'm Thread WaterMelon. I'm doing something 3
I'm Thread WaterMelon. I'm doing something 4
I'm Thread WaterMelon.  The object notify
Thread Apple Finish wait

```
在这个例子里面，Thread0_2这个线程在```synchronized (WaitNotifyDemo0.obj)```之后调用了```WaitNotifyDemo0.obj.wait();```，意思是	我进入了屋子之后，先离开了，让别人处理，但别人处理之后要记得notify我，不然我就不知道了。

需要注意的是，在这个例子里面，两个线程的启动之间有一个sleep，这个sleep很重要，下面会讲到。

### 情况4：有多个人wait

如果有多个人wait，则唤醒线程尽量用notifyAll。notify是告诉某一个线程，但具体是哪个线程是随机的。notifyAll就是大家再来竞争。

如果用了没有用notifyAll，也可以wait执行之后调用notify，也就是一个执行完再唤醒另外一个。代码示例如下：

```
package com.wardensky.multithread.waitnotify;

public class WaitNotifyDemo1 {
	public static Object obj = new Object();

	public static void main(String[] args) {
		Thread1_1 t1 = new Thread1_1();
		Thread1_2 t2 = new Thread1_2("Thread Apple");
		Thread1_2 t3 = new Thread1_2("Thread Orange");

		t2.start();
		t3.start();
		try {
			// 必须sleep一下
			Thread.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		t1.start();
		System.out.println("完成");
	}
}

class Thread1_1 extends Thread {
	@Override
	public void run() {
		try {
			synchronized (WaitNotifyDemo1.obj) {
				for (int i = 1; i < 5; i++) {
					Thread.sleep(1000);
					System.out.println("I'm thread111. I'm doing something " + i);
				}
				// WaitNotifyDemo.obj.notify();
				WaitNotifyDemo1.obj.notifyAll();
				System.out.println("I'm thread111. The object notify");
			}

		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}
}

class Thread1_2 extends Thread {

	String name;

	public Thread1_2(String name) {
		this.name = name;
	}

	@Override
	public void run() {
		try {
			System.out.println("I'm " + this.name + " . I'm waiting");
			synchronized (WaitNotifyDemo1.obj) {
				WaitNotifyDemo1.obj.wait();
				System.out.println(this.name + " Finish wait");
				// 如果不加这句话，就叫不醒别人了。
				// WaitNotifyDemo.obj.notify();
			}

		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}
}
```

输出：

```
I'm Thread Apple . I'm waiting
I'm Thread Orange . I'm waiting
完成
I'm thread111. I'm doing something 1
I'm thread111. I'm doing something 2
I'm thread111. I'm doing something 3
I'm thread111. I'm doing something 4
I'm thread111. The object notify
Thread Orange Finish wait
Thread Apple Finish wait


```

### 情况5：情况2里面的不加sleep

如果情况2里面的例子不加sleep，会出现程序无法执行完的情况。原因是两个线程几乎同时运行，但线程2在同步代码之前有一个输出```System.out.println("I'm " + this.name + " . I'm waiting");```。这个输出会导致他比线程1晚进入同步代码。线程1notify的时候，线程2还没有等待。等到线程2真正等待的时候，已经没有人来唤醒他了。

## 再看看源码

前面提到过，wait/notify/notifyAll都是在对象上的。实际上，Object对象上都有这些方法。代码如下：

```
    public final native void wait(long timeout) throws InterruptedException;
 
 
    public final void wait(long timeout, int nanos) throws InterruptedException {
        if (timeout < 0) {
            throw new IllegalArgumentException("timeout value is negative");
        }

        if (nanos < 0 || nanos > 999999) {
            throw new IllegalArgumentException(
                                "nanosecond timeout value out of range");
        }

        if (nanos > 0) {
            timeout++;
        }

        wait(timeout);
    }
    
    public final void wait() throws InterruptedException {
        wait(0);
    }
    
    public final native void notify();
    
    public final native void notifyAll();            
    
```
从源码中可以看出```notify();```和```notifyAll();```都是native的方法。

wait还有几个重载，可以指定等多久。比如下面的代码：

```
	@Override
	public void run() {
		System.out.println("I'm " + this.name + " . I'm waiting");
		try {
			synchronized (WaitNotifyDemo5.obj) {		
				WaitNotifyDemo5.obj.wait(1000 * 1);
				System.out.println(this.name + " Finish wait");
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
```
这个代码的意思是我等1秒钟，1秒之后就进入抢占式了。当前，1秒之前也可以用notify或者notifyAll叫醒我。

## 跟Thread.sleep的关系

线程还有一个sleep静态方法,有人会混淆sleep和wait的关系。

还用上面的例子，sleep的意思是这个人还在屋子里面睡觉，门还锁着呢，谁也进不来。wait的意思是我把门打开，出去睡觉，等着别人把我叫醒我再回来。

也就是说，wait会释放锁，sleep不会。wait是锁这个对象的方法，sleep是线程的静态方法。

## synchronized

synchronized(obj)一个代码段，意思是下面```{} ```里面的代码执行的时候，要去检查obj上面的锁有没有被别人占用。

同时还有这样一种写法，这是StringBuffer里面的源码:

```
    @Override
    public synchronized StringBuffer append(Object obj) {
        toStringCache = null;
        super.append(String.valueOf(obj));
        return this;
    }

```
这段代码相当于

```
    @Override
    public  StringBuffer append(Object obj) {
	    synchronized(this) {
	        toStringCache = null;
	        super.append(String.valueOf(obj));
	        return this;
	    }
    }

```
也就是说加锁的对象是this。

如果是静态方法，那么加锁的对象是obj.class，该类的类对象。

## 总结

- synchronized是要检查锁的代码，如果有锁不往下执行。
- wait只能在同步（synchronize）环境中被调用
- 进入wait状态的线程能够被notify和notifyAll线程唤醒
- wait通常有条件地执行，线程会一直处于wait状态，直到某个条件变为真
- wait方法在进入wait状态的时候会释放对象的锁
- wait方法是针对一个被同步代码块加锁的对象
- sleep方法和多线程没有直接关系，跟锁也没有关系
- wait释放锁，notify和notifyAll不释放锁

