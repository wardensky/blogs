package com.zch.blogs.algorithm.tree;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class TreeSearch {

	public static void main(String[] args) {
		TreeSearch ts = new TreeSearch();
		 Map<String, Object> node = ts.initTree();
		 ts.depthFirst(node);
		 
	}

	public Map<String, Object> initTree() {
		Map<String, Object> nodeA = new HashMap<String, Object>();
		Map<String, Object> nodeB = new HashMap<String, Object>();
		Map<String, Object> nodeC = new HashMap<String, Object>();
		Map<String, Object> nodeD = new HashMap<String, Object>();
		Map<String, Object> nodeE = new HashMap<String, Object>();
		Map<String, Object> nodeF = new HashMap<String, Object>();
		Map<String, Object> nodeG = new HashMap<String, Object>();
		Map<String, Object> nodeH = new HashMap<String, Object>();
		Map<String, Object> nodeI = new HashMap<String, Object>();
		List<Map<String, Object>> child1 = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> child2 = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> child3 = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> child4 = new ArrayList<Map<String, Object>>();
		child1.add(nodeC);
		child1.add(nodeB);
		child2.add(nodeE);
		child2.add(nodeD);
		child3.add(nodeI);
		child4.add(nodeH);
		child4.add(nodeG);
		child4.add(nodeF);
		
		
		nodeA.put("A", child1);
		nodeB.put("B", child2);
		nodeC.put("C", child4);
		nodeD.put("D", null);
		nodeE.put("E", child3);
		nodeF.put("F", null);
		nodeG.put("G", null);
		nodeH.put("H", null);
		nodeI.put("I", null);
		return nodeA;
	}

	/**
	 * 深度优先遍历 节点使用Map存放
	 */
	public void depthFirst(Map<String, Object> node ) {
		Stack<Map<String, Object>> nodeStack = new Stack<Map<String, Object>>();
		//Map<String, Object> node = new HashMap<String, Object>();
		nodeStack.add(node);
		while (!nodeStack.isEmpty()) {
			node = nodeStack.pop();
			System.out.println(node);
			// 获得节点的子节点，对于二叉树就是获得节点的左子结点和右子节点
			List<Map<String, Object>> children = getChildren(node);
			if (children != null && !children.isEmpty()) {
				for (Map child : children) {
					nodeStack.push(child);
				}
			}
		}
		while (!nodeStack.empty()) {
			Map<String, Object> node1 = nodeStack.pop();
			print(node1);
		}
	}

	/**
	 * 广度优先遍历
	 */
	public void breadthFirst() {
		Deque<Map<String, Object>> nodeDeque = new ArrayDeque<Map<String, Object>>();
		Map<String, Object> node = new HashMap<String, Object>();
		nodeDeque.add(node);
		while (!nodeDeque.isEmpty()) {
			node = nodeDeque.peekFirst();
			System.out.println(node);
			// 获得节点的子节点，对于二叉树就是获得节点的左子结点和右子节点
			List<Map<String, Object>> children = getChildren(node);
			if (children != null && !children.isEmpty()) {
				for (Map child : children) {
					nodeDeque.add(child);
				}
			}
		}
	}

	private void print(Map<String, Object> node) {
		for (String key : node.keySet()) {
			System.out.println(key);
			return;
		}
	}

	private List<Map<String, Object>> getChildren(Map<String, Object> node) {

		for (String key : node.keySet()) {
			Object o = node.get(key);
			if (null == o)
				return null;
			return (List<Map<String, Object>>) o;
		}
		return null;
	}
}
