/*
 * @description: 
 * @Author: vikingShip
 * @Date: 2021-08-03 13:26:25
 * @LastEditors: vikingShip
 * @LastEditTime: 2021-08-09 17:04:22
 */
import {LinkSelectItem} from "../cube-box/cubeBoxType"

/**
 * @LastEditors: vikingShip
 * @description: goodSwiper组件state类型
 * @param swiperList 轮播数组
 * @param padding
 * @param imageStyle 图片样式 1常规 2投影 
 * @param imageAngle 图片倒角 1直角 2圆角 
 * @param indicator 指示器 1样式一 2样式二 3样式三 
 * @param ImageClass 样式
 * @param curent 当前轮播序列号 
 * @param height 图片高度
 * @param radio 轮播类型 
 * @param btnImg 底图 
 * @param interval 轮播时间 
 * @param sidePadding 侧边距  
 */
export interface HomeSwiperFormData {
    type: string;
    swiperList: SwiperListItem[];
    padding: number;
    imageStyle: number;
    imageAngle: number;
    indicator: number;
    ImageClass: string;
    curent: number;
    height: number;
    radio: number;
    btnImg: string;
    interval: number;
    sidePadding: number;
    margin:number
}
/**
 * @LastEditors: vikingShip
 * @description: 轮播item类型
 * @param title 
 * @param img    
 * @param link   链接地址
 */
export interface SwiperListItem {
    title: string;
    img: string;
    link: LinkSelectItem;
}


