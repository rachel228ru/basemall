<!--
 * @description: 
 * @Author: vikingShip
 * @Date: 2021-07-28 09:25:02
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-18 20:41:08
-->
<template>
	<!-- 空白占位 -->
	<view :style="inlineStyle"></view>
</template>

<script lang="ts">
import { Component, Vue, Prop, Watch } from 'vue-property-decorator'
import { BlankPaceState, BlankProp } from './blankPaceType'
@Component({})
export default class BlankPaceholder extends Vue implements BlankPaceState {
	@Prop({
		type: Object,
		default: {
			height: 0
		}
	})
	decoretionProperties!: BlankProp
	formData: BlankProp = {
		height: 0
	}
	/** 样式 */
	inlineStyle: string = ''

	/**
	 * @LastEditors: chuyinlong
	 * @description: 监听装修数据变化
	 * @param {*}
	 * @returns {*}
	 */

	@Watch('decoretionProperties', {
		immediate: true
	})
	onPropUpdate(): void {
		this.formData = this.decoretionProperties
	}

	mounted() {
		this.setInlineStyle()
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 导入样式
	 * @param {*}
	 * @returns {*}
	 */

	setInlineStyle(): void {
		const { height } = this.formData
		this.inlineStyle = `height:${height}px;background-color:#fff;`
	}
}
</script>

<style></style>
