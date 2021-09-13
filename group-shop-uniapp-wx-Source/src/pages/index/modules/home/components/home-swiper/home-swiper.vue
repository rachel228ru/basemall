<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-12 15:36:21
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-30 14:21:00
 * 123
-->
<template>
	<!-- 首页轮播图 -->
	<view>
		<view v-if="swiperList.length > 0" class="homeSwiper">
			<swiper
				class="homeSwiper-swiper"
				:indicator-dots="false"
				:autoplay="true"
				:circular="false"
				:vertical="false"
				:interval="2000"
				:duration="500"
				:previous-margin="0 + 'px'"
				:next-margin="0 + 'px'"
				@change="onChange"
				:style="'height: ' + height + 'px'"
			>
				<swiper-item
					class="homeSwiper-swiper__item"
					v-for="(item, index) in swiperList"
					:key="index"
					@tap.stop="handleTap"
					:data-index="index"
				>
					<view
						class="homeSwiper-swiper__image"
						:style="'padding: 2px ' + padding + 'px'"
					>
						<image
							mode="aspectFill"
							:data-index="index"
							:data-key="item.img"
							:class="
								'skeleton--animate ' +
									('homeSwiper-swiper--angle' + imageAngle) +
									' ' +
									getImageClass
							"
							:src="item.img"
							:lazy-load="true"
						>
						</image>
						<button
							open-type="contact"
							class="contact-style"
							v-if="item.link.type === 0 && item.link.id === 8"
						></button>
					</view>
				</swiper-item>
			</swiper>
			<view
				v-if="indicator === 1"
				class="homeSwiper-swiper__indicator homeSwiper-swiper__icat1"
			>
				<text
					v-for="(ix, idx) in swiperList.length"
					:key="idx"
					:class="curent === idx ? 'homeSwiper-swiper__icat1--active' : ''"
				>
				</text>
			</view>
			<view
				v-if="indicator === 3"
				class="homeSwiper-swiper__indicator homeSwiper-swiper__icat3"
			>
				<text
					v-for="(ix, idx) in swiperList.length"
					:key="idx"
					:class="curent === idx ? 'homeSwiper-swiper__icat1--active' : ''"
				></text>
			</view>
			<view
				v-if="indicator === 2"
				class="homeSwiper-swiper__indicator homeSwiper-swiper__icat2"
				:style="'right: ' + (padding + 5) + 'px'"
			>
				<text>{{ curent + 1 }}/{{ swiperList.length }}</text>
			</view>
		</view>
	</view>
</template>

<script module="image" lang="wxs" src="@/wxs/image.wxs"></script>

<script lang="ts">
import { Component, Vue, Prop, Watch } from 'vue-property-decorator'
import { navtofun } from '@/utils/navtofun'

import { SwiperListItem, HomeSwiperState } from './homeSwiperType'

@Component({})
export default class HomeSwiper extends Vue implements HomeSwiperState {
	@Prop()
	decoretionProperties!: HomeSwiperState
	@Watch('decoretionProperties', {
		deep: true
	})
	onPropUpdate() {
		this.getProperties(this.decoretionProperties)
		this.getImageClass()
	}

	type = ''
	swiperList: SwiperListItem[] = []
	padding = 10
	imageStyle = 1
	imageAngle = 1
	indicator = 1
	ImageClass = ''
	curent = 0
	height = 185

	// 组件周期函数--监听组件挂载完毕
	mounted() {
		this.getProperties(this.decoretionProperties)
		this.getImageClass()
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 设置数据至组件
	 * @param {HomeSwiperState} formdata
	 * @returns {*}
	 */

	getProperties(formdata: HomeSwiperState): void {
		this.type = formdata.type
		this.swiperList = formdata.swiperList
		this.padding = formdata.padding
		this.imageStyle = formdata.imageStyle
		this.imageAngle = formdata.imageAngle
		this.indicator = formdata.indicator
		this.$forceUpdate()
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 跳转
	 * @param {number} e.currentTarget.dataset.index
	 * @returns {*}
	 */

	handleTap(e: { currentTarget: { dataset: { index: number } } }): void {
		const index: number = e.currentTarget.dataset.index
		const currentSwiperItem = this.swiperList[index]
		const itemLink = currentSwiperItem.link
		navtofun(itemLink, this)
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 获取图片样式
	 * @param {*}
	 * @returns {*}
	 */

	getImageClass(): void {
		// 1常规 2投影
		const styles = ['homeSwiper-swiper__boxNM', 'homeSwiper-swiper__boxCP']
		// 图片倒角 1直角 2圆角
		const angles = ['homeSwiper-swiper__corners', 'homeSwiper-swiper__angle']
		const gs = styles[+this.imageStyle - 1]
		const as = angles[+this.imageAngle - 1]
		this.ImageClass = `${gs} ${as}`
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 轮播切换
	 * @param {number} e.detail.current
	 * @returns {*}
	 */

	onChange(e: { detail: { current: number } }): void {
		this.curent = e.detail.current
	}
}
</script>

<style lang="scss" scoped>
@include b(homeSwiper) {
	background-color: #ffffff;
	position: relative;
}

@include b(homeSwiper-swiper) {
	height: 100%;

	@include e(item) {
		height: 100%;
	}

	@include e(image) {
		width: 100%;
		height: 100%;
		box-sizing: border-box;
		padding-top: 2px;

		image {
			display: block;
			width: 100%;
			height: 100%;
		}
	}

	@include m(angle1) {
		border-radius: 0rpx;
	}

	@include m(angle2) {
		border-radius: 20rpx;
	}

	@include e(boxNM) {
		box-shadow: none;
	}

	@include e(boxCP) {
		box-shadow: 0 0 4px rgba(0, 0, 0, 0.2);
	}

	@include e(corners) {
		border-radius: 0px;
	}

	@include e(angle) {
		border-radius: 7px;
	}

	@include e(indicator) {
		position: absolute;
		display: flex;
		align-items: center;
		justify-content: center;
		bottom: 10px;
		height: 20px;
		width: 100%;
		box-sizing: border-box;
	}

	@include e(icat1) {
		text {
			display: inline-block;
			width: 8px;
			height: 8px;
			background-color: #ebedf0;
			opacity: 0.3;
			margin-right: 6px;
			border-radius: 50%;
		}

		@include m(active) {
			opacity: 0.8 !important;
			background-color: #fff !important;
		}
	}

	@include e(icat2) {
		position: absolute;
		right: 0;
		bottom: 15px;
		display: block;

		text {
			float: right;
			box-sizing: border-box;
			padding: 5px 12px;
			background-color: rgba(0, 0, 0, 0.2);
			color: #fff;
			font-size: 13px;
			border-radius: 16px;
		}
	}

	@include e(icat3) {
		text {
			display: inline-block;
			width: 22px;
			height: 3px;
			background-color: #ebedf0;
			opacity: 0.3;
			margin-right: 6px;
			border-radius: 4px;
		}

		@include m(active) {
			opacity: 0.8 !important;
			background-color: #fff !important;
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
