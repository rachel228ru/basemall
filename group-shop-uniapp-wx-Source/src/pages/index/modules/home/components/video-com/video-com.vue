<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-12 15:36:21
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-30 14:21:20
 * 123
-->
<template>
	<view
		class="video--com"
		:style="'padding:' + (radioTC === 2 ? 20 : 0) + 'rpx'"
	>
		<video
			:src="radio === 1 ? video : videoLink"
			:style="'width:' + videowidth + 'rpx;height:' + videoheight + 'rpx'"
			:poster="poster"
		></video>
	</view>
</template>

<script lang="ts">
import { Component, Vue, Prop, Watch } from 'vue-property-decorator'
import { videoProp } from './videoCom'

@Component({})
export default class VideoCom extends Vue implements videoProp {
	@Prop({
		type: Object
	})
	decoretionProperties!: videoProp

	VideoCom = ''
	height = ''
	poster = ''
	radio = 1
	radioBL = 1
	radioTC = 1
	video = ''
	videoLink = ''
	width = ''
	videowidth = 750
	videoheight = 0

	@Watch('decoretionProperties')
	onPropUpdate() {
		this.getProperties(this.decoretionProperties)
		this.getvideo()
	}

	mounted() {
		this.getProperties(this.decoretionProperties)
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 设置数据至组件
	 * @param {videoProp} formdata
	 * @returns {*}
	 */

	getProperties(formdata: videoProp): void {
		this.VideoCom = formdata.VideoCom
		this.height = formdata.height
		this.poster = formdata.poster
		this.radio = formdata.radio
		this.radioBL = formdata.radioBL
		this.radioTC = formdata.radioTC
		this.video = formdata.video
		this.videoLink = formdata.videoLink
		this.width = formdata.width
		this.getvideo()
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 获取配置信息
	 * @param {*}
	 * @returns {*}
	 */
	getvideo(): void {
		if (this.radioTC === 2) {
			this.videowidth = 710
		}
		if (this.radioBL === 1) {
			this.videoheight = (this.videowidth / 16) * 9
		}
		if (this.radioBL === 2) {
			this.videoheight = this.videowidth
		}
		if (this.radioBL === 3) {
			const url = this.radio === 1 ? this.video : this.videoLink
			uni.downloadFile({
				//需要先下载文件获取临时文件路径 单个文件大小不得超过50M
				url: url,
				success: (res1) => {
					this.getVideoInfo(res1.tempFilePath)
				}
			})
		}
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 获取视频宽高比
	 * @param {string} url
	 * @returns {*}
	 */
	getVideoInfo(url: string): void {
		uni.getVideoInfo({
			src: url,
			success: (res) => {
				const bit = res.height / res.width
				this.videoheight = this.videowidth * bit
			},
			fail(err) {
				console.log(err)
			}
		})
	}
}
</script>

<style lang="scss" scoped>
.video--com {
	width: 100%;
	background-color: #fff;
}
</style>
