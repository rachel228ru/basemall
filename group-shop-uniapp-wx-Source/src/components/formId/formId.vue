<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-12 15:36:20
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-14 15:20:44
 * 123
-->
<template>
	<view>
		<form report-submit @submit="getFormId">
			<button class="formid__button" form-type="submit">
				<slot></slot>
			</button>
		</form>
	</view>
</template>
<script lang="ts">
import { Component, Vue } from 'vue-property-decorator'

@Component
export default class FormId extends Vue {
	/**
	 * @LastEditors: chuyinlong
	 * @description: 提交表单
	 * @param {string | number} e
	 * @returns {*}
	 */

	getFormId(e: { detail: { formId: string | number } }) {
		this.$emit('click', {
			formId: e.detail.formId
		})
	}
}
</script>
<style scoped></style>
