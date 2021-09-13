<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-12 15:36:21
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-09-01 16:03:21
 * 123
-->
<template>
	<view>
		<view class="footerBox" v-if="list.length > 0">
			<view
				v-for="(item, listIndex) in list"
				:key="listIndex"
				:data-index="listIndex"
				:data-list="List"
			>
				<view class="listTime">
					<view class="listDateCheck" v-if="isDeal">
						<van-checkbox
							:value="item.isDataCheck"
							@change="dataOnChange"
							checked-color="#FD4D63"
							icon-size="18px"
							:data-index="listIndex"
						></van-checkbox>
					</view>
					{{ item.date }}
				</view>
				<view class="box">
					<view
						v-for="(good, index) in item.shopList"
						:key="index"
						class="shopList"
						:data-index="listIndex"
						:data-shopindex="index"
						@longpress="deleteFootprint"
						@tap.stop="toProductDetail"
						style="position: relative"
					>
						<view class="listCheck" v-if="isDeal">
							<van-checkbox
								:value="good.isCheck || false"
								@change="onChange"
								icon-size="18px"
								checked-color="#FD4D63"
								:data-index="listIndex"
								:data-shopindex="index"
							></van-checkbox>
						</view>
						<view style="width: 102px;height: 102px;padding-left: 10px;">
							<image
								:src="good.productPic"
								lazy-load
								mode="aspectFill"
								style="width:102px;height:102px"
							></image>
							<view class="listContent__imgUseless" v-if="good.status === 0"
								>失效</view
							>
						</view>
						<view class="longDelate" v-if="good.isDelete">
							<view
								class="longDelate__centerDelete"
								@tap.stop="deleteSure"
								:data-index="listIndex"
								:data-shopindex="index"
							>
								删除
							</view>
						</view>
						<view class="listContent">
							<view class="product__name">{{ good.productName }}</view>
							<view class="price__line">
								<view class="product__price">
									<span>{{ good.intPrice }}</span>
									<span style="font-weight:weight;font-size:15px;"
										>.{{ good.smaPrice }}</span
									>
								</view>
								<view
									class="product__oriPrice"
									v-if="good.productPrice !== '0.00'"
								>
									{{ good.originalPrice }}
								</view>
							</view>
						</view>
					</view>
				</view>
			</view>
		</view>
		<view style="height:90px" class="tipNoMore" v-if="list.length > 0"
			>-------没有更多了-------</view
		>
		<view class="footerBox__bottom" v-if="list.length > 0">
			<view v-if="!isDeal" class="footerBox__bottom--choose">
				<view @tap="cleanAll">一键清空</view>
				<view @tap="editAll">编辑</view>
			</view>
			<view v-else class="footerBox__bottom--choose">
				<view @tap.stop="checkAllDeal" v-if="!isAllCheck" data-type="true"
					>全选</view
				>
				<view @tap.stop="checkAllDeal" v-if="isAllCheck" data-type="false"
					>取消全选</view
				>
				<view @tap.stop="cancelDeal">取消</view>
				<view style="color:#FE4E63" @tap.stop="checkAllDel">删除</view>
			</view>
		</view>
		<view
			v-if="list.length === 0"
			:style="'height:' + height + 'px'"
			class="noReviews"
		>
			<view class="noReviews__center">
				<image
					src="http://medusa-small-file.oss-cn-hangzhou.aliyuncs.com/api/footmark.png"
					class="noReviews__center--img"
				></image>
				<view class="noReviews__center--text">暂无足迹~</view>
			</view>
			<view class="noCollect__button" @tap="gotoIndex">去逛一逛</view>
		</view>
		<van-dialog id="van-dialog"></van-dialog>
	</view>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator'
import { userFooterList, userFooterDel } from '@/api/modules/goods'
import { FooterItemShopSet, FooterItemType } from './footerType'
import Dialog from '@/wxcomponents/vant-weapp/dialog/dialog'

@Component({})
export default class Collect extends Vue {
	list: FooterItemType[] = []
	isDeal = false
	isAllCheck = false
	query = {
		current: 1,
		size: 10
	}
	hasMore = false
	height = 0

