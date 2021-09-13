/*
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-26 15:10:37
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-26 16:23:06
 */
import api from "@/libs/request";

/**
 * 控制台拥有的店铺
 * @param data
 */
export const getShopList = (data: any) => {
  return api.get("/platform-open/shop/console/list", data);
};

/**
 * 控制台进入指定店铺后台
 * @param shopId
 */
export const enterShop = (shopId: number) => {
  return api.get(`/platform-open/shop/console/join/shop/${shopId}`, {});
};

/**
 * 控制台营业或打烊,取反只需要传入店铺id,只有营业中或已打烊调用才有效果
 * @param shopId
 */
export const closeOrOpenShop = (shopId: number) => {
  return api.put(`/platform-open/shop/console/close-open/${shopId}`, {});
};

/**
 * 控制台创建店铺
 * @param data
 * @query: Dto -->  (必须)
 */
export const createShop = (data: any) => {
  return api.post("/platform-open/shop/console/create", data, {
    headers: { "Content-Type": "application/json" },
  });
};

/**
 * 控制台删除店铺
 * @query: Dto -->  (必须)
 * @param id
 */
export const deleteShop = (id: number) => {
  return api.delete(`/platform-open/shop/console/del/${id}`, {});
};
/**
 * 控制台店铺设置(修改店铺信息)
 * @param shopId
 * @param data
 */
export const updateShop = (shopId: number, data: any) => {
  return api.put(`/platform-open/shop/console/update/${shopId}`, data, {
    headers: { "Content-Type": "application/json" },
  });
};
