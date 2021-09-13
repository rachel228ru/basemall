package com.medusa.gruul.common.redis.constant;

/**
 * 同步检测结果
 *
 * @author : wangpeng
 * @version : 1.0
 * @since : 16/6/29 上午11:28
 */
public enum SyncResultCodeEnum {
    /**
     * 1,isOld,老数据
     */
    IS_OLD(1, "isOld", "老数据"),

    /**
     * 2,isNew,新数据
     */
    IS_NEW(2, "isNew", "新数据"),;

    SyncResultCodeEnum(int code, String value, String descr) {
        this.code = code;
        this.value = value;
        this.descr = descr;
    }

    private int code;

    private String value;

    private String descr;

    public String getValue() {
        return value;
    }

    public int getCode() {
        return code;
    }

    public String getDescr() {
        return descr;
    }

    /**
     * 根据code获取枚举对象
     *
     * @param code
     * @return
     */
    public SyncResultCodeEnum getByCode(int code) {

        SyncResultCodeEnum[] syncResultCodes = SyncResultCodeEnum.values();

        for (SyncResultCodeEnum syncResultCode : syncResultCodes) {
            if (syncResultCode.getCode() == code) {
                return syncResultCode;
            }
        }

        return SyncResultCodeEnum.IS_OLD;
    }
}
