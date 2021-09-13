<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-12 15:36:21
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-18 20:40:52
 * 123
-->
<template>
	<view>
		<canvas canvas-id="shareCanvas" class="share--card"></canvas>
		<view
			class="absolute backgournd"
			:hidden="cardFlag"
			catchtouchmove="preventTouchMove"
		>
			<view class="absolute backgournd" catchtouchmove="preventTouchMove">
				<canvas canvas-id="cardCanvas" class="share--canvas"></canvas>
			</view>
		</view>
		<view
			style="width:380px;height:600px"
			:hidden="cardFlag"
			catchtouchmove="preventTouchMove"
			class="absolute center--middle"
		>
			<view class="absolute" v-if="showAll">
				<view>
					<view class="icon--end">
						<i
							class="iconfont icon-icon-close11"
							@tap="hiddenCrad"
							style="font-size:52rpx;color:white"
						></i>
					</view>
					<view class="icon--end">
						<i
							class="iconfont icon-shudian"
							style="font-size:52rpx;color:white"
						></i>
					</view>
				</view>
				<image
					style="width:560px;height:880px"
					@tap.stop="cardClick"
					:src="cardImgUrl"
				></image>
				<view
					class=" downButton"
					@tap.stop="savePhoto"
					style="margin-top:40rpx;margin-left:120rpx"
				>
					<view class="downButton--content">
						<i class="iconfont icon-xiazai"></i>
						<view style="margin-left:20rpx">保存至本地</view>
					</view>
				</view>
			</view>
		</view>
	</view>
</template>
<script lang="ts">
import { Component, Vue, Prop } from 'vue-property-decorator'

@Component
export default class CanvasCom extends Vue {
	@Prop()
	cardFlag!: boolean
	@Prop()
	showAll!: boolean
}
</script>
<style scoped></style>
