<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-12 15:36:21
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-30 14:24:45
 * 123
-->
<template>
	<view class="goods">
		<image
			:src="good.pic"
			mode="aspectFill"
			class="goods__image"
			alt
			@tap.stop="gotoDeatil"
		></image>
		<view
			:class="
				'goods__info ' + (good.status === 1 && checkStock != 0 ? '' : 'padding')
			"
		>
			<view
				:class="
					' ' +
						(good.status === 1 && checkStock != 0
							? 'goods--hasName'
							: 'goods--name')
				"
				@tap.stop="gotoDeatil"
			>
				{{ good.productName }}
			</view>
			<view>
				<view
					class="goods--num "
					@tap.stop="getSku"
					v-if="good.skuName != '' && checkStock != 0"
				>
					<view class="skuName">{{ good.skuName }}</view>
					<van-icon name="arrow-down" class="goods--icon"></van-icon>
				</view>
				<view
					class="goods--noNum "
					v-if="good.skuName != '' && checkStock === 0"
				>
					{{ good.skuName }}
				</view>
			</view>
			<view
				class="goods--specification"
				v-if="good.status === 1 && checkStock != 0"
			>
				<span class="goods--price">
					<view class="product__price">
						<span>{{ intPrice }}</span>
						<span style="font-weight:weight;font-size:13px;"
							>.{{ smaPrice }}</span
						>
					</view>
				</span>
				<slot></slot>
			</view>
			<view
				v-if="
					good.status === 1 && checkStock === 0 && good.skuStocks.length > 1
				"
			>
				<view class="goods--reTry">
					请重新选择规格
					<view class="button" @tap.stop="getSku">重选</view>
				</view>
				<view v-if="good.status !== 1">
					<view class="soldOut">
						<view>{{ good.status === 2 ? '已售罄' : '已下架' }}</view>
					</view>
				</view>
			</view>
		</view>
	</view>
</template>
<script lang="ts">
import { Component, Vue, Prop, Watch } from 'vue-property-decorator'
import mIcon from '@/components/m-icon/m-icon.vue'
import { GoodState } from './goodType'
import { GoodInfo } from '../../shopCarType'
@Component({
	components: {
		mIcon
	}
})
export default class Goods extends Vue implements GoodState {
	@Prop()
	good!: GoodInfo
	@Prop()
	checkStock!: number
	intPrice = ''
	smaPrice = ''

	@Watch('good', {
		immediate: true
	})
	goodChange() {
		if (this.good) {
			const good = this.good
			let intPrice = ''
			let smaPrice = ''
			if (good.actPrice) {
				intPrice = Number(good.actPrice)
					.toFixed(2)
					.split('.')[0]
				smaPrice = Number(good.actPrice)
					.toFixed(2)
					.split('.')[1]
				this.intPrice = intPrice
				this.smaPrice = smaPrice
			}
		}
	}
	/**
	 * @LastEditors: chuyinlong
	 * @description: 前往商品详情
	 * @param {*}
	 * @returns {*}
	 */

	gotoDeatil(): void {
		const good = this.good
		uni.navigateTo({
			url: `/subcontract/pages/detail/detail?id=${good.productId}&img=${good.pic}`
		})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 获取商品SKU
	 * @param {*}
	 * @returns {*}
	 */
	getSku(): void {
		this.$STORE.setStore.setTabVisible(false)
		const good = this.good
		const options = {
			id: good.id,
			skuId: good.skuId
		}
		this.$emit('getSku', options)
	}
}
</script>
<style lang="scss" scoped>
@import '@/assets/styles/_var.scss';

.goods {
	@include flex(space-between, flex-start);
	$s: 95px;

	.goods__image {
		width: $s;
		height: $s;
		flex: 1 0 $s;
		margin-right: 10px;
		border-radius: 10px;
	}

	.goods--hasName {
		font-size: 13px;
		font-weight: bold;
		color: #535353;
	}

	.goods--name {
		font-size: 13px;
		font-weight: bold;
		color: #9c9c9c;
	}

	.goods__info {
		@include flex(space-between, space-between);

		flex-direction: column;

		width: 220px;
		padding-right: 24px;
		height: $s;

		.goods--specification {
			@include flex(space-between, flex-start);

			width: 100%;
			color: #9c9c9c;
		}

		.goods--price {
			.important {
				font-size: 16.5px;
				color: #ff4444;
				font-weight: bold;
			}
		}

		.goods--num {
			display: flex;
			align-items: center;
			font-size: 11px;
			color: #333333;

			.skuName {
				border-radius: 50px;
				font-size: 11px;
				padding: 5px 20px 5px 10px;
				overflow: hidden;
				white-space: nowrap;
				text-overflow: ellipsis;
				background: #ececec;
				border: 1px solid $main-border-color;
				border-radius: 50px;
			}
		}

		.goods--noNum {
			display: flex;
			width: 90px;
			font-size: 12px;
			color: #9c9c9c;
		}

		.goods--icon {
			position: relative;
			top: 0px;
			margin-left: -17px;
		}

		.goods--reTry {
			display: flex;
			justify-content: space-between;
			align-items: center;
			margin-top: 5px;
			color: #333333;
			font-size: 14px;

			.button {
				width: 60px;
				height: 25px;
				@include flex(center, center);
				border: 1px solid #f84642;
				color: #f84642;
				border-radius: 40px;
			}
		}
	}
}

.padding {
	padding-bottom: 30px;
}

.soldOut {
	color: black;
	margin-top: 56px;
	font-size: 15px;
}

.product__price {
	font-size: 17px;
	font-weight: bold;
	color: #fa5555;
}

.product__price::before {
	content: '￥';
	font-weight: normal;
	font-size: 13px;
}
</style>
