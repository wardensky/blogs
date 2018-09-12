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
