/*
 * @description:
 * @Author: vikingShip
 * @Date: 2021-08-09 17:10:14
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-20 10:02:21
 */
import { LinkSelectItem } from '@/pages/index/modules/home/components/cube-box/cubeBoxType'
/**
 * @LastEditors: vikingShip
 * @description: mall组件state类型
 * @param isFirst 首次标识符
 * @param formData 组件装修数据
 * @param getRightBoxStyle 侧边栏模式商品盒子样式
 * @param getPageStyle 页面样式
 * @param currentFirstCategory 当前编辑一级分类
 * @param isShowLoadNext 侧边栏模式是否显示上拉加载下一个分类页
 * @param upperNextText 加载提示文本
 * @param nextCategoryName 下一个分类页名称
 * @param isComeBottom 是否已经触底
 * @param scrollTopValue 上拉距离
 * @param triggeredStatus 下拉状态
 * @param _freshing 是否正在刷新状态
 * @param safeHight
 */

export interface MallState {
	isFirst: boolean
	formData: string | Partial<BusinessSuper>
	getRightBoxStyle: string
	getPageStyle: string
	currentFirstCategory: Exclude<Partial<FirstCategory>, string>
	hasInfo: boolean
	isShowLoadNext: boolean
	upperNextText: string
	nextCategoryName: string
	isComeBottom: boolean
	scrollTopValue: number
	triggeredStatus: boolean
	_freshing: boolean
	safeHight: number
}
/**
 * @LastEditors: vikingShip
 * @description: SwiperListitem
 * @param title 标题
 * @param img 图片地址
 * @param link 链接地址
 * @param linkname链接名称
 */

export interface SwiperListItem {
	title: string
	img: string
	link: LinkSelectItem
	linkName: string
}
/**
 * @LastEditors: vikingShip
 * @description: 商品
 * @param id
 * @param name 商品名称
 * @param img 商品图片
 * @param endTime 结束时间
 * @param price 商品实售价
 * @param guide 商品指导价
 * @param soldCount 已售量
 * @param inventory 库存
 */
export interface Goods {
	id: number
	name: string
	img: string
	endTime: string
	price: number
	guide: number
	soldCount: number
	inventory: number
}
/**
 * @LastEditors: vikingShip
 * @description: 分类对象
 * @param id 分类Id
 * @param name 分类名称
 * @param parentId 父级Id
 * @param productNumber 商品数量
 * @param showCategoryVos 子集对象
 */
export interface Category {
	id: string
	name: string
	parentId?: string
	productNumber: number
	showCategoryVos?: Array<Category>
}

/**
 * @LastEditors: vikingShip
 * @description: 分类图片
 * @param img 图片地址
 * @param title 标题
 * @param category 分类
 */
export interface Chart {
	img: string
	title: string
	name?: string
	category: Category
	productNumber?: number
}
/**
 * @LastEditors: vikingShip
 * @description: navigation
 * @param name 导航名称
 * @param class 选择分类 二级分类Id
 * @param chartList 导航选择的二级分类
 * @param navType 导航类型 1默认  2新增
 */
export interface Navigation {
	name: string
	class: Array<string>
	chartList: Array<Chart>
	navType: number
}

/**
 * @LastEditors: vikingShip
 * @description: 一级类目
 * @param category 类目对象
 * @param isSelected 是否被选中为当前编辑对象
 * @param classChart 二级分类页图片
 * @param swiperList 海报列表
 * @param navigation 导航信息
 * @param navigationStyle 导航样式 1样式一  2样式二
 */
export interface FirstCategory {
	id: string | number
	category: Category
	isSelected: boolean
	classChart: Array<Chart>
	swiperList: Array<SwiperListItem>
	navigation: Array<Navigation>
	navigationStyle: number
}
/**
 * @LastEditors: vikingShip
 * @description: 商超分类页
 * @param selectMode 选择模式 1顶部模式 2侧边栏模式
 * @param selectType 选择样式 1商品列表 2分类列表
 * @param classChart 二级分类页图片
 * @param groupList 展示分类 已选择的商品分类数组
 * @param firstCatList 所有选择的一级分类列表
 * @param currentFirstCategory 当前编辑的一级分类
 * @param swiperList 临时存储海报列表
 * @param curruntNav 当前编辑的一级分类对象
 * @param navigation 导航信息
 * @param listStyle 列表样式 1大图样式 2列表样式
 * @param pagePadding 页面边距
 * @param fontColor 字体选中
 * @param fontBg 背景选中
 * @param fontNotColor 字体未选中
 * @param fontNotBg 背景未选中
 * @param goodsPadding 商品间距
 * @param goodsStyle 商品样式 1无底白边 2卡片投影 3描边白底
 * @param doodsAngle 图片倒角 1直角 2圆角
 * @param textStyle 文本样式 1常规体 2加粗体
 * @param cartButton 购买按钮样式 3/4
 * @param gList 商品列表
 * @param isSearch 是否是搜索状态 1内容页  2搜索页  3分类页
 * @param secondCategoryId 二级分类页Id
 * @param secondCatGoodsStyle 二级分类商品搜索页面样式 1矩形  2列表
 */
export interface BusinessSuper {
	selectMode: number
	selectType: number
	classChart: Array<Chart>
	groupList: Array<Category>
	firstCatList: Array<FirstCategory>
	currentFirstCategory: FirstCategory
	swiperList: Array<SwiperListItem>
	curruntNav: Category
	navigation: Navigation[]
	listStyle: number
	pagePadding: number
	fontColor: string
	fontBg: string
	fontNotColor: string
	fontNotBg: string
	goodsPadding: number
	goodsStyle: number
	doodsAngle: number
	textStyle: number
	cartButton: number
	goods: Goods
	gList: Goods[]
	isSearch: number
	secondCategoryId: string
	secondCatGoodsStyle: number
	saleMode?: number | string
}

export interface ICategoryVo extends Partial<Category> {
	hasNextCatgory: boolean
	hasUpperCatgory: boolean
	upperCatgory?: Category
}
