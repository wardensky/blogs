package com.zch.blogs.algorithm.sort;

/**
 * @Description 在当前要排序的数组中，选取一个数为基准，然后将数组分成两部分，一部分是比当前数小，另外一部分比此基准大，然后分治递归相同的操作。
 *              快排的最坏的时间复杂度是最坏时间为O(n2)，平均时间复杂度是O(nlgn)。
 * @author zch
 * @time 2018年9月14日 上午11:15:23
 * 
 */
public class QuickSortDemo extends SortBase {
	public static void main(String[] args) {

		System.out.println("快排排序前");
		printArray(a);
		qs(a, 0, 8);
		System.out.println("快排排序后");
		printArray(a);
	}

	/**
	 * 这个是我自己实现的版本
	 * 
	 * @param n
	 * @param leftPoint
	 * @param rightPoint
	 */
	static void qs(int n[], int leftPoint, int rightPoint) {
		if (leftPoint == rightPoint) {
			return;
		}
		int pos = p(n, leftPoint, rightPoint);
		System.out.println("pos = " + pos);
		if (pos > 2) {
			qs(n, leftPoint, pos - 1);
		}
		if (pos + 1 < rightPoint) {
			qs(n, pos + 1, rightPoint);
		}
	}

	static int p(int n[], int leftPoint, int rightPoint) {

		if (leftPoint >= rightPoint) {
			return leftPoint;
		}
		int current = n[leftPoint];
		while (leftPoint < rightPoint) {

			System.out.println("rightPoint = " + rightPoint);
			System.out.println("leftPoint = " + leftPoint);

			while (leftPoint < rightPoint) {

				int rightValue = n[rightPoint];
				if (current > rightValue) {
					System.out.println("右边小交换");
					n[leftPoint] = rightValue;
					n[rightPoint] = current;
					printArray(n);

					break;
				}
				rightPoint--;

			}
			while (leftPoint < rightPoint) {

				int leftValue = n[leftPoint];
				if (current < leftValue) {
					System.out.println("左边大交换");
					n[leftPoint] = current;
					n[rightPoint] = leftValue;
					printArray(n);

					break;
				}
				leftPoint++;
			}

		}
		System.out.println("跳出 = " + leftPoint);
		return leftPoint;
	}

	static void quicksort(int n[], int left, int right) {
		int dp;
		if (left < right) {
			dp = partition(n, left, right);
			quicksort(n, left, dp - 1);
			quicksort(n, dp + 1, right);
		}
	}

	static int partition(int n[], int left, int right) {
		int pivot = n[left];
		while (left < right) {
			while (left < right && n[right] >= pivot)
				right--;
			if (left < right)
				n[left++] = n[right];
			while (left < right && n[left] <= pivot)
				left++;
			if (left < right)
				n[right--] = n[left];
		}
		n[left] = pivot;
		return left;
	}
}
