/*
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:10
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-23 16:03:12
 */
/**
 * 底部导航
 */
import MenuList from "./NavBarItem";

export default class NavBar {
  /** 选择样式,1普通样式 2中间大图样式 */
  codeStyle = 1;

  /** 底部导航列表 */
  menuList: MenuList[] = [];

  /**字体选中颜色  */
  selectColor = "#F64E3F";

  /** 字体未选择颜色 */
  defaultColor = "#7A7E83";
}
