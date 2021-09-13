/*
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:06
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-26 09:49:25
 */
import api from "@/libs/request";

/**
 * 获取播报设置
 */
export const getOrderPrompt = () => {
  return api.get("/order-open/manage/reply/orderPrompt", {
    shopId: "100001100001",
  });
};

/**
 * 设置订单已播报
 */
export const setOrderPrompt = (id: any) => {
  return api.get("/order-open/manage/reply/setOrderPrompt", {
    id,
  });
};

/*
 * 获取省市区
 */
export const getArea = (data: any) => {
  return api.get("/account-open/mini-account-address/area", data);
};

/**
 * 获取分享设置
 */
export const getShareSetting = () => {
  return api.get("/order-open/manage/share/setting", null);
};

/**
 * 设置分享设置
 */
export const setShareSetting = (data: any) => {
  return api.put("/order-open/manage/share/setting", data);
};

/**
 * 获取引导页设置
 */
export const getGuide = () => {
  return api.get("/shops-open/shop/guide/page/get", null);
};

export const getGuideSetting = () => {
  return api.get("/shops-open/shop/guide/switch/page/get", null);
};

export const setGuideSetting = (status: any) => {
  return api.get("/shops-open/shop/guide/switch/page/update", { status });
};

export const getDefGuideSetting = () => {
  return api.get("/shops-open/shop/guide/page/getDefault", null);
};

export const setGuide = (data: any) => {
  return api.post("shops-open/shop/guide/page/guidePage/update", data, {
    headers: {
      "Content-Type": "application/json",
    },
  });
};
