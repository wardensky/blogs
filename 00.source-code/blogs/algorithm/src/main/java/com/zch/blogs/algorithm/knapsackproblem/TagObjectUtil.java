package com.zch.blogs.algorithm.knapsackproblem;

import java.util.ArrayList;
import java.util.List;

public class TagObjectUtil {
	public static final int BAG_CONTENT = 10;

	public static List<TagObject> init() {
		List<TagObject> objects = new ArrayList<TagObject>();
		objects.add(new TagObject(2, 6));
		objects.add(new TagObject(2, 3));
		objects.add(new TagObject(6, 5));
		objects.add(new TagObject(5, 4));
		objects.add(new TagObject(4, 6));

		return objects;
	}
}
