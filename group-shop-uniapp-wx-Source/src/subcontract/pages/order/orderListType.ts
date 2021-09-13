/*
 * @description:
 * @Author: vikingShip
 * @Date: 2021-08-11 13:26:07
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-19 15:21:45
 */

import { IGoodsList } from '@/typings/goods'

/**
 * @LastEditors: vikingShip
 * @description: order组件state
 * @param active 当前tab
 * @param orders 订单数据
 * @param isLast 是否为最后一条记录
 * @param isFistEnter 是否为第一次进度
 */

export interface orderListState {
	active: string
	orders: ApiOrderItemType[]
	query: SearchQuery
	isLast: boolean
	isFistEnter: boolean
}
/**
 * @LastEditors: vikingShip
 * @description: 查询条件
 * @param current 当前页码
 * @param keyword 关键词
 * @param size 条数
 * @param orderStatus 订单类型 -1：所有订单, 0.待付款（待买家付款）1.待发货（买家已付款）3.待提货（物流订单已发货）, 4.已完成（用户已经签收）5.待评价, 6.售后订单, 7.已关闭
 */
export interface SearchQuery {
	current: number
	keyword: string
	orderStatus: string
	size: number
}

/**
 * @LastEditors: vikingShip
 * @description: 余额列表item
 * @param createTime 创建时间
 * @param expireTime 过期时间
 * @param orderId 订单id
 * @param originalOrderId 原订单id，只要换货单有此属性
 * @param payAmount 订单支付金额
 * @param payType 支付类型
 * @param status 订单状态
 * @param type 订单类型
 */
export interface ApiOrderItemType {
	createTime: string
	expireTime: string
	orderId: number
	originalOrderId: number
	payAmount: number
	payType: string
	status: string
	type: string
	itemVoList?: IGoodsList[]
	disabled?: boolean
}
