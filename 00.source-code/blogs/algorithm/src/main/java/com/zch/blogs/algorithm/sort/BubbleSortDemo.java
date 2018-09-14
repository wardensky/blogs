package com.zch.blogs.algorithm.sort;

/**
 * @Description 冒泡排序。
 *              它重复地走访过要排序的元素列，依次比较两个相邻的元素，如果他们的顺序（如从大到小、首字母从A到Z）错误就把他们交换过来。
 *              走访元素的工作是重复地进行直到没有相邻元素需要交换，也就是说该元素已经排序完成。
 * @author zch
 * @time 2018年9月14日 下午2:45:57
 * 
 */
public class BubbleSortDemo extends SortBase {
	public static void main(String[] args) {

		System.out.println("冒泡排序前");
		printArray(a);
		sort(a);
		System.out.println("冒泡排序后");
		printArray(a);
	}

	static void sort(int[] array) {
		int j = array.length - 1;
		while (true) {
			for (int i = 0; i < j; i++) {
				if (array[i] > array[i + 1]) {
					int tmp = array[i];
					array[i] = array[i + 1];
					array[i + 1] = tmp;
				}
			}
			j--;
			if (j == 0) {
				break;
			}
		}
	}
}
