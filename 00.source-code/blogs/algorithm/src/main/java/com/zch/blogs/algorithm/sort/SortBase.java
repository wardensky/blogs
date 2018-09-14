package com.zch.blogs.algorithm.sort;

public class SortBase {
	static int a[] = { 57, 68, 59, 52, 72, 28, 96, 33, 24 };

	static void printArray(int[] a) {

		for (int i : a) {
			System.out.print(i + " ");
		}
		System.out.println("");
	}

	static void printArray(int[] a, int start, int end) {

		for (int i = start; i <= end; i++) {
			System.out.print(a[i] + " ");
		}
		System.out.println("");
	}
}
