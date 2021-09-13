<!--
 * @description: 
 * @Author: vikingShip
 * @Date: 2021-07-28 09:25:02
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-18 20:57:37
-->
<template>
	<view
		:style="
			'color:' +
				decoretionProperties.color +
				';background-color:' +
				decoretionProperties.backgroundColor +
				';text-align:center; font-size: 14px;font-weight: 800;position:relative'
		"
	>
		<van-cell
			v-if="decoretionProperties.showStyle === 'is-style-one'"
			class="spellpre__header"
			:style="'color: ' + decoretionProperties.color"
			center
			:title="decoretionProperties.titleName"
		>
			<!-- {{decoretionProperties.titleName}} -->
		</van-cell>
		<view
			v-else
			class="spellpre__header"
			@tap.stop="handleClick"
			:style="'color: ' + decoretionProperties.color"
		>
			<text class="spellpre__header--title">{{
				decoretionProperties.titleName
			}}</text>
			<text class="spellpre__header--more">查看更多 ></text>
			<view class="add__line"></view>
		</view>
		<button
			open-type="contact"
			class="contact-style"
			v-if="decoretionProperties.type === 0 && decoretionProperties.id === 8"
		></button>
	</view>
</template>
<script lang="ts">
import { Component, Vue, Prop } from 'vue-property-decorator'
import { navtofun } from '@/utils/navtofun'
import { titleProp } from './titleBarType'

@Component({})
export default class TitleBar extends Vue {
	@Prop({
		type: Object
	})
	decoretionProperties!: titleProp

	/**
	 * @LastEditors: chuyinlong
	 * @description: 查看更多
	 * @param {*}
	 * @returns {*}
	 */
	handleClick(): void {
		const { pathLink, append, id, type, name } = this.decoretionProperties
		const itemLink = {
			type: type,
			url: pathLink,
			append: append,
			id: id,
			name: name
		}
		navtofun(itemLink, this)
	}
}
</script>

<style lang="scss" scoped>
@include b(spellpre) {
	box-sizing: border-box;
	background-color: #fff;

	@include e(header) {
		color: #333;
		height: 40px;
		line-height: 40px;
		position: relative;

		@include m(title) {
			float: left;
			font-size: 18px;
			font-weight: 700;
			margin-left: 20px;
		}

		@include m(more) {
			font-size: 14px;
			font-weight: 400;
			float: right;
			margin-right: 20px;
		}

		.add__line {
			height: 100%;
			width: 4px;
			background-color: red;
			box-sizing: border-box;
			position: absolute;
			left: 0;
			top: 0;
		}
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
