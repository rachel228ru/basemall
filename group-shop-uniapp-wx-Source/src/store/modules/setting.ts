/*
 * @description:
 * @Author: vikingShip
 * @Date: 2021-07-31 09:02:11
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-09-02 14:46:00
 */
// 店铺设置模块
import {
	VuexModule,
	Module,
	Mutation,
	Action,
	getModule
} from 'vuex-module-decorators'
import store from '@/store'
import {
	ModuleSettingType,
	PluginProMenuList,
	DecorationPageType,
	IOrderSetting,
	DecorationComponent,
	IShopSetting,
	TabBarType,
	DecorationPolymerizeType,
	DecorationPluginType,
	DecorationPluginForm,
	GuideListItemType,
	LinkSelectItem
} from '../modulesType/settingType'
import { ShopInfoType } from '../modulesType/shopsetType'
import { ApiCenterAggregation } from '@/pages/index/modules/me/meType'
import { getSetting, getGuide, getGuideVisible } from '@/api/modules/global'
import { getAggregate } from '@/api/modules/userCenter'
import { getAllRegionList, getDecorationAll } from '@/api/modules/decoration'

import { isPrimeNum } from '@/utils'
import storage from '@/utils/storage'
import { isArray, isEqual } from 'lodash'
import { DecorationType } from '@/pages/index/modules/home/homeType'
@Module({ name: 'setStore', dynamic: true, namespaced: true, store })
export class SettingStore extends VuexModule implements ModuleSettingType {
	public shopSetting = {
		hasOpenEvaluate: false,
		rebateSet: {},
		openMember: false,
		curShopInfoDto: {} as Pick<ShopInfoType, 'shopName'>
	} as IShopSetting
	public isReady = false
	public guideVisible = false
	public linkPageId = ''
	public tabVisible = true
	public currentTab = ''
	public searchName = ''
	public currentPageId: string | number = ''
	public homeTab = {} as PluginProMenuList
	public templateId: string | number = ''
	public newCurrentPageId: string | number = ''
	public pageList: DecorationPageType[] = []
	public iftransFrom = false
	public hasInCustom = false
	public customName = ''
	public customPageId: string | number = ''
	public indexTitle = '首页'
	public isReSet = false
	public formShopCar = false
	public order = {} as IOrderSetting
	public navigationBarHeight = 0
	public tabBar: TabBarType = {
		mode: 'default',
		color: '#7A7E83',
		selectedColor: '#F64E3F',
		background: '#ffffff',
		list: [],
		selectColor: '#F64E3F',
		defaultColor: '#7A7E83'
	}
	public components: DecorationType[] = []
	public spellPage = {} as DecorationComponent
	public businessSuper = {} as DecorationComponent
	public userCenter = {} as ApiCenterAggregation
	public notSwitchTab = 0
	public historyPage: LinkSelectItem[] = []
	public guideList: GuideListItemType[] = [] as GuideListItemType[]
	public newCurrentPage: string | number = ''
	public saleMode: string | number = ''
	public modeType = 1
	public customPageName = ''
	public centerTabbarItem: LinkSelectItem & {
		centerNum: number
	} = {} as LinkSelectItem & {
		centerNum: number
	}
	/**
	 * @LastEditors: vikingShip
	 * @description: 设置是否开启转发进入状态
	 */
	@Mutation
	private SET_IF_TRANSFROM(value: boolean): void {
		this.iftransFrom = value
	}
	/**
	 * @LastEditors: vikingShip
	 * @description: 设置是否开启引导页
	 */
	@Mutation
	private SET_GUIDE_VISIBLE(value: boolean): void {
		this.guideVisible = value
	}
	/**
	 * @LastEditors: vikingShip
	 * @description: 设置自定义分类页标题
	 */
	@Mutation
	private SET_CUSTOM_PAGENAME(value: string): void {
		this.customPageName = value
	}
	/**
	 * @LastEditors: vikingShip
	 * @description: 设置自定义页面名称
	 */
	@Mutation
	private SET_CUSTOM_NAME(value: string): void {
		this.customName = value
	}
	/**
	 * @LastEditors: vikingShip
	 * @description: 设置当前tabbar
	 */
	@Mutation
	private SET_CURRENT_TAB(value: string): void {
		this.currentTab = value
	}
	/**
	 * @LastEditors: vikingShip
	 * @description: 设置准备完毕
	 */
	@Mutation
	private SET_ISREADY(value: boolean): void {
		this.isReady = value
	}
	/**
	 * @LastEditors: vikingShip
	 * @description: 设置当前自定义页面id
	 */
	@Mutation
	private SET_CURRENT_PAGEID(value: string | number): void {
		this.currentPageId = value
	}
	/**
	 * @LastEditors: vikingShip
	 * @description: 设置新的id
	 */
	@Mutation
	private SET_NEW_CURRENT_PAGEID(value: number | string): void {
		this.newCurrentPageId = value
	}
	/**
	 * @LastEditors: vikingShip
	 * @description: 设置tab显示状态
	 */
	@Mutation
	private SET_TAB_VISIBLE(value: boolean): void {
		this.tabVisible = value
	}
	/**
	 * @LastEditors: vikingShip
	 * @description: 设置引导页配置
	 */
	@Mutation
	private SET_GUIDE_LIST(value: GuideListItemType[]): void {
		this.guideList = value
	}
	/**
	 * @LastEditors: vikingShip
	 * @description: 设置订单设置
	 */
	@Mutation
	private SET_ORDER(value: IOrderSetting): void {
		this.order = value
	}
	/**
	 * @LastEditors: vikingShip
	 * @description: 设置店铺基本信息
	 */
	@Mutation
	private SET_SHOP_SETTING(value: IShopSetting): void {
		this.shopSetting = value
	}
	/**
	 * @LastEditors: vikingShip
	 * @description: 设置首页标题
	 */
	@Mutation
	private SET_INDEX_TITLE(value: string): void {
		this.indexTitle = value
	}
	/**
	 * @LastEditors: vikingShip
	 * @description: 设置个人中心数据
	 */
	@Mutation
	private SET_USER_CENTER(value: ApiCenterAggregation): void {
		this.userCenter = value
	}
	/**
	 * @LastEditors: vikingShip
	 * @description: 设置页面装修数据
	 */
	@Mutation
	private SET_COMPONENTS(value: DecorationType[]): void {
		this.components = value
	}
	/**
	 * @LastEditors: vikingShip
	 * @description: 设置页面装修模板ID
	 */
	@Mutation
	private SET_TEMPLATEID(value: string | number): void {
		this.templateId = value
	}
	/**
	 * @LastEditors: vikingShip
	 * @description: 页面数据
	 */
	@Mutation
	private SET_PAGE_LIST(value: DecorationPageType[]): void {
		this.pageList = value
	}
	/**
	 * @LastEditors: vikingShip
	 * @description: 设置tabBar数据
	 */
	@Mutation
	private SET_TABBAR(value: Partial<TabBarType>): void {
		this.tabBar = { ...this.tabBar, ...value }
	}
	/**
	 * @LastEditors: vikingShip
	 * @description: 设置homeTab的信息obj
	 */
	@Mutation
	private SET_HOME_TAB(value: PluginProMenuList): void {
		this.homeTab = value
	}
	/**
	 * @LastEditors: vikingShip
	 * @description: 设置
	 */
	@Mutation
	private SET_CENTER_TABBAR_ITEM(
		value: LinkSelectItem & {
			centerNum: number
		}
	): void {
		this.centerTabbarItem = value
	}
	/**
	 * @LastEditors: vikingShip
	 * @description: 自定义页面ID
	 */
	@Mutation
	private SET_CUSTOM_PAGEID(value: string | number): void {
		this.customPageId = value
	}
	/**
	 * @LastEditors: vikingShip
	 * @description: 设置是否进入过自定义页面
	 */
	@Mutation
	private SET_HAS_INCUSTOM(value: boolean): void {
		this.hasInCustom = value
	}
	/**
	 * @LastEditors: vikingShip
	 * @description: 重新设置底部选中对象
	 */
	@Mutation
	private SET_ISRESET(value: boolean): void {
		this.isReSet = value
	}
	/**
	 * @LastEditors: vikingShip
	 * @description: 设置导航栏高度
	 */
	@Mutation
	private SET_NAVIGATION_BAR_HEIGHT(value: number): void {
		this.navigationBarHeight = value
	}
	/**
	 * @LastEditors: vikingShip
	 * @description: 装修数据
	 */
	@Mutation
	private SET_SELL_PAGE(value: {
		spellPage: DecorationComponent
		businessSuperdata: DecorationComponent
	}): void {
		this.spellPage = value.spellPage
		this.businessSuper = value.businessSuperdata
	}
	/**
	 * @LastEditors: vikingShip
	 * @description: 设置展示tabbar
	 */
	@Mutation
	private SET_SHOW_TABBAR(value: LinkSelectItem): void {
		const tabBar = this.tabBar
		const { itemLink } = value
		const arr = tabBar.list.filter(function(item) {
			return (
				item.type === itemLink.type &&
				item.linkUrl === itemLink.url &&
				item.name === itemLink.append &&
				item.id === itemLink.id
			)
		})
		if (!arr.length) {
			this.notSwitchTab = 1
		} else {
			this.notSwitchTab = 0
		}
		if (this.currentPageId > 6 && value.itemLink.sortIndex !== 0) {
			this.notSwitchTab = 1
		}
	}
	@Mutation
	private SET_SWITCHTAB(value: number): void {
		this.notSwitchTab = value
	}
	@Mutation
	private SET_HISTORY_PAGE(value: LinkSelectItem | LinkSelectItem[]): void {
		if (isArray(value)) {
			this.historyPage.push(...value)
		} else {
			this.historyPage.push(value)
		}
	}
	@Mutation
	private SET_NEW_CURRENT_PAGE(value: string | number) {
		this.newCurrentPage = value
	}
	@Mutation
	private SET_FORM_SHOPCAR(value: boolean) {
		this.formShopCar = value
	}
	@Mutation
	private SET_SALE_MODE(value: string | number) {
		this.saleMode = value
	}
	/**
	 * @LastEditors: vikingShip
	 * @description: 获取是否开启引导页
	 */
	@Action
	public async getGuideVisible() {
		const res = await getGuideVisible()
		this.SET_GUIDE_VISIBLE(!!res)
	}
	/**
	 * @LastEditors: vikingShip
	 * @description: 判断当前分类页是拼团OR商超
	 * @param id 当前分类页ID
	 */
	@Action
	public async getPageType(id: string | number) {
		let selectItem = []
		const res = await getAllRegionList({})
		selectItem = res.filter(
			(item: { id: string | number }) => Number(item.id) === Number(id)
		)
		if (selectItem.length > 0) {
			this.SET_CUSTOM_PAGENAME(selectItem[0].modeName)
		}
	}
	/**
	 * @LastEditors: vikingShip
	 * @description: 获取引导页设置
	 */
	@Action
	public async getGuideList() {
		const res: GuideListItemType[] = await getGuide()
		const list: GuideListItemType[] = res.filter(
			(item: { url: string }) => !!item.url
		)
		this.SET_GUIDE_LIST(list)
	}
	/**
	 * @LastEditors: vikingShip
	 * @description: 获取店铺设置
	 * @returns miniMsgVoList
	 */
	@Action
	public async getSetting() {
		const {
			openEvaluate,
			setting,
			miniMsgVoList,
			curShopInfoDto
		} = await getSetting()
		const data: IShopSetting = {
			hasOpenEvaluate: !!openEvaluate,
			curShopInfoDto: curShopInfoDto
		}
		this.SET_SHOP_SETTING(data)
		this.SET_ORDER(setting)
		this.SET_INDEX_TITLE(curShopInfoDto.shopName)
		return miniMsgVoList
	}
	/**
	 * @LastEditors: vikingShip
	 * @description: 获取个人中心数据并缓存
	 */
	@Action({ rawError: true })
	public async getAllUserCenterData() {
		const storageData = storage.get('getAllUserCenterData')
		const res = await getAggregate()
		if (res && !isEqual(res, storageData)) {
			storage.set('getAllUserCenterData', res, 3 * 24 * 60 * 60 * 1000)
			this.SET_USER_CENTER(res)
		} else {
			this.SET_USER_CENTER(storageData)
		}
	}
	/**
	 * @LastEditors: vikingShip
	 * @description: 记录自定义跳转路径和页面组件
	 * @param nid 跳转id
	 * @param name 跳转路径名
	 */
	@Action
	public async setNewJump({
		nid,
		name
	}: {
		nid: string | number
		name: string
	}) {
		const showPage = this.pageList.filter((item) => {
			return Number(item.id) === Number(nid)
		})
		const components: DecorationType[] = []
		const assemblyVos = showPage[0].assemblyVos
		if (assemblyVos.length > 0) {
			assemblyVos.forEach((item: { properties: string }) => {
				const properties = JSON.parse(item.properties)
				components.push(properties)
			})
		}
		this.SET_COMPONENTS(components)
		this.SET_INDEX_TITLE(name)
	}
	/**
	 * @LastEditors: vikingShip
	 * @description: 获取聚合装修接口数据
	 * @param nid
	 */
	@Action
	public async getDecorationAllList(nid = '') {
		const res = await getDecorationAll()
		if (res && !isEqual(res, storage.get('getDecorationAllListData'))) {
			storage.set('getDecorationAllListData', res, 3 * 24 * 60 * 60 * 1000)
		}
		const storageData = storage.get('getDecorationAllListData')
		if (!storageData) {
			uni.showToast({
				title: '接口无数据',
				icon: 'none'
			})
			return
		}
		const data: DecorationPolymerizeType = storageData.data || storageData
		const plugins = data.plugins
		const pages = data.pages
		const id = data.id
		await this.parseControlList(plugins)
		await this.parseAssembleList({ pages, nid })

		this.SET_TEMPLATEID(id)

		const newPages = data.pages.filter((item) => {
			return item.isDef === '1'
		})
		if (!newPages.length) {
			uni.showToast({
				title: '无首页数据',
				icon: 'none'
			})
		} else {
			this.SET_NEW_CURRENT_PAGEID(newPages[0].id)
		}
		this.SET_PAGE_LIST(pages)
	}
	/**
	 * @LastEditors: vikingShip
	 * @description: 解析配置控件数据
	 * @param plugins 插件配置
	 */
	@Action
	public async parseControlList(plugins: DecorationPluginType[]) {
		let formdata: DecorationPluginForm | null = null
		const navbar = plugins.filter((item) => {
			return item.pluginNameEn === 'navBar'
		})
		const pluginProperties = navbar[0].pluginProperties as string
		formdata = JSON.parse(pluginProperties)[0].formData || null

		const navBarform = formdata ? formdata : ({} as DecorationPluginForm)
		const handleIcon = await this.handleIcon(formdata ? formdata.menuList : [])
		const navBarPlugin = navBarform ? handleIcon : []
		const tabBar = {
			...this.tabBar,
			selectColor: formdata ? formdata.selectColor : '#F64E3F',
			defaultColor: formdata ? formdata.defaultColor : '#7A7E83'
		}
		this.SET_TABBAR(tabBar)

		this.getPlugins(navBarPlugin)
		await this.settingBigPic(navBarform)
	}
	/**
	 * @LastEditors: vikingShip
	 * @description: 解析自定义页面组件数据
	 */
	@Action
	public async parseAssembleList({
		pages,
		nid
	}: {
		pages: DecorationPageType[]
		nid: string
	}) {
		const components: DecorationType[] = []
		let assembleList: DecorationPageType[] = []
		if (!nid) {
			assembleList = pages.filter((item) => item.isDef === '1')
		} else {
			assembleList = pages.filter((item) => Number(item.id) === Number(nid))
			this.SET_HAS_INCUSTOM(false)
		}
		if (!assembleList.length) {
			uni.showToast({
				title: '未获取到首页数据！',
				icon: 'none'
			})
			return
		}
		const assemblyVos: DecorationComponent[] = assembleList[0].assemblyVos
		if (assemblyVos.length > 0) {
			assemblyVos.forEach((item: { properties: string }) => {
				const properties = JSON.parse(item.properties)
				components.push(properties)
			})
		}
		this.SET_COMPONENTS(components)

		// 处理分类页页面数据
		const spell = pages.find((i) => i.type === '1')
		const businessSuper = pages.find((i) => i.type === '2')
		const spellPage = spell
			? spell.assemblyVos[0]
				? JSON.parse(spell.assemblyVos[0].properties)
				: {}
			: {}
		const businessSuperdata = businessSuper
			? businessSuper.assemblyVos[0]
				? JSON.parse(businessSuper.assemblyVos[0].properties)
				: {}
			: {}
		this.SET_SELL_PAGE({ spellPage, businessSuperdata })
	}
	/**
	 * @LastEditors: vikingShip
	 * @description 处理系统图标
	 * @param menuList
	 * @returns 菜单列表数据
	 */
	@Action
	public async handleIcon(menuList: PluginProMenuList[]) {
		const icons = [
			{
				def: 'https://oss-tencent.bgniao.cn/api/home_page.png',
				act: 'https://oss-tencent.bgniao.cn/api/home_page1.png'
			},
			{
				def: 'https://oss-tencent.bgniao.cn/api/shopping_mall1.png',
				act: 'https://oss-tencent.bgniao.cn/api/shopping_mall.png'
			},
			{
				def: 'https://oss-tencent.bgniao.cn/api/shopping_cart1.png',
				act: 'https://oss-tencent.bgniao.cn/api/shopping_cart.png'
			},
			{
				def: 'https://oss-tencent.bgniao.cn/api/my1.png',
				act: 'https://oss-tencent.bgniao.cn/api/my.png'
			},
			{
				def:
					'https://medusa-small-file-1253272780.cos.ap-shanghai.myqcloud.com/gruul/20210112/b2ef1106af8b4a6c9b22ee0afac6195c.png',
				act:
					'https://medusa-small-file-1253272780.cos.ap-shanghai.myqcloud.com/gruul/20210112/613afce069ca4cfdb980cd446e0195f1.png'
			}
		]
		if (menuList.length < 5) {
			menuList.forEach((menu, index) => {
				if (menu.iconType === 1) {
					menu.iconPath = icons[index].def
					menu.selectedIconPath = icons[index].act
				}
			})
		} else {
			if (menuList[0].iconType === 1) {
				menuList[0].iconPath = icons[0].def
				menuList[0].selectedIconPath = icons[0].act
			}
			if (menuList[1].iconType === 1) {
				menuList[1].iconPath = icons[1].def
				menuList[1].selectedIconPath = icons[1].act
			}
			if (menuList[2].iconType === 1) {
				menuList[2].iconPath = icons[2].def
				menuList[2].selectedIconPath = icons[2].act
			}
			if (menuList[3].iconType === 1) {
				menuList[3].iconPath = icons[3].def
				menuList[3].selectedIconPath = icons[3].act
			}
			if (menuList[4].iconType === 1) {
				menuList[4].iconPath = icons[4].def
				menuList[4].selectedIconPath = icons[4].act
			}
		}
		return menuList
	}
	/**
	 * @LastEditors: vikingShip
	 * @description: 获取并处理页面控件
	 * @param navBarPlugin 控件数据
	 */
	@Action
	getPlugins(navBarPlugin: PluginProMenuList[]) {
		if (!navBarPlugin || !navBarPlugin.length) {
			return
		}
		this.SET_HOME_TAB(navBarPlugin[0])

		navBarPlugin.forEach((item: { type: number }, index: number) => {
			if (item.type === 5) {
				navBarPlugin[index].name = 'home'
			}
		})
		const tabBar = {
			...this.tabBar,
			list: navBarPlugin
		}
		this.SET_TABBAR(tabBar)

		const navbarHome = navBarPlugin.filter((item) => {
			return item.isHome
		})
		if (navbarHome.length > 0) {
			if (navbarHome[0].type === 5) {
				this.SET_CURRENT_PAGEID(navbarHome[0].id)
				this.SET_CURRENT_TAB('home')
			} else {
				this.SET_CURRENT_PAGEID('')
				this.SET_CURRENT_TAB(navbarHome[0].name)
			}
		}
	}
	/**
	 * @LastEditors: vikingShip
	 * @description:  设置大图模式
	 */
	@Action
	public async settingBigPic(navBarform: DecorationPluginForm) {
		const listLen = this.tabBar.list.length
		const centerNum = Math.floor(listLen / 2)

		const centerTabbarItem = isPrimeNum(listLen)
			? Object.assign({}, this.tabBar.list[centerNum], {
					centerNum
			  })
			: ({} as LinkSelectItem & { centerNum: number })
		const tabBar = {
			...this.tabBar
		}
		if (navBarform && navBarform.codeStyle === 2) {
			tabBar.mode = 'center'
		} else {
			tabBar.mode = 'default'
		}
		this.SET_CENTER_TABBAR_ITEM(centerTabbarItem)
		this.SET_TABBAR(tabBar)
	}
	/**
	 * @LastEditors: vikingShip
	 * @description: 返回首页公共方法
	 */
	@Action
	backHome() {
		const { linkUrl, name, type, id } = this.homeTab
		if (linkUrl === '/pages/index/index' || linkUrl === 'pages/index/index') {
			this.SET_INDEX_TITLE(this.shopSetting.curShopInfoDto.shopName)
			this.SET_CURRENT_TAB(name)
			this.SET_CURRENT_PAGEID('')
			this.SET_CUSTOM_PAGEID('')
			this.SET_HAS_INCUSTOM(true)
			this.SET_ISRESET(false)
			if (type === 5) {
				this.SET_CURRENT_PAGEID(id)
				const newPages = this.pageList.filter((item: { isDef: string }) => {
					return item.isDef === '1'
				})
				this.SET_NEW_CURRENT_PAGEID(newPages[0].id)
			}
			uni.reLaunch({
				url: `/pages/index/index`
			})
		}
	}
	@Action
	public setIsReady(value: boolean) {
		this.SET_ISREADY(value)
	}
	@Action
	public setHistoryPage(value: LinkSelectItem | LinkSelectItem[]) {
		this.SET_HISTORY_PAGE(value)
	}
	/**
	 * @LastEditors: vikingShip
	 * @description: 设置当前tabbar
	 */
	@Action
	public setCurrentTab(value: string) {
		this.SET_CURRENT_TAB(value)
	}
	/**
	 * @LastEditors: vikingShip
	 * @description: action设置首页标题
	 */

