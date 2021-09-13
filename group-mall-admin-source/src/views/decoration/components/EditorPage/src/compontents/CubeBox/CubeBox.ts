/*
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:10
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-25 09:37:02
 */
import LinkSelectItem from "@/components/LinkSelect/src/components/LinkSelectItem";

export interface CubeBoxFormData {
  borderWidth: number;
  layoutWidth: number;
  layoutHeight: number;
  showMethod: number;
  pageMargin: number;
  width: number;
  subEntry: IBanners[];
}

export interface ITemplatCube extends CubeBoxFormData {
  img: string;
  title: string;
}

export interface IBanners {
  x: number;
  y: number;
  width: number;
  height: number;
  img: string;
  link: LinkSelectItem;
  linkName: string;
}

export interface ICubeListWrap {
  top: string;
  left: string;
  width: string;
  height: string;
  paddingTop: string;
  img: string;
  borderWidth: string;
}

export interface IShowCube {
  coordinates: string;
  finish: number;
  selected: boolean;
  start: number;
  style: {
    height: string;
    width: string;
  };
}

export type IShowCubeListItem = IShowCube[];

export type IShowCubeListWrap = Record<
  "height" | "img" | "left" | "paddingTop" | "text" | "top" | "width",
  string
>;

export default class BannerFormData {
  borderWidth = 0; // 图片间隙

  layoutWidth = 2; // 列数

  layoutHeight = 1; //行数

  showMethod = 0; //行数index

  pageMargin = 0; //页面边距离

  width = 2; //展示个数

  subEntry: IBanners[] = [
    {
      x: 0,
      y: 0,
      width: 1,
      height: 1,
      img: "",
      link: {
        id: "",
        type: 0,
        name: "",
        url: "",
        append: "",
      },
      linkName: "",
    },
    {
      x: 1,
      y: 0,
      width: 1,
      height: 1,
      img: "",
      link: {
        id: "",
        type: 0,
        name: "",
        url: "",
        append: "",
      },
      linkName: "",
    },
  ]; //图片数组
}
