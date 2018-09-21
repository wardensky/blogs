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

		findMdFiles(folder, ignoreList, fileList);
		// Collections.sort(fileList);

		for (String s : fileList) {
			System.out.println(s);
		}
		System.out.println(fileList.size());
	}

	static void findMdFiles(String folder, List<String> ignoreList, List<String> fileList) {
		if (fileList == null) {
			return;
		}

		File root = new File(folder);
		fileList.add("- " + root.getName());
		List<File> files = Arrays.asList(root.listFiles());
		Collections.sort(files);
		for (File f : files) {
			if (f.isFile()) {
				if (f.getName().endsWith(".md")) {
					fileList.add("	- " + f.getParentFile().getName() + "/" + f.getName());
				}
			} else {
				if (ignoreList.contains(f.getName())) {
					continue;
				}

				findMdFiles(f.getAbsolutePath(), ignoreList, fileList);
			}
		}
	}

}
