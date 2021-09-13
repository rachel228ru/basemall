/*
 * @description:
 * @Author: vikingShip
 * @Date: 2021-08-10 14:05:36
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-20 09:28:37
 */
import { ApiSkuType, GoodInfo } from '@/pages/index/modules/shopCar/shopCarType'
import { ShopInfoType } from '@/store/modulesType/shopsetType'
/**
 * @LastEditors: vikingShip
 * @description: 商品详情组件state
 * @param goodDetail 商品详情接口返回参数
 * @param norms 商品规格
 * @param buyShow 购物车 立即购买弹窗
 * @param show 优惠券显隐标识符
 * @param paraShow 参数弹窗查看
 * @param version 版本
 * @param shareVisible 生成卡片标识
 * @param shareImgUrl 分享图片地址
 * @param shareImgUrl 分享缩略图
 * @param cardImgUrl 分享卡片图片地址
 * @param cardFlag 分享卡片显隐标识
 * @param showAll 分享卡片显隐标识
 * @param goodId 商品id
 * @param goodsVideoUrl 商品视频
 * @param videoImg 视频封面
 * @param autoplay 是否自动播放
 * @param current 轮播图路径
 * @param showCountDown 是否有倒计时
 * @param countDetail count详情
 * @param remainTime 组件vant-count的倒计时总数
 * @param authType 是否登陆标识
 * @param evaluateOverview 评价概况
 * @param shopCarNum  购物车数量
 * @param clickShare 是否点击分享
 * @param shopInfo  店铺信息
 * @param menu  菜单显示
 */
export interface DetailState {
	goodDetail: GoodDetailInfo
	norms: ApiSkuType
	buyShow: boolean
	show: boolean
	paraShow: boolean
	version: string
	shareVisible: boolean
	shareImgUrl: string
	imgUrl: string
	cardImgUrl: string
	cardFlag: boolean
	showAll: boolean
	goodId: string
	goodsVideoUrl: string
	videoImg: string
	autoplay: boolean
	current: number
	videoContext: UniApp.VideoContext
	countDetail: InterCountDetail
	remainTime: number
	authType: boolean
	evaluateItem: EvaluateItemType
	evaluateOverview: Array<EvaluateOverviewType>
	shopCarNum: number
	clickShare: boolean
	shopInfo: Partial<ShopInfoType>
	menu: boolean
}

/**
 * @LastEditors: vikingShip
 * @description: 商品详情商品所有字段信息
 * @param albumPics 画册图片，连产品图片限制为6张，以逗号分割
 * @param attribute 销售属性
 * @param attributeId 属性模版ID
 * @param attributeName 属性模板名称
 * @param commissionType 佣金类型(0–金额，1–比例)
 * @param detail 商品详情
 * @param distributionMode 配送方式(0–商家配送，1–快递配送 2–同城配送)
 * @param freightTemplateId 运费模板ID
 * @param limitType 限购类型(默认统一规格，0–统一规格，1–统一限购，2–规格限购)
 * @param name 商品名称
 * @param openSpecs 规格是否展开
 * @param pic 图片地址
 * @param productSn 货号
 * @param providerId 供应商id
 * @param providerName 供应商名称
 * @param sale 销量
 * @param saleMode 销售专区(默认商超系统，0–商超系统，1–社群拼团，2–限时秒杀)
 * @param score 评分
 * @param serviceIds 以逗号分割的产品服务：1->无忧退货；2->快速退款；3->免费包邮
 * @param status 状态(默认上架，0–下架，1–上架)
 * @param useMemberPrice 是否使用会员价(0–不使用, 1–固定金额, 2–固定百分比)
 * @param useRebate 是否使用返利(0–不使用, 1–固定金额, 2–固定百分比)
 * @param widePic 宽屏展示图片
 */

export interface GoodDetailInfo extends GoodInfo {
	albumPics: string
	attribute: string
	attributeId: number
	attributeName: string
	commissionType: number
	detail: string
	distributionMode: number
	freightTemplateId: number
	id: number
	limitType: number
	name: string
	openSpecs: boolean
	pic: string
	productAttributes: Array<ProductAttributeType>
	productSn: string
	providerId: number
	providerName: string
	sale: number
	saleMode: number
	score: number
	serviceIds: string
	skuStocks: ApiSkuType[]
	status: number
	useMemberPrice: number
	useRebate: number
	widePic: number
	imgUrls?: Array<string>
	saleDescribe: string
	isCollect: boolean
	memPrice: number
	launchArea: any
}

/**
 * @LastEditors: vikingShip
 * @description: 商品属性
 * @param name 属性名称
 * @param productId 产品id
 * @param value 属性值
 */
export interface ProductAttributeType {
	id: number
	name: string
	productId: number
	value: string
}
export interface TimeDataType {
	hours: Array<string | number>
	minutes: Array<string | number>
	seconds: Array<string | number>
}
export interface InterCountDetail {
	status: string
	toBeginTime: number
	toEndTime: number
	remainTime: number
}
/**
 * @LastEditors: vikingShip
 * @description: 评价item类型
 * @param comment 评论内容
 * @param createTime 评价时间
 * @param picture 上传的图片
 * @param rate 商品评分
 * @param reply 卖家回复
 * @param replyTime 回复时间
 * @param specs 商品的销售属性
 * @param userAvatarUrl 用户头像
 * @param userId 用户id
 * @param userName 用户帐号
 */
export interface EvaluateItemType {
	comment: string
	createTime: string
	id: number
	picture: string
	rate: number
	reply: string
	replyTime: string
	specs: string
	userAvatarUrl: string
	userId: string
	userName: string
	pictureArr?: string[]
}
/**
 * @LastEditors: vikingShip
 * @description: 评价概况
 * @param all 所有的
 * @param hasContent 有内容的
 * @param hasContent 有图片的
 * @param praiseRate 好评率
 * @param total 总条数
 */

export type EvaluateOverviewType = Record<
	'all' | 'hasContent' | 'hasPicture' | 'praiseRate' | 'total',
	number
>
