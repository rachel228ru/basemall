/*
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:06
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-26 09:53:51
 */
import api from "@/libs/request";

/**
 * 获取商品列表
 * @param data
 */
export const getAfterList = (data: any) => {
  return api.get("/afs-open/manage/list", data);
};

/** 获取售后详情 */
export const getAfterDetail = (afsId: string) => {
  return api.get("/afs-open/manage/info", { afsId });
};

/**
 * 获取协商历史
 * @param data
 */
export const getNegotiateList = (orderId: any) => {
  return api.get("/afs-open/negotiate/history", { orderId });
};

/**
 * 备注
 * @param data
 */
export const note = (data: any) => {
  return api.post("/afs-open/manage/note", data);
};

/**
 * 同意申请
 * @param data
 */
export const agree = (afsId: any) => {
  return api.put("/afs-open/manage/seller/approve", null, {
    params: { afsId },
  });
};

/**
 * 拒绝申请
 * @param data
 */
export const disagree = (data: { afsId: string; refusalReason: string }) => {
  return api.post("/afs-open/manage/seller/refusal", data, { params: data });
};
