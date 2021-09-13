/*
 * @description: 
 * @Author: vikingShip
 * @Date: 2021-08-03 15:43:57
 * @LastEditors: vikingShip
 * @LastEditTime: 2021-08-04 11:02:12
 */

/**
 * @LastEditors: vikingShip
 * @description: goods组件state
 * @param good 购物车商品
 * @param intPrice 购物车整数价格
 * @param smaPrice 购物车小数价格
 */

import {GoodInfo} from "../../shopCarType"
 export interface GoodState {
    good: GoodInfo
    intPrice: string
    smaPrice: string
}

