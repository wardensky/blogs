package com.zch.blogs.java.multithreads.simple;

public class SimpleThread {
	static String s = "AA72000433B879860000030A";
	                 //AA72000433B8619E0000030A
	public static void main(String[] args) {
		System.out.println(s);
		System.out.println("++++++++++");
		byte[] ret = hexStringToByte(s);
		System.out.println("++++++++++");
		bytesToInts(ret);
		System.out.println("++++++++++");
		hexS2IntArray(s);
	}

	public static int[] hexS2IntArray(String hexString) {
		int[] result = new int[hexString.length()];
		char[] achar = hexString.toCharArray();
		for (int i = 0; i < result.length; i++) {
			result[i] = (int) "0123456789ABCDEF".indexOf(achar[i]);
		}
		for (int i : result) {
			System.out.print(i + " ");
		}
		return result;
	}

	public static byte[] hexStringToByte(String hex) {
		int len = (hex.length() / 2);
		byte[] result = new byte[len];
		char[] achar = hex.toCharArray();
		for (int i = 0; i < len; i++) {
			int pos = i * 2;
			result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
		}
		for (byte b : result) {
			System.out.print(b + " ");
		}
		return result;
	}
 
	private static byte toByte(char c) {
		byte b = (byte) "0123456789ABCDEF".indexOf(c);
		return b;
	}

	public static int[] bytesToInts(byte[] bytes) {
		int bytesLength = bytes.length;
		int[] ints = new int[bytesLength % 4 == 0 ? bytesLength / 4 : bytesLength / 4 + 1];
		int lengthFlag = 4;
		while (lengthFlag <= bytesLength) {
			ints[lengthFlag / 4 - 1] = (bytes[lengthFlag - 4] << 24) | (bytes[lengthFlag - 3] & 0xff) << 16
					| (bytes[lengthFlag - 2] & 0xff) << 8 | (bytes[lengthFlag - 1] & 0xff);
			lengthFlag += 4;
		}
		for (int i = 0; i < bytesLength + 4 - lengthFlag; i++) {
			if (i == 0)
				ints[lengthFlag / 4 - 1] |= bytes[lengthFlag - 4 + i] << 8 * (bytesLength + 4 - lengthFlag - i - 1);
			else
				ints[lengthFlag / 4 - 1] |= (bytes[lengthFlag - 4 + i] & 0xff) << 8
						* (bytesLength + 4 - lengthFlag - i - 1);
		}
		for (int i : ints) {
			System.out.print(i + " ");
		}
		return ints;
	}

}
