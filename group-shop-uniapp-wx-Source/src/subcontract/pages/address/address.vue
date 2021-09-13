<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-12 15:36:21
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-09-01 16:06:25
 * 123
-->
<template>
	<view class="address">
		<view v-if="addressList.length === 0" class="address--backGround">
			<m-icon name="icondizhi" class="icon" size="300rpx"></m-icon>
			<view>暂时没有地址</view>
		</view>
		<view class="address--info">
			<view
				v-for="(item, index) in addressList"
				:key="index"
				class="address--info__item"
			>
				<view class="location" @tap="chooseBack" :data-item="item">
					<view class="location--user">
						<text class="location--user__userName">{{ item.userName }}</text>
						<text class="location--user__phone">{{ item.phone }}</text>
					</view>
					<view class="location--info">
						<text class="location--info__default" v-if="item.isDefault === 1"
							>默认</text
						>
						<text class="location--info__province location--info__text">{{
							item.province
						}}</text>
						<text class="location--info__city location--info__text">{{
							item.city
						}}</text>
						<text class="location--info__county location--info__text">{{
							item.county
						}}</text>
						<text class="location--info__detailInfo location--info__text">{{
							item.detailInfo
						}}</text>
					</view>
				</view>
				<view class="address--item__edit">
					<van-checkbox
						:value="item.isDefault === 1"
						checked-color="black"
						@change="onCheckBoxChange"
						:data-item="item"
					>
						<text>设为默认</text>
					</van-checkbox>
					<view class="item__edit">
						<view
							class="edit--item edit"
							@tap="handleItemClick"
							:data-address="item"
						>
							<m-icon name="iconbianji icon" class="icon"></m-icon>
							<text>编辑</text>
						</view>
						<view
							class="edit--item delete"
							@tap="deleteAddress"
							:data-id="item.id"
						>
							<m-icon name="icondelete" class="icon"></m-icon>
							<text>删除</text>
						</view>
					</view>
				</view>
			</view>
		</view>
		<view class="address--buttons">
			<block>
				<view
					class="button--item warn"
					v-if="
						authLocationStatus === '首次授权' || authLocationStatus === '已授权'
					"
					@tap="addManualLocation"
				>
					<m-icon name="icongouwuche-jia" class="icon"></m-icon>
					<span>手动添加</span>
				</view>
				<button
					class="button--item warn"
					v-else
					open-type="openSetting"
					@opensetting="addManualOpenLocation"
				>
					<m-icon name="icongouwuche-jia" class="icon"></m-icon>
					<span>手动添加</span>
				</button>
				<button
					class="button--item primary"
					v-if="
						authAddressStatus === '首次授权' || authAddressStatus === '已授权'
					"
					@tap="addWxLocation"
				>
					<m-icon name="icongouwuche-jia" class="icon"></m-icon>
					<span>微信添加</span>
				</button>
				<button
					class="button--item primary"
					v-else
					open-type="openSetting"
					@opensetting="addWxOpenLocation"
				>
					<m-icon name="icongouwuche-jia" class="icon"></m-icon>
					<span>微信添加</span>
				</button>
			</block>
		</view>
		<van-dialog id="van-dialog"></van-dialog>
		<van-toast id="van-toast"></van-toast>
	</view>
</template>
<script lang="ts">
import { Component, Vue } from 'vue-property-decorator'
import {
	AddressState,
	AddressForm,
	ApiGetAddressType,
	ProvinceListItemType
} from '../address/addressType'
import mIcon from '@/components/m-icon/m-icon.vue'
import Dialog from '@/wxcomponents/vant-weapp/dialog/dialog.js'
import Toast from '@/wxcomponents/vant-weapp/toast/toast.js'
import {
	getAddressList,
	addAddress,
	getArea,
	deleteAddress,
	updateAddress,
	getLatAndLon,
	analysisLatAndLon
} from '@/api/modules/address'
@Component({
	components: {
		mIcon
	}
})
export default class Address extends Vue implements AddressState {
	// 地址列表
	addressList: Array<ApiGetAddressType> = []
	addressForm = {
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
	} as AddressForm
	areaList = {
		province_list: {} as ProvinceListItemType
	}
	authAddressStatus = '首次授权'
	defaultArea = ''
	authLocationStatus = '首次授权'
	redirect = ''

	async onLoad({ redirect }: { redirect: string }) {
		this.redirect = redirect
		this.getDataList()
		/** 初始化地址选择器数据（初始化省） */
		const area = await getArea({
			id: '',
			type: 1
		})
		const province_list: { [x: string]: string } = {}
		area.forEach((element: { value: string | number; label: any }) => {
			province_list[element.value] = element.label
		})
		this.areaList.province_list = province_list
		this.getPermission()
	}

