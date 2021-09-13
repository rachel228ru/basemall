<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-12 15:36:20
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-09-01 16:00:44
 * 123
-->
<template>
	<view
		:style="
			'height:' +
				(couponShow ? 600 : '') +
				'px;overflow:' +
				(couponShow ? 'hidden' : 'auto') +
				';padding-bottom:' +
				(couponShow ? '' : '60') +
				'px;'
		"
	>
		<view
			class="delivery"
			v-if="form.deliverType !== 'LOGISTICS' && deliveryActions.length"
			@click="chooseDeliveryMethod"
		>
			<view>
				<block>配送方式</block>
			</view>
			<view style="display:flex">
				物流配送
				<van-icon name="arrow" class="arrow"></van-icon>
			</view>
		</view>
		<block>
			<view class="address">
				<!-- 用户地址 -->
				<block v-if="miniAccountAddress.length">
					<view class="address__lead" @click.stop="goSelectAddress">
						<view class="address__lead--left">
							<van-icon name="location-o"></van-icon>
							<view style="margin-left:20rpx">{{
								miniAccountAddress[addressIndex].userName
							}}</view>
							<view style="margin-left:20rpx">{{
								miniAccountAddress[addressIndex].phone
							}}</view>
						</view>
						<view class="address__lead--right ">切换地址 ></view>
					</view>
				</block>
				<block v-else>
					<view class="address__empty" @click.stop="goSelectAddress">
						<view>请填写收货地址</view>
						<view>></view>
					</view>
					<view style="width:10px;height:10px"></view>
				</block>
				<image
					src="https://medusa-small-file.oss-cn-hangzhou.aliyuncs.com/%E5%9B%BE%E6%A0%87/static/xian-fd12ebbd3d.png"
				>
				</image>
			</view>
		</block>
		<view class="card">
			<view v-for="(item, index) in itemVoList" :key="index" class="card__good">
				<image :src="item.productSkuPic || item.productPic"></image>
				<view class="card__good--detail">
					<view class="goods--name">{{ item.productName }}</view>
					<view class="card__good--detail--sec">{{ item.specs }}</view>
					<view class="card__good--detail--sec" style="margin-top:40rpx">
						<view>数量 X {{ item.productQuantity }}</view>
						<view class="price">￥{{ item.productPrice }}</view>
					</view>
				</view>
			</view>
		</view>
		<view class="priceCard">
			<view class="priceCard__price">
				<view class="item--label">商品总价</view>
				<view>￥{{ totalPrice }}</view>
			</view>
			<view
				class="priceCard__freight"
				v-if="!helper.isSelfPick(form.deliverType)"
			>
				<block>
					<view>运费</view>
					<view>￥{{ offerOptions.freightAmount }}</view>
				</block>
			</view>
			<view class="priceCard__all">
				<view>合计：</view>
				<view class="priceCard__all--price">￥{{ realPrice }}</view>
			</view>
		</view>
		<view class="form custom" v-if="forms.length">
			<custom-form
				id="customForm"
				ref="customForm"
				:forms="forms"
			></custom-form>
		</view>
		<view class="payWay">
			<van-radio-group :value="form.payType">
				<van-cell-group>
					<van-cell clickable data-name="WECHAT" @click="onPayTypeClick">
						<view class="payWay__title" slot="title">
							<view class="van-cell-text">微信支付</view>
						</view>
						<van-radio
							slot="right-icon"
							checked-color="#FC425A"
							name="WECHAT"
						></van-radio>
					</van-cell>
				</van-cell-group>
			</van-radio-group>
		</view>
		<view class="submit">
			<view class=" submit__left">
				<view>合计：</view>
				<view class="submit__left--all ">
					<view>￥</view>
					<view style="font-size:40rpx">{{ realPrice }}</view>
				</view>
			</view>
			<formid @click="handleSubmit">
				<button>提交订单</button>
			</formid>
		</view>
		<van-toast id="van-toast" :forbidClick="true"></van-toast>
		<view class="submit__mask" v-if="hasSubmited"></view>
	</view>
