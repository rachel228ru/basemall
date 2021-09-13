<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-12 15:36:21
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-19 10:01:07
 * 123
-->
<template>
	<view class="return">
		<view class="return__goods">
			<goods :goodsData="goods"></goods>
		</view>
		<view class="return__dr">
			<van-cell-group>
				<van-cell
					title="物流公司"
					required
					:value="form.deliveryCompany || '请选择'"
					is-link
					@tap="toggleSheetVisible"
				></van-cell>
				<van-field
					:value="form.deliverySn"
					@change="setForm"
					data-key="deliverySn"
					label="物流单号"
					placeholder="请输入物流单号"
					type="text"
					maxlength="18"
					required
					:border="false"
				></van-field>
			</van-cell-group>
		</view>
		<view class="return__dr">
			<van-cell-group>
				<van-field
					:value="form.phone"
					@change="setForm"
					data-key="phone"
					label="联系电话"
					placeholder="请输入联系电话"
					type="number"
					maxlength="11"
					required
					:border="false"
				></van-field>
				<van-field
					:value="form.reason"
					@change="setForm"
					data-key="reason"
					label="退货说明"
					placeholder="请输入退货说明"
					required
					:border="false"
				></van-field>
			</van-cell-group>
		</view>
		<view class="return__submit" @tap="handleReturn">提交</view>
		<van-action-sheet
			title="选择物流"
			:show="sheetVisible"
			:actions="actions"
			@close="onClose"
			@select="onSelect"
		></van-action-sheet>
	</view>
</template>
<script lang="ts">
import { Component, Vue } from 'vue-property-decorator'
interface interForm {
	deliveryCompany: string
	deliverySn: string
	deliveryCode: string
	phone: string
	reason: string
}
import { returnOfGoods, getLogisticsCompany } from '@/api/modules/afterSale'
import goods from '@/components/goods/goods.vue'
interface IActions {
	name: string
	id: number | string
}
@Component({
	components: {
		goods
	}
})
export default class AfterSaleReturn extends Vue {
	goods: object = {}
	form: interForm = {
		deliveryCompany: '',
		deliverySn: '',
		deliveryCode: '',
		phone: '',
		reason: ''
	}
	actions: Array<IActions> = []
	sheetVisible: boolean = false
	afsId: string = ''

	onLoad({ goods, afsId }: { goods: string; afsId: string }) {
		this.goods = JSON.parse(goods)
		this.afsId = afsId
		getLogisticsCompany({})
			.then((res) => {
				const list: IActions[] = res.logisticsCompanyVos.map(
					(item: { name: string; logisticsCompanyId: string | number }) => {
						return {
							name: item.name,
							id: item.logisticsCompanyId
						}
					}
				)
				this.actions = list
			})
			.catch(() => {
				this.$Popup.toast('获取失败')
			})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 将数据设置到表单
	 */

	setForm({
		detail,
		currentTarget: {
			dataset: { key }
		}
	}: {
		detail: string
		currentTarget: {
			dataset: { key: string }
		}
	}) {
		this.form = Object.assign(this.form, { [key]: detail })
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 提交表单
	 */

	handleReturn() {
		if (!this.$global.isPhone(this.form.phone)) {
			return this.$Popup.toast('请填写正确手机号')
		}
		if (!this.form.deliveryCompany) {
			return this.$Popup.toast('请选择物流公司')
		}
		if (!this.form.deliverySn) {
			return this.$Popup.toast('请填写物流单号')
		}
		if (!this.form.reason) {
			return this.$Popup.toast('请填写退货说明')
		}
		returnOfGoods({ afsId: this.afsId, ...this.form })
			.then(() => {
				uni.navigateBack({ delta: -1 })
				this.$Popup.toast('退货成功')
			})
			.catch(() => {
				this.$Popup.toast('退货失败')
			})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 关闭物流弹窗
	 */
	onClose() {
		this.toggleSheetVisible()
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 物流弹窗选择物流
	 * @param {string} detail
	 * @param {string} id
	 */
	onSelect({ detail: { name, id } }: { detail: { name: string; id: string } }) {
		this.form.deliveryCompany = name
		this.form.deliveryCode = id
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 开启物流弹窗
	 */
	toggleSheetVisible() {
		this.sheetVisible = !this.sheetVisible
	}
}
</script>
<style lang="scss" scoped>
@include b(return) {
	width: 100vw;
	height: 100vh;
	background: #f4f4f4;

	@include e(goods) {
		background: #fff;
		margin-bottom: 10px;
		padding: 5px;
	}

	@include e(dr) {
		background: #fff;
		margin-bottom: 10px;
	}

	@include e(submit) {
		width: 100%;
		padding: 15px 10px;
		background: $main-light-color;
		position: fixed;
		bottom: 0;
		left: 0;
		text-align: center;
		color: #fff;
		font-size: 16px;
	}
}
</style>
