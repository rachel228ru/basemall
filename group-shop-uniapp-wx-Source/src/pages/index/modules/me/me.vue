<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-12 15:36:21
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-09-01 16:01:49
 * 123
-->
<template>
	<view>
		<view class="box">
			<view class="user--container" :style="headStyleConfig.headBackGround">
				<view
					:class="
						'user--layout ' + (userCenter.hideCartInlet ? '' : 'hideQrcode')
					"
				>
					<view class="user--layout__info">
						<image
							:src="userInfo.avatarUrl"
							class="user--layout__image"
							v-if="userInfo.nikeName"
						></image>
						<image
							src="http://medusa-small-file.oss-cn-hangzhou.aliyuncs.com/api/head_portrait.jpg"
							class="user--layout__image"
							v-else
						></image>
						<view class="user--layout__container">
							<view class="user--layout__nickName">
								<view class="user--layout__text" v-if="userInfo.nikeName">
									{{ userInfo.nikeName }}
								</view>
								<view class="user--layout__text" v-else @click="authLoad"
									>登录 / 注册</view
								>
								<div class="updateBtn" v-if="userInfo.nikeName">
									<button
										v-if="!canIUseGetUserProfile"
										@getuserinfo="getUserInfo"
										open-type="getUserInfo"
										hover-class="noshadow"
									>
										<m-icon
											name="iconshuaxin"
											class="icon radius--inner__icon"
											style="color: #FC4E50"
										></m-icon>
									</button>
									<button v-else @click="getUserProfile" hover-class="noshadow">
										<m-icon
											name="iconshuaxin"
											class="icon radius--inner__icon"
											style="color: #FC4E50"
										></m-icon>
									</button>
								</div>
							</view>
						</view>
					</view>
				</view>
				<view class="content">
					<view class="content__all">
						<view class="content__all--item" @click="gotoCollect">
							<view style="font-weight:bold;font-size:16px;">{{
								collectNum
							}}</view>
							<view style="margin-top:3px;font-size:13px">收藏</view>
						</view>
						<view>|</view>
						<view class="content__all--item" @click="gotoFooter">
							<view style="font-weight:bold;font-size:16px;">{{
								footNum
							}}</view>
							<view style="margin-top:3px;font-size:13px">足迹</view>
						</view>
					</view>
				</view>
			</view>
			<view class="order">
				<view class="order__all">
					<view class="order__all--me">我的订单</view>
					<view class="order__all--check" @click="goOrder">
						<text>查看全部订单</text>
						<van-icon name="arrow" class="order__all--arrow"></van-icon>
					</view>
				</view>
				<view class="order__quick">
					<view
						class="cover--group__cell"
						@click.stop="coverOrderClick"
						data-url="/pages/order/order?type=0"
					>
						<view class="order__quick--item">
							<view class="order__quick--badge">
								<view
									class="order__quick--number"
									v-if="orderBadgeInfo.waitForPay !== '0'"
								>
									{{ orderBadgeInfo.waitForPay }}
								</view>
								<image
									class="order__quick--image"
									:src="orderInfo.waitIcon.url"
								></image>
							</view>
							<view class="order__quick--text">{{
								orderInfo.waitIcon.name
							}}</view>
						</view>
					</view>
					<view
						class="cover--group__cell"
						@click.stop="coverOrderClick"
						data-url="/pages/order/order?type=1"
					>
						<view class="order__quick--item">
							<view class="order__quick--badge">
								<view
									class="order__quick--number"
									v-if="orderBadgeInfo.withDelivery !== '0'"
								>
									{{ orderBadgeInfo.withDelivery }}
								</view>
								<image
									class="order__quick--image"
									:src="orderInfo.waitDelivered.url"
								></image>
							</view>
							<view class="order__quick--text">{{
								orderInfo.waitDelivered.name
							}}</view>
						</view>
					</view>
					<view
						class="cover--group__cell"
						@click.stop="coverOrderClick"
						data-url="/pages/order/order?type=2"
					>
						<view class="order__quick--item">
							<view class="order__quick--badge">
								<view
									class="order__quick--number"
									v-if="orderBadgeInfo.shipped !== '0'"
								>
									{{ orderBadgeInfo.shipped }}
								</view>
								<image
									class="order__quick--image"
									:src="orderInfo.deliveryIcon.url"
								></image>
							</view>
							<view class="order__quick--text">{{
								orderInfo.deliveryIcon.name
							}}</view>
						</view>
					</view>
					<view
						class="cover--group__cell"
						@click.stop="coverOrderClick"
						data-url="/pages/order/order?type=3"
					>
						<view class="order__quick--item">
							<view class="order__quick--badge">
								<view
									class="order__quick--number"
									v-if="orderBadgeInfo.waitForPickup !== '0'"
								>
									{{ orderBadgeInfo.waitForPickup }}
								</view>
								<image
									class="order__quick--image"
									:src="orderInfo.waitPickingIcon.url"
								></image>
							</view>
							<view class="order__quick--text">{{
								orderInfo.waitPickingIcon.name
							}}</view>
						</view>
					</view>
					<view
						class="cover--group__cell"
						@click.stop="coverOrderClick"
						data-url="/pages/order/order?type=6"
					>
						<view class="order__quick--item">
							<view class="order__quick--badge">
								<view
									class="order__quick--number"
									v-if="orderBadgeInfo.afsOrder !== '0'"
								>
									{{ orderBadgeInfo.afsOrder }}
								</view>
								<image
									class="order__quick--image"
									:src="orderInfo.afterSaleIcon.url"
								></image>
							</view>
							<view class="order__quick--text">{{
								orderInfo.afterSaleIcon.name
							}}</view>
						</view>
					</view>
					<view
						class="cover--group__cell"
						v-if="hasOpenEvaluate"
						@click.stop="coverOrderClick"
						data-url="/pages/evaluationCenter/evaluationCenter"
					>
						<view class="order__quick--item">
							<view class="order__quick--badge">
								<image
									class="order__quick--image"
									:src="orderInfo.evaluateIcon.url"
								></image>
							</view>
							<view class="order__quick--text">{{
								orderInfo.evaluateIcon.name
							}}</view>
						</view>
					</view>
				</view>
			</view>
		</view>
		<view class="cover--container">
			<view class="cover--group" v-if="userCenter.menuStyle === 1">
				<view
					v-for="(item, index) in expandMenu"
					:key="index"
					:class="'cover--group__expand ' + (item.splitFlag ? 'splitFlag' : '')"
					@click.stop="linkClick"
					:data-link="item.linkSelectItem"
				>
					<block v-if="item.hideMenu">
						<button
							open-type="contact"
							class="contact__button cover--group__cell"
							v-if="item.menuName === '客服'"
						>
							<view class="group__cell--left">
								<image
									class="cell--left--image"
									:src="item.menuIconUrl"
								></image>
								<text class="cell--left--text">{{ item.menuName }}</text>
							</view>
							<van-icon class="group__cell--right" name="arrow"></van-icon>
						</button>
						<view
							v-else
							:class="
								'cover--group__cell ' + (item.splitFlag ? 'splitFlag' : '')
							"
						>
							<view class="group__cell--left">
								<image
									class="cell--left--image"
									:src="item.menuIconUrl"
								></image>
								<text class="cell--left--text">{{ item.menuName }}</text>
							</view>
							<van-icon class="group__cell--right" name="arrow"></van-icon>
						</view>
					</block>
				</view>
			</view>
			<view class="grid" v-if="userCenter.menuStyle === 2">
				<view class="user__menu--grid">
					<view>
						<view class="grid__name">我的工具</view>
						<view class="grid__wrap">
							<block v-for="(supItem, supIndex) in gridMenu" :key="supIndex">
								<view
									class="grid__navigator"
									@click.stop="linkClick"
									:data-link="supItem.linkSelectItem"
									v-if="supItem.hideMenu"
								>
									<button
										open-type="contact"
										class="grid__item"
										v-if="supItem.menuName === '客服'"
									>
										<image
											class="grid__navigator--image"
											:src="supItem.menuIconUrl"
										></image>
										<view class="grid__navigator--right">
											<text class="grid__navigator--name">{{
												supItem.menuName
											}}</text>
										</view>
									</button>
									<view v-else class="grid__item">
										<image
											class="grid__navigator--image"
											:src="supItem.menuIconUrl"
										></image>
										<view class="grid__navigator--right">
											<text class="grid__navigator--name">{{
												supItem.menuName
											}}</text>
										</view>
									</view>
								</view>
							</block>
						</view>
					</view>
				</view>
			</view>
			<view
				class="version__text"
				@click="pageLink"
				data-link="/pages/index/modules/me/systemBanner/systemBanner"
			>
				<image
					class="miniBottomLog"
					v-if="!!shopInfo.miniBottomLog"
					:src="!!shopInfo.miniBottomLog ? shopInfo.miniBottomLog : ''"
				></image>
				smart shop商城系统 v{{ APP_VERSION }}
			</view>
			<!-- 页面蒙版 用于新用户登录操作 勿删 -->
			<view
				class="loadMen"
				:style="'height:' + scrollHeight * 2 + 'rpx'"
				@click="authLoad"
				v-if="scrollType"
			></view>
			<auth
				:authType="authType"
				state="me"
				@cancel="cancel"
				@getNew="getNew"
			></auth>
		</view>
	</view>
