package com.cloudyoung.common.enums;

import org.apache.commons.lang.StringUtils;

/**
 * Description: 平台名称枚举
 * All Rights Reserved.
 *
 * @version 1.0  2017/10/9 10:57  by  李美根（limg@cloud-young.com）创建
 */
public enum PlatformNameEnum {

    AUTHORITY("AUTHORITY"), //权限管理中心
    PAY("PAY"), //支付中心
    EC("EC"),  //车型库
    WX("WX"), //微信营销
    DMS("DMS"),// dms 经销商管理系统
    MESSAGE_SMS("MESSAGE_SMS"),//消息中心－短信
    GIFT("GIFT"),//礼品中心
    DEALER("DEALER"),//经销商中心
    WORM_HOLE("WORM_HOLE"), //虫洞
    EXHIBITION("EXHIBITION"), //掌上展厅
    CLOUD_STORE("CLOUD_STORE"); //云店

    private String platformName;


    private PlatformNameEnum(String platformName) {
        this.platformName = platformName;
    }

    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }


    public static PlatformNameEnum getPlatformNameEnumByName(String platformName) {
        if (StringUtils.isNotBlank(platformName)) {
            for (PlatformNameEnum item : PlatformNameEnum.values()) {
                if (item.getPlatformName().equals(platformName.toUpperCase())) {
                    return item;
                }
            }
        }
        return null;
    }
}
