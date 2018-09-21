package com.zch.blogs.algorithm.toolkit;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileSearcher {
	static int key = 1;
	static Map<Integer, String> keyMap = new HashMap<Integer, String>(1000);

	public static void main(String[] args) {
		String folder = "/Users/zch/github/blogs";

		if (args.length > 1) {
			System.out.println(args[0]);
			folder = args[0];
		}

		List<String> ignoreList = new ArrayList<String>();
		ignoreList.add("images");
		ignoreList.add("00.source-code");
		ignoreList.add(".git");
		ignoreList.add(".DS_Store");
		ignoreList.add("LICENSE");
		ignoreList.add("README.md");
		List<String> fileList = new ArrayList<String>(1000);

		findMdFiles(folder, ignoreList, fileList, -1);

		for (String s : fileList) {
			System.out.println(s);
		}
		System.out.println();
		for (Integer k : keyMap.keySet()) {
			System.out.println("[" + k + "]:" + keyMap.get(k));
		}

	}

	static String findNParent(File file, int depth) {
		StringBuilder sb = new StringBuilder();
		String path = file.getAbsolutePath();
		String[] array = path.split("/");

		for (int i = (array.length - depth - 1); i < array.length; i++) {
			sb.append(array[i]);
			sb.append("/");
		}
		return sb.substring(0, sb.length() - 1).replace(" ", "%20");
	}

	static final String prefix = "https://github.com/wardensky/blogs/blob/master/";

	static String addUrl(File file, int depth) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < depth; i++) {
			sb.append("	");
		}
		if (file.isDirectory()) {
			sb.append("- [" + file.getName());
			sb.append("][");
			sb.append(key);
			sb.append("]");
			keyMap.put(key, prefix + findNParent(file, depth));
			key++;
			return sb.toString();
		} else {
			String fileName = file.getName();
			sb.append("- [");
			sb.append(fileName.replace(".md", ""));
			sb.append("][");
			sb.append(key);
			sb.append("]");
			keyMap.put(key, prefix + findNParent(file, depth));
			key++;
			return sb.toString();
		}
	}

	static void findMdFiles(String folder, List<String> ignoreList, List<String> fileList, int depth) {

		if (fileList == null) {
			return;
		}

		File root = new File(folder);
		if (!root.getName().equals("blogs")) {
			fileList.add(addUrl(root, depth));
		}
		List<File> files = Arrays.asList(root.listFiles());
		Collections.sort(files);
		depth++;
		for (File f : files) {
			if (ignoreList.contains(f.getName())) {
				continue;
			}
			if (f.isFile()) {
				if (f.getName().endsWith(".md")) {
					fileList.add(addUrl(f, depth));
				}
			} else {
				if (ignoreList.contains(f.getName())) {
					continue;
				}

				findMdFiles(f.getAbsolutePath(), ignoreList, fileList, depth);
			}
		}
	}

}
