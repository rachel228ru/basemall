/*
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:06
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-26 09:46:49
 */
import api from "@/libs/request";

/**
 * 获取用户发票抬头列表
 */
export const getInvoiceList = () => {
  return api.get(`/platform-open/invoice`, {});
};

/**
 * 新增用户发票抬头
 * @param data
 */
export const createInvoice = (data: any) => {
  return api.post(`/platform-open/invoice`, data, {
    headers: {
      "Content-Type": "application/json",
    },
  });
};

/**
 * 更新用户发票抬头
 * @param data
 */
export const updateInvoice = (data: any) => {
  return api.put(`/platform-open/invoice`, data, {
    headers: {
      "Content-Type": "application/json",
    },
  });
};

/**
 * 申请开票接口
 * @param data
 */
export const applyInvoice = (data: any) => {
  return api.post(`/platform-open/sys-shop-invoice-order`, data, {
    headers: {
      "Content-Type": "application/json",
    },
  });
};
