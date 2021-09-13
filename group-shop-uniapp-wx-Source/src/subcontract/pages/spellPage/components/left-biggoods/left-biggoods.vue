<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-12 15:36:21
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-20 11:05:03
 * 123
-->
<template>
	<view class="left__biggoods-page">
		<view v-for="(item, index) in dataList" :key="index" class="con__box">
			<view class="i__title">{{ item.title }}</view>
			<LeftSecondgoodssc
				:propData="formData"
				:dataList="item.list"
			></LeftSecondgoodssc>
		</view>
	</view>
</template>

<script lang="ts">
import { Component, Vue, Prop, Watch } from 'vue-property-decorator'
import { getEffectCategoryGoods } from '@/api/modules/goods'
import LeftSecondgoodssc from '@/pages/index/modules/mall/components/left-second-goods/leftSecondGoods.vue'
import { BusinessSuper } from '@/pages/index/modules/mall/mallType'
import {
	ApiClassifyGoodItem,
	ApiClassifyProduct
} from '@/pages/index/modules/mall/components/left-biggoods/leftBigType'

@Component({
	components: {
		LeftSecondgoodssc
	}
})
export default class LeftBiggoods extends Vue {
	@Prop() propData!: BusinessSuper
	formData = {} as BusinessSuper
	dataList: Array<ApiClassifyGoodItem> = []
	showCategoryId: string = ''
	isFirst: boolean = true

	@Watch('propData', {
		deep: true
	})
	getFormData() {
		if (this.propData) {
			this.setData({
				dataList: []
			})
			this.formData = this.propData
			if (!this.formData.firstCatList.length) return
			const showCategoryId = this.isFirst
				? this.formData.firstCatList[0].category.id
				: this.formData.currentFirstCategory.category.id
			this.formData.currentFirstCategory = this.isFirst
				? this.formData.firstCatList[0]
				: this.formData.currentFirstCategory
			this.setData(
				{
					showCategoryId,
					modeType: 1
				},
				() => {
					this.getAllList()
					this.isFirst = false
				}
			)
		}
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 获取商品
	 */

	async getAllList() {
		const param = {
			saleMode: this.formData.saleMode
		}
		getEffectCategoryGoods(this.showCategoryId, param).then((res) => {
			const dataList: ApiClassifyGoodItem[] = []
			this.$emit('getNoList', res.length ? 1 : 0)

			res.forEach(
				(sd: {
					name: string
					apiAliveProductVos: Array<ApiClassifyProduct>
				}) => {
					dataList.push({
						title: sd.name,
						list: sd.apiAliveProductVos
					})
				}
			)
			this.setData({
				dataList
			})
		})
	}

	mounted() {
		this.getFormData()
	}
}
</script>

<style lang="scss" scoped>
.left__biggoods-page {
	.i__title {
		height: 33px;
		box-sizing: border-box;
		color: #999999;
		background-color: #ffffff;
		padding-top: 20px;
	}

	.spellpre__goodsB--icon {
		height: 130px;

		.ipic {
			height: 130px;
			border-radius: 8px;
		}
	}

	.spellpre__goodsL--icon {
		height: 90px;
		width: 90px;

		.ipic {
			height: 90px;
			width: 90px;
			border-radius: 8px;
		}
	}

	.spellpre__goodsL--info {
		overflow-x: hidden;
	}

	.spellpre__goodsL--name {
		width: 100%;
		overflow: hidden;
		text-overflow: ellipsis;
		white-space: nowrap;
	}
}
</style>
