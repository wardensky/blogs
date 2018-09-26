package com.zch.blogs.algorithm.knapsackproblem;

public class TagObject {
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
