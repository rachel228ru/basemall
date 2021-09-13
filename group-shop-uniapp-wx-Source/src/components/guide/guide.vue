<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-12 15:36:20
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-16 09:21:47
 * 123
-->
<template>
	<view>
		<swiper :indicator-dots="true" class="guide" easing-function="linear">
			<block v-for="(item, index) in guideList" :key="index">
				<swiper-item class="guide__item">
					<image :src="item.url" @tap="execute" :data-item="item"></image>
				</swiper-item>
			</block>
		</swiper>
		<view class="guide__jump" @tap="jump">{{ jumpTime }}s | 跳过</view>
	</view>
</template>
<script lang="ts">
import { Component, Prop, Vue } from 'vue-property-decorator'
import { navtofun } from '@/utils/navtofun'
import { GuideListItemType } from '@/store/modulesType/settingType'

@Component({})
export default class Guide extends Vue {
	@Prop({
		type: Array,
		default: []
	})
	guideList: Array<GuideListItemType>

	jumpTime: number = 3

	IntervalTime: number

	/**
	 * @LastEditors: chuyinlong
	 * @description: 跳过
	 * @param {}
	 * @returns {}
	 */

	jump(): void {
		clearInterval(this.IntervalTime)
		this.$STORE.setStore.setGuideVisible(false)
	}

	created() {
		this.IntervalTime = setInterval(() => {
			if (this.jumpTime > 0) {
				this.jumpTime = this.jumpTime - 1
			} else {
				this.jump()
			}
		}, 1000)
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 跳转链接
	 * @param {*}
	 * @returns {*}
	 */

	execute({
		currentTarget: {
			dataset: { item }
		}
	}: {
		currentTarget: {
			dataset: {
				item: GuideListItemType
			}
		}
	}) {
		if (item.type === 0 && item.link) {
			if (JSON.parse(item.link).type === 0) {
				try {
					const { url, append, name } = JSON.parse(item.link)

					uni.navigateTo({
						url: `/subcontract${(url[0] === '/' ? '' : '/') +
							url}?page=${append}`,
						success: () => {
							this.jump()
							this.$STORE.setStore.setIndexTitle(name)
						}
					})
				} catch {
					this.$Popup.$toast('跳转失败')
					uni.reLaunch({
						url: '/pages/index/index'
					})
				}
			} else {
				const itemLink = JSON.parse(item.link)
				this.jump()
				navtofun(itemLink, this)
			}
		}

		if (item.type === 1 && item.appId) {
			uni.navigateToMiniProgram({
				appId: item.appId
			})
		}
	}
}
</script>
<style lang="scss" scoped>
@include b(guide) {
	width: 100vw;
	height: 100vh;
	background: #fff;
	position: fixed;
	z-index: 9999;
	top: 0;
	left: 0;

	@include e(item) {
		image {
			width: 100%;
			height: 100%;
		}
	}

	@include e(jump) {
		position: fixed;
		z-index: 10001;
		right: 10px;
		top: 190rpx;
		background: rgba($color: #000000, $alpha: 0.5);
		padding: 5px 15px;
		color: #fff;
		border-radius: 15px;
		font-size: 14px;
	}
}
</style>
