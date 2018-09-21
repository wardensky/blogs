# AtomicInteger用法及源码学习


## 用法

### 不安全的多线程

```
package com.wardensky.multithread.atomic;

public class NonSafeDemo {
	static int i = 0;

	public static void main(String[] args) {
		NonSafeDemo.run();
	}

	static void run() {
		Thread t1 = new Thread() {
			public void run() {
				for (int j = 0; j < 10000; j++) {
					i++;
				}
			}
		};
		t1.start();

		Thread t2 = new Thread() {
			public void run() {
				for (int j = 0; j < 10000; j++) {
					i++;
				}
			}
		};
		t2.start();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(i);
	}
}

```

### 安全的synchronized的写法

```
package com.wardensky.multithread.atomic;

public class SynchronizedDemo {
	public static void main(String[] args) {
		SynchronizedDemo.run();
	}

	static volatile int i = 0;

	static void run() {
		Thread t1 = new Thread() {
			public void run() {
				synchronized (this) {
					for (int j = 0; j < 10000; j++) {
						i++;
					}
				}
			}
		};
		t1.start();
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Thread t2 = new Thread() {
			public void run() {
				synchronized (this) {
					for (int j = 0; j < 10000; j++) {
						i++;
					}
				}
			}
		};
		t2.start();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(i);
	}
}

```

### 安全的lock的写法

```
package com.wardensky.multithread.atomic;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockDemo {

	public static void main(String[] args) {
		LockDemo.run();
	}

	static int i = 0;
	static Lock lockObject = new ReentrantLock();

	static void run() {
		Thread t1 = new Thread() {
			public void run() {
				for (int j = 0; j < 10000; j++) {
					lockObject.lock();
					i++;
					lockObject.unlock();
				}
			}
		};
		t1.start();

		Thread t2 = new Thread() {
			public void run() {
				for (int j = 0; j < 10000; j++) {
					lockObject.lock();
					i++;
					lockObject.unlock();
				}
			}
		};
		t2.start();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(i);
	}
}
```

### 采用AtomicInteger的写法
```

```

## 参考
