<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-12 15:36:21
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-09-01 16:02:57
 * 123
-->
<template>
	<view>
		<view class="apply--after">
			<view class="apply--after__goods">
				<goods-item :goodsData="goods"></goods-item>
			</view>
			<view class="apply--after__form">
				<block>
					<block v-if="form.type === 'EXCHANGE'">
						<van-radio-group>
							<van-cell-group :border="false">
								<van-cell
									title="未收到货"
									:border="false"
									clickable
									data-name="未收到货"
									@click="onClick"
								>
									<van-radio
										checked-color="#fe4e63"
										name="未收到货"
										:value="form.reason"
									></van-radio>
								</van-cell>
								<van-cell
									title="货物损坏"
									:border="false"
									clickable
									data-name="货物损坏"
									@click="onClick"
								>
									<van-radio
										checked-color="#fe4e63"
										name="货物损坏"
										:value="form.reason"
									></van-radio>
								</van-cell>
							</van-cell-group>
						</van-radio-group>
						<!-- 用户换货 -->
						<van-field
							label="数量："
							:value="form.productQuantity"
							type="number"
							placeholder="最大不超过购买数量"
							:border="false"
							@change="onProductQuantityChange"
						></van-field>
					</block>
					<!-- 用户退款 -->
					<block v-else-if="form.type === 'REFUND'">
						<view class="form--refund" v-if="hasPicked">
							退款金额：
							<text class="form--refund__amount"
								>￥{{ form.refundAmount }}</text
							>
						</view>
						<view v-else class="form--refund border">
							<van-field
								:border="false"
								type="digit"
								input-class="form--refund__input"
								:value="form.refundAmount"
								label="退款金额"
								placeholder="请输入退款金额"
								@change="onrefundAmountChange"
								custom-style="padding:0"
							></van-field>
							<view class="max--refund">最多￥{{ maxRefund }}</view>
						</view>
					</block>
					<!-- 用户退货退款 -->
					<block v-else>
						<view class="form--refund border">
							<van-field
								:border="false"
								type="digit"
								input-class="form--refund__input"
								:value="form.refundAmount"
								label="退款金额"
								placeholder="请输入退款金额"
								@change="onrefundAmountChange"
								custom-style="padding:0"
							></van-field>
							<view class="max--refund">最多￥{{ maxRefund }}</view>
						</view>
						<van-field
							label="填写退货数量："
							titleWidth="110px"
							:value="form.productQuantity"
							type="number"
							placeholder="最大不超过购买数量"
							:border="false"
							@change="onProductQuantityChange"
						></van-field>
					</block>
				</block>
				<view class="apply--after__textarea">
					说明 （{{
						200 - (form.description.length || 0) > 0
							? 200 - (form.description.length || 0)
							: 0
					}}个字符）
					<van-cell-group :border="false" custom-class="van-field">
						<van-field
							:value="form.description"
							type="textarea"
							placeholder="请输入退款说明"
							autosize
							:border="false"
							maxlength="200"
							show-word-limit
							@change="onDescriptionChange"
						></van-field>
					</van-cell-group>
				</view>
				<view class="apply--after__photo">
					拍照上传凭证（最多5张）
					<view class="photo--list">
						<view class="photo--item take--photo" @tap="handleTakePhoto">
							<image
								src="https://oss-tencent.bgniao.cn/api/camera-icon.png"
								mode="aspectFill"
							></image>
							<view>上传凭证</view>
							<view>(最多{{ 5 - photos.length }}张)</view>
						</view>
						<view
							v-for="(item, index) in photos"
							:key="index"
							class="photo--item"
						>
							<image :src="item" mode="aspectFill" class="photo"></image>
							<view
								class="photo--item__del"
								@tap="handleDelPhoto"
								:data-index="index"
							>
								<image
									src="https://oss-tencent.bgniao.cn/api/close.png"
									mode="aspectFill"
								></image>
							</view>
						</view>
					</view>
				</view>
			</view>
			<view class="apply--after__submit" @tap="handleApplyAfterSale">提交</view>
		</view>
		<van-toast id="van-toast"></van-toast>
	</view>
</template>

