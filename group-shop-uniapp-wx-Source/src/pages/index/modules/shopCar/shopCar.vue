<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-12 15:36:21
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-30 14:24:50
 * 123
-->
<template>
	<view>
		<view class="shop__car--top" :style="'top:' + navigationBarHeight + 'px'">
			<view class="shop__car--top--container">
				<view
					v-if="shopCarList.length > 0 || shopLoseCarList.length > 0"
					style="font-size:28rpx"
				>
					<view v-if="!editFlag" @tap="eidtList">编辑</view>
					<view v-else @tap="eidtFinish">完成</view>
				</view>
			</view>
		</view>
		<sku
			:showType="showType"
			:goodDetail="goodDetail"
			:norms="norms"
			@addShopCar="addShopCar"
			@close="closeSku"
			@checkNorms="checkNorms"
		></sku>
		<view
			class="shop__car"
			v-if="shopCarList.length > 0 || shopLoseCarList.length > 0"
		>
			<van-dialog id="van-dialog"></van-dialog>
			<view class="shop__car--goods">
				<view
					v-for="(good, goodIndex) in shopCarList"
					:key="goodIndex"
					class="shop__car--container"
					style="height:140px;box-shadow: 0px 0px 6px #EBEBEB"
				>
					<view v-if="good.status === 1" class="shop__car--container--item">
						<van-checkbox
							:value="good.isCheck"
							@change="onChange"
							checked-color="#FF4444"
							style="margin-right:10px"
							:data-index="goodIndex"
							:data-item="good"
							:disabled="good.checkStock === 0"
						></van-checkbox>
						<!-- 加入购物车商品 -->
						<goods
							:good="good"
							:index="goodIndex"
							:checkStock="good.checkStock"
							@getSku="getSku"
							@changeNum="changeNum"
						>
							<stepper
								:index="goodIndex"
								:good="good"
								:num="good.goodsNumber"
								:stockNum="good.checkStock"
								:perLimit="good.perLimit"
								v-if="good.status === 1"
								@changeNum="changeNum"
								@delGood="delGood"
							></stepper>
						</goods>
						<van-toast id="van-toast"></van-toast>
					</view>
				</view>
			</view>
			<!-- 购物车失效商品 -->
			<unusable
				:shopLoseCarList="shopLoseCarList"
				@emptyUseless="emptyUseless"
			></unusable>
			<view class="pageBottom">
				<view class="pageBottom__check">
					<van-checkbox
						@tap="chooseAll"
						:value="selectAll"
						checked-color="#FF4444"
						style="font-size:13px;"
						:disabled="
							shopCarList.length === 0 && shopLoseCarList.length > 0
								? true
								: false
						"
					>
						{{ selectAll ? '取消全选' : '全选' }}
					</van-checkbox>
					<view view class="pageBottom__check--allPrice" v-if="!editFlag">
						<span style="font-size:15px">合计:</span>
						<span class="pageBottom__check--allPrice--price"
							>￥{{ allPrice }}</span
						>
					</view>
				</view>
				<view class="pageBottom__button" v-if="!editFlag" @tap="submit">
					结算({{ selectList.length }})
				</view>
				<view v-else style="display:flex">
					<view class="moveCollect" @tap="addCollect">移入收藏夹</view>
					<view class="pageBottom__delBtn" @tap="deleteButton">删除</view>
				</view>
			</view>
		</view>
		<view v-else :style="'height:' + pageHeight + 'px;background-color:white'">
			<view class="shopNoCar">
				<image
					src="http://medusa-small-file.oss-cn-hangzhou.aliyuncs.com/api/noCar.png"
					class="shop__noCar"
				></image>
				<view class="shop__noCar--text">你的购物车空空如也</view>
				<view class="shop__noCar--buyButton" @tap="gotoHome">去下单</view>
			</view>
		</view>
	</view>
</template>
<script lang="ts">
import { Component, Vue, Prop } from 'vue-property-decorator'

