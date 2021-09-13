/*
 * @description: 
 * @Author: vikingShip
 * @Date: 2021-08-03 16:23:26
 * @LastEditors: vikingShip
 * @LastEditTime: 2021-08-07 12:49:32
 */
import {GoodInfo} from "@/pages/index/modules/shopCar/shopCarType"
export interface StepperState{
    num: number
    stockNum: number
    perLimit: number
    value: string 
    canClick: boolean 
}

export interface StepperGood extends GoodInfo{
    maxNum:number
    goodsNumber:number
}