<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-12 15:36:21
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-09-02 11:17:56
 * 123
-->
<template>
	<view>
		<canvas
			canvas-id="shareCanvas"
			style="width:560px;height:880px"
			class="share--card"
		></canvas>
		<view
			class="absolute backgournd"
			v-if="!cardFlag"
			catchtouchmove="preventTouchMove"
		>
			<view class="absolute backgournd" catchtouchmove="preventTouchMove">
				<canvas
					style="width:560px;height:880px"
					canvas-id="cardCanvas"
					class="share--canvas"
				></canvas>
			</view>
		</view>
		<view :class="couponShow ? 'stopOutside' : ''">
			<view
				style="width:100%;height:600px"
				v-if="!cardFlag"
				catchtouchmove="preventTouchMove"
				class="absolute center--middle"
			>
				<view class="absolute" v-if="showAll">
					<view class="icon--end">
						<view class="icon--end__content">
							<m-icon
								name="icontijiaodingdan-youhuiquan-guanbi"
								size="20px"
								color="#D1D1D1"
								@tap="hiddenCrad"
							></m-icon>
						</view>
					</view>
					<image
						class="share--image"
						@tap.stop="cardClick"
						mode="aspectFill"
						:src="cardImgUrl"
					></image>
					<view
						class=" downButton"
						@tap.stop="savePhoto"
						style="margin-top:40rpx;margin-left:20px"
					>
						<view class="downButton--content">
							<view style="font-size:15px">保存至本地</view>
						</view>
					</view>
				</view>
			</view>
			<swiper
				:indicator-dots="indicatorDots"
				:autoplay="autoplay"
				indicator-color="white"
				:interval="3000"
				:duration="300"
				circular="true"
				class="swiper"
				current="0"
				@change="onSlideChange"
			>
				<swiper-item v-if="goodsVideoUrl">
					<video
						id="myVideo"
						:src="goodsVideoUrl"
						@tap.stop="bofang"
						controls="true"
						@ended="endbofng"
						:poster="videoImg"
						style="width:100%;height:90%"
					></video>
				</swiper-item>
				<block v-for="(item, index) in goodDetail.imgUrls" :key="index">
					<swiper-item>
						<image :src="item" class="slide-image swiper-item"></image>
					</swiper-item>
				</block>
			</swiper>
			<view class="currentStyle">
				<view class="currentStyle__content">
					<view class="currentStyle__content--curr">
						{{ current }} /
						{{
							goodsVideoUrl
								? goodDetail.imgUrls.length + 1
								: goodDetail.imgUrls.length
						}}
					</view>
				</view>
			</view>
			<!-- 引入backAct部分 -->
			<block>
				<view class="backAct">
					<image
						src="http://medusa-small-file.oss-cn-hangzhou.aliyuncs.com/api/priceBackground.png"
						style="width:100%;height:60px;z-index: 10"
						v-if="showCountDown"
					></image>
					<view
						style="width:100%;height:60px;background-color:#FF0C19;z-index: 10"
						v-else
					></view>
					<view
						style="position:absolute;display:flex;justify-content:space-between;width:100%;align-items: center;z-index: 11"
					>
						<view class="bothNone__price">
							<view class="bothNone__price--actPrice">
								<view style="font-size:34rpx;margin-bottom: 2px">￥</view>
								{{
									goodDetail.actPrice !== '0.00'
										? goodDetail.actPrice
										: goodDetail.price
								}}
							</view>
							<view class="through" v-if="goodDetail.actPrice !== '0.00'"
								>￥{{ goodDetail.price }}</view
							>
						</view>
					</view>
				</view>
			</block>
			<view class="product">
				<view
					class="product__box"
					v-if="!goodDetail.openSpecs && goodDetail.skuStocks.length > 1"
				>
					<!-- <scroll-view class="scroll-view_x" scroll-x style="width:auto;overflow:hidden;">
                <view class="product__box--list" wx:for="{{goodDetail.skuStocks}}" wx:key="item" bind:tap="submit" data-mode="{{item}}">
                    <view class="item__book--text center--middle" style="color:{{item.getType && item.stock>0?'red':''}};border:1rpx solid {{item.getType  && item.stock>0?'red':'#81817F'}};background-color:{{item.stock===0?'#F3F2F1':''}}">
                        {{item.specs}}
                    </view>
                </view>
            </scroll-view> -->
					<view
						v-for="(item, index) in goodDetail.skuStocks"
						:key="index"
						class="product__box--list"
						@tap="submit"
						:data-mode="item"
					>
						<view
							class="item__book--text center--middle"
							:style="
								'color:' +
									(item.getType && item.stock > 0 ? 'red' : '') +
									';border:1rpx solid ' +
									(item.getType && item.stock > 0 ? 'red' : '#81817F') +
									';background-color:' +
									(item.stock === 0 ? '#F3F2F1' : '')
							"
						>
							{{ item.specs }}
						</view>
					</view>
				</view>
				<view class="product__name" style="font-family:'微软雅黑'">{{
					goodDetail.name
				}}</view>
				<view class="product__saleDescribe" v-if="goodDetail.saleDescribe">
					<view class="product__saleDescribe--text" @longpress="longCopy">
						{{ goodDetail.saleDescribe }}
					</view>
				</view>
				<view class="product__temp">
					<view class="product__description">
						<text class="product__description--title">销量</text>
						<view class="product__description--desc ">
							已售
							<text class="product__description--content">{{
								goodDetail.sale
							}}</text>
							件,
						</view>
						<view class="product__description--desc">
							仅剩
							<text class="product__description--content">{{
								goodDetail.stock
							}}</text>
							件
							<span v-if="goodDetail.goodType == 1">,</span>
						</view>
						<view
							class="product__description--desc"
							v-if="goodDetail.goodType == 1"
						>
							限购
							<text class="product__description--content">{{
								goodDetail.maxNum
							}}</text>
							件
						</view>
					</view>
					<view @click="addCollect" class="product__share">
						<m-icon
							name="iconshoucang"
							size="20px"
							style="color:#7C7C7C;"
							v-if="!goodDetail.isCollect"
						></m-icon>
						<m-icon
							name="iconshoucang1"
							size="20px"
							style="color:#EA0000;"
							v-else
						></m-icon>
						<view
							:style="
								'font-size:14px; color:' +
									(!goodDetail.isCollect ? '#7C7C7C' : '#EA0000') +
									';margin-left:5px'
							"
						>
							{{ !goodDetail.isCollect ? '收藏' : '已收藏' }}
						</view>
					</view>
				</view>
			</view>
			<view @tap="showShare" class="product__shareFloat" v-if="true">
				<view
					style="width:25px;display:flex;flex-wrap:wrap;justify-content:center;align-content: center;"
				>
					<view
						class="iconfont iconshangpinxiangqing-fenxiang share__floatIcon"
					></view>
					<view style="font-size:10px">分享</view>
				</view>
			</view>
			<view class="cell">
				<view class="cell__line" v-if="goodDetail.serviceIds">
					<text class="cell__title">服务</text>
					<view class="cell__title--right" style="padding-left: 8px">{{
						goodDetail.serviceIds
					}}</view>
				</view>
				<view
					class="cell__line"
					@tap="submit"
					v-if="goodDetail.openSpecs && goodDetail.skuStocks.length > 1"
				>
					<text class="cell__title">选择</text>
					<view class="cell__title--right" style="padding-left: 8px">
						<view style="margin-right:20rpx;color:#000" v-if="getNorms.specs">
							已选：{{ getNorms.specs }}
						</view>
						<view style="margin-right:20rpx" v-else>请选择规格</view>
						<van-icon
							name="arrow"
							style="font-size:16px;font-weight:bold"
						></van-icon>
					</view>
				</view>
				<view
					class="cell__line"
					@tap="getCoupon"
					data-mode="para"
					v-if="goodDetail.productAttributes.length > 0"
				>
					<text class="cell__title">参数</text>
					<view class="cell__title--right" style="padding-left: 8px">
						<view style="margin-right:20rpx;color: #b1afaf;">查看商品参数</view>
						<van-icon
							name="arrow"
							style="font-size:16px;font-weight:bold"
						></van-icon>
					</view>
				</view>
				<van-popup
					:show="show"
					position="bottom"
					:overlay="true"
					@close="onClose"
					:custom-style="
						'height:' +
							(paraShow ? '55%' : '400px') +
							';border-top-right-radius:' +
							(paraShow ? '10' : '20') +
							'px;border-top-left-radius:' +
							(paraShow ? '10' : '20') +
							'px'
					"
				>
					<view
						v-if="paraShow"
						class="popup__attr"
						style="padding-bottom:50px;"
					>
						<view class="cell__pop--reduce--text" style="font-weight:normal"
							>商品参数</view
						>
						<view
							v-for="(item, index) in goodDetail.productAttributes"
							:key="index"
							:style="'padding-top:' + (index === 0 ? '50px' : '')"
						>
							<view class="popup__attr--item">
								<view class="popup__attr--item--left">{{ item.name }}</view>
								<view class="popup__attr--item--right">{{ item.value }}</view>
							</view>
						</view>
						<view class="center--middle" @tap="onClose">
							<view class="popup__back">
								<view class="popup__attr--finish">完成</view>
							</view>
						</view>
					</view>
				</van-popup>
			</view>
			<view class="assess" v-if="hasOpenEvaluate">
				<view class="assess__top">
					<view class="assess__top--left"
						>评价({{ evaluateOverview.total }})</view
					>
					<view
						class="assess__top--right"
						@tap="gotoReviews"
						:data-id="goodDetail.id"
					>
						<view style="color: #fc425a;"
							>好评{{ evaluateOverview.praiseRate }}%</view
						>
						<van-icon
							name="arrow"
							style="font-size:16px;font-weight:bold"
						></van-icon>
					</view>
				</view>
				<view class="assess__list" @tap="gotoReviews" v-if="evaluateItem.id">
					<view class="assess__list--button center--middle">
						全部
						<view class="assess__list--button--num"
							>({{ evaluateOverview.all || 0 }})</view
						>
					</view>
					<view class="assess__list--button center--middle">
						有内容
						<view class="assess__list--button--num"
							>({{ evaluateOverview.hasContent || 0 }})</view
						>
					</view>
					<view class="assess__list--button center--middle">
						有图片
						<view class="assess__list--button--num"
							>({{ evaluateOverview.hasPicture || 0 }})</view
						>
					</view>
				</view>
				<view class="assess__user " v-if="evaluateItem.userName">
					<view style="display:flex;align-items: center;">
						<view class="assess__user--left">
							<view class="avatar">
								<image :src="evaluateItem.userAvatarUrl"></image>
							</view>
						</view>
						<view class="assess__user--right">
							<view class="name">{{ evaluateItem.userName }}</view>
							<view>
								{{ evaluateItem.createTime }}
								<block v-if="evaluateItem.specs"
									>规格：{{ evaluateItem.specs }}</block
								>
							</view>
						</view>
					</view>
					<view class="assess__bottom">{{ evaluateItem.comment }}</view>
					<!-- <view class="assets__reply" wx:if="{{evaluateItem.reply}}">
                <view class="assess__reply__top">
                    <view class="assess__reply__top--service">掌柜回复：</view>
                    <view class="assess_reply__top--time">{{item.replyTime}}</view>
                </view>
                <view style="margin-top:20rpx">{{evaluateItem.reply}}</view>
            </view> -->
				</view>
			</view>
			<view class="shop--content">— 商品详情 —</view>
			<view class="shop--english">COMMODITY DETAILS</view>
			<!-- 商品详情富文本 -->
			<!-- <view class="goods__text" bind:tap="pageLink" data-link="/pages/index/modules/me/systemBanner/systemBanner"> -->
			<view class="goods__text">
				<rich-text :nodes="goodDetail.detail"></rich-text>
				<image
					class="miniBottomLog"
					v-if="!!shopInfo.miniBottomLog"
					:src="!!shopInfo.miniBottomLog ? shopInfo.miniBottomLog : ''"
					@tap="pageLink"
					data-link="/pages/index/modules/me/systemBanner/systemBanner"
				></image>
				<sign
					:text="'smart shop商城系统 v' + APP_VERSION"
					@tap="pageLink"
					data-link="/pages/index/modules/me/systemBanner/systemBanner"
				></sign>
			</view>
			<!-- 底部按钮标签 -->
			<view class="detail__bottom">
				<view class="detail__img" @tap="returnHome">
					<m-icon name="iconshouye" size="26px"></m-icon>
					<text class="detail__img--icon">店铺</text>
				</view>
				<button open-type="contact" class="detail__img">
					<m-icon name="iconxinbaniconshangchuan-" size="22px"></m-icon>
					<text class="detail__img--icon">客服</text>
				</button>
				<view
					class="detail__img"
					@tap="gotoCar"
					v-if="goodDetail.status === 1 || goodDetail.status === 3"
					style="position:relative"
				>
					<m-icon name="icon3" size="22px"></m-icon>
					<text class="detail__img--icon">购物车</text>
					<view
						class="tip"
						:style="
							'width:' + (shopCarNum > 9 ? 22 : 18) + 'px; font-size:11px;'
						"
						v-if="shopCarNum > 0"
					>
						{{ shopCarNum }}
					</view>
				</view>
				<view
					class="buy__btn"
					@tap="submit"
					v-if="goodDetail.status === 1 || goodDetail.status === 3"
				>
					加入购物车
					<!-- <view class="good_box" hidden="{{hideGoodBox}}" style="left: {{busX}}px; top: {{busY}}px;">
                <image src="{{feiBox}}"></image>
            </view> -->
				</view>
				<view
					class="buy__btn "
					v-if="goodDetail.status === 0"
					style="background-color:#e5e5e5;width:200px"
				>
					商品已下架
				</view>
				<view
					@tap="submit"
					class="buy__btn "
					v-if="goodDetail.status === 1"
					style="background-color:#FF0C19"
				>
					立即购买
				</view>
				<view
					class="buy__btn "
					v-if="goodDetail.status === 2"
					style="background-color:#e5e5e5;width:200px"
				>
					商品已售罄
				</view>
			</view>
			<!-- 规格弹窗 -->
			<van-popup
				:show="buyShow"
				position="bottom"
				:overlay="true"
				@close="onClose"
				custom-style="overflow:visible;border-top-right-radius: 20px;border-top-left-radius: 20px"
			>
				<select-norms
					v-if="buyShow"
					:norms="norms"
					:goodDetail="goodDetail"
					:state="state"
					@onClose="onClose"
					@addShopCar="addShopCar"
					@checkNorms="checkNorms"
				></select-norms>
			</van-popup>
			<!-- 生成卡片分享 -->
			<van-popup :show="shareVisible" position="bottom" @close="onClose">
				<view class="share">分享</view>
				<m-icon
					name="iconshangpinxiangqing-guanbi"
					size="20px"
					@tap="onClose"
					style="position:fixed;margin-top:-22px;right:15px;z-index:101"
				></m-icon>
				<view v-if="clickShare">
					<view class="shareWay">
						<view class="shareWay__side">
							<button
								open-type="share"
								@tap="onClose"
								style="border-radius:50px;display:flex;justify-content;align-items:center"
							>
								<image
									src="http://medusa-small-file.oss-cn-hangzhou.aliyuncs.com/api/WeChat.png"
								></image>
							</button>
							<view class="shareWay__side--text">微信好友</view>
						</view>
						<view class="shareWay__side" @tap="shareBrCard">
							<image
								src="http://medusa-small-file.oss-cn-hangzhou.aliyuncs.com/api/poster.png"
							></image>
							<view class="shareWay__side--text">生成海报</view>
						</view>
					</view>
					<view class="shareTip">
						<view class="shareTip__tip">和好友分享你发现的好物</view>
					</view>
				</view>
				<view
					v-else
					style="display: flex;flex-wrap: wrap;justify-content: center;"
				>
					<view class="screenshot">好商品就要分享 点击按钮即可分享</view>
					<button open-type="share" @tap="onClose" class="screenshotBtn">
						分享给您的好友
					</button>
				</view>
			</van-popup>
			<!-- 授权弹窗 -->
			<auth :authType="authType" @authClick="authClick" @cancel="cancel"></auth>
			<van-dialog id="van-dialog"></van-dialog>
		</view>
	</view>
