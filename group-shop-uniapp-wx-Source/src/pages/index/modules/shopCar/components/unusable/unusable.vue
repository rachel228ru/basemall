<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-12 15:36:21
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-30 14:24:54
 * 123
-->
<template>
	<view class="shop" v-if="shopLoseCarList.length > 0">
		<view class="shop__top" style="border-radius:0px">
			<text style="font-size:30rpx">失效商品</text>
			<text style="font-size:30rpx;color:#FF4444" @tap="emptyUseless"
				>清空失效商品</text
			>
		</view>
		<view
			v-for="(good, index) in shopLoseCarList"
			:key="index"
			class="shop__goods"
		>
			<view class="shop__goods--container" style="height:280rpx">
				<van-checkbox
					disabled="true"
					style="margin-right:20rpx;"
					checked-color="#FF4444"
				></van-checkbox>
				<view class="shop__goods--container--unable">
					<view v-if="good.status === 2" class="soldOut">
						<view>已售罄</view>
						<view>SoldOut</view>
					</view>
					<view v-if="good.status === 0" class="soldOut">
						<view>已下架</view>
						<view>DropOff</view>
					</view>
				</view>
				<!-- 失效商品 -->
				<!-- name="{{good.productName}} " img="{{good.pic}}" goodId="{{good.productId}}" skuName="{{good.skuName}}" type="{{good.status}}" -->
				<goods :good="good"></goods>
			</view>
		</view>
	</view>
</template>
<script lang="ts">
import { Component, Vue, Prop } from 'vue-property-decorator'
import goods from '../goods/goods.vue'
import { GoodInfo } from '../../shopCarType'

@Component({
	components: {
		goods
	}
})
export default class Unusable extends Vue {
	@Prop()
	type!: number | string
	@Prop()
	shopLoseCarList!: Array<GoodInfo>

	/**
	 * @LastEditors: chuyinlong
	 * @description: 清空失效商品
	 * @param {*}
	 * @returns {*}
	 */

	emptyUseless(): void {
		this.$emit('emptyUseless')
	}
}
</script>
<style lang="scss" scoped>
@include b(shop) {
	height: 100%;
	background-color: white;
	margin: 0px 10px;
	border-radius: 10px;

	@include e(top) {
		width: 100%;
		height: 44px;
		display: flex;
		justify-content: space-between;
		align-items: center;
		padding: 0px 10px;
		font-size: 15px;
	}

	@include e(goods) {
		width: 100%;
		padding-top: 4rpx;
		border-radius: 20px;
		padding-bottom: 10px;

		@include m(container) {
			display: -webkit-box;
			display: flex;
			-webkit-box-pack: justify;
			justify-content: space-between;
			-webkit-box-align: center;
			align-items: center;
			width: 100%;
			border-top: 1px solid #fafafa;
			padding: 10px;
			background-color: white;

			@include m(unable) {
				width: 80px;
				height: 80px;
				border-radius: 50px;
				background-color: rgba(0, 0, 0, 0.3);
				position: absolute;
				left: 56px;
				@include flex(center, center);
				border: 2px solid white;
			}
		}
	}
}

.soldOut {
	color: white;
	font-size: 13px;
	display: flex;
	flex-wrap: wrap;
	padding: 4px;
	align-items: center;
	justify-content: center;
}
</style>
