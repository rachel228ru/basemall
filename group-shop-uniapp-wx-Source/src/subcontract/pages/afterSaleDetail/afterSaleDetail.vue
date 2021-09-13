<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-12 15:36:21
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-18 20:36:26
 * 123
-->
<template>
	<view>
		<!--<import src="./exchange.wxml"></import>-->
		<view class="after--detail">
			<!-- 因uniapp不支持微信小程序模板语法，故做此处理。 -->
			<!-- 退货退款 -->
			<template
				v-if="templateName === 'RETURN_REFUND-WAIT_FOR_BUSINESS_APPROVED'"
			>
				<view class="detail--top">
					<view class="detail--top__l">
						<view class="top--status">请等待卖家处理</view>
						<view class="top--time"
							>还剩{{ date.getTimeDifference(detail.deadline) }}</view
						>
					</view>
				</view>
				<view class="detail--description">
					<view class="description--top"
						>你已成功起发退货退款申请，请耐心等待商家处理。</view
					>
					<view class="description--center">
						<view>
							<text class="dot">●</text>
							商家同意或者超时未处理，系统将默认同意。
						</view>
						<view>
							<text class="dot">●</text>
							如果商家拒绝，你可以再次发起售后，卖家会重新处理。
						</view>
					</view>
					<view class="description--buttom">
						<van-button
							size="small"
							style="margin-left:15rpx;"
							@tap="handleCancel"
							>撤销申请</van-button
						>
					</view>
				</view>
			</template>
			<template v-if="templateName === 'RETURN_REFUND-WAIT_FOR_RETURN'">
				<view class="detail--top">
					<view class="detail--top__l">
						<view class="top--status">等待买家退货</view>
						<view class="top--time"
							>还剩{{ date.getTimeDifference(detail.deadline) }}</view
						>
					</view>
				</view>
				<view class="detail--description">
					<view class="description--top"
						>商家已同意你的退货申请，请尽快退货</view
					>
					<view
						class="description--addr"
						v-if="
							detail.originalOrder.orderDelivery.deliveryType === 'LOGISTICS'
						"
					>
						<van-cell-group :border="false">
							<van-cell
								:title="'收货人：' + detail.address.name"
								:value="detail.address.phone"
								:label="'收货地址：' + detail.address.address"
								label-class="addr"
								value-class="phone"
								custom-class="receiver-info"
								:border="false"
							>
								<view
									slot="icon"
									class="iconfont icongerenzhongxin-dizhiguanli"
								></view>
							</van-cell>
						</van-cell-group>
					</view>
					<!-- wx:if="{{detail.originalOrder.orderDelivery.deliveryType==='LOGISTICS'}}" -->
					<view class="description--center border__t">
						<view>
							<text class="dot">●</text>
							超时未处理，系统将自动关闭退款申请
						</view>
						<view>
							<text class="dot">●</text>
							未与商家协商一致，请勿使用到付或平邮，以免商家拒签货物，请填写正确的物流信息
						</view>
					</view>
					<view class="description--buttom">
						<van-button size="small" @tap="handleCancel">撤销申请</van-button>
						<van-button
							size="small"
							style="margin-left:15rpx;"
							@tap="handleUserReturn"
							>退货</van-button
						>
					</view>
				</view>
			</template>
			<!-- 物流退货退款，待商家确认 -->
			<template
				v-if="templateName === 'RETURN_REFUND-WAIT_FOR_BUSINESS_RECEIPT'"
			>
				<view class="detail--top">
					<view class="detail--top__l">
						<view class="top--status">等待商家确认收货</view>
					</view>
				</view>
				<view class="detail--description">
					<view class="description--top"
						>如果商家收到货并验货无误，将操作退款给您</view
					>
					<van-cell
						v-if="detail.steps.length"
						@tap="goDeliveryDetail"
						:title="
							detail.steps[detail.steps.length - 1] &&
								detail.steps[detail.steps.length - 1].text
						"
						:label="
							detail.steps[detail.steps.length - 1] &&
								detail.steps[detail.steps.length - 1].desc
						"
						title-class="logistic--detail"
						is-link
					>
						<view
							slot="icon"
							class="iconfont icondingdanxiangqing-wuliu"
						></view>
					</van-cell>
					<view class="description--center">
						<view>
							<text class="dot">●</text>
							如果商家拒绝退款，需要您和商家协商。
						</view>
						<view>
							<text class="dot">●</text>
							商家同意或者超时未处理，系统将默认同意。
						</view>
					</view>
				</view>
			</template>
			<!-- 换货超时 -->
			<template v-if="templateName === 'RETURN_REFUND-CLOSE'">
				<view class="detail--top">
					<view class="detail--top__l">
						<view class="top--status">退货退款关闭</view>
						<view class="top--time">{{ detail.closeTime }}</view>
					</view>
				</view>
				<view class="detail--description">
					<view class="description--top">
						因{{ af.getCloseType(detail.closeType) }}，本次退货退款关闭。
					</view>
					<view class="description--center">
						<text class="dot">●</text>
						如你的问题未解决，你还可以发起{{ 3 - detail.applyNum }}次申请。
					</view>
					<view class="description--buttom">
						<van-button
							size="small"
							style="margin-left:15rpx;"
							@tap="handleApplyAgain"
							:data-actions="
								af.afterActions(
									'user',
									detail.originalOrder.status,
									detail.originalOrder.orderDelivery.deliveryType ===
										'LOGISTICS'
								)
							"
						>
							再次申请
						</van-button>
					</view>
				</view>
			</template>
			<template v-if="templateName === 'RETURN_REFUND-SUCCESS'">
				<view class="detail--top">
					<view class="detail--top__l">
						<view class="top--status">退货成功</view>
					</view>
				</view>
			</template>

			<view class="detail--delivery">
				<van-cell v-if="af.isRefund(detail.type)">
					<view slot="title">
						<view class="detail--delivery__l">
							<text>申请退款金额</text>
						</view>
					</view>
					<view>￥{{ detail.refundAmount }}</view>
				</van-cell>
				<van-cell
					is-link
					:url="
						'/subcontract/pages/negotiationHistory/negotiationHistory?id=' +
							detail.item.orderId +
							'&type=' +
							'1'
					"
				>
					<view slot="title">
						<view class="detail--delivery__l">
							<text>协商历史</text>
						</view>
					</view>
				</van-cell>
			</view>
			<view class="detail--goods" style="margin-top:16rpx;">
				<view class="detail--goods__title">退款信息</view>
				<view class="detail--goods__container">
					<goods-item
						:goodsData="goods"
						:data-id="detail.item.productId"
						:data-img="goods.productPic"
						@tap="goGoods"
					>
					</goods-item>
				</view>
				<view class="detail--payinfo">
					<view class="detail--payinfo--description"
						>原因 ：{{ detail.description }}</view
					>
					<view v-if="af.isRefund(detail.type)"
						>退款金额 ￥{{ detail.refundAmount }}</view
					>
					<view v-else>换货数量 : {{ detail.item.productQuantity }}</view>
					<view v-if="detail.createTime"
						>申请时间 ：{{ detail.createTime }}</view
					>
					<view v-if="detail.closeTime">拒绝时间 ：{{ detail.closeTime }}</view>
					<view v-if="detail.successTime"
						>成功时间 ：{{ detail.successTime }}</view
					>
					<view v-if="detail.closeTime">关闭时间 ：{{ detail.closeTime }}</view>
				</view>
				<view
					class="detail--images"
					mode="aspectFill"
					v-if="detail.images && detail.images.length"
				>
					<view class="detail--images__title">凭证</view>
					<view class="image--list">
						<image
							v-for="(item, index) in detail.images"
							:key="index"
							:src="item"
							@click="showImage(item)"
						></image>
					</view>
				</view>
			</view>
			<view class="bottom-btns">
				<view
					class="detail--btn"
					@tap="pushHome"
					style="background: #fb4363;color:#fff"
					>返回首页</view
				>
			</view>
			<van-action-sheet
				title="申请售后"
				:show="sheetVisible"
				:actions="
					af.afterActions(
						'user',
						detail.originalOrder.status,
						detail.originalOrder.orderDelivery.deliveryType === 'LOGISTICS'
					)
				"
				@close="onClose"
				@select="onSelect"
			></van-action-sheet>
		</view>
		<!-- actions="{{after.afterActions('user',detail.status, detail.orderDelivery.deliveryType==='LOGISTICS')}}" -->
	</view>
