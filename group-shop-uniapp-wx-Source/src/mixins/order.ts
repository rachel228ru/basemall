/*
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-12 15:36:20
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-09-01 16:00:40
 * 123
 */
import { payment, receipt, cancel } from '@/api'
import Toast from '@/wxcomponents/vant-weapp/toast/toast'
import Dialog from '@/wxcomponents/vant-weapp/dialog/dialog'
import { Component, Vue } from 'vue-property-decorator'
import { IAfs } from '@/typings/goods'
@Component({})
export default class OrderMix extends Vue {
	/**
	 * @LastEditors: chuyinlong
	 * @description: 付款
	 * @param {{id:string,type:string}} param1
	 * @param {function} callback
	 * @returns {*}
	 */

	pay({ id, type }: { id: string; type: string }, callback: () => void): void {
		payment(id, type === 'BALANCE')
			.then((res) => {
				if (type === 'BALANCE') {
					Toast('支付成功')
					if (callback) {
						callback()
					}
					return
				}
				res = res.wxResult
				uni.requestPayment({
					timeStamp: res.timeStamp,
					nonceStr: res.nonceStr,
					package: res.packageValue,
					signType: res.signType,
					paySign: res.paySign,
					success: () => {
						if (callback) {
							callback()
						}
						Toast('支付成功')
					},
					fail: (e) => {
						const isCancel = (msg: string) => msg.includes('cancel')
						if (!isCancel(e.errMsg)) {
							Toast('支付失败')
						}
					}
				})
			})
			.catch((err) => {
				Toast(err || '支付失败')
			})
	}
	/**
	 * 取消订单
	 * @param {string} id 订单id
	 */
	/**
	 * @LastEditors: chuyinlong
	 * @description: 取消订单
	 * @param {{id:string}} param1
	 * @param {function} callback
	 * @returns {*}
	 */

	cancel({ id }: { id: string }, callback: () => void): void {
		Dialog.confirm({
			title: '温馨提示',
			message: '好不容易选好，确认要取消吗？',
			overlay: true
		})
			.then(() => {
				cancel(id)
					.then(() => {
						Toast('取消成功')
						if (callback) {
							callback()
						}
					})
					.catch((err) => {
						Toast(err || '取消失败')
					})
			})
			.catch(() => {
				//
			})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description:
	 * @param {*} param1
	 * @param {function} callback
	 * @returns {*}
	 */

	receipt(
		{ id, disabled }: { id: string; disabled: boolean },
		callback: () => void
	): void {
		if (disabled) return
		this.$Popup
			.modal({
				title: '',
				content: '确认收货该订单？'
			})
			.then(() => {
				receipt(id)
					.then(() => {
						if (callback) {
							callback()
						}
						uni.navigateTo({
							url: `/subcontract/pages/orderEvaluation/orderEvaluation?id=${id}`
						})
						Toast('收货成功')
					})
					.catch((err) => {
						Toast(err || '收货失败')
					})
			})
			.catch(() => {
				//
			})
	}

	/** 前往评价 */
	/**
	 * @LastEditors: chuyinlong
	 * @description: 前往评价
	 * @param {string} id
	 * @returns {*}
	 */

	goReviews({
		target: {
			dataset: { id }
		}
	}: {
		target: {
			dataset: { id: string }
		}
	}): void {
		uni.navigateTo({
			url: `/subcontract/pages/orderEvaluation/orderEvaluation?id=${id}`
		})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 操作按钮是否不可用
	 * @param {IAfs} afs
	 * @returns {boolean}
	 */

	receiptDisabled(afs: IAfs): boolean {
		if (
			[
				'WAIT_FOR_SEND',
				'WAIT_FOR_PICKUP',
				'WAIT_RECEIPT',
				'WAIT_FOR_BUSINESS_RECEIPT'
			].indexOf(afs.status) !== -1
		) {
			return true
		}
		return false
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 判断订单状态
	 * @param {string} status
	 * @returns {boolean}
	 */

	getHasAfsSendOrder(status: string): boolean {
		return ['WAIT_FOR_SEND'].indexOf(status) !== -1
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: mixins内成功回调
	 * @param {*}
	 * @returns {*}
	 */

	callbackAction() {
		this.$emit('call')
	}
}