<script module="helper" lang="wxs" src="./helper.wxs"></script>
<script lang="ts">
import { Component, Vue } from 'vue-property-decorator'
import { uploadFile } from '@/utils/upload'
import { applyAfterSale } from '@/api/modules/afterSale'
import Toast from '@/wxcomponents/vant-weapp/toast/toast'
import goodsItem from '@/components/goods/goods.vue'
import { isArray } from 'lodash'
import {
	IAfterDetailItem,
	IQueryStr
} from '../afterSaleDetail/afterSaleDetailType'
import { IAplyAfterSaleForm } from './applyAfterSaleType'
@Component({
	components: {
		goodsItem
	}
})
export default class ApplyAfterSale extends Vue {
	// 是否为退款
	isRefund: boolean = false
	// 本地图片组
	photos: Array<string> = []
	// 远程图片组
	originPhotos: Array<string> = []
	// 商品详情
	goods: IAfterDetailItem = {} as IAfterDetailItem
	// 售后表单
	form: IAplyAfterSaleForm = {
		type: '',
		orderId: '',
		productSkuId: '',
		productQuantity: 0,
		refundAmount: 0,
		reason: '未收到货'
	}
	// 最大可申请退款数
	maxRefund: number = 0

	haSubscribed: boolean = false

	/** 是否已提货 */
	hasPicked: boolean = false

	uploading: boolean = false

