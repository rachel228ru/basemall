<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-12 15:36:21
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-09-01 16:03:28
 * 123
-->
<template>
	<view class="order__detail">
		<van-toast id="van-toast"></van-toast>
		<van-dialog confirm-button-color="#fe4e63" id="van-dialog"></van-dialog>
		<view class="order--status">
			<block>
				<!-- 交易关闭 -->
				<view v-if="order.isClose(detail.status)">
					<view class="status--text">交易关闭</view>
					<view style="status--description">{{
						order.getOrderStatusTips(detail.status)
					}}</view>
				</view>
				<!-- 待付款 -->
				<view v-else-if="order.isPendingPayment(detail.status)">
					<view class="status--text">等待买家付款</view>
					<view class="status--description">
						<text>剩</text>
						<van-count-down
							:time="detail.expireTime"
							style="color:#FFFFFF"
						></van-count-down>
						<text>自动关闭</text>
					</view>
				</view>
				<view v-else>
					<view class="status--text">{{
						order.getOrderStatusName(detail.status)
					}}</view>
				</view>
				<view>
					<i :class="'iconfont ' + helper.getStatusIcon(detail.status)"></i>
				</view>
			</block>
		</view>
		<view class="order--receiver">
			<van-cell-group>
				<van-cell
					title="配送方式"
					:value="order.getDeliveryTypeName(detail.orderDelivery.deliveryType)"
					value-class="logistic--type"
				>
					<view slot="icon" class="iconfont icondingdanxiangqing-wuliu"></view>
				</van-cell>
				<van-cell
					v-if="steps.length"
					@tap="goDeliveryDetail"
					:title="steps[0] && steps[0].context"
					:label="steps[0] && steps[0].time"
					title-class="logistic--detail"
					is-link
				>
					<view slot="icon" class="iconfont icondingdanxiangqing-wuliu"></view>
				</van-cell>
				<van-cell
					v-if="detail.orderDelivery.deliveryType === 'LOGISTICS'"
					:title="'收货人：' + detail.orderDelivery.receiverName"
					:value="detail.orderDelivery.receiverPhone"
					:label="'取货地址：' + helper.getAddress(detail.orderDelivery)"
					label-class="addr"
					value-class="phone"
					custom-class="receiver-info"
				>
					<view
						slot="icon"
						class="iconfont icongerenzhongxin-dizhiguanli"
					></view>
				</van-cell>
				<van-cell
					v-else
					center
					:title="
						detail.orderDelivery.receiverName +
							' ' +
							detail.orderDelivery.receiverPhone
					"
					:label="helper.getAddress(detail.orderDelivery)"
					label-class="addr"
					value-class="phone"
					title-class="cell-style"
					label-style="cell-style"
					custom-class="receiver-info"
				>
					<view
						slot="icon"
						class="iconfont icongerenzhongxin-dizhiguanli"
					></view>
					<van-button
						class="addr__button"
						v-if="helper.canUpdateAddress(detail.status)"
						@tap="handleChangeAddress"
						:data-item="detail.orderDelivery"
						slot="right-icon"
						color="#FF4E63"
						plain
						size="mini"
					>
						修改
					</van-button>
				</van-cell>
			</van-cell-group>
		</view>
		<view class="order--message" v-if="detail.userNote">
			<view class="message--l">
				<image
					class="message--icon"
					src="https://medusa-small-file.oss-cn-hangzhou.aliyuncs.com/images/message-icon.png"
				></image>
			</view>
			<view class="message--r">
				<view class="message--label">买家留言</view>
				<view class="message--content">{{ detail.userNote }}</view>
			</view>
		</view>
		<view class="order--goods">
			<view v-for="(item, index) in detail.orderItemList" :key="index">
				<view
					@tap="goGoods"
					:data-id="item.productId"
					:data-img="item.productPic"
				>
					<goods :key="index" :goodsData="item"> </goods>
				</view>

				<!-- 配送中不可申请售后 -->
				<block
					v-if="
						after.userApplyBtnVisible(detail.status, item.afs) &&
							after.applyNumNotExceeded(item.afs, item.applyNum) &&
							!detail.expire
					"
				>
					<!-- <view>
          {{after.userApplyBtnVisible(detail.status, item.afs)  && 1}}
         {{after.applyNumNotExceeded(item.afs, detail.applyNum)  && 1}}
            {{!detail.expire  && 1}}
        </view> -->
					<view
						v-if="after.userApplyVisible(item.afs) || after.isRevoke(item.afs)"
						class="a__btn"
						:data-actions="
							after.afterActions(
								'user',
								detail.status,
								detail.orderDelivery.deliveryType === 'LOGISTICS'
							)
						"
						:data-afs="item.afs"
						:data-goods="item"
						@tap="handleApply"
					>
						{{
							after.afterActions(
								'user',
								detail.status,
								detail.orderDelivery.deliveryType === 'LOGISTICS'
							).length === 1
								? '申请退款'
								: '申请售后'
						}}
					</view>
					<block v-else>
						<view
							class="a__btn"
							@tap="goAfterDetail"
							:data-goods="item"
							:data-afsid="item.afs.id"
						>
							{{ after.getAfterTypeName(item.afs.type) }}
							{{ after.getAfterStatusName(item.afs.status) }}
						</view>
						<!-- <view wx:else class="a__btn" bind:tap="goAfterDetail" data-goods="{{item}}" data-afsid="{{item.afs.id}}"></view> -->
					</block>
				</block>
			</view>
		</view>
		<van-cell
			v-if="detail.couponAmount"
			title="优惠券"
			:value="'-￥' + detail.couponAmount"
			value-class="price"
		></van-cell>
		<van-cell
			v-if="detail.fullScaleAmount"
			title="满减优惠"
			:value="'-￥' + detail.fullScaleAmount"
			value-class="price"
		>
		</van-cell>
		<van-cell
			v-if="detail.memberAmount"
			title="会员本单立省"
			:value="detail.memberAmount"
			value-class="price"
		></van-cell>
		<van-cell
			v-if="detail.integrationAmount"
			title="积分抵扣"
			:value="'-￥' + detail.integrationAmount"
			value-class="price"
		>
		</van-cell>
		<van-cell
			v-if="detail.freightAmount"
			title="配送费"
			:value="detail.freightAmountstr"
			value-class="price"
		></van-cell>
		<van-cell
			title="支付总金额"
			:value="'¥' + detail.payAmount"
			value-class="price"
			custom-class="order-price"
		></van-cell>
		<block v-for="(item, index) in customForm" :key="index">
			<block v-if="item.value">
				<van-cell
					v-if="formHelper.isInputItem(item.type)"
					:key="index"
					:title="item.key"
					:value="item.value"
					custom-class="order-price"
				></van-cell>
				<van-cell
					v-else-if="formHelper.isDateItem(item.type)"
					:key="index"
					:title="item.key"
					:value="item.time"
					custom-class="order-price"
				></van-cell>
				<van-cell
					v-else
					@tap="handleSeePhoto"
					:data-item="item"
					:title="item.key"
					is-link
					value="点击查看"
					:border="false"
				>
				</van-cell>
			</block>
		</block>
		<view class="order--info">
			<view>订单编号：{{ detail.id }}</view>
			<view>支付方式：{{ order.getPayTypeName(detail.payType) }}</view>
			<view>创建时间：{{ detail.createTime }}</view>
			<view v-if="detail.payTime">付款时间：{{ detail.payTime }}</view>
			<view v-if="detail.orderDelivery.deliveryTime">
				发货时间：{{ detail.orderDelivery.deliveryTime }}
			</view>
			<view v-if="detail.closeTime">关闭时间：{{ detail.closeTime }}</view>
			<view v-if="detail.commentTime">评价时间：{{ detail.commentTime }}</view>
			<view v-if="detail.orderDelivery.receiveTime">
				成交时间：{{ detail.orderDelivery.receiveTime }}
			</view>
		</view>
		<view class="order--buttons" v-if="userType === 'MALL'">
			<!-- 待付款 -->
			<block v-if="order.isPendingPayment(detail.status)">
				<button class="button--item" @tap="backHome">返回首页</button>
				<button
					class="button--item primary"
					@click="pay({ id: detail.id, type: '' }, callbackAction)"
				>
					立即付款
				</button>
			</block>
			<!-- 已关闭 -->
			<block v-else-if="order.isClose(detail.status)">
				<button class="button--item" @tap="backHome">返回首页</button>
				<button class="button--item primary" @tap="buyAgain">再次购买</button>
			</block>
			<!-- 待提货 -->
			<block v-else-if="order.isWaitTake(detail.status)">
				<button class="button--item" @tap="backHome">返回首页</button>
				<button
					class="button--item primary"
					@click="receipt({ id: detail.id, disabled: false }, callbackAction)"
					v-if="!detail.rBtnStatus"
				>
					确认收货
				</button>
			</block>
			<!-- 待评价 -->
			<block v-else-if="order.isWaitComment(detail.status)">
				<button class="button--item" @tap="backHome">返回首页</button>
				<button
					class="button--item primary"
					@tap="goReviews"
					:data-id="detail.id"
					v-if="hasOpenEvaluate"
				>
					立即评价
				</button>
			</block>
			<block v-else>
				<button class="button--item" @tap="backHome">返回首页</button>
			</block>
		</view>
		<van-action-sheet
			title="申请售后"
			:show="sheetVisible"
			:actions="
				after.afterActions(
					'user',
					detail.status,
					detail.orderDelivery.deliveryType === 'LOGISTICS'
				)
			"
			@close="onClose"
			@select="onSelect"
		></van-action-sheet>
	</view>
