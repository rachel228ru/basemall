<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-12 15:36:20
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-09-02 14:43:08
-->
<template>
	<view>
		<navigation-bar
			:title-text="indexTitle"
			background="#ffffff"
		></navigation-bar>
		<view
			v-if="isReady"
			class="container"
			:style="
				'padding-top:' +
					navigationBarHeight +
					'px; ' +
					(!notSwitchTab ? '' : 'padding-bottom:0') +
					';'
			"
		>
			<home
				v-if="method.isCurrentTab(currentTab, 'home')"
				:userInfo="userInfo"
				ref="Home"
				:notSwitchTab="notSwitchTab"
				@stopShow="stopShow"
			></home>
			<me v-if="method.isCurrentTab(currentTab, 'me')" ref="Me"></me>
			<mall v-if="method.isCurrentTab(currentTab, 'mall')"></mall>
			<shopCar
				v-if="method.isCurrentTab(currentTab, 'shopCar')"
				@shopCarChange="shopCarChange"
				:navigationBarHeight="navigationBarHeight"
			></shopCar>
		</view>
		<!-- <view v-if="showType"> -->
		<view>
			<b-tab-bar
				:currentTab="currentTab"
				:isReady="isReady && !guideVisible"
				:tabBar="tabBar"
				:shopCarNum="shopCarNum"
				shopCarType="1"
				:centerTabbarItem="centerTabbarItem"
				v-if="tabVisible && !guideVisible"
				ref="tab_bar"
			></b-tab-bar>
		</view>
		<van-dialog id="van-dialog"></van-dialog>
		<van-toast id="van-toast"></van-toast>
		<loading v-if="!isReady"></loading>
		<guide v-if="guideVisible" :guideList="guideList"></guide>
	</view>
</template>

<script module="method" lang="wxs" src="./index.wxs"></script>

<script lang="ts">
import { Component, Ref, Vue } from 'vue-property-decorator'
import navigationBar from '@/components/navigation-bar/navigation-bar.vue'
import BTabBar from '@/components/b-tab-bar/index.vue'
import loading from '@/components/loading/index.vue'
import guide from '@/components/guide/guide.vue'
import home from './modules/home/home.vue'
import mall from './modules/mall/mall.vue'
import me from './modules/me/me.vue'
import MIcon from '@/components/m-icon/m-icon.vue'
import shopCar from './modules/shopCar/shopCar.vue'
import { LinkSelectItem } from '@/store/modulesType/settingType'

@Component({
	components: {
		navigationBar,
		BTabBar,
		loading,
		guide,
		home,
		mall,
		MIcon,
		shopCar,
		me
	}
})
export default class Index extends Vue {
	get currentTab() {
		return this.$STORE.setStore.currentTab
	}

	get tabVisible() {
		return this.$STORE.setStore.tabVisible
	}

	get isReady() {
		return this.$STORE.setStore.isReady || false
	}

	get tabBar() {
		return this.$STORE.setStore.tabBar
	}

	get centerTabbarItem() {
		return this.$STORE.setStore.centerTabbarItem
	}

	get userInfo() {
		return this.$STORE.userStore.userInfo
	}

	get searchName() {
		return this.$STORE.setStore.searchName
	}

	get notSwitchTab() {
		return this.$STORE.setStore.notSwitchTab
	}

	get guideVisible() {
		return this.$STORE.setStore.guideVisible
	}

	get shopCarNum() {
		return this.$STORE.userStore.goodsNumber
	}

	get guideList() {
		return this.$STORE.setStore.guideList
	}

	get indexTitle() {
		return this.$STORE.setStore.indexTitle
	}

	get navigationBarHeight() {
		return this.$STORE.setStore.navigationBarHeight
	}

	isFlase: boolean = false
	showType: boolean = true

	@Ref('home') readonly Homes!: home
	@Ref('BTabBar') readonly tab_Bar!: BTabBar
	@Ref('me') readonly Me!: me

	/**
	 * @LastEditors: chuyinlong
	 * @description: 首页加载完毕返回
	 * @param {*} e
	 * @returns {*}
	 */
	stopShow(e: { type: number }): void {
		this.showType = Boolean(e.type)
	}