</template>

<script module="helper" lang="wxs" src="./helper.wxs"></script>
<script lang="ts">
import { Component, Vue } from 'vue-property-decorator'
import Toast from '@/wxcomponents/vant-weapp/toast/toast'
import formid from '@/components/formId/formId.vue'
import customForm from './custom-form/custom-form.vue'
import { SubmitStateType, SubmitOrderType, OfferOptionsType } from './interface'
import {
	getConfirmOrder,
	createOrder,
	payment,
	getOrderStatus,
	getFreightAmount
} from '@/api'
import sum from 'lodash/sum'
import { IAdress, IProcuct, PayType } from './interface'

@Component({
	components: {
		formid,
		customForm
	}
})
export default class Submit extends Vue implements SubmitStateType {
	payTypes: Array<PayType> = [
		{
			name: '微信支付',
			value: 'WECHAT',
			icon: 'iconweixin'
		}
	]
	deliveryActions = [
		{
			name: '物流配送',
			value: 'LOGISTICS'
		}
	]
	forms = []
	form: SubmitOrderType = {
		customForm: '',
		deliverType: 'LOGISTICS',
		formId: '',
		groupLeaderId: '',
		itemDtoList: [],
		miniAccountAddressId: '',
		userNote: '',
		orderType: 'MALL',
		payType: 'WECHAT',
		sourceType: 'MINI_PROGRAM'
	}
	offerOptions: OfferOptionsType = {
		freightAmount: 0
	}
	itemVoList: Array<IProcuct> = []
	miniAccountAddress: Array<IAdress> = []
	addressIndex = 0
	realPrice = 0
	totalPrice = 0
	discountInfoDto = {
		ceiling: 0
	}
	isFirst = true
	address: IAdress = {} as IAdress
	haSubscribed = false
	hasSubmited = false
	prevRoute = ''
	useRebate = true
	supplyBonus = 0
	dataFrom = 'miniApp'
	timer: number = 0
	options = {
		items: ''
	}

	onLoad(options: { items: string }) {
		this.options = options
		this.getOrderData(options)

		this.$Pubsub.on('app-launch', () => {
			this.getOrderData(options)
		})
	}

	onShow() {
		if (!this.isFirst) {
			this.getOrderData(this.options)
		}
	}

	onHide() {
		this.setData({
			isFirst: false
		})
	}

	onUnload() {
		clearInterval(this.timer)
		Toast.clear()
	}

