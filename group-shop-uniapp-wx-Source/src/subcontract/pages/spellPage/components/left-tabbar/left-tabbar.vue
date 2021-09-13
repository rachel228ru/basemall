<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-12 15:36:21
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-09-01 16:03:45
 * 123
-->
<template>
	<view>
		<scroll-view
			class="left__mode-page"
			scroll-y="true"
			:scroll-top="scrollTop"
			:style="styleType.style2"
		>
			<view
				v-for="(item, index) in HeadList"
				:key="index"
				class="tab__item"
				:style="item.id === activeId ? styleType.style1 : styleType.style2"
				@tap="changeActiveItem"
				:data-item="item"
			>
				<text
					:style="
						item.id === activeId
							? 'background-color: #FC425A'
							: styleType.style2
					"
				></text>
				{{ item.name }}
			</view>
		</scroll-view>
	</view>
</template>

<script lang="ts">
import { BusinessSuper } from '@/pages/index/modules/mall/mallType'
import { Component, Vue, Prop, Watch } from 'vue-property-decorator'

@Component({})
export default class LeftTabbar extends Vue {
	@Prop() propData!: BusinessSuper
	formData: BusinessSuper = {} as BusinessSuper
	activeId: string = ''
	HeadList: Array<{ id: string; name: string }> = []
	styleType = {
		style1: '',
		style2: ''
	}
	scrollTop: number = 0
	isFirst: boolean = true

	@Watch('propData', { deep: true })
	getFormData() {
		this.formData = this.propData
		this.getHeadList()
		this.getstyleType()
	}

	mounted() {
		this.getFormData()
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 获取tabList
	 */

	getHeadList() {
		let activeId = this.formData.currentFirstCategory
			? this.formData.currentFirstCategory.category.id
			: ''
		const HeadList = this.formData.firstCatList.map((i, d) => {
			if (d === 0 && this.isFirst) activeId = i.category.id
			return {
				id: i.category.id,
				name: (i.category.name || '').substr(0, 4)
			}
		})
		let scrollTop = this.scrollTop
		const index = HeadList.findIndex((i) => i.id === activeId)
		if (index > 10) {
			scrollTop = (index - 10) * 50
		} else {
			scrollTop = 0
		}
		this.setData({
			activeId,
			HeadList,
			scrollTop
		})
		this.isFirst = false
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 修改当前激活tab
	 * @param {*} e
	 */

	changeActiveItem(e: {
		currentTarget: { dataset: { item: { id: string } } }
	}) {
		const item = e.currentTarget.dataset.item
		this.setData({
			activeId: item.id
		})
		this.$emit('changeCategoryId', item.id)
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 设置当前样式
	 */
	getstyleType() {
		const { fontColor, fontBg, fontNotColor, fontNotBg } = this.formData
		const style1 = `color: ${fontColor}; background-color: ${fontBg}`
		const style2 = `color: ${fontNotColor}; background-color: ${fontNotBg}`
		this.setData({
			styleType: {
				style1,
				style2
			}
		})
	}
}
</script>

<style lang="scss" scoped>
.left__mode-page {
	width: 100%;
	height: 100%;
	box-sizing: border-box;
	background-color: rgba(242, 242, 242, 1);

	.tab__item {
		display: block;
		height: 40px;
		line-height: 40px;
		font-size: 14px;
		color: #333;
		background-color: rgba(242, 242, 242, 1);
		cursor: pointer;
		user-select: none;
		box-sizing: border-box;
		padding-left: 10px;
		position: relative;
		overflow: hidden;
		box-sizing: border-box;
		padding-right: 6px;

		text {
			width: 4px;
			height: 15px;
			background-color: rgba(242, 242, 242, 1);
			top: 17px;
			position: absolute;
			left: 0;
		}
	}

	.tab__active {
		background-color: #fff;

		text {
			background-color: rgba(252, 66, 90, 1);
		}
	}
}
</style>
