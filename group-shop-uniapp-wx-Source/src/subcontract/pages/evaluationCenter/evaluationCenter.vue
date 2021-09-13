<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-12 15:36:21
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-09-01 16:03:16
 * 123
-->
<template>
	<view class="evaluation__center">
		<block v-if="list.length">
			<block v-for="(item, index) in list" :key="index">
				<block v-for="(goods, index2) in item.itemList" :key="index2">
					<view
						class="evaluation__center--item"
						:key="productId"
						@tap="goGoods"
						:data-id="goods.productId"
					>
						<view class="item--date">{{ goods.createTime }}</view>
						<view class="item--content">{{ goods.comment }}</view>
						<view class="item--images">
							<view
								v-for="(img, index) in goods.pictureArr"
								:key="index"
								class="image"
							>
								<image
									:src="img"
									@tap.stop="handleSeePhoto"
									:data-img="img"
									:data-item="goods.pictureArr"
								></image>
							</view>
						</view>
						<view class="item--reply" v-if="goods.reply">
							<view class="imp">商家回复</view>
							{{ goods.reply }}
						</view>
						<goods :goodsData="goods"></goods>
						<view class="item--score">
							<text>商品评分{{ goods.rate ? goods.rate : '0' }}.0</text>
							<van-rate
								:disabled="true"
								:value="goods.rate"
								disabled-color="#ffd21e"
							></van-rate>
						</view>
					</view>
				</block>
			</block>
		</block>
		<block v-else>
			<data-empty :infoVisible="false" tips="暂无数据"></data-empty>
		</block>
		<van-divider contentPosition="center" v-if="isLast">到底了</van-divider>
	</view>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator'
import { getEvaluateList } from '@/api/modules/order'
import { IEvaluate } from './evaluationCenterType'

@Component({})
export default class EvaluationCenter extends Vue {
	/** 评价列表 */
	list: Array<IEvaluate> = []
	/** 查询条件 */
	query = {
		keyword: '',
		current: 1,
		size: 10
	}
	// 是否到底
	isLast: boolean = false

	onLoad() {
		this.getEvaluateList()
		this.$Pubsub.on('app-launch', () => {
			this.getEvaluateList()
		})
	}

	onReachBottom() {
		this.setData({
			'query.current': ++this.query.current
		})
		this.getEvaluateList()
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 将字符串转为数组
	 * @param {IEvaluate[]} arr
	 * @returns {*}
	 */

	transformPics(arr: IEvaluate[]): IEvaluate[] {
		return arr.map((item) => {
			item.itemList = item.itemList.map((product) => {
				if (product.picture) {
					product.pictureArr = product.picture.split(',')
				}
				return product
			})
			return item
		})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 查看图片大图
	 * @param {*}
	 */
	handleSeePhoto({
		currentTarget: {
			dataset: { item, image }
		}
	}: {
		currentTarget: {
			dataset: {
				item: string[]
				image: string
			}
		}
	}) {
		uni.previewImage({
			current: image,
			urls: item
		})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 获取评价列表
	 */

	getEvaluateList() {
		getEvaluateList(this.query)
			.then((res) => {
				let { current, pages, list } = res
				list = this.transformPics(list)
				this.setData({
					list: current > 1 ? [...this.list, ...list] : list
				})

				/** 如果为最后一页设置到底状态 */
				if (current === pages) {
					this.setData({
						isLast: true
					})
				}
			})
			.catch((err) => {
				this.$Popup.toast(err || '列表获取失败')
			})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 前往商品详情
	 */
	goGoods({
		currentTarget: {
			dataset: { id }
		}
	}: {
		currentTarget: {
			dataset: {
				id: number | string
			}
		}
	}) {
		uni.navigateTo({
			url: `/subcontract/pages/detail/detail?id=${id}`
		})
	}
}
</script>

<style lang="scss" scoped>
page {
	background: $main-background;
}

@include b(evaluation) {
	@include e(center) {
		width: 100%;
		height: 100%;
		background: $main-background;

		@include m(item) {
			width: 100%;
			padding: 10px 15px;
			background: #fff;
			margin-bottom: 10px;

			.item--date {
				font-size: 12px;
				color: #ccc;
			}

			.item--content {
				margin: 10px 0;
			}

			.item--reply {
				width: 100%;
				height: auto;
				background: rgb(241, 241, 241);
				margin: 10px 0;
				padding: 10px 10px;
				border-radius: 5px;

				.imp {
					font-weight: bold;
					margin: 0 0 5px;
				}
			}

			.item--images {
				@include flex(flex-start);

				margin-top: 10px;
				margin-bottom: 10px;

				.image {
					$s: 60px;

					width: $s;
					height: $s;
					background: #ccc;
					margin-right: 10px;

					image {
						width: 100%;
						height: 100%;
					}
				}
			}

			.item--score {
				@include flex(space-between);
				padding: 15px 0;
				border-top: 1px solid $main-border-color;
			}
		}

		@include m(group) {
			@include flex(space-between);
			padding: 15px 15px;
			background: #fff;

			margin-bottom: 10px;
		}
	}
}
</style>
