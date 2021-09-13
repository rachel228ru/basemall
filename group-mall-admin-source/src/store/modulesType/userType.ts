/*
 * @description:
 * @Author: vikingShip
 * @Date: 2021-08-13 17:32:31
 * @LastEditors: vikingShip
 * @LastEditTime: 2021-08-26 11:25:38
 */

/**
 * @LastEditors: vikingShip
 * @description: userStore/store
 * @param userInfo 用户信息
 * @param token
 * @param shopInfo 商户信息
 * @param iphone 手机号
 */

export interface UserState {
  userInfo: ApiLoginUserType;
  shopInfo:any
  token: string;
  iphone: string;
}
/**
 * @LastEditors: vikingShip
 * @description: 用户信息
 * @param id 用户id
 * @param nikeName 用户名称
 * @param avatarUrl 头像url
 * @param phone 手机号码
 * @param token
 * @param invitationCode 邀请码
 * @param miniRoles 账号拥有的角色
 * @param miniMenus 拥有的菜单
 * @param backUrl 当前模板版本跳转地址
 */
export interface ApiLoginUserType {
  id: number;
  nikeName: string;
  avatarUrl: string;
  phone: string;
  token: string;
  invitationCode: string;
  miniRoles: MiniRoleType[];
  miniMenus: MiniMenuListType[];
  backUrl: string;
}
/**
 * @LastEditors: vikingShip
 * @description: 账号拥有的角色
 * @param roleName  角色名称
 * @param roleId  角色id
 * @param roleCode  角色编号
 */
interface MiniRoleType {
  roleName: string;
  roleId: number;
  roleCode: string;
}

/**
 * @LastEditors: vikingShip
 * @description: 菜单列表
 * @param title 标题
 * @param path 路径
 * @param name
 * @param menuId 菜单id
 * @param pId 父级id
 * @param icon 菜单图标
 */
interface MiniMenuListType {
  title: string;
  path: string;
  name: string;
  menuId: number;
  pId: number;
  icon: string;
  subMenu: MiniMenuListType[];
}

/**
 * @LastEditors: vikingShip
 * @description: 店铺信息
 * @param platformShopId 管理台店铺的shopId,表id
 * @param shopName 店铺名称
 * @param logoUrl 店铺log
 * @param businessHours 营业时间,自行分割
 * @param miniBottomLog 小程序底部打标
 * @param shopPhone 店铺电话
 * @param status 1部署中 2正常 ，3已打烊，4禁用,5不在营业时间 6店铺到期
 * @param templateName 店铺当前使用的模板名称
 * @param templateCode 店铺当前使用的模板标识
 * @param dueTime 到期时间
 * @param level 当前套餐等级
 * @param packageName 套餐名称
 * @param isDue 是否到期 0 不是 1是
 * @param teamplateVersion 店铺当前使用模板的版本，或当前最新的版本
 * @param payType 当前支付渠道 1-微信支付 2-环迅支付 3-随行支付
 * @param orderSource 订单来源   0-用户购买 1-管理台购买（店铺列表，购买，续费） 2-平台赠送（用户创建店铺时自动赠送） 3-平台创建（为指定商户直接创建指定套餐店铺）4-代理付费
 */
export interface ShopInfoType{
    platformShopId:number
    shopName:string
    logoUrl:string
    businessHours:string
     miniBottomLog:string
     shopPhone:string
     status:number
     templateName:string
     templateCode:string
     dueTime:string
     level:number
     packageName:string
     isDue:number
     teamplateVersion:string
     payType:number
     orderSource:number
}
