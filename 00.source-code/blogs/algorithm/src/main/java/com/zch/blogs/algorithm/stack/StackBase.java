package com.zch.blogs.algorithm.stack;

import java.util.Stack;

public class StackBase {
	public static void print(Stack<Integer> s) {
		Stack<Integer> t = new Stack<>();
		while (true) {
			if (s.isEmpty()) {
				break;
			}
			Integer i = s.pop();
			t.push(i);
			System.out.println(i);
		}
		while (true) {
			if (t.isEmpty()) {
				break;
			}
			s.push(t.pop());
		}
	}
}
