<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-12 15:36:21
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-30 14:28:19
 * 123
-->
<template>
	<view class="negotiation--history">
		<view
			v-for="(item, index) in list"
			:key="index"
			class="negotiation--history__item"
		>
			<view class="item--user">
				<view class="item--user__l">
					<image
						:src="item.userAvatarUrl || '/static/assets/images/seller.png'"
					></image>
				</view>
				<view class="item--user__r">
					<view class="user--name">{{ item.userName }}</view>
					<view class="user--time">{{ item.updateTime }}</view>
				</view>
			</view>
			<view class="item--message">
				{{ item.info }}
				<view class="item--images" v-if="item.image">
					<image
						v-for="(item, index2) in item.image"
						:key="index2"
						mode="aspectFill"
						:src="item"
					></image>
				</view>
			</view>
		</view>
	</view>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator'
import { getNegotiateHistory } from '@/api/modules/afterSale'
import { INegotitaonList } from './negotiationHistoryType'

@Component({})
export default class NegotiationHistory extends Vue {
	list: Array<INegotitaonList> = []

	onLoad({ id: orderId, type }: { id: string; type: string }) {
		let data = {
			orderId,
			type
		}

		getNegotiateHistory(data)
			.then((res: INegotitaonList[]) => {
				res.map((item) => {
					if (item.image) {
						item.imageArr = item.image.split(',')
					}
				})
				this.setData({
					list: res
				})
			})
			.catch(() => {
				this.$Popup.toast('协商历史获取失败')
			})
	}
}
</script>

<style lang="scss" scoped>
page {
	background: #f4f4f4;
}

.negotiation--history,
.negotiation--history__item,
.item--user {
	width: 100%;
	height: auto;
}

.negotiation--history__item {
	box-sizing: border-box;
	padding: 25rpx;
	background: #fff;
	margin-bottom: 20rpx;
}

.item--user {
	display: flex;
	align-items: center;
}

.item--user image {
	width: 67rpx;
	height: 67rpx;
}

.item--user__l {
	width: 67rpx;
	height: 67rpx;
	margin-right: 15rpx;
}

.user--time {
	margin-top: 5rpx;
	font-size: 22rpx;
	color: #919191;
}

.item--message {
	margin-top: 37rpx;
	word-break: break-all;
}

.item--images {
	width: 100%;
	display: flex;
	justify-content: flex-start;
	margin: 20rpx 0;
}

.item--images image {
	width: 174rpx;
	height: 174rpx;
	margin-right: 10rpx;
}
</style>
