package com.zch.spring_code_demo.demo2;

public class BllFactory {
	public static IPersonBll getPersonBll() {
		return new PersonBll3("老李");
	}
}
