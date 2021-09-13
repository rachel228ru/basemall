<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-12 15:36:21
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-09-01 16:03:31
-->
<template>
	<view class="spellPage--preview">
		<!-- 显示内容页面 -->
		<view class="show__content-info" v-if="hasInfo">
			<!-- 搜索 -->
			<search-plugin :chanel="3"></search-plugin>
			<!-- 内容显示区 -->
			<view class="show__con-box">
				<!-- 顶部模式 -->
				<view
					class="con__box"
					style="height: 100%;"
					v-if="formData.selectMode === 1"
				>
					<!-- 头部tab bar -->
					<top-modehead
						:propData="formData"
						@changeCategoryId="changeCategoryId"
					></top-modehead>
					<view class="viewiding__line"></view>
					<!-- 商品组件 -->
					<top-biggoods :propData="formData"></top-biggoods>
				</view>
				<!-- 侧边栏模式 -->
				<view class="con__box left-con__box" v-if="formData.selectMode === 2">
					<!-- 侧边tab bar -->
					<view class="left__tabbar-box">
						<left-tabbar
							@changeCategoryId="changeCategoryId"
							:propData="formData"
						></left-tabbar>
					</view>
					<scroll-view
						scroll-y="true"
						class="left__biggoods-box"
						:refresher-enabled="true"
						:scroll-anchoring="true"
						:refresher-triggered="triggeredStatus"
						refresher-default-style="black"
						:refresher-threshold="10"
						@refresherpulling="refresherpullingPulling"
						@touchstart="scrollStart"
						@touchend="scrollEnd"
						@scroll="scrollBindScroll"
						@refresherrefresh="refresherrefreshPull"
						@refresherrestore="refresherrestoreEnd"
					>
						<!-- 底部上拉加载下一个分类页 -->
						<view class="left__biggoods-upper" v-if="upperNextText">
							<image
								src="https://oss-tencent.bgniao.cn//gruul/20201125/05433c5d8fb7483f91b49576ce7ee618.png"
								mode="aspectFit"
								lazy-load="false"
							></image>
							<text>下拉浏览{{ upperNextText }}</text>
						</view>
						<view class="right__box" :style="getRightBoxStyle">
							<!-- 海报 -->
							<swiper-list :propData="formData"></swiper-list>
							<!-- 商品组件 -->
							<left-biggoods
								v-if="formData.selectType === 1"
								:propData="formData"
								@getNoList="getNoList"
							></left-biggoods>
							<!-- 分类导航 -->
							<classified-goods
								v-if="formData.selectType === 2"
								:propData="formData"
							></classified-goods>
						</view>
						<!-- 底部上拉加载下一个分类页 -->
						<view class="left__biggoods-more" v-show="isShowLoadNext">
							<image
								src="https://oss-tencent.bgniao.cn//gruul/20201117/26690c0bd517470d9dad39938555d93b.png"
								mode="aspectFit"
								lazy-load="false"
							></image>
							<text>上拉浏览{{ nextCategoryName }}</text>
						</view>
					</scroll-view>
				</view>
			</view>
		</view>
		<!-- 空白页面 -->
		<view class="shopNoCar" v-if="!hasInfo">
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
import { getAssembly } from '@/api/modules/decoration'
import { getEffectiveCategory } from '@/api/modules/goods'
import searchPlugin from './components/search-plugin/search-plugin.vue'
import topModehead from './components/top-modehead/top-modehead.vue'
import topBiggoods from './components/top-biggoods/top-biggoods.vue'
import leftTabbar from './components/left-tabbar/left-tabbar.vue'
import swiperList from './components/swiper-list/swiper-list.vue'
import leftBiggoods from './components/left-biggoods/left-biggoods.vue'
import classifiedGoods from './components/classified-goods/classified-goods.vue'
import {
	BusinessSuper,
	Category,
	FirstCategory,
	ICategoryVo,
	MallState,
	Navigation
} from '@/pages/index/modules/mall/mallType'

