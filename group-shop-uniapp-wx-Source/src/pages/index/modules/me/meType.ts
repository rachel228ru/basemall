/*
 * @description:
 * @Author: vikingShip
 * @Date: 2021-08-04 14:35:35
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-18 15:09:29
 */

import { LinkSelectItem } from '@/store/modulesType/settingType'

export interface MeState {
	userInfo: UserInfo
	userCenter: UserCenterType
	customStyle: CustomStyle
	orderInfo: UserInfoVoPick
	expandMenu: MenuList[]
	gridMenu: MenuList[]
	qrcodeShow: boolean
	authType: boolean
	headStyleConfig: CustomStyle
	scrollHeight: number
	scrollType: boolean
	/** 收藏数量 */
	collectNum: number
	/** 足迹数量 */
	footNum: number
	// 是否开启评价
	hasOpenEvaluate: boolean
	orderBadgeInfo: OrderBadgeType
	APP_VERSION: string | number
}

/**
 * @LastEditors: vikingShip
 * @description: 个人中心聚合接口返回类型
 * @param accountCenterVo 个人中心配置信息
 * @param accountFootMarkCount 个人中心用户足迹数量
 * @param collectCount 个人中心用户收藏数量
 */
export interface ApiCenterAggregation {
	accountCenterVo: UserCenterType
	accountFootMarkCount: number
	collectCount: number
	orderOverviewVo: OrderBadgeType
	userInfoVo: UserInfoVoType
}

/**
 * @LastEditors: vikingShip
 * @description: 用户信息类型
 * @param avatarUrl 头像url
 * @param city 所在城市
 * @param country 所在国家
 * @param firstLoginTime 首次登陆小程序时间
 * @param gender 性别 0：未知、1：男、2：女
 * @param infoExtends 用户扩展信息
 * @param language 所用的语言 en 英文 - zh_CN 简体中文 -zh_TW 繁体中文
 * @param nickName 用户名称
 * @param phone 手机号码
 * @param province 所在省份
 * @param shopUserId 用户店铺id
 * @param whetherAuthorization 是否授权过小程序 false 未授权 true授权过
 */
export interface UserInfo {
	avatarUrl: string
	city: string
	country: string
	firstLoginTime: string
	gender: number
	infoExtends: InfoExtendsType
	language: string
	nickName: string
	phone: string
	province: string
	shopUserId: string
	whetherAuthorization: boolean
}

/**
 * @LastEditors: vikingShip
 * @description: 用户信息拓展类型
 * @param consumeNum 消费次数
 * @param isBlacklist 是否黑名单用户 0 - 否 1 - 是
 * @param lastChooseLcation 用户最后一次选择的地理位置经纬度 经纬度, 经度在前维度在后逗号分隔
 * @param lastDealTime 最后交易时间
 * @param lastLoginTime 最后登录时间
 * @param shopId 商铺id
 * @param shopUserId 店铺用户id
 */
export interface InfoExtendsType {
	consumeNum: number
	isBlacklist: number
	lastChooseLcation: string
	lastDealTime: string
	lastLoginTime: string
	shopId: string
	shopUserId: string
}

/**
 * @LastEditors: vikingShip
 * @description: 个人中心配置数据
 * @param customStyle 自定义风格样式,json存储
 * @param headStyle 头部风格 1-系统风格 2-自定义风格
 * @param id 用户中心数据id
 * @param menuStyle 菜单选择样式 1-展开式 2-折叠式
 * @param menuVos 菜单列表
 * @param orderInfo 订单icon信息 json存储
 */

export interface UserCenterType {
	customStyle: string
	headStyle: number
	id: number
	menuStyle: number
	menuVos: MenuList[]
	orderInfo: string
}

/**
 * @LastEditors: vikingShip
 * @description: 菜单
 * @param allowUse 是否可用 0不可用 1-可用
 * @param defaultIcon 默认图标地址
 * @param hideMenu 菜单是否 0隐藏1显示
 * @param id 菜单id
 * @param linkSelectItem 跳转类型 0 -功能页面 1-商超商品 2 -展示分类 3-自定义页面 4 活动营销
 * @param menuIconUrl 菜单当前图标url
 * @param menuName 菜单名称
 * @param pid 父级id
 * @param sortIndex 排序位置
 * @param splitFlag 分隔
 * @param styleType 菜单样式类别 1-展开式 2-折叠式
 * @param subMenu 子菜单
 */
export interface MenuList {
	allowUse: number
	defaultIcon: string
	hideMenu: boolean
	id: number
	linkSelectItem: string | LinkSelectItem
	menuIconUrl: string
	menuName: string
	pid: number
	sortIndex: number
	splitFlag: boolean
	styleType: number
	subMenu: SubMenuList[] | null
}

export interface SubMenuList {
	id: number
	menuName: string
	menuIconUrl: string
	defaultIcon: string | null
	styleType: number
	sortIndex: number
	hideMenu: boolean
	allowUse: number
	linkSelectItem: string
	splitFlag: boolean
	subMenu: null
	pid: number
}

/**
 * @LastEditors: vikingShip
 * @description: 个人中心字体背景图配置
 * @param backgroundImage 背景图片
 * @param cardColor 卡面颜色
 * @param textColor 文字颜色
 */
export type CustomStyle = Partial<
	Record<
		'backgroundImage' | 'cardColor' | 'textColor' | 'headBackGround',
		string
	>
>

/**
 * @LastEditors: vikingShip
 * @description: 个人中心订单标记数量
 * @param shipped 配送中订单
 * @param waitForPay 待付款订单
 * @param waitForPickup 待提货订单
 * @param withDelivery 待发货订单
 */
export type OrderBadgeType = Record<
	'shipped' | 'waitForPay' | 'waitForPickup' | 'withDelivery',
	number | string
>

/**
 * @LastEditors: vikingShip
 * @description: 积分商城、签到、计步 开起开关
 * @param shop 类型 :0积分商城关闭 1.积分商城开启
 * @param singIn 签到是否开启
 * @param stepNumber 步数是否开启
 */

type UserInfoVoPick = Pick<UserInfo, 'avatarUrl' | 'nickName'>

export type UserInfoVoType = UserInfoVoPick
