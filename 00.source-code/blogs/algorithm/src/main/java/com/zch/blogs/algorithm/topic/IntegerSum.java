package com.zch.blogs.algorithm.topic;

/**
 * @Description 给函数传递一个正整数的列表alist和一个正整数T，
 *              假装它等于[1,3,6,4,2,7]，给出alist里所有相加等于T的元素的list，
 *              每个数只用一次。比如T=7，列表里3+4=7，7=7，1+6=7。 你的函数就要返回[[3,4],[7],[1,6]]。
 * @author zch
 * @time 2018年9月26日 下午4:54:31
 * 
 */
public class IntegerSum {

	private final static int TOTAL = 7;

	public static void main(String[] args) {
		IntegerSum is = new IntegerSum();
		int[] ret = is.init();
		int[] newA = new int[ret.length];
		is.search(newA, 0, ret);
	}

	public int[] init() {
		int[] ret = { 1, 3, 6, 4, 2, 7 };
		return ret;
	}

	public void search(final int[] a, int index, final int[] oriA) {
		if (this.sumArray(a) == TOTAL) {
			print(a);
			index = 0;
		} else {
			for (int i = index; i < oriA.length; i++) {
			//	System.out.println("index = " + index);
				if (judge(a, oriA[i], i)) {
					index++;
					search(a, index, oriA);
				} else if (i == oriA.length - 1) {
					a[index - 1] = 0;
				}
			}
		}
	}

	private void print(int[] a) {
		System.out.println("找到结果了。");
		for (int i = 0; i < a.length; i++) {
			if (a[i] > 0) {
				System.out.print(a[i]);
				System.out.print(" ");
			}
		}
		System.out.println();
	}

	/**
	 * 在一个数组的基础上，判断是否能再加一个数。
	 * 
	 * @param a
	 * @param data
	 * @param index
	 * @param TOTAL
	 * @return
	 */
	public boolean judge(final int[] a, final int data, final int index) {
		//System.out.println(data);
		//print(a);
		boolean ret = sumArray(a, data) <= TOTAL;
		if (ret) {
			a[index] = data;
		}
		return ret;
	}

	public int sumArray(int[] a, int data) {
		return sumArray(a) + data;
	}

	public int sumArray(int[] a) {
		int ret = 0;
		for (int i = 0; i < a.length; i++) {
			ret += a[i];
		}
		return ret;
	}
}
