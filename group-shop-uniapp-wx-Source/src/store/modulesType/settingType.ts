/*
 * @description:
 * @Author: vikingShip
 * @Date: 2021-08-05 11:38:19
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-18 15:15:14
 */
import { DecorationType } from '@/pages/index/modules/home/homeType'
import { ApiCenterAggregation } from '@/pages/index/modules/me/meType'
import { IidType } from '@/typings/global'
import { ShopInfoType } from '../modulesType/shopsetType'
/**
 * @LastEditors: vikingShip
 * @description: settingModule中state类型
 * @param shopSetting 店铺设置
 * @param isReady 是否准备完毕
 * @param guideVisible 引导页是否显示
 * @param linkPageId 当前自定义页面
 * @param tabVisible 首页tab显示隐藏 主要为了解决cover-view 层级最高的问题
 * @param currentTab 当前选中tab
 * @param searchName 搜索页面定义搜索词
 * @param currentPageId 当前自定义页面id
 * @param homeTab homeTab的信息obj（跳转页面前判断type==5为自定义页面，需要将store中currentPageId置为homeTab的id,然后跳转;非type==5,跳转linkUrl的地址）
 * @param templateId 使用中的模板
 * @param newCurrentPageId 重新请求自定义装修数据的id
 * @param pageList 页面列表
 * @param iftransFrom 是否转发进入
 * @param hasInCustom 是否进入过自定义页面
 * @param customName 自定义页面名称
 * @param customPageId 获取需要进入的自定义页面的新id
 * @param indexTitle 首页标题
 * @param isReSet 重新设置底部选中对象
 * @param formShopCar 购物车规格弹窗息屏问题
 * @param order 订单信息
 * @param navigationBarHeight 顶部导航栏高度
 * @param tabBar tabBar数据
 * @param components 自定义页面控件数据
 * @param spellPage 装修数据-拼团分类页
 * @param businessSuper 装修数据-商超分类页
 * @param userCenter 个人中心配置数据
 * @param notSwitchTab 判断是不是从底部导航跳转的
 * @param historyPage 跳转自定义和首页的历史纪录
 * @param guideList 引导页数据
 * @param newCurrentPage 新页面
 * @param saleMode 专区id
 * @param modeType 分类页所在专区 商超专区1
 * @param customPageName 自定义分类页标题
 * @param centerTabbarItem 自定义分类页标题
 */
export interface ModuleSettingType {
	shopSetting: IShopSetting
	isReady: boolean
	guideVisible: boolean
	linkPageId: string | number
	tabVisible: boolean
	currentTab: string
	searchName: string
	currentPageId: string | number
	homeTab: PluginProMenuList
	templateId: string | number
	newCurrentPageId: string | number
	pageList: DecorationPageType[]
	iftransFrom: boolean
	hasInCustom: boolean
	customName: string
	customPageId: string | number
	indexTitle: string
	isReSet: boolean
	formShopCar: boolean
	order: IOrderSetting
	navigationBarHeight: number
	tabBar: Partial<TabBarType>
	components: DecorationType[]
	spellPage: DecorationComponent
	businessSuper: DecorationComponent
	userCenter: ApiCenterAggregation
	notSwitchTab: number
	historyPage: LinkSelectItem[]
	guideList: GuideListItemType[]
	newCurrentPage: any
	saleMode: string | number
	modeType: number
	customPageName: string
	centerTabbarItem: LinkSelectItem & {
		centerNum: number
	}
}
/**
 * @LastEditors: vikingShip
 * @description: 店铺引导页数组
 * @param appId 跳转的小程序id
 * @param link path名称 用于比较
 * @param path 路径
 * @param type 0为path 1为app_id
 * @param url 网址
 */
export interface GuideListItemType {
	appId: string
	id: number
	link: string
	path: string
	type: number
	url: string
}
/**
 * @LastEditors: vikingShip
 * @description: shop设置配置信息
 * @param hasOpenEvaluate 是否开启评价
 * @param curShopInfoDto 当前店铺信息
 */
export interface IShopSetting {
	hasOpenEvaluate: boolean
	curShopInfoDto: Pick<ShopInfoType, 'shopName'>
	mergePoint?: boolean
	appMode?: string
}

/**
 * @LastEditors: vikingShip
 * @description: 装修聚合接口Api类型
 * @param tenantId 租户Id
 * @param shopId 商城ID
 * @param page 可装修页面组件数组
 * @param plugins 装修插件数组
 */

export interface DecorationPolymerizeType {
	id: number | string
	tenantId: string | number
	shopId: string | number
	pages: DecorationPageType[]
	plugins: DecorationPluginType[]
	colour: string
	createTime: string
	isDeleted: string
	isDevTemplate: string
	name: string
	onlineStatus: string
	operatorId: IidType
	operatorName: null | string
	type: IidType
	updateTime: string
}
/**
 * @LastEditors: vikingShip
 * @description: 可装修页面组件数组
 * @param templateId 模板ID
 * @param pageName 页面名称
 * @param assemblyVos 组件数组
 */
