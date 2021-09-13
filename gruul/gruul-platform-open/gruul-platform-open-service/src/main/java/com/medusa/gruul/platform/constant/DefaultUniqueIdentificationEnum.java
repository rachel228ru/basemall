package com.medusa.gruul.platform.constant;

import lombok.Getter;

/**
 * @author whh
 * @description 默认值
 * @data: 2020/12/21
 */
@Getter
public enum DefaultUniqueIdentificationEnum {
    /**
     * 买家订阅消息默认值
     * 创建店铺时发送
     */
    PLATFROM_SHOP_MSG,
    /**
     * 会员卡生成
     * 创建店铺时发送
     */
    MEMBER_CART,
    /**
     * 商品默认值
     * 创建店铺时发送
     */
    GOODS_DEFUALT,
    /**
     * 商家消息默认值
     * 创建店铺时发送
     */
    MINI_SHOP_MSG,
    /**
     * 套餐-商超默认值
     * 创建模板版本时发送
     */
    MALL,
    /**
     * 套餐-社区拼团默认值
     * 创建模板版本时发送
     */
    SQPT,
    /**
     * 店铺引导页默认值
     * 创建店铺时发送
     */
    SHOP_GUIDE_PAGE_DEFUALT,

    /**
     * 用户中心默认值
     * 创建店铺时发送
     */
    ACCOUNT_CENTRE_DEFAULT,

    /**
     * 装修模板的默认数据
     * 创建店铺时发送
     */
    SHOP_TEMPLATE_DEFAULT,
    /**
     * 订单相关默认值
     * 创建店铺时发送
     */
    ORDER_CENTRE_DEFAULT,
    /**
     * 财务中心默认值
     * 创建店铺时发送
     */
    FINANCE_CENTRE_DEFAULT,
    /**
     * 默认用户生成通知
     * 创建店铺时发送
     */
    ACCOUNT_DEFAULT,
    ;


}
