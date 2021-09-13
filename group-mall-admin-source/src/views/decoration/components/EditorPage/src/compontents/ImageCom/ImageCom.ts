/*
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:10
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-09-01 15:25:22
 */
import LinkSelectItem from "@/components/LinkSelect/src/components/LinkSelectItem";

export default class ImageCom {
  ImageCom = "图片";

  img = "";

  width = "";

  height = "";

  boxHeight = 240;

  boxWidth = 100;

  top = 0;

  left = 0;

  link: LinkSelectItem = {} as LinkSelectItem;
}

export interface imageItem {
  title: string;
  img: string;
  link: LinkSelectItem;
  linkName: string;
}
