package com.zch.blogs.algorithm.search;

/**
 * @Description 折半查找
 * @author zch
 * @time 2018年9月23日 下午2:34:58
 * 
 */
public class BinarySearch {

	public static void main(String[] args) {
		int[] arr = { 1, 3, 5, 7, 9, 11 };
		int key = 7;
		System.out.println(findRecursive(arr, key, 0, arr.length - 1));
		System.out.println(findNonRecursive(arr, key));
	}

	/**
	 * 递归版本
	 * 
	 * @param arr
	 * @param target
	 * @param low
	 * @param high
	 * @return
	 */
	static int findRecursive(int[] arr, int target, int low, int high) {
		if (target < arr[low] || target > arr[high] || low > high) {
			return -1;
		}
		int middle = (low + high) / 2;
		if (arr[middle] == target) {
			return middle;
		}
		if (arr[middle] < target) {
			return findRecursive(arr, target, middle + 1, high);
		} else {
			return findRecursive(arr, target, low, middle - 1);
		}
	}

	/** 非递归版本
	 * @param arr
	 * @param target
	 * @return
	 */
	static int findNonRecursive(int[] arr, int target) {
		int low = 0;
		int high = arr.length - 1;
		int middle = 0;
		if (arr[low] > target || arr[high] < target) {
			return -1;
		}
		while (true) {
			middle = (low + high) / 2;
			if (low > high) {
				return -1;
			}
			if (arr[middle] == target) {
				return middle;
			}
			if (arr[middle] > target) {
				high = middle - 1;
			} else {
				low = middle + 1;
			}
		}

	}

}
