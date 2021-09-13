/*
 * @description:
 * @Author: vikingShip
 * @Date: 2021-08-14 15:08:19
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-25 13:48:52
 */

import { GoodDetailInfo } from "../goodType";
import { SearchKeyType } from "./searchType";
import { ApiSpecArea } from "../marketType";

/**
 * @LastEditors: vikingShip
 * @description: GoodList/state
 * @param saleMode 模式
 * @param goodList 商品列表
 * @param hasList 是否有商品
 * @param cateFlag 编辑分类弹窗显隐标识
 * @param loading 商品列表加载中显隐标识
 * @param checkAll 商品是否全选标识
 * @param isIndeterminate 是否有选中的商品标识
 * @param goodIds 选中的商品id
 * @param currentGood 选中的商品id
 * @param total 商品总条数
 * @param searchType 搜索关键词
 * @param dialogVisible 商品码弹窗
 * @param codeImg code码地址
 * @param regionList 专区数组
 * @param currentRegion 当前专区名称
 */
export interface GoodListState {
  saleMode: string;
  goodList: GoodDetailInfo[];
  hasList: boolean;
  cateFlag: boolean;
  loading: boolean;
  checkAll: boolean;
  isIndeterminate: boolean;
  goodIds: number[];
  tableCheckedItem: GoodDetailInfo[];
  currentGood: GoodDetailInfo | null;
  total: number;
  searchType: SearchKeyType;
  dialogVisible: boolean;
  codeImg: string;
  regionList: ApiSpecArea[];
  currentRegion: string;
}

/**
 * @LastEditors: vikingShip
 * @description: 商品列表操作下拉
 * @param text 下拉选择项名称
 * @param command 下拉选择项参数值
 * @param show 展示标识符
 * @param disabled 可选标识符
 */

export interface GoodSelectOperate {
  text: string;
  command: string;
  show: boolean;
  disabled?: boolean;
}
