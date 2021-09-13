<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-12 15:36:21
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-30 14:28:13
 * 123
-->
<template>
	<view class="my--setting">
		<view class="info">允许“小程序名称”使用我的地理位置</view>
		<view class="cover--group">
			<view class="cover--group__cell" @tap.stop="toAddress" v-if="showAddress">
				<view class="group__cell--left">
					<m-icon
						name="icongerenzhongxin-dizhiguanli"
						class="icon cell--left--icon"
					></m-icon>
					<text class="cell--left--text">地址管理</text>
				</view>
				<van-icon class="group__cell--right" name="arrow"></van-icon>
			</view>
			<view class="cover--group__cell">
				<view class="group__cell--left">
					<m-icon name="icondaohang" class="icon cell--left--icon"></m-icon>
					<text class="cell--left--text">使用我的地理位置</text>
				</view>
				<van-switch
					:checked="status"
					size="21px"
					@change="onChange"
				></van-switch>
			</view>
			<view class="cover--group__cell" @tap="clearStorage">
				<view class="group__cell--left">
					<m-icon name="iconshanchu" class="icon cell--left--icon"></m-icon>
					<text class="cell--left--text">清除缓存</text>
				</view>
			</view>
		</view>
		<van-dialog
			use-slot
			title="提示"
			:show="showDialog"
			confirm-button-open-type="openSetting"
			show-cancel-button
			@opensetting="opensetting"
		>
			<view class="setting-dialog__tip">前往设置位置授权</view>
		</van-dialog>
	</view>
</template>
<script lang="ts">
import { Component, Vue } from 'vue-property-decorator'
import mIcon from '@/components/m-icon/m-icon.vue'
import { getSetting } from '@/utils/helper'
@Component({
	components: {
		mIcon
	}
})
export default class MySetting extends Vue {
	// 地理位置授权状态
	status: boolean = false

	showDialog: boolean = false

	showAddress: boolean = true

	async onLoad(options: { type: string }) {
		if (options.type === '1') {
			this.setData({ showAddress: false })
		} else {
			this.setData({ showAddress: true })
		}
		const res = (await getSetting()) as any
		if (!res.authSetting['scope.userLocation']) {
			this.setData({ status: false })
		} else if (res.authSetting['scope.userLocation']) {
			this.setData({ status: true })
		}
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 开关获取地理位置
	 */
	onChange() {
		this.setData({ showDialog: true })
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 打开用户设置
	 * @param {*} e
	 */

	opensetting(e: { detail: { authSetting: { [x: string]: any } } }) {
		if (e.detail.authSetting['scope.userLocation']) {
			this.setData({ status: true })
		} else {
			this.setData({ status: false })
		}
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 前往地址管理
	 */
	toAddress() {
		uni.navigateTo({
			url: '/subcontract/pages/address/address'
		})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 清除缓存
	 */

	clearStorage() {
		try {
			uni.clearStorageSync()
			uni.showToast({
				title: '清除成功',
				icon: 'success',
				duration: 2000
			})
		} catch (e) {
			uni.showToast({
				title: '清除失败',
				icon: 'none',
				duration: 2000
			})
		}
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

$default-font-size: 32rpx;
$page-text-color: #45403c;

page {
	height: 100%;
}

.my--setting {
	background: #f5f5f5;
	min-height: 100%;
}

.info {
	font-size: 24rpx;
	color: rgba(69, 64, 60, 0.6);
	padding: 16rpx 30rpx;
}

.cover--group {
	border-radius: 5px;
	margin-bottom: 15rpx;

	.cover--group__cell {
		@include flex(space-between, center);
		background-color: #ffffff;
		height: 100rpx;
		padding: 30rpx 25rpx 30rpx 30rpx;

		.group__cell--left {
			@include flex(center, center);

			.cell--left--icon {
				width: 45rpx;
				height: 45rpx;
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

	.cover--group__cell:not(:last-child) {
		@include bottom-line;
	}
}

.setting-dialog__tip {
	text-align: center;
	line-height: 88rpx;
}
</style>
