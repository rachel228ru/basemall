<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-12 15:36:21
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-09-01 16:01:11
-->
<template>
	<view>
		<view :class="{ home: true, tripList_root: showModalView }">
			<!-- <image src="https://oss-tencent.bgniao.cn//gruul/20200703/54fe31285d474fd793232f7b0b759f43.png" style="width=100%" bind:tap="tohuishou"/> -->
			<view v-for="(item, index) in components" :key="index">
				<!-- 魔方组件 -->
				<cube-box
					v-if="item.value === 'CubeBox'"
					id="CubeBox"
					:decoretionProperties="item.formData"
				></cube-box>
				<!-- 店铺导航组件 -->
				<shop-nav
					v-if="item.value === 'StoreNavigation'"
					:navList="item.formData.storeNavigations"
					@AssembleReload="AssembleReload"
				></shop-nav>
				<!-- 轮播图组件 -->
				<home-swiper
					v-if="item.value === 'HomeSwiper' && item.formData.swiperList.length"
					id="HomeSwiper"
					:decoretionProperties="item.formData"
				></home-swiper>
				<!-- 分隔符组件 -->
				<separator
					v-if="item.value === 'Separator'"
					id="Separator"
					:decoretionProperties="item.formData"
				></separator>
				<!-- 空白占位组件 -->
				<blank-paceholder
					v-if="item.value === 'BlankPaceholder'"
					id="BlankPaceholder"
					:decoretionProperties="item.formData"
				></blank-paceholder>
				<!-- 图片组件 -->
				<imageCom
					v-if="item.value === 'ImageCom'"
					id="ImageCom"
					:decoretionProperties="item.formData"
				></imageCom>
				<!-- 商品组件 -->
				<goods-ponents
					v-if="item.value === 'Goods'"
					ref="GroupGoods"
					:decoretionProperties="item.formData"
				>
				</goods-ponents>
				<!-- 搜索组件 -->
				<search-comp
					v-if="item.value === 'Search'"
					id="SearchItem"
					:decoretionProperties="item.formData"
					@search="search"
				></search-comp>
				<!-- 标题组件 -->
				<title-bar
					v-if="item.value === 'TitleBar'"
					id="TitleBar"
					:decoretionProperties="item.formData"
				></title-bar>
				<!-- 富文本组件 -->
				<rich-text
					v-if="item.value === 'RichText'"
					id="RichText"
					:decoretionProperties="item.formData"
				></rich-text>
				<!-- 商品轮播图 -->
				<goods-swiper
					v-if="item.value === 'GoodSwiper'"
					id="GoodsSwiper"
					:decoretionProperties="item.formData"
				>
				</goods-swiper>
				<!-- 视频组件 -->
				<video-com
					v-if="item.value === 'VideoCom'"
					id="VideoCom"
					:decoretionProperties="item.formData"
				></video-com>
			</view>

			<!-- #ifdef MP-WEIXIN -->
			<official-account style="margin-top: 20px;"></official-account>
			<!-- #endif -->

			<!-- #ifndef MP-WEIXIN -->
			<view
				>当前为非微信小程序环境，不支持公众号关注组件，请自行调整当前弹窗内容！</view
			>
			<!-- #endif -->

			<auth
				:authType="authType"
				@authClick="authClick"
				state="home"
				@getLocation="getLocation"
			></auth>
			<shopManage
				:shopMsg="shopMsg"
				:shoptype="shoptype"
				:shopTime="shopTime"
				:shopStatus="shopStatus"
				style="z-index:1000000"
			></shopManage>
		</view>
		<van-dialog id="van-dialog"></van-dialog>
	</view>
</template>

