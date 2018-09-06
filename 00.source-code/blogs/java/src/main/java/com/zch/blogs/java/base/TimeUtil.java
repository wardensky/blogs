package com.zch.blogs.java.base;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {
	private final static SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss ");

	public static String getShortTime() {
		return df.format(new Date());
	}
}
