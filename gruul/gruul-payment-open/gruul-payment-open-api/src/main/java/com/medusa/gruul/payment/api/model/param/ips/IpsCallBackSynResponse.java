package com.medusa.gruul.payment.api.model.param.ips;


import lombok.Data;

import java.util.List;

/**
 * @author create by zq
 * @date created in 2019/11/10
 */
@Data
public class IpsCallBackSynResponse {


    /**
     * 账户号
     */
    private String account;


    /**
     * 商户订单号
     * 商户生成的唯一编号，必须在自身账户交 易中唯一，由大小写字母及数字组成
     */
    private String trxId;


    /**
     * 订单日期YYYYMMDD
     */
    private String trxDtTm;


    /**
     * 币种
     */
    private String trxCcyCd;


    /**
     * 订单金额 单位元，保留2位小数
     */
    private String trxAmt;


    /**
     * 支付额
     */
    private String payAmt;


    /**
     * payFee
     */
    private String payFee;


    /**
     * feeWho
     */
    private String feeWho;


    /**
     * payType
     */
    private String payType;


    /**
     * cardType
     */
    private String cardType;


    /**
     * trxStatus
     */
    private String trxStatus;


    /**
     * trxResultMsg
     */
    private String trxResultMsg;


    /**
     * attach
     */
    private String attach;


    /**
     * ipsTrxId
     */
    private String ipsTrxId;


    /**
     * bkTrxId
     */
    private String bkTrxId;


    /**
     * ipsTrxDtTm
     */
    private String ipsTrxDtTm;


    /**
     * thdTrxId
     */
    private String thdTrxId;


    /**
     * extInfo
     */
    private String extInfo;

}
