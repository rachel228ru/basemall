/*
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:06
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-27 14:25:41
 */
import api from "@/libs/request";
import { ApiLogisticsCompanyList } from "@/views/logistics/logistics/logisticType";

/**
 * 获取物流公司
 * @param data
 */
export const getLogistCompany = (data: any) => {
  return api.get("logistics-open/feign/getLogisticsCom", data);
};

/**
 * 获取运费模板
 */
export const getLogistModel = (data: any) => {
  return api.get("logistics-open/get/template/list", data);
};

/**
 * 删除运费模板
 */
export const delLogistModel = (id: any, data: any) => {
  return api.delete(`logistics-open/delete/template/info/${id}`, data);
};

/**
 * 新增单个模板信息
 */
export const addLogistModel = (data: any) => {
  return api.post("logistics-open/set/template/info", data);
};

/**
 * 获取地址信息
 */
export const getShopAddress = (data: any) => {
  return api.get("logistics-open/get/address/list", data);
};

/**
 * 获取所有地址信息
 */
export const getAllShopAddress = (data: any) => {
  return api.get("logistics-open/get/all/address", data);
};

/**
 * 设置收货退货地址
 */
export const setShopAddress = (id: any, type: any, data: any) => {
  return api.put(`logistics-open/set/def/address/${id}/${type}`, data);
};

/**
 * 删除
 */
export const delShopAddress = (id: any, data: any) => {
  return api.delete(`logistics-open/del/address/${id}`, data);
};

/**
 * 添加地址信息
 */
export const addShopAddress = (data: any) => {
  return api.post("logistics-open/set/address", data);
};

/**
 * 获取物流公司
 */
export const getLogisticsCompany = (data?: any) => {
  return api.get("logistics-open/list/company", data);
};

/**
 * 批量发货
 */
export const bulkShipment = (data: any) => {
  return api.post("logistics-open/batch/deliver/goods", data);
};

/**
 * 获取电子面单打印机列表
 * @param data
 */
export const getSheetList = (data: any) => {
  return api.get("logistics-open/get/express/print/list", data);
};

/**
 * 新增/修改电子面单打印机
 */
export const addSheetMach = (data: any) => {
  return api.post("logistics-open/set/express/print/info", data);
};

/**
 * 启用/停用电子面单打印机
 */
export const sheetLink = (id: any, type: any, data: any) => {
  return api.put(`logistics-open/set/express/print/status/${id}/${type}`, data);
};

/**
 * 删除电子面单打印机
 */
export const delSheetMach = (id: any, data: any) => {
  return api.delete(`logistics-open/del/express/print/${id}`, data);
};

/**
 * 获取物流公司地址
 */
export const getLogAddress = (data: any) => {
  return api.get<ApiLogisticsCompanyList[]>(
    `logistics-open/get/express/list`,
    data,
  );
};

/**
 * 开通物流地址
 */
export const openLogAddress = (data: any) => {
  return api.post(`logistics-open/set/express/info`, data);
};

/**
 * 删除物流公司地址
 */
export const delCompony = (id: any, data: any) => {
  return api.delete(`logistics-open/del/express/${id}`, data);
};

/**
 * 设置默认发货公司
 */
export const setDefaultAddress = (logisticsCompanyId: string) => {
  return api.get(`logistics-open/set/default`, {
    logisticsCompanyId,
  });
};

/**
 * 导入所有待发货订单
 */
export const importAllOrder = (data: any) => {
  return api.get(
    `integral-open/manager/integralOrder/get/allWaitForSend`,
    data,
  );
};
