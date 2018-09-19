package com.zch.blogs.framework.mybatis;

import java.util.List;

public interface WebContentPoMapper {
	List<WebContentPo> selectAll();
	int insert(WebContentPo entity);
}
