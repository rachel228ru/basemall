<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-12 15:36:21
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-18 20:56:15
 * 123
-->
<template>
	<view class="page">
		<view class="page__box  van-search">
			<view class="page__header  " @tap="toPop">
				<van-icon name="search" class="page__header__icon"></van-icon>
				{{ query.name ? query.name : '请输入关键字搜索' }}
			</view>
		</view>
	</view>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator'
import { ISearchPlugin } from './searchPlugin'

@Component({})
export default class SearchPlugin extends Vue implements ISearchPlugin {
	name: String = ''
	show: Boolean = false
	query = {
		name: ''
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 跳转搜索二级页面
	 * @param {*}
	 * @returns {*}
	 */

	toPop(): void {
		uni.navigateTo({
			url: '/subcontract/pages/Search/Search'
		})
	}
}
</script>

<style lang="scss" scoped>
.page {
	&__header {
		border-radius: 40px;
		height: 70rpx;
		display: inline-block;
		display: flex;
		align-items: center;
		background-color: #f8f8f8;
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
