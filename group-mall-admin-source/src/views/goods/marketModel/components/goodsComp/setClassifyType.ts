/*
 * @description: 
 * @Author: vikingShip
 * @Date: 2021-08-16 09:55:29
 * @LastEditors: vikingShip
 * @LastEditTime: 2021-08-16 10:04:04
 */

import {ApiSpecArea} from '@/views/goods/marketModel/marketType'
import {GoodSelectOperate} from '@/views/goods/marketModel/components/goodListType'
/**
 * @LastEditors: vikingShip
 * @description: setClassify组件state
 * @param idList 批量操作选中id
 * @param regionList 专区数组
 * @param popVisible 弹窗控制标识
 * @param dropdownList 下拉菜单列表
 * @param dropdownItemList 商品下拉菜单
 */
export interface SetClassifyState{
    idList:number[]
    regionList:ApiSpecArea[]
    popVisible:boolean
    dropdownList:GoodSelectOperate[]
    dropdownItemList:GoodSelectOperate[]
}