<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-12 15:36:21
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-30 14:21:04
 * 123
-->
<template>
	<!-- 首页轮播图 -->

	<view v-if="swiperList.length" class="homeSwiper">
		<block v-if="radio === 1">
			<swiper
				class="homeSwiper-swiper"
				:indicator-dots="false"
				:autoplay="true"
				:circular="false"
				:vertical="false"
				:interval="interval * 1000"
				:duration="500"
				:previous-margin="0 + 'px'"
				:next-margin="0 + 'px'"
				@change="onChange"
				:style="'height: ' + height + 'rpx'"
			>
				<block v-for="(item, index) in swiperList" :key="index">
					<swiper-item
						class="homeSwiper-swiper__item"
						@tap.stop="handleTap"
						:data-index="index"
					>
						<view
							class="homeSwiper-swiper__image"
							:style="'padding: 4rpx ' + padding + 'rpx'"
						>
							<image
								:data-index="index"
								:data-key="item.img"
								:class="
									'skeleton--animate ' +
										('homeSwiper-swiper--angle' + imageAngle) +
										' ' +
										getImageClass
								"
								:src="item.img"
								mode="aspectFill"
								:lazy-load="true"
							></image>
							<!-- 骨架屏，体验有问题，暂时注释 -->
							<!-- <skeleton height="{{height/2}}" loaded="{{item.loaded}}" skeletonKey="{{item.img}}">
          </skeleton> -->
							<button
								open-type="contact"
								class="contact-style"
								v-if="item.link.type === 0 && item.link.id === 8"
							></button>
						</view>
					</swiper-item>
				</block>
			</swiper>
		</block>
		<block v-if="radio == 2">
			<view
				class="kuang"
				:style="'height: ' + height + 'rpx;padding: ' + sidePadding + 'rpx;'"
			>
				<scroll-view
					class="homeSwiper-swiper"
					:enable-flex="true"
					:scroll-x="true"
					:enhanced="true"
					:show-scrollbar="false"
					@change="onChange"
					:style="
						'height: ' + height + 'rpx;background-image: url(' + btnImg + ');'
					"
				>
					<!-- <view class="homeSwiper-swiper__view"> -->
					<block v-for="(item, index) in swiperList" :key="index">
						<view
							class="homeSwiper-swiper__item"
							@tap.stop="handleTap"
							:data-index="index"
							:style="'width:750rpx;margin-right: ' + margin + 'rpx;'"
						>
							<view
								class="homeSwiper-swiper__image"
								:style="'padding: 0 ' + padding + 'rpx'"
							>
								<image
									:data-index="index"
									:data-key="item.img"
									:class="
										'skeleton--animate ' +
											('homeSwiper-swiper--angle' + imageAngle) +
											' ' +
											getImageClass
									"
									:src="item.img"
									mode="aspectFill"
									:lazy-load="true"
								></image>
								<button
									open-type="contact"
									class="contact-style"
									v-if="item.link.type === 0 && item.link.id === 8"
								></button>
							</view>
						</view>
					</block>
					<view style="width:375rpx;height:100%;display:inline-block;"></view>
					<!-- </view> -->
				</scroll-view>
			</view>
		</block>
		<block v-if="radio === 3">
			<swiper
				class="homeSwiper-swiper"
				:indicator-dots="false"
				:autoplay="true"
				:circular="true"
				:vertical="false"
				:interval="interval * 1000"
				:duration="500"
				:previous-margin="200 + 'rpx'"
				:next-margin="200 + 'rpx'"
				@change="onChange"
				:style="'height: ' + height + 'rpx'"
			>
				<block v-for="(item, index) in swiperList" :key="index">
					<swiper-item
						class="homeSwiper-swiper__item"
						@tap.stop="handleTap"
						:data-index="index"
					>
						<view
							class="homeSwiper-swiper__image"
							:style="
								'transform:' +
									(curent === index ? 'scale(1, 1)' : 'scale(0.8, 0.8)') +
									';margin:auto'
							"
						>
							<image
								:data-index="index"
								:data-key="item.img"
								:class="
									'skeleton--animate ' +
										('homeSwiper-swiper--angle' + imageAngle) +
										' ' +
										getImageClass
								"
								:src="item.img"
								mode="aspectFill"
								:lazy-load="true"
							></image>
							<!-- 骨架屏，体验有问题，暂时注释 -->
							<!-- <skeleton height="{{height/2}}" loaded="{{item.loaded}}" skeletonKey="{{item.img}}">
          </skeleton> -->
							<button
								open-type="contact"
								class="contact-style"
								v-if="item.link.type === 0 && item.link.id === 8"
							></button>
						</view>
					</swiper-item>
				</block>
			</swiper>
		</block>
		<view v-if="radio === 3"></view>
		<view v-else-if="radio === 2"></view>
		<view
			v-else-if="indicator === 1"
			class="homeSwiper-swiper__indicator homeSwiper-swiper__icat1"
		>
			<text
				v-for="(ix, idx) in swiperList.length"
				:key="idx"
				:class="curent === idx ? 'homeSwiper-swiper__icat1--active' : ''"
			></text>
		</view>
		<view
			v-else-if="indicator === 3"
			class="homeSwiper-swiper__indicator homeSwiper-swiper__icat3"
		>
			<text
				v-for="(ix, idx) in swiperList.length"
				:key="idx"
				:class="curent === idx ? 'homeSwiper-swiper__icat1--active' : ''"
			></text>
		</view>
		<view
			v-else-if="indicator === 2"
			class="homeSwiper-swiper__indicator homeSwiper-swiper__icat2"
			:style="'right: ' + padding + 10 + 'rpx'"
		>
			<text>{{ curent + 1 }}/{{ swiperList.length }}</text>
		</view>
	</view>
