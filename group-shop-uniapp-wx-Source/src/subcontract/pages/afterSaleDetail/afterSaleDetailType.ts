import { IGoodsList } from '@/typings/goods'
import { String } from 'lodash'

/*
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-18 16:22:31
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-20 11:22:23
 * 123
 */
export interface IAfterDetail {
	address: IAfterDetailAddress
	applyNum: number
	closeTime: string
	closeType: String
	createTime: string
	deadline: string
	deleted: boolean
	deliveryCode: string
	deliveryCompany: string
	deliverySn: string
	description: string
	id: string
	images: string[]
	isLogistics: boolean
	item: IAfterDetailItem
	no: string
	note: string
	orderId: string
	originalOrder: IAfterOrderDetail
	productSkuId: string
	reason: string
	receiptBillId: string
	refundAmount: number
	refusalReason: string
	refusalTime: string
	returnTemplateId: string
	shopId: string
	status: string
	successTime: string
	templateId: string
	tenantId: string
	type: string
	updateTime: string
	userAvatarUrl: string
	userId: string
	userName: string
	steps?: ISteps
}

export type IAfterDetailAddress = Record<'address' | 'name' | 'phone', string>

export interface IAfterDetailItem {
	afsId: string
	createTime: string
	deleted: boolean
	id: string
	orderArea: number
	orderId: string
	productId: string
	productName: string
	productPic: string
	productPrice: number
	productQuantity: number
	productSkuId: string
	refundAmount: number
	refundIntegration: number
	shopId: string
	specs: string
	tenantId: string
	updateTime: string
}

export interface IAfterOrderDetail {
	activityIds?: string
	closeTime?: string
	commentTime?: string
	completeTime?: string
	couponAmount: number
	couponId: string
	createTime: string
	customForm: string
	deleted: boolean
	discountsAmount: number
	estimatedDeliveryTime?: string
	expireDate?: string
	expireTime: string
	freightAmount: number
	fullScaleAmount: number
	fullScaleId?: string
	giftIntegration: number
	id: string
	integration: number
	integrationAmount: number
	memberAmount: number
	memberTemplateId: string
	note: string
	orderDelivery: IAfterOrderDetailDelivery
	orderItemList: IGoodsList[]
	payAmount: number
	payTime: string
	payType: string
	privileges?: string
	productTotalQuantity: number
	promotionAmount: number
	rebateAmount: number
	refundAmount?: number
	refundQuantity?: number
	refundTransactionId?: string
	shopId: string
	solitaireActivityId?: string
	sourceType: string
	status:
		| 'WAIT_FOR_SEND'
		| 'WAIT_FOR_PICKUP'
		| 'WAIT_RECEIPT'
		| 'WAIT_FOR_BUSINESS_RECEIPT'
	tenantId: string
	totalAmount: number
	transactionId?: string
	type: 'MALL'
	updateTime: string
	userAvatarUrl: string
	userId: string
	userName: string
	userNote: string
	applyNumHasExceeded?: boolean
	rBtnStatus?: boolean
	freightAmountstr?: string
}

export interface IAfterOrderDetailDelivery {
	createTime: string
	deleted: false
	deliveryCode?: string
	deliveryCompany?: string
	deliverySn?: string
	deliveryTemplateId?: string
	deliveryTime?: string
	deliveryType: string
	orderId: string
	packageCode?: string
	packageName?: string
	receiveTime?: string
	received: false
	receiverCity: string
	receiverDetailAddress: string
	receiverName: string
	receiverPhone: string
	receiverPostCode: string
	receiverProvince: string
	receiverRegion: string
	shopId: string
	sortingCode?: string
	tenantId: string
	updateTime: string
	id?: string
}

export type IDeliveryInfo = Record<
	'deliverySn' | 'deliveryCompany' | 'deliveryCode',
	string
>

export type ISteps = Record<'address' | 'context' | 'time', string>

export type Idelivery = Record<
	'tracesId' | 'companyName' | 'deliveryCode',
	string
>

export interface IQueryStr {
	title: string
	type: string
	orderId: string
	productSkuId: string
	goods: string
	productQuantity: number
	refundAmount: number
	userType: string
	hasPicked: number
	[x: string]: string | number
}
