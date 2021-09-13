/*
 * @description: 
 * @Author: vikingShip
 * @Date: 2021-08-05 09:57:37
 * @LastEditors: vikingShip
 * @LastEditTime: 2021-08-05 10:26:43
 */

export interface ModuleStateType{
    shopInfo:Partial<ShopInfoType>
}
/**
 * @LastEditors: vikingShip
 * @description: shopInfo类型
 * @param businessHours 营业时间,自行分割
 * @param dueTime 到期时间
 * @param isDue 是否到期 0 不是 1是
 * @param level 当前套餐等级
 * @param logoUrl 店铺log
 * @param miniBottomLog 小程序底部打标
 * @param orderSource 订单来源 0-用户购买 1-管理台购买（店铺列表，购买，续费） 2-平台赠送（用户创建店铺时自动赠送） 3-平台创建（为指定商户直接创建指定套餐店铺）4-代理付费
 * @param packageName 套餐名称
 * @param payType 当前支付渠道 1-微信支付 2-环迅支付 3-随行支付
 * @param platformShopId 管理台店铺的shopId,表id
 * @param shopName 店铺名称
 * @param shopPhone 店铺电话
 * @param status 1部署中 2正常 ，3已打烊，4禁用,5不在营业时间 6店铺到期
 * @param teamplateVersion 店铺当前使用模板的版本，或当前最新的版本
 * @param templateCode 店铺当前使用的模板标识
 * @param templateName 店铺当前使用的模板名称
 */
export interface ShopInfoType{
    businessHours:string
    dueTime:string
    isDue:number
    level:number
    logoUrl:string
    miniBottomLog:string
    orderSource:number
    packageName:string
    payType:number
    platformShopId:number
    shopName:string
    shopPhone:string
    status:number
    teamplateVersion:string
    templateCode:string
    templateName:string
}

