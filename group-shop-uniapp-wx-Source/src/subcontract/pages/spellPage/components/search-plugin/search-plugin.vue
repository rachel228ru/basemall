<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-12 15:36:21
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-20 10:08:54
 * 123
-->
<template>
	<view class="page">
		<view class="page__box  van-search">
			<view class="page__header  " @tap="toPop">
				<van-icon name="search" class="page__header__icon"></van-icon>
				{{ queryName || '请输入关键字搜索' }}
			</view>
		</view>
	</view>
</template>

<script lang="ts">
import { Component, Vue, Prop } from 'vue-property-decorator'

@Component({})
export default class SearchPlugin extends Vue {
	@Prop() queryName!: string
	@Prop() chanel!: number //  2商超分类页  4首页搜索组件  12商超分类页二级分类导航
	@Prop() pageType!: number // 页面标识 1商品列表页

	name: string = ''
	show: boolean = false

	/**
	 * @LastEditors: chuyinlong
	 * @description: 跳转搜索二级页面
	 */

	toPop() {
		if ([12].includes(this.chanel)) {
			let chanel = this.chanel
			switch (chanel) {
				case 12:
					chanel = 2
			}
			uni.navigateTo({
				url: `/subcontract/pages/Search/Search?chanel=${chanel}`
			})
		} else {
			uni.navigateBack({})
		}
	}
}
</script>

<style lang="scss" scoped>
.page {
	&__header {
		border-radius: 40px;
		height: 40px;
		display: inline-block;
		display: flex;
		align-items: center;
		background-color: #f8f8f8;
		font-size: 14px;
		width: 100%;

		&__icon {
			font-size: 14px;
			margin: 0 20px;
		}
	}

	&__box {
		background: #fff;
		height: 60px;
		width: 100%;
		padding: 5px 10px;
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
