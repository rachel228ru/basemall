/*
 * @description:
 * @Author: vikingShip
 * @Date: 2021-08-16 09:20:25
 * @LastEditors: vikingShip
 * @LastEditTime: 2021-08-24 13:28:33
 */
import { ApiSkuType } from "@/views/goods/marketModel/goodType";
/**
 * @LastEditors: vikingShip
 * @description: goodPrice组件state
 * @param inputVisible 显示输入框
 * @param popoverVisible 显示popover
 * @param minPrice 商品最小价格
 * @param maxPrice 商品最大价格
 * @param editPrice 编辑的价格
 * @param skuStocksEdit 编辑的规格价格
 */
export interface GoodPriceState {
  inputVisible: boolean;
  popoverVisible: boolean;
  minPrice: number;
  maxPrice: number;
  editPrice: number;
  skuStocksEdit: PickApiSkuType[];
}

export type PickApiSkuType = Pick<
  ApiSkuType,
  "id" | "specs" | "price" | "originalPrice"
> & { [x: string]: any };
