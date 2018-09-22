package com.zch.blogs.algorithm.sort;

import java.util.Arrays;

public class SortBase {
	static int a[] = { 57, 68, 59, 52, 72, 28, 96, 33, 24 };

	static void printArray(int[] a) {
		System.out.println(Arrays.toString(a));
		 
	}

	static void printArray(int[] a, int start, int end) {

		for (int i = start; i <= end; i++) {
			System.out.print(a[i] + " ");
		}
		System.out.println("");
	}
}
