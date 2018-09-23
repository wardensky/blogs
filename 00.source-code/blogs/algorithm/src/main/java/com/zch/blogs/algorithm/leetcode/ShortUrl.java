package com.zch.blogs.algorithm.leetcode;

import java.util.HashMap;
import java.util.Map;

public class ShortUrl {

	// Encodes a URL to a shortened URL.
	public String encode(String longUrl) {
		current++;
		this.map.put(current, longUrl);
		return current + "";
	}

	// Decodes a shortened URL to its original URL.
	public String decode(String shortUrl) {
		return this.map.get(Integer.parseInt(shortUrl));
	}

	private int current = 0;
	private Map<Integer, String> map = new HashMap<Integer, String>();
}
