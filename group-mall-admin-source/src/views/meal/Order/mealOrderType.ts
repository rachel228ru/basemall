/*
 * @description:
 * @Author: vikingShip
 * @Date: 2021-08-26 10:59:36
 * @LastEditors: vikingShip
 * @LastEditTime: 2021-08-26 11:29:46
 */

/**
 * @LastEditors: vikingShip
 * @description: 套餐列表
 * @param name 套餐名称
 * @param remark 套餐说明
 * @param level 套餐等级
 * @param packagePrice 套餐价格
 * @param packagePriceUnit 套餐使用价格单位 1天，2月，3年
 * @param discountsJson 优惠价json y-年 m-月 d-天  [{"unit":"y","price":100}]
 * @param functionDesc 功能描述
 * @param openState 套餐开启状态 0-关闭  1-开启
 */
export interface mealList {
  id: number | null;
  name: string;
  remark: string;
  level: number | null;
  packagePrice: number | null;
  packagePriceUnit: number | null;
  discountsJson: string | Array<{unit:string;price:number}>;
  functionDesc: string[] | string;
  openState: number | null;
}
