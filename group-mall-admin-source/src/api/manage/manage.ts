/*
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:06
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-09-02 14:37:06
 */
import api from "@/libs/request";

/**
 * 数据概况订单数据
 */
export const getManageOrder = () => {
  return api.get("/order-open/manage/overview", null);
};
