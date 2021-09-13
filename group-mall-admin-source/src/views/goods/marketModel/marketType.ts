/*
 * @description:
 * @Author: vikingShip
 * @Date: 2021-08-14 14:32:37
 * @LastEditors: vikingShip
 * @LastEditTime: 2021-08-26 09:34:15
 */
import { GoodDetailInfo } from "./goodType";
import { SearchKeyType } from "./components/searchType";
/**
 * @LastEditors: vikingShip
 * @description: goods数组state
 * @param list 分类专区数组
 * @param activeName 选中专区名称
 * @param chooseId 选中专区id
 */
export interface GoodState {
  list: ApiSpecArea[];
  activeName: string;
  chooseId: number | string | null;
  fromSaleMode: string;
}
/**
 * @LastEditors: vikingShip
 * @description: api专区
 * @param id 专区id
 * @param modeName 专区名称
 * @param modeType 专区类型
 * @param productNumber 分区商品数量
 * @param sort 排序id
 */
export interface ApiSpecArea {
  id: number | string |null;
  modeName: string;
  modeType: string;
  productNumber: number;
  sort: number;
  text?: string;
  show?: boolean;
  disabled?:boolean;
  command?: string | number | null;
  isCustom?: boolean;
  link?: string;
}
/**
 * @LastEditors: vikingShip
 * @description: listApartState数组state
 * @param goodIds 选择的数组id
 * @param showGetList 用于批量展示分类获取已选择的分类
 * @param searchType 顶部搜索条件
 * @param pageSize 分页条数
 * @param pageNum 分页页码
 * @param total 数据长度
 */
export interface ListApartState {
  goodIds: string[];
  showGetList: GoodDetailInfo[];
  searchType: SearchKeyType;
  pageSize: number;
  pageNum: number;
  total: number;
}