<script lang="ts">
import { Component, Vue, Prop } from 'vue-property-decorator'
import CubeBox from './components/cube-box/cube-box.vue'
import ShopNav from './components/shop-nav/shop-nav.vue'
import HomeSwiper from './components/home-swiper/home-swiper.vue'
import separator from './components/home-separator/home-separator.vue'
import BlankPaceholder from './components/blank-paceholder/blank-paceholder.vue'
import imageCom from './components/imageCom/imageCom.vue'
import GoodsPonents from './components/goods-ponents/goods-ponents.vue'
import SearchComp from './components/search-comp/search-comp.vue'
import TitleBar from './components/title-bar/title-bar.vue'
import RichText from './components/rich-text/rich-text.vue'
import GoodsSwiper from './components/goods-swiper/goods-swiper.vue'
import VideoCom from './components/video-com/video-com.vue'
import Auth from '@/components/auth/auth.vue'
import shopManage from '@/components/shopManage/shopManage.vue'
import { getAssembly } from '@/api/modules/decoration'
import { StateInfer, Animation, DecorationType } from './homeType'
import isEqual from 'lodash/isEqual'
import { PolyUserInfo } from '@/store/modulesType/userType'
import { ShopInfoType } from '@/store/modulesType/shopsetType'
import { DecorationComponent } from '@/store/modulesType/settingType'
@Component({
	components: {
		CubeBox,
		ShopNav,
		HomeSwiper,
		separator,
		BlankPaceholder,
		imageCom,
		GoodsPonents,
		SearchComp,
		TitleBar,
		RichText,
		GoodsSwiper,
		VideoCom,
		Auth,
		shopManage
	}
})
export default class home extends Vue implements StateInfer {
	name: string = 'home'

	@Prop({
		type: Object
	})
	userInfo!: PolyUserInfo
	@Prop({
		type: Boolean,
		default: false
	})
	hasInCustom!: Boolean
	@Prop({
		type: Boolean,
		default: false
	})
	isCustom!: Boolean
	@Prop({
		type: Number,
		default: 0
	})
	notSwitchTab!: Number

	authType = false
	showModalView = false
	components: DecorationType[] = []
	scrollTop = 0
	shoptype = false //
	shopTime = '00:00'
	shopStatus = 2
	shopMsg: ShopInfoType = {} as ShopInfoType

	GroupGoods: Extract<any, GoodsPonents>
	animation!: Animation

	get isCurrent() {
		return this.$STORE.setStore.currentTab === this.name
	}

	created() {
		this.GroupGoods = this.$refs.GroupGoods

		this.getPageSetting()

		const obj = uni.getLaunchOptionsSync()
		const shopMsg = this.$STORE.shopSetStore.shopInfo
		this.$emit('stopShow', {
			type: shopMsg.status !== 2 && obj.scene ? 0 : 1
		})
		this.PageScroll({ scrollTop: 10 })
	}

