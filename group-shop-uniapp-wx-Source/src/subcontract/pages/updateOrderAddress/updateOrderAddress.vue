<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-12 15:36:21
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-20 11:33:11
 * 123
-->
<template>
	<view>
		<view class="address--add">
			<view class="address--add__item border-bottom">
				<text class="address--cells__title">收货人</text>
				<view class="address--cell">
					<input
						class="address-input"
						maxlength="10"
						:value="addressForm.receiverName"
						@blur="fieldInput"
						data-input="addressForm.receiverName"
						placeholder="请输入收货人姓名"
						placeholder-class="placeholder"
					/>
				</view>
			</view>
			<view class="address--add__item border-bottom">
				<text class="address--cells__title">手机号</text>
				<view class="address--cell">
					<input
						class="address-input"
						:value="addressForm.receiverPhone"
						@input="fieldInput"
						data-input="addressForm.receiverPhone"
						placeholder="请输入收货人手机号"
						placeholder-class="placeholder"
					/>
				</view>
			</view>
			<view class="address--add__item" @tap="selectLocation">
				<text class="address--cells__title">所在地区</text>
				<view class="address--cell">
					<text class="location__province location__item">{{
						addressForm.receiverProvince
					}}</text>
					<text class="location__city location__item">{{
						addressForm.receiverCity
					}}</text>
					<text class="location__county location__item">{{
						addressForm.receiverRegion
					}}</text>
				</view>
				<van-icon class="address--cells__icon" name="location-o"></van-icon>
			</view>
			<view class="address--add__item" style="margin-top: 30rpx;">
				<text class="address--cells__title">详细地址</text>
				<view class="address--cell">
					<textarea
						class="address-textarea"
						maxlength="48"
						:value="addressForm.receiverDetailAddress"
						@input="fieldInput"
						data-input="addressForm.receiverDetailAddress"
						placeholder="街道、楼牌号等"
						placeholder-class="placeholder"
					></textarea>
				</view>
			</view>
			<button type="warn" @tap="updateAddress">保存</button>
		</view>
		<van-popup
			:show="showSelectLocation"
			position="bottom"
			@close="onSelectClose"
		>
			<van-area
				:area-list="areaList"
				@change="areaChange"
				@cancel="onSelectClose"
				@confirm="onSelectConfirm"
				:value="defaultArea"
			></van-area>
		</van-popup>
		<van-toast id="van-toast"></van-toast>
	</view>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator'
import { getAddressList, getArea } from '@/api/modules/address'
import { updateAddress } from '@/api/modules/order'
import { IAfterOrderDetailDelivery } from '../afterSaleDetail/afterSaleDetailType'
import { ApiGetAddressType, ProvinceListItemType } from '../address/addressType'

@Component({})
export default class LinkPage extends Vue {
	addressList: Array<ApiGetAddressType> = []
	addressForm: Partial<IAfterOrderDetailDelivery> = {
		orderId: '',
		receiverCity: '',
		receiverDetailAddress: '',
		receiverName: '',
		receiverPhone: '',
		receiverPostCode: '',
		receiverProvince: '',
		receiverRegion: ''
	}
	areaList = {
		province_list: {} as ProvinceListItemType,
		city_list: {} as ProvinceListItemType,
		county_list: {} as ProvinceListItemType
	}
	defaultArea: string = ''
	showSelectLocation: boolean = false

