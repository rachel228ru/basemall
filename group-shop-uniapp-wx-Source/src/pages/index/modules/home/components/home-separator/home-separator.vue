<!--
 * @description: 
 * @Author: vikingShip
 * @Date: 2021-07-28 09:25:02
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-18 20:50:42
-->
<template>
	<!-- 分隔符 -->
	<view :style="inlineBox">
		<view :style="inlineStyle"></view>
	</view>
</template>

<script lang="ts">
import { Component, Vue, Prop, Watch } from 'vue-property-decorator'
import { ISeparator, HomeSeparatorType } from './homeSeparatorType'

@Component({})
export default class Separator extends Vue implements HomeSeparatorType {
	@Prop({
		type: Object
	})
	decoretionProperties!: ISeparator

	formData: Partial<ISeparator> = {}
	inlineBox = ''
	inlineStyle = ''

	@Watch('decoretionProperties', {
		immediate: true
	})
	onPropUpdate() {
		this.formData = this.decoretionProperties
		this.getinlineBox()
		this.getinlineStyle()
	}

	mounted() {
		this.getinlineBox()
		this.getinlineStyle()
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 分隔符边框样式
	 * @param {*}
	 * @returns {*}
	 */

	getinlineBox(): void {
		const { hasMargin } = this.formData
		this.inlineBox = `box-sizing: 'border-box'; padding: 10px ${
			hasMargin ? 15 : 0
		}px;`
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 分隔符线条样式
	 * @param {*}
	 * @returns {*}
	 */

	getinlineStyle(): void {
		const { borderColor, borderStyle } = this.formData
		this.inlineStyle = `border-bottom: 1px ${borderStyle} ${borderColor};`
	}
}
</script>

<style></style>