</template>

<script module="image" lang="wxs" src="@/wxs/image.wxs"></script>
<script lang="ts">
import { Component, Vue } from 'vue-property-decorator'
import selectNorms from '@/components/select-norms/selectNorms.vue'
import mIcon from '@/components/m-icon/m-icon.vue'
import auth from '@/components/auth/auth.vue'
import sign from '@/components/sign/sign.vue'
import Dialog from '@/wxcomponents/vant-weapp/dialog/dialog'
import canvasCom from './cavasCom/canvasCom.vue'
import {
	getGoodDetail,
	getProductEvaluate,
	getProductEvaluateOverview,
	checkIsCollect,
	userAddCollect,
	userDelCollect,
	getImgCode,
	userFooterAdd
} from '@/api/modules/goods'
import { getCarsList } from '@/api/modules/shopCar'
import { addCars } from '@/api/modules/shopCar'
import { base64src } from '@/utils/base64src'
import URL from './canvas'
import {
	GoodDetailInfo,
	DetailState,
	TimeDataType,
	InterCountDetail,
	EvaluateItemType,
	EvaluateOverviewType
} from './detailType'
import { ApiSkuType, GoodInfo } from '@/pages/index/modules/shopCar/shopCarType'
import { IPoint } from './components/animation'

interface SelectGoodInfo extends GoodInfo {
	goodsNumber: number
	skuName: string
}

