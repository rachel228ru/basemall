<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-12 15:36:21
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-18 20:55:56
 * 123
-->
<template>
	<!-- 搜索组件 -->
	<view class="page">
		<view class="page__box  van-search">
			<view class="page__header  " @tap="toPop" :style="style">
				<van-icon name="search" class="page__header__icon"></van-icon>
				{{ query.name ? query.name : formData.keyWord }}
			</view>
		</view>
		<van-popup :show="show" position="top" custom-style="height: 100%;">
			<view style="display:flex">
				<view style="line-height:50px;padding-left:20px;">
					<van-icon name="apps-o" @tap="close"></van-icon>
				</view>
				<van-search
					:value="query.name"
					@change="onChangePop"
					:placeholder="formData.keyWord"
					use-action-slot
					style="flex:1"
				>
					<view slot="action" @tap="onClick" class="page__pop--search"
						>搜索</view
					>
				</van-search>
			</view>
			<van-cell title="热门推荐"></van-cell>
			<view>
				<view
					v-for="(item, index) in formData.hotWord"
					:key="index"
					class="page__item"
				>
					<text class="page__item--word" @tap="choose" :data-variable="item">{{
						item
					}}</text>
				</view>
			</view>
			<van-cell title="历史搜索"></van-cell>
			<view>
				<view
					v-for="(item, index) in historyWords"
					:key="index"
					class="page__item"
				>
					<text class="page__item--word" @tap="choose" :data-variable="item">{{
						item
					}}</text>
				</view>
			</view>
		</van-popup>
	</view>
</template>

<script lang="ts">
import { Component, Vue, Prop, Watch } from 'vue-property-decorator'
import { SearchProp, SearchState } from './searchComp'
@Component({})
export default class SearchComp extends Vue implements SearchState {
	@Prop({
		type: Object
	})
	decoretionProperties!: SearchProp

	style = ''
	formData = {} as SearchProp
	historyWords: string[] = []
	name = ''
	show = false
	query = {
		name: ''
	}
	/** 搜索范围 2商超 */
	scope = 2

	@Watch('decoretionProperties', {
		deep: true,
		immediate: true
	})
	rechformdata() {
		this.getFormData()
	}

	// 组件周期函数--监听组件挂载完毕
	mounted() {
		const historyWords = uni.getStorageSync('historyWords')
			? JSON.parse(uni.getStorageSync('historyWords'))
			: []
		this.historyWords = historyWords
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 动态获取样式
	 * @param {string} v
	 * @returns {string}
	 */

	getListSytle(v: string): string {
		let style = ''
		switch (v) {
			case 'is-style-one':
				style =
					'background-color: #eeeeee;border: 1px solid transparent;border-radius: 44px;'
				break
			case 'is-style-two':
				style =
					'background-color: #eeeeee;border: 1px solid transparent;border-radius: 4px;'
				break
			case 'is-style-three':
				style = 'border: 1px solid #eeeeee;border-radius: 44px;'
				break
		}
		return style
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 获取配置控件数据
	 * @param {*}
	 * @returns {*}
	 */

	getFormData(): void {
		const style = this.decoretionProperties.showStyle
		this.formData = this.decoretionProperties
		this.style = this.getListSytle(style)
		this.scope = 2
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 搜索
	 * @param {*}
	 * @returns {*}
	 */

	onSearch(): void {
		const scope = this.scope
		const name = this.query.name
		const val = {
			name,
			scope
		}
		this.$emit('onSearch', val, {})
		this.show = false
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 前往搜索页面
	 * @param {*}
	 * @returns {*}
	 */

	toPop(): void {
		uni.navigateTo({
			url: `/subcontract/pages/Search/Search?chanel=4&formData=${JSON.stringify(
				this.formData
			)}`
		})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 打开弹窗
	 * @param {*}
	 * @returns {*}
	 */

	onOpen(): void {
		const val = this.query.name
		this.$emit('onSearch', val, {})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 关闭弹窗
	 * @param {*}
	 * @returns {*}
	 */

	close(): void {
		this.show = false
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 输入更新
	 * @param {string} e
	 * @returns {*}
	 */

	onChangePop(e: string): void {
		this.query.name = e
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 发起搜索
	 * @param {*}
	 * @returns {*}
	 */

	onClick(): void {
		const val = this.query.name
		this.addStoreage(val)
		this.show = false
		this.$emit('search', val, {})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 选择历史或热门
	 * @param {string} event.currentTarget.dataset.variable
	 * @returns {*}
	 */

	choose(event: { currentTarget: { dataset: { variable: string } } }): void {
		const val = event.currentTarget.dataset.variable
		this.query.name = val
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 历史记录搜索放入storeage
	 * @param {string} value
	 * @returns {*}
	 */

	addStoreage(value: string): void {
		const historyWords: string[] = uni.getStorageSync('historyWords')
			? JSON.parse(uni.getStorageSync('historyWords'))
			: []
		if (
			!historyWords.includes(value) &&
			value &&
			this.formData &&
			!this.formData.hotWord.includes(value)
		) {
			historyWords.unshift(value)
		}

		if (historyWords.length > 6) {
			historyWords.pop()
		}
		this.historyWords = historyWords
		const words = JSON.stringify(historyWords)
		uni.setStorageSync('historyWords', words)
	}
}
</script>

<style lang="scss" scoped>
.page {
	&__header {
		height: 36px;
		display: inline-block;
		display: flex;
		align-items: center;
		font-size: 14px;
		width: 100%;

		&__icon {
			font-size: 14px;
			margin: 0 10px;
		}
	}

	&__box {
		background: #fff;
		height: 46px;
		width: 100%;
		padding: 5px 20px;
	}

	.page__pop--search {
		color: #fff;
		font-weight: 800;
		margin-right: 10px;
		background: #fe5468;
		padding: 0 20px;
		border-radius: 20px;
	}

	.page__item {
		width: 33%;
		padding: 7px 10px;
		float: left;
		text-align: center;

		&--word {
			display: block;
			width: 80%;
			white-space: nowrap;
			text-overflow: ellipsis;
			overflow: hidden;
			border-radius: 20px;
			background: #f5f5f5;
			font-size: 14px;
			padding: 10px 10px;
			text-align: center;
		}
	}
}
</style>