</template>

<script module="image" lang="wxs" src="@/wxs/image.wxs"></script>
<script lang="ts">
import { Component, Vue } from 'vue-property-decorator'
import auth from '@/components/auth/auth.vue'
import mIcon from '@/components/m-icon/m-icon.vue'

import {
	UserInfo,
	UserCenterType,
	CustomStyle,
	UserInfoVoType,
	MenuList,
	OrderBadgeType,
	ApiCenterAggregation
} from './meType'

import { userMsgGet } from '@/api/modules/user'
import { navtofun } from '@/utils/navtofun'
import { ShopInfoType } from '@/store/modulesType/shopsetType'
import { LinkSelectItem } from '@/store/modulesType/settingType'

@Component({
	components: {
		auth,
		mIcon
	}
})
export default class Me extends Vue {
	name = 'mall'

	get userInfo() {
		return this.$STORE.userStore.userInfo
	}

	/** 接口设置数据 */
	userCenter: UserCenterType = {
		customStyle: '',
		headStyle: 1,
		id: 0,
		menuStyle: 1,
		menuVos: [],
		orderInfo: ''
	}
	/** 自定义样式数据 */
	customStyle = {} as CustomStyle
	/** 用户信息数据 */
	orderInfo = {} as UserInfoVoType
	/** 展开式菜单数据 */
	expandMenu: Array<MenuList> = []
	/** 九宫格菜单数据 */
	gridMenu: Array<MenuList> = []
	qrcodeShow = false
	authType = false
	headStyleConfig: CustomStyle = {
		headBackGround: '',
		cardColor: '#45403C',
		textColor: '#E4CB98'
	}
	scrollHeight = 0
	scrollType = false
	/** 收藏数量 */
	collectNum = 0
	/** 足迹数量 */
	footNum = 0
	// 是否开启评价
	hasOpenEvaluate = true
	orderBadgeInfo: OrderBadgeType = {
		/** 代付款数目 */
		waitForPay: '',
		/** 配送中数目 */
		shipped: '',
		/** 待提货数目 */
		waitForPickup: '',
		/** 待发货数目 */
		withDelivery: ''
	}
	/** 版本号 */
	APP_VERSION = process.env.VUE_APP_APP_SHOW_VERSION
	shopInfo: ShopInfoType = {} as ShopInfoType
	canIUseGetUserProfile: boolean = false

