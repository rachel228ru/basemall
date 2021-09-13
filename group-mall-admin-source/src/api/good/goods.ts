/*
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:06
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-27 17:16:06
 */
import api from "@/libs/request";
import {
  FreightTempType,
  GoodAttributeTempType,
  GoodCategroyType,
  GoodDetailInfo,
} from "@/views/goods/marketModel/goodType";
import { ApiSpecArea } from "@/views/goods/marketModel/marketType";

/**
 * 获取展示分类列表
 * @param data
 */
export const getProList = (data: any) => {
  return api.get<{ total: number; list: GoodDetailInfo[] }>(
    "/goods-open/manager/product/list",
    data,
  );
};

/**
 * 获取csv导入商品列表
 * @param data
 */
export const getCsvProList = (data: any) => {
  return api.get("/goods-open/manager/product/csv/list", data);
};

/**
 * 获取属性列表
 * @param data
 */
export const getAttsList = (data: any) => {
  return api.get<{ total: number; list: GoodAttributeTempType[] }>(
    "/goods-open/manager/attribute/template/list",
    data,
  );
};

/**
 * 获取所有属性列表
 */
export const getAllAttsList = (data: any) => {
  return api.get("/goods-open/manager/attribute/template/get/all", data);
};
/**
 * 删除属性列表
 */
export const delAttr = (ids: any, data: any) => {
  return api.delete(
    `/goods-open/manager/attribute/template/delete/${ids}`,
    data,
  );
};
/**
 * 删除子属性模板
 */
export const delAttrSon = (ids: any, data: any) => {
  return api.delete(
    `/goods-open/manager/attribute/template/delete/child/${ids}`,
    data,
  );
};

/**
 * 添加属性列表
 */
export const addAttrList = (data: any) => {
  return api.post("/goods-open/manager/attribute/template/save", data);
};

/**
 * 编辑属性列表
 */
export const updateAttrList = (data: any) => {
  return api.put("/goods-open/manager/attribute/template/update", data);
};

/**
 * 获取供应商列表
 * @param data
 */
export const getSupList = (data: any) => {
  return api.get("/goods-open/manager/supplier/list", data);
};

/**
 * @param ids 获取所有供应商
 * @param data
 */
export const getAllSupList = (data: any) => {
  return api.get("/goods-open/manager/supplier/get/all", data);
};

/**
 * 删除供应商
 */
export const delSup = (ids: any, data: any) => {
  return api.delete(`/goods-open/manager/supplier/delete/${ids}`, data);
};

/**
 * 添加供应商
 */
export const addSup = (data: any) => {
  return api.post("/goods-open/manager/supplier/save", data);
};

/**
 * 修改供应商
 */
export const updateSup = (data: any) => {
  return api.put("/goods-open/manager/supplier/update", data);
};

/** 供应商审核 */
export const dealApplySup = (data: any) => {
  return api.put(`/goods-open/manager/supplier/check`, data);
};

/**
 * 查看供应商名下商品
 */
export const ShowSupPro = (data: any) => {
  return api.get("/goods-open/manager/supplier/product", data);
};

/**
 * 获取展示分类列表
 * @param data
 */
export const getList = (data: any) => {
  return api.get<{ list: GoodCategroyType[]; total: number }>(
    "/goods-open/manager/show/category/list",
    data,
  );
};

/**
 * 获取活动有效展示分类列表
 * @param data
 */
export const getEffectiveList = (data: any) => {
  return api.post(
    "/assemble-open/activity/ass/list/product_show_category",
    data,
  );
};

/**
 * 获取所有展示分类
 * @param data
 */
export const getAllCategory = (data: any) => {
  return api.get("/goods-open/manager/show/category/get/all", data);
};

/**
 * 获取商超有效展示分类 0-商超
 * @param data
 */
export const getEffectiveCategory = (data: any) => {
  return api.get(`/goods-open/api/show/category/get/${data}`, {});
  // return api.get(`/goods-open/manager/show/category/get/${data}`, {});
};

/**
 * 删除展示分类
 */
export const delLevel = (id: any, data: any) => {
  return api.delete(`/goods-open/manager/show/category/delete/${id}`, data);
};

/**
 * 添加展示分类
 */
