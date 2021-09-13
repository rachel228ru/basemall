<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-12 15:36:21
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-30 14:28:35
 * 123
-->
<template>
	<view>
		<order-item
			v-for="(item, index) in orders"
			:key="index"
			:orderData="item"
			:currentOrderType="active"
		>
			<view slot="header--left">
				<view class="order__top">
					<block v-if="query.orderStatus === '6'">
						<tag
							v-if="item.afs"
							:label="af.getAfterTypeName(item.afs.type)"
						></tag>
					</block>
					<block v-else>
						<tag
							v-if="order.getOrderTypeName(item.type)"
							:label="order.getOrderTypeName(item.type)"
						></tag>
					</block>
					订单号：{{
						query.orderStatus === '6'
							? item.no
							: item.orderId || item.applyOrderId
					}}
					<view
						class="order__top--copy text__pink"
						@tap.stop="copyOrder"
						:data-item="item.orderId || item.applyOrderId"
					>
						复制
					</view>
				</view>
			</view>
			<view slot="header-right">
				<block v-if="item.originalOrderId">
					<text
						class="order__status text__pink"
						:data-id="item.originalOrderId"
						:data-type="item.type"
						@tap.stop="goDetail"
					>
						查看原订单
					</text>
				</block>
				<!-- 待付款 bind:finish="onFinish" -->
				<block v-else-if="order.isPendingPayment(item.status)">
					<view class="order__expire">
						<text>还剩</text>
						<!-- bind:finish="onFinish" -->
						<van-count-down :time="item.expireTime"></van-count-down>
						<text>关闭</text>
					</view>
					<!-- <text class="text__pink"></text> -->
				</block>
				<!-- 待发货 wx:elif="{{!order.isClose(item.status)}}" -->
				<block v-else>
					<text
						class="order__status text__pink"
						v-if="!order.isExcludeStatus(item.status)"
					>
						{{ order.getOrderStatusName(item.status) || '' }}
					</text>
				</block>
			</view>
			<view class="order__button" slot="control-button">
				<!-- 待付款 -->
				<view class="order__button" v-if="order.isPendingPayment(item.status)">
					<button
						@tap.stop="cancel({ id: item.orderId }, callbackAction)"
						class="m__button"
					>
						取消订单
					</button>
					<button
						@tap.stop="
							pay({ id: item.orderId, type: item.payType }, callbackAction)
						"
						class="m__button primary"
					>
						支付
					</button>
				</view>
				<!-- 待提货 -->
				<view class="order__button" v-if="order.isWaitTake(item.status)">
					<button
						@tap.stop="
							receipt(
								{ id: item.orderId, disabled: item.disabled },
								callbackAction
							)
						"
						:disabled="item.disabled"
						class="m__button primary"
						:class="{ disabled: item.disabled }"
					>
						确认收货
					</button>
				</view>
				<!-- 待评价 -->
				<view
					class="order__button"
					v-if="
						order.isWaitComment(item.status) &&
							hasOpenEvaluate &&
							!order.excludeActive([4], active) &&
							order.isafs(item.itemVoList)
					"
				>
					<button
						@tap.stop="goReviews"
						:data-id="item.orderId"
						class="m__button primary"
					>
						立即评价
					</button>
				</view>
				<view
					class="order__button"
					v-if="af.isWaitReturn(item.afs && item.afs.status)"
				>
					<button
						@tap.stop="handleReturn"
						:data-id="item.afs.id"
						class="m__button primary"
					>
						我已退货
					</button>
				</view>
			</view>
		</order-item>
		<data-empty v-if="!orders.length"></data-empty>
		<van-divider contentPosition="center" v-if="isLast">到底了</van-divider>
	</view>
</template>

<script module="order" lang="wxs" src="../order.wxs"></script>
<script module="af" lang="wxs" src="@/wxs/afterSale.wxs"></script>

<script lang="ts">
import { Component, Mixins, Prop } from 'vue-property-decorator'
import OrderMix from '@/mixins/order'
import OrderItem from '../order-item/order-item.vue'
import DataEmpty from '@/components/data-empty/data-empty.vue'
import { SearchQuery } from '../orderListType'
import { ApiOrderItemType } from '../orderListType'
import { returnOfGoods } from '@/api/modules/afterSale'

@Component({
	components: {
		OrderItem,
		DataEmpty
	}
})
export default class OrderList extends Mixins(OrderMix) {
	/** 订单列表 */
	@Prop({
		default: []
	})
	orders!: Array<ApiOrderItemType>
	/** 订单查询条件 */
	@Prop() query!: SearchQuery
	/** 是否为最后一条记录 */
	@Prop() isLast!: boolean
	/** 是否为第一次进度 */
	@Prop() isFistEnter!: boolean

	get hasOpenEvaluate() {
		return this.$STORE.setStore.shopSetting.hasOpenEvaluate
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 复制订单号
	 * @param {*} e
	 */
	copyOrder(e: { currentTarget: { dataset: { item: string } } }) {
		const orderId = e.currentTarget.dataset.item
		uni.setClipboardData({
			data: orderId,
			success() {
				uni.getClipboardData({})
			}
		})
	}

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

	/**
	 * @LastEditors: chuyinlong
	 * @description: 处理退货
	 * @param {*}
	 */

	handleReturn({
		currentTarget: {
			dataset: { id }
		}
	}: {
		currentTarget: {
			dataset: {
				id: string
			}
		}
	}) {
		returnOfGoods({
			afsId: id,
			deliverySn: ''
		})
			.then(() => {
				this.$Popup.toast('操作成功')
			})
			.catch(() => {
				this.$Popup.toast('操作失败')
			})
	}
}
</script>

<style lang="scss" scoped>
.order {
	&__button {
		@include flex(flex-end);
	}

	&__top {
		@include flex();

		&--copy {
			margin-left: 10px;
		}
	}

	&__expire {
		@include flex();

		.van-count-down {
			position: relative;
			bottom: -1rpx;
			color: $main-light-color;
			margin: 0 3px;
		}
	}

	&__status {
		position: relative;
		top: -14rpx;
	}

	.van-dialog__message {
		padding: 30rpx;
	}

	.van-dialog__message-text {
		font-size: 16px;
	}
}
</style>
