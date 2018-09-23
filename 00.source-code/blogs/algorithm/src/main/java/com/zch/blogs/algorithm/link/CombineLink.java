package com.zch.blogs.algorithm.link;

public class CombineLink {
	public static void main(String[] args) {
		LinkNode n1 = LinkNode.genLink1();
		LinkNode n2 = LinkNode.genLink2();
		LinkNode n3 = combineNonRecursive(n1, n2);
		LinkNode.printLink(n3);
	}

	/**
	 * 递归合并
	 * 
	 * @param link1
	 * @param link2
	 * @return
	 */
	static LinkNode combineRecursive(LinkNode link1, LinkNode link2) {
		if (link1 == null) {
			return link2;
		}
		if (link2 == null) {
			return link1;
		}
		if (link1.getValue() < link2.getValue()) {
			link1.setNextNode(combineRecursive(link1.getNextNode(), link2));
			return link1;
		} else {
			link2.setNextNode(combineRecursive(link1, link2.getNextNode()));
			return link2;
		}
	}

	/**
	 * 非递归合并
	 * 
	 * @param link1
	 * @param link2
	 * @return
	 */
	static LinkNode combineNonRecursive(LinkNode link1, LinkNode link2) {
		if (link1 == null) {
			return link2;
		}
		if (link2 == null) {
			return link1;
		}
		LinkNode retNode = null;// 头
		LinkNode currentNode = null;// 当前
		while (true) {
			if (link1 == null && link2 == null) {
				break;
			}

			if (link1 == null) {
				if (retNode == null) {
					retNode = link2;
					break;
				}
				currentNode.setNextNode(link2);
				break;
			}
			if (link2 == null) {
				if (retNode == null) {
					retNode = link1;
					break;
				}
				currentNode.setNextNode(link1);
				break;
			}
			if (link1.getValue() < link2.getValue()) {
				if (retNode == null) {
					retNode = link1;
					currentNode = retNode;
				} else {
					currentNode.setNextNode(link1);
					currentNode = currentNode.getNextNode();
				}
				link1 = link1.getNextNode();
			} else {
				if (retNode == null) {
					retNode = link2;
					currentNode = retNode;
				} else {
					currentNode.setNextNode(link2);
					currentNode = currentNode.getNextNode();
				}
				link2 = link2.getNextNode();
			}
		}

		return retNode;
	}

}