@Component({
	components: {
		sign,
		canvasCom,
		selectNorms,
		mIcon,
		auth
	}
})
export default class Detail extends Vue implements DetailState {
	goodDetail = {} as GoodDetailInfo
	norms = {} as ApiSkuType
	getNorms = {} as ApiSkuType
	buyShow = false
	show = false
	paraShow = false
	timeData: TimeDataType = {
		hours: [],
		seconds: [],
		minutes: []
	}
	state = ''
	version = 'V1.8'
	shareVisible = false
	shareImgUrl = ''
	imgUrl = ''
	cardImgUrl = ''
	cardFlag = true
	showAll = false
	goodId = ''
	goodsVideoUrl = ''
	videoImg = ''
	autoplay = true
	current: number = 1
	videoContext!: UniApp.VideoContext
	countDetail: InterCountDetail = {
		status: '',
		toBeginTime: 0,
		toEndTime: 0,
		remainTime: 0
	}
	remainTime = 0
	authType = false
	evaluateItem = {} as EvaluateItemType
	evaluateOverview: EvaluateOverviewType[] = []
	shopCarNum = 0
	clickShare = false
	rebateRatio = 0
	isRebate = false
	hideGoodBox = true
	feiBox = ''
	busPos = {} as IPoint
	finger = {} as IPoint
	hasJoinType = false
	APP_VERSION = process.env.VUE_APP_APP_SHOW_VERSION
	shopInfo = {}
	menu = false
	options = {} as {
		id: number
		img: string
		goodTranObj?: string
		transData?: string
	}