	get isCurrent() {
		return this.$STORE.setStore.currentTab === this.name
	}

	created() {
		uni.setNavigationBarTitle({
			title: '个人中心'
		})
		this.$Pubsub.on('app-launch', () => {
			this.getData()
		})
		this.init()
		if (uni.getUserProfile as any) {
			this.canIUseGetUserProfile = true
		}
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 生成数据
	 * @param {*}
	 * @returns {*}
	 */

	async init(): Promise<void> {
		try {
			const windowHeight = uni.getSystemInfoSync().windowHeight // 屏幕的高度
			const windowWidth = uni.getSystemInfoSync().windowWidth // 屏幕的宽度
			const user = this.$STORE.userStore.userInfo
			// 是否开启评价
			this.hasOpenEvaluate = this.$STORE.setStore.shopSetting.hasOpenEvaluate
			this.scrollHeight = (windowHeight * 750) / windowWidth
			this.scrollType = !user.whetherAuthorization ? true : false
			// 获取缓存初次渲染
			this.updateAllData(this.$STORE.setStore.userCenter)
			// 获取最新个人中心数据并缓存
			this.getData()
		} catch (err) {
			console.log(err)
		}
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 获取数据
	 * @param {*}
	 * @returns {*}
	 */

	async getData(): Promise<void> {
		await this.$STORE.setStore.getAllUserCenterData()
		const userCenterData = this.$STORE.setStore.userCenter
		this.getUserMsg()
		this.updateAllData(userCenterData)
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 获取用户数据
	 * @param {*}
	 * @returns {*}
	 */

	async getUserMsg(): Promise<void> {
		const res = await userMsgGet({
			infoLevel: 2
		})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 页面数据配置
	 * @param {*} userCenterData
	 * @returns {*}
	 */
	updateAllData(userCenterData: ApiCenterAggregation): void {
		// 我的页面配置数据
		this.getUserCenterSetting(userCenterData.accountCenterVo)
		// 用户足迹数量
		this.footNum = userCenterData.accountFootMarkCount
		// 用户收藏数量
		this.collectNum = userCenterData.collectCount
		// 数量标记数据
		this.orderBadgeInfo = userCenterData.orderOverviewVo
		// 会员信息
		this.shopInfo = this.$STORE.shopSetStore.shopInfo
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 页面跳转路径
	 * @param {*} e
	 * @returns {*}
	 */

	pageLink(e: { currentTarget: { dataset: { link: string } } }): void {
		const url = e.currentTarget.dataset.link
		uni.navigateTo({
			url: `/subcontract${url}`
		})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 前往我的足迹
	 * @param {*}
	 * @returns {*}
	 */

	gotoFooter(): void {
		uni.navigateTo({
			url: '/subcontract/pages/footer/footer'
		})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 前往我的收藏页面
	 * @param {*}
	 * @returns {*}
	 */

	gotoCollect(): void {
		uni.navigateTo({
			url: '/subcontract/pages/collect/collect'
		})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 登录注册
	 * @param {*}
	 * @returns {*}
	 */

	authLoad(): void {
		this.authType = true
		this.scrollType = false
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 手动更新用户数据
	 * @param { UniApp.GetUserInfoRes} detail
	 * @returns {*}
	 */

	async getUserInfo({
		detail
	}: {
		detail: UniApp.GetUserInfoRes
	}): Promise<void> {
		if (!detail.rawData) {
			// 拒绝授权
			return
		} else {
			await this.$STORE.userStore.updateUser(detail.userInfo)
		}
		this.getData()
		uni.showToast({
			title: '更新成功',
			icon: 'none'
		})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 手动更新用户数据
	 * @param {*}
	 * @returns {*}
	 */

	getUserProfile() {
		uni.showLoading({
			title: '',
			mask: true
		})
		uni.getUserProfile({
			desc: '用于完善会员资料',
			success: async (detail) => {
				if (!detail.rawData) {
					// 拒绝授权
					uni.hideLoading()
					return
				} else {
					await this.$STORE.userStore.updateUser(detail.userInfo)
				}
				this.getData()
				uni.hideLoading()
				uni.showToast({
					title: '更新成功',
					icon: 'none'
				})
			},
			fail: () => {
				uni.hideLoading()
			}
		})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 取消授权
	 * @param {*}
	 * @returns {*}
	 */

	cancel(): void {
		this.authType = false
		this.scrollType = true
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 获取个人中心配置数据
	 * @param {UserCenterType} userCenterVo
	 * @returns {*}
	 */
	async getUserCenterSetting(userCenterVo: UserCenterType): Promise<void> {
		if (!userCenterVo) {
			return
		}
		const expandMenu = userCenterVo.menuVos
			.filter((item: MenuList) => {
				// 过滤出展开式菜单
				return item.styleType === 1
			})
			.map((mapItem: MenuList) => {
				if (typeof mapItem.linkSelectItem === 'string') {
					mapItem.linkSelectItem = JSON.parse(mapItem.linkSelectItem)
				}
				return mapItem
			})
		const gridMenu = userCenterVo.menuVos
			.filter((item: MenuList) => {
				// 过滤出展开式菜单
				return item.styleType === 2
			})
			.map((mapItem: MenuList) => {
				if (typeof mapItem.linkSelectItem === 'string') {
					mapItem.linkSelectItem = JSON.parse(mapItem.linkSelectItem)
				}
				return mapItem
			})
		this.userCenter = userCenterVo
		this.orderInfo = JSON.parse(userCenterVo.orderInfo)
		this.customStyle = JSON.parse(userCenterVo.customStyle)
		this.expandMenu = expandMenu
		this.gridMenu = gridMenu
		// 系统风格
		if (this.userCenter.headStyle === 1) {
			this.headStyleConfig.headBackGround = `background-color: #FE4E63`
		}
		if (this.userCenter.headStyle === 2) {
			;(this.headStyleConfig.headBackGround = `background-image: url(${this.customStyle.backgroundImage})`),
				(this.headStyleConfig.cardColor = this.customStyle.cardColor)
			this.headStyleConfig.textColor = this.customStyle.textColor
		}
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 登陆后获取最新数据
	 * @param {*}
	 * @returns {*}
	 */

	getNew(): void {
		this.authType = false
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 前往全部订单
	 * @param {*}
	 * @returns {*}
	 */
	goOrder(): void {
		uni.navigateTo({
			url: '/subcontract/pages/order/order'
		})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 菜单跳转链接点击处理事件
	 * @param {*} e
	 * @returns {*}
	 */

	linkClick(e: { currentTarget: { dataset: { link: LinkSelectItem } } }): void {
		const link = e.currentTarget.dataset.link
		if (link.append && link.append === 'shopCar') {
			navtofun(link, this)
			return
		}
		if (link.type && link.type === 7) {
			uni.navigateToMiniProgram({
				appId: link.url,
				envVersion: 'release'
			})
			return
		}
		if (
			(link.type && (link.type === 5 || link.type === 6)) ||
			link.append === 'mall'
		) {
			navtofun(link, this)
			return
		}
		this.linkLogcal(link)
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 其他链接跳转
	 * @param {LinkSelectItem}link
	 * @returns {*}
	 */

	linkLogcal(link: LinkSelectItem): void {
		// 空链接不进行跳转
		if (!link.url) {
			return
		}

		// 首页跳转
		if (link.url === '/pages/index/index') {
			this.$STORE.setStore.setCurrentTab(link.append)
			return
		}
		// 列表和九宫格的设置跳转
		if (link.menuName === '设置' || link.url === '/pages/mySetting/mySetting') {
			if (this.userCenter.menuStyle === 1) {
				uni.navigateTo({
					url: '/subcontract/pages/mySetting/mySetting?type=1'
				})
			}
			if (this.userCenter.menuStyle === 2) {
				uni.navigateTo({
					url: '/subcontract/pages/mySetting/mySetting?type=2'
				})
			}
			return
		}
		if (link.type && link.type === 6) {
			uni.navigateTo({
				url: '/subcontract/pages/linkPge/linkPge?src=' + link.url
			})
			return
		}
		// // 普通链接跳转
		uni.navigateTo({
			url: '/subcontract' + link.url
		})
		this.linkTypeHandle(link)
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 其他页面跳转
	 * @param {LinkSelectItem} link
	 * @returns {*}
	 */
	linkTypeHandle(link: LinkSelectItem): void {
		// 分类页
		if (link.type === 2) {
			this.$STORE.setStore.setCurrentPageId(link.id + '')
			this.$STORE.setStore.setCurrentTab(link.append)
			return
		}
		// 商超商品
		if (link.type === 1) {
			// 自定义分类id
			uni.navigateTo({
				url: '/subcontract' + link.url + '?id=' + link.id
			})
			return
		}
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 订单跳转各个分类
	 * @param {*} e
	 * @returns {*}
	 */
	coverOrderClick(e: { currentTarget: { dataset: { url: string } } }): void {
		const url = e.currentTarget.dataset.url
		uni.navigateTo({
			url: `/subcontract${url}`
		})
	}
}
</script>
<style lang="scss" scoped>
@mixin flex($justify-content: center, $align-items: center) {
	display: flex;
	justify-content: $justify-content;
	align-items: $align-items;
}

@mixin bottom-line($weight: 1px, $color: #f8f5f9) {
	border-bottom: $weight solid $color;
}

// 页面主色
$page-main-color: #fe4e63;
// 字体颜色
$page-text-color: #45403c;
// 默认边距
$default-side: 18rpx;
// 默认字体大小
$default-font-size: 26rpx;
// 内容字体大小
$content-font-size: 26rpx;

.loadMen {
	width: 100%;
	background-color: rgba(0, 0, 0, 0);
	position: absolute;
	top: 0;
	z-index: 9;
}

.updateBtn {
	width: 50rpx;
	height: 50rpx;
	background-color: white;
	border-radius: 100rpx;
	display: flex;
	align-items: center;
	justify-content: center;
	margin-left: 20rpx;
}

.box {
	width: 100vw;
	position: relative;
	background-color: #f5f5f5;
	overflow: hidden;

	.user--container {
		background-size: cover;
		background-repeat: no-repeat;
		width: 100vw;
		z-index: 2;

		.user--layout {
			width: 90vw;
			margin: 0 auto;
			padding: 40rpx 0 20rpx 0;
			@include flex(space-between, center);

			.user--layout__info {
				@include flex;
			}

			.user--layout__icon {
				display: flex;
				width: 110rpx;
				flex-wrap: wrap;
				justify-content: center;

				.user--layout__icon__text {
					font-size: 24rpx;
					color: white;
					margin-top: 8rpx;
				}
			}

			.user--layout__container {
				padding-left: 20rpx;
				@include flex(space-between, flex-start);
				flex-direction: column;
			}

			.user--layout__nickName {
				@include flex;
			}

			.user--layout__image {
				flex: 0 0 118rpx;
				width: 118rpx;
				height: 118rpx;
				border-radius: 50%;
				box-shadow: 0 0 0 6rpx white;
			}

			.user--layout__text {
				vertical-align: middle;
				font-size: 32rpx;
				color: #ffffff;
				padding-left: 20rpx;
			}

			.user--layout__member {
				line-height: 44rpx;
				border: 2rpx solid white;
				border-radius: 22rpx;
				padding: 0 10rpx;
				display: flex;
				justify-content: center;
				align-items: center;

				.icon {
					color: #f3ca7e;
				}

				text {
					font-size: 22rpx;
					color: #ffffff;
				}
			}

			.user--layout__recode {
				display: flex;
				color: white;
				font-size: 26rpx;
			}

			.user--layout__qrcode {
				font-size: 50rpx;
				line-height: 0rpx;
				width: 100rpx;
				height: 100rpx;
				background-color: white;
				opacity: 0.25;
				border-radius: 12rpx;
			}
		}

		.content {
			margin: 20rpx 20rpx 0rpx 20rpx;
			padding: 0rpx 20rpx !important;
			border-radius: 15rpx;
			z-index: 10;
			height: 136rpx;

			.content__all {
				height: 136rpx;
				display: flex;
				justify-content: space-around;
				align-items: center;
				font-size: 26rpx;
				color: #ffffff;

				.content__all--item {
					width: 56rpx;
					display: flex;
					flex-wrap: wrap;
					justify-content: center;
				}
			}
		}

		.user--layout__recode__text {
			width: 60rpx;
			height: 76rpx;
			flex-wrap: wrap;
			display: flex;
			justify-content: center;
		}

		.user--layout__recode__line {
			height: 50rpx;
			background-color: white;
			width: 2rpx;
			margin: 4rpx 20rpx 0px 20rpx;
		}

		/* 会员权益 */
		.radius {
			width: 100vw;
			margin: 20rpx auto 0;

			.radius--inner {
				width: calc(100% - 36rpx);
				height: 90rpx;
				border-radius: 10rpx 10rpx 0% 0%;
				margin: 0rpx $default-side;
				padding: 0rpx 30rpx 0rpx 40rpx;
				@include flex(space-between, center);

				.radius--inner__item {
					font-size: $content-font-size;
				}

				.radius--inner__split {
					padding: 0 20rpx;
				}

				.radius--inner__btn {
					border-radius: 80rpx;
					width: 139rpx;
					height: 39rpx;
					font-size: $content-font-size;
					line-height: 39rpx;
					text-align: center;
				}
			}
		}
	}
}

.order {
	width: 100vw;
	margin: 0 auto;
	padding: 20rpx 20rpx 0 20rpx !important;

	.order__all {
		@include flex(space-between, center);
		padding: 30rpx 28rpx 30rpx 36rpx;
		border-radius: 15rpx 15rpx 0 0;
		@include bottom-line;
		font-size: $content-font-size;
		background-color: #ffffff;

		.order__all--me {
			color: $page-text-color;
			font-weight: bold;
			font-size: 30rpx;
		}

		.order__all--check {
			color: rgba(69, 64, 60, 0.6);
			background-color: #ffffff;

			.order__all--arrow {
				font-size: 20rpx;
			}
		}
	}

	.order__quick {
		@include flex(space-between, center);
		padding: 35rpx 24rpx;
		margin: 0;
		background-color: #ffffff;
		border-radius: 0 0 15rpx 15rpx;
		overflow: hidden;

		.order__quick--item {
			text-align: center;
			position: relative;
			width: 110rpx;

			.order__quick--badge {
				position: relative;
				display: inline-block;

				.order__quick--image {
					margin-bottom: 10rpx;
					width: 50rpx;
					height: 50rpx;
				}

				.order__quick--number {
					position: absolute;
					top: 0;
					right: 0;
					box-sizing: border-box;
					min-width: 32rpx;
					padding: 0 6rpx;
					color: #fff;
					font-weight: 500;
					font-size: 24rpx;
					font-family: PingFang SC, Helvetica Neue, Arial, sans-serif;
					line-height: 28rpx;
					text-align: center;
					background-color: #ee0a24;
					border: 2rpx solid #fff;
					border-radius: 32rpx;
					-webkit-transform: translate(50%, -50%);
					transform: translate(50%, -50%);
					-webkit-transform-origin: 100%;
					transform-origin: 100%;
				}
			}

			.order__quick--text {
				font-size: $content-font-size;
				color: rgb(102, 94, 88);
			}
		}
	}
}

.cover--container {
	background-color: #f5f5f5;
	padding: $default-side;

	.cover--group {
		border-radius: 10rpx;
		margin-bottom: 15rpx;

		.cover--group__expand:first-child {
			.cover--group__cell {
				border-top-right-radius: 15rpx;
				border-top-left-radius: 15rpx;
				font-family: 'first-child';
			}
		}

		.cover--group__expand:last-child {
			.cover--group__cell {
				border-bottom-right-radius: 15rpx;
				border-bottom-left-radius: 15rpx;
				font-family: 'last-child';
			}
		}

		.cover--group__expand:not(:last-child) {
			.cover--group__cell {
				@include bottom-line;
			}
		}

		.cover--group__expand.splitFlag + .cover--group__expand {
			.cover--group__cell {
				border-top-right-radius: 15rpx;
				border-top-left-radius: 15rpx;
				font-family: '+';
			}
		}

		.cover--group__expand.splitFlag {
			.cover--group__cell {
				margin-bottom: 15rpx;
				border-bottom-right-radius: 15rpx;
				border-bottom-left-radius: 15rpx;
				font-family: 'splitFlag';
			}
		}

		.cover--group__cell {
			@include flex(space-between, center);
			background-color: #ffffff;
			padding: 30rpx 25rpx 30rpx 42rpx;

			.group__cell--left {
				@include flex(center, center);

				.cell--left--image {
					width: 38rpx;
					height: 38rpx;
					padding-right: 23rpx;
				}

				.cell--left--text {
					font-size: $default-font-size;
					color: $page-text-color;
				}
			}

			.group__cell--right {
				color: rgba(69, 64, 60, 0.6);
				font-size: 24rpx;
				line-height: 0rpx;
			}
		}
	}

	.grid {
		background-color: #ffffff;
		margin-bottom: 30rpx;
		border-radius: 10rpx;
		padding: 4rpx 0;

		.grid__name {
			font-size: 28rpx;
			color: #45403c;
			padding: 30rpx 0 20rpx 38rpx;
			font-weight: bolder;
		}

		.grid__item {
			text-align: center;
		}

		.grid__wrap {
			@include flex(flex-start);
			flex-wrap: wrap;

			.grid__navigator {
				@include flex();
				flex-direction: column;
				flex: 0 0 33%;
				padding: 30rpx 0;

				.grid__navigator--image {
					width: 50rpx;
					height: 50rpx;
				}

				.grid__navigator--right {
					text-align: center;
					padding-top: 20rpx;
					display: flex;
					flex-direction: column;
					justify-content: center;

					.grid__navigator--name {
						font-size: 26rpx;
						color: #58524d;
					}

					.grid__navigator--text {
						font-size: 28rpx;
						color: #b2b2b2;
					}
				}
			}
		}
	}

	.navigator-hover {
		background-color: #f1f2f6;
	}

	.version__text {
		font-size: 26rpx;
		color: #b2b2b2;
		text-align: center;
		padding: 40rpx 0px;
	}

	.borderRadius {
		border-radius: 100%;
	}

	.contact__button {
		color: #45403c;
		background-color: transparent;
		font-size: 32rpx;
	}
}

.miniBottomLog {
	width: 100rpx;
	margin: 0 auto;
	height: 100rpx;
	display: flex;
}
</style>