	@Action
	public setIndexTitle(value: string) {
		this.SET_INDEX_TITLE(value)
	}
	/**
	 * @LastEditors: chuyinlong
	 * @description: 设置是否开启引导页
	 */

	@Action
	public setGuideVisible(value: boolean) {
		this.SET_GUIDE_VISIBLE(value)
	}
	/**
	 * @LastEditors: chuyinlong
	 * @description: 设置导航栏高度
	 */

	@Action
	public setNavigationBarHeight(value: number) {
		this.SET_NAVIGATION_BAR_HEIGHT(value)
	}
	/**
	 * @LastEditors: chuyinlong
	 * @description: 设置购物车规格弹窗锁屏
	 */

	@Action
	public setFormShopCar(value: boolean) {
		this.SET_FORM_SHOPCAR(value)
	}
	/**
	 * @LastEditors: chuyinlong
	 * @description: 设置是否从底部导航跳转
	 */

	@Action
	public setSwitchTab(value: number) {
		this.SET_SWITCHTAB(value)
	}
	/**
	 * @LastEditors: chuyinlong
	 * @description: 设置tab显示状态
	 */

	@Action
	public setTabVisible(value: boolean) {
		this.SET_TAB_VISIBLE(value)
	}
	/**
	 * @LastEditors: chuyinlong
	 * @description: 设置当前自定义页面id
	 */

