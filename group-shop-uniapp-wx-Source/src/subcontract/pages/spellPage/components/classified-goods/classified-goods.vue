<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-12 15:36:21
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-20 09:57:19
 * 123
-->
<template>
	<view class="classified__goods-page">
		<view v-for="(nav, index) in navigation" :key="index">
			<block v-if="nav.name">
				<view
					class="navi__box1"
					v-if="
						formData.currentFirstCategory.navigationStyle === 1 &&
							(navigation.length > 1 ||
								(navigation.length === 1 && nav.navType !== 1))
					"
				>
					<text class="nav_b"></text>
					<text class="nav_p">{{ nav.name }}</text>
					<text class="nav_b"></text>
				</view>
				<view
					class="navi__box2"
					v-if="
						formData.currentFirstCategory.navigationStyle === 2 &&
							(navigation.length > 1 ||
								(navigation.length === 1 && nav.navType !== 1))
					"
				>
					<text class="nav_b2"></text>
					<text class="nav_p2">{{ nav.name }}</text>
				</view>
				<view
					v-for="(item, idx) in nav.chartList"
					:key="idx"
					:class="'class-item ' + (idx % 3 === 0 ? 'remove__margin' : '')"
					@tap="showSecondCategory"
					:data-item="item"
				>
					<image :src="item.img" mode="scaleToFill" lazy-load="true"></image>
					<text class="item_p">{{ item.category.name }}</text>
				</view>
			</block>
		</view>
	</view>
</template>

<script lang="ts">
import {
	BusinessSuper,
	Chart,
	Navigation
} from '@/pages/index/modules/mall/mallType'
import { Component, Vue, Prop, Watch } from 'vue-property-decorator'

@Component({})
export default class ClassifiedGoods extends Vue {
	@Prop() propData!: BusinessSuper

	formData: BusinessSuper = {} as BusinessSuper
	navigation: Array<Navigation> = []

	@Watch('propData', { deep: true })
	getFormData() {
		const { currentFirstCategory } = this.propData
		let { navigation } = currentFirstCategory
		if (navigation.length > 1) {
			const temp = navigation.findIndex((i) => i.navType === 1)
			if (temp !== -1) {
				const list = [...navigation]
				list.push(...list.splice(temp, 1))
				navigation = [...list]
			}
		}
		this.setData({
			formData: this.propData,
			navigation
		})
	}

	mounted() {
		this.getFormData()
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 查看二级分类商品
	 * @param {*} e
	 */

	showSecondCategory(e: { currentTarget: { dataset: { item: Chart } } }) {
		const data = e.currentTarget.dataset.item
		wx.navigateTo({
			url: `/subcontract/pages/spellPage/second-catgoods/second-catgoods?chanel=11&categoryId=${data.category.id}&cartButton=${this.formData.cartButton}`
		})
	}
}
</script>

<style lang="scss" scoped>
.classified__goods-page {
	width: 100%;
	box-sizing: border-box;

	.class-item {
		width: 71px;
		height: 111px;
		display: inline-block;
		margin-left: calc((100% - 215px) / 2);
		margin-bottom: 8px;
		vertical-align: top;

		image {
			display: block;
			width: 100%;
			height: 71px;
			border-radius: 3px;
		}

		.item_p {
			display: block;
			height: 40px;
			line-height: 40px;
			width: 100%;
			color: #333;
			text-align: center;
		}
	}

	.remove__margin {
		margin-left: 0px;
	}

	.navi__box1 {
		height: 50px;
		text-align: center;

		text {
			display: inline-block;
		}

		.nav_p {
			height: 50px;
			line-height: 40px;
			padding: 0 15px;
			vertical-align: middle;
		}

		.nav_b {
			width: 52px;
			height: 1px;
			background-color: rgba(236, 236, 236, 1);
			vertical-align: super;
		}
	}

	.navi__box2 {
		height: 50px;
		position: relative;

		.nav_b2 {
			height: 13px;
			left: 0;
			top: 14px;
			width: 2px;
			background-color: #333;
			position: absolute;
		}

		.nav_p2 {
			display: inline-block;
			height: 50px;
			line-height: 40px;
			padding-left: 15px;
		}
	}
}
</style>