</template>

<script module="after" lang="wxs" src="@/wxs/afterSale.wxs"></script>
<script module="formHelper" lang="wxs" src="@/wxs/customForm.wxs"></script>
<script module="helper" lang="wxs" src="./helper.wxs"></script>
<script module="order" lang="wxs" src="../order/order.wxs"></script>

<script lang="ts">
import { Component, Mixins } from 'vue-property-decorator'
import OrderMix from '@/mixins/order'
import { getOrderDetail, getdeliveryInfo } from '@/api/modules/order'
import Toast from '@/wxcomponents/vant-weapp/toast/toast'
import { getApplyNumber } from '@/api/modules/afterSale'
import { isNumber } from 'lodash'
import {
	IAfterOrderDetail,
	IAfterOrderDetailDelivery,
	IQueryStr,
	ISteps
} from '../afterSaleDetail/afterSaleDetailType'
import { IAfs, IGoodsList } from '@/typings/goods'

const order = require('../order/order.wxs')
const after = require('@/wxs/afterSale.wxs')
const formHelper = require('@/wxs/customForm.wxs')
const helper = require('./helper.wxs')

interface IFromItem {
	key: string
	value: string | number
	type: string
	required: boolean
}

interface IApplyItem {
	productSkuId: string
	userApplyNumber: string
}
@Component({})
export default class Order extends Mixins(OrderMix) {
	detail: IAfterOrderDetail = {
		applyNumHasExceeded: true
	} as IAfterOrderDetail
	/** 当前查看的用户 做交互按钮显示隐藏用 */
	userType: string = 'user'
	/** 物流信息 */
	steps: ISteps[] = []
	/** 自定义表单数据 */
	customForm: IFromItem[] = []
	/** 是否为第一次进入 */
	isFirstEnter: boolean = true
	/** 售后弹窗 */
	sheetVisible: boolean = false