export interface DecorationPageType {
	id: number | string
	templateId: string | number
	pageName: string
	assemblyVos: DecorationComponent[]
	isDef: string
	type: string
	createTime: string
	isDeleted: string
	shopId: string
	tenantId: string
	updateTime: string
}

/**
 * @LastEditors: vikingShip
 * @description: 装修模板组件类型
 * @param properties 组件json内容
 */
export type DecorationComponent = {
	properties: string
	value: string
	createTime: string
	id: number
	isDeleted: string
	pageId: number
	tenantId: string
	updateTime: string
}

/**
 * @LastEditors: vikingShip
 * @description:
 * @param templateId 模板ID
 * @param pluginProperties 插件json内容
 * @param pluginNameCn 插件名称
 */
export interface DecorationPluginType {
	templateId: string | number
	pluginProperties: string | DecorationPluginProp[]
	pluginNameCn: string
	pluginNameEn: string
	createTime: string
	id: number
	isDeleted: string
	isMandatory: string
	isSelection: string
	shopId: string
	tenantId: string
	updateTime: string
}

/**
 * @LastEditors: vikingShip
 * @description: 插件properties解析类型
 * @param value 插件组件名称
 * @param formData 插件配置集合
 * @param defaultColor 默认颜色
 * @param selectColor 选择颜色
 * @param codeStyle 排版样式
 */
export interface DecorationPluginProp {
	value: string
	formData: DecorationPluginForm
}
export interface DecorationPluginForm {
	menuList: PluginProMenuList[]
	defaultColor: string
	selectColor: string
	codeStyle: number
}
/**
 * @LastEditors: vikingShip
 * @description: 插件元素
 * @param selectedIconPath 选中icon路径
 * @param linkUrl 链接地址
 * @param isHome 是否是主页标识
 * @param text 插件元素文字描述
 * @param iconPath 元素icon地址
 * @param linkName 链接描述
 */

export interface PluginProMenuList extends LinkSelectItem {
	id: number
	selectedIconPath: string
	linkUrl: string
	isHome: boolean
	text: string
	iconPath: string
	linkName: string
	iconType: number
	type: number
	name: string
}
/**
 * @LastEditors: vikingShip
 * @description: 订单设置
 * @param customFrom 自定义表单
 * @param kdAppId 快递AppId
 * @param kdAppKey 快递AppKey
 * @param orderNotify 是否开启下单通知：0->关闭；1->开启
 * @param paymentModel 支付方式：1->微信支付;
 * @param shopId 商铺id
 * @param afsApplyNumber 最大售后申请次数
 * @param commentOvertime 订单完成后自动好评时间（天）
 * @param confirmOvertime 发货后自动确认收货时间（天）
 * @param deleted 删除状态：0->未删除；1->已删除
 * @param finishOvertime 自动完成交易时间，不能申请售后（天）
 * @param flashOrderOvertime 秒杀订单超时关闭时间(分)
 * @param openEvaluate 是否开启订单评论
 * @param tenantId 租户ID
 * @param userReturnOvertime 用户最大退货期限(天)
 * @param merchantConfirmOvertime 商家最大审核期限(天)
 * @param normalOrderOvertime 正常订单超时时间(分)
 */
export interface IOrderSetting {
	customFrom: string
	id: number
	kdAppId: string
	kdAppKey: string
	orderNotify: true
	paymentModel: string
	shopId: string
	supportAllowedDay: number
	supportAuditDay: number
	type: string
	afsApplyNumber: number
	commentOvertime: number
	confirmOvertime: number
	deleted: boolean
	finishOvertime: number
	flashOrderOvertime: number
	userReturnOvertime: number
	merchantConfirmOvertime: number
	normalOrderOvertime: number
	openEvaluate: boolean
	tenantId: string
}

/**
 * @LastEditors: vikingShip
 * @description: tabbar配置
 * @param mode 模式
 * @param color 颜色
 * @param selectedColor 选中颜色
 * @param background 北京
 * @param selectColor 未选中颜色
 * @param defaultColor 默认颜色
 */
export interface TabBarType {
	mode: string
	color: string
	selectedColor: string
	background: string
	list: LinkSelectItem[]
	selectColor: string
	defaultColor: string
}

/**
 * @LastEditors: vikingShip
 * @description: 绑定单个元素链接描述信息
 * @param type 链接类型 0 -功能页面 FunctionPage 1-商超商品 Goods 2 -展示分类 DisplayClassify 3-自定义页面 CustomPage 4 活动营销 ActivityMarket
 * @param name 链接名称
 * @param url 链接地址
 * @param append 附加参数
 */

export interface LinkSelectItem {
	id: number
	type: number
	name: string
	url: string
	append: string
	[x: string]: any
}
