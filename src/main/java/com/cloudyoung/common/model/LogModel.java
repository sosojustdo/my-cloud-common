package com.cloudyoung.common.model;

import java.io.Serializable;

/**
 * Description:
 * All Rights Reserved.
 *
 * @version 1.0  2017/9/20 14:15  by  李美根（limg@cloud-young.com）创建
 */
public class LogModel implements Serializable {

    //方法名称
    private String MethodName;

    //传入参数
    private String InputParam;

    //返回结果
    private String result;

    //错误消息
    private String Message;

    //备注
    private String  Remark;

    //调用服务名称
    private String ServiceName;

    //创建时间
    private String CreateTime;

    //级别
    private String Level;

    //平台名称
    private String Platform;

    public LogModel(){

    }

    public String getMethodName() {
        return MethodName;
    }

    public void setMethodName(String methodName) {
        MethodName = methodName;
    }

    public String getInputParam() {
        return InputParam;
    }

    public void setInputParam(String inputParam) {
        InputParam = inputParam;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public String getServiceName() {
        return ServiceName;
    }

    public void setServiceName(String serviceName) {
        ServiceName = serviceName;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String createTime) {
        CreateTime = createTime;
    }

    public String getLevel() {
        return Level;
    }

    public void setLevel(String level) {
        Level = level;
    }

    public String getPlatform() {
        return Platform;
    }

    public void setPlatform(String platform) {
        Platform = platform;
    }
}
