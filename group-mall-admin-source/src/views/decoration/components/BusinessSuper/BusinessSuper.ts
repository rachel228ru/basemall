/*
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:09
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-27 14:23:08
 */
import { Vue } from "vue-property-decorator";

interface ShowCon {
  /** 商品名称 */
  name: boolean;
  /** 商品实售价 */
  price: boolean;
  /** 商品指导价 */
  guide: boolean;
  /** 商品销售量及库存是否显示 */
  sales: boolean;
  /** 购物按钮是否显示 */
  showCartBtn: boolean;
  /** 购物按钮 1有背景 2无背景 3显示文字 0不显示 */
  cartButton: number;
  /** 商品角标是否显示 */
  showCorner: boolean;
  /** 商品角标 1新品 2热卖 3抢购 */
  corner: number;
}

export interface LinkSelectItem {
  id: number;
  /** 链接类型 0 -功能页面 FunctionPage 1-商超商品 Goods 2 -展示分类 DisplayClassify 3-自定义页面 CustomPage */
  type: number;
  /** 链接名称 */
  name: string;
  /** 链接地址 */
  url: string;
  /** 附加参数 */
  append?: string;
}

export interface SwiperListItem {
  title: string;
  img: string;
  link: LinkSelectItem;
  linkName: string;
}

interface Goods {
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
 * 分类对象
 */
export interface ICategory {
  /** 分类Id */
  id: string;
  /** 分类名称 */
  name: string;
  /** 父级Id */
  parentId: string;
  /** 商品数量 */
  productNumber?: number;
  /** 子集对象 */
  showCategoryVos: ICategoryItemVos[];
}

/**
 * 分类图片
 */
export interface Chart {
  /** 图片 */
  img: string;
  /** 标题 */
  title: string;
  /** 分类 */
  category: ICategoryItemVos;
  sortId: number;
}

export interface Navigation {
  /** 导航名称 */
  name: string;
  /** 选择分类 二级分类Id */
  class: Array<string>;
  /** 导航选择的二级分类 */
  chartList: Array<Chart>;
  /** 导航类型 1默认  2新增 */
  navType: number;
}

export interface ICategoryItem {
  id: string;
  name: string;
  parentId: string;
  saleMode: string;
  showCategoryId: string;
  showCategoryVos: ICategoryItemVos[];
  /** 商品数量 */
  productNumber?: number;
}

export interface ICategoryItemVos {
  id: string;
  name: string;
  parentId: string;
  productNumber: number;
  saleMode: string;
  showCategoryId: string;
  sort: number;
}

/**
 * 一级类目
 */
export class FirstCategory {
  /** 类目对象 */
  category: ICategory = {} as ICategory;

  /** 是否被选中为当前编辑对象 */
  isSelected = false;

  /** 二级分类页图片 */
  classChart: Array<Chart> = [];

  /** 海报列表 */
  swiperList: Array<any> = [];

  /** 导航信息 */
  navigation: Array<Navigation> = [];

  /** 导航样式 1样式一  2样式二 */
  navigationStyle = 1;
}

/**
 * 商超分类页
 */
export default class BusinessSuper {
  /** 选择模式 1顶部模式 2侧边栏模式 */
  selectMode = 1;

  /** 选择样式 1商品列表 2分类列表 */
  selectType = 1;

  /** 二级分类页图片 */
  classChart: Array<Chart> = [];

  /** 展示分类 已选择的商品分类数组 */
  groupList: Array<ICategory> = [];

  /** 所有选择的一级分类列表 */
  firstCatList: Array<FirstCategory> = [];

  /** 当前编辑的一级分类 */
  currentFirstCategory: FirstCategory = new FirstCategory();

  /** 临时存储海报列表 */
  swiperList = [];

  /** 当前编辑的一级分类对象 */
  curruntNav: ICategory = {
    id: "",
    name: "",
    parentId: "",
    productNumber: 0,
    showCategoryVos: [],
  };

  /** 导航信息 */
  navigation: Navigation[] = [];

  /** 列表样式 1大图样式 2列表样式 */
  listStyle = 1;

  /** 页面边距 */
  pagePadding = 12;

  /** 字体选中 */
  fontColor = "#FF0000";

  /** 背景选中 */
  fontBg = "#FFFFFF";

  /** 字体未选中 */
  fontNotColor = "#000000";

  /** 背景未选中 */
  fontNotBg = "#F5F5F5";

  /** 商品间距 */
  goodsPadding = 10;

  /** 商品样式 1无底白边 2卡片投影 3描边白底 */
  goodsStyle = 6;

  /** 图片倒角 1直角 2圆角 */
  doodsAngle = 1;

  /** 文本样式 1常规体 2加粗体 */
  textStyle = 1;

  /** 购买按钮样式 3/4 */
  cartButton = 3;

  goods: Goods;

  /** 商品列表 */
  gList: Goods[] = [];

  /** 是否是搜索状态 1内容页  2搜索页  3分类页 */
  isSearch = 1;

  /** 二级分类页Id */
  secondCategoryId = "";

  /** 二级分类商品搜索页面样式 1矩形  2列表 */
  secondCatGoodsStyle = 1;

  [x: string]: any;

  constructor() {
    this.gList = [
      {
        id: 1,
        name: "商品名称商品名称商品名称商品名称商品名称商品名称",
        img: "https://qiniu-app.qtshe.com/64345435.jpg",
        endTime: "20:15:14",
        price: 99,
        guide: 219,
        soldCount: 10,
        inventory: 120,
        deliveryTime: "06月24日 14:00",
      },
      {
        id: 2,
        name: "商品名称商品名称商品名称商品名称商品名称商品名称",
        img: "https://qiniu-app.qtshe.com/64345435.jpg",
        endTime: "20:15:14",
        price: 99,
        guide: 219,
        soldCount: 10,
        inventory: 120,
        deliveryTime: "06月24日 14:00",
      },
    ];

    this.goods = {
      id: 1,
      name: "商品名称商品名称商品名称商品名称商品名称商品名称",
      img: "https://qiniu-app.qtshe.com/u391.png",
      endTime: "20:15:14",
      price: 99,
      guide: 219,
      soldCount: 10,
      inventory: 120,
      deliveryTime: "06月24日 14:00",
    };
  }
}

export const bus = new Vue();
