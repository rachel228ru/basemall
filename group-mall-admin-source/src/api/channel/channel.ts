/*
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:06
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-26 09:47:30
 */
import api from "@/libs/request";

/**
 * 获取扫码授权url,需存在正确token值
 * @param data Dto
 */
export const authPreauthcode = (data: any) => {
  return api.post(`/platform-open/mini/authorization/preauthcode`, data, {
    headers: {
      "Content-Type": "application/json",
    },
  });
};

/**
 * 获取绑定的小程序信息|公众号信息
 * @param data 获取信息类型,1-公众号 2-小程序
 */
export const getMiniBaseInfo = (data: any) => {
  return api.get(`/platform-open/mini-info/base/info`, data);
};

/**
 * 公众号配置添加或修改
 * @param data Dto
 */
export const modifyMpConfig = (data: any) => {
  return api.post(`/platform-open/mini-mp-conf`, data, {
    headers: {
      "Content-Type": "application/json",
    },
  });
};

/**
 * 获取小程序码,返回base64
 * @param data Dto
 */
export const getwxacode = (data: any) => {
  return api.post(`/platform-open/mini-info/wxa/getwxacode`, data, {
    headers: {
      "Content-Type": "application/json",
    },
  });
};

/**
 * 小程序基本信息更新
 */
export const updateMiniBaseInfo = () => {
  return api.get(`/platform-open/mini-info/base/info/update`, {});
};

/**
 * 小程序版本更新
 */
export const updateMiniVersion = data => {
  return api.get(`/platform-open/mini-info/version/update`, data);
};

/**
 * 撤销小程序当前审核中的版本
 */
export const revocationMiniVersion = () => {
  return api.get(`/platform-open/mini-info/revocation`, {});
};

/**
 * 获取的体验版二维码,返回体验二维码base64
 */
export const getQrcode = (data?: any) => {
  return api.get(`/platform-open/mini-experience/wxa/get_qrcode`, data);
};

// 更新小程序数据
export const updateMini = (data: any) => {
  return api.put("/platform-open/mini-info/update", data, {
    headers: { "Content-Type": "application/json" },
  });
};

/**
 * 根据平台店铺id查询当前授权信息,每次授权消息最多保留5分钟
 */
export const miniAuthorizationError = (data?: any) => {
  return api.get(`/platform-open/mini/authorization/error`, data);
};
