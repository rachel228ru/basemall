/*
 * @description:
 * @Author: vikingShip
 * @Date: 2021-08-04 10:35:03
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-19 09:42:34
 */
/**
 * @LastEditors: vikingShip
 * @description: shopCar的state
 * @param shopCarList 购物车商品数组
 * @param temShopList 购物车商品数组
 * @param shopType 购物车顶部模式标识
 * @param shopLoseCarList 失效商品数组
 * @param editFlag 编辑状态标识
 * @param selectAll 全选状态
 * @param selectList 选择数组
 * @param showType 弹窗状态
 * @param goodDetail 选中商品
 * @param norms 选中商品所选规格
 * @param allPrice 商品总价
 * @param isPhone11 iPhone11 设置底部按钮
 * @param isAddCollect 是否添加收藏
 * @param canClick 加载中禁用
 */
export interface shopCarState {
	shopCarList: Array<GoodInfo>
	temShopList: Array<GoodInfo>
	shopType: boolean
	shopLoseCarList: Array<GoodInfo>
	editFlag: boolean
	selectAll: boolean
	selectList: Array<GoodInfo>
	showType: boolean
	goodDetail: GoodInfo
	norms: ApiSkuType
	allPrice: string
	isPhone11: number | boolean
	isAddCollect: boolean
	canClick: boolean
	pageHeight: number
}
/**
 * @LastEditors: vikingShip
 * @description: 购物车商品字段类型
 * @param activityEndTime 活动结束时间
 * @param activityId  活动id
 * @param activityStartTime  活动开始时间
 * @param activityStatus  活动状态 0-为开始 1-进行中 2-失效
 * @param deleted  删除状态(0–未删除，1–已删除)
 * @param distributionMode  配送方式(0–商家配送，1–快递配送)
 * @param goodsNumber  商品数量
 * @param id 购物车商品自增长id
 * @param pic 展示图片
 * @param productId 产品id
 * @param productName 商品名称
 * @param productSn 货号
 * @param saleMode 销售专区(默认商超系统，0–商超系统，1–社群拼团，2–限时秒杀)
 * @param selectStatus  选中状态(0–未选中，1–已选中)
 * @param skuId  skuId
 * @param skuStocks  商品sku信息
 * @param sortTime  sortTime
 * @param status  状态(默认上架，0–下架（仓库中），1–上架，2–已售完)
 * @param userId   用户id
 */
export interface GoodInfo {
	activityEndTime: string
	activityId: number
	activityStartTime: string
	activityStatus: string
	deleted: number
	distributionMode: number
	goodsNumber: number
	id: number
	pic: string
	productId: number
	productName: string
	productSn: string
	saleMode: number
	selectStatus: number
	skuId: number
	skuStocks: ApiSkuType[]
	sortTime: string
	status: number
	userId: string
	isCheck?: boolean
	itemIndex?: number
	getType?: boolean
	originalPrice?: number | string
	actPrice?: number | string
	activityProductId?: number | string
	dominoState?: number | string
	price?: number | string
	skuList?: number[]
	perLimit?: number
	checkStock?: number
	skuName?: string
}

/**
 * @LastEditors: vikingShip
 * @description: 商品sku
 * @param id
 * @param lowStock 预警库存
 * @param originalPrice 指导价
 * @param perLimit 限购数量
 * @param pic 展示图片
 * @param price 实售价
 * @param productId 产品id
 * @param rebateType 返利价格类型 0-不使用 1-价格 2-比例 3-使用全局返利设置
 * @param sale 销量
 * @param skuCode sku编码
 * @param skuRebates 商品返利信息
 * @param specs 商品规格
 * @param stock 库存
 * @param version 版本号
 * @param weight 商品sku重量
 */

export interface ApiSkuType {
	id: number
	lowStock: number
	originalPrice: number | string
	perLimit: number
	pic: string
	price: number | string
	productId: string
	rebateType: number
	sale: number
	skuCode: string
	skuRebates: ApiRebate[]
	specs: string
	stock: number
	version: number
	weight: number
	maxNum: number
	getType?: boolean
	memPrice?: number | string
	intPrice?: number | string
	smaPrice?: number | string
}

/**
 * @LastEditors: vikingShip
 * @description: 商品返利
 * @param id
 * @param productId 产品id
 * @param rebatePrice 返利金额
 * @param skuId 产品sku-id
 */
type ApiRebate = Record<'id' | 'productId' | 'rebatePrice' | 'skuId', number>

export interface IAddCar {
	productId: number
	productName: string
	productPic: string
	productPrice?: string | number
	status: number
	originalPrice?: string | number
}
