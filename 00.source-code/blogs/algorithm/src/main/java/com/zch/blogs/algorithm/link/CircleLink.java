package com.zch.blogs.algorithm.link;

import java.util.HashSet;
import java.util.Set;

/**
 * @Description 环形链表
 * @author zch
 * @time 2018年9月14日 下午9:40:55
 * 
 */
public class CircleLink {
	public static void main(String[] args) {

	}

	/**
	 * @Description 我们遍历所有结点并在哈希表中存储每个结点的引用（或内存地址）。如果当前结点为空结点
	 *              null（即已检测到链表尾部的下一个结点），那么我们已经遍历完整个链表，并且该链表不是环形链表。如果当前结点的引用已经存在于哈希表中，那么返回
	 *              true（即该链表为环形链表）。
	 * 
	 * @param head
	 * @return
	 */
	public boolean hasCycle(ListNode head) {
		Set<ListNode> nodesSeen = new HashSet<>();
		while (head != null) {
			if (nodesSeen.contains(head)) {
				return true;
			} else {
				nodesSeen.add(head);
			}
			head = head.next;
		}
		return false;
	}

	/**
	 * @Description 通过使用具有 不同速度 的快、慢两个指针遍历链表，空间复杂度可以被降低至 O ( 1 )
	 *              O(1)。慢指针每次移动一步，而快指针每次移动两步。
	 * 
	 *              如果列表中不存在环，最终快指针将会最先到达尾部，此时我们可以返回 false。
	 * 
	 *              现在考虑一个环形链表，把慢指针和快指针想象成两个在环形赛道上跑步的运动员（分别称之为慢跑者与快跑者）。而快跑者最终一定会追上慢跑者。这是为什么呢？考虑下面这种情况（记作情况
	 *              A） - 假如快跑者只落后慢跑者一步，在下一次迭代中，它们就会分别跑了一步或两步并相遇。
	 * @param head
	 * @return
	 */
	public boolean hasCycle1(ListNode head) {
		if (head == null || head.next == null) {
			return false;
		}
		ListNode slow = head;
		ListNode fast = head.next;
		while (slow != fast) {
			if (fast == null || fast.next == null) {
				return false;
			}
			slow = slow.next;
			fast = fast.next.next;
		}
		return true;
	}
}