	@Action
	public setCurrentPageId(value: string | number) {
		this.SET_CURRENT_PAGEID(value)
	}
	/**
	 * @LastEditors: chuyinlong
	 * @description: 设置展示tabbar
	 */

	@Action
	public setShowTabBar(value: LinkSelectItem) {
		this.SET_SHOW_TABBAR(value)
	}
	/**
	 * @LastEditors: chuyinlong
	 * @description: 设置是否进入过自定义页面
	 */

	@Action
	public setHasInCustom(value: boolean) {
		this.SET_HAS_INCUSTOM(value)
	}
	/**
	 * @LastEditors: chuyinlong
	 * @description: 设置自定义页面名称
	 */

	@Action
	public setcustomName(value: string) {
		this.SET_CUSTOM_NAME(value)
	}
	/**
	 * @LastEditors: chuyinlong
	 * @description: 设置是否开启转发进入状态
	 */

	@Action
	public setIftransFrom(value: boolean) {
		this.SET_IF_TRANSFROM(value)
	}
	/**
	 * @LastEditors: chuyinlong
	 * @description: 设置专区ID
	 */

	@Action
	public setSaleMode(value: string | number) {
		this.SET_SALE_MODE(value)
	}
	/**
	 * @LastEditors: chuyinlong
	 * @description: 设置新的id
	 */

	@Action
	public setNewCurrentPageId(value: string | number) {
		this.SET_NEW_CURRENT_PAGEID(value)
	}
	/**
	 * @LastEditors: chuyinlong
	 * @description: 设置新进入的页面ID
	 */

	@Action
	public setNewCurrentPage(value: string | number) {
		this.SET_NEW_CURRENT_PAGE(value)
	}
}

export const setStore = getModule(SettingStore)
