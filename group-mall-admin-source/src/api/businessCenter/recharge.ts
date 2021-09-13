/*
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:06
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-26 09:46:53
 */
import api from "@/libs/request";

/**
 * 查询指定订单是否支付成功,支付成功返回date=true
 */
export const confirmRecharge = (orderId: any) => {
  return api.get(`/platform-open/recharge/${orderId}`, {});
};

/**
 * 商家个人充值记录
 */
export const getRechargeList = (data: any) => {
  return api.get(`/platform-open/recharge/console/order`, data);
};

/**
 * 商户余额充值回调接口
 * @param data
 */
export const createInvoice = (data: any) => {
  return api.post(`/platform-open/recharge/balance/notify/wx`, data, {
    headers: {
      "Content-Type": "application/json",
    },
  });
};

/**
 * 商户余额支付接口
 * @param data
 */
export const rechargeMoney = (data: any) => {
  return api.post(`/platform-open/recharge/balance/pay`, data, {
    headers: {
      "Content-Type": "application/json",
    },
  });
};
