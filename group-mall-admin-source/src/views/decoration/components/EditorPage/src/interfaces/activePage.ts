/*
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-24 09:00:59
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-27 13:58:40
 */
export interface IActivePageItem {
  id: string;
  isDef: string;
  isDeleted: string;
  modelId: string | number | null;
  pageName: string;
  shopId: string;
  templateId: string;
  tenantId: string;
  type: string;
  operatorId?: string;
  operatorName?: string;
  name?: string;
}

export type IActivePage = IActivePageItem | "usercenter";
