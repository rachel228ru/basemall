/*
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-25 14:47:49
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-27 09:44:03
 */
import api from "@/libs/request";
import { IAddGoodsPointList } from "@/views/decoration/components/EditorPage/src/interfaces/component";

/**
 * 获取优惠券商品
 */
export const getCouponsGoods = (data: any) => {
  return api.get<{ total: number; list: IAddGoodsPointList[] }>(
    `/goods-open/manager/product/discount/list`,
    data,
  );
};