	// 页面周期函数--监听页面加载
	async onLoad(options: { form: string }) {
		this.setData({
			addressForm: JSON.parse(options.form)
		})
		this.getDataList()
		/** 初始化地址选择器数据（初始化省） */
		const area = await getArea({
			id: '',
			type: 1
		})

		const province_list: { [x: string]: string } = {}

		area.forEach((element: { value: string; label: string }) => {
			province_list[element.value] = element.label
		})

		const areaList = {
			province_list
		}
		this.setData({
			areaList
		})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 更新地址
	 */

	updateAddress() {
		updateAddress(this.addressForm)
			.then(() => {
				this.$Popup.toast('更新成功')
				uni.navigateBack({
					delta: -1
				})
			})
			.catch((err) => {
				this.$Popup.toast(err)
			})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 省市区选择确定
	 * @param {*} picker
	 */
	onSelectConfirm(picker: { detail: { values: { name: string }[] } }) {
		const { values } = picker.detail
		this.setData({
			['addressForm.receiverProvince']: values[0].name,
			['addressForm.receiverCity']: values[1].name,
			['addressForm.receiverRegion']: values[2].name,
			showSelectLocation: false
		})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 获取地址列表
	 */

	async getDataList() {
		const addressList = await getAddressList({
			type: 1
		})
		this.setData({
			addressList
		})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 省市区点击确认
	 * @param {*} picker
	 */
	async areaChange(picker: { detail: { values: any; index: number } }) {
		const { values, index } = picker.detail
		/* 设置地址选择器城市数据 */
		if (index === 0) {
			this.provinceChange(values[index].code)
		}
		/* 设置地址选择器区域数据 */
		if (index === 1) {
			this.cityChange(values[index].code, this.areaList.city_list)
		}
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 市级联动
	 * @param {string} code
	 * @param {ProvinceListItemType} city_list
	 */

	async cityChange(code: string, city_list: ProvinceListItemType) {
		/* 设置地址选择器区域数据 */
		const countryData = await getArea({
			id: code,
			type: 3
		})
		const county_list: { [x: string]: string } = {}
		countryData.forEach(
			(element: { value: string | number; label: string }) => {
				county_list[element.value] = element.label
			}
		)
		const areaList = {
			province_list: this.areaList.province_list,
			city_list,
			county_list
		}
		this.setData({
			areaList
		})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: ProvinceListItemType
	 * @param {string} code
	 * @returns {*}
	 */

	async provinceChange(code: string) {
		const cityData = await getArea({
			id: code,
			type: 2
		})
		const city_list: { [x: string]: string } = {}
		cityData.forEach((element: { value: string | number; label: string }) => {
			city_list[element.value] = element.label
		})
		await this.cityChange(cityData[0].value, city_list)
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 关闭弹窗
	 */
	onSelectClose() {
		this.setData({
			showSelectLocation: false
		})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 获取输入框输入文字
	 * @param {*} e
	 */
	fieldInput(e: {
		currentTarget: { dataset: { input: string } }
		detail: { value: string }
	}) {
		this.setData({
			[e.currentTarget.dataset.input]: e.detail.value
		})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 点击地址选择
	 */
	async selectLocation() {
		this.setData({
			showSelectLocation: true
		})
		for (const provinceCode in this.areaList.province_list) {
			if (
				this.areaList.province_list[provinceCode] ===
				this.addressForm.receiverProvince
			) {
				const city_list: { [x: string]: string } = {}
				const county_list: { [x: string]: string } = {}
				let cityCode = ''
				/* 获取城市数据 */
				const resCity = await getArea({
					id: provinceCode,
					type: 2
				})
				/* 转换城市数据格式 */
				resCity.forEach(
					(element: { value: string | number; label: string }) => {
						city_list[element.value] = element.label
					}
				)
				/* 获取城市代码 */
				resCity.forEach(
					(element: { label: string | undefined; value: string }) => {
						if (element.label === this.addressForm.receiverCity) {
							cityCode = element.value
						}
					}
				)
				/* 获取区数据 */
				const resCounty = await getArea({
					id: cityCode,
					type: 3
				})

				/* 转换区数据格式 */
				let defaultArea = ''
				resCounty.forEach((element: { value: string; label: string }) => {
					county_list[element.value] = element.label
					if (element.label === this.addressForm.receiverRegion) {
						defaultArea = element.value
					}
				})
				const areaList = {
					province_list: this.areaList.province_list,
					city_list,
					county_list
				}
				this.setData({
					areaList,
					defaultArea
				})
			}
		}
	}
}
</script>

<style lang="scss" scoped>
@import '@/assets/styles/var';

page {
	height: 100%;
}

.address--add {
	background-color: #f5f5f5;
	height: 100%;

	.address--add__item {
		position: relative;
		display: flex;
		width: 100%;
		font-size: 28rpx;
		line-height: 48rpx;
		color: #333;
		background-color: #fff;
		box-sizing: border-box;
		padding: 20rpx 32rpx;

		.address--cells__title {
			max-width: 90px;
			min-width: 90px;
		}

		.address--cells__icon {
			position: absolute;
			right: 50rpx;
			top: 25rpx;
		}
	}

	.icon {
		position: absolute;
		top: 0rpx;
		right: 0rpx;
	}

	button {
		padding: 30rpx;
		height: 102rpx;
		border-radius: 45rpx;
		margin: 350rpx 50rpx 0 50rpx;
		background-color: #fc425a;
	}

	.border-bottom {
		border-bottom: 1rpx solid #f8f5f9;
	}

	.placeholder {
		color: #cacaca;
	}
}

.address-textarea {
	height: 150rpx;
	width: 500rpx;
}

.van-picker-column__item {
	color: #888888;
}
</style>
