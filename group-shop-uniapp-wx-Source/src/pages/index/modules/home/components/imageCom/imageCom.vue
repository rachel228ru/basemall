<!--
 * @description: 
 * @Author: vikingShip
 * @Date: 2021-07-28 09:25:02
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-30 14:21:08
-->
<template>
	<view class="boxContent" :style="'height:' + imageContent.boxHeight + 'px'">
		<image
			@tap="clickImg"
			:data-item="imageContent"
			:src="imageContent.img"
			:style="
				'width:' +
					imageContent.width +
					';height:' +
					imageContent.height +
					';margin-left:' +
					imageContent.newLeft +
					'px;margin-top:' +
					imageContent.top +
					'px'
			"
		>
		</image>
	</view>
</template>

<script lang="ts">
import { Component, Vue, Prop } from 'vue-property-decorator'
import { navtofun } from '@/utils/navtofun'
import { ImageInfo } from './imageComType'
@Component({})
export default class ImageCom extends Vue {
	@Prop({
		type: Object
	})
	decoretionProperties!: ImageInfo

	imageContent: ImageInfo = {} as ImageInfo

	// 组件周期函数--监听组件挂载完毕
	mounted() {
		this.getImageCom()
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 获取系统信息
	 * @param {*}
	 * @returns {*}
	 */

	getImageCom(): void {
		uni.getSystemInfo({
			success: (res) => {
				try {
					let pWidth = 0
					pWidth = res.windowWidth
					const decoretionProperties = this.decoretionProperties
					const width = decoretionProperties.width
					const widthStr = width.match(/(\S*)px/)
					let imgWidth = 0
					if (widthStr) {
						imgWidth = Number(widthStr[1])
					}

					const newLeft =
						(decoretionProperties.left * (pWidth - imgWidth)) / (371 - imgWidth)
					decoretionProperties.newLeft = newLeft.toFixed(0)
					if (decoretionProperties.width === '371px') {
						decoretionProperties.width = '100%'
					}
					this.imageContent = decoretionProperties
				} catch (err) {
					console.log(err)
				}
			}
		})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 点击图片跳转链接
	 * @param {ImageInfo} e.currentTarget.dataset.item
	 * @returns {*}
	 */

	clickImg(e: { currentTarget: { dataset: { item: ImageInfo } } }): void {
		const item: ImageInfo = e.currentTarget.dataset.item
		const itemLink = item.link
		navtofun(itemLink, this)
	}
}
</script>

<style lang="scss" scoped>
.boxContent {
	background-color: white;
}
</style>
