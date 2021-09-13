<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-12 15:36:21
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-19 14:11:26
 * 123
-->
<template>
	<view class="container">
		<web-view :src="webSrc"></web-view>
	</view>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator'

@Component({})
export default class LinkPage extends Vue {
	webSrc: string = ''

	onLoad(opition: { src: string }) {
		if (opition.src) {
			this.setData({
				webSrc: opition.src
			})
		}
	}
}
</script>

<style lang="scss" scoped>
.container {
	width: 100%;
	height: 100%;
	overflow-x: hidden;
}
</style>
