/*
 * @description:
 * @Author: vikingShip
 * @Date:21-08-02 10:17:20
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-16 14:00:32
 */

/**
 * @LastEditors: vikingShip
 * @description: cube组件s
 * @param borderWidth 边距宽度
 * @param layoutWidth 块宽
 * @param layoutHeight 块高
 * @param showMethod 展示类型
 * @param pageMargin 页面外边距
 * @param width 宽度
 * @param subEntry 魔方描述信息
 *
 */
export interface CubeStateType {
	borderWidth: number
	layoutWidth: number
	layoutHeight: number
	showMethod: number
	pageMargin: number
	width: number
	subEntry: Array<IBanners>
	showCubeListWrap?: Array<ShowCubeItemType>
	pageMarginStyle?: pageMarginStyle
}

export type pageMarginStyle = {
	style: string
	width?: number
	height?: number
	margin?: string
}

/**
 * @LastEditors: vikingShip
 * @description: 魔方单个元素信息
 * @param x x轴个数
 * @param y y轴个数
 * @param width 单个元素宽度
 * @param height 单个元素高度
 * @param link 单个元素绑定链接信息
 * @param linkName 单个元素绑定描述
 */

export interface IBanners {
	x: number
	y: number
	width: number
	height: number
	img: string
	link: LinkSelectItem
	linkName: string
}

/**
 * @LastEditors: vikingShip
 * @description: 绑定单个元素链接描述信息
 * @param type 链接类型 0 -功能页面 FunctionPage 1-商超商品 Goods 2 -展示分类 DisplayClassify 3-自定义页面 CustomPage 4 活动营销 ActivityMarket
 * @param name 链接名称
 * @param url 链接地址
 * @param append 附加参数
 */

export interface LinkSelectItem {
	id: number
	type: number
	name: string
	url: string
	append: string
	[x: string]: any
}

/**
 * @LastEditors: vikingShip
 * @description: 展示单个魔方元素
 * @param borderWidth 间隙距离
 * @param height 魔方高度
 * @param width 魔方高度
 * @param img  魔方图片地址
 * @param left 魔方绝对定位left值
 * @param top 魔方绝对定位top值
 * @param paddingTop 魔方paddingTop
 * @param style 魔方内容实际样式
 */
type ShowCubeItem =
	| 'borderWidth'
	| 'height'
	| 'img'
	| 'left'
	| 'paddingTop'
	| 'style'
	| 'top'
	| 'width'
export type ShowCubeItemType = Record<ShowCubeItem, string>