</template>

<script module="af" lang="wxs" src="@/wxs/afterSale.wxs"></script>
<script module="date" lang="wxs" src="@/wxs/date.wxs"></script>
<script lang="ts">
import { Component, Vue } from 'vue-property-decorator'
import GoodsItem from '@/components/goods/goods.vue'
import {
	getAfterDetail,
	cancel,
	getApplyNumber,
	getReturnAddress
} from '@/api/modules/afterSale'
import { getdeliveryInfo } from '@/api/modules/order'
import {
	IAfterDetail,
	IAfterDetailAddress,
	IAfterDetailItem,
	IAfterOrderDetail,
	Idelivery,
	IQueryStr,
	ISteps
} from './afterSaleDetailType'

const after = require('@/wxs/afterSale.wxs')

@Component({
	components: {
		GoodsItem
	}
})
export default class AfterSaleDetail extends Vue {
	/** 售后详情 */
	detail: IAfterDetail = {} as IAfterDetail

	/** 商品详情 */
	goods: IAfterDetailItem = {} as IAfterDetailItem

	/** 售后选项显示隐藏 */
	sheetVisible: boolean = false

	address: IAfterDetailAddress = {} as IAfterDetailAddress

	options: any = {}

	get templateName(): string {
		return this.detail.type + '-' + this.detail.status
	}

