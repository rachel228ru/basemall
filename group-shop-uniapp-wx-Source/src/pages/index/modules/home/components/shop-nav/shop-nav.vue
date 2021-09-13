<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-12 15:36:21
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-09-01 16:01:21
 * 123
-->
<template>
	<view>
		<view class="shopNav">
			<view v-for="(item, index) in navList" :key="index">
				<view
					class="shopNav-item"
					@tap.stop="handleTap"
					:data-index="index"
					:style="
						'width:' +
							(navList.length % 5 === 0 || navList.length > 8
								? '140.5rpx'
								: '175.5rpx')
					"
				>
					<image
						v-if="item.navIcon"
						class="shopNav-item__image"
						:src="item.navIcon"
						:lazy-load="true"
					></image>
					<view class="shopNav-item__text" :style="'color:' + item.fontColor">{{
						item.navName
					}}</view>
					<button
						open-type="contact"
						class="contact-style"
						v-if="item.type === 0 && item.id === 8"
					></button>
				</view>
			</view>
		</view>
		<auth :authType="authType"></auth>
	</view>
</template>

<script lang="ts">
import { Component, Vue, Prop } from 'vue-property-decorator'
import { navtofun } from '@/utils/navtofun'
import { getAggregate } from '@/api/modules/userCenter'
import { navItemType, ShopNavType } from './shopNav'
@Component({})
export default class ShopNav extends Vue implements ShopNavType {
	@Prop({
		type: Array
	})
	navList!: navItemType[]

	authType: boolean = false

	/**
	 * @LastEditors: chuyinlong
	 * @description: 导航点击
	 * @param {number} e.currentTarget.dataset.index
	 * @returns {*}
	 */

	async handleTap(e: {
		currentTarget: { dataset: { index: number } }
	}): Promise<void> {
		const getAllUserCenterData = await getAggregate()
		const index = e.currentTarget.dataset.index
		const { linkUrl, append, id, type, linkName } = this.navList[index]
		const itemLink = {
			type: type,
			url: linkUrl,
			append: append,
			id: id,
			name: linkName
		}
		// 积分商城开通
		if (itemLink.url === '/pages/integralShop/integralShop') {
			const shopOpen: boolean = getAllUserCenterData.shopFunctionInfoVo.shop
			if (!shopOpen) {
				uni.showToast({
					title: '积分商城暂未开放',
					icon: 'none'
				})
				return
			}
		}

		const userInfo = this.$STORE.userStore.userInfo
		if (!userInfo.whetherAuthorization) {
			this.authType = true
			return
		}
		if (!itemLink.id) {
			return
		}
		if (itemLink && itemLink.append === 'mall' && itemLink.type === 2) {
			uni.setStorageSync('goMallItemLink', itemLink)
		}

		navtofun(itemLink, this)
		const buttomList = this.$STORE.setStore.tabBar.list

		const jumpToButtom = buttomList.filter((item) => {
			return item.linkName === itemLink.name
		})
		if (jumpToButtom.length > 0) {
			this.$emit('AssembleReload', {}, {})
		}
	}
}
</script>

<style lang="scss" scoped>
@include b(shopNav) {
	margin: 20rpx 0;
	background: #fff;
	display: flex;
	flex-direction: row;
	flex-wrap: wrap;
	justify-content: flex-start;
	padding: 6rpx 20rpx;
}

@include b(shopNav-item) {
	position: relative;
	text-align: center;
	width: 140.5rpx;
	height: 185rpx;
	padding-top: 22rpx;

	@include e(image) {
		width: 100rpx;
		height: 100rpx;
	}

	@include e(text) {
		margin-top: 8rpx;
		font-size: 24rpx;
		color: #000000;
	}
}

@include b(contact-style) {
	position: absolute;
	top: 0;
	left: 0;
	width: 100%;
	height: 100%;
	opacity: 0;
}
</style>
