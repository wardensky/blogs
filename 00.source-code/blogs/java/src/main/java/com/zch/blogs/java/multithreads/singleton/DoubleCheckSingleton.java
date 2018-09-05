package com.zch.blogs.java.multithreads.singleton;

/**
 * @Description 基于双重检查锁的单例
 * @author zch
 *
 */
public class DoubleCheckSingleton {
	private static volatile DoubleCheckSingleton inst;

	private DoubleCheckSingleton() {
	}

	public static DoubleCheckSingleton getInst() {
		if (null == inst) {
			synchronized (DoubleCheckSingleton.class) {
				if (null == inst) {
					inst = new DoubleCheckSingleton();
				}
			}
		}
		return inst;
	}

	public static DoubleCheckSingleton getInst1() {
		synchronized (DoubleCheckSingleton.class) {
			if (null == inst) {
				inst = new DoubleCheckSingleton();
			}
		}
		return inst;
	}

	public synchronized static DoubleCheckSingleton getInst2() {
		if (null == inst) {
			inst = new DoubleCheckSingleton();
		}
		return inst;
	}
}