</template>

<script module="image" lang="wxs" src="@/wxs/image.wxs"></script>

<script lang="ts">
import { navtofun } from '@/utils/navtofun'
import { Component, Vue, Prop, Watch } from 'vue-property-decorator'
import { HomeSwiperFormData, SwiperListItem } from './goodSwiper'

@Component({})
export default class GoodsSwiper extends Vue implements HomeSwiperFormData {
	@Prop({
		type: Object
	})
	decoretionProperties!: HomeSwiperFormData

	type = ''
	swiperList: SwiperListItem[] = []
	padding = 10
	imageStyle = 1
	imageAngle = 1
	indicator = 1
	ImageClass = ''
	curent = 0
	height = 185
	radio = 1
	btnImg = ''
	interval = 1
	sidePadding = 10
	margin = 5

	@Watch('decoretionProperties')
	onPropUpdate() {
		this.getProperties(this.decoretionProperties)
		this.getImageClass()
	}

	mounted() {
		this.getProperties(this.decoretionProperties)
		this.getImageClass()
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 设置数据至组件
	 * @param {HomeSwiperFormData} formdata
	 * @returns {*}
	 */

	getProperties(formdata: HomeSwiperFormData): void {
		this.type = formdata.type
		this.swiperList = formdata.swiperList
		this.padding = formdata.padding
		this.imageStyle = formdata.imageStyle
		this.imageAngle = formdata.imageAngle
		this.indicator = formdata.indicator
		this.height = formdata.height
		this.radio = formdata.radio
		this.btnImg = formdata.btnImg
		this.interval = formdata.interval
		this.sidePadding = formdata.sidePadding
		this.margin = formdata.margin
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description:
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
	white-space: nowrap;
	background-size: 100% 100%;

	@include e(view) {
		height: 100%;
		display: flex;
		white-space: nowrap;
	}

	@include e(item) {
		height: 100%;
		display: inline-block;
	}

	@include e(image) {
		width: 100%;
		height: 100%;
		box-sizing: border-box;
		padding-top: 2px;
		transition: transform 0.5s;

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

.kuang {
	width: 100%;
}</style
>>
