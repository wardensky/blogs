package com.zch.blogs.algorithm.knapsackproblem;

import java.util.List;

/**
 * @Description 动态规划解决背包问题。
 * @author zch
 * @time 2018年9月26日 下午7:41:32
 * 
 */
public class KnapsackProblemDynamic {

	public static void main(String[] args) {
		List<TagObject> data = TagObjectUtil.init();
		KnapsackProblemDynamic kpd = new KnapsackProblemDynamic();
		int result = kpd.findMax(data, TagObjectUtil.BAG_CONTENT);
		System.out.println(result);
	}

	public int findMax(List<TagObject> data, int total) {
		int tagNumber = data.size();
		int[][] result = new int[tagNumber][total + 1];
		for (int rowIndex = 0; rowIndex < tagNumber; rowIndex++) {
			int weight = data.get(rowIndex).getWeight();
			int value = data.get(rowIndex).getValue();

			System.out.println("weight = " + weight + " value =" + value);
			for (int totalWeight = 1; totalWeight <= total; totalWeight++) {
				// 第一行单独处理。
				if (rowIndex == 0) {
					if (totalWeight < weight) {
						result[rowIndex][totalWeight] = 0;
					} else {
						result[rowIndex][totalWeight] = value;
					}
				} else {
					if (totalWeight < weight) {
						result[rowIndex][totalWeight] = result[rowIndex - 1][totalWeight];
					} else {
						int value1 = 0;
						value1 = result[rowIndex - 1][totalWeight];
						int value2 = value;
						if (totalWeight > weight) {
							value2 = value + result[rowIndex - 1][totalWeight - weight];
						}
						result[rowIndex][totalWeight] = value1 > value2 ? value1 : value2;
					}
				}
			}
		}
		return result[tagNumber - 1][total];
	}

}
