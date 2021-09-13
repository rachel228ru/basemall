/*
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-18 19:10:59
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-18 19:21:04
 * 123
 */
export interface IAplyAfterSaleForm {
	type: string
	orderId: string
	productSkuId: string
	productQuantity: number
	refundAmount: number
	description?: string
	reason?: string
}
