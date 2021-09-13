/*
 * @description:
 * @Author: vikingShip
 * @Date: 2021-08-13 13:20:46
 * @LastEditors: vikingShip
 * @LastEditTime: 2021-08-25 16:25:29
 */

import { IActivePage } from "@/views/decoration/components/EditorPage/src/interfaces/activePage";

/**
 * @LastEditors: vikingShip
 * @description: decorationStore的state
 * @param templateId 可以更改的临时模板id
 * @param saveTemplateId 最终保存的模板id
 * @param pageName 自定义页面名称
 * @param activePage 自定义页面属性
 * @param activeComIndex 当前页面操作组件下标
 * @param homePageId 设为首页id
 * @param activePageType 管理页面管理tab栏 customize:自定义 userCenter:用户中心 bussiness:营销
 * @param copyLink 复制链接
 * @param isUsercenterCompontents
 * @param userCenterType
 * @param firstStatus
 * @param activeTab 左侧标签页面
 * @param getFrom 是否底部导航添加链接
 * @param saleMode 商品专区id
 * @param isCustom
 */
export interface DecorationState {
  templateId: string;
  saveTemplateId: string;
  pageName: string;
  activePage: IActivePage;
  activeComIndex: number;
  homePageId: string | number;
  activePageType: string;
  copyLink: string;
  isUsercenterCompontents: boolean;
  userCenterType: string;
  firstStatus: boolean;
  activeTab: number;
  getFrom: string;
  saleMode: string;
  isCustom: boolean;
}
