<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-12 15:36:21
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-30 14:29:35
 * 123
-->
<template>
	<view>
		<div class="swiper__list-page" v-if="dataList.length">
			<swiper
				class="homeSwiper-swiper"
				:indicator-dots="true"
				indicator-color="#fff"
				:autoplay="true"
				:circular="false"
				:vertical="false"
				:interval="3000"
				:duration="500"
				:previous-margin="0 + 'px'"
				:next-margin="0 + 'px'"
				style="height: 110px;"
			>
				<block v-for="(item, index) in dataList" :key="index">
					<swiper-item
						class="homeSwiper-swiper__item"
						@tap.stop="handleTap"
						:data-index="index"
					>
						<view
							class="homeSwiper-swiper__image"
							:style="'padding: 2px ' + padding + 'px'"
						>
							<image
								class="homeSwiper-swiper--angle2 homeSwiper-swiper__angle"
								:src="item.img"
								mode="scaleToFill"
								lazy-load="true"
							></image>
						</view>
					</swiper-item>
				</block>
			</swiper>
		</div>
	</view>
</template>

<script lang="ts">
import { Component, Vue, Prop, Watch } from 'vue-property-decorator'
import { navtofun } from '@/utils/navtofun'
import {
	BusinessSuper,
	SwiperListItem
} from '@/pages/index/modules/mall/mallType'

@Component({})
export default class SwiperList extends Vue {
	@Prop()
	propData!: BusinessSuper
	formData = {} as BusinessSuper
	dataList: Array<SwiperListItem> = []
	isFirst: boolean = true

	@Watch('propData', { deep: true })
	getFormData() {
		if (this.propData) {
			this.setData({
				dataList: [],
				formData: this.propData
			})
			if (!this.formData.firstCatList.length) return
			const dataList = this.isFirst
				? this.formData.firstCatList[0].swiperList
				: this.formData.currentFirstCategory.swiperList
			this.setData(
				{
					dataList
				},
				() => {
					this.isFirst = false
				}
			)
		}
	}

	mounted() {
		this.getFormData()
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description:
	 * @param {*} e
	 */

	handleTap(e: { currentTarget: { dataset: { index: number } } }) {
		const index: number = e.currentTarget.dataset.index
		const currentSwiperItem = this.dataList[index]
		const itemLink = currentSwiperItem.link
		navtofun(itemLink, this, 'poster')
	}
}
</script>

<style lang="scss" scoped>
.swiper__list-page {
	@include b(homeSwiper) {
		background-color: #ffffff;
		position: relative;
	}

	@include b(homeSwiper-swiper) {
		height: 100%;
		margin-bottom: 30px;

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
}
</style>
