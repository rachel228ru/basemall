<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-12 15:36:21
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-31 15:04:32
 * 123
-->
<template>
	<!-- pages/settle/settle.wxml -->
	<view>
		<form @submit="formSubmit" report-submit="true">
			<view class="title">
				<text class="title__text">个人资料</text>
			</view>
			<view class="input">
				<!-- <i class="iconfont icon-wuliuyunshu-xianxing-" style="font-size:22px;color:#cacaca;"></i> -->
				<m-icon
					name="icontijiaodingdan-haoyou"
					size="22px"
					color="#CACACA"
				></m-icon>
				<view class="input__item">姓名</view>
				<input
					name="name"
					placeholder="请输入你的姓名"
					style="font-size:14px;margin-left:40px"
				/>
			</view>
			<view class="input">
				<m-icon name="icondianhua" size="22px" color="#CACACA"></m-icon>
				<view class="input__item">手机号</view>
				<input
					name="mobile"
					placeholder="请输入你的手机号"
					type="number"
					maxlength="11"
					style="font-size:14px;margin-left:24px"
				/>
			</view>
			<view class="input">
				<m-icon
					name="icontijiaodingdan-dizhi"
					size="22px"
					color="#CACACA"
				></m-icon>
				<view class="input__item">地区</view>
				<input
					name="area"
					placeholder="请输入你的地址"
					style="font-size:14px;margin-left:40px"
					:value="area[0].name + ' ' + area[1].name + ' ' + area[2].name"
					disabled
					@click="areaShow = true"
				/>
			</view>
			<van-popup :show="areaShow" position="bottom">
				<van-area
					:area-list="areaList"
					title=" "
					@confirm="changeArea"
					@cancel="areaShow = false"
				/>
			</van-popup>
			<view class="input">
				<m-icon
					name="icontijiaodingdan-dizhi"
					size="22px"
					color="#CACACA"
				></m-icon>
				<view class="input__item">地址</view>
				<input
					name="address"
					placeholder="请输入你的地址"
					style="font-size:14px;margin-left:40px"
				/>
			</view>
			<view class="title">
				<text class="title__text">产品资料</text>
			</view>
			<view class="detail">
				<view class="detail__text">
					<m-icon
						name="iconbigeguanlitai-jinrifangke"
						size="28px"
						color="#CACACA"
					></m-icon>
					<view class="detail__text--msg">产品信息</view>
				</view>
				<textarea
					name="productInfo"
					placeholder="请输入产品名称及详细信息"
					maxlength="-1"
					style="font-size:14px;margin-top:30rpx;width:360px"
					:show-confirm-bar="false"
				></textarea>
			</view>
			<button form-type="submit" class="detail__button" style="font-size:14px">
				立即提交
			</button>
		</form>
	</view>
</template>
<script lang="ts">
import { Component, Vue } from 'vue-property-decorator'
import { supApply } from '@/api/modules/resource'
import mIcon from '@/components/m-icon/m-icon.vue'
import { areaList } from '@vant/area-data'
type AreaItem = Record<'code' | 'name', string>
@Component({
	components: {
		mIcon
	}
})
export default class ResourceApply extends Vue {
	haSubscribed: boolean = false
	areaList = areaList
	area: AreaItem[] = [
		{ code: '', name: '请点击选择地区' },
		{ code: '', name: '' },
		{ code: '', name: '' }
	]
	areaShow: boolean = false

	changeArea(e: { detail: { values: AreaItem[] } }) {
		this.area = e.detail.values
		this.areaShow = false
	}

	async formSubmit(e: {
		detail: {
			value: {
				area?: string
				name?: string
				mobile?: number
				productInfo?: string
			}
		}
	}) {
		await this.$STORE.messageStore
			.subscribe([this.$STORE.messageStore.type.auditMsg])
			.then(() => {
				this.setData({
					haSubscribed: true
				})
			})

		const deliveryTemplateId = await this.$STORE.messageStore.getTemplateId(
			this.$STORE.messageStore.type.auditMsg
		)
		const { name, mobile, productInfo } = e.detail.value
		if (!name || !mobile || !productInfo) {
			uni.showToast({
				title: '请完整填写表单',
				icon: 'none'
			})
			return
		}
		if (!/^1[34578]\d{9}$/.test(String(mobile))) {
			uni.showToast({
				title: '手机号码格式不正确',
				icon: 'none'
			})
			return
		}
		const area = this.area
		const param = {
			...e.detail.value,
			area: area[0].name + area[1].name + area[2].name,
			city: area[1].code,
			cityId: area[1].code,
			country: area[2].code,
			countryId: area[2].code,
			province: area[0].code,
			provinceId: area[0].code,
			templateId: deliveryTemplateId
		}
		supApply(param)
			.then(() => {
				uni.showToast({
					title: `申请成功`,
					icon: 'none'
				})
			})
			.catch((err) => {
				uni.showToast({
					title: `${err}`,
					icon: 'none'
				})
			})
		setTimeout(() => {
			uni.navigateBack({
				delta: 1
			})
		}, 1500)
	}
}
</script>
<style lang="scss" scoped>
page {
	background-color: #f4f4f4;
}

.title {
	display: flex;
	align-items: center;
	padding-left: 20px;
	height: 35px;

	&__text {
		font-size: 13px;
		color: #cacaca;
	}
}

.input {
	display: flex;
	align-items: center;
	background-color: white;
	padding-left: 20px;
	height: 55px;
	border-bottom: 1rpx solid #f1f2f6;

	&__item {
		position: relative;
		font-size: 14px;
		margin-left: 20px;
		font-weight: bold;
	}
}

.detail {
	background-color: white;
	padding-left: 20px;
	padding-top: 20px;
	height: 150px;
	margin-bottom: 25px;

	&__text {
		display: flex;
		align-items: center;

		&--msg {
			font-weight: bold;
			margin-left: 15px;
			position: relative;
			font-size: 14px;
		}
	}

	&__button {
		display: flex;
		align-items: center;
		justify-content: center;
		width: 640rpx;
		height: 88rpx;
		margin: 0 auto;
		background-color: #fc425a;
		color: #ffffff;
		border-radius: 50rpx;
		position: fixed;
		bottom: 100rpx;
		left: 60rpx;
	}
}
</style>