	mounted() {
		this.getPageSetting()
		this.$Pubsub.on('app-launch', () => {
			this.getPageSetting()
		})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 显示弹窗
	 * @param {*}
	 * @returns {*}
	 */
	showview(): void {
		this.showModalView = true
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 关闭弹窗
	 * @param {*}
	 * @returns {*}
	 */
	closeview(): void {
		this.showModalView = false
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 用户登录授权成功
	 * @param {*}
	 * @returns {*}
	 */

	authClick(): void {
		this.authType = false
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 导航点击操作
	 * @param {string|number} itemLink.id
	 * @returns {*}
	 */

	navClick(itemLink: { id: string | number }): void {
		this.reGetPageAssembly(itemLink.id)
		if (this.$STORE.setStore.customName) {
			uni.setNavigationBarTitle({
				title: `${this.$STORE.setStore.customName}`
			})
			this.$STORE.setStore.setcustomName('')
		}
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 页面滚动监听
	 * @param {*}
	 * @returns {*}
	 */
	PageScroll(e: { scrollTop: number }): void {
		this.scrollTop = e.scrollTop
		const animation = uni.createAnimation({
			duration: 500,
			timingFunction: 'ease'
		})
		animation.translate(50, 0).step()
		this.animation = animation.export()
		const timer = setTimeout(() => {
			if (this.scrollTop === e.scrollTop) {
				this.scrollTop = e.scrollTop
				animation.translate(0, 0).step()
				this.animation = animation.export()
				clearTimeout(timer)
			}
		}, 500)
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 缓存获取页面组件数据
	 * @param {*}
	 * @returns {*}
	 */
	AssembleReload(): void {
		this.components = this.$STORE.setStore.components
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 获取页面组件
	 * @param {*}
	 * @returns {*}
	 */

	async getPageAssembly(): Promise<void> {
		try {
			if (!this.$STORE.setStore.hasInCustom) {
				// 初始话通过聚合接口获取到首页数据
				this.components = this.$STORE.setStore.components
			} else {
				// 除第一次外 进入首页需重新获取接口获取首页数据
				this.reGetPageAssembly(this.$STORE.setStore.newCurrentPageId)
			}
			await this.getShopCom()
		} catch (error) {
			this.$Popup.toast(error)
		}
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 获取页面组件
	 * @param {string | number} id
	 * @returns {*}
	 */

	async reGetPageAssembly(id: string | number = ''): Promise<void> {
		try {
			const res = await getAssembly({
				pageId: id ? id : this.$STORE.setStore.newCurrentPageId
			})

			if (res && !isEqual(res, this.$storage.get(`reGetPageAssembly-${id}`))) {
				this.$storage.set(
					`reGetPageAssembly-${id}`,
					res,
					3 * 24 * 60 * 60 * 1000
				)
			}

			const storageData = this.$storage.get(`reGetPageAssembly-${id}`)
			this.components = res ? this.parseAssembleList(storageData) : []
			await this.getShopCom()
		} catch (error) {
			this.$Popup.toast(error)
		}
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 解析自定义页面组件数据
	 * @param {DecorationComponent[]} assembleList
	 * @returns {DecorationType[]}
	 */

	parseAssembleList(assembleList: DecorationComponent[]): DecorationType[] {
		const components: DecorationType[] = []
		if (assembleList.length > 0) {
			assembleList.forEach((item) => {
				const properties = JSON.parse(item.properties)
				components.push(properties)
			})
		}
		return components
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 获取店铺状态
	 * @param {*}
	 * @returns {*}
	 */

	async getShopCom(): Promise<void> {
		const shopMsg = this.$STORE.shopSetStore.shopInfo
		if (!shopMsg.businessHours) return
		const time = JSON.parse(shopMsg.businessHours)
		if (shopMsg.status !== 2) {
			this.$STORE.setStore.setTabVisible(false)
			this.shopMsg = shopMsg
			this.shoptype = true
			this.shopTime = time[0].slice(0, 5)
			this.shopStatus = shopMsg.status
		} else {
			this.shoptype = false
		}
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 搜索触发
	 * @param {number} val.scope
	 * @param {string} val.name
	 * @returns {*}
	 */
	search(val: { scope: number; name: string }): void {
		this.GroupGoods = this.$refs.GroupGoods
		if (this.GroupGoods && val.scope === 1) {
			this.GroupGoods.onSearch(val.name)
		}
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 获取页面组件
	 * @param {*}
	 * @returns {*}
	 */

	async getPageSetting(): Promise<void> {
		await this.getPageAssembly()
		this.animation = uni.createAnimation({})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 取消授权
	 * @param {boolean} e
	 * @returns {*}
	 */
	getLocation(e: boolean): void {
		this.authType = false
		this.stopRoll(e)
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 底部禁止滚动
	 * @param {boolean} e
	 * @returns {*}
	 */

	stopRoll(e: boolean): void {
		const type = e
		this.setData({
			showModalView: type
		})
	}
}
</script>

<style lang="scss" scoped>
@include b(home) {
	background-color: #f8f8f8;
}

.icon {
	color: red;
}

.popupSty {
	width: 70%;
	height: 70%;
}

.tripList_root {
	top: 0px;
	left: 0px;
	width: 100%;
	height: 100%;
	overflow: hidden;
	position: fixed;
	z-index: 0;
}

.choiceShop2 {
	position: fixed;
	bottom: 300rpx;
	right: 40rpx;
	width: 90rpx;
	height: 90rpx;
	display: -webkit-box;
	display: flex;
	-webkit-box-align: center;
	align-items: center;
	-webkit-box-pack: center;
	justify-content: center;
	flex-wrap: wrap;
	background-color: rgba(0, 0, 0, 0.5);
	border-radius: 100rpx;
	color: white;
	z-index: 10;

	.choiceWrap {
		width: 25px;
		display: flex;
		flex-wrap: wrap;
		justify-content: center;
		align-content: center;
	}

	.ft {
		font-size: 10px;
		margin-top: -6rpx;
	}
}
</style>