	afterSaleForm = {
		description: '',
		images: '',
		productQuantity: '',
		productSkuId: '',
		refundAmount: '',
		templateId: '',
		type: ''
	}

	/** 要申请的售后商品 */
	afGoods: any = {}

	afs: IAfs = {} as IAfs

	options = { orderId: '', userType: '' }

	order = order
	after = after
	formHelper = formHelper
	helper = helper

	onLoad(options: { orderId: string; userType: string }) {
		const orderId = options.orderId
		this.setData({
			options
		})
		if (this.$STORE.setStore.isReady) {
			this.getOrderDetail(orderId)
		}

		this.$Pubsub.on('app-launch', () => {
			this.getOrderDetail(orderId)
		})
	}

	onShow() {
		if (this.isFirstEnter) return
		this.getOrderDetail(this.detail.id)
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 获取售后次数
	 */
	getApplyNum() {
		getApplyNumber(this.detail.id)
			.then(({ number, expire }: { number: IApplyItem[]; expire: boolean }) => {
				let orderItemList: IGoodsList[] = []
				this.detail.orderItemList.forEach((goods) => {
					const apply: IApplyItem[] = number.filter((item) => {
						return item.productSkuId === goods.productSkuId
					})
					const applyNum = apply.length > 0 ? apply[0].userApplyNumber : '0'
					orderItemList.push({
						...goods,
						applyNum
					})
				})
				this.setData({
					['detail.orderItemList']: orderItemList,
					['detail.expire']: expire
				})
			})
			.catch(() => {
				this.$Popup.toast('申请次数获取失败')
			})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 处理申请售后
	 * @param {*}e
	 */

	handleApply({
		currentTarget: {
			dataset: { actions, goods, afs }
		}
	}: {
		currentTarget: {
			dataset: {
				actions: Record<'name' | 'id', string>[]
				goods: IGoodsList
				afs: IAfs
			}
		}
	}) {
		if (Number(goods.applyNum) > 3) return this.$Popup.toast('超过最大申请数')

		this.setData({
			afs
		})
		if (actions.length === 1) {
			this.setData({
				afGoods: goods
			})
			this.handleApplyAfter('退款', 'REFUND', {
				hasPicked: 1
			})
			return
		}

		this.setData({
			afGoods: goods
		})

		this.toggleSheetVisible()
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 前往售后详情
	 * @param {*} e
	 */

	goAfterDetail({
		currentTarget: {
			dataset: { afsid, goods }
		}
	}: {
		currentTarget: {
			dataset: {
				afsid: string
				goods: IGoodsList
			}
		}
	}) {
		uni.navigateTo({
			url: `/subcontract/pages/afterSaleDetail/afterSaleDetail?afsid=${afsid}&userType=${
				this.userType
			}&goods=${encodeURIComponent(JSON.stringify(goods))}`
		})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description:售后action选择
	 * @param {*} e
	 */

	onSelect({
		detail: { name, id: type }
	}: {
		detail: {
			name: string
			id: string
		}
	}) {
		this.handleApplyAfter(name, type, {})
	}

	/** 将对象序列化成query字符串 */
	/**
	 * @LastEditors: chuyinlong
	 * @description: 将对象序列化成query字符串
	 * @param {*} data
	 */

	objectToQueryStr(data: IQueryStr) {
		let str = ''
		Object.keys(data).map((key, i) => {
			return (str += `${!i ? '' : '&'}${key}=${data[key]}`)
		})
		return str
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 处理申请售后
	 * @param {string} name
	 * @param {string} type
	 * @param {{hasPicked?:number}} params
	 * @returns {*}
	 */

	handleApplyAfter(name: string, type: string, params: { hasPicked?: number }) {
		const { id, status } = this.detail
		const goods = this.afGoods

		const afterList = this.detail.orderItemList.filter((item) => {
			return after.userApplyVisible(item.afs) || after.isRevoke(item.afs)
		})
		const peisong =
			this.detail.privileges && this.detail.privileges.includes('2')

		const formStr = this.objectToQueryStr({
			...this.afterSaleForm,
			title: name,
			type,
			orderId: id,
			productSkuId: goods.productSkuId,
			goods: encodeURIComponent(JSON.stringify(goods)),
			productQuantity: goods.productQuantity,
			refundAmount:
				goods.realAmount +
				(afterList.length > 1 && peisong ? 0 : this.detail.freightAmount),
			hasPicked: status === 'WAIT_FOR_PICKUP' ? 1 : 0,
			userType: this.userType,
			...params
		})
		uni.navigateTo({
			url: `/subcontract/pages/applyAfterSale/applyAfterSale?${formStr}`
		})
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
	 * @description: 转换表单数据
	 * @param {*} forms
	 */

	transformFormToData(forms: string) {
		try {
			const customForm = JSON.parse(forms)
			this.setData({
				customForm
			})
			return true
		} catch {
			return false
		}
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 处理修改地址
	 * @param {*} e
	 */

	handleChangeAddress({
		currentTarget: {
			dataset: { item }
		}
	}: {
		currentTarget: {
			dataset: {
				item: IAfterOrderDetailDelivery
			}
		}
	}) {
		uni.navigateTo({
			url: `/subcontract/pages/updateOrderAddress/updateOrderAddress?form=${JSON.stringify(
				{
					...item,
					orderId: this.detail.id
				}
			)}`
		})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 查看物流详情
	 */

	goDeliveryDetail() {
		const data = {
			orderItemList: this.detail.orderItemList,
			orderDelivery: this.detail.orderDelivery
		}
		uni.navigateTo({
			url: `/subcontract/pages/deliveryInfo/deliveryInfo?data=${encodeURIComponent(
				JSON.stringify(data)
			)}`
		})
		//
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 获取物流信息
	 * @param {*}
	 */

	getdeliveryInfo({
		tracesId,
		companyName,
		deliveryCode
	}: {
		tracesId: string
		companyName: string
		deliveryCode: string
	}) {
		getdeliveryInfo({
			tracesId,
			companyName,
			deliveryCode
		})
			.then((res) => {
				const steps = res ? JSON.parse(res) : []
				this.setData({
					steps: steps
				})
			})
			.then(() => null)
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 查看大图
	 * @param {*}
	 * @returns {*}
	 */
	handleSeePhoto({
		currentTarget: {
			dataset: { item }
		}
	}: {
		currentTarget: {
			dataset: {
				item: { value: string }
			}
		}
	}) {
		uni.previewImage({
			current: item.value,
			urls: [item.value]
		})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description:mixins内成功回调
	 */

	callbackAction() {
		this.getOrderDetail(this.options.orderId || this.detail.id)
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 获取订单详情
	 * @param {string} orderId
	 * @returns {*}
	 */

	getOrderDetail(orderId: string) {
		getOrderDetail(orderId)
			.then((detail) => {
				this.setLocalFreightAmount(detail)
				detail = this.setCountdown(detail)
				detail = this.setAfsData(detail)

				this.setData(
					{
						detail,
						isFirstEnter: false,
						userType: detail.type
					},
					() => this.getApplyNum()
				)

				// 如果存在物流数据 获取物流信息
				if (detail.orderDelivery) {
					this.getdeliveryInfo({
						tracesId: detail.orderDelivery.deliverySn,
						companyName: detail.orderDelivery.deliveryCompany,
						deliveryCode: detail.orderDelivery.deliveryCode
					})
				}

				this.transformFormToData(detail.customForm)
			})
			.catch(() => {
				Toast({
					message: '详情获取失败'
				})
			})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 获取售后信息
	 * @param {IAfterOrderDetail} item
	 */
	setAfsData(item: IAfterOrderDetail) {
		let status: boolean = false
		if (item.orderItemList) {
			item.orderItemList.map((goods) => {
				if (goods.afs) {
					if (!status) {
						status = this.receiptDisabled(goods.afs)
					}
				}
			})
		}
		item.rBtnStatus = status

		return item
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 获取配送费
	 * @param {IAfterOrderDetail} detail
	 */
	setLocalFreightAmount(detail: IAfterOrderDetail) {
		detail.freightAmountstr =
			detail.privileges && detail.privileges.includes('2')
				? '免配送费'
				: '￥' + detail.freightAmount
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 设置倒计时
	 * @param {IAfterOrderDetail} detail
	 */

	setCountdown(detail: IAfterOrderDetail) {
		if (detail.expireTime && !isNumber(detail.expireTime)) {
			detail.expireTime = String(
				+new Date(detail.expireTime.replace(/-/gi, '/')) - +new Date()
			)
		}
		return detail
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 前往商品详情
	 * @param {*} e
	 */
	goGoods({
		currentTarget: {
			dataset: { id, img }
		}
	}: {
		currentTarget: {
			dataset: {
				id: string
				img: string
			}
		}
	}) {
		uni.navigateTo({
			url: `/subcontract/pages/detail/detail?id=${id}&img=${img}`
		})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 返回首页
	 */
	backHome() {
		this.$STORE.setStore.backHome()
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 再来一单
	 */
	buyAgain() {
		const items = this.detail.orderItemList.map((item) => {
			return {
				activityId: item.activityId,
				activityProductId: item.activityProductId,
				dominoState: item.dominoState,
				number: item.productQuantity,
				skuId: item.productSkuId
			}
		})

		uni.navigateTo({
			url: `/modules/pages/submit/submit?items=${encodeURIComponent(
				JSON.stringify(items)
			)}`
		})
	}
}
</script>

<style lang="scss" scoped>
@import '@/assets/styles/var';

.order__detail {
	@mixin m-t {
		margin-bottom: 10px;
	}

	background: $main-background;
	min-height: 100%;
	padding-bottom: 40px;

	.a__btn {
		display: block;
		max-width: 150px;
		min-width: 80px;
		padding: 10rpx 20rpx;
		text-align: center;
		border: 1px solid $main-light-color;
		margin: 0 20rpx 20rpx auto;
		border-radius: 30rpx;
		margin-left: auto;
		color: $main-light-color;
	}

	.addr__button {
		margin-left: 0;
	}

	&--tag {
		width: 50px;
		height: 16px;
		background-color: #ffecee;
		border-radius: 25rpx;
		color: #fe7888;
		margin-right: 20rpx;
		display: flex;
		justify-content: center;
		align-items: center;
		font-size: 12px;
		border: 1px solid #fe7888;
		padding: 0px 5px;
	}

	.order--status {
		display: flex;
		justify-content: space-between;
		align-items: center;
		padding: 0 40px;
		height: 135px;
		background: $main-light-color;
		color: #ffffff;

		.status--text {
			font-size: 19px;
		}

		.status--description {
			@include flex();

			margin-top: 15rpx;
		}

		.iconfont {
			font-size: 70px;
		}
	}

	.order--receiver {
		@include m-t;

		&__left {
			@include flex(flex-start);
		}

		.logistic--type {
			font-size: 28rpx;
			color: #fff;
			background: $main-light-color;
			border-radius: 10rpx;
			width: 132rpx;
			text-align: center;
			position: absolute;
			right: 15px;
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

		.icongerenzhongxin-dizhiguanli {
			position: relative;
		}
	}

	.order--message {
		@include flex(space-between);
		@include m-t;

		width: 100%;
		height: auto;
		background: #fff;
		padding: 10px 12.5px;
		box-sizing: border-box;
		color: #585858;
		font-size: 14px;

		.message {
			&--l {
				width: 15px;
				height: 15px;
				flex: 0 0 15px;
			}

			&--r {
				width: calc(100% - 22px);
				padding-left: 10px;
				box-sizing: border-box;
			}

			&--icon {
				width: 15px;
				height: 15px;
			}
		}
	}

	.order--goods {
		padding: 20px 10px 10px;
		background: #fafafa;
	}

	.order--contact {
		height: 80rpx;
		background: #ffffff;
		display: flex;
		justify-content: center;
		align-items: center;

		image {
			width: 25px;
			height: 25px;
			margin-right: 12px;
		}
	}

	.order--info {
		@include m-t;

		font-size: 13px;
		line-height: 25px;
		padding: 20px;
		margin-top: 10px;
		background: #ffffff;
		color: #5c5c5c;
	}

	.order--buttons {
		@include flex;
		position: fixed;
		left: 0;
		bottom: 0;
		width: 100%;

		.button--item {
			flex: 1 1;
			text-align: center;
			padding: 10px 10px;
			background: #fff;

			&.primary {
				background: $main-light-color;
				color: #fff;
			}
		}
	}

	.price {
		font-size: 16px;
		color: $main-light-color;
	}

	.logistic--detail {
		color: rgba(18, 177, 0, 1);
	}

	.receiver--info {
		.van-cell__value,
		.van-cell-text {
			color: #979797;
		}
	}
}

.cell-style {
	width: 560rpx;
}
</style>
