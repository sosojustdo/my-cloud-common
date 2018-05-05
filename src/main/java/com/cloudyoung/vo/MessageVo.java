package com.cloudyoung.vo;

import java.io.Serializable;
import java.util.Date;

import com.cloudyoung.common.utils.DateUtil;

/**
 * 
 * Description: 返回结果VO
 * Copyright (c) Department of Research and Development/Beijing
 * All Rights Reserved.
 * @param <T>
 * @version 1.0  2017年8月31日 下午8:18:39  by 代鹏（daipeng@cloud-young.com）创建
 */
public class MessageVo<T> implements Serializable {
	
	private static final long serialVersionUID = 211423191531659616L;

	private Boolean result = false ;
	
	private String errorCode ;
	
	private String errorMessage ;
	
	private T data ;
	
	private String systemDate = DateUtil.formatDateTime(new Date(), DateUtil.FORMAT_ONE);
	
	private Long systemTimeMillis= System.currentTimeMillis() ;
	
	public MessageVo() {
            super();
        }

    /**
	 * 错误信息-返回结果(false)-MessageVo的构造方法
	 * @param result 返回结果
	 * @param errorCode 错误Code
	 * @param errorMessage 错误信息
	 */
	public MessageVo(Boolean result , String errorCode , String errorMessage){
		this.result = result ;
		this.errorCode = errorCode ;
		this.errorMessage = errorMessage ;
	}
	
	/**
	 * 真确信息-返回结果(true)-MessageVo的构造方法
	 * @param result
	 * @param data
	 */
	public MessageVo(Boolean result , T data){
		this.result = result ;
		this.data = data ;
	}
	
	public Boolean getResult() {
		return result;
	}

	public void setResult(Boolean result) {
		this.result = result;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}


	public String getSystemDate() {
		return systemDate;
	}

	public void setSystemDate(String systemDate) {
		this.systemDate = systemDate;
	}

	public Long getSystemTimeMillis() {
		return systemTimeMillis;
	}

	public void setSystemTimeMillis(Long systemTimeMillis) {
		this.systemTimeMillis = systemTimeMillis;
	}
	
}