	onLoad(query: IQueryStr) {
		const { title, goods, userType, hasPicked, ...form } = query

		uni.setNavigationBarTitle({
			title: `申请${title}`
		})

		this.goods = JSON.parse(decodeURIComponent(goods))
		this.form = form
		this.maxRefund = form.refundAmount
		this.hasPicked = !!hasPicked
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 表单验证
	 */
	verification() {
		if (!this.form.description) {
			this.$Popup.toast('原因不可为空')
			return false
		}
		return true
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 处理申请售后
	 */

	async handleApplyAfterSale() {
		if (!this.verification()) return

		if (this.uploading) return this.$Popup.toast('图片上传中')

		const userType = 'user'
		const orderRefundNotify = this.$STORE.messageStore.type.orderRefundNotify
		const orderRefund = this.$STORE.messageStore.type.orderRefund

		if (!this.haSubscribed) {
			/** 模板订阅 */
			await this.$STORE.messageStore
				.subscribe([orderRefund, orderRefundNotify])
				.then(() => {
					this.haSubscribed = true
				})
		}
		const templateId = await this.$STORE.messageStore.getTemplateId(
			orderRefundNotify
		)
		const refundTemplateld = await this.$STORE.messageStore.getTemplateId(
			orderRefund
		)
		applyAfterSale({
			...this.form,
			images: this.originPhotos.join(','),
			templateId: templateId || null,
			refundTemplateld: refundTemplateld || null
		})
			.then((res) => {
				uni.redirectTo({
					url: `/subcontract/pages/afterSaleDetail/afterSaleDetail?afsid=${res}&userType=${userType}&goods=${encodeURIComponent(
						JSON.stringify(this.goods)
					)}`
				})
			})
			.catch((err) => {
				this.$Popup.toast(err)
			})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 选择照片
	 */

	handleTakePhoto() {
		if (this.photos.length >= 5) {
			return this.$Popup.toast('已达到上传上限')
		}

		uni.chooseImage({
			count: 5,
			sizeType: ['original', 'compressed'],
			sourceType: ['camera', 'album'],
			success: async ({ tempFilePaths }) => {
				if (isArray(tempFilePaths)) {
					tempFilePaths.forEach((url) => {
						this.uploadPhoto(url)
					})
				}
			}
		})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 上传图片
	 * @param {string} tempFilePaths
	 */

	uploadPhoto(tempFilePaths: string) {
		this.setData(
			{
				uploading: true
			},
			() =>
				Toast.loading({
					duration: 10000
				})
		)

		uploadFile(tempFilePaths)
			.then((data) => {
				this.setData(
					{
						photos: this.originPhotos.concat(data),
						originPhotos: this.originPhotos.concat(data),
						uploading: false
					},
					() => Toast.clear()
				)
			})
			.catch(() => {
				this.$Popup.toast('上传失败')
			})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 换货类型点击
	 * @param {string}name
	 */

	onClick({
		currentTarget: {
			dataset: { name }
		}
	}: {
		currentTarget: {
			dataset: {
				name: string
			}
		}
	}) {
		this.setForm('reason', name)
	}

	/** 设置描述 */
	/**
	 * @LastEditors: chuyinlong
	 * @description:设置描述
	 * @param {*}
	 */

	onDescriptionChange({ detail }: { detail: string }) {
		this.setForm('description', detail)
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 设置form值
	 * @param {string} key
	 * @param {string} value
	 */

	setForm(key: string, value: string | number) {
		this.setData({
			form: Object.assign(this.form, {
				[key]: value
			})
		})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 设置退款金额
	 * @param {*}
	 */
	onrefundAmountChange({ detail }: { detail: number }) {
		this.setForm('refundAmount', detail)
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 设置发货数量
	 * @param {*}
	 */

	onProductQuantityChange({ detail }: { detail: number }) {
		this.setForm('productQuantity', detail)
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 删除照片
	 * @param {number}index
	 */

	handleDelPhoto({
		currentTarget: {
			dataset: { index }
		}
	}: {
		currentTarget: {
			dataset: {
				index: number
			}
		}
	}) {
		const photos = this.photos
		const originPhotos = this.originPhotos
		photos.splice(index, 1)
		originPhotos.splice(index, 1)
		this.setData({
			photos,
			originPhotos
		})
	}
}
</script>
<style lang="scss" scoped>
page {
	background: #f4f4f4;
}

.apply--after {
	width: 100%;
	height: auto;
	padding: 13rpx;
	box-sizing: border-box;
}

.apply--after__goods {
	padding: 20rpx 17rpx;
	background: #fff;
	box-sizing: border-box;
	border-radius: 10rpx;
	height: auto;
	overflow: hidden;
	margin-bottom: 10px;
}

.apply--after__form {
	margin-top: 15rpx;
	padding: 20rpx 17rpx;
	background: #fff;
	box-sizing: border-box;
	border-radius: 10rpx;

	.van-radio {
		justify-content: flex-end;
	}
}

.apply--after__textarea,
.apply--after__photo {
	margin-top: 15rpx;
	padding: 20rpx 15px;
	font-size: 28rpx;
}

.van-field .van-cell {
	padding-left: 0 !important;
	padding-right: 0 !important;
}

.van-len {
	margin-top: 20rpx;
}

.photo--list {
	width: 100%;
	display: flex;
	justify-content: flex-start;
	flex-wrap: wrap;
	margin-top: 48rpx;
}

.photo--item {
	width: 177rpx;
	height: 177rpx;
	margin-right: 24rpx;
	margin-top: 24rpx;
	border: 1px dotted #979797;
	background-size: contain;
	background-repeat: no-repeat;
	position: relative;
}

.photo--item .photo {
	width: 100%;
	height: 100%;
}

.photo--item__del {
	width: 44rpx;
	height: 44rpx;
	/* border-radius: 50%; */
	/* background: rgba(0, 0, 0, 0.8); */
	position: absolute;
	top: -15rpx;
	right: -15rpx;
}

.photo--item__del image {
	width: 44rpx !important;
	height: 44rpx !important;
}

.apply--after__submit {
	width: 100%;
	height: 98rpx;
	background: #fe4e63;
	text-align: center;
	color: #fff;
	line-height: 98rpx;
	margin-top: 15rpx;
	position: fixed;
	bottom: 0;
	left: 0;
}

.form--refund {
	width: 100%;
	padding: 29rpx 46rpx;
	background: rgb(235, 235, 235);
	box-sizing: border-box;
	height: 85rpx;
	border-radius: 42.5rpx;
}

.form--refund__amount {
	color: #fb95a1;
}

.form--refund__input .van-cell {
	background: none;
}

.take--photo {
	display: flex;
	justify-content: center;
	align-items: center;
	flex-direction: column;
	font-size: 20rpx;
	color: #868686;
}

.take--photo image {
	width: 48rpx;
	height: 45rpx;
}

.form--refund__input {
	background: none;
	color: #fe4e63;
	height: 84rpx;
}

.form--refund .van-cell {
	background: none !important;
	padding: 0 !important;
}

.border {
	background: none !important;
	border: 1px solid #d3d3d3;
	position: relative;
	overflow: hidden;
	padding: 20rpx 30rpx;
}

.max--refund {
	position: absolute;
	right: 20rpx;
	top: 20rpx;
	color: #c7c7c7;
}
</style>
