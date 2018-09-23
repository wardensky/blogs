package com.zch.blogs.algorithm.link;

public class LinkNode {

	private int value;
	private LinkNode nextNode;

	public LinkNode(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public LinkNode setValue(int value) {
		this.value = value;
		return this;
	}

	public LinkNode getNextNode() {
		return nextNode;
	}

	public LinkNode setNextNode(LinkNode nextNode) {
		this.nextNode = nextNode;
		return this;
	}

	public static void printLink(LinkNode node) {
		node.print();
	}

	public void print() {
		LinkNode node = this;
		while (true) {
			if (node == null) {
				break;
			}
			System.out.print(node.getValue() + " ");
			node = node.getNextNode();
		}
		System.out.println("");
	}

	public static LinkNode genLink1() {
		LinkNode n1 = new LinkNode(1);
		LinkNode n2 = new LinkNode(3);
		LinkNode n3 = new LinkNode(5);
		LinkNode n4 = new LinkNode(7);
		n1.setNextNode(n2.setNextNode(n3.setNextNode(n4.setNextNode(null))));
		return n1;
	}

	public static LinkNode genLink2() {
		LinkNode n1 = new LinkNode(2);
		LinkNode n2 = new LinkNode(4);
		LinkNode n3 = new LinkNode(6);
		LinkNode n4 = new LinkNode(8);
		n1.setNextNode(n2.setNextNode(n3.setNextNode(n4.setNextNode(null))));
		return n1;
	}
}