import goods from './components/goods/goods.vue'
import unusable from './components/unusable/unusable.vue'
import sku from './components/pop/sku/sku.vue'
import stepper from '@/components/stepper/stepper.vue'
import mIcon from '@/components/m-icon/m-icon.vue'

import { shopCarState, GoodInfo, ApiSkuType, IAddCar } from './shopCarType'

import {
	getCarsList,
	delCarsList,
	editCarsList,
	emptyCarsList,
	changeType
} from '@/api/modules/shopCar'
import { userAddCollect } from '@/api/modules/goods'
import Dialog from '@/wxcomponents/vant-weapp/dialog/dialog'
interface SelectGoodInfo extends GoodInfo {
	goodsNumber: number
	skuName: string
}

@Component({
	components: {
		goods,
		unusable,
		sku,
		stepper,
		mIcon
	}
})
export default class ShopCar extends Vue implements shopCarState {
	@Prop()
	navigationBarHeight = ''
	shopCarList: Array<GoodInfo> = []
	temShopList: Array<GoodInfo> = []
	shopType = true
	shopLoseCarList: Array<GoodInfo> = []
	editFlag = false
	selectAll = false
	selectList: Array<GoodInfo> = []
	showType = false
	goodDetail = {} as GoodInfo
	norms = {} as ApiSkuType
	allPrice = '0.00'
	isPhone11: number | boolean = 46 || true
	isAddCollect = false
	canClick = false
	pageHeight = 0

