<!--
 * @description: 
 * @Author: vikingShip
 * @Date: 2021-07-28 09:25:02
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-18 20:55:41
-->
<template>
	<!-- 富文本组件 -->
	<view style="width:100%;overflow:hidden">
		<rich-text :nodes="decoretionProperties.text"></rich-text>
	</view>
</template>

<script lang="ts">
import { Component, Vue, Prop } from 'vue-property-decorator'
import { richProp } from './richTextType'
@Component({})
export default class RichText extends Vue {
	@Prop({
		type: Object
	})
	decoretionProperties!: richProp
}
</script>

<style></style>
