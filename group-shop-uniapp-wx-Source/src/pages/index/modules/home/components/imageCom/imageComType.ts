/*
 * @description: 
 * @Author: vikingShip
 * @Date: 2021-08-02 15:56:17
 * @LastEditors: vikingShip
 * @LastEditTime: 2021-08-02 16:09:57
 */
import {LinkSelectItem} from "../cube-box/cubeBoxType"

/**
 * @LastEditors: vikingShip
 * @description: 组件描述
 * @param boxHeight 外层高度
 * @param img 图片地址
 * @param width 图片宽度
 * @param height 图片高度
 * @param newLeft 左边距
 * @param top 
 * @param left 
 * @param link  链接信息
 */
export interface ImageInfo{
    boxHeight:number
    img:string
    width:string
    height:string
    newLeft:string
    top:number
    link:LinkSelectItem
    left:number
    boxWidth?:string
    ImageCom?:string
}
