<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-12 15:36:21
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-30 14:26:01
 * 123
-->
<template>
	<view>
		<view class="collectBox" v-if="list.length > 0">
			<view class="collectBox__listBox">
				<view
					v-for="(item, index) in list"
					:key="index"
					class="collectBox__listBox--in"
					@longpress="deleteCollect"
					:data-index="index"
					@tap="toProductDetail"
					:data-item="item"
				>
					<image
						:src="item.productPic"
						style
						lazy-load
						mode="aspectFill"
					></image>
					<view
						class="collectBox__listBox--in--imgUseless"
						v-if="item.status !== 1"
						>失效</view
					>
					<view class="collectBox__listBox--in--content">
						<view
							class="product__name"
							:style="'color:' + (item.status !== 1 ? '#ccc' : '')"
						>
							{{ item.productName }}
						</view>
						<view class="price__line" v-if="item.status === 1">
							<view class="product__price">
								<span>{{ item.intPrice }}</span>
								<span style="font-weight:weight;font-size:13px;"
									>.{{ item.smaPrice }}</span
								>
							</view>
							<view class="product__oriPrice">{{ item.originalPrice }}</view>
						</view>
						<view v-else class="price__useless">失效</view>
					</view>
					<view class="longDelate" v-if="item.longType">
						<view
							class="longDelate__centerDelete"
							@tap.stop="deleteSure"
							:data-index="index"
						>
							删除
						</view>
					</view>
					<van-dialog id="van-dialog"></van-dialog>
				</view>
			</view>
			<view class="tipNoMore">-------没有更多了-------</view>
		</view>
		<view v-else :style="'height:' + height + 'px'" class="noReviews">
			<view class="noReviews__center">
				<image
					src="http://medusa-small-file.oss-cn-hangzhou.aliyuncs.com/api/collect.png"
					class="noReviews__center--img"
				></image>
				<view class="noReviews__center--text">暂无收藏~</view>
			</view>
			<view class="noCollect__button" @tap="gotoIndex">去逛一逛</view>
		</view>
	</view>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator'
import { userCollectList, userDelCollect } from '@/api/modules/goods'
import Dialog from '@/wxcomponents/vant-weapp/dialog/dialog'
import { GoodInfo } from '@/pages/index/modules/shopCar/shopCarType'
type PickGoodInfo = Pick<
	GoodInfo,
	'originalPrice' | 'productId' | 'productName' | 'userId' | 'status'
>
interface ListItem extends PickGoodInfo {
	productPic: string
	productPrice: string | number
	longType: boolean
	collectId?: number
	intPrice: string
	productName: string
	smaPrice: string
	status: number
	userId: string
}
@Component({})
export default class Collect extends Vue {
	/** 收藏商品 */
	list: Array<ListItem> = []
	height: number = 0

