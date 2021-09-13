package com.medusa.gruul.payment.api.model.param.ips;


import lombok.Data;

import java.util.List;

/**
 * @author create by zq
 * @date created in 2019/11/10
 */
@Data
public class IpsWxPlatformPay {


    /**
     * 账户号
     */
    private String account;


    /**
     * 平台编码
     */
    private String platCode;


    /**
     * 商户订单号
     * 商户生成的唯一编号，必须在自身账户交 易中唯一，由大小写字母及数字组成
     */
    private String trxId;


    /**
     * 产品类型
     */
    private String productType;


    /**
     * 场景类型
     */
    private String sceneType;


    /**
     * 商户类型
     */
    private String merType;


    /**
     * 子商户号
     */
    private String subMerCode;


    /**
     * 微信开放平台审核通过的APPID 场景类型为APP时必填 场景类型为JSAPI时必填 产品类型为9527时对应卖家支付宝用户 I
     */
    private String appId;


    /**
     * 用户在微信注册后的开放ID 场景类型为JSAPI时必填 产品类型为9527时对应买家支付宝账
     */
    private String openId;


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
     * 商户附加数据  附加数据，在查询API和支付通知中原样 返回，该字段主要用于商户携带订单的自 定义数据
     */
    private String attach;


    /**
     * 异步通知Url 迅付服务器主动通知商户服务器里指定 的页面http/https路径
     */
    private String notifyUrl;


    /**
     * 订单截止时间 ：YYYYMMDDH24MISS
     */
    private String expireDtTm;


    /**
     * 商品描述，支付页面展示
     */
    private String goodsDesc;


    /**
     * 商品详情
     */
    private List<GoodsDetails> goodsDetails;


    /**
     * 扩展信息
     */
    private ExtFields extFields;


    /**
     * 分账信
     */
    private RoyaltyInfo royaltyInfo;


    /**
     * 个人账户信息
     */
    private AccountInfo accountInfo;


    @Data
    public class GoodsDetails {

        /**
         * 商品ID
         */
        private String goodsId;

        /**
         * 商品name
         */
        private String goodsName;

        /**
         * 商品数量
         */
        private String goodsCount;

        /**
         * 产品单价
         */
        private String goodsPrice;

    }

    @Data
    public class ExtFields {

        /**
         * 商户类别
         */
        private String merCategory;

        /**
         * 交易场所 一般为平台网站地址
         */
        private String platUrl;

        /**
         * 场所名称
         */
        private String platName;

        /**
         * 下单终端设备
         */
        private String orderTerminal;

        /**
         * 设备号
         */
        private String gitLat;

        /**
         * 设备类型
         */
        private String deviceType;

        /**
         * 终端IP
         */
        private String clientIp;

        /**
         * 场景信息
         */
        private String sceneinfo;

    }

    @Data
    public class RoyaltyInfo {

        /**
         * 分账冻结标志1:不冻结  2:冻结  默认1；冻结是分账后，
         * 资金冻结在对应账户中,后续商户可以发起 解冻通知解冻
         */
        private String frozenFlag;

        /**
         * 分账类型 1:实时【目前只支持实时
         */
        private String royaltyType;

        /**
         * 金额类型 1:固定金额 2：金额百分比
         */
        private String amtType;

        /**
         * 分账明细 JSON数组，
         * 参考royaltyDetails 定义 最大支持 10 条分账明细(即 10 个分账方
         */
        private RoyaltyDetails[] royaltyDetails;

    }

    @Data
    public class RoyaltyDetails {

        /**
         * 分账流水号 商户分账的关联号，用于关联到每一笔分账信 息，商户需保证其唯一性
         */
        private String royaltyTrxId;

        /**
         * Ips账户
         */
        private String account;

        /**
         * 账户类
         */
        private String accountType;

        /**
         * 分账金额，如金额类型为百分比，
         * 代表分账的 百分比，20代表20%的分账比例
         */
        private String royaltyAmt;

        /**
         * 描述
         */
        private String desc;

    }

    @Data
    public class AccountInfo {

        /**
         * 个人账户号
         */
        private String accountNo;

    }


}
