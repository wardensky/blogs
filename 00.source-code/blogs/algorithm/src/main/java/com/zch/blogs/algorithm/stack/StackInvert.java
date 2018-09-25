package com.zch.blogs.algorithm.stack;

import java.util.Stack;

/**
 * @Description 递归反转栈的顺序-------只使用常数量个变量
 * @author zch
 * @time 2018年9月25日 下午7:28:06
 * 
 */
public class StackInvert {
	public static void main(String[] args) {
		Stack<Integer> s1 = init();
		StackBase.print(s1);
		Stack<Integer> s2 = invert(s1);
		StackBase.print(s2);
	}

	public static Stack<Integer> init() {
		Stack<Integer> s1 = new Stack<Integer>();
		s1.push(5);
		s1.push(4);
		s1.push(3);
		s1.push(2);
		s1.push(1);
		return s1;
	}

	static Stack<Integer> invert(Stack<Integer> s) {
		if (s.size() == 1) {
			return s;
		}
		Integer head = s.pop();
		Stack<Integer> t = invert(s);
		t.push(head);
		return t;
	}
}
