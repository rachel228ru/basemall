<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-12 15:36:20
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-16 09:44:17
 * 123
-->
<template>
	<view class="box">
		<!-- <image class="icon" src="./icon.png" /> -->
		<text class="text">{{ text }}</text>
	</view>
</template>
<script lang="ts">
import { Component, Vue, Prop } from 'vue-property-decorator'

@Component
export default class Sign extends Vue {
	@Prop()
	text: string
	@Prop()
	icon: string
}
</script>
<style lang="scss" scoped>
.box {
	width: 100%;
	/* position: absolute; */
	/* bottom:0; */
	display: flex;
	justify-content: center;
	align-items: center;
	height: 70rpx;
}

.icon {
	width: 44rpx;
	height: 44rpx;
	vertical-align: bottom;
	margin-right: 10rpx;
}

.text {
	font-size: 22rpx;
	color: #999999;
}
</style>
