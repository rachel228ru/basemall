<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-12 15:36:20
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-09-01 16:00:37
 * 123
-->
<template>
	<view>
		<!-- tabbar默认模式 -->
		<view
			class="tab--bar"
			v-if="tabBar.mode === 'default'"
			:style="{ background: tabBar.background }"
		>
			<!-- <view class="tab--bar__mask"></view> -->
			<view class="tab--bar__border" v-if="isReady"></view>
			<view
				v-for="(item, index) in tabBar.list"
				:key="index"
				class="tab--bar__item"
				@click.native="switchTab(item)"
				style="position:relative"
			>
				<image
					v-if="item.selectedIconPath"
					:src="
						currentTab === item.name && currentPageId == item.id
							? item.selectedIconPath
							: item.iconPath
					"
				>
				</image>
				<view
					:style="{
						color:
							currentTab === item.name && currentPageId == item.id
								? tabBar.selectColor
								: tabBar.defaultColor
					}"
				>
					{{ item.text }}
				</view>
				<view class="number" v-if="item.name === 'shopCar' && shopCarNum > 0">
					{{ shopCarNum }}
				</view>
			</view>
		</view>
		<!-- tabbar中按钮模式 -->
		<block v-if="tabBar.mode === 'center'">
			<!-- <view class="tab--bar__mask"></view> -->
			<view
				v-if="centerTabbarItem"
				class="tab--bar__center"
				@click.native="switchTab(centerTabbarItem)"
			>
				<image
					class="center_img"
					:src="
						currentTab === centerTabbarItem.name &&
						currentPageId == centerTabbarItem.id
							? centerTabbarItem.selectedIconPath
							: centerTabbarItem.iconPath
					"
				>
				</image>
				<view
					:style="{
						color:
							currentTab === centerTabbarItem.name &&
							currentPageId == centerTabbarItem.id
								? tabBar.selectColor
								: tabBar.defaultColor
					}"
				>
					{{ centerTabbarItem.text }}
				</view>
			</view>
			<view class="tab--bar" :style="{ background: tabBar.background }">
				<view class="tab--bar__border"></view>
				<view
					v-for="(item, index) in tabBar.list"
					:key="index"
					class="tab--bar__item"
					@click.native="switchTab(item)"
				>
					<block v-if="index !== centerTabbarItem.centerNum">
						<image
							v-if="item.selectedIconPath"
							:src="
								currentTab === item.name && currentPageId == item.id
									? item.selectedIconPath
									: item.iconPath
							"
						>
						</image>
						<view
							:style="{
								color:
									currentTab === item.name && currentPageId == item.id
										? tabBar.selectColor
										: tabBar.defaultColor
							}"
						>
							{{ item.text }}
						</view>
					</block>
					<block v-else>
						<view></view>
					</block>
				</view>
			</view>
		</block>
	</view>
</template>
<script lang="ts">
import { Component, Vue, Prop } from 'vue-property-decorator'
import { navtofun } from '@/utils/navtofun'
import { TabBarType, LinkSelectItem } from '@/store/modulesType/settingType'

@Component
export default class index extends Vue {
	// 当前选中的tab
	@Prop({ default: 'home' })
	currentTab!: string
	// 是否加载完成
	@Prop({ default: false })
	isReady!: boolean
	// tabbar数据
	@Prop({ default: [] })
	tabBar!: Partial<TabBarType>
	// 购物车数据
	@Prop({ default: 0 })
	shopCarNum!: number
	// 购物车需求来源
	@Prop({ default: 0 })
	shopCarType!: number
	// 中间元素
	@Prop()
	centerTabbarItem: any
	// 获取当前显示页面的id值
	currentPageId: string | number = ''

	mounted() {
		this.currentPageId = this.$STORE.setStore.currentPageId
	}

	created() {
		this.initPageId()
		this.$Pubsub.on('onTap', () => {
			this.currentPageId = this.$STORE.setStore.currentPageId
		})
	}

