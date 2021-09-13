package com.medusa.gruul.platform.conf;

/**
 * 数据库系统配置key值
 *
 * @author whh
 * @description
 * @data: 2020/9/28
 */
public interface SystemConstant {

    /**
     * 阿里云 param_key
     */
    String STORAGE_ALIYUN = "storage_aliyun";
    /**
     * 阿里云 param_key
     */
    String STORAGE_QINIOUYUN = "storage_qiniouyun";
    /**
     * 阿里云 param_key
     */
    String STORAGE_TENCENT_CLOUD = "storage_tencent_cloud";
    /**
     * 系统配置 param_key
     */
    String SERVER_CONFIG = "server_config";
    /**
     * 收款配置 param_key
     */
    String PLATFROM_PAY_CONFIG = "platfrom_pay_config";
    /**
     * 当前使用的oos配置类型 param_key
     */
    String CURRENT_OSS_TYPE = "currentOssType";

    /**
     * 客服消息通知相关配置
     */
    String KF_MSG_NOTIFY = "kfMsgNotify";

    /**
     * 客服消息通知相关配置
     */
    String CODE_EMAIL_CONF = "CodeEmailConf";
}
