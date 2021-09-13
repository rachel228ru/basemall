<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-12 15:36:21
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-17 16:52:14
 * 123
-->
<template>
	<view>
		<image
			class="img"
			src="https://medusa-small-file.oss-cn-hangzhou.aliyuncs.com/images/banner.jpg"
		></image>
		<cover-view class="btn" @tap.stop="makeCall"></cover-view>
	</view>
</template>
<script lang="ts">
import { Component, Vue } from 'vue-property-decorator'

@Component
export default class SystemBanner extends Vue {
	/**
	 * @LastEditors: chuyinlong
	 * @description: 拨打电话
	 * @param {*}
	 * @returns {*}
	 */

	makeCall(): void {
		uni.makePhoneCall({
			phoneNumber: '18967889883'
		})
	}
}
</script>
<style lang="scss" scoped>
page {
	position: relative;
}

.img {
	width: 100vw;
	height: 4114rpx;
}
.btn {
	position: absolute;
	width: 100%;
	height: 102rpx;
	bottom: 61rpx;
	z-index: 999;
}
</style>
