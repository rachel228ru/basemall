<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-12 15:36:20
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-20 14:11:31
 * 123
-->
<template>
	<view @click="clickHandler">
		<text
			:class="'icon iconfont ' + [name]"
			:style="'font-size:' + size + '; color:' + color"
		></text>
	</view>
</template>

<script lang="ts">
import { Component, Prop, Vue } from 'vue-property-decorator'

@Component({})
export default class MIcon extends Vue {
	@Prop({
		type: String
	})
	name!: string
	@Prop({
		type: String,
		default: '16px'
	})
	size!: string
	@Prop()
	color: string = ''

	externalClasses = ['icon']

	/**
	 * @LastEditors: chuyinlong
	 * @description: 点击反馈
	 */

	clickHandler() {
		this.$emit('click', {})
	}
}
</script>

<style lang="scss">
@import '@/assets/styles/font/iconfont.scss';
</style>
