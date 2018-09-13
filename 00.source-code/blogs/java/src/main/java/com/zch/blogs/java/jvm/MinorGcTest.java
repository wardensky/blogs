package com.zch.blogs.java.jvm;

public class MinorGcTest {
	private static final int _1MB = 1024 * 1024;

	// VM 参数：-verbose:gc-Xms20M-Xmx20M-Xmn10M-XX:+PrintGCDetails -XX: SurvivorRatio=
	// 8
	@SuppressWarnings("unused")
	public static void testAllocation() {
		byte[] allocation1, allocation2, allocation3, allocation4;

		allocation1 = new byte[2 * _1MB];
		allocation2 = new byte[2 * _1MB];
		allocation3 = new byte[2 * _1MB];
		allocation4 = new byte[4 * _1MB];
	}

	/**
	 * 大对象直接进入老年代
	 */
	@SuppressWarnings("unused")
	public static void testPretenureSizeThreshold() {
		byte[] allocation;
		allocation = new byte[4 * _1MB];
	}

	/**
	 * 长期存活的对象将进入老年代
	 */
	@SuppressWarnings("unused")
	public static void testTenuringThreshold() {
		byte[] allocation1, allocation2, allocation3;
		allocation1 = new byte[_1MB / 4];
		allocation2 = new byte[_1MB * 4];
		allocation3 = new byte[_1MB * 4];
		allocation3 = null;
		allocation3 = new byte[_1MB * 4];
	}

	@SuppressWarnings("unused")
	public static void testTenuringThreshold2() {
		byte[] allocation1, allocation2, allocation3, allocation4;
		allocation1 = new byte[_1MB / 4];
		// allocation1+ allocation2 大于 survivo 空间 一半 allocation2= new byte[ _1MB/ 4];
		allocation3 = new byte[4 * _1MB];
		allocation4 = new byte[4 * _1MB];
		allocation4 = null;
		allocation4 = new byte[4 * _1MB];
	}

	public static void main(String[] args) {
		// testAllocation();
		// testPretenureSizeThreshold();
		//testTenuringThreshold();
		testTenuringThreshold2();
	}
}
