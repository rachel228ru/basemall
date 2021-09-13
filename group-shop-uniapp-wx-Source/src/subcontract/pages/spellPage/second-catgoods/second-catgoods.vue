<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-12 15:36:21
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-20 11:01:09
 * 123
-->
<template>
	<view class="second__cat-page">
		<view class="header__box">
			<view class="search__box">
				<search-plugin
					:chanel="chanel"
					style="flex: 1;"
					:queryName="queryName"
				></search-plugin>
				<view class="icon__box" @tap="changeStatus">
					<image
						class="icon__image"
						src="https://oss-tencent.bgniao.cn//gruul/20200626/26bffe66093f4086b501a2fce01e9388.png"
						alt
					></image>
					<view class="change__item">{{
						secondCatGoodsStyle === 1 ? '矩形' : '列表'
					}}</view>
				</view>
			</view>
		</view>
		<view class="tab__con">
			<view
				v-for="(i, d) in tabList"
				:key="d"
				:class="'tab__item ' + (activeTab === d + 1 ? 'active__tab' : '')"
				@tap="changeActiveTab"
				:data-index="d + 1"
			>
				{{ i }}
				<view class="price__icon" v-if="d === 3">
					<van-icon
						:class="
							'price__icon_up ' + (isPrice === 'asc' ? 'active__icon' : '')
						"
						name="arrow-up"
					></van-icon>
					<van-icon
						:class="
							'price__icon_down ' + (isPrice === 'desc' ? 'active__icon' : '')
						"
						name="arrow-down"
					>
					</van-icon>
				</view>
			</view>
		</view>
		<view class="list__page" v-if="goodsList.length">
			<second-goodslist
				@loadMored="loadMored"
				v-if="secondCatGoodsStyle === 1"
				:goodsList="goodsList"
				:cartButton="cartButton"
			></second-goodslist>
			<second-goodsdetail
				@loadMored="loadMored"
				v-if="secondCatGoodsStyle === 2"
				:goodsList="goodsList"
				:cartButton="cartButton"
			></second-goodsdetail>
		</view>
		<view v-if="!goodsList.length" class="shopNoCar">
			<image
				src="http://medusa-small-file.oss-cn-hangzhou.aliyuncs.com/api/noCar.png"
				class="shop__noCar"
			></image>
			<view class="shop__noCar--text">暂无商品~</view>
		</view>
	</view>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator'
import { getGoodsList, searchGoodsList } from '@/api/modules/goods'
import SecondGoodslist from '../components/second-goodslist/second-goodslist.vue'
import SecondGoodsdetail from '../components/second-goodsdetail/second-goodsdetail.vue'
import SearchPlugin from '../components/search-plugin/search-plugin.vue'
import { ApiClassifyProduct } from '@/pages/index/modules/mall/components/left-biggoods/leftBigType'

interface ISearchForm {
	current: number
	size: number
	type?: number
	name?: string
	showCategoryId?: string
	sort?: string
}

@Component({
	components: {
		SecondGoodslist,
		SecondGoodsdetail,
		SearchPlugin
	}
})
export default class SecondCatgoods extends Vue {
	/** 当前激活tab */
	activeTab: number = 1
	/** tab列表 */
	tabList: string[] = ['综合', '销量', '新品', '价格']
	/** 列表状态 */
	secondCatGoodsStyle: number = 1 // 1矩形  2列表
	showCategoryId: string = ''
	cartButton: number = 3
	searchForm: ISearchForm = {
		// 获取商品时搜索对象
		current: 1,
		size: 10
	}
	hasMore: boolean = true // 是否还有更多
	goodsList: Array<ApiClassifyProduct> = []
	chanel: number = 0 //  2商超分类页  4首页搜索组件  12商超分类页二级分类导航
	queryName: string = '' // 搜索关键词
	isPrice: string = '' // asc升序 desc降序
	couponId: string = ''

	get saleMode() {
		return this.$STORE.setStore.saleMode
	}

