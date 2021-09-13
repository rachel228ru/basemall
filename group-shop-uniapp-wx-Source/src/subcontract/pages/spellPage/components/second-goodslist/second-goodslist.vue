<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-12 15:36:21
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-20 10:15:01
 * 123
-->
<template>
	<scroll-view
		:class="'top__biggoods-page ' + getBigGoodsPageClass"
		scroll-x="false"
		scroll-y="true"
		@scrolltolower="onReachBottom"
	>
		<!-- 列表模式 -->
		<view
			v-for="(item, index) in goodsList"
			:key="index"
			class="spellpre__goods spellpre__goodsL"
			style="padding: 0; padding-top: 11px;"
		>
			<view
				:class="'spellpre__goods--box ' + getGoodsBoxClass"
				:data-id="item.id"
				@tap.stop="gotoDetail"
			>
				<view class="spellpre__goodsL--content">
					<view class="spellpre__goodsL--icon">
						<view class="no__goods" v-if="!item.inventory">
							<text>已售罄</text>
							<text>Sold out</text>
						</view>
						<view class="ipic">
							<image
								:src="item.img"
								mode="widthFix"
								alt
								:lazy-load="true"
							></image>
						</view>
					</view>
					<view class="spellpre__goodsL--info">
						<view :class="'spellpre__goodsL--name ' + getGoodsNameClass">{{
							item.name
						}}</view>
						<view class="spellpre__goodsL--buy">
							<view class="spellpre__goodsL--prinum">
								{{
									'¥' + (item.actPrice !== '0.00' ? item.actPrice : item.price)
								}}
							</view>
							<view
								v-if="cartButton === 3"
								class="spellpre__goods--cart spellpre__goods--cart1"
							>
								<image
									src="https://oss-tencent.bgniao.cn//gruul/20200710/810700b67e264a7b82bba347528b9eb0.png"
									alt
								>
								</image>
							</view>
							<view
								v-if="cartButton === 4"
								class="spellpre__goods--cart spellpre__goods--cart2"
							>
								<image
									src="https://oss-tencent.bgniao.cn//gruul/20200710/3de00f3d8099459c83771b1f4decb197.png"
									alt
								>
								</image>
							</view>
							<view
								v-if="cartButton === 1"
								class="spellpre__goods--cart spellpre__goods--cart3"
							>
								立即购买
							</view>
							<view
								v-if="cartButton === 2"
								class="spellpre__goods--cart spellpre__goods--cart4"
							>
								立即购买
							</view>
						</view>
					</view>
				</view>
			</view>
		</view>
	</scroll-view>
</template>

<script lang="ts">
import { Component, Vue, Prop, Watch } from 'vue-property-decorator'

@Component({})
export default class SecondGoodsdetail extends Vue {
	@Prop({
		default: 3
	})
	cartButton!: number
	@Prop() goodsList!: any[]

	getGoodsNameClass: string = '' // 商品名称文本样式
	getGoodsBoxClass: string = '' // 商品边框样式
	getBigGoodsPageClass: string = ''

