/*
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:10
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-23 18:08:51
 */
export default class StoreNavigationItemTs {
  /** 导航名称 */
  navName = "导航名称";

  /** 字体颜色 */
  fontColor = "#333333";

  /** 图标 */
  navIcon = "https://qiniu-app.qtshe.com/u391.png";

  /** 跳转路径 */
  pathLink = "";

  /** 链接跳转地址 */
  linkUrl = "";

  /** 链接名称 */
  linkName = "";

  /** home功能页面会有此参数 */
  append = "";

  /** 链接类型5为自定义页面 */
  type: string | number = "";

  /** 链接类型5为自定义页面 */
  id: string | number = "";
}
