/*
 * @description:
 * @Author: vikingShip
 * @Date: 2021-08-02 11:21:09
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-17 10:15:20
 */

/**
 * @LastEditors: vikingShip
 * @description: shopNav组件中state类型
 * @param authType 登录标识符
 * @param navList 导航列表
 */
export interface ShopNavType {
	authType: boolean
	navList: Array<navItemType>
}

/**
 * @LastEditors: vikingShip
 * @description: navitem类型
 * @param append 追加参数
 * @param fontColor 导航元素字体颜色
 * @param linkName 链接名称
 * @param linkUrl 链接地址
 * @param navIcon 导航元素icon图标
 * @param navName 导航title
 * @param type 跳转类型
 */

export interface navItemType {
	append: string
	fontColor: string
	id: number
	linkName: string
	linkUrl: string
	navIcon: string
	navName: string
	pathLink: string
	type: number
}
