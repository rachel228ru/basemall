<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-12 15:36:21
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-09-01 16:01:15
 * 123
-->
<template>
	<view>
		<!-- 商品组件 -->
		<scroll-view
			class="scroll-view_HPage"
			scroll-x="true"
			v-if="
				formData.ponentType === 1 &&
					formData.firstCatList &&
					formData.firstCatList.length > 0
			"
		>
			<view
				v-for="(item, idx) in formData.firstCatList || []"
				:key="idx"
				:data-id="item.id"
				:data-mode="item.saleMode"
				@tap.stop="activeClass"
				:class="'tab__itam ' + (currentCategoryId === item.id ? 'active' : '')"
			>
				<view v-if="formData.titleStyle === 2" class="text new__active-class">
					{{ item.name }}
				</view>
				<view v-if="formData.titleStyle === 1" class="text">{{
					item.name
				}}</view>
				<text v-if="formData.titleStyle === 1" class="line"></text>
			</view>
		</scroll-view>
		<view
			v-if="
				(formData.ponentType === 1 &&
					formData.firstCatList &&
					formData.firstCatList.length > 0) ||
					(formData.ponentType === 2 && goodsList.length)
			"
			:class="'goods ' + formData.listStyle"
			:style="'padding: 0px ' + formData.pagePadding * 2 + 'rpx'"
		>
			<view
				v-for="(item, idx) in goodsList"
				:key="idx"
				:data-id="item.id"
				:data-img="item.img"
				@tap.stop="gotoDetail"
				:class="'goods-item ' + getGoodsBoxClass"
				:style="
					idx === goodsList.length - 1 ? getGoodsLastStyle : getGoodsItemStyle
				"
			>
				<view
					v-if="formData.showContent.tagShow"
					:class="'goods-item__coner ' + getGoodsCornerMark.class"
				>
					<image
						:src="getGoodsCornerMark.url"
						mode="aspectFill"
						:lazy-load="true"
					></image>
				</view>
				<view class="goods-item__img">
					<view class="no__goods" v-if="!item.inventory">
						<text>已售罄</text>
						<text>Sold out</text>
					</view>
					<image
						:src="item.img"
						alt
						:lazy-load="true"
						mode="aspectFill"
						style="height:100%;"
					></image>
					<!-- <image src="{{ image.transformImage(item.img, '375')}}" mode="widthFix" alt="" lazy-load="{{true}}"></image> -->
				</view>
				<view
					:class="
						'goods-item__foot ' +
							(formData.listStyle !== 'goods-style--three' ? 'add__f-p' : '')
					"
				>
					<view
						v-if="formData.showContent.nameShow"
						:class="'goods-item__name ' + getGoodsNameClass"
					>
						{{ item.name }}
					</view>
					<view
						v-if="
							formData.showContent.priceShow || formData.showContent.buttonShow
						"
						class="goods-item__bottom"
					>
						<view class="goods-item__price">
							<view class="price__one" v-if="formData.showContent.priceShow">
								{{ '￥' + item.price }}
								<text class="price__two">{{ '￥' + item.maxPrice }}</text>
							</view>
						</view>
						<view
							v-if="
								formData.showContent.buttonShow &&
									formData.showContent.buttonStyle === 1
							"
							:class="'goods-item__cart goods-item__cart1 ' + getClassBugCard3"
						>
							<image
								src="https://oss-tencent.bgniao.cn//gruul/20200710/810700b67e264a7b82bba347528b9eb0.png"
								mode="aspectFit"
								:lazy-load="true"
							></image>
						</view>
						<view
							v-if="
								formData.showContent.buttonShow &&
									formData.showContent.buttonStyle === 2
							"
							:class="'goods-item__cart goods-item__cart2 ' + getClassBugCard3"
						>
							<image
								src="https://oss-tencent.bgniao.cn//gruul/20200710/3de00f3d8099459c83771b1f4decb197.png"
								mode="aspectFit"
								:lazy-load="true"
							></image>
						</view>
						<view
							v-if="
								formData.showContent.buttonShow &&
									formData.showContent.buttonStyle === 3
							"
							class="goods-item__cart goods-item__cart3"
						>
							{{ formData.showContent.buttonText || '立即购买' }}
						</view>
						<view
							v-if="
								formData.showContent.buttonShow &&
									formData.showContent.buttonStyle === 4
							"
							class="goods-item__cart goods-item__cart4"
						>
							{{ formData.showContent.buttonText || '立即购买' }}
						</view>
					</view>
				</view>
			</view>
		</view>
	</view>
