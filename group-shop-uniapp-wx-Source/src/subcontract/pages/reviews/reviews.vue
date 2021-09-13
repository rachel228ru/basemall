<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-12 15:36:21
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-30 14:29:15
 * 123
-->
<template>
	<view>
		<view class="assess__list">
			<view
				class="assess__list--button"
				:class="{ active: helper.isCurrent(0, query.type) }"
				@tap="onTypeClick"
				data-type="0"
			>
				全部
				<view class="assess__list--button--num"
					>({{ evaluateOverview.all || 0 }})</view
				>
			</view>
			<view
				class="assess__list--button"
				:class="{ active: helper.isCurrent(2, query.type) }"
				@tap="onTypeClick"
				data-type="2"
			>
				有内容
				<view class="assess__list--button--num"
					>({{ evaluateOverview.hasContent || 0 }})</view
				>
			</view>
			<view
				class="assess__list--button"
				:class="{ active: helper.isCurrent(1, query.type) }"
				@tap="onTypeClick"
				data-type="1"
			>
				有图片
				<view class="assess__list--button--num"
					>({{ evaluateOverview.hasPicture || 0 }})</view
				>
			</view>
		</view>
		<block v-if="evaluateList.length > 0">
			<view v-for="(item, index) in evaluateList" :key="index" class="review">
				<view>
					<view style="display:flex">
						<image :src="item.userAvatarUrl" class="review__img "></image>
						<view>
							<view class="review__content__rank">
								{{ item.userName }}
								<!-- <view class="review__content__rank--vip" wx:if="{{item.vipType}}">
                    {{item.vip}}
                </view> -->
							</view>
							<view class="review__content__time">
								{{ item.createTime }}
								<view class="review__content__time--para">{{
									item.specs
								}}</view>
							</view>
						</view>
					</view>
					<view class="review__content">
						<view class="review__content--rev">{{ item.comment }}</view>
						<view class="review__content__shopImg">
							<view v-for="(img, index2) in item.picture" :key="index2">
								<image
									:src="img"
									class="review__content__shopImg--item"
									@tap="handleSeePhoto"
									:data-img="img"
									:data-item="item"
								></image>
							</view>
						</view>
					</view>
				</view>
				<view class="review__reply" v-if="item.reply">
					<view class="review__reply__top">
						<view class="review__reply__top--service">掌柜回复：</view>
						<view class="review__reply__top--time">{{ item.replyTime }}</view>
					</view>
					<view style="margin-top:20rpx">{{ item.reply }}</view>
				</view>
			</view>
		</block>
		<view v-else :style="'height:' + height + 'px'" class="noReviews">
			<view class="noReviews__center">
				<image
					src="http://medusa-small-file.oss-cn-hangzhou.aliyuncs.com/api/comment.png"
					class="noReviews__center--img"
				></image>
				<view class="noReviews__center--text">暂无评价内容~</view>
			</view>
		</view>
	</view>
</template>

<script module="helper" lang="wxs" src="./helper.wxs"></script>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator'
import {
	getProductEvaluate,
	getProductEvaluateOverview
} from '@/api/modules/goods'
import { EvaluateItemType } from '../detail/detailType'

type IEvaluateOverview = Record<
	'all' | 'hasContent' | 'hasPicture' | 'praiseRate' | 'total',
	number
>

@Component({})
export default class Reviews extends Vue {
	/** 评价列表 */
	evaluateList: Array<EvaluateItemType> = []
	/** 评价概况 */
	evaluateOverview: IEvaluateOverview = {
		all: 0,
		hasContent: 0,
		hasPicture: 0,
		praiseRate: 0,
		total: 0
	}
	/** 查询条件 */
	query = {
		current: 1,
		type: 0,
		keyword: '',
		productId: '',
		size: 10
	}
	/** 是否已触底 */
	isLast: boolean = false

	height: number = 0
	options = {
		id: 55
	}

