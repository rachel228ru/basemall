/*
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:06
 * @LastEditors: vikingShip
 * @LastEditTime: 2021-08-26 14:30:58
 */
import api from "@/libs/request";
import { mealList } from "@/views/meal/Order/mealOrderType";

/**
 * 获取店铺当前可购买套餐列表
 */
export const getMealList = () => {
  return api.get<mealList[]>(`/platform/shop/package/info`, {});
};

/**
 * 用户套餐操作(续费|购买|升级)
 * @param data
 */
export const orderMeal = (data: any) => {
  return api.post(`/platform-open/sys-shop-package-order`, data, {
    headers: {
      "Content-Type": "application/json",
    },
  });
};

/**
 * 更新指定模版版本套餐
 * @param data
 */
export const updateInvoice = (data: any) => {
  return api.put(`/platform-open/sys-shop-package`, data, {
    headers: {
      "Content-Type": "application/json",
    },
  });
};

/**
 * 查询指定套餐模板
 */
export const getMeal = (packageId: any) => {
  return api.get(`/platform-open/sys-shop-package/${packageId}`, {});
};

/**
 * 用户套餐操作(续费|购买|升级)
 * @param data
 */
export const buyMeal = (data: any) => {
  return api.post(
    `/platform-open/sys-shop-package-order/user/buy`,
    JSON.stringify(data),
    {
      headers: {
        "Content-Type": "application/json",
      },
    },
  );
};

/**
 * 查询指定订单是否支付成功,支付成功返回date=true
 */
export const checkPayStatus = (orderId: any) => {
  return api.get(`/platform-open/sys-shop-package-order/${orderId}`, {});
};

// 查询收款配置
export const getPayConfig = () => {
  return api.get(`/platform-open/system-conf/pay/config`, {});
};

/**
 * 商户查询自己的套餐订单
 */
export const getOwnMeal = (data: any) => {
  return api.get(`/platform-open/sys-shop-package-order/console/order`, data);
};

/**
 * 计算套餐价格（购买/升级/消费）
 */
export const calculateMealPrice = (data: any) => {
  return api.post(
    `/platform-open/sys-shop-package-order/calculate/price`,
    JSON.stringify(data),
    {
      headers: {
        "Content-Type": "application/json",
      },
    },
  );
};
