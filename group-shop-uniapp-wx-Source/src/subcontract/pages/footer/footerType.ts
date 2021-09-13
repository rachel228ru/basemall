/*
 * @description:
 * @Author: vikingShip
 * @Date: 2021-08-11 16:46:52
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-19 13:59:04
 */

/**
 * @LastEditors: vikingShip
 * @description: footer组件state
 * @param list 商品数组列表
 * @param isDeal 编辑状态
 * @param isAllCheck 是否全选状态
 * @param query 商品列表查询条件
 * @param hasMore 是否有分页信息
 */
export interface FooterState {
	list: FooterItemType[]
	isDeal: boolean
	isAllCheck: boolean
	query: {
		current: 1
		size: 10
	}
	hasMore: boolean
	height: number
}

export interface FooterItemType {
	date: string
	isDataCheck: boolean
	shopList: FooterItemShopSet[]
}

/**
 * @LastEditors: vikingShip
 * @description: FooterItemType
 * @param footmarkId 用户收藏表id
 * @param userId 用户id
 * @param productId 商品id
 * @param productPic 商品主图
 * @param productName 商品名称
 * @param status 商品状态  0-上架 1-下架 2-售罄
 * @param productPrice 商品实际售价
 * @param originalPrice 指导价划线价
 */
export interface FooterItemShopSet {
	footmarkId: number
	userId: string
	productId: number
	productPic: string
	productName: string
	status: number
	productPrice: string | number
	originalPrice: string | number
	time: string
	isCheck: boolean
	isDelete: boolean
	createTime: string
	intPrice: string
	smaPrice: string
}
