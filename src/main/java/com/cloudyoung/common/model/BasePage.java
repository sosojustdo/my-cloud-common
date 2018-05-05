package com.cloudyoung.common.model;

import java.io.Serializable;

/**
 * Description:分页基础类
 * All Rights Reserved.
 * @version 1.0  2017年12月6日 下午3:03:58  by 代鹏（daipeng.456@gmail.com）创建
 */
public class BasePage implements Serializable{
	
	private static final long serialVersionUID = 4258742125928697918L;
	
	private Integer pageNo = 1; // 分页页数
	private Integer pageSize = 10; // 分页大小
	private Integer pageStart; // 分页数据起始位置
	private Integer pageEnd; // 分页数据截至位置

	public Integer getPageNo() {
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getPageStart() {
		if (pageNo != null && pageSize != null) {
			return (pageNo - 1) * pageSize;
		}
		return pageStart;
	}

	public void setPageStart(Integer pageStart) {
		this.pageStart = pageStart;
	}

	public Integer getPageEnd() {
		if (pageNo != null && pageSize != null) {
			return pageNo * pageSize;
		}
		return pageEnd;
	}

	public void setPageEnd(Integer pageEnd) {
		this.pageEnd = pageEnd;
	}
}