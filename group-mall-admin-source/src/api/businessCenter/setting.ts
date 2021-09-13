/*
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:06
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-30 15:14:25
 */
import api from "@/libs/request";
import { userStore } from "@/store/modules/user";
import storage from "@/libs/storage";
import { Message } from "element-ui";

/**
 * 获取服务信息
 */
export const getSystemServiceInfo = () => {
  return api.get(`/platform-open/mini-info/service/info`, {});
};

/**
 * 获取基本信息
 */
export const getSystemBaseInfo = () => {
  return api.get(`/platform-open/mini-info/base/info`, {});
};

/**
 * 获取开发设置
 */
export const getSystemDevelopSetting = () => {
  return api.get(`/platform-open/mini-info/develop/setting`, {});
};

/**
 * 获取当前租户公众号配置
 */
export const getMiniMpConf = () => {
  return api.get(`/platform-open/mini-mp-conf`, {});
};

/**
 * 公众号配置添加或修改
 * @param data
 */
export const ModifyMiniMpConf = (data: any) => {
  return api.post(`/platform-open/mini-mp-conf`, data, {
    headers: {
      "Content-Type": "application/json",
    },
  });
};

/**
 * 获取体验者列表,返回人员对应的唯一字符串
 */
export const getMemberAuthList = () => {
  return api.get(`/platform-open/mini-experience/wxa/memberauth`, {});
};

/**
 * 解除绑定或绑定体验者
 * @param data
 */
export const setMiniExperience = (data: any) => {
  return api.post(`/platform-open/mini-experience/wxa/tester`, data, {
    headers: {
      "Content-Type": "application/json",
    },
  });
};

/**
 * 获取支付配置
 */
export const getPaymentConfig = (data: any) => {
  return api.get(`/platform-open/shop/pay/info`, data);
};

/**
 * 修改支付配置
 * @param data
 */
export const updatePaymentConfig = (data: any) => {
  return api.post(`/platform-open/shop/pay/info`, data, {
    headers: {
      "Content-Type": "application/json",
    },
  });
};

/**
 * 上传支付证书
 * @param data
 */
export const uploadCertificate = (data: any) => {
  const formData = new FormData();
  formData.append("file", data.file);
  return api.post(`/platform-open/shop/upload/certificate`, formData);
};

/**
 * 根据租户id获取店铺基本信息
 */
export const getShopInfo = async () => {
  let result = null;
  try {
    const response = await api.get(`/platform-open/shop/info`, {});
    const { data, code } = response;
    if (code === 200) {
      if (typeof data.businessHours === "string") {
        data.businessHours = JSON.parse(data.businessHours);
      }
      result = data;
      await userStore.setShopInfo(JSON.parse(JSON.stringify(data)));
      await storage.set("shopInfo", data);
    }
  } catch (e) {
    Message({
      type: "warning",
      message: e,
    });
  }
  return result;
};

/**
 * 控制台店铺设置(修改店铺信息)
 * @param shopId
 * @param data
 */
export const updateShopInfo = (shopId: any, data: any) => {
  return api.put(`/platform-open/shop/console/update/${shopId}`, data, {
    headers: {
      "Content-Type": "application/json",
    },
  });
};

/**
 * 修改电子发票邮箱
 * @param data
 */
export const changeEmail = (data: any) => {
  return api.put(`/platform-open/account-info/email/change`, data, {
    headers: {
      "Content-Type": "application/json",
    },
  });
};

/* 获取模板数据列表
 * @param data
 * @query: page --> 指定页数 (必须)
 * @query: type --> 分类类型：1 系统模版 2 定制模版 (必须)
 * @query: size --> 数据条数 (必须)
 */
export const getTemplateList = (data: any) => {
  return api.get("/platform-open/shopTemplate/list", data);
};

/* 删除店铺模板版本
 * @param id 模板版本id
 */
export const deleteTpVersion = (id: number) => {
  return api.delete(`/platform-open/shopTemplate/version/${id}`, {});
};

// 从开放平台获取小程序模板库
export const getTemplateVersionList = () => {
  return api.get("/platform-open/wechat/gettemplatelist", {});
};

/**
 * 编辑模板版本
 * @param data
 * @query: sendCodeDto --> 模板版本实体 (必须)
 */
export const updateTemplateVersion = (data: any) => {
  return api.put("/platform-open/shopTemplate/version", data, {
    headers: { "Content-Type": "application/json" },
  });
};

/**
 * 创建模板版本
 * @param data
 * @query: sendCodeDto --> 模板版本实体 (必须)
 */
export const createTemplateVersion = (data: any) => {
  return api.post("/platform-open/shopTemplate/version", data, {
    headers: { "Content-Type": "application/json" },
  });
};
