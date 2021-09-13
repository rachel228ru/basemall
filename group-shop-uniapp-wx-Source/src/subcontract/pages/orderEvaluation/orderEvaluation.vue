<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-12 15:36:21
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-30 14:28:59
 * 123
-->
<template>
	<view class="evaluation">
		<!-- detail.orderItemList -->
		<block v-for="(item, index) in detail.orderItemList" :key="index">
			<view class="evaluation__goods">
				<goods :goodsData="item"></goods>
			</view>
			<view class="evaluation__textarea">
				<van-field
					:autosize="{ minHeight: 100 }"
					custom-style="padding-top:2px;"
					maxlength="200"
					@change="onFieldChange"
					data-key="comment"
					:data-index="index"
					type="textarea"
					placeholder="已经使用一段时间了，有更多宝贝心得？分享给买得他们吧"
					:border="false"
				></van-field>
			</view>
			<view class="evaluation__images">
				<view
					class="image--add"
					@tap="handleTakePhoto"
					data-key="pictures"
					:data-index="index"
				>
					<image
						src="http://medusa-small-file.oss-cn-hangzhou.aliyuncs.com/images/icon-c.png"
					></image>
					添加图片
				</view>
				<view
					v-for="(ite, imageIndex) in form.productEvaluateDtoList[index]
						.pictures"
					:key="imageIndex"
					class="image--item"
				>
					<view
						class="del"
						@tap="handleDelPhoto"
						data-key="pictures"
						:data-index="index"
						:data-imageIndex="imageIndex"
					>
						<i class="iconfont icontijiaodingdan-youhuiquan-guanbi"></i>
					</view>
					<image
						:src="ite"
						@tap="handleSeePhoto"
						:data-item="item"
						:data-ing="ite"
					></image>
				</view>
			</view>
			<view class="evaluation__rate">
				<van-cell
					title="综合评分"
					value="满意请给5分哦"
					:border="false"
				></van-cell>
				<van-cell title="商品质量">
					<van-rate
						slot="right-icon"
						@change="onFieldChange"
						data-key="rate"
						:data-index="index"
					></van-rate>
				</van-cell>
			</view>
		</block>
		<div class="evaluation__button" @tap="submit">提交评论</div>
	</view>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator'
import { evaluate, getOrderDetail } from '@/api/modules/order'
import { uploadFile } from '@/utils/upload'
import { isArray } from 'lodash'
import { IGoodsList } from '@/typings/goods'
import { IAfterOrderDetail } from '../afterSaleDetail/afterSaleDetailType'

@Component({})
export default class OrderEvaluation extends Vue {
	message: string = ''
	detail: IAfterOrderDetail = {} as IAfterOrderDetail
	// 评价表单
	form = {
		orderId: '',
		productEvaluateDtoList: [] as Array<{
			comment: string
			picture: string
			// 本地维护的图片数组
			pictures: string[]
			// 原始图片
			originPhotos: string[]
			productSkuId: string
			rate: number
		}>,
		shopRate: 0
	}
	// 是否已提交
	submitted: boolean = false

	get appMode() {
		return this.$STORE.setStore.shopSetting.appMode
	}

