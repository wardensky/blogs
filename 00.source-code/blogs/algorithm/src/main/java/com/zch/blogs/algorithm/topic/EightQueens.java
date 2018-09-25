package com.zch.blogs.algorithm.topic;

/**
 * ## 问题描述：
 * 
 * 八皇后问题，是一个古老而著名的问题，是回溯算法的典型案例：在8X8格的国际象棋棋盘上摆放八个皇后，使其不能互相攻击，即任意两个皇后都不能处于同一行、同一列或同一斜线上，问有多少种摆法。
 * 
 * ## 问题求解：
 * 
 * 采用回溯算法，即从第一行开始，依次探查可以放置皇后的位置，若找到，则放置皇后，开始探查下一行；若该行没有位置可以放置皇后，则回溯至上一行，清除该行放置皇后的信息，从该行原本放置皇后的下一个位置开始探查可以放置皇后的位置。求所有解时，每找到一组解，就清除这一组解最后一个皇后的位置信息，开始探查该行另外一个可以放置皇后的位置，依次回溯求解。
 * 
 * @Description
 * @author zch
 * @time 2018年9月24日 下午10:20:42
 * 
 */
public class EightQueens {

	private int[] a = new int[8]; // 存储第i行皇后位于第a[i]列
	private static int count = 0;

	public static void main(String[] args) {
		EightQueens queen = new EightQueens();
		queen.Search(0);
		System.out.println("所有解的数量是：" + count);
	}

	public void Search(int m) {
		if (m >= 8) {
			System.out.println("八皇后的一组解为： " + m);
			count++;
			printResult();
		} else {
			System.out.println("m = " + m);
			for (int i = 0; i < 8; i++) {
				if (CanPlace(m, i)) {
					a[m] = i;
					Search(m + 1);
					a[m] = -10;
				}
			}
		}
	}

	private boolean CanPlace(int k, int j) {
		System.out.println("call can place k=" + k + " j=" + j);
		for (int i = 1; i <= k; i++) {
			if ((a[k - i] == j) || (a[k - i] == j - i) || (a[k - i] == j + i)) { // 判断左上，右上，该列有没有其他皇后
				return false;
			}
		}
		return true;

	}

	private void printResult() {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (a[i] == j) {
					System.out.print("Q");
				} else {
					System.out.print("0");
				}
			}
			System.out.println();
		}
	}

}