	onLoad({ page }: { page?: string }) {
		try {
			if (!this.$STORE.setStore.isReSet && page) {
				this.$STORE.setStore.setCurrentTab(page)
			}
			this.$STORE.setStore.setSwitchTab(0)
		} catch (err) {
			this.$Popup.toast('首页解析失败')
		}
	}

	onShareAppMessage() {
		return {
			title: this.$STORE.shopSetStore.shopInfo.shopName,
			path: `/pages/index/index?page=home`
		}
	}

	onShareTimeline() {
		return {
			title: this.$STORE.shopSetStore.shopInfo.shopName,
			path: `/pages/index/index?page=home`
		}
	}

	onReachBottom() {
		this.$Pubsub.emit('onReachBottom')
	}

	async onShow() {
		/** 有切换自定义页面后 重新获取首页内容  */
		if (this.Homes) {
			const id = this.$STORE.setStore.newCurrentPageId
			this.Homes.reGetPageAssembly(id)
		}
		if (this.Me) {
			this.Me.getData()
			this.$STORE.setStore.getAllUserCenterData()
		}
		this.$Pubsub.emit('onShow')
		// 购物车规格弹窗锁屏后的判断 一次性判断页面显示有问题 拆分开判断 TODO
		if (
			this.$STORE.setStore.formShopCar &&
			this.$STORE.setStore.currentTab === 'shopCar'
		) {
			this.$STORE.setStore.setTabVisible(false)
		} else {
			this.$STORE.setStore.setTabVisible(true)
		}
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 回退按钮
	 * @param {*}
	 * @returns {*}
	 */

	onBack(): void {
		const historyPage = this.$STORE.setStore.historyPage
		const setSettingStore = (
			currentTab: string,
			currentPageId: string | number,
			itemLink: LinkSelectItem,
			indexTitle: string
		) => {
			this.$STORE.setStore.setCurrentTab(currentTab)
			this.$STORE.setStore.setCurrentPageId(currentPageId)
			this.$STORE.setStore.setIndexTitle(indexTitle)
			this.$STORE.setStore.setShowTabBar(itemLink)
		}

		this.$STORE.setStore.setHistoryPage(
			historyPage.splice(historyPage.length - 1, 1)
		)

		if (historyPage.length) {
			const itemLink = historyPage[historyPage.length - 1]
			const title = `${itemLink.name ? itemLink.name : ''}`
			setSettingStore('home', itemLink.id, itemLink, title)
			uni.setNavigationBarTitle({
				title
			})
		} else {
			const { homeTab } = this.$STORE.setStore

			this.$STORE.setStore.setHistoryPage([])
			setSettingStore('home', homeTab.id, homeTab, '首页')
			homeTab.append = homeTab.name
			homeTab.url = homeTab.linkUrl
			uni.setNavigationBarTitle({
				title: `${this.$STORE.setStore.customName}`
			})
		}
		this.$Pubsub.emit('onTap')
	}

	onPageScroll(e: { scrollTop: number }) {
		if (this.Homes) {
			this.Homes.PageScroll(e)
		}
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 购物车编辑商品触发
	 * @param {*}
	 * @returns {*}
	 */
	async shopCarChange(): Promise<void> {
		await this.$STORE.userStore.getShopCarNum()
	}
}
</script>

<style lang="scss" scoped>
.container {
	padding-bottom: calc(88rpx + constant(safe-area-inset-bottom));
	padding-bottom: calc(88rpx + env(safe-area-inset-bottom));
}

.choiceShop2 {
	position: fixed;
	bottom: 300rpx;
	right: 40rpx;
	width: 90rpx;
	height: 90rpx;
	display: -webkit-box;
	display: flex;
	-webkit-box-align: center;
	align-items: center;
	-webkit-box-pack: center;
	justify-content: center;
	flex-wrap: wrap;
	background-color: rgba(0, 0, 0, 0.5);
	border-radius: 100rpx;
	color: white;
	z-index: 10;

	.choiceWrap {
		width: 50rpx;
		display: flex;
		flex-wrap: wrap;
		justify-content: center;
		align-content: center;
	}

	.ft {
		font-size: 20rpx;
		margin-top: -6rpx;
	}
}
</style>