	onLoad({ id = '1324995623470723073' }) {
		this.getOrderDetail(id)
		this.$Pubsub.on('app-launch', () => {
			this.getOrderDetail(id)
		})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 获取订单详情
	 * @param {string} id
	 */
	getOrderDetail(id: string) {
		getOrderDetail(id)
			.then((detail: IAfterOrderDetail) => {
				const orderItemList: IGoodsList[] = detail.orderItemList.filter(
					(item: IGoodsList) => {
						return !item.afs || item.afs.status === 'CLOSE'
					}
				)
				this.setData({
					detail: { ...detail, orderItemList },
					'form.productEvaluateDtoList': this.setProductEvaluateDtoList(
						orderItemList
					)
				})
			})
			.catch((err) => {
				this.$Popup.toast(err || '详情获取失败')
			})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 设置评分队列
	 * @param {IGoodsList[]} list
	 * @returns {*}
	 */

	setProductEvaluateDtoList(
		list: IGoodsList[]
	): {
		comment: string
		picture: string
		pictures: string[]
		productSkuId: string
		rate: number
	}[] {
		return list.map((item: IGoodsList) => {
			return {
				comment: '',
				picture: '',
				// 本地维护的图片数组
				pictures: [],
				productSkuId: item.productSkuId,
				rate: 0
			}
		})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 监听表单输入并设置对应值
	 * @param {*} e
	 */

	onFieldChange({
		detail,
		target: {
			dataset: { key, index }
		}
	}: {
		detail: number | string
		target: {
			dataset: {
				key: string
				index: number
			}
		}
	}) {
		if (key === 'comment') {
			this.form.productEvaluateDtoList[index].comment = String(detail)
			return
		}
		this.form.productEvaluateDtoList[index].rate = Number(detail)
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 删除图片
	 * @param {*} e
	 */

	handleDelPhoto({
		currentTarget: {
			dataset: { index, key, imageIndex }
		}
	}: {
		currentTarget: {
			dataset: {
				index: number
				key: string
				imageIndex: number
			}
		}
	}) {
		const pictures = this.form.productEvaluateDtoList[index].pictures
		pictures.splice(imageIndex, 1)
		this.setData({
			[`form.productEvaluateDtoList[${index}].${key}`]: pictures
		})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 查看图片大图
	 * @param {*} e
	 */
	handleSeePhoto({
		currentTarget: {
			dataset: { item, img }
		}
	}: {
		currentTarget: {
			dataset: {
				item: string[]
				img: string
			}
		}
	}) {
		uni.previewImage({
			current: img,
			urls: item
		})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 选取照片
	 * @param {*} e
	 */

	handleTakePhoto({
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
		const pictures = this.form.productEvaluateDtoList[index].pictures
		if (pictures.length >= 5) {
			return uni.showToast({
				title: '已达到上传上限',
				icon: 'none'
			})
		}
		uni.chooseImage({
			count: 5,
			sizeType: ['original', 'compressed'],
			sourceType: ['camera'],
			success: async ({ tempFilePaths }) => {
				if (isArray(tempFilePaths)) {
					tempFilePaths.forEach((url) => {
						uploadFile(url)
							.then((res) => {
								this.form.productEvaluateDtoList[index].pictures.push(res)
							})
							.catch((err) => {
								uni.showToast({
									title: err || '上传失败',
									icon: 'none'
								})
							})
					})
				}
			},
			fail: (err) => {
				uni.showToast({
					title: err || '详情获取失败',
					icon: 'none'
				})
			}
		})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 将图片转为字符串
	 */

	tranfromPics() {
		const productEvaluateDtoList = this.form.productEvaluateDtoList.map(
			(item: { picture: any; pictures: any[] }) => {
				item.picture = item.pictures.join(',')
				return item
			}
		)
		this.setData({
			'form.productEvaluateDtoList': productEvaluateDtoList
		})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 验证表单
	 */

	verification() {
		const { productEvaluateDtoList } = this.form

		const evLen = productEvaluateDtoList.every((item: { rate: number }) => {
			item.rate >= 1
		})

		if (evLen) {
			this.$Popup.toast('商品评分不可为空')
			return false
		}
		return true
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 提交评价
	 */

	submit() {
		if (!this.verification() || this.submitted) return

		this.setData({
			submitted: true
		})
		this.tranfromPics()

		evaluate(
			Object.assign({}, this.form, {
				orderId: this.detail.id
			})
		)
			.then(() => {
				uni.showToast({
					title: '评价成功',
					icon: 'success'
				})
				setTimeout(() => {
					uni.redirectTo({
						url: '/subcontract/pages/order/order?type=4'
					})
				}, 1500)
			})
			.catch((err) => {
				this.setData({
					submitted: false
				})
				uni.showToast({
					title: err || '详情获取失败',
					icon: 'none'
				})
			})
	}
}
</script>

<style lang="scss" scoped>
@import '@/assets/styles/var';

.evaluation {
	background: $main-background;
	min-height: 100vh;
	padding-bottom: 100px;

	@mixin p($v: 10px, $l: 10px) {
		padding: 10px 10px;
	}

	&__images {
		@include flex(flex-start);
		@include p;

		width: 100%;
		background: #fff;
		flex-wrap: wrap;

		view:nth-child(4n) {
			margin-right: 0;
		}

		.image--add {
			@include flex;

			flex-direction: column;
			width: 80px;
			height: 80px;
			border: 1px dashed $main-border-color;
			font-size: 12px;
			color: #ccc;
			margin-right: 10px;
			margin-bottom: 10px;

			image {
				width: 20px;
				height: 20px;
			}
		}

		.image--item {
			width: 80px;
			height: 80px;
			margin-right: 10px;
			margin-bottom: 10px;
			position: relative;

			.del {
				$s: 15px;
				width: $s;
				height: $s;
				position: absolute;
				right: -$s/2;
				top: -$s/2;
				border-radius: 50%;
			}

			image {
				width: 100%;
				height: 100%;
			}
		}
	}

	&__textarea {
		min-height: 100px;
		background: #fff;

		.van-field__body--textarea {
			min-height: 48px;
		}

		.van-field__input {
			padding-top: 10px;
			min-height: 240rpx !important;
		}
	}

	&__goods {
		padding: 10px 10px;
		border-bottom: 1px solid $main-border-color;
		background: #fff;
	}

	&__rate {
		margin-bottom: 10px;
	}

	&__button {
		position: fixed;
		bottom: 0;
		left: 0;
		width: 100%;
		padding: 10px 15px;
		text-align: center;
		background: $main-light-color;
		color: #fff;
	}
}
</style>
