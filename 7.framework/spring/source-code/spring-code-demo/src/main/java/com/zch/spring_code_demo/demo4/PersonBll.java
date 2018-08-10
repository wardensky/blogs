package com.zch.spring_code_demo.demo4;

import java.util.Set;

import com.zch.spring_code_demo.demo2.IPersonBll;

public class PersonBll  implements IPersonBll {
	private Set<String> idSet;

	public Set<String> getIdSet() {
		return idSet;
	}

	public void setIdSet(Set<String> idSet) {
		this.idSet = idSet;
	}

	public void show() {
		for (String s : this.idSet) {
			System.out.println(s);
		}
	}
}
