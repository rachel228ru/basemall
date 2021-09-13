/*
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:06
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-26 09:54:16
 */
import api from "@/libs/request";

/**
 * 获取商品列表
 * @param data
 */
export const getOrders = (data: any) => {
  return api.get("/order-open/manage/search", data);
};

/**
 * 获取商品详情
 * @param orderId 订单id
 */
export const getOrderDetail = (orderId: string | number) => {
  return api.get(`/order-open/manage/info/${orderId}`, null);
};

/**
 * 备注
 */
export const remarks = (data: any) => {
  return api.put(`/order-open/manage/note`, data.orderIds, { params: data });
};

/**
 * 关闭订单
 */
export const close = (ids: string[]) => {
  return api.put("/order-open/manage/close", ids);
};

/**
 * 获取评价列表
 */
export const getEvaluateList = (query: any) => {
  return api.get("/order-open/manage/evaluate/search", query);
};

/**
 * 设置精选
 */
export const setChoice = (ids: string[]) => {
  return api.put("/order-open/manage/choice/evaluate", ids);
};

/**
 * 取消精选
 */
export const cancelChoice = (ids: string[]) => {
  return api.put("/order-open/manage/unChoice/evaluate", ids);
};

/**
 * 回复评价
 */
export const reply = (form: any) => {
  return api.put("/order-open/manage/reply/evaluate", form, { params: form });
};

/**
 * 获取物流信息
 */
export const getdeliveryInfo = (form: any) => {
  return api.get("/order-open/manage/traces", form);
};

/**
 * 获取订单设置
 */
export const getOrderManageSetting = () => {
  return api.get("/order-open/manage/setting", null);
};

/**
 * 设置订单设置
 */
export const setOrderManageSetting = (form: any) => {
  return api.post("/order-open/manage/setting", form, {
    params: form,
    headers: { "Content-Type": "application/json" },
  });
};

/**
 * 获取评价是否开启
 * @param form
 */
export const getManageSetting = () => {
  return api.get("/order-open/manage/evaluate/setting", null);
};

/**
 * 设置评价是否开启
 * @param form
 */
export const setManageSetting = (form: any) => {
  return api.put("/order-open/manage/evaluate/setting", null, { params: form });
};

/**
 * 查询快递订单
 * @param query
 */
export const searchDeliveryOrders = (query: any) => {
  return api.get("/order-open/manage/reply/searchDeliveryOrders", query);
};

/**
 * 查询快递订单v2
 * @param query
 */
export const searchLogistics = (query: any) => {
  return api.get("/order-open/manage/search/logistics", query);
};

/**
 * 打印并发货
 * @param query
 */
export const printDeliver = (query: any) => {
  return api.post("/logistics-open/print/deliver/goods", query);
};

/**
 * 获取是否存在待发货订单
 * @param query
 */
export const getLogisticsWait = () => {
  return api.get("/order-open/manage/logistics/wait/send", null);
};

/**
 * 无需物流发货
 */
export const logisticsSend = (orderIds: string) => {
  return api.put("/order-open/manage/logistics/send", { orderIds });
};

/**
 * 更新订单地址
 * @param form
 */
export const updateAddress = (form: any) => {
  return api.put("/order-open/manage/receiver/address", form);
};
