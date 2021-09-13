<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-12 15:36:20
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-16 09:21:57
 * 123
-->
<template>
	<view class="loading">
		<image src="https://oss-tencent.bgniao.cn/api/loading.svg"></image>
	</view>
</template>
<script lang="ts">
import { Component, Vue } from 'vue-property-decorator'
@Component
export default class Index extends Vue {}
</script>
<style lang="scss" scoped>
.loading {
	@include flex;
	width: 100vw;
	height: 100vh;
	position: fixed;
	left: 0;
	top: 0;
	z-index: 99;
	background: #fff;

	image {
		width: 100px;
		height: 100px;
	}
}
</style>