	created() {
		this.getList()
		uni.setNavigationBarTitle({
			title: '购物车'
		})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 移入收藏夹
	 * @param {*}
	 * @returns {*}
	 */

	addCollect(): void {
		const selectList = this.getSelectList()
		if (selectList.length === 0) {
			uni.showToast({
				title: '请选择要加入商品',
				icon: 'none'
			})
			return
		}
		const list: IAddCar[] = []
		selectList.forEach((item) => {
			item.skuStocks.forEach((v) => {
				if (v.id === item.skuId) {
					item.originalPrice = v.originalPrice
				}
			})
			list.push({
				originalPrice: item.originalPrice,
				productId: item.productId,
				productName: item.productName,
				productPic: item.pic,
				productPrice: item.actPrice,
				status: item.status
			})
		})
		userAddCollect(list)
			.then(() => {
				uni.showToast({
					title: '收藏成功',
					icon: 'none'
				})
				this.isAddCollect = true
				this.deleteButton()
			})
			.catch((err) => {
				console.log(err)
			})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 订单提交
	 * @param {*}
	 * @returns {*}
	 */

	submit(): void {
		const items = this.shopCarList
			.filter((item) => item.isCheck)
			.map((item) => {
				return {
					activityId: item.activityId,
					activityProductId: item.activityProductId,
					dominoState: item.dominoState,
					number: item.goodsNumber,
					skuId: item.skuId
				}
			})
		if (!items.length) {
			uni.showToast({
				title: '请选择商品',
				icon: 'none'
			})
			return
		}
		uni.navigateTo({
			url: `/modules/pages/submit/submit?items=${encodeURIComponent(
				JSON.stringify(items)
			)}`
		})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 获取购物车列表
	 * @param {*}
	 * @returns {*}
	 */

	async getList(): Promise<void> {
		this.$emit('shopCarChange', {}, {})
		let model = ''
		let isPhone11 = this.isPhone11
		let selectAll = false

		const v = uni.getSystemInfoSync()
		uni.getSystemInfo({
			success(result) {
				model = result.model
				if (model === 'iPhone XS<iPhone11,2>') {
					isPhone11 = 80
				}
				if (
					model === 'iPhone 11<iPhone12,1>' ||
					model === 'iPhone XR<iPhone11,8>'
				) {
					isPhone11 = 86
				}
				if (model === 'iPhone X (GSM+CDMA)<iPhone10,3>') {
					isPhone11 = 82
				}
			}
		})
		const res = await getCarsList()
		let list: GoodInfo[] = res
		const useList: GoodInfo[] = []
		const noList: GoodInfo[] = []
		let shopType = false
		if (!list) {
			this.shopCarList = [...useList]
			this.temShopList = [...useList]
			this.shopLoseCarList = noList
			this.allPrice = '0.00'
			this.isPhone11 =
				model === 'iPhone XS<iPhone11,2>' ||
				model === 'iPhone 11<iPhone12,1>' ||
				model === 'iPhone XR<iPhone11,8>' ||
				model === 'iPhone X (GSM+CDMA)<iPhone10,3>'
					? true
					: false
			this.shopType = true
			this.selectAll = false
			this.pageHeight = v.windowHeight
			return
		}
		const deliveryList: GoodInfo[] = []
		list.forEach((item) => {
			deliveryList.push(item)
		})
		if (!shopType) {
			list = deliveryList
		}
		list.forEach((item, index) => {
			item.isCheck = false
			item.itemIndex = index
			item.skuList = []
			item = this.dealPrice(item)
			const type = item.skuStocks.every((v) => {
				return v.stock === 0
			})
			item.status = type ? 2 : item.status
			if (item.deleted === 1) {
				item.status = 2
			}
			item.skuStocks.forEach((v) => {
				item.skuList = item.skuList ? item.skuList : []
				item.skuList.push(v.id)
				v.price = v.price === 0 ? v.originalPrice : v.price
				if (item.skuId === v.id) {
					item.price = v.price
					item.perLimit = v.perLimit
					item.checkStock = v.stock
					item.pic = item.skuStocks.length > 1 ? v.pic : item.pic
				}
			})
			item.skuList = item.skuList ? item.skuList : []
			item.status = item.skuList.indexOf(item.skuId) === -1 ? 0 : item.status
			if (item.activityEndTime) {
				item.status =
					item.activityStatus === '1' || item.activityStatus === '0' ? 1 : 0
			}
			if (item.status === 1) {
				useList.push(item)
				if (useList.length > 0) {
					useList.forEach((item) => {
						item.isCheck = Boolean(item.selectStatus)
					})
					selectAll = useList.every((item) => {
						return item.isCheck === true
					})
				}
			} else {
				noList.push(item)
			}
		})

		this.shopCarList = useList
		this.temShopList = JSON.parse(JSON.stringify(useList))
		this.shopLoseCarList = noList
		this.allPrice = '0.00'
		this.isPhone11 = isPhone11
		this.shopType = shopType
		this.selectAll = selectAll
		this.pageHeight = v.windowHeight
		this.dealSelct()
		this.getAllPrice()
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 选中商品操作
	 * @param {*}
	 * @returns {*}
	 */

	dealSelct(): void {
		const selectList = this.getSelectList()
		this.selectList = selectList
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 获得当前选中购物车价格
	 * @param {GoodInfo} item
	 * @returns {*}
	 */

	dealPrice(item: GoodInfo): GoodInfo {
		item.skuStocks.forEach((v: ApiSkuType, i: number) => {
			if (v.id === item.skuId) {
				item.actPrice =
					item.skuStocks[i].price === 0
						? item.skuStocks[i].originalPrice
						: item.skuStocks[i].price
				item.skuName = item.skuStocks[i].specs
			}
		})
		return item
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 左侧选择商品
	 * @param {*} e
	 * @returns {*}
	 */

	onChange(e: { currentTarget: { dataset: { index: number } } }): void {
		if (this.canClick) {
			return
		}
		const id = [] as any
		const currentIndex = e.currentTarget.dataset.index
		const shopCarList = this.shopCarList
		const selectList: GoodInfo[] = []
		const currentItem = shopCarList[currentIndex]

		currentItem.isCheck = !currentItem.isCheck
		id.push(currentItem.skuId)
		const selectStatus = currentItem.selectStatus
		this.canClick = true
		uni.showLoading({
			title: '加载中'
		})
		changeType(selectStatus === 1 ? 0 : 1, id).then(() => {
			setTimeout(() => {
				uni.hideLoading()
				this.canClick = false
			}, 100)
		})
		const selectAll = shopCarList.every((item) => item.isCheck)
		shopCarList.forEach((item) => {
			if (item.isCheck) {
				selectList.push(item)
			}
		})
		this.shopCarList[currentIndex].isCheck = currentItem.isCheck
		this.selectAll = selectAll
		this.selectList = selectList

		this.getAllPrice()
	}

	/**
	 * 商品组件上点击规格
	 */
	/**
	 * @LastEditors: chuyinlong
	 * @description:
	 * @param {*} e
	 * @returns {*}
	 */

	getSku(e: { id: number; skuId: number }): void {
		this.$STORE.setStore.setTabVisible(false)
		this.$STORE.setStore.setFormShopCar(true)
		const id = e.skuId

		let norms: ApiSkuType = {} as ApiSkuType
		this.shopCarList.forEach((item) => {
			if (item.skuId === id) {
				this.goodDetail = item
			}
		})
		this.goodDetail.skuStocks.forEach((skuItem) => {
			skuItem.getType = false
			skuItem.maxNum =
				skuItem.perLimit < skuItem.stock ? skuItem.perLimit : skuItem.stock
			if (skuItem.id === id) {
				skuItem.getType = true
				norms = skuItem
				if (String(this.goodDetail.actPrice) !== '0.00') {
					norms.price = this.goodDetail.actPrice
						? this.goodDetail.actPrice
						: this.goodDetail.price
						? this.goodDetail.price
						: '0.00'
				} else {
					norms.price = this.goodDetail.price ? this.goodDetail.price : '0.00'
				}
			}
		})
		this.showType = true
		this.goodDetail
		this.norms = norms
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 关闭规格选择弹窗
	 * @param {*}
	 * @returns {*}
	 */
	closeSku(): void {
		this.showType = false
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 点击选择规格
	 * @param {ApiSkuType} check
	 * @returns {*}
	 */
	checkNorms(check: ApiSkuType): void {
		this.goodDetail.skuStocks.forEach((item) => {
			item.getType = false
			if (item.id === check.id) {
				item.getType = true
			}
		})
		this.setData({
			norms: check,
			goodDetail: Object.assign({}, this.goodDetail)
		})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 删除商品
	 * @param {number} e
	 * @returns {*}
	 */

	async delGood(e: number): Promise<void> {
		const itemIndex = e
		const apiShoppingCartDtos: GoodInfo[] = []
		apiShoppingCartDtos.push(this.shopCarList[itemIndex])
		Dialog.confirm({
			title: '删除商品',
			message: '确认删除此商品吗，删除后可再次添加购物车'
		}).then(() => {
			delCarsList(apiShoppingCartDtos)
				.then(() => {
					uni.showToast({
						title: '删除成功',
						icon: 'none'
					})
				})
				.then(() => {
					this.getList()
				})
		})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 编辑开关
	 * @param {*}
	 * @returns {*}
	 */

	eidtList(): void {
		this.editFlag = !this.editFlag
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 修改购物车商品数量
	 * @param {*} e
	 * @returns {*}
	 */

	changeNum(e: { code: number; index: number; num: number }): void {
		if (e.code === 100) {
			uni.showToast({
				title: '已达限购数量',
				icon: 'none'
			})
			return
		}

		if (e.code === 200) {
			uni.showToast({
				title: '库存不足',
				icon: 'none'
			})
			return
		}

		const currentIndex = e.index
		const currentItem = this.shopCarList[currentIndex]
		const oldItem = this.temShopList[currentIndex]
		currentItem.goodsNumber = e.num

		this.editShopCar(currentItem, oldItem, 0)
		if (currentItem.isCheck) {
			this.getAllPrice()
		}
		this.shopCarList[currentIndex].goodsNumber = currentItem.goodsNumber
		this.$emit('shopCarChange', {})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 购物车重新修改规格
	 * @param {*} e
	 * @returns {*}
	 */

	addShopCar(e: { goodDetail: SelectGoodInfo; index: number }): void {
		const currentItem = e.goodDetail
		let oldItem: GoodInfo = {} as GoodInfo
		this.shopCarList.forEach((item, index) => {
			if (item.itemIndex === currentItem.itemIndex) {
				oldItem = this.temShopList[index]
			}
		})
		this.editShopCar(currentItem, oldItem, 1)
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 编辑购物车商品
	 * @param {GoodInfo} newItem
	 * @param {GoodInfo} oldItem
	 * @param {number} type
	 * @returns {*}
	 */

	editShopCar(newItem: GoodInfo, oldItem: GoodInfo, type: number): void {
		const params = {
			oldApiShoppingCartDto: {
				...oldItem
			},
			newApiShoppingCartDto: {
				...newItem
			}
		}
		editCarsList(params)
			.then(() => {
				this.showType = false
				this.$STORE.setStore.setTabVisible(true)
				this.$emit('shopCarChange')
				if (type) {
					this.getList()
				}
			})
			.catch((err) => {
				uni.showToast({
					icon: 'none',
					title: `${err}`
				})
			})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 清空失效商品
	 * @param {*}
	 * @returns {*}
	 */

	emptyUseless(): void {
		const ids: number[] = []
		this.shopLoseCarList.forEach((item) => {
			ids.push(item.skuId)
		})
		Dialog.confirm({
			title: '清空商品',
			message: '确认清空失效商品吗'
		})
			.then(() => {
				emptyCarsList(ids).then(() => {
					uni.showToast({
						icon: 'none',
						title: '清空成功'
					})
					this.getList()
				})
			})
			.catch((err) => {
				console.log(err)
			})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 完成编辑
	 * @param {*}
	 * @returns {*}
	 */

	eidtFinish(): void {
		this.editFlag = false
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 全选/取消全选
	 * @param {*}
	 * @returns {*}
	 */

	chooseAll(): void {
		const shopCarList = this.shopCarList
		const selectList: GoodInfo[] = []
		let selectAll = this.selectAll
		if (this.shopCarList.length === 0) {
			return
		}
		selectAll = !selectAll
		shopCarList.forEach((item) => {
			item.isCheck = selectAll
			if (item.isCheck) {
				selectList.push(item)
			}
		})
		this.shopCarList = shopCarList
		this.selectAll = selectAll
		this.selectList = selectList
		this.getAllPrice()
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 删除购物车
	 * @param {*}
	 * @returns {*}
	 */

	deleteButton(): void {
		const selectList = this.getSelectList()
		if (selectList.length === 0) {
			uni.showToast({
				title: '请先选择要删除商品',
				icon: 'none'
			})
			return
		}
		if (this.isAddCollect) {
			delCarsList(selectList).then(() => {
				this.isAddCollect = false
				this.getList()
			})
		} else {
			Dialog.confirm({
				title: '删除商品',
				message: '确认删除选中商品吗，删除后可再次添加购物车'
			}).then(() => {
				delCarsList(selectList).then(() => {
					uni.showToast({
						icon: 'none',
						title: '删除成功'
					})
					this.getList()
					this.$STORE.userStore.getShopCarNum()
				})
			})
		}
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 统计总价
	 * @param {*}
	 * @returns {*}
	 */

	getAllPrice(): void {
		const allPrice = this.shopCarList.reduce((prev, current: any) => {
			const currentPrice = current.isCheck
				? current.price * current.goodsNumber
				: 0
			return prev + currentPrice
		}, 0)
		this.allPrice = allPrice.toFixed(2)
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 返回所有选中商品
	 * @param {*}
	 * @returns {GoodInfo[]}
	 */

	getSelectList(): GoodInfo[] {
		return this.shopCarList.filter((item) => item.isCheck)
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 返回首页
	 * @param {*}
	 * @returns {*}
	 */

	gotoHome(): void {
		this.$STORE.setStore.backHome()
	}
}
</script>
<style lang="scss" scoped>
@include b(shop) {
	$p-l: 10px;

	@include e(car) {
		background: $main-background;
		min-height: 650px;
		padding: 0px 0 80px;

		@include m(control) {
			background: #fff;
			font-size: 14px;
			padding: 0px $p-l 0;
			margin-bottom: -1px;
		}

		@include m(container) {
			@include flex(space-between);
			padding-top: 10px;
			width: 100%;
			border-bottom: 1px solid $main-border-color;
			padding-bottom: 10px;
			background-color: white;
			border-radius: 10px;
			margin-bottom: 10px;

			@include m(item) {
				display: flex;
				align-items: center;
				padding-left: 10px;
			}
		}

		@include m(unable) {
			width: 90px;
			height: 90px;
			border-radius: 50px;
			background-color: rgba(0, 0, 0, 0.3);
			position: absolute;
			left: 42px;
			@include flex(center, center);
			border: 2px solid white;
		}

		@include m(goods) {
			width: 100%;
			padding: 10px;
			padding-top: 65px;
			padding-bottom: 6px;
		}

		.clear {
			color: $main-light-color;
		}
	}
}

.topSelect {
	display: flex;
	font-size: 14px;
}

.topSelect__left {
	display: flex;
	align-items: center;
	justify-content: center;
	width: 80px;
	height: 35px;
	border: 1px solid #ff4444;
	border-top-left-radius: 50px;
	border-bottom-left-radius: 50px;
	border-right: none;
}

.topSelect__right {
	display: flex;
	align-items: center;
	justify-content: center;
	width: 80px;
	height: 35px;
	border: 1px solid #ff4444;
	border-top-right-radius: 50px;
	border-bottom-right-radius: 50px;
	border-left: none;
}

.shop__car--top {
	position: fixed;
	top: 0px;
	width: 100%;
	z-index: 10;
}

.shop__car--top--container {
	padding: 0px 20px;
	display: flex;
	justify-content: flex-end;
	align-items: center;
	padding-top: 10px;
	border-bottom: 1px solid $main-border-color;
	padding-bottom: 10px;
	background-color: white;
	border-radius: 0px;
}

.shopNoCar {
	display: flex;
	justify-content: center;
	flex-wrap: wrap;
	width: 100%;
	background-color: white;
}

.shop__noCar {
	margin-top: 70px;
	width: 69%;
}

.shop__noCar--text {
	display: flex;
	justify-content: center;
	color: #acacac;
	width: 100%;
}

.shop__noCar--buyButton {
	display: flex;
	justify-content: center;
	align-items: center;
	width: 120px;
	height: 40px;
	border-radius: 50px;
	color: #e4322c;
	border: 1px solid #e4322c;
	margin-top: 20px;
	font-size: 14px;
}

.submit {
	bottom: 40px;
}

@include b(pageBottom) {
	position: fixed;
	bottom: calc(88rpx + constant(safe-area-inset-bottom));
	bottom: calc(88rpx + env(safe-area-inset-bottom));
	z-index: 10;
	width: 100%;
	height: 47px;
	background-color: #fff;
	display: flex;
	align-items: center;
	justify-content: space-between;
	padding-left: 10px;

	@include e(check) {
		width: 62%;
		display: flex;
		align-items: center;
		justify-content: space-between;
		font-size: 17px;
		height: 100%;

		@include m(allPrice) {
			height: 100%;
			display: flex;
			align-items: center;

			@include m(price) {
				margin-left: 10px;
				color: #ff4444;
				font-size: 15px;
				font-weight: bold;
			}
		}
	}

	@include e(button) {
		width: 34%;
		height: 100%;
		background-color: #ff4444;
		@include flex(center, center);
		color: white;
		font-size: 17px;
	}

	@include e(delBtn) {
		width: 60px;
		height: 27px;
		display: -webkit-box;
		display: flex;
		-webkit-box-pack: center;
		justify-content: center;
		-webkit-box-align: center;
		align-items: center;
		font-size: 14px;
		border: 1px solid #ff4444;
		color: #ff4444;
		border-radius: 50px;
		margin-right: 20px;
	}
}

.moveCollect {
	margin-right: 20px;
	width: 75px;
	height: 27px;
	border: 1px solid #ff4444;
	color: #ff4444;
	border-radius: 50px;
	font-size: 12px;
	display: flex;
	justify-content: center;
	align-items: center;
}
</style>