	onLoad(options: { afsid?: '301' }) {
		this.options = options
		const { afsid = '301' } = options

		if (this.$STORE.setStore.isReady) {
			this.getAfterDetail(afsid)
		}

		this.$Pubsub.on('app-launch', () => {
			this.getAfterDetail(afsid)
		})
	}

	onShow() {
		this.getAfterDetail(this.options.afsid)
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 前往商品详情
	 */
	goGoods({
		target: {
			dataset: { id, img }
		}
	}: {
		target: {
			dataset: { id: string; img: string }
		}
	}) {
		uni.navigateTo({
			url: `/subcontract/pages/detail/detail?id=${id}&img=${img}`
		})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 处理用户退货
	 */

	handleUserReturn() {
		uni.navigateTo({
			url: `/subcontract/pages/afterSaleReturn/afterSaleReturn?afsId=${
				this.detail.id
			}&goods=${JSON.stringify(this.detail.item)}`
		})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 获取物流信息
	 * @param {Idelivery} res
	 */

	getdeliveryInfo(res: Idelivery) {
		if (!res || Object.values(res).filter((item) => !item).length) return
		getdeliveryInfo({
			tracesId: res.tracesId,
			companyName: res.companyName,
			deliveryCode: res.deliveryCode
		})
			.then((res) => {
				const steps: ISteps[] = JSON.parse(res) || []

				this.setData({
					['detail.steps']: steps
						.map(({ context, time }) => {
							return {
								text: context,
								desc: time
							}
						})
						.reverse()
				})
			})
			.catch(() =>
				this.setData({
					['detail.steps']: []
				})
			)
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 获取售后详情
	 * @param {string} afsid
	 */

	getAfterDetail(afsid: string) {
		getAfterDetail(afsid)
			.then((res) => {
				// 处理截止日期
				res.deadline && (res.deadline = res.deadline.replace(/-/g, '/'))
				// 处理售后图片
				res.images && (res.images = res.images.split(','))
				this.getReturnAddress(res.originalOrder)
				this.setData(
					{
						detail: res,
						goods: res.item
					},
					() => {
						const orderDelivery = res.originalOrder.orderDelivery

						this.getdeliveryInfo({
							tracesId: orderDelivery.deliverySn,
							companyName: orderDelivery.deliveryCompany,
							deliveryCode: orderDelivery.deliveryCode
						})
						this.getApplyNum()
					}
				)
			})
			.catch(() => {
				this.$Popup.toast('售后详情请求失败')
			})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 获取退货地址
	 * @param {IAfterOrderDetail} data
	 */
	getReturnAddress(data: IAfterOrderDetail) {
		if (data.orderDelivery.deliveryType !== 'LOGISTICS' || !data.id) return

		getReturnAddress(data.id)
			.then((address) => {
				this.setData({
					['detail.address']: address
				})
			})
			.catch(() => {
				this.$Popup.toast('退货地址获取失败')
			})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 获取售后申请次数
	 */
	getApplyNum() {
		getApplyNumber(this.detail.item.orderId)
			.then(({ number }) => {
				this.setData({
					['detail.applyNum']: number
				})
			})
			.catch(() => {
				this.$Popup.toast('申请次数获取失败')
			})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 售后action选择
	 */

	onSelect({
		detail: { name, id: type }
	}: {
		detail: { name: string; id: string }
	}) {
		this.userApply(name, type)
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 售后弹窗关闭
	 */

	onClose() {
		this.toggleSheetVisible()
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 显示隐藏售后弹窗
	 */

	toggleSheetVisible() {
		this.setData({
			sheetVisible: !this.sheetVisible
		})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 撤销申请
	 */

	handleCancel() {
		this.$Popup
			.modal({
				title: '提示',
				content: '确认撤销此次售后申请吗？'
			})
			.then(() => {
				cancel(this.detail.id)
					.then(() => {
						this.$Popup.toast('撤销成功')
						this.getAfterDetail(this.options.afsid)
					})
					.catch(() => {
						this.$Popup.toast('撤销失败')
					})
			})
			.catch(() => null)
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 将对象序列化成query字符串
	 * @param {*}
	 */

	objectToQueryStr(data: IQueryStr) {
		let str = ''
		Object.keys(data).map((key: string, i) => {
			return (str += `${!i ? '' : '&'}${key}=${data[key]}`)
		})
		return str
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 用户再次申请
	 * @param {string} name
	 * @param {string} type
	 * @param {number} hasPicked
	 */

	userApply(name: string, type: string, hasPicked: number = 0) {
		const { item, originalOrder } = this.detail
		const goods = originalOrder.orderItemList.find(
			(g: { productSkuId: string }) => g.productSkuId === item.productSkuId
		)

		const afterList = this.detail.originalOrder.orderItemList.filter((item) => {
			return after.userApplyVisible(item.afs) || after.isRevoke(item.afs)
		})
		const peisong =
			this.detail.originalOrder.privileges &&
			this.detail.originalOrder.privileges.includes('2')
		if (goods) {
			const formStr = this.objectToQueryStr({
				title: name,
				type,
				orderId: goods.orderId,
				productSkuId: goods.productSkuId,
				goods: encodeURIComponent(JSON.stringify(item)),
				productQuantity: goods.productQuantity,
				refundAmount:
					goods.realAmount +
					(afterList.length > 1 && peisong
						? 0
						: this.detail.originalOrder.freightAmount),
				userType: 'MALL',
				hasPicked
			})

			uni.navigateTo({
				url: `/subcontract/pages/applyAfterSale/applyAfterSale?${formStr}`
			})
		}
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 再次申请
	 */

	handleApplyAgain({
		currentTarget: {
			dataset: { actions }
		}
	}: {
		currentTarget: {
			dataset: { actions: { name: string; id: string }[] }
		}
	}) {
		if (this.detail.applyNum >= 3)
			return this.$Popup.toast('已超过最大申请次数')

		if (actions.length === 1) {
			return this.userApply('退款', 'REFUND', 1)
		}

		this.toggleSheetVisible()
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 返回首页
	 */
	pushHome() {
		this.$STORE.setStore.backHome()
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description:
	 * @param {string} item
	 */
	showImage(item: string) {
		uni.previewImage({ current: item, urls: this.detail.images })
	}
}
</script>
<style lang="scss" scoped>
page {
	background: #f4f4f4;
}

.after--detail {
	width: 100%;
	height: auto;
	padding-bottom: 50px;
}

.detail--goods__container {
	width: 750rpx;
	background: #fafafa;
	margin-left: -25rpx;
	padding: 20rpx 25rpx 5rpx;
}

.detail--top {
	width: 100%;
	height: 210rpx;
	background: #fe4e63;
	color: #fff;
	display: flex;
	align-items: center;
	justify-content: space-between;
	box-sizing: border-box;
	padding: 101rpx;
	font-size: 34rpx;
}

// .detail--top__l {}

.detail--top__r {
	width: 150rpx;
	height: 109rpx;
}

.detail--top__img {
	width: 150rpx;
	height: 109rpx;
}

.detail--delivery {
	width: 100%;
	height: auto;
	border-bottom: 1px solid #f4f4f4;
}

.detail--delivery__l {
	display: flex;
	align-items: center;
	justify-content: flex-start;
}

.detail--delivery__icon {
	width: 44rpx;
	height: 37rpx;
	margin-right: 10rpx;
}

.detail--cell {
	display: flex;
	align-items: center;
	justify-content: space-between;
	padding: 32rpx 30rpx;
	box-sizing: border-box;
	background: #fff;
	color: #585858;
}

.detail--cell__c {
	font-size: 28rpx;
	margin-left: 24rpx;
	margin-right: auto;
}

.detail--cell__icon {
	width: 30rpx;
	height: 40rpx;
}

.detail--cell__text {
	max-width: 647rpx;
}

.detail--goods {
	padding: 25rpx;
	box-sizing: border-box;
	background: #fff;
}

.detail--goods__btn {
	display: flex;
	justify-content: flex-end;
	padding: 5rpx;
	/* border: 1px solid #999; */
}

.goods-class {
	padding-bottom: 0px !important;
}

.detail__amount {
	color: #fe4e63;
}

.detail--concat {
	background: #fff;
	padding: 15rpx 0;
	display: flex;
	align-items: center;
	justify-content: center;
}

.detail--concat__icon {
	width: 41rpx;
	height: 34rpx;
	margin-right: 15rpx;
}

.detail--payinfo {
	/* padding: 25rpx; */
	box-sizing: border-box;
	background: #fff;
	font-size: 26rpx;
	color: #585858;
	margin-top: 16rpx;
}

.detail--payinfo view {
	margin-bottom: 10rpx;
}

.detail--btn {
	width: 100%;
	text-align: center;
	padding: 20rpx 0;
	margin-top: 16rpx;
	background: #fff;
	font-size: 30rpx;
}

.top--status {
	font-size: 34rpx;
}

.top--time {
	margin-top: 14rpx;
	font-size: 26rpx;
}

.detail--description {
	width: 100%;
	padding-left: 31rpx;
	box-sizing: border-box;
	display: flex;
	justify-content: flex-end;
	flex-direction: column;
	justify-items: flex-end;
	background: #fff;
	margin-bottom: 20rpx;
}

.description--top {
	font-size: 30rpx;
	color: #000;
	padding: 31rpx 0;
	border-bottom: 1px solid #e7e7e7;
	margin-bottom: 15rpx;
}

.description--center {
	font-size: 24rpx;
	color: #8d8d8d;
	margin-bottom: 20rpx;
}

.description--center view:first {
	margin-bottom: 15rpx;
}

.description--buttom {
	margin-left: auto;
	margin-right: 20rpx;
	margin-bottom: 20rpx;
}

.detail--goods__title {
	margin: 10rpx 0 24rpx;
	font-size: 34rpx;
}

.detail--images {
	width: 100%;
}

.image--list {
	width: 100%;
	display: flex;
}

.image--list image {
	margin-right: 8rpx;
	width: 158rpx;
	height: 158rpx;
}

.detail--images__title {
	font-size: 24rpx;
	margin-bottom: 15rpx;
	color: #000000;
}

.dot {
	font-size: 16rpx;
	color: #e2e2e2;
	position: relative;
	top: -3rpx;
}

.bottom-btns {
	position: fixed;
	display: flex;
	width: 100%;
	left: 0;
	bottom: 0;
}

.description--addr {
	width: calc(100% + 20px);
	position: relative;
	left: -20px;
}

.addr {
	width: 528rpx;
}

.receiver-info {
	padding-bottom: 20px;
}

.van-icon,
.iconfont {
	margin-right: 10px;
	font-size: 18px;
}

.addr,
.phone {
	font-size: 28rpx;
	color: #000000;
}

.phone {
	position: absolute;
	right: 15px;
}

.description--addr {
	margin-bottom: 5px;
}

.border__t {
	border-top: 1px solid #f4f4f4;
	padding-top: 5px;
}

.detail--payinfo--description {
	word-break: break-all;
}
</style>
