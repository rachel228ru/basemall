/*
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:10
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-23 16:03:47
 */
export default class MenuList {
  /** 导航名称 */
  text = "导航名称";

  /** 图标类型 1-系统图标 2-自定义图标 */
  iconType = 1;

  /** 图标url */
  iconPath = "";

  /** 选中图标url */
  selectedIconPath = "";

  /** 系统图标 */
  defIcon = "";

  actIcon = "";

  /** 添加链接时的 跳转类型 0 -功能页面 1-商超商品 2 -展示分类 3-自定义页面 4 活动营销 */
  // linkSelectItem: any;
  /** 链接跳转地址 */
  linkUrl = "";

  /** 链接名称 */
  linkName = "";

  /** append index页面固定页面会有此参数 */
  name = "";

  /** 排序位置 */
  sortIndex = 0;

  /** 是否为首页 */
  isHome = false;

  /** 链接类型5为自定义页面 */
  type: string | number = "";

  id: number | string = "";

  isAdd = true;
}

// const arr = [
//   {navName: "导航名称",
//   fontColor: "#333333",
//   navIcon: "",
//   pathLink: "",
//   linkUrl: "pages/index/index",
//   linkName: "自定义页面2",
//   linkNameWord: "",
//   type: 5,
// }]
