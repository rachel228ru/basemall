/*
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:06
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-26 09:53:37
 */
import api from "@/libs/request";

/**
 * pc获取租户所有买家消息或商家消息
 * @param type 获取类型1-买家消息 2-商家消息
 */
export const getSubMessage = (type: any) => {
  return api.get(`/platform-open/min-subscriberi-message/pc/${type}`, {});
};

/**
 * pc获取租户所有买家消息
 */
export const getBuyerMessage = () => {
  return api.get(`/platform-open/shop-message/buyer/notify`, {});
};

/**
 * 更新租户指定版本商家消息
 * @param data
 */
export const updateMerchantMessage = (data: any) => {
  return api.get(`/platform-open/min-subscriberi-message/up/shop/msg`, data);
};

/**
 * 更新租户指定版本买家消息
 * @param data
 */
export const updateBuyerMessage = (data: any) => {
  return api.get(
    `/platform-open/min-subscriberi-message/up/subscription/msg`,
    data,
  );
};

/**
 * 更新商家消息使用状态
 * @param id
 * @param data
 */
export const updateMerchantState = (id: any, data: any) => {
  return api.put(`/platform-open/min-subscriberi-message/${id}`, data, {
    headers: {
      "Content-Type": "application/json",
    },
  });
};

/**
 * 修改消息开启状态
 * @param data
 */
export const updateMessageState = (data: any) => {
  return api.put(`/platform-open/shop-message/state`, data, {
    headers: {
      "Content-Type": "application/json",
    },
  });
};
