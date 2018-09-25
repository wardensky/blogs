package com.zch.blogs.java.map;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class LinkedHashMapDemo {
	public static void main(String[] args) {
		LinkedHashMap<Integer, String> map = new LinkedHashMap<Integer, String>(10, 0.75f, true);
		map.put(1, "a");
		map.put(2, "b");
		map.put(3, "c");
		map.put(4, "d");
		map.put(5, "e");
		for (int i = 0; i < 5; i++) {
			map.get(2);
		}
		map.get(4);
		Iterator iter = map.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			System.out.println(entry.getKey() + "=" + entry.getValue());

		}
	}
}