	@Watch('cartButton')
	getCartButton() {
		this.getInfo()
	}
	@Watch('goodsList')
	getGoodsList() {
		this.getInfo()
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 触底加载更多
	 */
	onReachBottom() {
		this.$emit('loadMored')
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 加载数据
	 */

	getInfo() {
		this.GoodsNameClass()
		this.GoodsBoxClass()
		this.BigGoodsPageClass()
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 获取商品边框样式
	 */

	GoodsBoxClass() {
		const goodsStyle = 1
		const doodsAngle = 2
		// 商品样式 1无底白边 2卡片投影 3描边白底
		const gstyles = [
			'spellpre__goods--boxWB',
			'spellpre__goods--boxCP',
			'spellpre__goods--boxSW'
		]
		// 图片倒角 1直角 2圆角
		const astyles = ['spellpre__goods--corners', 'spellpre__goods--angle']
		const gs = gstyles[+goodsStyle - 1]
		const as = astyles[+doodsAngle - 1]
		this.setData({
			getGoodsBoxClass: `${gs} ${as}`
		})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 获取商品名称文本样式
	 */

	GoodsNameClass() {
		const { textStyle = 1 } = {}
		// 1常规体 2加粗体
		const styles = ['spellpre__goods--nameF', 'spellpre__goods--nameB']
		this.setData({
			getGoodsNameClass: styles[+textStyle - 1]
		})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 设置商品class值
	 */

	BigGoodsPageClass() {
		// 1顶部模式 2侧边栏模式 1大图样式 2列表样式
		const { selectMode = 1 } = {}
		const list = [] as any
		selectMode === 1 && list.push('page__goods-addPadding')
		this.setData({
			getBigGoodsPageClass: `${list.join(' ')}`
		})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 进入商品详情
	 * @param {*} e
	 */

	gotoDetail(e: { currentTarget: { dataset: { id: number } } }) {
		const goodId = e.currentTarget.dataset.id
		uni.navigateTo({
			url: `/subcontract/pages/detail/detail?id=${goodId}`
		})
	}

	mounted() {
		this.getInfo()
	}
}
</script>

<style lang="scss" scoped>
.top__biggoods-page {
	font-size: 0;

	.spellpre__goods--box {
		padding: 0px;
	}

	@include b(spellpage) {
		.el-radio {
			margin-right: 20px;
		}

		@include e(addmart) {
			margin-top: 7px;
		}
	}

	@include b(spellpre) {
		box-sizing: border-box;
		background-color: #f8f8f8;
		position: relative;

		@include e(header) {
			color: #333;
			height: 50px;
			line-height: 50px;
			background-color: #fff;
			width: 100%;
			position: absolute;
			left: 0;
			top: 0;
			z-index: 2;

			@include m(title) {
				font-size: 18px;
				font-weight: 700;
			}

			@include m(more) {
				font-size: 14px;
				font-weight: 400;
				float: right;
			}
		}

		@include e(headtemp) {
			height: 50px;
			line-height: 50px;
			width: 100%;
		}

		@include e(goods) {
			box-sizing: border-box;

			.spellpre__goods--box {
				position: relative;
				background-color: #fff;
				overflow: hidden;
			}

			@include m(boxCP) {
				box-shadow: 0 0 4px rgba(0, 0, 0, 0.2);
			}

			@include m(boxSW) {
				border: 1px solid #f8f8f8;
			}

			@include m(angle) {
				border-radius: 7px;
			}

			@include m(corners) {
				border-radius: 0;
			}

			@include m(nameF) {
				font-weight: 400;
			}

			@include m(nameB) {
				font-weight: 600;
			}

			.no__goods {
				position: absolute;
				left: 50%;
				top: 50%;
				margin-left: -70rpx;
				margin-top: -70rpx;
				z-index: 11;
				width: 140rpx;
				height: 140rpx;
				background: rgba(0, 0, 0, 0.5);
				border-radius: 50%;
				display: flex;
				align-items: center;
				justify-content: center;
				color: #fff;
				flex-direction: column;

				text {
					display: block;
					width: 176rpx;
					text-align: center;
					font-size: 25rpx;
				}
			}

			@include m(cart1) {
				float: right;
				height: 28px;
				width: 28px;
				background-color: rgba(252, 98, 63, 1);
				box-sizing: border-box;
				border-radius: 50%;
				display: flex;
				justify-content: center;
				align-items: center;
				margin-top: 5px;

				image {
					display: inline-block;
					width: 19px;
					height: 19px;
				}
			}

			@include m(cart) {
				float: right;
				height: 28px;
				width: 28px;
				box-sizing: border-box;
				border-radius: 50%;
				display: flex;
				justify-content: center;
				align-items: center;
				margin-top: 5px;
			}

			@include m(cart1) {
				background-color: #fff;

				image {
					display: inline-block;
					width: 30px;
					height: 30px;
				}
			}

			@include m(cart2) {
				background: linear-gradient(
					164deg,
					rgba(243, 243, 243, 1),
					rgba(229, 56, 46, 1),
					rgba(253, 78, 38, 1)
				);
				box-shadow: 0px 2px 7px 0px rgba(255, 14, 0, 0.27);
				border-radius: 50%;

				image {
					display: inline-block;
					width: 20px;
					height: 20px;
				}
			}

			@include m(cart3) {
				border: 1px solid rgba(252, 98, 63, 1);
				width: 80px;
				color: rgba(252, 98, 63, 1);
				font-size: 14px;
				border-radius: 20px;
				height: 30px;
				line-height: 30px;
			}

			@include m(cart4) {
				border: 1px solid rgba(252, 98, 63, 1);
				background-color: rgba(252, 98, 63, 1);
				width: 80px;
				color: #fff;
				font-size: 14px;
				border-radius: 20px;
				height: 30px;
				line-height: 30px;
			}

			@include m(coner) {
				position: absolute;

				image,
				text {
					display: block;
					width: 100%;
					height: 100%;
					position: absolute;
				}
			}

			@include m(coner1) {
				left: -2px;
				top: 10px;
				width: 52px;
				height: 24px;
			}

			@include m(coner2) {
				left: 10px;
				top: 0px;
				width: 48px;
				height: 26px;

				text {
					color: white;
					font-size: 12px;
					text-align: center;
					line-height: 24px;
				}
			}

			@include m(coner3) {
				left: 0;
				top: 0;
				width: 48px;
				height: 40px;
			}

			@include m(delivery) {
				view {
					color: #fa6454;
					font-size: 12px;
					font-weight: 400;
					display: inline-block;
					background-color: rgba(250, 91, 74, 0.2);
					border-radius: 17px;
					padding: 2px 8px;
				}
			}

			@include m(footer) {
				.timebox {
					display: inline-block;
					height: 24px;
				}

				.tip {
					display: inline-block;
					height: 24px;
					line-height: 24px;
					color: #8b8b8b;
					font-size: 13px;
					margin-right: 4px;
					margin-top: 1px;
				}
			}

			.time_flex {
				width: 110px;
				height: 24px;
				display: inline-block;
				color: #2f2f2f;
				font-size: 9px;
				vertical-align: inherit;

				view {
					font-size: 13px;
					display: inline-block;
					width: 12px;
					height: 15px;
					line-height: 15px;
					text-align: center;
					color: #fff;
					background-color: #2f2f2f;
					border-radius: 3px;
					margin: 0px 1px;
					vertical-align: text-bottom;
				}
			}

			.time-info {
				float: right;
				height: 24px;
				line-height: 24px;
				color: #a3a3a3;
				font-size: 13px;
				margin-left: 8px;
			}

			.user__icon {
				float: right;
				height: 24px;
				width: 60px;
				position: relative;
				margin-top: 1px;

				view {
					display: inline-block;
					width: 24px;
					height: 24px;
					border-radius: 50%;
					border: 1px solid rgba(255, 255, 255, 1);
					background-color: #d1d1d1;
					top: 0;
					position: absolute;

					image {
						width: 24px;
						height: 24px;
						border-radius: 50%;
					}
				}

				.icon_1 {
					right: 0;
					z-index: 1;
				}

				.icon_2 {
					right: 12px;
					z-index: 2;
				}

				.icon_3 {
					right: 24px;
					z-index: 3;
				}
			}
		}

		@include e(goodsL) {
			box-sizing: border-box;

			.spellpre__goods--box {
				box-sizing: border-box;
				padding: 10px;
			}

			@include m(content) {
				display: flex;
			}

			@include m(box) {
				display: flex;
				box-sizing: border-box;
				overflow: hidden;
				position: relative;
			}

			@include m(icon) {
				width: 138px;
				height: 138px;
				position: relative;

				.ipic {
					display: inline-block;
					width: 138px;
					height: 138px;
					overflow: hidden;
					border-radius: 3px;

					image {
						min-height: 276rpx;
						width: 100%;
					}
				}
			}

			.spellpre__goods--cart {
				margin-top: 12px;
			}

			.spellpre__goods--coner1 {
				left: 10px;
				top: 20px;
			}

			.spellpre__goods--coner2 {
				left: 10px;
				top: 10px;
			}

			.spellpre__goods--coner3 {
				left: 10px;
				top: 10px;
			}

			@include m(info) {
				flex: 1;
				box-sizing: border-box;
				margin-left: 10px;
				position: relative;
			}

			@include m(name) {
				text-overflow: -o-ellipsis-lastline;
				overflow: hidden;
				text-overflow: ellipsis;
				display: -webkit-box;
				-webkit-line-clamp: 2;
				line-clamp: 2;
				-webkit-box-orient: vertical;
				font-size: 15px;
				line-height: 20px;
				color: #0e0e0e;
				box-sizing: border-box;
				padding: 8px 0;
				max-height: 48px;
			}

			@include m(buy) {
				width: 100%;
				height: 65px;
				position: absolute;
				bottom: 0;
				left: 0;
				box-sizing: border-box;
				padding-top: 65rpx;
			}

			@include m(prinum) {
				display: inline-block;
				font-size: 22px;
				font-weight: 400;
				color: #ec342c;
				font-weight: 600;
			}

			@include m(guaid) {
				font-size: 14px;
				color: #aaaaaa;
				text-decoration: line-through;
			}

			@include m(footer) {
				margin-top: 12px;
				height: 20px;
				line-height: 20px;
				position: relative;
				box-sizing: border-box;

				.tip {
					vertical-align: middle;
				}
			}
		}

		@include e(goodsB) {
			box-sizing: border-box;

			@include m(box) {
				box-sizing: border-box;
				overflow: hidden;
				position: relative;
			}

			@include m(icon) {
				width: 100%;
				position: relative;

				image {
					width: 100%;
					height: 175px;
				}
			}

			.spellpre__goods--delivery {
				padding-top: 0px;
			}

			.detail__box {
				box-sizing: border-box;
				padding: 0 8px;
			}

			@include m(name) {
				text-overflow: ellipsis;
				overflow: hidden;
				white-space: nowrap;
				font-size: 15px;
				line-height: 31px;
				height: 31px;
				color: #0e0e0e;
			}

			@include m(buy) {
				width: 100%;
				height: 40px;
				line-height: 40px;
				box-sizing: border-box;
			}

			@include m(prinum) {
				display: inline-block;
				font-size: 22px;
				font-weight: 500;
				color: #ec342c;
				height: 40px;
				line-height: 40px;
				font-weight: 600;
			}

			@include m(guaid) {
				font-size: 14px;
				color: #aaaaaa;
				display: inline-block;
				margin-left: 5px;
				height: 40px;
				line-height: 40px;
				text-decoration: line-through;
			}

			@include m(info) {
				box-sizing: border-box;
				height: 35px;
				position: relative;
			}
		}
	}
}

.page__goods-addPadding {
	padding: 0px 10px;
	height: calc(100vh - 100px);
	overflow-y: scroll;
	box-sizing: border-box;
}

.top__biggoods-page.left__Lgoods-style {
	.spellpre__goodsL--icon,
	.ipic {
		width: 90px;
		height: 90px;
	}

	.spellpre__goodsL--content {
		width: 100%;
		overflow: hidden;
	}

	.spellpre__goodsL--info {
		width: calc(100% - 100px);
	}

	.spellpre__goods--delivery {
		display: none;
	}

	.spellpre__goodsL--name {
		padding: 4px 0;
		max-height: 22px;
		white-space: nowrap;
	}
}
</style>