	onShareAppMessage() {
		const shopName = this.$STORE.shopSetStore.shopInfo.shopName
		return {
			title: shopName,
			path: '/pages/index/index?page=home',
			imageUrl: ''
		}
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 设置route
	 */

	getPrevRoute(): string {
		try {
			const pages = getCurrentPages()
			return pages[pages.length - 2].route
		} catch {
			return ''
		}
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 获取配送类型
	 * @returns Promise
	 */

	getShipperSetting(): Promise<unknown> {
		const _form = this.form
		return new Promise((resolve) => {
			_form.deliverType = 'LOGISTICS'
			this.setData({
				deliveryActions: [
					{
						name: '物流配送',
						value: 'LOGISTICS'
					}
				],
				form: _form
			})
			return resolve('1')
		})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 获取实际价格
	 * @returns {number} 实际价格
	 */

	getRealPrice(): number {
		return this.totalPrice
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 设置实际价格
	 */

	setRealPrice(): void {
		const { freightAmount } = this.offerOptions
		const realPrice = this.getRealPrice()
		this.setData({
			realPrice:
				realPrice <= 0
					? freightAmount > 0
						? freightAmount
						: 0.01
					: (realPrice * 100000000 + freightAmount * 100000000) / 100000000
		})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 设置总价格
	 */

	setTotalPrice(): void {
		const totalPrice = sum(
			this.itemVoList.map((item) => {
				return item.productPrice * 1000000 * item.productQuantity
			})
		)

		this.setData(
			{
				totalPrice: (totalPrice / 1000000).toFixed(2)
			},
			() => {
				this.setRealPrice()
			}
		)
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 轮训订单状态
	 * @param {string} orderId 订单号
	 * @param {() => Promise<void>} cb 下一步执行函数
	 */

	getOrderStatus(orderId: string, cb: () => Promise<void>): void {
		this.timer = setInterval(() => {
			getOrderStatus({
				orderId,
				items: JSON.parse(
					decodeURIComponent(
						this.options.items || this.getOrSetItemStorage(true, '')
					)
				)
			})
				.then((res) => {
					// -1->秒杀失败;0->排队中;order->成功;
					if (res === -1) {
						clearInterval(this.timer)
						return this.$Popup.toast('下单失败，请重试')
					}
					if (res === 0) {
						return
					}
					if (res) {
						clearInterval(this.timer)
						cb()
					}
				})
				.catch((err) => {
					clearInterval(this.timer)
					this.$Popup
						.alert({
							title: '温馨提示',
							content: err || '订单状态获取失败'
						})
						.then(() => null)
				})
		}, 1500)
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 获取配送费
	 * @param {{ addressId: string }} query
	 */

	getFreightAmount(query: { addressId: string | number }): void {
		// 配送类型：1->物流模式（需要总金额、收货地址ID）
		const type = 1
		// 物流
		const addressData = this.miniAccountAddress[this.addressIndex]

		// 如果没有地址数据 不发起请求
		if (!addressData) {
			return
		}

		// 如果为物流模式 需要将addressId改为postCode
		const addressId = addressData.postCode

		Object.assign(query, {
			amount: this.totalPrice,
			addressId,
			region: addressId,
			items: this.form.itemDtoList
		})

		getFreightAmount({
			type,
			amount: this.totalPrice,
			...query
		})
			.then(({ cost }) => {
				if (cost === null || cost === -1) {
					return this.$Popup.toast('不在配送范围内')
				}
				this.setData(
					{
						['offerOptions.freightAmount']: cost
					},
					() => {
						this.setRealPrice()
					}
				)
			})
			.catch((err) => {
				this.$Popup.toast(err || '运费获取失败')
			})
	}

	/** 获取提交信息 */
	/**
	 * @LastEditors: chuyinlong
	 * @description:
	 * @param {*} options
	 */

	getOrderData({ items = this.getOrSetItemStorage(true, '') }): void {
		Toast.loading({
			duration: 15000
		})
		getConfirmOrder({
			items: JSON.parse(decodeURIComponent(items)),
			type: 'MALL'
		})
			.then((res) => {
				// 设置一系列数据
				this.setResToData(res, items)
				Toast.clear()
			})
			.catch((err) => {
				this.$Popup.toast(err || '结算页面信息获取失败')
			})
	}

	/** 将数据设置到data */
	/**
	 * @LastEditors: chuyinlong
	 * @description:
	 * @param {*} res
	 * @param {string} items
	 */

	setResToData(
		res: {
			miniAccountAddress: Array<IAdress>
			itemVoList: Array<IProcuct>
			discountInfoDto: {
				ceiling: number
			}
			supplyBonus: number
		},
		items: string
	): void {
		const { miniAccountAddress, itemVoList, discountInfoDto, supplyBonus } = res
		let integralValue = 1

		this.getOrSetItemStorage(false, items)

		this.setData(
			{
				miniAccountAddress,
				itemVoList,
				discountInfoDto,
				integralValue,
				supplyBonus,
				['form.itemDtoList']: JSON.parse(decodeURIComponent(items))
			},
			async () => {
				try {
					this.setData({
						forms: this.getCustomFrom(),
						prevRoute: this.getPrevRoute()
					})

					await this.getShipperSetting()

					this.setAddressIndexAndId()

					this.setTotalPrice()
				} catch (err) {
					this.$Popup.toast(err)
				}
			}
		)
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 获取自定义表单
	 * @returns {string} customFrom 自定义表单数据
	 */

	getCustomFrom(): string {
		return JSON.parse(this.$STORE.setStore.order.customFrom)
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 设置items缓存做跳转回来使用
	 * @param {boolean} isGet
	 * @param {?string} items
	 * @returns {string} items
	 */

	getOrSetItemStorage(isGet = false, items: string): string {
		if (isGet) {
			return uni.getStorageSync('order-items') || items
		}

		uni.setStorageSync('order-items', items || this.options.items)
		return ''
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 获取或者设置数据缓存
	 * @param {boolean} isGet
	 */

	getOrsetStorage(isGet = false): void {
		const keys = ['form', 'itemVoList', 'miniAccountAddress', 'addressIndex']
		const data: { [x: string]: any } = {}
		const storageData = JSON.parse(uni.getStorageSync('submitData') || '{}')
		const setData = (store: { [x: string]: any }) => {
			keys.forEach((key) => {
				data[key] = store[key]
			})
		}

		if (isGet) {
			if (storageData) {
				uni.getStorageSync('submitData')
				setData(storageData)
				uni.setStorageSync('submitData', JSON.stringify(data))
				return
			} else {
				return
			}
		}
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 验证表单
	 * @returns {boolean}
	 */
	verificationForm(): boolean {
		const formComponent = this.$refs.customForm as customForm
		return formComponent.verificationForm()
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 验证表单
	 * @returns {boolean}
	 */

	handleVerification(): boolean {
		const formComponent = this.$refs.customForm as customForm
		// 自定义表单验证
		if (!formComponent || !formComponent.verificationForm()) {
			return false
		}

		const addressData = this.miniAccountAddress[this.addressIndex]
		if (!addressData || !addressData.id) {
			this.$Popup
				.alert({
					title: '温馨提示',
					content: '请添加地址'
				})
				.then(() => {
					this.goSelectAddress()
				})
			return false
		}

		return true
	}
	/**
	 * 提交订单
	 * BALANCE WECHAT FRIEND
	 */
	/**
	 * @LastEditors: chuyinlong
	 * @description: 提交订单
	 * @param {string} formId
	 * @returns {Promise<void>}
	 */

	async handleSubmit(formdata: { formId: string }): Promise<void> {
		try {
			if (!this.handleVerification() || this.hasSubmited) {
				return
			}
			this.setData({
				hasSubmited: true
			})
			const formComponent = this.$refs.customForm as customForm

			if (!this.haSubscribed) {
				/** 模板订阅 */
				await this.$STORE.messageStore.subscribe([
					this.$STORE.messageStore.type.sendMsg,
					this.$STORE.messageStore.type.memberCartOpen
				])
				this.setData({
					haSubscribed: true
				})
			}

			Toast.loading({
				duration: 300000
			})

			/** 如果是从H5进入小程序支付订单 删除activityId 添加solitaireActivityId */
			if (this.dataFrom === 'h5') {
				this.form.itemDtoList.forEach((inItem: any) => {
					inItem.solitaireActivityId = inItem.activityId
					delete inItem.activityId
				})
			}

			const { id } = this.miniAccountAddress[this.addressIndex]
			// 创建订单
			const orderId = (await createOrder(
				Object.assign(this.form, {
					formId: formdata.formId,
					groupLeaderId: this.form.groupLeaderId || '',
					groupLeaderNickname: '',
					pointAuthor: '',
					point_phone: '',
					miniAccountAddressId: id,
					customForm: formComponent
						? JSON.stringify(formComponent.localForms)
						: '',
					deliveryTemplateId:
						(await this.$STORE.messageStore.getTemplateId(
							this.$STORE.messageStore.type.sendMsg
						)) || null,
					memberTemplateId:
						(await this.$STORE.messageStore.getTemplateId(
							this.$STORE.messageStore.type.memberCartOpen
						)) || null,
					solitaireActivityId: this.form.itemDtoList[0].solitaireActivityId
						? this.form.itemDtoList[0].solitaireActivityId
						: null
				})
			)) as string

			// 轮训订单状态
			this.getOrderStatus(orderId, async () => {
				this.handleSubmitType(orderId).catch((err: string) => {
					this.$Popup.toast(err || '支付失败')
					this.setData({
						hasSubmited: false
					})
				})

				Toast.clear()
			})
		} catch (err) {
			this.setData({
				hasSubmited: false
			})
			Toast.clear()
			this.$Popup.alert({
				title: '温馨提示',
				content: err || '订单状态获取失败'
			})
		}
	}

	/** 处理付款类型 */
	/**
	 * @LastEditors: chuyinlong
	 * @description: 处理付款类型
	 * @param {string} orderId
	 * @returns {*}
	 */

	handleSubmitType(orderId: string): Promise<string> {
		switch (this.form.payType) {
			case 'WECHAT':
				return this.payment(orderId)
			default:
				return Promise.resolve('')
		}
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 支付
	 * @param {string} orderId
	 */

	payment(orderId: string): Promise<string> {
		return new Promise((reject) => {
			payment(orderId)
				.then((res) => {
					this.wecharPay(res.wxResult, orderId)
				})
				.catch((err) => {
					reject(err || '支付失败')
				})
		})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 下单成功操作
	 * @param {string} orderId
	 * @param {()=>void} cb
	 * @param {boole} succ
	 */

	orderSuccess(orderId: string, cb?: () => void, succ: boolean = true): void {
		succ && this.$Popup.toast('支付成功')
		if (cb) return cb()
		const arr = JSON.parse(
			decodeURIComponent(
				this.options.items || this.getOrSetItemStorage(true, '')
			)
		)
		return uni.redirectTo({
			url: `/subcontract/pages/orderSuccess/orderSuccess?price=${this.realPrice}&orderId=${orderId}`
		})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 微信支付
	 * @param {[x:string]:string|undefined} res
	 * @param {string} orderId
	 */

	wecharPay(res: { [x: string]: string | undefined }, orderId: string): void {
		const isCancel = (msg: string | string[]) => msg.includes('cancel')
		uni.requestPayment({
			timeStamp: res.timeStamp,
			nonceStr: res.nonceStr,
			package: res.packageValue,
			signType: res.signType,
			paySign: res.paySign,
			success: () => {
				this.orderSuccess(orderId)
			},
			fail: (e) => {
				clearInterval(this.timer)

				// 如果为取消 跳至订单详情
				if (isCancel(e.errMsg)) {
					Toast.clear()
					return uni.redirectTo({
						url: `/subcontract/pages/order/order?type=0`
					})
				}
				this.$Popup.toast('支付失败')
			}
		})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 前往选择地址
	 */

	goSelectAddress(): void {
		this.getOrsetStorage()
		uni.navigateTo({
			url: `/subcontract/pages/address/address?redirect=${encodeURIComponent(
				`/modules/pages/submit/submit`
			)}`
		})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 设置地址索引
	 */

	setAddressIndexAndId(): void {
		const defaultAddressIndex = this.miniAccountAddress.findIndex((item) => {
			return item.isDefault === 1
		})
		const addressIndex = this.miniAccountAddress.findIndex((item) => {
			return item.id === this.address.id
		})
		const index = addressIndex >= 0 ? addressIndex : defaultAddressIndex
		const addressData = this.miniAccountAddress[index]
		const id = addressData
			? this.form.deliverType === 'LOGISTICS'
				? addressData.id
				: addressData.postCode
			: ''

		this.setData(
			{
				addressIndex: index,
				[`form.miniAccountAddressId`]: id
			},
			() => {
				// 获取运费
				this.getFreightAmount({
					addressId: id ? id : ''
				})
			}
		)
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 支付方式选择
	 * @param {string} name
	 * @param {boolean} disabled
	 */

	onPayTypeClick({
		currentTarget: {
			dataset: { name, disabled }
		}
	}: {
		currentTarget: {
			dataset: { name: string; disabled: boolean }
		}
	}): void {
		if (disabled) return
		let _form = this.form
		_form.payType = name
		this.form = _form
	}
}
</script>
<style lang="scss" scoped>
@import '@/assets/styles/submit/submit.scss';
</style>
