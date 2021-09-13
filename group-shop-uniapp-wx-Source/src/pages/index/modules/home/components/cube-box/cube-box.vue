<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-12 15:36:21
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-18 20:42:10
 * 123
-->
<template>
	<!-- 魔方组件 -->
	<view class="rc-design-react-preview rc-design-component-default-preview">
		<view class="cap-cube-wrap" v-if="showCubeListWrap.length > 0">
			<view class="cap-cube" :style="pageMarginStyle.style">
				<view
					v-for="(item, index) in showCubeListWrap"
					:key="index"
					class="cap-cube__item"
					:style="item.style"
					@tap.stop="handleTap"
					:data-index="index"
				>
					<!-- <image class="cap-cube__table-image cap-cube__table-image--invisible" src="{{item.img}}" wx:if="{{item.img}}" mode="aspectFill" /> -->
					<button
						open-type="contact"
						class="contact-style"
						v-if="
							subEntry[index].link.type === 0 && subEntry[index].link.id === 8
						"
					></button>
				</view>
			</view>
		</view>
	</view>
</template>

<script lang="ts">
import { Component, Vue, Prop, Watch } from 'vue-property-decorator'
import { navtofun } from '@/utils/navtofun'

import {
	CubeStateType,
	IBanners,
	ShowCubeItemType,
	pageMarginStyle
} from './cubeBoxType'

@Component({})
export default class CubeBox extends Vue implements CubeStateType {
	@Prop({
		type: Object
	})
	decoretionProperties!: CubeStateType

	borderWidth: number = 0
	layoutWidth: number = 0
	layoutHeight: number = 0
	showMethod: number = 0
	pageMargin: number = 0
	width: number = 0
	subEntry: IBanners[] = []
	showCubeListWrap: ShowCubeItemType[] | undefined = []
	pageMarginStyle: pageMarginStyle | undefined = {
		style: `height:200px`
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 监听装修数据变化
	 * @param {*}
	 * @returns {*}
	 */

	@Watch('decoretionProperties')
	onPropUpdate(): void {
		this.getProperties(this.decoretionProperties)
		this.drawCube()
	}

	mounted() {
		this.getProperties(this.decoretionProperties)
		this.drawCube()
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 设置装修数据至组件
	 * @param {CubeStateType} formdata
	 * @returns {*}
	 */

	getProperties(formdata: CubeStateType): void {
		this.borderWidth = formdata.borderWidth
		this.layoutWidth = formdata.layoutWidth
		this.layoutHeight = formdata.layoutHeight
		this.showMethod = formdata.showMethod
		this.pageMargin = formdata.pageMargin
		this.width = formdata.width
		this.subEntry = formdata.subEntry
		this.showCubeListWrap = formdata.showCubeListWrap
		this.pageMarginStyle = formdata.pageMarginStyle
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 获取系统信息
	 * @param {*}
	 * @returns {*}
	 */

	async drawCube(): Promise<void> {
		uni.getSystemInfo({
			success: (res) => {
				const perviewLayoutWidth = res.windowWidth
				const layoutWidth = this.layoutWidth
				const layoutHeight = this.layoutHeight
				const wrapWith =
					perviewLayoutWidth + this.borderWidth - this.pageMargin * 2
				const styleWidth = wrapWith / layoutWidth
				const styleHeight =
					layoutHeight !== 1 ? perviewLayoutWidth / layoutHeight : styleWidth
				this.drawCubeWrap(styleWidth, styleHeight, wrapWith)
			}
		})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 动态计算图片规格
	 * @param {number} divWidth
	 * @param {number} divHeight
	 * @param {number} wrapWith
	 * @returns {*}
	 */

	drawCubeWrap(divWidth: number, divHeight: number, wrapWith: number): void {
		const subEntry: IBanners[] = this.subEntry
		const showCubeListWrap: ShowCubeItemType[] = []
		let maxY = 0,
			maxIndex = 0,
			maxHeight = 0
		if (subEntry.length) {
			for (let i = 0; i < subEntry.length; i++) {
				const a: IBanners = subEntry[i]
				const coverDiv = {
					top: a.y * divHeight + 'px',
					left: a.x * divWidth + this.pageMargin + 'px',
					width: divWidth * a.width - this.borderWidth + 'px',
					height: divHeight * a.height - this.borderWidth + 'px',
					paddingTop: (divHeight * a.height) / 2 + 'px',
					img: a[`img`] ? a[`img`] : '',
					borderWidth: this.borderWidth / 2 + 'px',
					style: `width:${divWidth * a.width -
						this.borderWidth}px;height:${divHeight * a.height -
						this.borderWidth}px;top:${a.y * divHeight}px;left:${a.x * divWidth +
						this.pageMargin}px;magin:${
						this.borderWidth
					}px;background-image:url(${a[`img`]});`
				}
				if (maxY <= a.y) {
					maxY = a.y
					maxIndex = i
				}
				showCubeListWrap.push(coverDiv)
			}
			maxHeight =
				maxY + subEntry[maxIndex].height < this.layoutHeight
					? maxY + subEntry[maxIndex].height
					: this.layoutHeight
			this.showCubeListWrap = showCubeListWrap
			this.pageMarginStyle = {
				width: wrapWith,
				height: divHeight * maxHeight,
				margin: `-${this.borderWidth / 2}px`,
				style: `width:${wrapWith}px;height:${divHeight * maxHeight -
					this.borderWidth}px;`
			}
		}
	}

	/**
	 * 跳转
	 */
	/**
	 * @LastEditors: chuyinlong
	 * @description: 跳转
	 * @param {index: number} e
	 * @returns {*}
	 */

	handleTap(e: { currentTarget: { dataset: { index: number } } }): void {
		const index: number = e.currentTarget.dataset.index
		const currentSwiperItem = this.subEntry[index]
		const itemLink = currentSwiperItem.link
		navtofun(itemLink, this)
	}
}
</script>

<style lang="scss" scoped>
.rc-design-react-preview {
	position: relative;
	width: 100%;
	overflow: hidden;
	background: #fff;
}

.cap-cube__item {
	position: absolute;
	background-repeat: no-repeat;
	background-size: cover;
	background-position: 50%;
	overflow: hidden;
}

image {
	width: 100%;
}

.cap-cube {
	position: relative;
}

@include b(contact-style) {
	position: relative;
	top: 0;
	left: 0;
	width: 100%;
	height: 100%;
	opacity: 0;
}
</style>
