<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-12 15:36:20
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-16 09:25:31
 * 123
-->
<template>
	<view class="nav" :style="'height: ' + (status + navHeight) + 'px'">
		<view
			class="status"
			:style="'height: ' + status + 'px;' + containerStyle"
		></view>
		<view
			class="navbar"
			:style="'height:' + navHeight + 'px;' + containerStyle"
		>
			<view class="back-icon" v-if="backIcon" bindtap="back">
				<image :src="backIcon"></image>
			</view>
			<view class="home-icon" v-if="homeIcon" bindtap="home">
				<image :src="homeIcon"></image>
			</view>
			<view class="nav-icon" v-if="titleImg">
				<image :src="titleImg" :style="{ iconStyle }"></image>
			</view>
			<view class="nav-title" v-if="titleText && !titleImg">
				<text :style="{ textStyle }">{{ titleText }}</text>
			</view>
		</view>
	</view>
</template>

<script lang="ts">
import { Component, Vue, Prop } from 'vue-property-decorator'
@Component({})
export default class NavigationBar extends Vue {
	@Prop({
		type: String,
		default: '#ffffff'
	})
	background: string
	@Prop({
		type: String,
		default: 'rgba(0, 0, 0, 1)'
	})
	color: string
	@Prop({
		type: String,
		default: '导航栏'
	})
	titleText: string
	@Prop({
		type: String,
		default: ''
	})
	titleImg: string
	@Prop({
		type: String,
		default: ''
	})
	backIcon: string
	@Prop({
		type: String,
		default: ''
	})
	homeIcon: string
	@Prop({
		type: Number,
		default: 16
	})
	fontSize: number
	@Prop({
		type: Number,
		default: 19
	})
	iconHeight: number
	@Prop({
		type: Number,
		default: 58
	})
	iconWidth: number

	navHeight: number = 0
	status: number = 0
	containerStyle: string = ''
	textStyle: string = ''
	iconStyle: string = ''

	/**
	 * @LastEditors: chuyinlong
	 * @description: 通过获取系统信息计算导航栏高度
	 */

	setNavSize() {
		const sysinfo = uni.getSystemInfoSync()
		const statusHeight = sysinfo.statusBarHeight
		const isiOS = sysinfo.system.indexOf('iOS') > -1
		const navHeight = !isiOS ? 48 : 44

		this.$STORE.setStore.setNavigationBarHeight(statusHeight + navHeight)

		this.status = statusHeight
		this.navHeight = navHeight
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 设置样式至组件
	 */

	setStyle() {
		const containerStyle = ['background:' + this.background].join(';')

		const textStyle = [
			'color:' + this.color,
			'font-size:' + this.fontSize + 'px'
		].join(';')

		const iconStyle = [
			'width: ' + this.iconWidth + 'px',
			'height: ' + this.iconHeight + 'px'
		].join(';')

		this.containerStyle = containerStyle
		this.textStyle = textStyle
		this.iconStyle = iconStyle
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 返回事件
	 */

	back() {
		uni.navigateBack({
			delta: 1
		})
		this.$emit(
			'back',
			{
				back: 1
			},
			{}
		)
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 跳转至首页按钮
	 */

	home() {
		this.$emit('home', {}, {})
	}

	mounted() {
		this.setNavSize()
		this.setStyle()
	}
}
</script>

<style lang="scss" scoped>
.nav {
	position: fixed;
	width: 100%;
	top: 0px;
	left: 0px;
	z-index: 99;
}

.navbar,
.status {
	position: relative;
	transition: all 300ms linear 0s;
}

.back-icon,
.home-icon {
	width: 28px;
	height: 100%;
	position: absolute;
	transform: translateY(-50%);
	top: 50%;
	display: flex;
}

.back-icon {
	left: 16px;
}

.home-icon {
	left: 44px;
}

.back-icon image {
	width: 28px;
	height: 28px;
	margin: auto;
}

.home-icon image {
	width: 20px;
	height: 20px;
	margin: auto;
}

.nav-title,
.nav-icon {
	position: absolute;
	transform: translate(-50%, -50%);
	font-size: 14px;
	left: 50%;
	top: 50%;
	color: #000000;
	transition: all 1s ease 0s;
	font-weight: bold;
}
</style>
