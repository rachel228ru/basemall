/*
 * @description:
 * @Author: vikingShip
 * @Date: 2021-08-02 16:25:51
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-18 20:49:32
 */

/**
 * @LastEditors: vikingShip
 * @description: 商品组件state
 * @param formData 父级传参
 * @param currentCategoryId 当前商品分类Id
 * @param GoodsCornerMark   商品角标地址及样式
 * @param goodsList   已经筛选后商品数组
 * @param GoodsNameClass   商品名称和样式
 * @param GoodsBoxClass   商品边框样式
 * @param ClassBugCard3   购买按钮样式
 * @param GoodsItemStyle   商品item样式
 * @param GoodsLastStyle   最后一个商品样式
 * @param saleMode   选择的商品专区
 */

export interface GoodState {
	formData: GoodProp
	currentCategoryId: string
	GoodsCornerMark: IGoodsCornerMark
	goodsList: Array<any>
	GoodsNameClass: string
	GoodsBoxClass: string
	ClassBugCard3: string
	GoodsItemStyle: string
	GoodsLastStyle: string
	saleMode: string
}

/**
 * @LastEditors: vikingShip
 * @description: 父级传参类型
 * @param ponentType 1展示分类  2不展示分类
 * @param goods 商品
 * @param titleStyle 类目样式 1新  2下划线
 * @param listStyle 列表样式
 * @param pagePadding 页面边距
 * @param goodPadding 商品间距
 * @param goodsStyle 商品样式
 * @param angle 图片倒角
 * @param textStyle 文本样式
 * @param showContent 显示内容
 * @param goodsItem 商品对象
 * @param goodsCount 商品数量
 */

export interface GoodProp {
	ponentType: number
	goods: GoodItem[]
	titleStyle: number
	listStyle: string
	pagePadding: number
	goodPadding: number
	goodsStyle: string
	angle: string
	textStyle: string
	showContent: IShowContent
	goodsItem: Partial<GoodItem>
	goodsCount: number
	firstCatList: Array<firstCatItem>
	currentCategoryId?: '6'
	goodsTemp?: GoodsTemp
}

interface GoodsTemp {
	deliveryTime: string
	endTime: string
	guide: number
	id: number
	img: string
	inventory: number
	name: string
	price: number
	soldCount: number
}
/**
 * @LastEditors: vikingShip
 * @description: 商品角标地址及样式
 * @param url 地址
 * @param class 样式
 */
export interface IGoodsCornerMark {
	url: string
	class: string
}

type GoodItem = Record<
	'albumPics' | 'id' | 'img' | 'name' | 'price' | 'saleMode',
	string
>

/**
 * @LastEditors: vikingShip
 * @description:
 * @param name  名称
 * @param productNumber  商品数量
 * @param saleMode  选择的商品专区
 * @param showCategoryVos  二级选项
 */
export interface firstCatItem {
	id: string
	name: string
	productNumber: number
	saleMode: string
	showCategoryVos: Array<firstCatItemSec>
}

export type firstCatItemSec = Pick<
	firstCatItem,
	'id' | 'name' | 'productNumber'
>

/**
 * @LastEditors: vikingShip
 * @description:
 * @param nameShow 商品名称显示标识
 * @param priceShow 商品价格显示标识
 * @param buttonShow 购物按钮显示标识
 * @param buttonStyle 购物按钮样式
 * @param buttonText 购物车按钮文案
 * @param tagShow 商品角标
 * @param tagStyle 商品角标样式
 */
interface IShowContent {
	nameShow: boolean
	priceShow: boolean
	buttonShow: boolean
	buttonStyle: number
	buttonText: string
	tagShow: boolean
	tagStyle: number
}

export interface IProductList {
	albumPics?: string
	createTime?: string
	distributionMode?: number
	id: string | number
	inventory?: number
	limitType?: number
	maxPrice?: number | string
	minPrice?: number
	name: string
	pic?: string
	productSn?: string
	sale?: string
	widePic?: string
	img?: string
	price?: number | string
	actPrice?: number
	soldCount?: string
	hasTimeTips?: boolean
	goodTime?: string
}
