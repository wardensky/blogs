package com.zch.blogs.algorithm.sort;

/**
 * @Description 堆排序 查看
 *              https://github.com/wardensky/blogs/blob/master/06.algorithm/02.%E6%8E%92%E5%BA%8F%E7%9B%B8%E5%85%B3/%E5%A0%86%E6%8E%92%E5%BA%8F.md
 * @author zch
 * @time 2018年9月25日 下午8:08:08
 * 
 */
public class HeapSort extends SortBase {

	public static void main(String[] args) {
		// int[] arr = { 4, 6, 8, 5, 9 };
		// sort(arr);
		 printArray(a);
		 sort(a);
		 
	}

	

	public static void adjustSmallReal(int[] arr) {
		for (int i = arr.length / 2 - 1; i >= 0; i--) {
			// 从第一个非叶子结点从下至上，从右至左调整结构
			adjustHeapSmall(arr, i, arr.length);
			// myAdjustHeap(arr, i, arr.length);
			printArray(arr);
		}
	}
	
	public static void sort(int[] arr) {
		// 1.构建大顶堆
		for (int i = arr.length / 2 - 1; i >= 0; i--) {
			// 从第一个非叶子结点从下至上，从右至左调整结构
			adjustHeapBig(arr, i, arr.length);
			// myAdjustHeap(arr, i, arr.length);
			printArray(arr);
		}

		System.out.println("_____");
		// 2.调整堆结构+交换堆顶元素与末尾元素
		for (int j = arr.length - 1; j > 0; j--) {
			swap(arr, 0, j);// 将堆顶元素与末尾元素进行交换
			System.out.println(arr[j] + " 放到队尾。");
			adjustHeapBig(arr, 0, j);// 重新对堆进行调整
			printArray(arr);
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

	/**
	 * 调整大顶堆（仅是调整过程，建立在大顶堆已构建的基础上）
	 * 
	 * @param arr
	 * @param i
	 * @param length
	 */
	public static void adjustHeapBig(int[] arr, int i, int length) {
		System.out.println("call adjustheap big i = " + i + "    length = " + length);
		int temp = arr[i];// 先取出当前元素i
		for (int k = i * 2 + 1; k < length; k = k * 2 + 1) {
			// 从i结点的左子结点开始，也就是2i+1处开始 在这个地方用k = k *2 +1很巧妙
			if (k + 1 < length && arr[k] < arr[k + 1]) {
				// 如果左子结点小于右子结点，k指向右子结点
				k++;
			}
			if (arr[k] > temp) {// 如果子节点大于父节点，将子节点值赋给父节点（不用进行交换）
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

	public static void myAdjustHeap(int[] arr, int i, int length) {
		int temp = arr[i];// 先取出当前元素i
		int m = i;
		for (int k = i * 2 + 1; k < length; k = m * 2 + 1) {// 从i结点的左子结点开始，也就是2i+1处开始
			if (k + 1 < length && arr[k] < arr[k + 1]) {// 如果左子结点小于右子结点，k指向右子结点
				k++;
			}
			if (arr[k] > temp) {// 如果子节点大于父节点，将子节点值赋给父节点（不用进行交换）
				swap(arr, i, k);
				i = k;
				m = i;
			} else {
				break;
			}
		}

	}

	public static void swap(int[] arr, int a, int b) {
		int temp = arr[a];
		arr[a] = arr[b];
		arr[b] = temp;
	}
}