	onLoad(options: { id: number }) {
		const id = options.id || 55
		this.options = options
		this.getProductEvaluate(id)
		this.$Pubsub.on('app-launch', () => {
			this.getProductEvaluate(id)
		})
		wx.getSystemInfo({
			success: (res) => {
				this.setData({
					height: res.windowHeight
				})
			}
		})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 查看图片大图
	 * @param {*}
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
		wx.previewImage({
			current: img,
			urls: item
		})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 获取评价列表数据
	 * @param {number} productId
	 */
	getProductEvaluateList(productId: number) {
		getProductEvaluate({
			...this.query,
			productId
		})
			.then((res) => {
				let {
					current,
					pages,
					list
				}: {
					current: number
					pages: number
					list: EvaluateItemType[]
				} = res
				// 将字符串转为图片
				list = list.map((item: EvaluateItemType) => {
					item.pictureArr = this.handlePicture(item.picture)
					return item
				})

				this.setData({
					evaluateList: current > 1 ? [...this.evaluateList, ...list] : list
				})
				/** 如果为最后一页设置到底状态 */
				if (current === pages) {
					this.setData({
						isLast: true
					})
				}

				this.setData({
					evaluateList: list
				})
			})
			.catch((err) => {
				this.$Popup.toast(err || '评价列表获取失败')
			})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 获取评价总体数据
	 * @param {number} productId
	 */

	getProductEvaluate(productId: number) {
		this.getProductEvaluateList(productId)
		getProductEvaluateOverview({
			productId
		})
			.then((res: IEvaluateOverview) => {
				this.setData({
					evaluateOverview: res
				})
			})
			.catch((err) => {
				this.$Popup.toast(err || '评价概况获取失败')
			})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 切换筛选条件
	 * @param {*} e
	 */
	onTypeClick({
		currentTarget: {
			dataset: { type }
		}
	}: {
		currentTarget: {
			dataset: {
				type: number
			}
		}
	}) {
		this.setData(
			{
				['query.type']: Number(type)
			},
			() => {
				this.getProductEvaluateList(this.options.id)
			}
		)
	}

	handlePicture(pictures: string) {
		return pictures ? pictures.split(',') : []
	}
}
</script>

<style lang="scss" scoped>
@mixin center-middle {
	display: flex;
	align-items: center;
	justify-content: center;
}

page {
	background-color: #f2f1f2;
}

.assess__list {
	display: flex;
	justify-content: flex-start;
	font-size: 30rpx;
	padding: 10px 15px;
	flex-wrap: wrap;
	margin-bottom: 10px;
	background: #fff;

	.assess__list--button {
		padding: 6px 15px;
		background-color: #f9f2ef;
		border-radius: 50rpx;
		font-size: 12px;
		margin-right: 10px;
		display: flex;
		align-items: center;
		justify-content: center;
		height: 31px;

		.assess__list--button--num {
			font-size: 13px;
		}

		&.active {
			background: #fd000c;
			color: #fff;
		}
	}
}

.review {
	padding: 40rpx;
	margin-bottom: 10px;
	background: #fff;
}

.review__img {
	width: 80rpx;
	height: 80rpx;
	border-radius: 100%;
	margin-right: 10px;
}

.review__content {
	width: 500rpx;
	font-size: 26rpx;
	color: #878787;

	&__rank {
		display: flex;
		align-items: center;
		color: #000;
		margin-bottom: 9px;
		font-size: 26rpx;

		&--vip {
			margin-left: 20rpx;
			width: 160rpx;
			height: 40rpx;
			border-radius: 50rpx;
			background-color: #dfd2b5;
			color: white;
			display: flex;
			justify-content: center;
			align-items: center;
		}
	}

	&__time {
		display: flex;
		margin-top: 10rpx;
		color: #b1afaf;
		font-size: 24rpx;

		&--para {
			margin-left: 20rpx;
		}
	}

	&--rev {
		margin-top: 24rpx;
		color: #333333;
	}

	&__shopImg {
		display: flex;
		margin-top: 27rpx;

		&--item {
			display: flex;
			width: 160rpx;
			height: 160rpx;
			margin-right: 20rpx;
		}
	}
}

.review__reply {
	padding: 30rpx;
	border-radius: 5px;
	margin-top: 10rpx;
	font-size: 28rpx;
	background-color: #f2f2f2;

	&__top {
		display: flex;
		justify-content: space-between;

		&--service {
			color: #4a4a4a;
			font-weight: bold;
		}

		&--time {
			color: #c4c4c4;
		}
	}
}

.noReviews {
	display: flex;
	width: 100%;
	justify-content: center;
	background-color: white;
	flex-wrap: wrap;

	&__center {
		display: flex;
		justify-content: center;
		width: 590rpx;
		flex-wrap: wrap;
		height: 340px;

		&--img {
			margin-top: 50px;
		}

		&--text {
			margin-top: 20px;
			font-size: 16px;
		}
	}
}
</style>
