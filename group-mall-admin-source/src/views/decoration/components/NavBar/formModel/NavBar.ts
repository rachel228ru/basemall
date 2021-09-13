export interface INavBar {
  codeStyle: number;
  menuVos: MenuList[];
}

export interface MenuList {
  /** 导航名称 */
  menuName: string;
  /** 导航id */
  // "id": number;
  /** 图标类型 1-系统图标 2-自定义图标 */
  iconType: number;
  /** 未选中图标url */
  noSelectIcon: string;
  /** 选中图标url */
  selectIcon: string;
  /** 添加链接时的 跳转类型 0 -功能页面 1-商超商品 2 -展示分类 3-自定义页面 4 活动营销 */
  linkSelectItem: any;
  /** 排序位置 */
  sortIndex: number;
}

export interface SubMenuList {
  menuName: string;
  /** 导航id */
  id: number;
  /** 图标类型 1-系统图标 2-自定义图标 */
  iconType: number;
  /** 未选中图标url */
  noSelectIcon: string;
  /** 选中图标url */
  selectIcon: string;
  /** 添加链接时的 跳转类型 0 -功能页面 1-商超商品 2 -展示分类 3-自定义页面 4 活动营销 */
  linkSelectItem: any;
  /** 排序位置 */
  sortIndex: number;
}

/**
 * 底部导航配置
 */
export default class NavBar implements INavBar {
  /** 选择样式,1普通样式 2中间大图样式 */
  codeStyle = 1;

  /** 底部导航列表 */
  menuVos: MenuList[] = [];
}
