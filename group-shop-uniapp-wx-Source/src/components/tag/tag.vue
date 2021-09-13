<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-12 15:36:20
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-16 09:49:55
 * 123
-->
<template>
	<view>
		<text class="tag">{{ label }}</text>
	</view>
</template>
<script lang="ts">
import { Component, Vue, Prop } from 'vue-property-decorator'

@Component
export default class Tag extends Vue {
	@Prop()
	label: string
}
</script>
<style lang="scss" scoped>
@include b(tag) {
	background: $main-light-color;
	color: #fff;
	display: inline-block;
	padding: 1px 2px;
	margin-right: 5px;
}
</style>
