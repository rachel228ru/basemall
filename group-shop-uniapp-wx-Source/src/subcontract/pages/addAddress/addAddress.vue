<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-12 15:36:21
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-09-01 16:02:25
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
						:value="addressForm.userName"
						@blur="fieldInput"
						data-input="addressForm.userName"
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
						:value="addressForm.phone"
						@input="fieldInput"
						data-input="addressForm.phone"
						placeholder="请输入收货人手机号"
						placeholder-class="placeholder"
					/>
				</view>
			</view>
			<view class="address--add__item" @tap="selectLocation">
				<text class="address--cells__title">所在地区</text>
				<view class="address--cell">
					<text class="location__province location__item">{{
						addressForm.province
					}}</text>
					<text class="location__city location__item">{{
						addressForm.city
					}}</text>
					<text class="location__county location__item">{{
						addressForm.county
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
						:value="addressForm.detailInfo"
						@input="fieldInput"
						data-input="addressForm.detailInfo"
						placeholder="街道、楼牌号等"
						placeholder-class="placeholder"
					></textarea>
				</view>
			</view>
			<button type="warn" @tap="addOrUpdateAddress">保存</button>
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
import {
	getAddressList,
	addAddress,
	getArea,
	updateAddress,
	getLatAndLon
} from '@/api/modules/address'
import {
	ApiGetAddressType,
	AddressForm,
	ProvinceListItemType
} from '../address/addressType'
@Component
export default class AddAddress extends Vue {
	addressList: Array<ApiGetAddressType> = []
	addressForm: AddressForm = {
		id: 0,
		city: '',
		county: '',
		detailInfo: '',
		location: '',
		phone: '',
		province: '',
		userName: '',
		defaultStatus: '',
		postCode: ''
	}
	areaList = {
		province_list: {} as ProvinceListItemType,
		city_list: {} as ProvinceListItemType,
		county_list: {} as ProvinceListItemType
	}
	defaultArea = ''
	showSelectLocation = false

	async onLoad(options: { addressForm: string }) {
		this.addressForm = JSON.parse(options.addressForm)
		this.getDataList()
		/** 初始化地址选择器数据（初始化省） */
		const area = await getArea({ id: '', type: 1 })
		const province_list: { [x: string]: string } = {}
		area.forEach((element: { value: string; label: string }) => {
			province_list[element.value] = element.label
		})
		this.areaList.province_list = province_list
		const provinceStrList = ['北京市', '重庆市', '上海市', '天津市']
		if (provinceStrList.find((p) => this.addressForm.province === p)) {
			this.addressForm.city = '市辖区'
		}
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 获取地址列表
	 */

	async getDataList() {
		const addressList = await getAddressList({ type: 1 })
		this.addressList = addressList
	}
	/**
	 * @LastEditors: chuyinlong
	 * @description: 点击地址选择
	 */

	async selectLocation() {
		this.showSelectLocation = true
		for (const provinceCode in this.areaList.province_list) {
			if (
				this.areaList.province_list[provinceCode] === this.addressForm.province
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
				resCity.forEach((element: { value: string | number; label: any }) => {
					city_list[element.value] = element.label
				})
				/* 获取城市代码 */
				resCity.forEach((element: { label: string; value: string }) => {
					if (element.label === this.addressForm.city) {
						cityCode = element.value
					}
				})
				/* 获取区数据 */
				const resCounty = await getArea({
					id: cityCode,
					type: 3
				})
				/* 转换区数据格式 */
				let defaultArea = ''
				resCounty.forEach((element: { value: string; label: string }) => {
					county_list[element.value] = element.label
					if (element.label === this.addressForm.county) {
						defaultArea = element.value
					}
				})
				const areaList = {
					province_list: this.areaList.province_list,
					city_list,
					county_list
				}
				this.areaList = areaList
				this.defaultArea = defaultArea
			}
		}
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
			[`${e.currentTarget.dataset.input}`]: e.detail.value
		})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 关闭省市区弹窗
	 */
	onSelectClose() {
		this.showSelectLocation = false
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description:省市区选择确定
	 * @param {*} picker
	 */

	onSelectConfirm(picker: { detail: { values: { name: string }[] } }) {
		const { values } = picker.detail
		this.addressForm.province = values[0].name
		this.addressForm.city = values[1].name
		this.addressForm.county = values[2].name
		this.showSelectLocation = false
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 省级联动
	 * @param {*} code
	 */

	async provinceChange(code: string) {
		const cityData = await getArea({
			id: code,
			type: 2
		})
		let city_list: { [x: string]: string } = {}
		cityData.forEach((element: { value: string; label: string }) => {
			city_list[element.value] = element.label
		})
		await this.cityChange(cityData[0].value, city_list)
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
		let county_list: { [x: string]: string } = {}
		countryData.forEach((element: { value: string; label: string }) => {
			county_list[element.value] = element.label
		})
		const areaList = {
			province_list: this.areaList.province_list,
			city_list,
			county_list
		}
		this.areaList = areaList
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
	 * @description: 验证表单
	 */

	beforeValuate() {
		if (!this.addressForm.userName) {
			uni.showToast({
				title: '请输入收货人姓名',
				icon: 'none'
			})
			return false
		}
		if (!(this.addressForm.phone && this.isPhone(this.addressForm.phone))) {
			uni.showToast({
				title: '手机号不正确',
				icon: 'none'
			})
			return false
		}
		return true
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 保存地址
	 */

	async addOrUpdateAddress() {
		if (!this.beforeValuate()) {
			return
		}
		const province = this.addressForm.province
		const city = this.addressForm.city
		const county = this.addressForm.county
		const provinceStrList = ['北京市', '重庆市', '上海市', '天津市']
		if (provinceStrList.find((p) => this.addressForm.province === p)) {
			this.addressForm.city = '市辖区'
		}
		if (!(province && city && county)) {
			uni.showToast({
				title: '存在地址信息为空',
				icon: 'none'
			})
			return
		}
		if (this.addressForm.id !== 0) {
			/* 更新地址 */
			await this.getLatAndLonByAddress()
			// 如果不修改直接保存，后端会报重复错误，不进行处理，认为修改成功
			try {
				await updateAddress(JSON.stringify(this.addressForm))
				uni.showToast({ title: '修改地址成功', icon: 'none' })
				uni.navigateBack({
					delta: 1
				})
			} catch (err) {
				// 后端会做重复判断
				if (err === '请勿添加相同地址') {
					uni.showToast({ title: '修改地址成功', icon: 'none' })
					uni.navigateBack({
						delta: 1
					})
				} else {
					uni.showToast({ title: err, icon: 'none' })
				}
			}
		} else {
			try {
				await this.getLatAndLonByAddress()
				await addAddress(JSON.stringify(this.addressForm))
				uni.showToast({ title: '添加地址成功', icon: 'none' })
				uni.navigateBack({
					delta: 1
				})
			} catch (err) {
				uni.showToast({ title: err, icon: 'none' })
			}
		}
	}

	/* 根据地址信息获取经纬度 */
	/**
	 * @LastEditors: chuyinlong
	 * @description: 根据地址信息获取经纬度
	 */

	async getLatAndLonByAddress() {
		const { province, city, county, detailInfo } = this.addressForm
		const site = province + city + county + detailInfo
		const latAndLon = await getLatAndLon({ site })
		this.addressForm.location = latAndLon.location
		this.addressForm.postCode = latAndLon.andlongitudeVo.adcode
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 手机号验证
	 * @param {string} value
	 */

	isPhone(value: string) {
		return !!(/^1(3|4|5|7|8)\d{9}$/.test(value) && value.match(/^\d{11}$/))
	}
}
</script>
<style lang="scss" scoped>
@import '@/assets/styles/_var.scss';

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
