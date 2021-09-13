/*
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:10
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-25 11:06:11
 */
interface IShowContent {
  /** 商品名称 */
  nameShow: boolean;
  /** 商品价格 */
  priceShow: boolean;
  /** 购物按钮 */
  buttonShow: boolean;
  /** 购物按钮样式 */
  buttonStyle: number;
  /** 购物车按钮文案 */
  buttonText: string;
  /** 商品角标 */
  tagShow: boolean;
  /** 商品角标样式 */
  tagStyle: number;
}

interface GoodsItem {
  id: number;
  /** 商品名称 */
  name: string;
  /** 商品图片 */
  img: string;
  /** 结束时间 */
  endTime: string;
  /** 商品实售价 */
  price: number;
  /** 商品指导价 */
  guide: number;
  /** 已售量 */
  soldCount: number;
  /** 库存 */
  inventory: number;
  /** 提货时间 */
  deliveryTime: string;
}

/**
 * 商品
 */
export default class Goods {
  /** 1展示分类  2不展示分类 */
  ponentType = 1;

  /** 商品 */
  goods: ISubFormGoods[] = [];

  /** 列表样式 */
  listStyle = "goods-style--one";

  /** 页面边距 */
  pagePadding = 17;

  /** 类目样式 1新  2下划线 */
  titleStyle = 1;

  /** 商品间距 */
  goodPadding = 12;

  /** 商品样式 */
  goodsStyle = "is-none";

  /** 图片倒角 */
  angle = "is-straight";

  /** 文本样式 */
  textStyle = "is-normal";

  /** 一级类目 */
  firstCatList: ICategoryItem[] = [];

  /** 当前分类ID */
  currentCategoryId = "";

  /** 显示内容 */
  showContent: IShowContent = {
    nameShow: true,
    priceShow: true,
    buttonShow: true,
    buttonStyle: 1,
    buttonText: "",
    tagShow: true,
    tagStyle: 1,
  };

  /** 商品数量 */
  goodsCount = 1;

  goodsTemp: GoodsItem = {
    id: 1,
    name: "商品名称",
    img: "https://qiniu-app.qtshe.com/u391.png",
    endTime: "20:15:14",
    price: 99,
    guide: 219,
    soldCount: 10,
    inventory: 120,
    deliveryTime: "06月24日 14:00",
  };
}

export interface ICategoryItem {
  id: string | number;
  name: string;
  productNumber: number | string;
  saleMode: string;
  showCategoryVos: ICategoryItemVos[];
}

export type ICategoryItemVos = Record<"id" | "name" | "productNumber", string>;

export interface ICategoryMode {
  id: string;
  modeName: string;
  modeType: string;
  productNumber: string | number;
  sort: number;
}

export interface ISubFormGoods {
  albumPics: string;
  id: string;
  img?: string;
  pic?: string;
  name: string;
  price?: string | number;
  saleMode: string;
  minPrice?: string;
  maxPrice?: string;
}
