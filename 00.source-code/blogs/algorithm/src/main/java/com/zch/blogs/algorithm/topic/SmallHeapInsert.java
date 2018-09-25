package com.zch.blogs.algorithm.topic;

import com.zch.blogs.algorithm.sort.SortBase;

/**
 * @Description 小顶堆插入。 给一个小顶堆，再给你一个数，让你插入到这个堆里。
 * @author zch
 * @time 2018年9月25日 下午8:35:33
 * 
 */
public class SmallHeapInsert {

	public static void main(String[] args) {
		int[] arr = init();
		int[] arr1 = insert(arr, 2);
		SortBase.printArray(arr1);
	}

	public static int[] insert(int[] arr, int value) {
		int[] newArr = new int[arr.length + 1];
		for (int i = 0; i < arr.length; i++) {
			newArr[i] = arr[i];
		}
		newArr[arr.length] = value;
		ajust(newArr, arr.length);
		return newArr;
	}

	public static void ajust(int[] arr, int index) {
		if (index <= 0) {
			return;
		}
		int tmp;
		int parentIndex = (index - 1) / 2;
		if (arr[index] < arr[parentIndex]) {
			tmp = arr[parentIndex];
			arr[parentIndex] = arr[index];
			arr[index] = tmp;
			ajust(arr, parentIndex);
		}
	}

	public static int[] init() {
		int[] arr = { 4, 5, 8, 6, 9, 3,10,11,1 };
		for (int i = arr.length / 2 - 1; i >= 0; i--) {
			// 从第一个非叶子结点从下至上，从右至左调整结构
			adjustHeapSmall(arr, i, arr.length);
			SortBase.printArray(arr);
		}
		SortBase.printArray(arr);
		return arr;
	}

	public static void adjustSmallReal(int[] arr) {
		for (int i = arr.length / 2 - 1; i >= 0; i--) {
			// 从第一个非叶子结点从下至上，从右至左调整结构
			adjustHeapSmall(arr, i, arr.length);
			// myAdjustHeap(arr, i, arr.length);
			SortBase.printArray(arr);
		}
	}

	/**
	 * 调整小顶堆（仅是调整过程，建立在小顶堆已构建的基础上）
	 * 
	 * @param arr
	 * @param i
	 * @param length
	 */
	public static void adjustHeapSmall(int[] arr, int i, int length) {
		System.out.println("call adjustheap small i = " + i + "    length = " + length);
		int temp = arr[i];// 先取出当前元素i
		for (int k = i * 2 + 1; k < length; k = k * 2 + 1) {
			// 从i结点的左子结点开始，也就是2i+1处开始 在这个地方用k = k *2 +1很巧妙
			if (k + 1 < length && arr[k] > arr[k + 1]) {
				// 如果左子结点小于右子结点，k指向右子结点
				k++;
			}
			if (arr[k] < temp) {// 如果子节点小于于父节点，将子节点值赋给父节点（不用进行交换）
				System.out.println("swap i=" + i + "  k=" + k);
				arr[i] = arr[k];
				i = k;
			} else {
				System.out.println("break");
				break;
			}
		}
		arr[i] = temp;// 将temp值放到最终的位置
	}

}
