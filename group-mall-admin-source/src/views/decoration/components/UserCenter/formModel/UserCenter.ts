import LinkSelectItem from "@/components/LinkSelect/src/components/LinkSelectItem";

/*
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:10
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-27 14:23:49
 */
export interface IUserCenter {
  customStyle: string;
  headStyle: number;
  id: number;
  menuStyle: number;
  menuVos: MenuList[];
  orderInfo: string;
}

export interface MenuList {
  /** 是否可用 0不可用 1-可用 */
  allowUse: number;
  /** 默认图标地址 */
  defaultIcon: string;
  /** 菜单是否 0隐藏1显示 */
  hideMenu: boolean;
  /** 菜单id */
  id: number;
  /** 跳转类型 0 -功能页面 1-商超商品 2 -展示分类 3-自定义页面 4 活动营销 */
  linkSelectItem: LinkSelectItem | string;
  /** 菜单当前图标url */
  menuIconUrl: string;
  /** 菜单名称 */
  menuName: string;
  /** 父级id */
  pid: number | string;
  /** 排序位置 */
  sortIndex: number;
  /** 分隔 */
  splitFlag: boolean;
  /** 菜单样式类别 1-展开式 2-折叠式 */
  styleType: number;
  /** 领卡文案 */
}

export interface SubMenuList {
  id: number;
  menuName: string;
  menuIconUrl: string;
  defaultIcon: string | null;
  styleType: number;
  sortIndex: number;
  hideMenu: boolean;
  allowUse: number;
  linkSelectItem: LinkSelectItem | string;
  splitFlag: boolean;
  pid: number | string;
}

/**
 * 用户中心配置
 */
export default class UserCenter implements IUserCenter {
  /** 自定义风格样式,json存储 */
  customStyle = "";

  /** 头部风格 1-系统风格 2-自定义风格 */
  headStyle = 1;

  /** 用户中心数据id */
  id = 0;

  /** 菜单选择样式 1-展开式 2-折叠式 */
  menuStyle = 1;

  /** 菜单列表 */
  menuVos: MenuList[] = [];

  /** 订单icon信息 json存储 */
  orderInfo = "";
}
