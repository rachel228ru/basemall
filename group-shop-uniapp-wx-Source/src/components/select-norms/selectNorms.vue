<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-12 15:36:20
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-09-02 10:54:36
 * 123
-->
<template>
	<view
		:class="' ' + (goodDetail.skuStocks.length > 1 ? 'specific' : 'specific2')"
	>
		<view class="specific__info">
			<image :src="normsData.pic" class="specific__info--image"></image>
			<view class="specific__info--product">
				<view style="display:flex;align-items:flex-end">
					<view class="product__price">
						<span>{{ normsData.intPrice }}</span>
						<span style="font-weight:weight;font-size:13px;"
							>.{{ normsData.smaPrice }}</span
						>
					</view>
				</view>
				<view class="product__line">
					<view class="product__stock" v-if="goodDetail.skuStocks.length > 1">
						<span v-if="normsData.specs">已选：{{ normsData.specs }}</span>
						<span v-else>请先选择规格</span>
					</view>
					<view class="product__max">
						<span v-if="goodDetail.skuStocks.length > 1 && normsData.perLimit">
							限购{{ normsData.perLimit }}件
						</span>
						<span
							v-if="
								goodDetail.skuStocks.length === 1 &&
									goodDetail.skuStocks[0].perLimit !== 0
							"
						>
							限购{{ goodDetail.skuStocks[0].perLimit }}件
						</span>
					</view>
				</view>
			</view>
			<m-icon
				name="iconshangpinxiangqing-guanbi"
				size="19px"
				@tap="onClose"
				style="position:absolute;top:15px;right:15px;"
			></m-icon>
		</view>
		<view class="select" v-if="goodDetail.skuStocks.length > 1">
			<view class="select__title">规格</view>
			<scroll-view scroll-y style="height:200px;overflow:hidden;">
				<view class="select__list">
					<view
						v-for="(item, index) in goodDetail.skuStocks"
						:key="index"
						class="select__list--value "
						@tap="checkNorms"
						:data-mode="item"
					>
						<van-toast id="van-toast"></van-toast>
						<view
							class="select__list--value--info center--middle "
							style="background-color:#F2F2F2;color:#CDCFD0"
							v-if="item.stock === 0"
						>
							{{ item.specs }}
						</view>
						<view
							class="select__list--value--info center--middle "
							:style="
								'color:' +
									(item.getType && item.stock > 0 ? 'red' : '') +
									';border:1px solid ' +
									(item.getType ? 'red' : '#F2F2F2') +
									';background-color:' +
									(item.getType ? '#F6EBEC' : '#F2F2F2')
							"
							v-else
						>
							{{ item.specs }}
						</view>
					</view>
				</view>
			</scroll-view>
		</view>
		<view
			class="btn"
			:style="
				'bottom:' + (goodDetail.skuStocks.length > 1 ? '70' : '65') + 'px'
			"
		>
			<view class="select__title">数量</view>
			<van-stepper
				:value="numValue"
				integer
				min="1"
				:max="normsData.maxNum"
				step="1"
				@change="onChange"
				input-width="120rpx"
			></van-stepper>
		</view>
		<view class="poup" v-if="shopCar">
			<view class="poup__btn" @tap="addShopCar" style="width:100%"
				>加入购物车</view
			>
		</view>
		<view class="poup" v-else>
			<view class="poup__btn" @tap="addShopCar">加入购物车</view>
			<van-toast id="van-toast"></van-toast>
			<view
				@tap="addShopCar"
				:data-index="1"
				class="poup__btn"
				style="background-color:#fa5555"
			>
				立即购买
			</view>
		</view>
	</view>
</template>
<script lang="ts">
import { Component, Vue, Prop } from 'vue-property-decorator'
import Toast from '@/wxcomponents/vant-weapp/toast/toast'
import mIcon from '@/components/m-icon/m-icon.vue'
import { GoodInfo, ApiSkuType } from '@/pages/index/modules/shopCar/shopCarType'
interface SelectGoodInfo extends GoodInfo {
	goodsNumber: number
	skuName: string
}
@Component({
	components: {
		mIcon
	}
})
export default class SelectNorms extends Vue {
	numValue: number = 1
	@Prop()
	buyShow!: boolean
	@Prop({
		default: {}
	})
	norms!: ApiSkuType
	@Prop()
	goodDetail!: SelectGoodInfo
	@Prop()
	state!: string
	@Prop()
	buyType!: boolean
	@Prop()
	shopCar!: boolean

	normsData: ApiSkuType = {} as ApiSkuType

	created() {
		this.propDataChange()
	}

