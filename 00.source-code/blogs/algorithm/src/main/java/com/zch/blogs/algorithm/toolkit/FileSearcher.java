package com.zch.blogs.algorithm.toolkit;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FileSearcher {
	public static void main(String[] args) {
		String folder = "/Users/zch/github/blogs";
		List<String> ignoreList = new ArrayList<String>();
		ignoreList.add("images");
		ignoreList.add("00.source-code");
		ignoreList.add(".git");
		ignoreList.add(".DS_Store");
		ignoreList.add("LICENSE");
		ignoreList.add("README.md");
		List<String> fileList = new ArrayList<String>(1000);

		findMdFiles(folder, ignoreList, fileList, 0);
		// Collections.sort(fileList);

		for (String s : fileList) {
			System.out.println(s);
		}
		System.out.println(fileList.size());
	}

	static String addUrl(File file, int depth) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < depth; i++) {
			sb.append("	");
		}
		if (file.isDirectory()) {
			sb.append("- " + file.getName());
			return sb.toString();
		} else {
			final String prefix = "https://github.com/wardensky/blogs/blob/master/";
			String fileName = file.getName();
			sb.append("- ");
			sb.append(fileName);
			return sb.toString();
			//return "[" + fileName.replace(".md", "") + "](" + prefix + ")";
		}
	}

	static void findMdFiles(String folder, List<String> ignoreList, List<String> fileList, int depth) {

		if (fileList == null) {
			return;
		}

		File root = new File(folder);
		fileList.add(addUrl(root, depth));
		List<File> files = Arrays.asList(root.listFiles());
		Collections.sort(files);
		depth++;
		for (File f : files) {
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
