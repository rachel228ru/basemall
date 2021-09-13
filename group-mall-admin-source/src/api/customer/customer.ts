/*
 * @description:
 * @Author: vikingShip
 * @Date: 2021-08-21 09:28:28
 * @LastEditors: vikingShip
 * @LastEditTime: 2021-08-26 14:30:29
 */
import api from "@/libs/request";

/**
 * 获取用户列表
 * @param data
 */
export const getCustomerList = (data: any) => {
  return api.get(`/account-open/mini-account/list`, data);
};

/**
 * 批量黑名单
 * @param data
 */
export const batchSetBlackList = (data: any) => {
  return api.put(`/account-open/mini-account/set/blacklist`, data);
};

/**
 * 获取黑名单用户列表
 * @param data
 */
export const getBlackList = (data: any) => {
  return api.get(`/account-open/mini-account/blacklist`, data);
};

/**
 * 查询所有标签
 */
export const getAllTags = () => {
  return api.get(`/account-open/mini-account-tag`, {});
};

/**
 * 设置用户标签(添加|删除|设置)
 * @param data
 */
export const editTags = (data: any) => {
  return api.post(`/account-open/mini-account-tag`, data, {
    headers: {
      "Content-Type": "application/json",
    },
  });
};