	/** 持续获取首页pageId直到获取到为止 */
	/**
	 * @LastEditors: chuyinlong
	 * @description: 持续获取首页pageId直到获取到为止
	 */

	initPageId(): void {
		if (!this.currentPageId) {
			const time = setInterval(() => {
				if (this.currentPageId) {
					clearInterval(time)
				}
				this.currentPageId = this.$STORE.setStore.currentPageId
			}, 100)
		}
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description:
	 * @param {LinkSelectItem} item
	 * @returns {*}
	 */

	switchTab(item: LinkSelectItem): void {
		this.currentPageId = item.id
		item.append = item.name
		item.url = item.linkUrl
		if (!item.name) {
			item.url = `${item.url}?btnShow=true`
			uni.reLaunch({
				url: item.url
			})
			return
		}
		navtofun(item, this)
		if (item.type === 5 && item.sortIndex === 0) {
			this.$STORE.setStore.setHistoryPage([])
			this.$STORE.setStore.setCurrentTab('home')
		}

		if (item.type === 5) {
			this.$emit('AssembleReload', {}, {})
		}

		uni.pageScrollTo({
			scrollTop: 0,
			duration: 0
		})
		this.$STORE.setStore.setIndexTitle(item.linkName)

		if (item.type === 5 && item.sortIndex === 0) {
			this.$STORE.setStore.setIndexTitle(
				this.$STORE.setStore.shopSetting.curShopInfoDto.shopName
			)
		}
	}
}
</script>
<style lang="scss" scoped>
$border-color: rgba(0, 0, 0, 0.33);

.number {
	position: absolute;
	top: 12rpx;
	right: 1;
	-webkit-box-sizing: border-box;
	box-sizing: border-box;
	min-width: 32rpx;
	padding: 0 6rpx;
	color: #fff;
	font-weight: 500;
	font-family: PingFang SC, Helvetica Neue, Arial, sans-serif;
	line-height: 30rpx;
	text-align: center;
	background-color: #ee0a24;
	border: 2rpx solid #fff;
	border-radius: 32rpx;
	-webkit-transform: translate(50%, -50%);
	transform: translate(50%, -50%);
	-webkit-transform-origin: 100%;
	transform-origin: 100%;
	padding-top: 2rpx;
	display: flex;
	align-items: center;
	justify-content: center;
}

.tab--bar {
	position: fixed;
	width: 100%;
	bottom: 0;
	left: 0;
	height: 96rpx;
	background: #ffffff;
	display: flex;
	z-index: 98;
	padding-bottom: constant(safe-area-inset-bottom);
	padding-bottom: env(safe-area-inset-bottom);
	box-sizing: content-box;

	&__border {
		position: absolute;
		left: 0;
		top: 0;
		width: 100%;
		height: 2rpx;
		transform: scaleY(0.5);
	}

	&__item {
		flex: 1;
		text-align: center;
		display: flex;
		justify-content: center;
		align-items: center;
		flex-direction: column;

		image {
			width: 40rpx;
			height: 40rpx;
		}

		view {
			font-size: 20rpx;
			margin-top: 10rpx;
		}
	}

	&__center {
		width: 100rpx;
		height: 125rpx;
		padding-bottom: constant(safe-area-inset-bottom);
		padding-bottom: env(safe-area-inset-bottom);

		.center_img {
			padding-top: 4rpx;
			width: 50rpx;
			height: 50rpx;
		}

		view {
			font-size: 20rpx;
			margin-top: 15rpx;
		}

		display: flex;
		justify-content: center;
		flex-direction: column;
		align-items: center;
		position: fixed;
		z-index: 99;
		bottom: 0px;
		border-radius: 50rpx 50rpx 0 0;
		left: calc(50% - 50rpx);
		text-align: center;
		font-size: 24rpx;
		line-height: 40rpx;
		background: #fff;
		box-sizing: content-box;
	}
}
</style>
