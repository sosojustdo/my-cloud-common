package com.cloudyoung.common.model;

import org.apache.logging.log4j.Logger;

/**
 * Description:  针对日志中的 Error 数据进行的存储
 * All Rights Reserved.
 *
 * @version 1.0  2017/9/27 19:09  by  李美根（limg@cloud-young.com）创建
 */
public class LogErrorModel {

    //传入参数
    private String inputParam;

    //错误信息
    private Exception exception;

    //错误消息
    private String message;

    //备注
    private String  remark;

    //平台名称
    private String platform;

    //logger控件
    private Logger logger;


    public String getInputParam() {
        return inputParam;
    }

    public void setInputParam(String inputParam) {
        this.inputParam = inputParam;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public Logger getLogger() {
        return logger;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }
}
