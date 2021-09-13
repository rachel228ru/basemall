<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-12 15:36:20
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-16 09:16:16
 * 123
-->
<template>
	<view>
		<view class="goods">
			<view class="goods__image">
				<view class="goods__image--tag"></view>
				<image
					:src="
						goodsData.productSkuPic ||
							goodsData.productPic ||
							goodsData.picture ||
							goodsData.img
					"
					mode="aspectFill"
				></image>
			</view>
			<!-- <image style="width: 200px; height: 200px; background-color: #eeeeee;" mode="{{item.mode}}" src="{{src}}"></image> -->
			<view class="goods__info">
				<view class="goods--name">{{
					goodsData.productName || goodsData.goodName
				}}</view>
				<view class="goods--spec">{{ goodsData.specs }}</view>
				<view class="goods--specification">
					<text class="goods__price"
						>￥{{ goodsData.productPrice || goodsData.price }}</text
					>
					<text>x{{ goodsData.productQuantity || goodsData.num }}</text>
				</view>
			</view>
		</view>
		<view class="goods__status">
			<slot></slot>
		</view>
	</view>
</template>

<script module="image" lang="wxs" src="@/wxs/image.wxs"></script>
<script lang="ts">
import { IGoodsList } from '@/typings/goods'
import { Component, Vue, Prop } from 'vue-property-decorator'

@Component
export default class Goods extends Vue {
	@Prop()
	goodsData: IGoodsList
}
</script>
<style lang="scss" scoped>
@import '@/assets/styles/_var.scss';
.goods {
	@include flex(space-between, flex-start);
	margin-bottom: 10px;
	.goods__image {
		width: 100px;
		height: 100px;
		background: #ccc;
		margin-right: 10px;
		flex: 1 0 75px;
		position: relative;

		&--tag {
			position: absolute;
			left: 0;
			top: 0;
		}

		image {
			width: 100%;
			height: 100%;
			object-fit: cover;
		}
	}

	.goods__info {
		@include flex(space-between, flex-start);

		flex-direction: column;
		width: 250px;
		height: 100px;

		.goods--spec {
			font-size: 12px;
			color: #c7c7c7;
		}

		.goods--specification {
			@include flex(space-between, flex-start);

			margin-top: auto;

			.goods__price {
				font-size: 14px;
				color: #000;
			}

			width: 100%;
			color: #9c9c9c;
		}
	}
}
</style>
