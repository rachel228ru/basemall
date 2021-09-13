<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-12 15:36:21
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-30 14:07:46
 * 123
-->
<template>
	<view class="delivery">
		<view class="delivery__goods">
			<view class="delivery__img">
				<image :src="orderItemList[0].productPic"></image>
			</view>
			<view class="delivery__info">
				<view>承运公司：{{ orderDelivery.deliveryCompany }}</view>
				<view style="margin:5px 0;"
					>运单编号：{{ orderDelivery.deliverySn }}</view
				>
				<view>官方电话：暂无</view>
			</view>
		</view>
		<van-steps
			:steps="steps"
			:active="active"
			direction="vertical"
			active-color="#ee0a24"
		></van-steps>
	</view>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator'
import { getdeliveryInfo } from '@/api/modules/order'
import {
	Idelivery,
	IDeliveryInfo
} from '../afterSaleDetail/afterSaleDetailType'
import { IGoodsList } from '@/typings/goods'

@Component({})
export default class DeliveryInfo extends Vue {
	steps: Array<Idelivery> = []
	orderItemList: Array<IGoodsList> = []
	orderDelivery: IDeliveryInfo = {} as IDeliveryInfo
	active: number = 0

	onLoad({ data }: { data: string }) {
		const {
			orderItemList,
			orderDelivery
		}: {
			orderItemList: IGoodsList[]
			orderDelivery: IDeliveryInfo
		} = JSON.parse(decodeURIComponent(data))
		this.setData({
			orderItemList,
			orderDelivery
		})
		this.getdeliveryInfo({
			tracesId: orderDelivery.deliverySn,
			companyName: orderDelivery.deliveryCompany,
			deliveryCode: orderDelivery.deliveryCode
		})

		this.$Pubsub.on('app-launch', () => {
			this.getdeliveryInfo({
				tracesId: orderDelivery.deliverySn,
				companyName: orderDelivery.deliveryCompany,
				deliveryCode: orderDelivery.deliveryCode
			})
		})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 获取物流信息
	 * @param {string} tracesId
	 * @param {string} companyName
	 * @param {string} deliveryCode
	 */
	getdeliveryInfo({ tracesId, companyName, deliveryCode }: Idelivery) {
		getdeliveryInfo({
			tracesId,
			companyName,
			deliveryCode
		}).then((res) => {
			const steps = JSON.parse(res) || []
			if (!steps.map) return
			this.setData({
				steps: steps.map(
					({ context, time }: { context: string; time: string }) => {
						return {
							text: context,
							desc: time
						}
					}
				)
			})
		})
	}
}
</script>

<style lang="scss" scoped>
page {
	background: $main-background;
}

@include b(delivery) {
	@include e(goods) {
		@include flex(flex-start);

		padding: 20px 12px;
		margin-bottom: 10px;
		background-color: #fff;
	}

	@include e(img) {
		width: 73px;
		height: 73px;
		margin-right: 10px;

		image {
			width: 100%;
			height: 100%;
		}
	}

	@include e(info) {
		@include flex(flex-start, flex-start);

		flex-direction: column;
	}
}
</style>