	onShow() {
		this.getFooterList()
		uni.getSystemInfo({
			success: (res) => {
				this.setData({
					height: res.windowHeight
				})
			}
		})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 获取足迹商品
	 * @param {*} query
	 * @param {*} type
	 */

	getFooterList(
		query: { current: number; size: number } = { current: 1, size: 10 },
		type: boolean = false
	) {
		if (this.hasMore) return
		userFooterList(Object.assign({}, this.query, query)).then((res) => {
			const list = this.list
			let newList: FooterItemShopSet[] = []
			let dates: string[] = []
			if (res.length > 0) {
				res.forEach((v: string) => {
					const vList: FooterItemShopSet[] = JSON.parse(v)
					vList.forEach((vItem: FooterItemShopSet) => {
						vItem.originalPrice = Number(vItem.originalPrice).toFixed(2)
						vItem.productPrice = Number(vItem.productPrice).toFixed(2)
						vItem.intPrice =
							vItem.productPrice !== '0.00'
								? vItem.productPrice.split('.')[0]
								: vItem.originalPrice.split('.')[0]
						vItem.smaPrice =
							vItem.productPrice !== '0.00'
								? vItem.productPrice.split('.')[1]
								: vItem.originalPrice.split('.')[1]
					})
					newList = newList.concat(vList)
				})
			}
			this.hasMore = res.length === this.query.size ? false : true
			dates = [
				...new Set(
					newList.map((v) => {
						return v.time
					})
				)
			]
			if (list.length > 0 && list[list.length - 1].date === dates[0]) {
				list[list.length - 1].shopList = list[list.length - 1].shopList.concat(
					newList.filter((v) => {
						return v.time === dates[0]
					})
				)
				dates.shift()
			}
			dates.forEach((v) => {
				list.push({
					date: v,
					isDataCheck: false,
					shopList: newList.filter((m) => {
						return m.time === v
					})
				})
			})
			if (type && this.isAllCheck && this.isDeal) {
				list.forEach((item) => {
					item.isDataCheck = true
					item.shopList.forEach((v) => {
						v.isCheck = true
					})
				})
			}
			this.setData({
				list,
				hasMore: this.hasMore
			})
		})
	}

	onReachBottom() {
		this.setData({
			'query.current': ++this.query.current
		})
		this.getFooterList({ current: 1, size: 10 }, true)
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 选择时间
	 * @param {*} e
	 */

	dataOnChange(e: {
		currentTarget: { dataset: { index: number } }
		detail: boolean
	}) {
		const outIndex = e.currentTarget.dataset.index
		const list = this.list
		list.forEach((item, index) => {
			if (index === outIndex) {
				item.isDataCheck = !item.isDataCheck
				item.shopList.map((v) => (v.isCheck = e.detail))
			}
		})
		this.checkout()
		this.list = [...list]
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 商品选择
	 * @param {*} e
	 */

	onChange(e: {
		currentTarget: { dataset: { index: number; shopindex: number } }
	}) {
		const index = e.currentTarget.dataset.index
		const shopindex = e.currentTarget.dataset.shopindex
		let list = this.list
		list[index].shopList[shopindex].isCheck = !list[index].shopList[shopindex]
			.isCheck
		list.forEach((item) => {
			item.isDataCheck = item.shopList.every((v) => {
				return v.isCheck === true
			})
		})
		this.checkout()
		this.list = [...list]
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 检查是否有全选
	 */

	checkout() {
		const list = this.list
		const isAllCheck = list.every((v) => {
			return v.isDataCheck === true
		})
		this.setData({
			isAllCheck
		})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description:长按触发
	 * @param {*} e
	 */

	deleteFootprint(e: {
		currentTarget: { dataset: { index: number; shopindex: number } }
	}) {
		const index = e.currentTarget.dataset.index
		const shopindex = e.currentTarget.dataset.shopindex
		const list = this.list
		list.forEach((v) => {
			v.shopList.forEach((item) => {
				item.isDelete = false
			})
		})
		this.setData({
			list
		})
		if (this.isDeal) {
			return
		}

		list[index].shopList[shopindex].isDelete = true
		this.setData({
			list
		})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 点击操作
	 * @param {*} e
	 */

	toProductDetail(e: {
		currentTarget: { dataset: { index: number; shopindex: number } }
	}) {
		const index = e.currentTarget.dataset.index
		const shopindex = e.currentTarget.dataset.shopindex
		const list = this.list
		list.forEach((item) => {
			item.shopList.map((v) => {
				return (v.isDelete = false)
			})
		})
		this.setData({
			list
		})
		const id = list[index].shopList[shopindex].productId
		if (!this.isDeal) {
			uni.navigateTo({
				url: '/subcontract/pages/detail/detail?id=' + id
			})
		}
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 删除操作
	 * @param {*} e
	 */

	deleteSure(e: {
		currentTarget: { dataset: { index: number; shopindex: number } }
	}) {
		const index = e.currentTarget.dataset.index
		const shopindex = e.currentTarget.dataset.shopindex
		const list = this.list
		const footmarkIds = [] as any
		footmarkIds.push(list[index].shopList[shopindex].footmarkId)
		Dialog.confirm({
			title: '提示',
			message: '确认删除此商品记录吗'
		})
			.then(() => {
				userFooterDel(0, footmarkIds).then(() => {
					const query = {
						current: 1,
						size: this.query.size * this.query.current
					}
					this.setData({
						list: [],
						hasMore: false
					})
					this.getFooterList(query)
					uni.showToast({
						title: '删除成功',
						icon: 'none'
					})
				})
			})
			.catch(() => {
				list[index].shopList[shopindex].isDelete = false
				this.setData({
					list
				})
			})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 一键清空
	 */

	cleanAll() {
		Dialog.confirm({
			title: '提示',
			message: '确认清空商品浏览记录吗'
		}).then(() => {
			userFooterDel(1, [-1]).then(() => {
				this.getFooterList()
				this.setData({
					list: []
				})
			})
		})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 编辑
	 */

	editAll() {
		const list = this.list
		list.forEach((item) => {
			item.shopList.map((v) => {
				return (v.isDelete = false)
			})
		})
		this.setData({
			list,
			isDeal: true
		})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 全选与取消全选
	 */

	checkAllDeal() {
		this.list.forEach((item) => {
			item.isDataCheck = !item.isDataCheck
			item.shopList.forEach((v) => {
				v.isCheck = !v.isCheck
			})
		})
		this.checkout()
		this.setData({
			list: this.list
		})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 取消清除
	 */

	cancelDeal() {
		this.list.forEach((item) => {
			item.isDataCheck = false
			item.shopList.forEach((v) => {
				v.isCheck = false
			})
		})
		this.setData({
			list: this.list,
			isDeal: false,
			isAllCheck: false
		})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 多选删除
	 */

	checkAllDel() {
		const list = this.list
		const footmarkIds = [] as any
		list.forEach((listItem) => {
			listItem.shopList.forEach((shopItem) => {
				if (shopItem.isCheck) {
					footmarkIds.push(shopItem.footmarkId)
				}
			})
		})
		if (!footmarkIds.length) {
			uni.showToast({
				icon: 'none',
				title: '请先选择删除商品'
			})
			return
		}

		Dialog.confirm({
			title: '提示',
			message: '确认删除选中商品吗'
		}).then(() => {
			userFooterDel(0, footmarkIds).then(() => {
				const query = {
					current: 1,
					size: this.query.size * this.query.current
				}
				this.setData({
					list: [],
					isDeal: false,
					hasMore: false
				})
				this.getFooterList(query)
			})
		})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 返回首页
	 */

	gotoIndex() {
		this.$STORE.setStore.backHome()
	}
}
</script>

<style lang="scss" scoped>
page {
	background-color: #f4f4f4;
}

.listTime {
	height: 35px;
	display: flex;
	align-items: center;
	padding-left: 10px;
	font-size: 13px;
}

.box {
	background-color: white;
	border-radius: 9px;
	padding: 5px;
}

.shopList {
	height: 121px;
	background-color: white;
	display: flex;
	padding: 10px 0px;
}

.listDateCheck {
	width: 40px;
	height: 100%;
	display: flex;
	align-items: center;
}

.listCheck {
	width: 40px;
	height: 100%;
	display: flex;
	align-items: center;
	padding-left: 10px;
}

.listContent {
	width: 215px;
	margin-left: 20px;
	display: flex;
	flex-wrap: wrap;
}

.listContent__imgUseless {
	width: 100px;
	height: 25px;
	position: absolute;
	bottom: 0px;
	display: flex;
	align-items: center;
	justify-content: center;
	color: white;
	font-size: 12px;
	background-color: #fea00c;
	opacity: 0.8;
}

.longDelate {
	position: absolute;
	width: 40px;
	height: 50px;
	background-color: rgba(0, 0, 0, 0.4);
	display: flex;
	justify-content: center;
	align-items: center;
	top: 0px;
	width: 100%;
	height: 100%;
}

.longDelate__centerDelete {
	display: flex;
	justify-content: center;
	width: 140rpx;
	height: 140rpx;
	background-color: #fea00b;
	border-radius: 70rpx;
	color: white;
	align-items: center;
}

.product__name {
	width: 215px;
	font-size: 26rpx;
	display: flex;
	flex-wrap: wrap;
}

.price__line {
	display: flex;
	align-items: flex-end;
}

.product__price {
	margin-top: 5px;
	font-size: 20px;
	color: #fa5555;
	margin-right: 10px;
	font-weight: bold;
}

.product__price::before {
	margin-top: 5px;
	content: '￥';
	font-size: 15px;
	font-weight: normal;
}

.product__oriPrice {
	margin-top: 5px;
	font-size: 12px;
	color: #ccc;
	font-weight: bold;
	text-decoration: line-through;
}

.product__oriPrice::before {
	margin-top: 5px;
	font-size: 12px;
	content: '￥';
}

.footerBox__bottom {
	position: fixed;
	bottom: 0px;
	width: 100%;
	height: 50px;
	background-color: white;
	display: flex;
	align-items: center;
	justify-content: space-around;
	font-size: 14px;
}

.footerBox__bottom--choose {
	display: flex;
	justify-content: space-around;
	width: 100%;
}

.tipNoMore {
	display: flex;
	justify-content: center;
	color: #d0d2d6;
	font-size: 15px;
	margin-top: 18px;
}

.noCollect {
	display: flex;
	align-items: center;
	justify-content: center;
	width: 100%;
	flex-wrap: wrap;
}

.noCollect__button {
	height: 40px;
	width: 120px;
	display: flex;
	align-items: center;
	justify-content: center;
	color: #e4322c;
	border-radius: 50px;
	border: 1px solid #e4322c;
	font-size: 14px;
	margin-top: -110px;
}

.shop__noCar {
	margin-top: 70px;
	width: 69%;
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
			margin-top: 10px;
			font-size: 16px;
		}
	}
}
</style>
