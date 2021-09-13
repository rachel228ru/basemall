<!--
 * @description: 
 * @Author: vikingShip
 * @Date: 2021-08-03 13:21:35
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-19 09:30:14
-->
<template>
	<view>
		<van-popup
			:show="showType"
			position="bottom"
			custom-style="height: 60%;"
			@close="onClose"
			closeable
		>
			<select-norms
				:norms="norms"
				:goodDetail="goodDetail"
				@onClose="onClose"
				buyType="true"
				@addShopCar="addShopCar"
				@checkNorms="checkNorms"
				:shopCar="true"
			>
			</select-norms>
		</van-popup>
	</view>
</template>
<script lang="ts">
import { Component, Vue, Prop } from 'vue-property-decorator'
import selectNorms from '@/components/select-norms/selectNorms.vue'
import { GoodInfo, ApiSkuType } from '../../../shopCarType'
interface SelectGoodInfo extends GoodInfo {
	goodsNumber: number
	skuName: string
}
@Component({
	components: {
		selectNorms
	}
})
export default class Sku extends Vue {
	@Prop()
	showType!: boolean
	@Prop()
	goodDetail!: GoodInfo
	@Prop()
	norms!: ApiSkuType

	/**
	 * @LastEditors: chuyinlong
	 * @description: 弹窗关闭
	 * @param {*}
	 * @returns {*}
	 */
	onClose(): void {
		this.$STORE.setStore.setTabVisible(true)
		this.$STORE.setStore.setFormShopCar(false)
		this.$emit('close', {})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 加入购物车
	 * @param {*} norms
	 * @returns {*}
	 */
	addShopCar(norms: { goodDetail: SelectGoodInfo; index: number }): void {
		this.$emit('addShopCar', norms)
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 规格选择
	 * @param {*} check
	 * @returns {*}
	 */

	checkNorms(check: ApiSkuType): void {
		this.$emit('checkNorms', check)
	}
}
</script>
<style lang="scss" scoped>
.border {
	background: #f0f0f0;

	&__title {
		background-color: white;
		height: 50px;
		display: flex;
		justify-content: center;
		align-items: center;
		font-size: 18px;
		font-weight: bold;
		margin-bottom: 20px;
	}
}

.coupon {
	color: black;
	display: flex;
	padding: 5px 20px;
}
</style>
