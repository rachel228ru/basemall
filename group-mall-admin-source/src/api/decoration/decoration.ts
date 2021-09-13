/*
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:06
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-26 09:48:22
 */
import api from "@/libs/request";

/**
 * 获取展示分类列表
 * @param data
 */
export const getAllCategory = (data: any) => {
  return api.get("/goods-open/api/show/category/get/all", data);
};

/**
 * 获取用户中心配置
 * @param data
 */
export const getUserCenterConfig = () => {
  return api.get(`/shops-open/account-center`, {});
};

/**
 * 设置用户中心配置
 * @param data
 */
export const setUserCenterConfig = (data: any) => {
  return api.put(`/shops-open/account-center`, data, {
    headers: {
      "Content-Type": "application/json",
    },
  });
};

/**
 * 新增装修模板
 */
export const addTemplate = (params: any) => {
  return api.post("/shops-open/renovation/template/add", params);
};
/**
 * 批量新增控件
 */
export const addControlMore = (params: any) => {
  return api.post("/shops-open/renovation/plugins/add", params);
};
/**
 * 获取商铺装修模板列表
 */
export const getTemplate = (params: any) => {
  return api.post("/shops-open/renovation/template/list", params);
};

/**
 * 获取默认模板列表
 */
export const getdefTemplate = (params: any) => {
  return api.post("/shops-open/renovation/template/def/list", params);
};

/**
 * 删除商铺装修模板
 */
export const deleteTemplate = (params: any) => {
  return api.get("/shops-open/renovation/template/delete", params);
};

/** 删除页面组件byids */
export const deleteAssembly = (params: any) => {
  return api.get(
    "/shops-open/renovation/template/page/assembly/delete",
    params,
  );
};

/** 新增控件 */
export const addControl = (params: any) => {
  return api.post("/shops-open/renovation/plugin/add", params);
};

/** 删除装修控件 */
export const deleteControl = (params: any) => {
  return api.get("/shops-open/renovation/plugin/delete", params);
};

/** 获取装修控件列表 */
export const getControl = (params: any) => {
  return api.post("/shops-open/renovation/plugin/list", params);
};

/** 批量新增修改控件 */
export const editControls = (params: any) => {
  return api.post("/shops-open/renovation/plugins/add", params);
};

/** 模版复制 */
export const copyTemplate = (params: any) => {
  return api.get("/shops-open/renovation/template/copy", params);
};

/** 获取装修后的商品专区 */
export const getDecList = (params: any) => {
  return api.get("/shops-open/renovation/fitment/end/page/list", params);
};