	onLoad(option: {
		chanel: number | string
		categoryId: string
		cartButton: number
		queryName: string
	}) {
		this.setData(
			{
				chanel: Number(option.chanel),
				showCategoryId: option.categoryId || '',
				cartButton: option.cartButton || 3,
				queryName: option.queryName || ''
			},
			() => {
				this.initData()
			}
		)
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 初始化数据
	 */

	initData() {
		if (this.chanel === 2 || this.chanel === 12) {
			// 2商超分类页 12商超分类页二级分类导航
			this.getGooodsList()
		} else if (this.chanel === 4) {
			// 4首页搜索组件
			this.searchGoodsList()
		}
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 修改列表状态
	 */

	changeStatus() {
		this.setData({
			secondCatGoodsStyle: this.secondCatGoodsStyle === 1 ? 2 : 1
		})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 修改激活tab
	 * @param {*} e
	 */

	changeActiveTab(e: { currentTarget: { dataset: { index: number } } }) {
		let isPrice = this.isPrice
		const activeTab = e.currentTarget.dataset.index
		if (activeTab === 4) isPrice = this.isPrice === 'asc' ? 'desc' : 'asc'
		else isPrice = ''
		if (activeTab === 3) isPrice = 'desc'
		this.setData(
			{
				goodsList: [],
				hasMore: true,
				searchForm: {
					// 获取商品时搜索对象
					current: 1,
					size: 10
				},
				activeTab,
				isPrice
			},
			() => {
				this.initData()
			}
		)
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 加载更多
	 */

	loadMored() {
		this.initData()
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 获取商超商品
	 */

	getGooodsList() {
		if (!this.hasMore) return
		const searchForm = {
			...this.searchForm,
			type: this.activeTab,
			name: this.queryName,
			showCategoryId: this.showCategoryId,
			sort: this.isPrice
		}
		getGoodsList(searchForm, {}).then((res) => {
			const goodsList: ApiClassifyProduct[] = []
			res.list.forEach((item: ApiClassifyProduct) => {
				goodsList.push({
					id: Number(item.id),
					name: item.name,
					img: this.returnGoodsImg(item),
					actPrice: item.minPrice,
					price: item.maxPrice,
					soldCount: item.sale,
					inventory: item.inventory
				})
			})
			this.setData(
				{
					goodsList: this.goodsList.concat(goodsList)
				},
				() => {
					this.dealSearchParam(res)
				}
			)
		})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 获取商品专区商品
	 */

	searchGoodsList() {
		if (!this.hasMore) return
		const searchForm = {
			...this.searchForm,
			type: this.activeTab,
			name: this.queryName,
			sort: this.isPrice
		}
		searchGoodsList(searchForm).then((res) => {
			const goodsList: ApiClassifyProduct[] = []
			res.list.forEach((item: ApiClassifyProduct) => {
				goodsList.push({
					id: Number(item.id),
					name: item.name,
					img: this.returnGoodsImg(item),
					actPrice: item.minPrice,
					price: item.maxPrice,
					soldCount: item.sale,
					inventory: item.inventory
				})
			})
			this.setData(
				{
					goodsList: this.goodsList.concat(goodsList)
				},
				() => {
					this.dealSearchParam(res)
				}
			)
		})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 处理请求参数
	 * @param {*} res
	 */

	dealSearchParam(res: { total: number }) {
		const hasMore = this.goodsList.length < res.total
		const searchForm = this.searchForm
		if (hasMore) {
			searchForm.current += 1
		}
		this.setData({
			hasMore,
			searchForm
		})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 处理商品图片
	 * @param {ApiClassifyProduct} item
	 */

	returnGoodsImg(item: ApiClassifyProduct) {
		const { widePic, pic } = item
		let url = pic
		if (this.secondCatGoodsStyle === 2) {
			url = widePic ? widePic.split(',')[0] : pic
		}
		return url
	}
}
</script>

<style lang="scss" scoped>
.second__cat-page {
	min-height: 100vh;
	background-color: #f2f2f2;

	.header__box {
		box-sizing: border-box;

		.search__box {
			height: 60px;
			display: flex;
			padding: 0px 10px;
			background-color: #fff;

			.input__iocn {
				flex: 1;
				height: 30px;
				background-color: rgba(228, 228, 228, 1);
				border-radius: 20px;
				margin-top: 3px;
				cursor: pointer;
			}

			.icon__box {
				width: 50px;
				height: 100%;
				position: relative;
				cursor: pointer;
				padding-top: 2px;

				.icon__image {
					float: right;
					width: 50rpx;
					height: 50rpx;
					margin-right: 10rpx;
				}

				.change__item {
					width: 120rpx;
					line-height: 36rpx;
					height: 36rpx;
					text-align: right;
					position: absolute;
					top: 60rpx;
					right: 8rpx;
					font-size: 24rpx;
				}
			}
		}
	}

	.tab__con {
		display: flex;
		background-color: #fff;

		.tab__item {
			flex: 1;
			text-align: center;
			font-size: 14px;
			cursor: pointer;
			height: 40px;
			line-height: 40px;
		}

		.active__tab {
			color: #fc425a;
		}
	}

	.list__page {
		height: calc(100% - 80px);
	}
}

.price__icon {
	width: 50rpx;
	height: 100%;
	position: relative;
	vertical-align: middle;
	float: right;

	.price__icon_up {
		display: inline-block;
		font-size: 22rpx;
		position: absolute;
		top: -6rpx;
		right: 28px;
		color: #ccc;
	}

	.price__icon_down {
		font-size: 22rpx;
		position: absolute;
		top: 19px;
		right: 28px;
		color: #ccc;
	}

	.active__icon {
		color: #fc425a;
	}
}

.shopNoCar {
	display: flex;
	justify-content: center;
	flex-wrap: wrap;
	width: 100%;
	height: calc(100vh - 100px);
	background-color: #fff;
	position: relative;
}

.shop__noCar {
	margin-top: 70px;
	width: 69%;
}

.shop__noCar--text {
	display: block;
	justify-content: center;
	color: #acacac;
	width: 100%;
	text-align: center;
	position: absolute;
	top: 320px;
}
</style>
