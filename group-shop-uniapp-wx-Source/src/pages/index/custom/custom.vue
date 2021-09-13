<!--
 * @description: 
 * @Author: vikingShip
 * @Date: 2021-07-28 09:25:02
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-09-01 16:01:04
-->
<template>
	<home ref="Home" isCustom="true"></home>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator'
import home from '../modules/home/home.vue'
import { getDecorationAll } from '@/api/modules/decoration'
import { ITemplateOnePage } from '@/typings/template'

@Component({
	components: {
		home
	}
})
export default class custom extends Vue {
	onLoad() {
		this.getDecorationAllList()
		this.$STORE.setStore.setHasInCustom(true)
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 获取装修列表
	 * @param {*}
	 * @returns {*}
	 */
	async getDecorationAllList(): Promise<void> {
		try {
			const res = await getDecorationAll()
			const data = res.data || res
			const pages = data.pages
			this.parseAssembleList(pages)
		} catch (e) {
			uni.showToast({
				title: `${e}`,
				icon: 'none'
			})
		}
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 设置装修数据
	 * @param {ITemplateOnePage[]} pageList
	 * @returns {*}
	 */
	parseAssembleList(pageList: ITemplateOnePage[]): void {
		let assembleList = [] as ITemplateOnePage[]
		const nid = this.$STORE.setStore.newCurrentPageId
		assembleList = pageList.filter((item) => Number(item.id) === Number(nid))
		uni.setNavigationBarTitle({
			title: `${assembleList[0].pageName}`
		})
	}
}
</script>

<style></style>
