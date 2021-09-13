/*
 * @description:
 * @Author: vikingShip
 * @Date: 2021-07-17 13:29:51
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-26 09:54:42
 */
import api from "@/libs/request";
/** 获取弹窗图片 */
export const getDialogImg = (data: any) => {
  return api.put(`/recycling-open/recycling-setting/popup/window`, data);
};

/** 获取相关订单 */
export const getRecoveryOrderList = (data: any) => {
  return api.get(`/recycling-open/recycling-order/pc/list`, data);
};