	onShow() {
		this.getDataList()
	}

	/**
	 * 选择地址后返回
	 */
	/**
	 * @LastEditors: chuyinlong
	 * @description:选择地址后返回
	 * @param {*} e
	 */

	chooseBack(e: { currentTarget: { dataset: { item: ApiGetAddressType } } }) {
		const addressOption = e.currentTarget.dataset.item
		if (this.redirect) {
			const pages = getCurrentPages() // eslint-disable-line
			const prevPage = pages[pages.length - 2] as any
			;(prevPage.$vm.address = addressOption),
				uni.navigateBack({
					delta: 1
				})
		}
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 获取地址列表
	 */

	async getDataList() {
		const addressList = await getAddressList({
			type: 1
		})
		this.addressList = addressList
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 是否设为默认
	 * @param {*} e
	 */
	async onCheckBoxChange(e: {
		currentTarget: { dataset: { item: ApiGetAddressType } }
		detail: boolean
	}) {
		const address = e.currentTarget.dataset.item
		const addressForm = this.addressForm
		const addressForEachList = this.addressList
		addressForm.id = address.id
		addressForm.defaultStatus = e.detail ? 1 : 0
		addressForm.phone = address.phone
		addressForm.location = address.location
		addressForm.city = address.city
		addressForm.county = address.county
		addressForm.detailInfo = address.detailInfo
		addressForm.userName = address.userName
		addressForm.province = address.province
		addressForm.postCode = address.postCode
		addressForEachList.forEach((item) => {
			if (item.id === addressForm.id) {
				item.defaultStatus = e.detail ? 1 : 0
			}
		})
		this.addressForm = addressForm
		this.addressList = addressForEachList
		this.addOrUpdateAddress('直接修改')
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 手动添加(首次授权/已授权)
	 */

	async addManualLocation() {
		const locationEnabled = this.verifyLocationEnabled()
		if (!locationEnabled) {
			return
		}
		uni.showLoading({
			title: '',
			mask: true
		})
		this.addressForm.id = 0
		uni.getLocation({
			type: 'wgs84',
			success: async (res) => {
				const latitude = res.latitude
				const longitude = res.longitude
				const site = `${longitude}, ${latitude}`
				await this.setAddressByLatAndLon(site)
				const initAddressForm = {
					id: 0,
					city: this.addressForm.city,
					county: this.addressForm.county,
					detailInfo: '',
					location: '',
					phone: '',
					province: this.addressForm.province,
					userName: '',
					defaultStatus: '',
					postCode: ''
				}
				this.addressForm = initAddressForm
				uni.hideLoading()
				uni.navigateTo({
					url:
						'/subcontract/pages/addAddress/addAddress?addressForm=' +
						JSON.stringify(this.addressForm)
				})
			},
			fail: () => {
				uni.hideLoading()
				this.authLocationStatus = '未授权'
			}
		})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 手动添加(未授权)
	 */

	async addManualOpenLocation() {
		const locationEnabled = this.verifyLocationEnabled()
		if (!locationEnabled) {
			return
		}
		uni.showLoading({
			title: '',
			mask: true
		})
		this.authLocationStatus = '已授权'
		uni.getLocation({
			type: 'wgs84',
			success: async (res) => {
				const latitude = res.latitude
				const longitude = res.longitude
				const site = `${longitude}, ${latitude}`
				await this.setAddressByLatAndLon(site)
				const initAddressForm = {
					id: 0,
					city: this.addressForm.city,
					county: this.addressForm.county,
					detailInfo: '',
					location: '',
					phone: '',
					province: this.addressForm.province,
					userName: '',
					defaultStatus: '',
					postCode: ''
				}
				this.addressForm = initAddressForm
				uni.hideLoading()
				uni.navigateTo({
					url:
						'/subcontract/pages/addAddress/addAddress?addressForm=' +
						JSON.stringify(this.addressForm)
				})
			},
			fail: () => {
				uni.hideLoading()
				this.authLocationStatus = '未授权'
			}
		})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 根据经纬度设置省市区
	 * @param {*} site
	 */

	async setAddressByLatAndLon(site: string) {
		const latAndLon = await analysisLatAndLon({
			site
		})
		/** 省市区赋值 */
		this.addressForm.province = latAndLon.province.label
		this.addressForm.city = latAndLon.city ? latAndLon.city.label : ''
		this.addressForm.county = latAndLon.district ? latAndLon.district.label : ''
		this.addressForm.postCode = latAndLon.adcode ? latAndLon.adcode : ''
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 微信添加
	 */

	async addWxLocation() {
		const locationEnabled = this.verifyLocationEnabled()
		if (!locationEnabled) {
			return
		}
		if (this.authAddressStatus === '首次授权') {
			uni.chooseAddress({
				success: (res) => {
					this.addressForm.id = 0
					this.addressForm.phone = res.telNumber
					this.addressForm.city = res.cityName
					this.addressForm.county = res.countyName
					this.addressForm.detailInfo = res.detailInfo
					this.addressForm.userName = res.userName
					this.addressForm.province = res.provinceName
					this.authAddressStatus = '已授权'
					this.addOrUpdateAddress()
				},
				fail: () => {
					this.authAddressStatus = '未授权'
				}
			})
		}
		if (this.authAddressStatus === '已授权') {
			uni.chooseAddress({
				success: (res) => {
					this.addressForm.id = 0
					this.addressForm.phone = res.telNumber
					this.addressForm.city = res.cityName
					this.addressForm.county = res.countyName
					this.addressForm.detailInfo = res.detailInfo
					this.addressForm.userName = res.userName
					this.addressForm.province = res.provinceName
					this.addOrUpdateAddress()
				}
			})
		}
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 微信添加
	 */
	async addWxOpenLocation() {
		const locationEnabled = this.verifyLocationEnabled()
		if (!locationEnabled) {
			return
		}
		this.authAddressStatus = '已授权'
		uni.chooseAddress({
			success: (res) => {
				this.addressForm.id = 0
				this.addressForm.phone = res.telNumber
				this.addressForm.city = res.cityName
				this.addressForm.county = res.countyName
				this.addressForm.detailInfo = res.detailInfo
				this.addressForm.userName = res.userName
				this.addressForm.province = res.provinceName
				this.addOrUpdateAddress()
			},
			fail: () => {
				this.authAddressStatus = '未授权'
			}
		})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description:
	 * @param {string} type
	 * @returns {*}
	 */

	async addOrUpdateAddress(type?: string) {
		if (!this.addressForm.userName) {
			uni.showToast({
				title: '请输入收货人姓名',
				icon: 'none'
			})
			return
		}
		if (!(this.addressForm.phone && this.isPhone(this.addressForm.phone))) {
			uni.showToast({
				title: '手机号不正确',
				icon: 'none'
			})
			return
		}
		if (
			!(
				this.addressForm.province &&
				this.addressForm.city &&
				this.addressForm.county
			)
		) {
			uni.showToast({
				title: '存在空的地址信息',
				icon: 'none'
			})
			return
		}
		if (this.addressForm.id !== 0) {
			// 如果不修改直接保存，后端会报重复错误，不进行处理，认为修改成功
			try {
				/* 更新地址 */
				if (type !== '直接修改') {
					await this.getLatAndLonByAddress()
				}
				await updateAddress(JSON.stringify(this.addressForm))
				uni.showToast({
					title: '操作成功',
					icon: 'none'
				})
				this.getDataList()
			} catch (err) {
				// 后端会做重复判断
				if (err === '请勿添加相同地址') {
					uni.showToast({
						title: '修改地址成功',
						icon: 'none'
					})
				} else {
					uni.showToast({
						title: err,
						icon: 'none'
					})
				}
			}
		} else {
			try {
				await this.getLatAndLonByAddress()
				await addAddress(JSON.stringify(this.addressForm))
				uni.showToast({
					title: '添加地址成功',
					icon: 'none'
				})
				this.getDataList()
			} catch (err) {
				uni.showToast({
					title: err,
					icon: 'none'
				})
			}
		}
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 根据地址信息获取经纬度
	 */

	async getLatAndLonByAddress() {
		const { province, city, county, detailInfo } = this.addressForm
		const site = province + city + county + detailInfo
		const latAndLon = await getLatAndLon({
			site
		})
		this.addressForm.location = latAndLon.location
		this.addressForm.postCode = latAndLon.andlongitudeVo.adcode
	}
	// 删除地址

	/**
	 * @LastEditors: chuyinlong
	 * @description: 删除地址
	 * @param {*} e
	 */

	deleteAddress(e: { currentTarget: { dataset: { id: string | number } } }) {
		Dialog.confirm({
			message: '确定删除该地址',
			className: 'wxDialog'
		})
			.then(async () => {
				await deleteAddress(e.currentTarget.dataset.id)
				Toast.success('删除成功')
				this.getDataList()
			})
			.catch(() => {
				this.getDataList()
			})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 获取授权信息
	 */
	async getPermission() {
		let that = this
		// 确认授权
		uni.getSetting({
			success(res) {
				const authSetting = res.authSetting
				if (authSetting['scope.userLocation'] === undefined) {
					that.authLocationStatus = '首次授权'
				} else if (authSetting['scope.userLocation']) {
					that.authLocationStatus = '已授权'
				} else {
					that.authLocationStatus = '未授权'
				}
				if (authSetting['scope.address'] === undefined) {
					that.authAddressStatus = '首次授权'
				} else if (authSetting['scope.address']) {
					that.authAddressStatus = '已授权'
				} else {
					that.authAddressStatus = '未授权'
				}
			}
		})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 验证手机号
	 * @param {string} value
	 * @returns {boolean}
	 */
	isPhone(value: string): boolean {
		if (/^1[0-9]\d{9}$/.test(value) && value.match(/^\d{11}$/)) {
			return true
		} else {
			return false
		}
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 编辑地址信息
	 * @param {*} e
	 */
	handleItemClick(e: { currentTarget: { dataset: { address: any } } }) {
		const address = e.currentTarget.dataset.address
		this.addressForm.id = address.id
		this.addressForm.defaultStatus = address.isDefault
		this.addressForm.phone = address.phone
		this.addressForm.city = address.city
		this.addressForm.county = address.county
		this.addressForm.detailInfo = address.detailInfo
		this.addressForm.userName = address.userName
		this.addressForm.province = address.province
		this.addressForm.postCode = address.postCode
		uni.navigateTo({
			url:
				'/subcontract/pages/addAddress/addAddress?addressForm=' +
				JSON.stringify(this.addressForm)
		})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 判断手机是否开启定位服务
	 */

	verifyLocationEnabled() {
		const { locationAuthorized } = uni.getSystemInfoSync()
		if (!locationAuthorized) {
			Dialog.alert({
				message:
					'无法获取你的位置信息。请到手机系统的[设置]->[隐私]->[定位服务]中打开定位，并允许微信使用定位服务。'
			}).then(() => {
				return false
			})
		} else {
			return true
		}
	}
}
</script>
<style lang="scss" scoped>
@import '@/assets/styles/_var.scss';

page {
	height: 100%;
}

.address {
	@mixin b-padding {
		padding: 40rpx;
	}

	background: #f5f5f5;
	min-height: 100%;

	.address--backGround {
		position: absolute;
		width: 100%;
		height: 100%;
		@include flex;
		flex-direction: column;
		color: #cacaca;
	}

	.address--info {
		color: #5c5c5c;
		overflow: scroll;
		height: 100%;
		margin-bottom: 123rpx;

		.address--info__item {
			margin: 0 20rpx 20rpx 20rpx;
			background-color: #fff;
			font-size: 28rpx;

			&:first-child {
				margin-top: 20rpx;
			}

			.address--item__edit {
				@include flex(space-between, center);
				background-color: #fff;
				padding: 0rpx 20rpx 20rpx 20rpx;

				.icon,
				.edit {
					padding-right: 12rpx;
				}

				.edit--item {
					@include flex();
				}

				.item__edit {
					@include flex(space-between, center);
				}
			}
		}
	}

	.address--buttons {
		@include flex;
		position: fixed;
		bottom: 0;
		width: 100%;
		padding: 30rpx 0rpx;
		background: #fff;

		.button--item {
			@include flex;
			width: 320rpx;
			height: 64rpx;
			border-radius: 32rpx;
			text-align: center;

			span {
				padding-left: 10rpx;
			}

			&.warn {
				background: #e64340;
				color: #fff;
			}

			&.primary {
				background: #1aad19;
				margin-left: 20rpx;
				color: #fff;
			}
		}
	}

	.location {
		color: #333;
		padding: 20rpx;

		.location--user {
			@include flex(space-between, center);
			padding: 15rpx 0rpx;
			font-size: 32rpx;
		}

		.location--info {
			font-size: 24rpx;
			padding: 20rpx 0rpx;
			border-bottom: 1rpx solid #f8f5f9;

			.location--info__default {
				box-sizing: border-box;
				font-size: 22rpx;
				padding: 1rpx 10rpx;
				color: #fff;
				font-weight: 300;
				background-color: red;
				border-radius: 20rpx;
			}

			.location--info__text {
				padding: 0 5rpx;
				color: #909399;
			}
		}
	}
}
</style>
