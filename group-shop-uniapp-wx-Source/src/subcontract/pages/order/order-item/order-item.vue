<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-12 15:36:21
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-09-01 16:03:23
 * 123
-->
<template>
	<view
		class="order__item"
		@tap="goDetail"
		:data-id="orderData.orderId || orderData.applyOrderId"
		:data-type="orderData.type"
	>
		<view class="order__item--header">
			<view class="header--left">
				<slot name="header--left"></slot>
			</view>
			<view class="header--right">
				<slot name="header-right"></slot>
			</view>
		</view>
		<view class="order__item--goods">
			<block
				v-for="(item, index) in orderData.itemVoList || [orderData]"
				:key="index"
			>
				<goods :key="index" :goodsData="item">
					<block
						v-if="
							currentOrderType === '6' ||
								af.orderItemStatusVisible(item.afs.status)
						"
					>
						<view
							class="order__item--status green"
							style="margin-bottom: 10px;"
						>
							<block v-if="currentOrderType === '6'">
								{{ currentOrderType
								}}{{ af.orderItemStatusVisible(item.afs.status) }}
								{{ af.getAfterTypeName(item.type) }}
								{{ af.getAfterStatusName(item.status) }}
								<block v-if="af.timeVisible(item.status)">
									还剩{{ date.getTimeDifference(item.deadline) }}
								</block>
							</block>
							<block v-else>
								{{ af.getAfterTypeName(item.afs.type) }}
								{{ af.getAfterStatusName(item.afs.status) }}
							</block>
						</view>
					</block>
				</goods>
			</block>
		</view>
		<view class="order__item--control">
			<view class="control__info">
				<view class="info--left text__green">
					<slot name="control-af"></slot>
				</view>
				<view class="info--right">
					支付金额：
					<text>￥{{ orderData.payAmount }}</text>
				</view>
			</view>
			<view class="control__button">
				<slot name="control-button"></slot>
			</view>
		</view>
	</view>
</template>

<script module="date" lang="wxs" src="@/wxs/date.wxs"></script>
<script module="af" lang="wxs" src="@/wxs/afterSale.wxs"></script>

<script lang="ts">
import { Component, Vue, Prop } from 'vue-property-decorator'
import { ApiOrderItemType } from '../orderListType'

@Component({})
export default class OrderItem extends Vue {
	options = {
		multipleSlots: true
	}

	@Prop() orderData!: ApiOrderItemType
	@Prop({
		default: '6'
	})
	currentOrderType!: string

	/**
	 * @LastEditors: chuyinlong
	 * @description: 查看订单详情
	 * @param {*}e
	 */

	goDetail({
		currentTarget: {
			dataset: { id, type }
		}
	}: {
		currentTarget: {
			dataset: {
				id: string
				type: string
			}
		}
	}) {
		uni.navigateTo({
			url: `/subcontract/pages/orderDetail/orderDetail?orderId=${id}&userType=${type}`
		})
	}
}
</script>

<style lang="scss" scoped>
@import '@/assets/styles/var';

.order__item {
	$p-v: 10px;

	background: #fff;
	font-size: 12px;
	margin-top: 10px;

	.green {
		color: #09bb07;
	}

	&--header {
		@include flex(space-between);

		width: 100%;
		height: 45px;
		padding: 0 $p-v;
		border-bottom: 1px solid $main-border-color;
	}

	&--goods {
		padding: $p-v;
		border-bottom: 1px solid $main-border-color;
	}

	&--control {
		padding: $p-v $p-v 5px;

		.control__info {
			@include flex(space-between, center);

			.info--right {
				font-size: 13px;
				color: rgba(28, 28, 28, 1);

				text {
					font-weight: bold;
				}
			}
		}

		.control__button {
			@include flex(flex-end, center);

			width: 100%;
			margin-top: 12px;
		}
	}

	.text__green {
		color: #08bb07;
		margin-bottom: 10px;
	}
}
</style>
