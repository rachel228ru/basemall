/*
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-12 15:36:21
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-30 14:16:41
 * 123
 */
/*
 * @description:
 * @Author: vikingShip
 * @Date: 2021-07-31 15:39:21
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-17 10:30:05
 */

import { ShopInfoType } from '@/store/modulesType/shopsetType'
import { BlankPaceState } from './components/blank-paceholder/blankPaceType'
import { CubeStateType } from './components/cube-box/cubeBoxType'
import { GoodProp } from './components/goods-ponents/goodsType'
import { HomeSwiperFormData } from './components/goods-swiper/goodSwiper'
import { ISeparator } from './components/home-separator/homeSeparatorType'
import { HomeSwiperState } from './components/home-swiper/homeSwiperType'
import { ImageInfo } from './components/imageCom/imageComType'
import { richProp } from './components/rich-text/richTextType'
import { SearchProp } from './components/search-comp/searchComp'
import { ISearchPlugin } from './components/search-plugin/searchPlugin'
import { navItemType } from './components/shop-nav/shopNav'
import { titleProp } from './components/title-bar/titleBarType'
import { videoProp } from './components/video-com/videoCom'

/**
 * @LastEditors: vikingShip
 * @description: wx创建动画类
 * @param Animation
 */

export type Animation = UniApp.Animation

/**
 * @LastEditors: vikingShip
 * @description: home组件state类型
 * @param authType 登录弹窗标识
 * @param showModalView 弹窗标识
 * @param components 装修组件
 * @param scrollTop 滚动条高度
 * @param shoptype 店铺禁用与打烊
 * @param shopTime 店铺工作时间
 * @param shopStatus 店铺状态
 */
export interface StateInfer {
	authType: boolean
	showModalView: boolean
	components: Array<DecorationType>
	scrollTop: number
	shoptype: boolean
	shopTime: string
	shopStatus: number
	shopMsg: ShopInfoType
}
/**
 * @LastEditors: vikingShip
 * @description: 装修组件类型
 * @param formData 装修组件中渲染节点
 * @param icon     图标
 * @param id       id
 * @param label    组件描述
 * @param value    组件名称
 */

export interface DecorationType {
	formData: IComponentsFormData
	icon: string
	id: number
	label: string
	value: string
	properties?: string
	createTime?: string
	isDeleted?: string
	pageId?: number
	tenantId?: string
	updateTime?: string
}

interface IShopNavFormdata {
	storeNavigations: navItemType[]
}

type IComponentsFormData =
	| BlankPaceState
	| CubeStateType
	| GoodProp
	| HomeSwiperFormData
	| ISeparator
	| HomeSwiperState
	| ImageInfo
	| richProp
	| SearchProp
	| ISearchPlugin
	| IShopNavFormdata
	| titleProp
	| videoProp
