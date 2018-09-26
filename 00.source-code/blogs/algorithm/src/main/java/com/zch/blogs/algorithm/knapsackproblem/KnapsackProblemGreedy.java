package com.zch.blogs.algorithm.knapsackproblem;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description 0-1背包问题。
 *              有N件物品和一个承重为C的背包，每件物品的重量是w，价值是p，求解放哪几件物品到背包可使背包不超重的情况价值总和最大。
 *              选中策略，有三种策略： 1）选中价值最高的；2）选则最轻的；3）选则性价比最高的。
 * @author zch
 * @time 2018年9月23日 上午10:10:55
 * 
 */
public class KnapsackProblemGreedy {

	public static void main(String[] args) {
		final int BAG_CONTENT = 1300;
		List<TagObject> objects = new ArrayList<TagObject>();
		objects.add(new TagObject(101, 101));
		objects.add(new TagObject(220, 202));
		objects.add(new TagObject(330, 303));
		objects.add(new TagObject(404, 404));
		objects.add(new TagObject(555, 505));
		objects.add(new TagObject(606, 606));
		List<TagObject> selectObjects = new ArrayList<TagObject>();
		while (true) {
			TagObject select = chooseFunc2(objects);
			if (select == null) {
				break;
			}
			if (calTotalWeight(selectObjects) + select.getWeight() > BAG_CONTENT) {
				select.setStatus(TagObjectStatus.不可用);
			} else {
				selectObjects.add(select);
				select.setStatus(TagObjectStatus.已选中);
			}
		}
		selectObjects.stream().forEach(o -> System.out.println(o));
		System.out.println(calTotalValue(selectObjects));
	}

	static int calTotalWeight(List<TagObject> tagList) {
		return tagList.stream().mapToInt(tag -> tag.getWeight()).sum();
	}

	static int calTotalValue(List<TagObject> tagList) {
		return tagList.stream().mapToInt(tag -> tag.getValue()).sum();
	}

	/**
	 * 选价值最高的
	 * 
	 * @return
	 */
	static TagObject chooseFunc1(List<TagObject> tagList) {
		TagObject ret = null;
		int value = 0;
		for (int i = 0; i < tagList.size(); i++) {
			if (tagList.get(i).getStatus().equals(TagObjectStatus.未选中)) {
				if (tagList.get(i).getValue() > value) {
					value = tagList.get(i).getValue();
					ret = tagList.get(i);
				}
			}
		}
		return ret;
	}

	/**
	 * 选最轻的
	 * 
	 * @return
	 */
	static TagObject chooseFunc2(List<TagObject> tagList) {
		TagObject ret = null;
		int weight = Integer.MAX_VALUE;
		for (int i = 0; i < tagList.size(); i++) {
			if (tagList.get(i).getStatus().equals(TagObjectStatus.未选中)) {
				if (tagList.get(i).getWeight() < weight) {
					weight = tagList.get(i).getValue();
					ret = tagList.get(i);
				}
			}
		}
		return ret;
	}

	/**
	 * 选性价比最高的
	 * 
	 * @return
	 */
	static TagObject chooseFunc3(List<TagObject> tagList) {
		TagObject ret = null;
		double d = 0;
		for (int i = 0; i < tagList.size(); i++) {
			if (tagList.get(i).getStatus().equals(TagObjectStatus.未选中)) {
				if (tagList.get(i).getValue() / tagList.get(i).getWeight() > d) {
					d = tagList.get(i).getValue() / tagList.get(i).getWeight();
					ret = tagList.get(i);
				}
			}
		}
		return ret;
	}
}

enum TagObjectStatus {

	未选中, 已选中, 不可用;
}

class TagObject {
	private int weight;
	private int value;
	private TagObjectStatus status;

	public TagObject(int weight, int value) {
		this.weight = weight;
		this.value = value;
		this.status = TagObjectStatus.未选中;
	}

	public int getWeight() {
		return weight;
	}

	public TagObject setWeight(int weight) {
		this.weight = weight;
		return this;
	}

	public int getValue() {
		return value;
	}

	public TagObject setValue(int value) {
		this.value = value;
		return this;
	}

	public TagObjectStatus getStatus() {
		return status;
	}

	public TagObject setStatus(TagObjectStatus status) {
		this.status = status;
		return this;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("weight = ");
		sb.append(this.weight);
		sb.append(" value = ");
		sb.append(this.value);
		return sb.toString();
	}
}