</template>

<script lang="ts">
import { Component, Vue, Prop } from 'vue-property-decorator'
import { discountList, getAllCategory } from '@/api/modules/goods'
import storage from '@/utils/storage'
import isEqual from 'lodash/isEqual'
import { getEffectCategoryGoods } from '@/api/modules/goods'
import {
	GoodProp,
	IGoodsCornerMark,
	GoodState,
	firstCatItemSec,
	firstCatItem,
	IProductList
} from './goodsType'
import md5 from 'md5'

@Component({})
export default class GoodsPonents extends Vue implements GoodState {
	@Prop({
		type: Object
	})
	decoretionProperties!: GoodProp

	formData: GoodProp = {} as GoodProp
	currentCategoryId = '-1'
	GoodsCornerMark = {
		url: '',
		class: ''
	} as IGoodsCornerMark
	goodsList: IProductList[] = []
	name = ''
	GoodsNameClass = ''
	GoodsBoxClass = ''
	ClassBugCard3 = ''
	GoodsItemStyle = ''
	GoodsLastStyle = ''
	saleMode = ''

	mounted() {
		const formData = this.decoretionProperties
		const md5Str = md5(JSON.stringify(formData))
		const storageData = storage.get(`discountList-${md5Str}`)

		if (formData.ponentType === 1) {
			this.filterCategory(formData)
		} else if (formData.ponentType === 2) {
			this.discountList(md5Str, storageData, formData)
		}
		this.getGoodsCornerMark(formData)
		this.getGoodsNameClass(formData)
		this.getGoodsBoxClass(formData)
		this.getClassBugCard3(formData)
		this.getGoodsItemStyle(formData)
		this.getGoodsLastStyle(formData)

		this.formData = formData
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 获取商品名称文本样式
	 * @param {GoodProp} formData
	 * @returns {*}
	 */

	getGoodsNameClass(formData: GoodProp): void {
		const { textStyle } = formData
		this.GoodsNameClass = textStyle
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 获取商品边框样式
	 * @param {GoodProp} formData
	 * @returns {*}
	 */

	getGoodsBoxClass(formData: GoodProp): void {
		const { goodsStyle, angle } = formData
		this.GoodsBoxClass = `${goodsStyle} ${angle}`
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 购买按钮样式
	 * @param {GoodProp} formData
	 * @returns {*}
	 */

	getClassBugCard3(formData: GoodProp): void {
		const {
			listStyle,
			showContent: { buttonStyle }
		} = formData
		let ClassBugCard3 = ''

		if (buttonStyle === 1 && listStyle === 'goods-style--four') {
			ClassBugCard3 = 'goods-buy__cart3'
		}
		if (buttonStyle === 2 && listStyle === 'goods-style--four') {
			ClassBugCard3 = 'goods-buy__cart3 buy-icon2'
		}

		this.ClassBugCard3 = ClassBugCard3
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description:
	 * @param {GoodProp} formData
	 * @returns {*}
	 */

	getGoodsItemStyle(formData: GoodProp): void {
		const { listStyle, goodPadding } = formData
		let margin = ''
		let width = ''

		if (listStyle === 'goods-style--four') {
			margin = `margin: 0px ${goodPadding}px 0px 0px;`
		} else {
			margin = `margin: 0px 0px ${goodPadding}px 0px;`
		}
		if (listStyle === 'goods-style--two') {
			width = `width: calc(50% - ${goodPadding / 2}px);`
		} else {
			width = ''
		}
		this.GoodsItemStyle = `${margin}${width}`
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 最后一个商品样式
	 * @param {GoodProp} formData
	 * @returns {*}
	 */

	getGoodsLastStyle(formData: GoodProp): void {
		const { listStyle, goodPadding = 0 } = formData
		let margin = ''
		if (listStyle === 'goods-style--two') {
			margin = `margin: 0px 0px ${goodPadding}px 0px;`
		} else {
			margin = ''
		}
		this.GoodsLastStyle = margin
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 获取商品ids
	 * @param {GoodProp} formData
	 * @returns {*}
	 */

	goodsIds(formData: GoodProp): string {
		const list = Array.isArray(formData.goods) ? formData.goods : []
		return list.map((i) => i.id).join(',')
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 获取销售专区id
	 * @param {GoodProp} formData
	 * @returns {*}
	 */

	saleModeIds(formData: GoodProp): string[] {
		const list = Array.isArray(formData.goods) ? formData.goods : []
		return Array.from(new Set(list.map((i) => i.saleMode)))
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 初始化数据，需要过滤掉不可用掉一级分类
	 * @param {GoodProp} formData
	 * @returns {*}
	 */

	async filterCategory(formData: GoodProp): Promise<void> {
		try {
			const res = await getAllCategory({})
			const list = res || []
			const { firstCatList = [] } = formData
			const value: firstCatItem[] = []
			list.forEach(
				(i: { productNumber: number; showCategoryVos: firstCatItemSec[] }) => {
					i.productNumber = i.showCategoryVos.reduce((a, b) => {
						a += Number(b.productNumber)
						return a
					}, 0)
					if (i.productNumber > 50) i.productNumber = 50
				}
			)
			firstCatList.forEach((i) => {
				const category = list.find((k: { id: string }) => k.id === i.id)
				if (category) {
					category.saleMode = i.saleMode
					value.push(category)
				}
			})
			formData.firstCatList = value
			this.currentCategoryId = value[0].id
			this.saleMode = value[0].saleMode
			this.formData = formData
			this.getAllList()
		} catch (e) {
			console.log('goods-ponents.vue--lin337', e)
		}
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 激活tab
	 * @param {id: string; mode: string;} Event
	 * @returns {*}
	 */

	activeClass(e: {
		currentTarget: { dataset: { id: string; mode: string } }
	}): void {
		const id = e.currentTarget.dataset.id
		const saleMode = e.currentTarget.dataset.mode
		;(this.currentCategoryId = id),
			(this.saleMode = saleMode),
			this.getAllList()
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description:
	 * @param {string} md5
	 * @param {IProductList[]} storageData
	 * @param {GoodProp} formData
	 * @returns {*}
	 */

	async discountList(
		md5: string,
		storageData: IProductList[],
		formData: GoodProp
	): Promise<void> {
		const ids: string = this.goodsIds(formData)
		const saleModeList = this.saleModeIds(formData)
		if (!ids) return
		let list: IProductList[] = []
		const goodsList: IProductList[] = []
		setTimeout(async () => {
			for (let i = 0; i < saleModeList.length; i++) {
				const res = await discountList(ids, {
					saleMode: saleModeList[i],
					launchArea: ''
				})
				list = list.concat(res)
				if (i === saleModeList.length - 1) {
					list.forEach((item) => {
						goodsList.push({
							id: item.id,
							img: this.returnGoodsImg(item),
							name: item.name,
							price: item.minPrice,
							maxPrice: item.maxPrice,
							inventory: item.inventory
						})
						const obj = {
							goodsList
						}
						this.dealGoodsSort(obj, ids.split(','))
						this.goodsList = obj.goodsList
					})
					if (list && !isEqual(list, storageData)) {
						storage.set(`discountList-${md5}`, list, 3 * 24 * 60 * 60 * 1000)
					}
				}
			}
		}, 100)
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 处理商品顺序
	 * @param {goodsList: IProductList[]} obj
	 * @param {string[]} ids
	 * @returns {*}
	 */

	dealGoodsSort(obj: { goodsList: IProductList[] }, ids: string[] = []): void {
		const temp: IProductList[] = []
		const goods = obj.goodsList
		ids.forEach((i: any) => {
			const e = goods.find((t: any) => t.id === i)
			e && temp.push(e)
		})
		goods.forEach((i: any) => {
			!ids.includes(i.id) && temp.push(i)
		})
		obj.goodsList = temp
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 处理商品图片
	 * @param {IProductList} item
	 * @returns {string}
	 */

	returnGoodsImg(item: IProductList): string {
		const { widePic, pic } = item
		let url = pic || ''
		if (this.formData.listStyle === 'goods-style--one') {
			url = widePic ? widePic.split(',')[0] : pic || ''
		}
		return url
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 获取商品角标地址及样式
	 * @param {GoodProp} formData
	 * @returns {*}
	 */

	getGoodsCornerMark(formData: GoodProp = this.formData): void {
		const { tagStyle } = formData.showContent
		// 商品角标 1新品 2热卖 3抢购
		const srcs = [
			'https://medusa-small-file-1253272780.cos.ap-shanghai.myqcloud.com/gruul/20201026/37a28e49acb448fc8618d5e72851b027.png',
			'https://medusa-small-file-1253272780.cos.ap-shanghai.myqcloud.com/gruul/20201026/1622a28ef72040f9a2f367a2efa7c32d.png',
			'https://medusa-small-file-1253272780.cos.ap-shanghai.myqcloud.com/gruul/20201026/0f33444422b14a8e980cc50d0011e2d0.png'
		]
		const styles = [
			'goods-item__coner1',
			'goods-item__coner2',
			'goods-item__coner3'
		]
		this.GoodsCornerMark = {
			url: srcs[+tagStyle - 1],
			class: styles[+tagStyle - 1]
		}
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 获取商品
	 * @param {*}
	 * @returns {*}
	 */

	async getAllList(): Promise<void> {
		const ids: string[] = []
		if (this.currentCategoryId === '-1') {
			this.formData.firstCatList.map((i) => {
				if (i.id !== '-1') ids.push(i.id)
			})
		} else {
			ids.push(this.currentCategoryId)
		}
		const param = {
			saleMode: this.saleMode
		}

		const res = await getEffectCategoryGoods(ids.join(','), param)
		const datas: IProductList[] = []
		res.forEach((k: { apiAliveProductVos: any[] }) => {
			k.apiAliveProductVos.forEach((sd: IProductList) => {
				if (!datas.find((t: { id: any }) => t.id === sd.id)) datas.push(sd)
			})
		})
		const goodsList: IProductList[] = []
		this.goodsList = goodsList
		datas.forEach((item: IProductList) => {
			goodsList.push({
				id: Number(item.id),
				name: item.name,
				img: this.returnGoodsImg(item),
				actPrice: Number(item.maxPrice),
				price: this.dealGoogsPrice(item.minPrice),
				maxPrice: this.dealGoogsPrice(Number(item.maxPrice)),
				soldCount: item.sale,
				inventory: item.inventory
			})
		})
		this.goodsList = goodsList.sort((a, b) => (a.name > b.name ? -1 : 1))
		const formData = this.formData
		this.getGoodsCornerMark(formData)
		this.getGoodsNameClass(formData)
		this.getGoodsBoxClass(formData)
		this.getClassBugCard3(formData)
		this.getGoodsItemStyle(formData)
		this.getGoodsLastStyle(formData)
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 处理商品价格
	 * @param {number} minPrice
	 * @returns {*}
	 */

	dealGoogsPrice(minPrice: number = 0): string {
		let price = String(minPrice)
		if (isNaN(minPrice)) {
			price = '0'
		} else {
			if (minPrice >= 10000 && minPrice < 100000000) {
				price = `${parseFloat((minPrice / 10000).toFixed(2))}万`
			} else if (minPrice > 100000000) {
				price = `${parseFloat((minPrice / 100000000).toFixed(2))}亿`
			}
		}
		return price
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 进入商品详情
	 * @param {*} event
	 * @returns {*}
	 */

	gotoDetail(e: {
		currentTarget: { dataset: { id: string | number; img: string } }
	}) {
		const { id, img } = e.currentTarget.dataset
		uni.navigateTo({
			url: `/subcontract/pages/detail/detail?id=${id}&img=${img}`
		})
	}
}
</script>

<style lang="scss" scoped>
@include b(goods-item) {
	position: relative;
	background-color: #fff;

	@include when(circle) {
		border-radius: 6px;
		overflow: hidden;
	}

	@include when(shadow) {
		box-shadow: 0px 1px 56px 6px rgba(109, 109, 109, 0.1);
	}

	@include when(border) {
		border: 1px solid #eeeeee;
	}

	@include e(img) {
		height: 180px;
		background-color: #eeeeee;
		overflow: hidden;
		position: relative;

		image {
			height: 180px;
			width: 100%;
		}
	}

	@include e(name) {
		overflow: hidden;
		text-overflow: ellipsis;
		padding-top: 10rpx;
		-webkit-line-clamp: 2;
		word-wrap: break-word;
		white-space: normal !important;
		-webkit-box-orient: vertical;
		display: -webkit-box;
		height: 100rpx;
		line-height: 45rpx;

		@include when(bold) {
			font-weight: 800;
		}
	}

	@include e(foot) {
		padding: 0 7px;
		position: relative;

		.is-bold {
			font-weight: 800;
		}
	}

	.add__f-p {
		padding-bottom: 15px;
	}

	@include e(bottom) {
		display: flex;
		justify-content: space-between;
		height: 35px;
		padding: 3px 0;
		margin-top: 10px;
	}

	@include e(price) {
		line-height: 37px;
		flex: 1;

		view {
			display: inline-block;
			line-height: 37px;
			height: 37px;
		}

		.price__one {
			font-size: 36rpx;
			color: #dd3c2b;
			font-weight: bold;
		}

		.price__two {
			text-decoration: line-through;
			color: #bbbbbb;
			font-size: 10px;
			margin-left: 8px;
		}
	}

	@include e(icon) {
		display: inline-block;
		width: 28px;
		height: 28px;
		line-height: 28px;
		text-align: center;
		background-color: red;
		border-radius: 100%;
		color: #ffffff;
	}

	@include e(cart1) {
		float: right;
		height: 30px;
		width: 30px;
		background-color: rgba(252, 98, 63, 1);
		box-sizing: border-box;
		border-radius: 50%;
		display: flex;
		justify-content: center;
		align-items: center;
		margin-top: 5px;
	}

	@include e(cart) {
		float: right;
		height: 30px;
		width: 30px;
		box-sizing: border-box;
		border-radius: 50%;
		display: flex;
		justify-content: center;
		align-items: center;
		margin-top: 17px;

		image {
			display: inline-block;
			width: 20px;
			height: 20px;
		}
	}

	@include e(cart1) {
		background-color: #fff;

		image {
			display: inline-block;
			width: 30px;
			height: 30px;
		}
	}

	@include e(cart2) {
		background: linear-gradient(
			164deg,
			rgba(243, 243, 243, 1),
			rgba(229, 56, 46, 1),
			rgba(253, 78, 38, 1)
		);
		box-shadow: 0px 2px 7px 0px rgba(255, 14, 0, 0.27);
		border-radius: 50%;
	}

	@include e(cart3) {
		border: 1px solid rgba(252, 98, 63, 1);
		width: auto;
		padding: 0 5px;
		color: rgba(252, 98, 63, 1);
		font-size: 12px;
		border-radius: 20px;
		height: 25px;
		line-height: 25px;
	}

	@include e(cart4) {
		border: 1px solid rgba(252, 98, 63, 1);
		background-color: rgba(252, 98, 63, 1);
		width: auto;
		padding: 0 5px;
		color: #fff;
		font-size: 12px;
		border-radius: 20px;
		height: 25px;
		line-height: 25px;
	}

	@include e(coner) {
		position: absolute;
		z-index: 12;

		image,
		text {
			display: block;
			width: 100%;
			height: 100%;
			position: absolute;
		}
	}

	@include e(coner1) {
		left: -1px;
		top: 6px;
		width: 38px;
		height: 22px;
		z-index: 7;
	}

	@include e(coner2) {
		left: 0px;
		top: 0px;
		width: 38px;
		height: 41px;
	}

	@include e(coner3) {
		left: 8px;
		top: 7px;
		width: 42px;
		height: 21px;
	}

	@include e(delivery) {
		padding-top: 2px;

		.i_box {
			border: 1px solid rgba(233, 56, 38, 1);
			border-radius: 17px;
			color: #e93826;
			font-size: 11px;
			display: inline-block;
			padding: 0 6px;
		}

		.base_info {
			padding: 1px 6px;
			background-color: #e93826;
			color: #fff;
			border-radius: 17px;
			display: none;
		}

		.hasTip {
			padding-left: 0px;

			.base_info {
				display: inline-block;
				margin-right: 1px;
			}
		}
	}

	.goods-item__delivery3 {
		transform: scale(0.76);
		position: absolute;
		left: 2px;
		top: 100rpx;
	}

	.goods-buy__cart3 {
		width: 17px;
		height: 18px;
	}

	.buy-icon2 {
		margin-top: 8px;

		image {
			width: 38rpx;
			height: 38rpx;
		}
	}
}

@include b(goods-style) {
	@include m(two) {
		display: flex;
		flex-wrap: wrap;
		justify-content: space-between;

		@include b(goods-item) {
			width: 49%;
		}
	}

	@include m(three) {
		@include b(goods-item) {
			padding: 5px 0;
			display: flex;
			width: 100%;

			@include e(img) {
				height: 135px;
				width: 135px;
				margin-right: 10px;
				flex: none;
			}

			@include e(foot) {
				flex: 1;
				min-width: 0;
				display: flex;
				flex-direction: column;
				justify-content: space-between;
			}

			@include e(name) {
				overflow: hidden;
				text-overflow: ellipsis;
				white-space: nowrap;
			}
		}
	}

	@include m(four) {
		white-space: nowrap;
		overflow-x: auto;

		@include b(goods-item) {
			display: inline-block;
			width: 135px;

			@include e(img) {
				height: 135px;
				width: 135px;
				margin-right: 10px;
			}

			@include e(foot) {
			}

			@include e(name) {
				overflow: hidden;
				text-overflow: ellipsis;
				white-space: nowrap;
			}
		}
	}
}

@include b(goods) {
	box-sizing: border-box;
}

.no__goods {
	position: absolute;
	left: 50%;
	top: 50%;
	margin-left: -88rpx;
	margin-top: -88rpx;
	z-index: 11;
	width: 176rpx;
	height: 176rpx;
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

.goods-style--one {
	.goods-item__cart {
		margin-top: 5rpx;
	}
}

.goods-style--two {
	.goods-item__bottom {
		height: 35px;
	}

	.goods-item__cart {
		margin-top: 5rpx;
	}
}

.goods-style--three {
	.goods-item__cart {
		margin-top: 5rpx;
	}

	.goods-item__foot {
		position: relative;
	}

	.goods-item__delivery {
		position: absolute;
		left: 15rpx;
		top: 105rpx;
	}

	.goods-item__bottom {
		height: 86rpx;
	}

	.goods-item__name {
		padding-top: 18rpx;
		line-height: 45rpx;
	}
}

.goods-style--four {
	.goods-item__bottom {
		height: 80rpx;
		margin-top: 25px;
	}

	.goods-item__cart {
		margin-top: 10rpx;
	}

	.goods-buy__cart3 {
		width: 30px;
		height: 30px;
	}
}

.scroll-view_HPage {
	width: 100%;
	height: 90rpx;
	background-color: #fff;
	margin-bottom: 8px;
	white-space: nowrap;

	.tab__itam {
		display: inline-block;
		position: relative;
		height: 80rpx;
		padding: 0rpx 22rpx;
		font-size: 16px;
		font-family: Hiragino Sans GB;
		font-weight: normal;
		color: #6b6b6b;
		padding-top: 12rpx;

		.line {
			display: none;
		}

		.text {
			z-index: 8;
			white-space: nowrap;
		}
	}

	.active {
		font-size: 18px;
		font-family: Hiragino Sans GB;
		font-weight: normal;
		color: #000000;

		.new__active-class {
			z-index: 8;
			white-space: nowrap;
			display: inline-block;
			padding-bottom: 2px;
			border-bottom: 2px solid red;
			font-weight: bold;
		}

		.line {
			display: block;
			float: left;
			width: 100%;
			background: linear-gradient(164deg, #f3f3f3, #e5382e, #fd4e26);
			border-radius: 4rpx;
			margin-top: -20rpx;
			height: 18rpx;
		}
	}
}
</style>
