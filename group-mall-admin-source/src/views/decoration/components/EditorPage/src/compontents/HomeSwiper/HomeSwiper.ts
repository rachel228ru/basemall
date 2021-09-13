/*
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:10
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-23 15:09:27
 */
import LinkSelectItem from "@/components/LinkSelect/src/components/LinkSelectItem";

export interface SwiperListItem {
  title: string;
  img: string;
  link: LinkSelectItem;
  linkName?: string;
}

/**
 * 轮播图
 */
export default class HomeSwiperFormData {
  type = "HomeSwiper";

  swiperList: SwiperListItem[] = [];

  padding = 14;

  /** 图片样式 1常规 2投影 */
  imageStyle = 1;

  /** 图片倒角 1直角 2圆角 */
  imageAngle = 1;

  /** 指示器 1样式一 2样式二 3样式三 */
  indicator = 1;

  /** 图片高度 */
  height = 185;
}
