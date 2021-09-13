/*
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:10
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-24 10:57:12
 */
export interface CustomizedPages {
  /** 页面id */
  id: number;
  /** 商品名称 */
  name: string;
  /** 是否是首页 */
  isHome: boolean;
}

export interface CategoryPage extends CustomizedPages {
  /** 页面类型  2商超分类页 */
  type: string;
}

export interface IControlItem {
  id: string;
  isDeleted: string;
  isMandatory: string;
  isSelection: string;
  operatorId: string;
  operatorName: string;
  pluginNameCn: string;
  pluginNameEn: string;
  pluginProperties: string;
  shopId: string;
  spare?: string;
  templateId: string;
  tenantId: string;
}

export interface INavBarPlugin {
  actIcon: string;
  defIcon: string;
  getFrom: string;
  iconPath: string;
  iconType: number;
  id: string;
  isHome: boolean;
  linkName: string;
  linkSelectItem: string;
  linkUrl: string;
  name: string;
  selectedIconPath: string;
  sortIndex: number;
  text: string;
  type: number;
}
