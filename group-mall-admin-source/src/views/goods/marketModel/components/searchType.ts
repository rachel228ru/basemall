/*
 * @description:
 * @Author: vikingShip
 * @Date: 2021-08-14 14:37:18
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-25 13:52:20
 */

/**
 * @LastEditors: vikingShip
 * @description: search组件state
 * @param showCateList 展示分类选择
 * @param goodsType 商品状态
 * @param searchType 搜索参数
 * @param saleMode 模式
 * @param createTime 创建时间
 */
export interface SearchState {
  showCateList: string[];
  goodsType: ComType[];
  searchType: SearchKeyType;
  saleMode: string;
  createTime: string;
}

type ComType = Record<"value" | "label", string>;

/**
 * @LastEditors: vikingShip
 * @description: 搜索关键词
 * @param name 商品名称
 * @param showCategoryId 商品展示分类
 * @param status 商品上下架状态
 * @param dominoState 商品是否参与接龙
 * @param createBeginTime 创建开始时间
 * @param createEndTime 创建结束时间
 */
export interface SearchKeyType {
  name?: string;
  showCategoryId?: string;
  status?: string;
  dominoState?: string;
  createBeginTime?: string;
  createEndTime?: string;
  size?: number;
  current?: number;
  saleMode?: string;
}