	get hasOpenEvaluate() {
		return this.$STORE.setStore.shopSetting.hasOpenEvaluate
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 长按复制卖点描述
	 */

	longCopy() {
		uni.setClipboardData({
			data: this.goodDetail.saleDescribe,
			success: () => {
				uni.showToast({
					title: '复制成功'
				})
			}
		})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 关闭pop
	 */

	onClose() {
		this.show = false
		this.paraShow = false
		this.buyShow = false
		this.shareVisible = false
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 展开分享方式
	 */

	showShare() {
		this.clickShare = true
		this.shareVisible = true
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 前往评论
	 */

	gotoReviews() {
		uni.navigateTo({
			url: `/subcontract/pages/reviews/reviews?id=${this.options.id}`
		})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 规格选择
	 * @param {ApiSkuType} check
	 */
	checkNorms(check: ApiSkuType) {
		this.goodDetail.skuStocks.forEach((item) => {
			item.getType = false
			if (item.id === check.id) {
				item.getType = true
			}
		})
		this.setData({
			getNorms: check,
			goodDetail: Object.assign({}, this.goodDetail)
		})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 进入购物车页面
	 */

	gotoCar() {
		const name = this.$STORE.setStore.tabBar.list.filter(
			(item) => item.name === 'shopCar'
		)
		this.$STORE.setStore.setIndexTitle(name[0].text)
		this.$STORE.setStore.setCurrentPageId(name[0].id)
		uni.reLaunch({
			url: '/pages/index/index?page=shopCar'
		})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 返回首页
	 */

	returnHome() {
		this.$STORE.setStore.backHome()
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 加入购物车
	 * @param {goodDetail: SelectGoodInfo; index: number} norms
	 */

	async addShopCar(norms: { goodDetail: SelectGoodInfo; index: number }) {
		const getNorms = norms.goodDetail
		const goodDetail = this.goodDetail
		if (norms.index) {
			this.gotoSubmit(getNorms)
			return
		}
		goodDetail.goodsNumber = getNorms.goodsNumber
		goodDetail.productId = goodDetail.id
		goodDetail.productName = goodDetail.name
		goodDetail.skuId = getNorms.skuId
		goodDetail.pic = getNorms.pic
		goodDetail.saleMode = getNorms.saleMode
		const parms = {
			goodsNumber: goodDetail.goodsNumber,
			id: '',
			saleMode: goodDetail.saleMode,
			productId: goodDetail.productId,
			productName: goodDetail.productName,
			pic: goodDetail.pic,
			productSn: goodDetail.productSn,
			skuId: goodDetail.skuId,
			skuStocks: goodDetail.skuStocks,
			status: goodDetail.status,
			activityStartTime: goodDetail.activityStartTime,
			activityEndTime: goodDetail.activityEndTime,
			activityId: goodDetail.activityId,
			selectStatus: 1
		}

		this.feiBox = this.goodDetail.pic
		if (!this.hideGoodBox) return
		const topPoint: IPoint = { x: 0, y: 0 }
		const finger = this.finger
		if (finger['y'] < this.busPos['y']) {
			topPoint['y'] = finger['y'] - 80
		} else {
			topPoint['y'] = this.busPos['y'] - 80
		}

		if (finger['x'] < this.busPos['x']) {
			topPoint['x'] = Math.abs(finger['x'] - this.busPos['x']) / 2 + finger['x']
		} else {
			topPoint['x'] = this.busPos['x']
			finger['x'] = this.busPos['x']
		}
		const url = require('./components/animation.ts')
		const linePos: { bezierPoints: IPoint[] } = url.methods.bezier(
			[finger, topPoint, this.busPos],
			30
		)
		addCars(parms)
			.then(() => {
				uni.showToast({ title: '添加成功', icon: 'none' })
				this.buyShow = false
				this.startAnimation(linePos)
				this.getShopCarNum()
				this.$STORE.userStore.getShopCarNum()
			})
			.catch((res) => {
				uni.showToast({ title: res, icon: 'none' })
			})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 跳动开始
	 * @param {{ bezierPoints: IPoint[] }} linePos
	 */

	startAnimation(linePos: { bezierPoints: IPoint[] }) {
		let index = 0
		const bezierPoints = linePos.bezierPoints.reverse()
		this.hideGoodBox = false
		const timer = setInterval(() => {
			index++
			if (index >= 28) {
				clearInterval(timer)
				this.hideGoodBox = true
			}
		}, 20)
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 前往确认订单页面
	 * @param {SelectGoodInfo} goodDetail
	 */

	gotoSubmit(goodDetail: SelectGoodInfo) {
		const items = [] as any
		items.push({
			activityId: goodDetail.activityId,
			activityProductId: goodDetail.activityProductId,
			dominoState: goodDetail.dominoState,
			number: goodDetail.goodsNumber,
			skuId: goodDetail.skuId
		})
		uni.navigateTo({
			url: `/modules/pages/submit/submit?items=${encodeURIComponent(
				JSON.stringify(items)
			)}`
		})
		this.buyShow = false
	}

	onReady() {
		this.videoContext = uni.createVideoContext('myVideo')
		uni.getSystemInfo({
			success: (res) => {
				//可视窗口宽度
				const ww = res.windowWidth
				//可视窗口高度
				const hh = res.windowHeight
				this.busPos['x'] = ww * 0.5
				this.busPos['y'] = hh * 0.9
				this.finger['x'] = ww * 0.3
				this.finger['y'] = hh * 0.9
			}
		})
	}

	onLoad(options: {
		id: number
		img: string
		goodTranObj?: string
		transData?: string
	}) {
		this.options = options
		if (options.img) {
			this.goodDetail = { ...this.goodDetail, imgUrls: [options.img] }
		}

		if (options.goodTranObj) {
			this.$STORE.userStore.login(1).then(() => {
				getGoodDetail(options.id, {}).then((res) => {
					const goodData = {}
					const newList = (options.goodTranObj
						? options.goodTranObj
						: ''
					).split(' ')
					const transData = {
						shopUserId: newList[3],
						communityId: newList[0],
						groupType: newList[1],
						isGrouper: newList[2],
						goodData: goodData
					}
					options.transData = JSON.stringify(transData)
					this.options = options
				})
			})
		}

		this.getData(options)

		this.$Pubsub.on('app-launch', () => {
			this.getData(options)
		})
	}

	onShow() {
		uni.hideHomeButton()
		this.shareVisible = false
		this.shopInfo = this.$STORE.shopSetStore.shopInfo
		uni.onUserCaptureScreen(() => {
			this.clickShare = false
			this.shareVisible = true
		})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 获取商品列表总体数据
	 * @param {number} id
	 */

	getData(options: { id: number }) {
		this.getGood(options.id)
	}
	/** 获取评价数据 */
	/**
	 * @LastEditors: chuyinlong
	 * @description:
	 * @param {*} productId
	 */

	getProductEvaluate(productId: number) {
		getProductEvaluate({ productId, type: 0, size: 10, current: 1 })
			.then((res) => {
				this.evaluateItem = res.list[0]
			})
			.catch((err) => {
				this.$Popup.toast(err || '评价列表获取失败')
			})
		getProductEvaluateOverview({ productId })
			.then((res) => {
				this.evaluateOverview = res
			})
			.catch((err) => {
				this.$Popup.toast(err || '评价概况获取失败')
			})
	}

	/** 获取商品详情 */
	getGood(id: number) {
		getGoodDetail(id, {})
			.then((res) => {
				res.imgUrls = res.albumPics.split(',')
				res.serviceIds = res.serviceIds.split(',').join(' · ')
				res.detail = res.detail.replace(
					/\<img/g,
					'<img style="width:100%;height:auto;display:block"'
				)

				res.stock = 0
				const type = res.skuStocks.every((v: { stock: number }) => {
					return v.stock === 0
				})
				res.status = type ? 2 : res.status

				// 单规格商品处理显示价格
				if (res.skuStocks.list === 1) {
					res.price = res.price.toFixed(2)
					res.actPrice = res.originalPrice.toFixed(2)
				} else {
					// 多规格商品处理显示价格
					const priceList = res.skuStocks.map(
						(item: { originalPrice: any }) => item.originalPrice
					)
					res.price = Math.min(...priceList).toFixed(2)
					const actPriceList = res.skuStocks.map(
						(item: { price: any }) => item.price
					)
					res.actPrice = Math.min(...actPriceList).toFixed(2)
					res.leadPrice = Math.max(...actPriceList).toFixed(2)
				}
				res.skuStocks.forEach(
					(item: { price: number; stock: any; sale: any }) => {
						item.price = Number(item.price.toFixed(2))
						res.stock += item.stock
						res.sale += item.sale
					}
				)
				res.savePrice = res.actPrice === '0.00' ? res.price : res.actPrice
				res.savePrice = (Number(res.savePrice) - Number(res.memPrice)).toFixed(
					2
				)
				this.setData({
					goodDetail: res,
					goodsVideoUrl: res.videoUrl,
					goodId: id,
					clickShare: true
				})
				this.getCollectType(id)
			})
			.then(() => {
				this.addFooterAdd()
				this.getProductEvaluate(id || 144)
				this.getShopCarNum()
			})
			.catch((err) => {
				// 根据报错查看到商品是哪一相关模块报错
				Dialog.alert({
					title: '提示',
					message: `${err}`
				}).then(() => {
					this.$STORE.setStore.backHome()
				})
				return
			})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description:查看参数
	 */

	getCoupon({
		currentTarget: {
			dataset: { mode }
		}
	}: {
		currentTarget: {
			dataset: { mode: string }
		}
	}) {
		this.show = true
		switch (mode) {
			case 'para':
				this.paraShow = true
				break
			default:
				break
		}
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 页面跳转路径
	 * @param {*} e
	 */

	pageLink(e: { currentTarget: { dataset: { link: string } } }) {
		const url = e.currentTarget.dataset.link
		uni.navigateTo({
			url: `/subcontract/${url}`
		})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description:主图视频
	 * @param {*} e
	 */

	onSlideChange(e: { detail: { current: number } }) {
		this.setData({
			current: e.detail.current + 1
		})
		if (this.current !== 1) {
			this.videoContext.pause()
			this.setData({
				autoplay: true
			})
		}
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 视频播放
	 */
	bofang() {
		this.setData({
			autoplay: false
		})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 视频结束播放
	 */
	endbofng() {
		this.setData({
			autoplay: true
		})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 获取购物车数量
	 */

	async getShopCarNum() {
		const res: GoodInfo[] = await getCarsList()
		const useList: any[] = []
		const noList = []
		const list = res
		if (!res || res.length < 1) {
			this.setData({
				shopCarNum: 0
			})
			return
		}
		// 购物车数量需提出失效商品数量 且数量要计算不同规格的数量总和
		list.forEach((item: GoodInfo) => {
			item.skuStocks.forEach((v: { id: any }) => {
				item.skuList = item.skuList ? item.skuList : []
				item.skuList.push(v.id)
			})
			const type = item.skuStocks.every((v: { stock: number }) => {
				return v.stock === 0
			})
			item.status = type ? 2 : item.status
			if (item.deleted === 1) {
				item.status = 2
			}
			item.skuList = item.skuList ? item.skuList : []
			item.status = item.skuList.indexOf(item.skuId) === -1 ? 0 : item.status
			if (item.activityEndTime) {
				item.status =
					item.activityStatus === '1' || item.activityStatus === '0' ? 1 : 0
			}
			if (item.status === 1) {
				useList.push(item)
			} else {
				noList.push(item)
			}
		})
		const goodsNumber = useList.reduce((preVal, item) => {
			return preVal + item.goodsNumber
		}, 0)
		this.setData({
			shopCarNum: goodsNumber
		})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 添加用户足迹
	 */

	addFooterAdd() {
		const datail = this.goodDetail
		if (datail.id) {
			const parma = {
				originalPrice: datail.price,
				productId: datail.id,
				productName: datail.name,
				productPic: datail.pic,
				productPrice: datail.actPrice,
				status: datail.status
			}
			userFooterAdd(parma)
		}
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 查看图片
	 */

	handleSeePhoto({
		currentTarget: {
			dataset: { item }
		}
	}: {
		currentTarget: {
			dataset: { item: { value: string } }
		}
	}) {
		uni.previewImage({ current: item.value, urls: [item.value] })
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 查看此商品是否有被收藏
	 * @param {number} productId
	 */

	getCollectType(productId: number) {
		checkIsCollect({ productId }).then((res) => {
			const goodDetail = this.goodDetail
			goodDetail.isCollect = res
			this.setData({
				goodDetail
			})
		})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 添加收藏
	 */

	addCollect() {
		const datail = this.goodDetail
		const parma = {
			originalPrice: datail.price,
			productId: datail.id,
			productName: datail.name,
			productPic: datail.pic,
			productPrice: datail.actPrice,
			status: datail.status
		}
		const list = [] as any
		list.push(parma)
		if (datail.isCollect) {
			this.cancelCollect(parma)
			return
		}
		userAddCollect(list)
			.then(() => {
				uni.showToast({ title: '收藏成功', icon: 'none' })
				let goodDetail = this.goodDetail
				goodDetail.isCollect = true
				this.setData({
					goodDetail: Object.assign({}, goodDetail)
				})
			})
			.catch((err) => {
				console.log(err)
			})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description:取消收藏
	 * @param {*} parma
	 */

	cancelCollect(parma: {
		originalPrice: string | number | undefined
		productId: number
		productName: string
		productPic: string
		productPrice: string | number | undefined
		status: number
	}) {
		userDelCollect(parma).then(() => {
			uni.showToast({ title: '取消成功', icon: 'none' })
			let goodDetail = this.goodDetail
			goodDetail.isCollect = false
			this.setData({
				goodDetail: Object.assign({}, goodDetail)
			})
		})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 确认授权回调
	 */
	authClick() {
		this.setData({
			authType: false
		})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 取消授权
	 * @param {boolean} e
	 */
	cancel(e: boolean) {
		this.setData({
			authType: e
		})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 弹起底部规格选择
	 */

	submit() {
		// 新用户选择规格需先登录
		const userInfo = this.$STORE.userStore.userInfo
		if (!userInfo.whetherAuthorization) {
			this.setData({
				authType: true
			})
			return
		}

		const goodDetail = this.goodDetail

		let norms = {} as any
		const list = goodDetail.skuStocks
		if (this.getNorms.id) {
			norms = goodDetail.skuStocks[0]
			norms.pic =
				list.length === 1 ? goodDetail.pic : goodDetail.skuStocks[0].pic
			norms.price =
				String(goodDetail.actPrice) !== '0.00'
					? goodDetail.actPrice
					: goodDetail.price
			if (this.getNorms.id) {
				norms = this.getNorms
			}
		} else {
			norms.pic = goodDetail.albumPics.split(',')[0]
			if (String(goodDetail.actPrice) !== '0.00') {
				norms.price = goodDetail.actPrice
					? goodDetail.actPrice
					: goodDetail.price
			} else {
				norms.price = goodDetail.price
			}
			norms.maxNum =
				goodDetail.skuStocks[0].perLimit === 0
					? goodDetail.skuStocks[0].stock
					: goodDetail.skuStocks[0].perLimit
			norms.id = ''
			norms.memPrice = goodDetail.memPrice
		}
		this.setData({
			norms,
			buyShow: true
		})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 关闭卡片
	 */

	hiddenCrad() {
		this.setData({
			cardFlag: true
		})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 查看大图
	 */

	cardClick() {
		uni.canvasToTempFilePath({
			canvasId: 'cardCanvas',
			success: (res) => {
				this.setData({
					cardFlag: true
				})
				const tempFilePath = res.tempFilePath
				uni.previewImage({
					current: tempFilePath,
					urls: [tempFilePath]
				})
			}
		})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 保存图片
	 */
	savePhoto() {
		uni.getSetting({
			success: (res) => {
				if (res.authSetting['scope.writePhotosAlbum'] === false) {
					uni.showModal({
						title: '',
						content: '请在设置中开启保存相册功能后再次保存图片',
						success() {
							uni.openSetting({})
						}
					})
				} else {
					uni.saveImageToPhotosAlbum({
						filePath: this.cardImgUrl,
						success() {
							uni.showToast({
								title: '保存成功',
								icon: 'none'
							})
						}
					})
				}
			}
		})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 保存卡片分享
	 */

	shareBrCard() {
		const url = require('./canvas.ts')
		let codeImg = ''
		this.showLoad()
		uni.pageScrollTo({
			scrollTop: 0,
			duration: 200
		})
		this.setData({
			cardFlag: false,
			shareVisible: false
		})
		let wxaGetwxacode: object = {}
		const transData = this.$STORE.userStore.userInfo.transData
		transData.goodData.launchArea = this.goodDetail.launchArea
		transData.goodData.id = this.goodDetail.id
		const goodTranObj = `${transData.communityId} ${transData.groupType} ${transData.isGrouper} ${this.$STORE.userStore.userInfo.shopUserId}`
		wxaGetwxacode = {
			path: `subcontract/pages/detail/detail?id=${this.goodDetail.id}&goodTranObj=${goodTranObj}`,
			width: 0
		}
		getImgCode(wxaGetwxacode).then((result) => {
			codeImg = result
			base64src(codeImg, (res) => {
				codeImg = res
				uni.downloadFile({
					url: this.goodDetail.pic,
					success: (v) => {
						URL.methods
							.canvCard(this.goodDetail, 0, v.tempFilePath, codeImg)
							.then((response) => {
								this.setData({
									cardImgUrl: response,
									showAll: true
								})
								this.showLoad()
							})
					}
				})
			})
		})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 显示图片加载弹窗
	 */
	showLoad() {
		if (!this.cardImgUrl) {
			uni.showLoading({
				title: '图片生成中'
			})
		} else {
			uni.hideLoading()
		}
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 禁止滚动穿透
	 */

	preventTouchMove() {
		return
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 获取分享信息
	 * @returns {{title:string,path:string,imageUrl:string}}
	 */
	getShareData(): { title: string; path: string; imageUrl: string } {
		return {
			title: this.goodDetail.name,
			path: `/subcontract/pages/detail/detail?id=${this.goodDetail.id}`,
			imageUrl: this.imgUrl
		}
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description:小程序分享
	 */

	onShareAppMessage() {
		return this.getShareData()
	}
}
</script>
<style lang="scss" scoped>
@import '@/assets/styles/detail/detail.scss';
</style>
