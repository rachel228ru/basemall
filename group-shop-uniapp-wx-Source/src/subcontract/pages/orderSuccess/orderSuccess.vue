<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-12 15:36:21
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-30 14:29:07
 * 123
-->
<template>
	<view>
		<view class="success">
			<!-- <view class="success__image">
    <image src="https://oss-tencent.bgniao.cn/api/os-c.png"></image>
  </view> -->
			<view class="success__top">
				<image
					class="success__top--img"
					src="https://oss-tencent.bgniao.cn/api/os.jpg"
				></image>
				<image
					class="success__top--icon"
					src="https://oss-tencent.bgniao.cn/api/zficon.png"
				></image>
				<view class="success__title">支付成功!</view>
			</view>
			<view class="success__card">
				<view class="success__card--price">
					<view class="price--p">￥</view>
					<view>{{ price }}</view>
				</view>
				<view class="success__card--field">订单编号: {{ orderDetail.id }}</view>
				<view class="success__card--field"
					>下单时间: {{ orderDetail.createTime }}</view
				>
				<view class="success__card--field"
					>支付方式:{{ order.getPayTypeName(orderDetail.payType) }}</view
				>
				<view class="success__btn">
					<view class="success__btn--item" @tap="backHome">返回首页</view>
				</view>
			</view>
		</view>
		<van-popup
			:show="isshow"
			close-on-click-overlay
			custom-style="background-color:transparent;"
			@close="cancel"
		>
			<view class="popup">
				<image :src="homePromptBgImg"></image>
				<view class="text">{{ goodsNumber * fundQuota }}元</view>
				<view class="close" @tap="cancel"></view>
				<view class="btn" @tap="topage"></view>
			</view>
		</van-popup>
	</view>
</template>

<script module="order" lang="wxs" src="../order/order.wxs"></script>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator'
import { getOrderDetail } from '@/api/modules/order'
import Toast from '@/wxcomponents/vant-weapp/toast/toast'

@Component({})
export default class OrderSuccess extends Vue {
	detail: any = {}
	image: string = ''
	price: number = 0
	orderDetail: any = {}
	orderId: any = null
	isFirstEnter: boolean = true

	async onLoad(options: { orderId: string; price: number }) {
		this.setData(
			{
				orderId: options.orderId,
				price: options.price
			},
			() => {
				if (this.$STORE.setStore.isReady) {
					this.getOrderDetail(options.orderId)
				}
			}
		)

		this.$Pubsub.on('app-launch', () => {
			this.getOrderDetail(options.orderId)
		})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 查看订单详情
	 */

	goDetail() {
		uni.redirectTo({
			url: `/subcontract/pages/orderDetail/orderDetail?orderId=${this.orderId}`
		})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 获取订单详情
	 * @param {string} orderId
	 */

	getOrderDetail(orderId: string) {
		getOrderDetail(orderId)
			.then((detail) => {
				this.setData({
					orderDetail: detail,
					isFirstEnter: false
				})
			})
			.catch(() => {
				Toast({
					message: '详情获取失败'
				})
			})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 返回首页
	 */
	backHome() {
		this.$STORE.setStore.backHome()
	}
}
</script>

<style lang="scss" scoped>
@include b(success) {
	@include e(top) {
		width: 100%;
		height: 170px;
		padding-left: 120px;
		padding-top: 50px;
		background: #f9604c;
		color: #fff;
		position: relative;

		@include m(img) {
			position: absolute;
			width: 100%;
			height: 100%;
			left: 0;
			top: 0;

			image {
				width: 100%;
				height: 100%;
			}
		}

		@include m(icon) {
			position: absolute;
			width: 141px;
			height: 121px;
			left: 0;
			top: 0;
			z-index: 3;
			left: 0px;
			top: 15px;
		}
	}

	@include e(title) {
		font-weight: bold;
		position: relative;
		z-index: 3;
		margin-bottom: 10px;
	}

	@include e(time) {
		font-size: 14px;
		position: relative;
		z-index: 3;
	}

	@include e(canvas) {
		position: fixed;
		top: -1000px;
	}

	@include e(card) {
		&::before {
			content: '';
			position: absolute;
			z-index: 3;
			width: 355px;
			height: 6px;
			background: #c43e2e;
			margin-left: -15px;
			border-radius: 3px;
		}

		width: 345px;
		margin: 0 auto;
		margin-bottom: 10px;
		box-shadow: 0 0 10px rgba($color: #000000, $alpha: 0.2);
		padding: 0 10px;
		margin-top: -40px;
		background: #fff;
		z-index: 3;
		position: relative;

		@include m(price) {
			@include flex;

			text-align: center;
			font-size: 24px;
			padding: 20px;
			border-bottom: 1px solid rgb(235, 235, 235);
			vertical-align: bottom;

			.price--p {
				font-size: 14px;
				position: relative;
				bottom: -3px;
			}
		}

		@include m(field) {
			font-size: 12px;
			margin: 10px 0;
		}
	}

	@include e(image) {
		@include flex;

		margin-top: 120px;

		image {
			width: 514rpx;
			height: 332rpx;
			display: block;
		}
	}

	@include e(btn) {
		@include flex();

		@include m(item) {
			border: 1px solid #ff5a52;
			padding: 5px 15px;
			border-radius: 15px;
			font-size: 14px;
			margin: 10px;
			color: #ff5a52;

			&.solid {
				background: #ff5a52;
				color: #fff;
			}
		}
	}

	.share {
		@include flex();

		width: 100vw;
		height: 100vh;
		background-color: rgba($color: #000000, $alpha: 0.8);
		flex-direction: column;
		position: fixed;
		top: 0;
		left: 0;
		z-index: 99;

		&__img {
			width: auto;
			height: 220px;
			background: #fff;
		}

		img {
			width: 100%;
			height: auto;
		}
	}
}

.popup {
	width: 653rpx;
	height: 681rpx;
	position: relative;

	image {
		width: 100%;
		height: 100%;
	}

	.text {
		width: 100%;
		font-size: 88rpx;
		color: #fc575a;
		position: absolute;
		top: 280rpx;
		left: 0;
		right: 0;
		text-align: center;
	}

	.close {
		width: 100rpx;
		height: 100rpx;
		position: absolute;
		top: 0;
		right: 40rpx;
	}

	.btn {
		width: 420rpx;
		height: 90rpx;
		position: absolute;
		bottom: 35rpx;
		right: 0;
		left: 0;
		margin: 0 auto;
	}
}
</style>
