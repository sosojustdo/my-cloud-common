package com.cloudyoung.common.enums;

import org.apache.commons.lang.StringUtils;

/**
 * Description: redis 运行模式 
 * All Rights Reserved.
 * @version 1.0  2017年8月23日 下午12:09:36  by 代鹏（daipeng.456@gmail.com）创建
 */
public enum RedisArchModelEnum {
	
	SINGLE("single", "SINGLE"),
	MASTERSLAVE("ms", "MASTER-SLAVE"),
	SENTINEL("sentinel", "SENTINEL"),
	CLUSTER("cluster", "CLUSTER");
	
	private String model;
	private String desc;
	
	private RedisArchModelEnum(String model, String desc) {
		this.model = model;
		this.desc = desc;
	}
	
	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	public static RedisArchModelEnum getRedisArchModelByModel(String model){
		if(StringUtils.isNotBlank(model)){
			for(RedisArchModelEnum item:RedisArchModelEnum.values()){
				if(item.getModel().equals(model)){
					return item;
				}
			}
		}
		return null;
	}

}