export const addList = (data: any) => {
  return api.post("/goods-open/manager/show/category/save", data);
};

/**
 * 获取物流模板
 */
export const getLogistics = (data: any) => {
  return api.get(
    "/goods-open/manager/freight/template/list/by/logistics",
    data,
  );
};

/**
 * 获取物流模板详情
 */
export const getLogisticsContent = (freightTemplateId: any, data: any) => {
  return api.get<FreightTempType>(
    `/goods-open/manager/freight/template/info/by/logistics/${freightTemplateId}`,
    data,
  );
};

/**
 * 二级分类保存
 */
export const addSecondList = (data: any) => {
  return api.post("/goods-open/manager/show/category/save/second", data);
};

/**
 * 分类编辑保存
 */
export const updateList = (data: any) => {
  return api.put("/goods-open/manager/show/category/update", data);
};

/**
 * 商品专区变更
 */
export const updateGoodsApart = (saleMode: any, mode: any, data: any) => {
  return api.put(
    `/goods-open/manager/product/updateSaleMode/${saleMode}/${mode}`,
    data,
  );
};

/**
 * 发布商品
 */
export const putWayGood = (data: any) => {
  return api.post("/goods-open/manager/product/issue", data);
};

/**
 * 编辑商品
 */
export const editGood = (id: any, data: any = {}) => {
  return api.get(`/goods-open/manager/product/get/${id}`, data);
};

/**
 * @param status 编辑发布商品
 * @param data
 */
export const updateGood = (data: any) => {
  return api.put("/goods-open/manager/product/update", data);
};

/**
 * 编辑展示分类
 */
export const updateGoodShow = (ids: any, data: any) => {
  return api.post(
    `/goods-open/manager/product/updateProductShowCategory/${ids}`,
    data,
  );
};

/**
 * 商品上下架
 */
export const GoodUpDown = (status: any, mode: any, data: any) => {
  return api.put(
    `/goods-open/manager/product/updateStatus/${status}/${mode}`,
    data,
  );
};

/**
 * 商品删除
 */
export const GoodDel = (ids: any, mode: any, data: any) => {
  return api.delete(`/goods-open/manager/product/delete/${ids}/${mode}`, data);
};

/**
 * 分类排序
 */
export const Sort = (data: any) => {
  return api.put("/goods-open/manager/show/category/sort", data);
};

/**
 * 获取商品码
 */
export const getImgCode = (data: any) => {
  return api.post("/platform-open/mini-info/wxa/getwxacode", data);
};

/**
 * 过滤不可用的商品
 */
export const discountList = (data: any) => {
  return api.get(`/goods-open/manager/product/alive/product/list/${data}`, {});
};

/**
 * 商品素材导入
 */
export const importGood = (data: any) => {
  const formData = new FormData();
  formData.append("file", data.file);
  return api.post(`/goods-open/manager/product/import/csv/product`, formData);
};

/**
 * 通用设置
 */
export const getRebateInSet = (data: any) => {
  return api.get(`/goods-open/manager/rebate/set/get`, data);
};

/**
 * 保存通用设置
 */
export const putRebateInSet = (data: any) => {
  return api.put(`/goods-open/manager/rebate/set/save`, data);
};

/**
 * 获取默认专区
 */
export const getRegionList = (data: any) => {
  return api.get(`/goods-open/manager/sale/mode/list`, data);
};

/**
 * 删除专区
 */
export const delRegionItem = (id: any, data: any) => {
  return api.delete(`/goods-open/manager/sale/mode/delete/${id}`, data);
};

/**
 * 获取所有专区
 */
export const getAllRegionList = (data: any) => {
  return api.get<Array<ApiSpecArea>>(
    `/goods-open/manager/sale/mode/get/all`,
    data,
  );
};

/**
 * 专区排序
 */
export const sortRegionList = (data: any) => {
  return api.put(`/goods-open/manager/sale/mode/sort`, data);
};

/**
 * 专区编辑
 */
export const editRegionList = (data: any) => {
  return api.put(`/goods-open/manager/sale/mode/update`, data);
};

/**
 * 专区新增
 */
export const addRegionList = (data: any) => {
  return api.post(`/goods-open/manager/sale/mode/save`, data);
};
