package com.zch.blogs.algorithm.sort;

/**
 * @Description 归并的含义是将两个或两个以上的有序表合并成一个新的有序表。归并排序有多路归并排序、两路归并排序,
 *              可用于内排序，也可以用于外排序。
 * @author zch
 * @time 2018年9月14日 下午6:30:18
 * 
 */
public class MergeSortDemo extends SortBase {
	public static void main(String[] args) {
		System.out.println("排序之前");
		printArray(a);
		sort(a, 0, a.length - 1);
		System.out.println("排序之后");
		printArray(a);
	}

	static int[] newArray(int[] array, int start, int end) {

		if (start > end) {
			return array;
		}
		int[] ret = new int[end - start + 1];
		int j = 0;
		for (int i = start; i <= end; i++) {
			ret[j] = array[i];
			j++;
		}
		return ret;
	}

	static void sort(int[] array, int start, int end) {
		System.out.println("\n单次分割");
		printArray(array, start, end);

		if (start >= end) {
			return;
		}

		int mid = (start + end) / 2;
		System.out.println("mid = " + mid);
		System.out.println("分割结果");

		printArray(array, start, mid);
		sort(array, start, mid);

		printArray(array, mid + 1, end);
		sort(array, mid + 1, end);

		mergeArray(array, start, mid, end);

	}

	static void mergeArray(int[] array, int start, int mid, int end) {

		int[] array1 = newArray(array, start, mid);
		int[] array2 = newArray(array, mid + 1, end);
		int[] ret = merge(array1, array2);
		for (int i = 0; i < ret.length; i++) {
			array[start + i] = ret[i];
		}
	}

	/**
	 * 合并两个已经排序好的数组，需要一个额外的空间。
	 * 
	 * @param array1
	 * @param array2
	 * @return
	 */
	static int[] merge(int[] array1, int[] array2) {
		System.out.println("\n单次合并");
		printArray(array1);
		printArray(array2);
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
		System.out.print("合并结果 ");
		printArray(ret);
		return ret;
	}
}
