/*
 * @description:
 * @Author: vikingShip
 * @Date: 2021-08-18 09:54:37
 * @LastEditors: vikingShip
 * @LastEditTime: 2021-08-25 09:57:26
 */

import { ApiSpecArea } from "@/views/goods/marketModel/marketType";
/**
 * @LastEditors: vikingShip
 * @description: GoodRegion/state
 * @param showRegion 弹窗显示添加专区
 * @param addFlag 添加还是编辑专区
 * @param deleteType 删除专区弹窗
 * @param pageSize 分页页数
 * @param pageNum 分页页码
 * @param total 数据总数
 * @param regionList 获取到已有专区数据
 * @param redirectUrl 重定向地址
 * @param addList 新增弹窗内数据
 * @param showType 是否还能添加专区
 * @param shopInfoId 商超id
 * @param click 确认按钮标识
 */
export interface GoodRegionState {
  showRegion: boolean;
  addFlag: boolean;
  deleteType: boolean;
  pageSize: number;
  total: number;
  regionList: ApiSpecArea[];
  redirectUrl: string;
  addList: Array<Partial<ApiSpecArea>>
  showType: boolean;
  shopInfoId: number | string;
  code: string;
  click: boolean;
}
