<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-12 15:36:21
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-30 14:28:27
 * 123
-->
<template>
	<view class="order">
		<van-tabs
			:active="active"
			:ellipsis="false"
			@change="onTabChange"
			sticky
			animated
			swipeable
			ref="tabs"
			id="tabs"
		>
			<van-tab title="全部" name="-1">
				<order-list
					:orders="orders"
					:query="query"
					:isLast="isLast"
					:isFistEnter="isFistEnter"
					@call="callbackAction"
				></order-list>
			</van-tab>
			<van-tab title="待付款" name="0">
				<order-list
					:orders="orders"
					:query="query"
					:isLast="isLast"
					:isFistEnter="isFistEnter"
					@call="callbackAction"
				></order-list>
			</van-tab>
			<van-tab title="待发货" name="1">
				<order-list
					:orders="orders"
					:query="query"
					:isLast="isLast"
					:isFistEnter="isFistEnter"
					@call="callbackAction"
				></order-list>
			</van-tab>
			<van-tab title="待提货" name="3">
				<order-list
					:orders="orders"
					:query="query"
					:isLast="isLast"
					:isFistEnter="isFistEnter"
					@call="callbackAction"
				></order-list>
			</van-tab>
			<van-tab title="已完成" name="4">
				<order-list
					:orders="orders"
					:query="query"
					:isLast="isLast"
					:isFistEnter="isFistEnter"
					@call="callbackAction"
				></order-list>
			</van-tab>
			<van-tab title="待评价" name="5" v-if="hasOpenEvaluate">
				<order-list
					:orders="orders"
					:query="query"
					:isLast="isLast"
					:isFistEnter="isFistEnter"
					@call="callbackAction"
				></order-list>
			</van-tab>
			<van-tab title="售后订单" name="6">
				<order-list
					:orders="orders"
					:query="query"
					:isLast="isLast"
					:isFistEnter="isFistEnter"
					@call="callbackAction"
				></order-list>
			</van-tab>
			<van-tab title="已关闭" name="7">
				<order-list
					:orders="orders"
					:query="query"
					:isLast="isLast"
					:isFistEnter="isFistEnter"
					@call="callbackAction"
				></order-list>
			</van-tab>
		</van-tabs>
		<van-toast id="van-toast" />
		<van-dialog
			confirm-button-color="#fe4e63"
			id="van-dialog"
			width="280px"
			custom-style="font-size:18px"
		/>
	</view>
</template>

<script lang="ts">
import { Component, Mixins } from 'vue-property-decorator'
import OrderList from './order-list/order-list.vue'
import Toast from '@/wxcomponents/vant-weapp/toast/toast'
import { orderListState, SearchQuery, ApiOrderItemType } from './orderListType'
import { getOrders } from '@/api'
import isNumber from 'lodash/isNumber'
import { getAfterSaleList } from '@/api/modules/afterSale'
import OrderMix from '@/mixins/order'
const order = require('./order.wxs')
const af = require('@/wxs/afterSale.wxs')

@Component({
	components: {
		OrderList
	}
})
export default class Order extends Mixins(OrderMix) implements orderListState {
	order = order
	af = af
	active = '-1'
	orders: ApiOrderItemType[] = []
	/** 订单查询条件 */
	query: SearchQuery = {
		current: 1,
		keyword: '',
		orderStatus: '-1',
		size: 10
	}
	isLast = false
	isFistEnter = true

	get hasOpenEvaluate() {
		return this.$STORE.setStore.shopSetting.hasOpenEvaluate
	}

	onLoad(options: { type: string }) {
		const type = options.type || '-1'
		if (this.$STORE.setStore.isReady) {
			this.setData(
				{
					active: type,
					['query.orderStatus']: type
				},
				() => {
					this.getOrderslist()
				}
			)
		}

		this.$Pubsub.on('app-launch', () => {
			this.setData(
				{
					active: type
				},
				() => {
					this.getOrderslist()
				}
			)
		})
	}

	async onShow() {
		await this.$STORE.setStore.getSetting()
		if (this.isFistEnter) {
			this.setData({
				isFistEnter: false
			})
			return
		} else {
			this.getOrderslist(
				{
					current: 1
				},
				true
			)
		}
	}

	onReachBottom() {
		this.setData({
			'query.current': ++this.query.current
		})
		this.getOrderslist()
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: mixins内成功回调
	 */

	callbackAction() {
		this.getOrderslist(
			{
				current: 1
			},
			true
		)
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description:切换标签页事件
	 * @param {*}e
	 */

	onTabChange({
		detail: { name: orderStatus }
	}: {
		detail: {
			name: string
		}
	}) {
		this.orders = []
		this.updateQuery({
			orderStatus,
			current: 1
		})
		this.getOrderslist({}, false, () => {
			this.setData({
				active: orderStatus
			})
		})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 更新查询条件
	 * @param {*}params
	 */

	updateQuery(params: { orderStatus: string; current: number }) {
		this.setData({
			query: {
				...this.query,
				...params
			},
			isLast: false
		})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 处理倒计时结束
	 */

	onFinish() {
		this.getOrderslist({}, true)
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description:
	 * @param {SearchQuery} query
	 * @param {boolean} refresh
	 * @param {()=>void} cb
	 */

	async getOrderslist(
		query: Partial<SearchQuery> = {},
		refresh: boolean = false,
		cb?: () => void
	) {
		/** 如果已为最后一条记录，跳过请求 */
		if (this.isLast && !refresh) {
			cb && cb()
			return
		}
		Toast.loading({
			duration: 10000
		})

		const res =
			this.query.orderStatus === '6'
				? await getAfterSaleList({
						...this.query,
						...query
				  })
				: await getOrders({
						...this.query,
						...query
				  })
		const { current, pages, list } = res
		this.setData({
			orders: this.handOrderList(
				current > 1 && !refresh ? [...this.orders, ...list] : list
			)
		})

		/** 如果为最后一页设置到底状态 */
		if (current === pages) {
			this.setData({
				isLast: true
			})
		}
		cb && cb()
		Toast.clear()
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 设置倒计时
	 * @param {ApiOrderItemType[]} list
	 */

	handOrderList(list: ApiOrderItemType[]) {
		return list.map((item) => {
			if (item.expireTime && !isNumber(item.expireTime)) {
				item.expireTime = String(
					+new Date(item.expireTime.replace(/-/gi, '/')) - +new Date()
				)
			}

			let status = false

			if (item.itemVoList) {
				item.itemVoList.map((goods) => {
					if (goods.afs) {
						if (!status) {
							status = this.receiptDisabled(goods.afs)
						}
					}
				})
			}

			item.disabled = status
			return item
		})
	}
}
</script>

<style lang="scss" scoped>
@import '@/assets/styles/_var.scss';

page {
	background: $main-background;
}

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
