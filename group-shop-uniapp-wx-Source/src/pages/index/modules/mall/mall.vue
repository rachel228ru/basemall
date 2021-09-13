<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-12 15:36:21
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-09-01 16:01:24
 * 123
-->
<template>
	<view
		class="businessSuper--preview"
		:style="{ height: 'calc(100vh - ' + safeHight + 'px - 96rpx)' }"
	>
		<!-- 显示搜索状态 -->
		<!-- <view a:if="{{ formData.isSearch === 2 }}">
      <SearchPage :formData="formData"></SearchPage>
    </view> -->
		<!-- 显示内容页面 -->
		<view class="show__content-info" v-if="hasInfo">
			<!-- 搜索 -->
			<search-plugin></search-plugin>
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
							:canClick="canClick"
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
						<view class="left__biggoods-upper" v-if="upperNextText !== ''">
							<image
								src="https://oss-tencent.bgniao.cn//gruul/20201125/05433c5d8fb7483f91b49576ce7ee618.png"
								mode="aspectFit"
								lazy-load="false"
							></image>
							<text>下拉浏览{{ upperNextText }}</text>
						</view>
						<view
							class="right__box"
							:style="getRightBoxStyle"
							id="leftBiggoods"
						>
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
						<view
							class="left__biggoods-more"
							v-show="isShowLoadNext"
							id="moreTip"
						>
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
import searchPlugin from './components/search-plugin/searchPlugin.vue'
import topModehead from './components/top-modehead/topModehead.vue'
import topBiggoods from './components/top-biggoods/topBigGoods.vue'
import leftTabbar from './components/left-tabbar/leftTabbar.vue'
import swiperList from './components/swiper-list/swiperList.vue'
import leftBiggoods from './components/left-biggoods/leftBigGoods.vue'
import classifiedGoods from './components/classified-goods/classifiedGoods.vue'
import {
	MallState,
	BusinessSuper,
	FirstCategory,
	Category,
	Navigation,
	ICategoryVo
} from './mallType'
import { getEffectiveCategory } from '@/api/modules/goods'
import { getAssembly } from '@/api/modules/decoration'
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
export default class Mall extends Vue implements MallState {
	name = 'mall'
	isFirst = true
	formData = {} as BusinessSuper
	getRightBoxStyle = ''
	getPageStyle = ''
	currentFirstCategory = {}
	hasInfo = true
	isShowLoadNext = false
	upperNextText = ''
	nextCategoryName = ''
	isComeBottom = true
	scrollTopValue = 0
	triggeredStatus = false
	_freshing = false

	safeHight: number = 0

	get navigationBarHeight() {
		return this.$STORE.setStore.navigationBarHeight
	}

	created() {
		this.isFirst = true
		this.getPageAssembly()
		this.getHeight()
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 获取系统参数
	 * @param {*}
	 * @returns {*}
	 */
	getHeight(): void {
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
	 * @param {*}
	 * @returns {*}
	 */

	async getPageAssembly(): Promise<void> {
		let formData: BusinessSuper = {} as BusinessSuper
		const btmList = this.$STORE.setStore.tabBar.list
		const templateId = this.$STORE.setStore.templateId
		const fromList = btmList.filter((item) => item.name === 'mall')
		this.$STORE.setStore.setSaleMode(fromList[0].id)
		this.$STORE.setStore.getPageType(fromList[0].id)
		const param = {
			templateId,
			modelId: fromList[0].id
		}
		uni.setNavigationBarTitle({
			title: `${fromList[0].linkName}`
		})
		await getAssembly(param).then((res) => {
			const properties = JSON.parse(res[0].properties)
			formData = properties.formData
			this.dealManage(formData, fromList[0].id)
		})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 处理formData数据
	 * @param {BusinessSuper} formData
	 * @param {number} id
	 * @returns {*}
	 */

	dealManage(formData: BusinessSuper, id: number): void {
		this.filterCategory(formData, id).then(() => {
			if (
				formData.selectMode === 2 &&
				this.isFirst &&
				formData.firstCatList.length
			) {
				formData.currentFirstCategory = formData.firstCatList[0]
			}
			this.formData = formData
			this.isFirst = false
			this.hasInfo = Boolean(formData.firstCatList.length)
			this.getRightBoxStyleHandle()
		})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 获取分类下是否有商品
	 * @param {number} e
	 * @returns {*}
	 */

	getNoList(e: number) {
		this.triggeredStatus = false
		this._freshing = false
		this.hasInfo = Boolean(e)
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 初始化数据，需要过滤掉不可用掉一级分类
	 * @param {BusinessSuper} formData
	 * @param {number} id
	 * @returns {*}
	 */

	filterCategory(formData: BusinessSuper, id: number): Promise<string> {
		return new Promise(async (resolve, reject) => {
			try {
				const res = await getEffectiveCategory(id)
				const list: Category[] = res
				const { firstCatList } = formData
				const value: FirstCategory[] = []
				firstCatList.forEach((i: FirstCategory) => {
					const category = list.find((k) => k.id === i.category.id)
					if (category) {
						i.category = category
						value.push(i)
					}
				})
				formData.firstCatList = value
				this.fitlerClassSed(formData)
				resolve('输出')
			} catch (e) {
				reject()
			}
		})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 过滤分类导航中选择的不可用的二级分类
	 * @param {BusinessSuper} formData
	 * @returns {*}
	 */

	fitlerClassSed(formData: BusinessSuper): void {
		const { firstCatList, selectMode, selectType } = formData
		const value: FirstCategory[] = []
		firstCatList.forEach((i: FirstCategory) => {
			const { showCategoryVos = [] } = i.category
			const { navigation, swiperList } = i
			navigation.forEach((k: Navigation, f: number) => {
				if (!k.class.length) {
					navigation.splice(f, 1)
				} else {
					k.class.forEach((j: string, m: number) => {
						const idx = showCategoryVos.findIndex((t: Category) => t.id === j)
						if (idx !== -1) {
							k.chartList[m].category =
								showCategoryVos.find((t: Category) => t.id === j) ||
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
	 * @description: 获取一级分类ID数组
	 * @param {BusinessSuper} formData
	 * @returns {string[]}
	 */

	getFirstCategoryIds(formData: BusinessSuper): string[] {
		const firstCatList = formData.firstCatList || []
		return firstCatList.length
			? firstCatList.map((i: FirstCategory) => i.category.id)
			: []
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 动态设置侧边栏商品盒子样式
	 * @param {*}
	 * @returns {*}
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
	 * @LastEditors: chuyinlong
	 * @description: 获取当前显示的展示分类
	 * 并判断是否还有下一个以及上一个
	 * @param {BusinessSuper} formData
	 * @returns {*}
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
.businessSuper--preview {
	width: 100vw;
	height: calc(100vh - 288rpx);
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
