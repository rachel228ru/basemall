<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-12 15:36:21
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-20 09:39:11
 * 123
-->
<template>
	<view class="page">
		<view style="display:flex">
			<van-search
				:value="queryName"
				@change="onChangePop"
				:placeholder="keyWord"
				use-action-slot
				style="flex:1"
			>
				<view slot="action" @tap="onClick" class="page__pop--search">搜索</view>
			</van-search>
		</view>
		<van-cell v-if="hotWord.length" title="热门推荐"></van-cell>
		<view v-if="hotWord.length">
			<view v-for="(item, index) in hotWord" :key="index" class="page__item">
				<text class="page__item--word" @tap="choose" :data-variable="item">{{
					item
				}}</text>
			</view>
		</view>
		<van-cell title="历史搜索">
			<van-icon
				slot="right-icon"
				name="clear"
				class="custom-icon"
				@tap="closeChoose"
			></van-icon>
		</van-cell>
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
	</view>
</template>

<script lang="ts">
import { SearchProp } from '@/pages/index/modules/home/components/search-comp/searchComp'
import { Component, Vue } from 'vue-property-decorator'

@Component({})
export default class Search extends Vue {
	historyWords: Array<string> = [] // 历史搜索词
	hotWord: Array<string> = [] // 热门关键词
	queryName: string = '' // 搜索词
	chanel: number = 2 //  2商超分类页  4首页搜索组件
	keyWord: string = '' // 输入框若提示

	onLoad(option: { chanel: number; formData: string }) {
		const historyWords = uni.getStorageSync('historyWords')
			? JSON.parse(uni.getStorageSync('historyWords'))
			: []
		let hotWord: string[] = []
		let keyWord = ''
		const chanel = Number(option.chanel)
		// 如果来自首页搜索组件，需要带上formate
		if (chanel === 4) {
			const params: SearchProp = JSON.parse(option.formData || '{}')
			hotWord = params.hotWord || []
			keyWord = params.keyWord || ''
		}
		this.setData({
			historyWords,
			hotWord: hotWord.filter((i) => i !== ''),
			keyWord,
			chanel
		})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 输入值改变时实时更新
	 * @param {*} e
	 */

	onChangePop(e: { detail: string }) {
		this.setData({
			queryName: e.detail
		})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 点击搜索
	 */

	onClick() {
		this.addStoreage()
		this.queryName = this.queryName === '' ? this.keyWord : this.queryName
		uni.navigateTo({
			url: `/subcontract/pages/spellPage/second-catgoods/second-catgoods?chanel=${this.chanel}&queryName=${this.queryName}`
		})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 选定搜索词
	 * @param {*} event
	 */

	choose(event: { currentTarget: { dataset: { variable: string } } }) {
		const queryName = event.currentTarget.dataset.variable
		this.setData(
			{
				queryName
			},
			() => {
				this.onClick()
			}
		)
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 清除按钮
	 */

	closeChoose() {
		let historyWords: string[] = uni.getStorageSync('historyWords')
			? JSON.parse(uni.getStorageSync('historyWords'))
			: []
		if (historyWords.length > 0) {
			historyWords = []
			uni.removeStorageSync('historyWords')
			this.setData({
				historyWords
			})
			uni.showToast({
				title: '删除成功',
				icon: 'none',
				duration: 800
			})
		}
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 历史记录搜索放入storeage
	 */

	addStoreage() {
		const value = this.queryName
		const historyWords: string[] = uni.getStorageSync('historyWords')
			? JSON.parse(uni.getStorageSync('historyWords'))
			: []
		if (historyWords.findIndex((i) => i === value) !== -1) {
			historyWords.splice(
				historyWords.findIndex((i) => i === value),
				1
			)
		}
		if (!historyWords.includes(value) && value) {
			historyWords.unshift(value)
		}
		if (historyWords.length > 6) historyWords.pop()
		this.setData({
			historyWords
		})
		uni.setStorageSync('historyWords', JSON.stringify(historyWords))
	}
}
</script>

<style lang="scss" scoped>
.page {
	background: #fff;
	height: 100vh;
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

.custom-icon {
	color: #bbbbbb;
	font-size: large;
}
</style>
