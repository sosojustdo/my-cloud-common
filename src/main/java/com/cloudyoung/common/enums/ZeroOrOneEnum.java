package com.cloudyoung.common.enums;

/**
 * Description: 0 1标识
 * Copyright (c) Department of Research and Development/Beijing
 * All Rights Reserved.
 * @version 1.0  2016年8月9日 上午11:10:51  by 陆怀彬（luhb@cloud-young.com）陆怀彬
 */
public enum ZeroOrOneEnum {
	ZERO(0,"否"),
	ONE(1,"是");
	
	private Integer code;
	private String  desc ;
	
	private ZeroOrOneEnum(Integer code, String desc) {
		this.code = code;
		this.desc = desc;
	}
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
}
