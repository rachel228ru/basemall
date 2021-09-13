/*
 * @description:
 * @Author: vikingShip
 * @Date: 2021-08-10 18:03:52
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-18 20:22:34
 */
import { GoodInfo } from '@/pages/index/modules/shopCar/shopCarType'
import { ApiSkuType } from '@/pages/index/modules/shopCar/shopCarType'
/**
 * @LastEditors: vikingShip
 * @description:
 * @param payTypes 支付方式
 * @param deliveryActions 配送方式
 * @param forms 自定义表单
 * @param itemVoList 商品列表
 * @param miniAccountAddress 地址
 * @param addressIndex 地址索引
 * @param realPrice 真实价格
 * @param totalPrice 总价
 * @param isFirst 是否第一次进入
 * @param address 选择地址页面选择后会设置这个值
 * @param haSubscribed 是否已订阅标识
 * @param hasSubmited 是否已经提交表单标识
 * @param prevRoute 上级路由，用于判断是否刷新数据
 * @param useRebate  是否开启返利
 */
export interface SubmitStateType {
	payTypes: PayType[]
	deliveryActions: Array<{ name: string; value: string }>
	forms: Array<CustomFormType>
	form: SubmitOrderType
	offerOptions: OfferOptionsType
	itemVoList: IProcuct[]
	miniAccountAddress: IAdress[]
	addressIndex: number
	realPrice: number
	totalPrice: number
	isFirst: boolean
	address: IAdress
	haSubscribed: boolean
	hasSubmited: boolean
	prevRoute: string
	useRebate: boolean
}

export interface PayType {
	name: string
	value: string
	icon: string
}

/**
 * @LastEditors: vikingShip
 * @description:
 * @param key 关键词
 * @param type 类型
 * @param required 是否必传
 * @param placeholder 提示信息
 */

export interface CustomFormType {
	key: string
	type: string
	required: boolean
	placeholder: string
	value?: string | number
}

/**
 * @LastEditors: vikingShip
 * @description: 提交订单信息
 * @param customForm 自定义表单
 * @param deliverType 配送方式 SELF_PICKUP, HOME_DELIVERY, LOGISTICS
 * @param formId formId
 * @param groupLeaderId groupLeaderId
 * @param miniAccountAddressId  收货人地址ID
 * @param userNote  用户留言
 * @param orderType  订单类型
 * @param payType  支付方式
 */
export interface SubmitOrderType {
	customForm: string
	deliverType: string
	formId: string
	groupLeaderId: string
	itemDtoList: any[]
	miniAccountAddressId: string
	userNote: string
	orderType: string
	payType: string
	sourceType: string
}

/**
 * @LastEditors: vikingShip
 * @description: 优惠选项
 * @param freightAmount 配送费
 */
export interface OfferOptionsType {
	freightAmount: number
}

/**
 * @LastEditors: vikingShip
 * @description: 地址类型
 * @param id 地址id
 * @param userName 收货人姓名
 * @param phone 收货人联系电话
 * @param postCode 编码
 * @param isDefault 是否为默认地址 0-非默认 1-默认
 * @param province 省
 * @param city 市
 * @param county 区
 * @param detailInfo 详细收货地址信息
 * @param userId 用户id
 * @param location 经度,维度
 */
export interface IAdress {
	tenantId: string
	createTime: string
	updateTime: string
	deleted: boolean
	id: string | number
	userName: string
	phone: string
	postCode: string
	isDefault: number
	province: string
	city: string
	county: string
	detailInfo: string
	userId: string
	location: string
	lngLat: string
}

export interface Ipoint {
	type: number
	name: string
	lineId: string
	lineName: string
	communityId: string
	communityName: string
	provinceId: string
	provinceName: string
	cityId: string
	cityName: string
	districtId: string
	districtName: string
	lngLat: string
	author: string
	phone: string
	address: string
	pointId: string
	img: string[]
	range: number
	baseMoney: number
	shippingMoney: number
	freeMoney: number
	pointUserId: string
}

export interface IProcuct
	extends GoodInfo,
		Pick<ApiSkuType, 'skuRebates' | 'rebateType'> {
	distributionMode: 0 | 1
	productId: number
	productPic: string
	productName: string
	productSn: string
	productPrice: number
	productOriginalPrice: number
	productQuantity: number
	productSkuId: string
	productSkuCode: any
	productSkuPic: string
	giftIntegration: number
}

export interface IRebateSet {
	tenantId: string
	createTime: string
	updateTime: string
	deleted: boolean
	id: string
	shopId: string
	rebateRatio: number
	balanceUseCondition: string
	rebateCondition: string
}
