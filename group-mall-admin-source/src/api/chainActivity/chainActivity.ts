/*
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:06
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-26 09:47:21
 */
import api from "@/libs/request";

/**
 * 获取默认协议
 */
export const robotLogin = (data: any) => {
  return api.post(`/solitaire-open/weChat/boot/login`, data);
};
