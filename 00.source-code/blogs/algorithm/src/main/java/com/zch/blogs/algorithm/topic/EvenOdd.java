package com.zch.blogs.algorithm.topic;

import com.zch.blogs.algorithm.sort.SortBase;

public class EvenOdd extends SortBase {

	public static void main(String[] args) {
		printArray(a);
		convert(a);
		printArray(a);
	}

	static void convert(int[] array) {
		int number = 0;
		for (int i = 0; i < array.length; i++) {
			boolean isEven = false;
			if (array[i] % 2 == 0) {
				number++;
				isEven = true;
			}
			if (number > 0 && !isEven) {
				System.out.println("number=" + number + " i=" + i);
				swap(array, i, number);

			}
		}
	}

	static void swap(int[] array, int index, int number) {
		for (int i = index; i > index - number; i--) {
			int tmp = array[i];
			array[i] = array[i - 1];
			array[i - 1] = tmp;
		}
	}

}
