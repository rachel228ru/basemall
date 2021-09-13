/*
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:06
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-26 09:50:43
 */
import api from "@/libs/request";

/** 获取getUrl */
export const getUrl = (data: any) => {
  return api.get(`/platform-open/shopTemplate/version`, data);
};
/**
 * 获取装修自定义页面
 * @param data
 */
export const getPageList = (data: any) => {
  return api.get("/shops-open/renovation/template/page/list", data);
};
/**
 * 页面获取装修组件
 * @param data
 */
export const getAssembly = (data: any) => {
  return api.post("/shops-open/renovation/template/page/assembly/list", data);
};
/**
 * 新增装修自定义页面
 * @param data
 */
export const addPage = (data: any) => {
  return api.put("/shops-open/renovation/template/page/save", data);
};
/**
 * 页面新增装修组件
 * @param data
 */
export const addAssembly = (data: any) => {
  return api.post("/shops-open/renovation/template/page/assembly/add", data);
};

/**
 * 删除装修自定义页面
 * @param data
 */
export const delPage = (data: any) => {
  return api.get("/shops-open/renovation/template/page/delete", data);
};
/**
 * 获取是否页面已添加内容
 */
export const getJudgeAssembly = (data: any) => {
  return api.get("/shops-open/renovation/template/page/modelId", data);
};
