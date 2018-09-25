package com.zch.blogs.algorithm.topic;

import java.util.Stack;

/**
 * @Description 用两个堆栈实现队列的push和pop功能。<br/>
 *              这就是逆序啊。
 * @author zch
 * @time 2018年9月25日 下午9:00:22
 * 
 */
public class Stack2Queue {
	public static void main(String[] args) {
		Stack2Queue myQueue = new Stack2Queue();
		myQueue.push(1);
		myQueue.push(2);
		myQueue.push(3);
		myQueue.push(4);
		
		System.out.println(myQueue.pop());
		System.out.println(myQueue.pop());
		System.out.println(myQueue.pop());
		System.out.println(myQueue.pop());
	}

	private Stack<Integer> s1 = new Stack<Integer>();
	private Stack<Integer> s2 = new Stack<Integer>();

	public Integer pop() {
		
		while (!s1.isEmpty()) {
			s2.push(s1.pop());
		}
		Integer ret = s2.pop();
		while (!s2.isEmpty()) {
			s1.push(s2.pop());
		}
	
		return ret;
	}

	public void push(Integer item) {
		this.s1.push(item);
	}
}
