package com.zch.blogs.base;

/**
 * @Description 主要介绍位移操作 <br>
 *              java中有三种移位运算符
 * 
 *              << : 左移运算符，num << 1,相当于num乘以2
 * 
 *              >> : 右移运算符，num >> 1,相当于num除以2
 * 
 *              >>> : 无符号右移，忽略符号位，空位都以0补齐
 * @author zch
 *
 */
public class ShiftDemo {
	public static void main(String[] args) {
		oper1();
		oper2();
		oper3();
		oper4();
		oper5();
		/**
		 * 输出：2<br/>
		 * 8<br/>
		 * -2<br/>
		 * 16777215 = 2 ^ 24 - 1<br/>
		 * -1<br/>
		 **/

	}

	/**
	 * 最简单的右移，就是除2
	 */
	static void oper1() {
		int i = 4;
		System.out.println(i >> 1);
	}

	/**
	 * 最简单的左移，就是乘2
	 */
	static void oper2() {
		int i = 4;
		System.out.println(i << 1);
	}

	/**
	 * 负数的右移
	 */
	static void oper3() {
		int i = -4;
		System.out.println(i >> 1);
	}

	/**
	 * 负数的无符号右移
	 * 负数就是原码取反=反码，然后再加一，得到补码。
	 * 补码就是负数在计算机中的二进制表示方法。
	 */
	static void oper4() {
		int i = -4;
		System.out.println(i >>> 8);
		//输出：16777215 = 2 ^ 24 - 1<br/>
	}

	/**
	 * 负数的右移
	 */
	static void oper5() {
		int i = -4;
		System.out.println(i >> 2);
	}
}
