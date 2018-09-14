package com.zch.blogs.algorithm.sort;

public class MergeSortDemo extends SortBase {
	public static void main(String[] args) {

		int[] a = { 1, 3, 5 };
		int[] b = { 2, 4, 6, 8, 10 };
		int[] c = merge(a, b);
		printArray(c);
	}

	static void sort(int[] a) {
		while (true) {

		}
	}
	
	static void mergeArray(int[] array, int start1, int start2, int start3) {
		
	}
	

	/**
	 * 合并两个已经排序好的数组，需要一个额外的空间。
	 * 
	 * @param array1
	 * @param array2
	 * @return
	 */
	static int[] merge(int[] array1, int[] array2) {
		int[] ret = new int[array1.length + array2.length];
		int index1 = 0;
		int index2 = 0;

		while (true) {
			if (index2 >= array2.length) {
				while (index1 <= array1.length - 1) {
					ret[index1 + index2] = array1[index1];
					index1++;
				}
				break;
			}
			if (index1 >= array1.length) {
				while (index2 <= array2.length - 1) {
					ret[index1 + index2] = array2[index2];
					index2++;
				}
				break;
			}
			if (array1[index1] > array2[index2]) {
				ret[index1 + index2] = array2[index2];
				index2++;
			} else {
				ret[index1 + index2] = array1[index1];
				index1++;
			}
			if (index1 + index2 == ret.length) {
				break;
			}
		}
		return ret;
	}
}