	onShow() {
		this.getCollList()
		uni.getSystemInfo({
			success: (res) => {
				this.setData({
					height: res.windowHeight
				})
			}
		})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 获取收藏商品
	 */

	getCollList() {
		userCollectList({}).then((res) => {
			res.forEach((item: ListItem) => {
				item.originalPrice = Number(item.originalPrice).toFixed(2)
				item.longType = false
				item.productPrice = Number(item.productPrice).toFixed(2)
				item.intPrice = item.productPrice.split('.')[0]
				item.smaPrice = item.productPrice.split('.')[1]
			})
			this.setData({
				list: res
			})
		})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 删除收藏
	 * @param {*} e
	 */

	deleteCollect(e: { currentTarget: { dataset: { index: number } } }) {
		const index = e.currentTarget.dataset.index
		const list = this.list
		list.forEach((v) => {
			v.longType = false
		})
		this.setData({
			list
		})

		list[index].longType = true
		this.setData({
			[`list[${index}].longType`]: list[index].longType
		})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 前往商品详情
	 * @param {*} e
	 */
	toProductDetail(e: { currentTarget: { dataset: { index: number } } }) {
		const list = this.list
		const index = e.currentTarget.dataset.index
		const type = list.some((v) => {
			return v.longType === true
		})
		if (!type) {
			uni.navigateTo({
				url: '/subcontract/pages/detail/detail?id=' + list[index].productId
			})
			return
		}
		list.map((item) => (item.longType = false))
		this.setData({
			list
		})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 删除收藏
	 * @param {*} e
	 */

	deleteSure(e: { currentTarget: { dataset: { index: number } } }) {
		const index = e.currentTarget.dataset.index
		const list = this.list
		const parma = {
			originalPrice: list[index].originalPrice,
			productId: list[index].productId,
			productName: list[index].productName,
			productPic: list[index].productPic,
			productPrice: list[index].productPrice,
			status: list[index].status
		}
		Dialog.confirm({
			title: '提示',
			message: '确认删除此收藏商品吗'
		}).then(() => {
			userDelCollect(parma).then(() => {
				wx.showToast({ title: '删除成功', icon: 'none' })
				this.getCollList()
			})
		})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 返回首页
	 */

	gotoIndex() {
		this.$STORE.setStore.backHome()
	}
}
</script>

<style lang="scss" scoped>
page {
	background-color: #f4f4f4;
}

.collectBox {
	padding: 10px;
	background-color: #f4f4f4;
	height: 100%;

	.collectBox__listBox {
		display: flex;
		justify-content: space-between;
		flex-wrap: wrap;

		.collectBox__listBox--in {
			width: 345rpx;
			margin-bottom: 16rpx;
			position: relative;
		}
		image {
			width: 345rpx;
			height: 345rpx;
		}

		.collectBox__listBox--in--imgUseless {
			width: 100%;
			height: 25px;
			position: absolute;
			bottom: 67px;
			display: flex;
			align-items: center;
			justify-content: center;
			color: white;
			font-size: 13px;
			background-color: #f9b752;
			opacity: 0.5;
		}

		.collectBox__listBox--in--content {
			width: 100%;
			background-color: white;
			margin-top: -11rpx;
			padding: 10px;
			border-bottom-right-radius: 9px;
			border-bottom-left-radius: 9px;
		}
	}
}

.product__name {
	overflow: hidden;
	white-space: nowrap;
	text-overflow: ellipsis;
	font-size: 26rpx;
	height: 18px;
}

.price__line {
	display: flex;
	align-items: flex-end;
	margin-top: 5px;
}

.price__useless {
	width: 40px;
	height: 20px;
	border-radius: 50px;
	display: flex;
	align-items: center;
	justify-content: center;
	color: #f6f6f6;
	font-size: 13px;
	margin-top: 14px;
	background-color: #d3d3d3;
}

.product__price {
	margin-top: 5px;
	font-size: 17px;
	color: #fa5555;
	margin-right: 4px;
	font-weight: bold;
}

.product__price::before {
	margin-top: 5px;
	content: '￥';
	font-size: 13px;
}

.product__oriPrice {
	margin-top: 5px;
	font-size: 12px;
	color: #ccc;
	font-weight: bold;
	text-decoration: line-through;
	margin-left: 5px;
}

.product__oriPrice::before {
	margin-top: 5px;
	font-size: 12px;
	content: '￥';
}

.longDelate {
	position: absolute;
	width: 40px;
	height: 50px;
	background-color: rgba(0, 0, 0, 0.4);
	display: flex;
	justify-content: center;
	align-items: center;
	top: 0px;
	width: 349rpx;
	height: 100%;
	border-radius: 9px;
}

.longDelate__centerDelete {
	display: flex;
	justify-content: center;
	width: 140rpx;
	height: 140rpx;
	background-color: #fea00b;
	border-radius: 70rpx;
	color: white;
	align-items: center;
}

.tipNoMore {
	display: flex;
	justify-content: center;
	align-items: center;
	color: #d0d2d6;
	font-size: 12px;
}

.noCollect {
	display: flex;
	align-items: center;
	justify-content: center;
	width: 100%;
	flex-wrap: wrap;
}

.noCollect__button {
	height: 40px;
	width: 120px;
	display: flex;
	align-items: center;
	justify-content: center;
	color: #e4322c;
	border-radius: 50px;
	border: 1px solid #e4322c;
	font-size: 14px;
	margin-top: -110px;
}

.shop__noCar {
	margin-top: 70px;
	width: 69%;
}

.noReviews {
	display: flex;
	width: 100%;
	justify-content: center;
	background-color: white;
	flex-wrap: wrap;

	&__center {
		display: flex;
		justify-content: center;
		width: 590rpx;
		flex-wrap: wrap;
		height: 340px;

		&--img {
			margin-top: 50px;
		}

		&--text {
			margin-top: 10px;
			font-size: 16px;
		}
	}
}
</style>