@Component({
	components: {
		searchPlugin,
		topModehead,
		topBiggoods,
		leftTabbar,
		swiperList,
		leftBiggoods,
		classifiedGoods
	}
})
export default class SpellPage extends Vue implements MallState {
	isFirst: boolean = true
	getPageStyle = ''
	formData: BusinessSuper = {} as BusinessSuper
	getRightBoxStyle: string = '' // 侧边栏模式商品盒子样式
	currentFirstCategory: Exclude<Partial<FirstCategory>, string> = {} // 当前编辑一级分类
	hasInfo: boolean = false
	isShowLoadNext: boolean = false // 侧边栏模式是否显示上拉加载下一个分类页
	upperNextText: string = '' // 侧边栏模式是否显示下拉加载下一个分类页
	nextCategoryName: string = '' // 下一个分类页名称
	isComeBottom: boolean = false // 是否已经触底
	scrollTopValue: number = 0 // 上拉距离
	saleMode: string = ''
	triggeredStatus: boolean = false // 下拉状态
	_freshing: boolean = false //是否正在刷新状态

	safeHight: number = 0

	onLoad(options: { titleName: string; id: number }) {
		if (options.titleName) {
			wx.setNavigationBarTitle({
				title: `${options.titleName}专区`
			})
		}
		this.setData({
			saleMode: options.id
		})
		this.isFirst = true
		this.getPageAssembly()
		this.getHeight()
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 获取系统信息
	 */
	getHeight() {
		const sysinfo = uni.getSystemInfoSync()
		const statusHeight = sysinfo.statusBarHeight
		const isiOS = sysinfo.system.indexOf('iOS') > -1
		const navHeight = !isiOS ? 48 : 44
		const safeHeight = sysinfo.screenHeight - sysinfo.safeArea.bottom

		this.safeHight = statusHeight + navHeight + safeHeight - 5
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 获取页面组件
	 */

	async getPageAssembly() {
		let formData = {} as BusinessSuper
		const templateId = this.$STORE.setStore.templateId
		await this.$STORE.setStore.getPageType(this.saleMode)
		const param = {
			templateId,
			modelId: this.saleMode ? this.saleMode : this.$STORE.setStore.saleMode
		}
		getAssembly(param).then((res) => {
			if (res === '无此专区商品信息') {
				return
			}
			const properties = JSON.parse(res[0].properties) || ''
			formData = properties.formData
			this.dealManage(formData)
		})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 获取分类下是否有商品
	 * @param {number} e
	 */

	getNoList(e: number) {
		this.triggeredStatus = false
		this._freshing = false
		this.hasInfo = Boolean(e)
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 页面数据获取
	 * @param {BusinessSuper} formData
	 */
	dealManage(formData: BusinessSuper) {
		this.filterCategory(formData).then(() => {
			if (
				formData.selectMode === 2 &&
				this.isFirst &&
				formData.firstCatList.length
			)
				formData.currentFirstCategory = formData.firstCatList[0]
			formData.saleMode = this.saleMode || this.$STORE.setStore.saleMode
			this.setData(
				{
					formData,
					isFirst: false,
					hasInfo: Boolean(formData.firstCatList.length)
				},
				() => {
					this.RightBoxStyle()
				}
			)
		})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 动态设置侧边栏商品盒子样式
	 */

	RightBoxStyle() {
		const getCategoryVo = this.getCategoryVo(this.formData)
		const style = `width: calc(100% - ${this.formData.pagePadding *
			2}px); margin-left: ${this.formData.pagePadding}px;`
		this.setData({
			getRightBoxStyle: `${style} padding-bottom: ${
				getCategoryVo.hasNextCatgory ? '50px' : '0px'
			}`,
			isShowLoadNext: getCategoryVo.hasNextCatgory,
			nextCategoryName: getCategoryVo.name || ''
		})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 初始化数据，需要过滤掉不可用掉一级分类
	 * @param {BusinessSuper} formData
	 */

	filterCategory(formData: BusinessSuper) {
		return new Promise(async (resolve, reject) => {
			try {
				const res = await getEffectiveCategory(
					this.saleMode || this.$STORE.setStore.saleMode
				)
				const list = res
				const { firstCatList } = formData
				const value: any[] = []
				firstCatList.forEach((i: { category: { id: string | number } }) => {
					const category = list.find(
						(k: { id: string | number }) => k.id === i.category.id
					)
					if (category) {
						i.category = category
						value.push(i)
					}
				})
				formData.firstCatList = value
				this.fitlerClassSed(formData)
				resolve('')
			} catch (e) {
				reject()
			}
		})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 过滤分类导航中选择的不可用的二级分类
	 * @param {BusinessSuper} formData
	 */

	fitlerClassSed(formData: BusinessSuper) {
		const { firstCatList, selectMode, selectType } = formData
		const value: any[] = []
		firstCatList.forEach((i: FirstCategory) => {
			const { showCategoryVos = [] } = i.category
			const { navigation, swiperList } = i
			navigation.forEach((k: Navigation, f: number) => {
				if (!k.class.length) {
					navigation.splice(f, 1)
				} else {
					k.class.forEach((j: string, m: number) => {
						const idx = showCategoryVos.findIndex(
							(t: { id: string }) => t.id === j
						)
						if (idx !== -1) {
							k.chartList[m].category =
								showCategoryVos.find((t: { id: string }) => t.id === j) ||
								({} as Category)
						} else {
							k.class.splice(m, 1)
							k.chartList.splice(m, 1)
						}
					})
				}
			})
			if (
				!(
					selectType === 2 &&
					selectMode === 2 &&
					!swiperList.length &&
					!navigation.length
				)
			) {
				value.push(i)
			}
		})
		formData.firstCatList = [...value]
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 动态设置侧边栏商品盒子样式
	 */

	getRightBoxStyleHandle(): void {
		const getCategoryVo = this.getCategoryVo(this.formData)
		const style = `width: calc(100% - ${this.formData.pagePadding *
			2}px); margin-left: ${this.formData.pagePadding}px;`
		this.getRightBoxStyle = `${style} padding-bottom: ${
			getCategoryVo.hasNextCatgory ? '100rpx' : '0rpx'
		}`
		this.isShowLoadNext = getCategoryVo.hasNextCatgory
		this.nextCategoryName = getCategoryVo.name || ''
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description:修改tab值
	 * @param {string} e
	 * @returns {*}
	 */

	changeCategoryId(e: string): void {
		const id = e
		let formData = { ...this.formData }
		const currentFirstCategory: FirstCategory =
			formData.firstCatList.find((i) => i.category.id === id) ||
			({} as FirstCategory)
		formData.currentFirstCategory = currentFirstCategory
		this.currentFirstCategory = currentFirstCategory
		this.formData = { ...formData }
		this.refresherpullingPulling()
		this.getRightBoxStyleHandle()
	}

	/**
	 * 获取当前显示的展示分类
	 * 并判断是否还有下一个以及上一个
	 */
	getCategoryVo(formData: BusinessSuper): ICategoryVo {
		const { category } = formData.currentFirstCategory
		const firstCatList = formData.firstCatList || []
		const id = category ? category.id : ''
		const index = firstCatList.findIndex(
			(i: { category: { id: any } }) => i.category.id === id
		)
		const hasNextCatgory = index !== -1 && index !== firstCatList.length - 1
		const hasUpperCatgory = index !== -1 && index !== 0
		const nextCategory = hasNextCatgory
			? firstCatList[index + 1].category
			: null
		const upperCatgory = hasUpperCatgory
			? firstCatList[index - 1].category
			: undefined
		return {
			...nextCategory,
			hasNextCatgory,
			hasUpperCatgory,
			upperCatgory
		}
	}
	/**
	 * @LastEditors: chuyinlong
	 * @description: 开始上拉时初始化状态
	 * @param {*} e
	 * @returns {*}
	 */

	scrollStart(e: { changedTouches: { clientY: number }[] }): void {
		this.scrollTopValue = e.changedTouches[0].clientY
		this.isComeBottom = false
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 上拉结束判断状态
	 * @param {*} e
	 * @returns {*}
	 */

	scrollEnd(e: { changedTouches: { clientY: number }[] }): void {
		const scrollTopValue = e.changedTouches[0].clientY - this.scrollTopValue
		if (!this.isComeBottom && scrollTopValue < 0) {
			const categoryVo = this.getCategoryVo(this.formData)
			if (categoryVo.hasNextCatgory) {
				this.changeCategoryId(categoryVo.id || '')
			}
		}
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 如果往上滚动则修改状态
	 * @param {*}
	 * @returns {*}
	 */

	scrollBindScroll(): void {
		this.isComeBottom = true
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 自定义下拉刷新
	 * @param {*}
	 * @returns {*}
	 */

	refresherrefreshPull(): void {
		if (this._freshing) return
		this._freshing = true
		if (!this.triggeredStatus) {
			this.triggeredStatus = true
		}
		const getCategoryVo = this.getCategoryVo(this.formData)
		if (getCategoryVo.hasUpperCatgory) {
			this.changeCategoryId(
				getCategoryVo.upperCatgory ? getCategoryVo.upperCatgory.id : ''
			)
		} else {
			this.upperNextText = ''
			this.$nextTick(() => {
				this.triggeredStatus = false
				this._freshing = false
			})
		}
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 下拉刷新复位
	 * @param {*}
	 * @returns {*}
	 */

	refresherrestoreEnd(): void {
		this.upperNextText = ''
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 下拉过程中检测状态
	 * @param {*}
	 * @returns {*}
	 */

	refresherpullingPulling(): void {
		const getCategoryVo = this.getCategoryVo(this.formData)
		if (getCategoryVo.hasUpperCatgory && getCategoryVo.upperCatgory) {
			this.upperNextText = getCategoryVo.upperCatgory.name
		} else {
			this.upperNextText = ''
		}
		if (getCategoryVo.hasNextCatgory && getCategoryVo.name) {
			this.nextCategoryName = getCategoryVo.name
		} else {
			this.nextCategoryName = ''
		}
	}
}
</script>

<style lang="scss" scoped>
.spellPage--preview {
	width: 100vw;
	height: 100vh;
	background-color: #f2f2f2;

	.show__content-info {
		height: 100%;
		box-sizing: border-box;
		overflow-y: scroll;
	}

	.search__box {
		height: 60px;
		box-sizing: border-box;
		padding: 12px 20px;

		.search-icon {
			height: 35px;
			background-color: rgba(242, 242, 242, 1);
			border-radius: 6px;

			i {
				float: right;
				margin-top: 10px;
				margin-right: 10px;
			}
		}
	}

	.show__con-box {
		height: calc(100% - 60px);
		box-sizing: border-box;
	}

	.dividing__line {
		width: 100%;
		height: 5px;
		background-color: rgba(242, 242, 242, 1);
	}

	.con__box {
		min-height: 100%;
		overflow-x: hidden;
		overflow-y: scroll;
	}

	.left__tabbar-box {
		width: 80px;
		height: 100%;
		overflow-x: hidden;
		overflow-y: scroll;
	}

	.left-con__box {
		display: flex;
		height: 100%;
		overflow-x: hidden;
		overflow-y: scroll;
		box-sizing: border-box;
	}

	.left__biggoods-box {
		flex: 1;
		height: 100%;
		overflow-y: scroll;
		overflow-x: hidden;
		background-color: #ffffff;
	}

	.shopNoCar {
		display: flex;
		justify-content: center;
		flex-wrap: wrap;
		width: 100%;
		height: 100vh;
		background-color: #ffffff;
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
		padding-bottom: 220px;
	}

	.left__biggoods-box {
		.right__box {
			box-sizing: border-box;
			min-height: 100%;
			padding-bottom: 50px;
		}

		.left__biggoods-more {
			height: 100rpx;
			font-size: 0rpx;
			color: #666;
			text-align: center;
			line-height: 100rpx;
			margin-top: -100rpx;
			width: 100%;
			white-space: nowrap;
			text-overflow: ellipsis;
			overflow: hidden;
			box-sizing: border-box;

			image {
				display: inline-block;
				width: 20px;
				height: 18px;
				vertical-align: middle;
				margin: 2px;
			}

			text {
				display: inline-block;
				font-size: 26rpx;
				vertical-align: middle;
				max-width: calc(100% - 40px);
				overflow: hidden;
				text-overflow: ellipsis;
				white-space: nowrap;
			}
		}

		.left__biggoods-upper {
			height: 100rpx;
			font-size: 0rpx;
			color: #666;
			text-align: center;
			line-height: 100rpx;
			width: 100%;
			white-space: nowrap;
			text-overflow: ellipsis;
			overflow: hidden;
			box-sizing: border-box;

			image {
				display: inline-block;
				width: 20px;
				height: 18px;
				vertical-align: middle;
				margin: 2px;
			}

			text {
				display: inline-block;
				font-size: 26rpx;
				vertical-align: middle;
				max-width: calc(100% - 40px);
				overflow: hidden;
				text-overflow: ellipsis;
				white-space: nowrap;
			}
		}
	}
}
</style>
