package com.zch.blogs.algorithm.topic;

/**
 * @Description 用字符串表示整数，实现整数的加法： 如“9991” + “209”
 * @author zch
 * @time 2018年9月22日 下午3:13:35
 * 
 */
public class StringInteger {
	public static void main(String[] args) {
		String input1 = "900000999009909099000009990099090990000099900990909900000999009909099000009990099090990000099900990909900000999009909099000009990099090990000099900990909900000999009909099";
		String input2 = "9000009990099090990000099900990909900000999009909099000009990099090990000099900990909900000999009909099000009990099090990000099900990909900000999009909099000009990099090911";
		String out = stringAdd(input1, input2);
		System.out.println(out);
	}

	static String stringAdd(String input1, String input2) {
		int max = input1.length() > input2.length() ? input1.length() + 1 : input2.length() + 1;
		String ins1 = addZero(input1, max);
		String ins2 = addZero(input2, max);
		int[] ret = new int[max];
		char[] arr1 = ins1.toCharArray();
		char[] arr2 = ins2.toCharArray();

		charArrayAdd(arr1, arr2, max - 1, 0, ret);
		StringBuilder sb = new StringBuilder();
		if (ret[0] == 0) {
			sb.append("");
		} else {
			sb.append(ret[0]);
		}
		for (int i = 1; i < max; i++) {
			sb.append(ret[i]);
		}
		return sb.toString();

	}

	static String addZero(String s, int max) {
		int zeroNumber = max - s.length();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < zeroNumber; i++) {
			sb.append("0");
		}
		return sb.append(s).toString();
	}

	static void charArrayAdd(char[] input1, char[] input2, int index, int extra, int[] ret) {

		if (index < 0) {
			return;
		}
		int value = convert(input1[index]) + convert(input2[index]) + extra;

		extra = value > 9 ? 1 : 0;
		value = value % 10;
		ret[index] = value;
		index--;
		charArrayAdd(input1, input2, index, extra, ret);
	}

	static int convert(char c) {
		return Integer.parseInt(c + "");
	}

	static char convert(int i) {
		return (char) (i);
	}
}
