/*
 * @description: 
 * @Author: vikingShip
 * @Date: 2021-08-02 14:20:12
 * @LastEditors: vikingShip
 * @LastEditTime: 2021-08-02 14:47:58
 */
import { LinkSelectItem } from "../cube-box/cubeBoxType"

/**
 * @LastEditors: vikingShip
 * @description: 轮播组件state接口
 * @param type 类型
 * @param swiperList 轮播元素数组
 * @param padding padding
 * @param imageStyle 图片样式 1常规 2投影 
 * @param imageAngle 图片倒角 1直角 2圆角 
 * @param indicator 指示器 1样式一 2样式二 3样式三 
 * @param ImageClass 样式
 * @param curent 当前轮播序列号
 * @param height 图片高度
 */

export interface HomeSwiperState{
    type: string
    swiperList: SwiperListItem[]
    padding: number
    imageStyle: number
    imageAngle: number
    indicator: number
    ImageClass: string
    curent: number
    height: number
}

/**
 * @LastEditors: vikingShip
 * @description: 轮播item类型
 * @param title 轮播组件title
 * @param img 轮播组件背景图
 * @param link 轮播组件链接地址
 */
export interface SwiperListItem {
    title: string
    img: string
    link: LinkSelectItem
}