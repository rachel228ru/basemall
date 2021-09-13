/*
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-14 15:34:42
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-19 16:22:19
 * 123
 */

import { IidType } from './global'

export interface IGoodsList {
	activityId: IidType
	activityProductId: IidType
	afs: IAfs | null
	applyNum: string
	brokerageAmount: number
	couponAmount: number
	createTime: string
	deleted: boolean
	giftIntegration: number
	id: string
	integrationAmount: number
	memberAmount: number
	orderId: string
	productId: string
	productName: string
	productOriginalPrice: number
	productPic: string
	productPrice: number
	productQuantity: number
	productSkuCode: null | string
	productSkuId: string
	productSn: string
	promotionAmount: number
	providerId: IidType
	realAmount: number
	rebateAmount: number
	refundAmount: null | number
	shopId: string
	specs: string
	tenantId: string
	updateTime: string
	dominoState?: string | number
}

type ICloseType = 'USER_CANCEL' | 'RE_EXCHANGE' | 'SELLER_REFUSE'

type IAfsStatus =
	| 'WAIT_FOR_BUSINESS_APPROVED'
	| 'WAIT_FOR_RETURN'
	| 'WAIT_FOR_BUSINESS_RECEIPT'
	| 'WAIT_FOR_SEND'
	| 'WAIT_FOR_PICKUP'
	| 'SHIPPED'
	| 'WAIT_RECEIPT'
	| 'SUCCESS'
	| 'CLOSE'

type IAfsType = 'REFUND' | 'EXCHANGE' | 'RETURN_REFUND'

export interface IAfs {
	closeTime: string
	closeType: ICloseType
	createTime: string
	deadline: string | null
	deleted: boolean | null
	deliveryCode: string | null
	deliveryCompany: string | null
	deliverySn: string | null
	description: string | null
	id: string
	images: string
	isLogistics: boolean
	no: string
	note: string
	orderId: IidType
	productSkuId: IidType
	reason: string | null
	receiptBillId: string | null
	refundAmount: number
	refusalReason: string | null
	refusalTime: string | null
	returnTemplateId: string | null
	shopId: IidType
	status: IAfsStatus
	successTime: string | null
	templateId: IidType
	tenantId: IidType
	type: IAfsType
	updateTime: string
	userAvatarUrl: string
	userId: string
	userName: string
}
