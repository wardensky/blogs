package com.zch.blogs.java.multithreads.synchronizeddemo;

/**
 * @Description 零长度的byte数组对象创建起来将比任何对象都经济――查看编译后的字节码：生成零长度的byte[]对象只需3条操作码，而Object
 *              lock = new Object()则需要7行操作码。
 * 
 * 
 * @author zch
 * @time 2018年9月6日 上午9:40:18
 * 
 */
public class SynchronizedDemo4 implements Runnable {
	private byte[] lock = new byte[0];

	public void method() {
		synchronized (lock) {

		}
	}

	public void run() {

	}

}
