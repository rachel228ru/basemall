/*
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:06
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-26 17:26:35
 */
import api from "@/libs/request";

/* 获取模板数据列表
 * @param data
 * @query: page --> 指定页数 (必须)
 * @query: type --> 分类类型：1 系统模版 2 定制模版 (必须)
 * @query: size --> 数据条数 (必须)
 */
export const getTemplateList = (data: any) => {
  return api.get("/platform-open/shopTemplate/list", data);
};

/**
 * 获取支付配置
 */
export const getPaymentConfig = data => {
  return api.get(`/platform-open/shop/pay/info`, data);
};

export const test = (data: any) => {
  return api.post<{ test: "test" }>("/test", data);
};

/** 上传接口 */
export const upLoad = (data: any) => {
  const formData = new FormData();
  formData.append("file", data.file);
  return api.post(`oss-open/upload`, formData);
};

/**
 * 商城信息
 */
export const getShopMsg = (data: any) => {
  return api.get(`/integral-open/manager/shop/get/shopId`, data);
};
