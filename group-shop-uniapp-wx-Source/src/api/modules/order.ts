/*
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-12 15:36:20
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-28 13:16:24
 */
import api from '@/utils/request'

/**
 * 获取订单列表
 * @param query
 */
export const getOrders = (query: any) => {
	return api.get('/order-open/mini/search', query)
}

/**
 * 付款
 * @param orderId
 */
export const payment = (orderId: string, userBalance: boolean = false) => {
	return api.put(`/order-open/mini/pay/${orderId}?userBalance=${userBalance}`)
}

/**
 * 确认收货
 * @param orderId
 */
export const receipt = (orderId: string) => {
	return api.put(`/order-open/mini/receipt/${orderId}`)
}

/**
 * 取消订单
 * @param orderId
 */
export const cancel = (orderId: string) => {
	return api.put(`/order-open/mini/cancel/${orderId}`)
}

/**
 * 获取订单详情
 * @param orderId
 */
export const getOrderDetail = (orderId: string) => {
	return api.get(`/order-open/mini/info/${orderId}`)
}

/**
 * 获取评价列表
 * @param query
 */
export const getEvaluateList = (query: any) => {
	return api.get('/order-open/mini/evaluate', query)
}

/**
 * 评价订单
 * @param body
 */
export const evaluate = (body: any) => {
	return api.post('/order-open/mini/evaluate', body)
}

/**
 * 获取结算页面
 * @param form
 */
export const getConfirmOrder = (form: any) => {
	return api.post(`/order-open/mini/confirm_order`, form)
}

/**
 * 创建订单
 * @param form
 */
export const createOrder = (form: any) => {
	return api.post(`/order-open/mini/create`, form)
}

/**
 * 创建订单创建状态
 * @param form
 * res -1->秒杀失败;0->排队中;order->成功;
 */
export const getOrderStatus = (form: any) => {
	return api.post(`/order-open/mini/result`, form)
}

/**
 * 模拟支付
 */
export const mockPay = (orderId: any) => {
	return api.put(`/order-open/mini/pay/notify/${orderId}`)
}

/**
 * 获取运费
 */
export const getFreightAmount = (query: any) => {
	return api.post(`/order-open/mini/freightAmount`, query)
}

/**
 * 用户订单概况
 */
export const getOrderOverview = () => {
	return api.get('/order-open/mini/overview')
}

/**
 * 获取物流信息
 */
export const getdeliveryInfo = (form: any) => {
	return api.get('/order-open/manage/traces', form)
}

/**
 * 获取物流信息
 */
export const getSenddelivery = (form: any) => {
	return api.get('/integral-open/manager/integralOrder/traces/info', form)
}
/**
 * 评价设置
 */
export const getEvaluateSetting = () => {
	return api.get('/order-open/mini/evaluate/setting', null)
}

/**
 * 获取订单设置
 */
export const getOrderSetting = () => {
	return api.get('/finance-open/manage/setting/order', null)
}

/**
 * 更新订单地址
 * @param form
 */
export const updateAddress = (form: any) => {
	return api.put('/order-open/mini/receiver/address', form)
}

/**
 * 获取分享所需信息
 * @param orderId
 */
export const getShareInfo = (orderId: any) => {
	return api.get(`/order-open/mini/share/${orderId}`, null)
}
