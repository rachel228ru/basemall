/*
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:10
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-25 11:08:33
 */
import BusinessSuper from "../../../BusinessSuper/BusinessSuper";
import BlankPaceholder from "../compontents/BlankPaceholder/BlankPaceholder";
import CubeBox from "../compontents/CubeBox/CubeBox";
import Goods from "../compontents/Goods/Goods";
import GoodSwiper from "../compontents/GoodSwiper/GoodSwiper";
import HomeSwiper from "../compontents/HomeSwiper/HomeSwiper";
import ImageCom from "../compontents/ImageCom/ImageCom";
import NavBar from "../compontents/NavBar/NavBar";
import RichText from "../compontents/RichText/RichText";
import Search from "../compontents/Search/Search";
import Separator from "../compontents/Separator/Separator";
import StoreNavigation from "../compontents/StoreNavigation/StoreNavigation";
import TitleBar from "../compontents/TitleBar/TitleBar";
import VideoCom from "../compontents/VideoCom/VideoCom";

/*
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:10
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-23 14:53:46
 */
export interface IGoods {
  goods: any[];
  type: "big" | "swiper" | "line" | "detail";
  style: "white" | "card" | "border";
}

export interface IComponentItem {
  value: string;
  label: string;
  icon: string;
  type?: boolean;
  showType?: boolean;
}

export interface IComponent extends IComponentItem {
  id: number;
  formData:
    | HomeSwiper
    | Goods
    | Search
    | TitleBar
    | BlankPaceholder
    | Separator
    | StoreNavigation
    | NavBar
    | CubeBox
    | RichText
    | BusinessSuper
    | ImageCom
    | VideoCom
    | GoodSwiper;
}

export interface IComponentStr {
  properties: string;
  pageId: string;
  templateId: string;
  modelId: string | null;
}

/* AddGoods组件 */

export type IAddGoodsList = {
  albumPics: string;
  id: string;
  limitType?: number;
  maxPrice: number | string;
  minPrice: number | string;
  name: string;
  img?: string;
  pic: string;
  price?: string | number;
  productSn?: string;
  saleMode: string;
  isCheck?: boolean;
};

export interface IAddGoodsPointList {
  albumPics: string;
  id: string;
  limitType?: number;
  maxPrice: number | string;
  minPrice: number | string;
  name: string;
  pic: string;
  productSn?: string;
  saleMode: string;
  isCheck?: boolean;
}

export interface IAddGoodsCategoryList {
  id: string;
  name: string;
  parentId: string;
  saleMode: string;
  showCategoryId: string;
  showCategoryVos: IAddGoodsCategoryVosList[];
  sort: number;
}

export interface IAddGoodsCategoryVosList {
  id: string;
  name: string;
  parentId: string;
  productNumber: number;
  saleMode: string;
  showCategoryId: string;
  sort: number;
}

export interface IAddGoodsModeList {
  id?: string | number;
  modeName: string;
  modeType: string;
  productNumber?: null | number;
  link?: string;
}
