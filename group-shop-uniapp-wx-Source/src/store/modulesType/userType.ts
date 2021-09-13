/*
 * @description: 
 * @Author: vikingShip
 * @Date: 2021-08-07 09:56:18
 * @LastEditors: vikingShip
 * @LastEditTime: 2021-08-09 09:34:42
 */
import { UserInfo } from "@/pages/index/modules/me/meType"

/**
 * @LastEditors: vikingShip
 * @description: userStore的state类型
 * @param userInfo 用户信息集合
 * @param goodsNumber 购物车商品数量
 */
export interface UserStoreState {
    userInfo: Partial<PolyUserInfo>
    goodsNumber: number
}


export interface PolyUserInfo extends UserInfo {
    tenantId: string;
    token: string;
    couponType: boolean;
    shopUserId: string;
    transData: any;
    sessionKey:string
}