	mounted() {
		this.propDataChange()
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 触发关闭
	 * @param {*}
	 * @returns {*}
	 */

	onClose(): void {
		this.$emit('onClose', {}, {})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 数据处理
	 * @param {*}
	 * @returns {*}
	 */

	propDataChange(): void {
		this.setnorms(this.norms)
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 数据处理存储
	 * @param {ApiSkuType} normsData
	 * @returns {*}
	 */

	setnorms(normsData: ApiSkuType): void {
		const goodDetail = this.goodDetail
		this.numValue = goodDetail.goodsNumber ? goodDetail.goodsNumber : 1
		const norms = {
			...normsData,
			price:
				typeof normsData.price === 'number'
					? normsData.price.toFixed(2)
					: Number(normsData.price).toFixed(2),
			intPrice: (normsData.price + '').split('.')[0],
			smaPrice: (normsData.price + '').split('.')[1] || '00'
		}
		if (!norms.maxNum) {
			norms.maxNum = norms.perLimit < norms.stock ? norms.perLimit : norms.stock
			norms.maxNum = norms.perLimit === 0 ? norms.stock : norms.maxNum
		}
		this.normsData = { ...norms }
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 点击切换规格
	 * @param {*} e
	 * @returns {*}
	 */

	checkNorms(e: { currentTarget: { dataset: { mode: ApiSkuType } } }): void {
		const goodDetail = this.goodDetail
		let norms = this.normsData
		const mode = e.currentTarget.dataset.mode
		if (mode.stock === 0) {
			uni.showToast({
				title: '此规格暂无库存',
				icon: 'none'
			})
			return
		}
		goodDetail.skuStocks.forEach((item) => {
			item.getType = mode.id === item.id ? true : false
			if (item.getType) {
				norms = item
			}
		})

		this.numValue = 1
		this.goodDetail = goodDetail
		this.$emit('checkNorms', mode)
		this.setnorms(norms)
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 修改商品数量触发
	 * @param {*} e
	 * @returns {*}
	 */

	onChange(e: number): void {
		const norms = this.normsData
		if (e === norms.stock) {
			Toast({
				message: '库存不足'
			})
		} else if (e === norms.perLimit) {
			Toast({
				message: '您已达限购数量'
			})
		}

		this.numValue = e
		this.$emit('onChange', {}, {})
	}
	/**
	 * 加入购物车
	 */
	/**
	 * @LastEditors: chuyinlong
	 * @description: 加入购物车
	 * @param {*} e
	 * @returns {*}
	 */

	addShopCar(e: {
		currentTarget: { dataset: { index: number | null } }
	}): void {
		this.$STORE.setStore.setFormShopCar(false)
		const goodDetail = this.goodDetail
		const norms = this.normsData
		if (goodDetail.skuStocks.length === 1) {
			this.soloAdd(goodDetail, e)
			return
		}
		if (!norms.id) {
			uni.showToast({
				title: '请先选择规格',
				icon: 'none'
			})
			return
		}

		if (norms.stock === 0) {
			Toast({
				message: '此商品暂无库存'
			})
			return
		}
		goodDetail.goodsNumber = this.numValue
		goodDetail.pic = norms.pic
		goodDetail.skuId = norms.id
		goodDetail.skuName = norms.specs

		this.$emit('addShopCar', {
			goodDetail,
			index: e.currentTarget.dataset.index
		})
	}
	/**
	 * 单规格商品
	 */
	/**
	 * @LastEditors: chuyinlong
	 * @description: 单规格商品
	 * @param {*} goodDetail
	 * @param {*} e
	 * @returns {*}
	 */

	soloAdd(
		goodDetail: SelectGoodInfo,
		e: { currentTarget: { dataset: { index: number | null } } }
	): void {
		goodDetail.goodsNumber = this.numValue
		goodDetail.skuId = goodDetail.skuStocks[0].id
		this.$emit(
			'addShopCar',
			{
				goodDetail,
				index: e.currentTarget.dataset.index
			},
			{}
		)
	}
}
</script>
<style lang="scss" scoped>
.center--middle {
	display: flex;
	justify-content: center;
	align-items: center;
}

.specific {
	height: 460px;
}

.specific2 {
	height: 225px;
}

.specific__info {
	width: 100%;
	height: 125px;
	display: flex;
	flex-direction: row;
	padding-left: 30rpx;
	padding-right: 30rpx;
	box-sizing: border-box;
	align-items: flex-end;
}

.specific__info--image {
	width: 110px;
	height: 110px;
}

.specific__info--product {
	flex: 1;
	margin-left: 15px;
	margin-bottom: 10px;

	.product__price {
		font-size: 17px;
		font-weight: bold;
		color: #fa5555;
	}

	.product__price::before {
		content: '￥';
		font-weight: normal;
		font-size: 13px;
	}

	.product__line {
		display: flex;
		margin-top: 12px;
		align-items: center;
	}

	.product__max {
		font-size: 13px;
		color: #fa5555;
	}

	.product__stock {
		height: 30rpx;
		line-height: 30rpx;
		font-size: 13px;
		margin-right: 10px;
	}
}

.select {
	padding: 20px 15px 10px 20px;
	font-size: 15px;
}

.select__title {
	font-size: 14px;
}

.select__list {
	display: flex;
	flex-wrap: wrap;
	margin-top: 10px;
	overflow: auto;
}

.select__list::-webkit-scrollbar {
	display: none;
}

.select__list--value {
	margin-bottom: 20rpx;
	margin-top: 20rpx;
	margin-right: 20px;

	.select__list--value--info {
		height: 27px;
		border-radius: 50px;
		font-size: 13px;
		padding: 12px;
	}
}

.btn {
	padding-left: 30rpx;
	padding-right: 30rpx;
	padding-top: 20rpx;
	padding-bottom: 20rpx;
	font-size: 36rpx;
	display: flex;
	justify-content: space-between;
	height: 60rpx;
	position: fixed;
	width: 100%;
	align-items: center;
	bottom: 80px;
}

.poup {
	position: fixed;
	width: 100%;
	display: flex;
	bottom: 0;

	&__btn {
		width: 50%;
		height: 98rpx;
		background-color: #515151;
		text-align: center;
		line-height: 98rpx;
		font-size: 30rpx;
		font-family: MicrosoftYaHei;
		font-weight: 400;
		color: #fff;
	}
}
</style>
