/*
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:06
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-26 09:48:42
 */
import api from "@/libs/request";

/**
 * 批量备注
 */
export const bulkNote = (ids: any, note: any, over: any) => {
  return api.put("/finance-open/manage/withdrawal/note", ids, {
    params: { note, over },
  });
};

/**
 * 获取订单设置
 * @param data
 */
export const getOrderSetting = () => {
  return api.get("/finance-open/manage/setting/order", null);
};

/**
 * 设置订单设置
 * @param data
 */
export const setOrderSetting = (data: any) => {
  return api.post("/finance-open/manage/setting/order", data);
};

/**
 * 快递设置
 */
export const logisticsSetting = (data: any) => {
  return api.post("/finance-open/manage/setting/kd", data);
};

/**
 * 查询快递设置
 */
export const getLogisticsSetting = (data: any) => {
  return api.get("/finance-open/manage/setting/kd", data);
};
