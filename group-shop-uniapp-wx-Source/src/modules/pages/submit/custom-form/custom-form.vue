<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-12 15:36:20
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-09-01 16:00:50
 * 123
-->
<template>
	<view>
		<van-cell-group>
			<block v-for="(item, index) in localForms" :key="index">
				<van-field
					v-if="helper.isInputItem(item.type)"
					:key="index"
					:data-index="index"
					:label="item.key"
					:value="item.value"
					:required="item.required"
					:placeholder="item.placeholder"
					@change="onChange"
					:border="false"
				></van-field>
				<van-cell
					v-else-if="helper.isDateItem(item.type)"
					@click="onDateItemClick"
					:data-item="item"
					:data-index="index"
					:title="item.key"
					:required="item.required"
					is-link
					:value="item.time ? item.time : item.placeholder"
					:border="false"
				></van-cell>
				<van-cell
					v-else
					@click="handleTakePhoto"
					:data-item="item"
					:data-index="index"
					:title="item.key"
					:required="item.required"
					is-link
					:value="item.value ? '已上传' : item.placeholder"
					:border="false"
				></van-cell>
			</block>
		</van-cell-group>
		<van-popup
			:show="datePopVisible"
			position="bottom"
			custom-style="height: 40%;"
			@close="onClose"
		>
			<van-datetime-picker
				:type="currentFrom.type || 'date'"
				:value="currentValue"
				@input="onDateInput"
				@cancel="onClose"
				@confirm="onDateConfirm"
			></van-datetime-picker>
		</van-popup>
	</view>
</template>
<script module="helper" lang="wxs" src="@/wxs/customForm.wxs"></script>
<script lang="ts">
import { Component, Vue, Prop, Watch } from 'vue-property-decorator'
import { IFromItem } from './form.schema'
import DateUtil from '@/mixins/date'
import { uploadFile } from '@/utils/upload'
import { CustomFormType } from '@/modules/pages/submit/interface'
@Component
export default class CustomForm extends Vue {
	@Prop()
	forms!: Array<CustomFormType>

	localForms: CustomFormType[] = []
	datePopVisible: boolean = false
	currentValue: number = new Date().getTime()
	currentFrom: IFromItem = {} as IFromItem
	currentIndex: number = 0

	/** 日期选择配置 */
	minHour: number = 10
	maxHour: number = 20
	minDate: number = new Date().getTime()
	maxDate: number = new Date(2019, 10, 1).getTime()
	isFirstEnter: boolean = false

	created() {
		this.formsChange(this.forms)
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 监听forms变化
	 * @param {*}
	 * @returns {*}
	 */

	@Watch('forms')
	setForms(): void {
		this.formsChange(this.forms)
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 将数据设置到页面
	 * @param {CustomFormType[]} newValue
	 */

	formsChange(newValue: CustomFormType[]): void {
		if (!this.isFirstEnter) {
			this.transformFormToData(newValue)
			this.setData({ isFirstEnter: true })
		}
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description:
	 * @param {*} forms
	 * @returns {*}
	 */

	transformFormToData(forms: CustomFormType[]): void {
		const localForms = forms.map((item) => {
			item.value = ''
			return item
		})
		this.setData({ localForms })
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 点击触发
	 * @param {{ index:number, item:CustomFormType }} e
	 * @returns {*}
	 */

	onDateItemClick({
		currentTarget: {
			dataset: { index, item }
		}
	}: {
		currentTarget: {
			dataset: { index: number; item: CustomFormType }
		}
	}): void {
		this.setData({
			currentFrom: item,
			currentIndex: index,
			currentValue:
				item.value || item.type === 'time' ? '12:00' : new Date().getTime()
		})
		this.setDatePopVisible(true)
	}
	/** 表单变化 */
	/**
	 * @LastEditors: chuyinlong
	 * @description: 表单变化
	 * @param {*}
	 * @returns {*}
	 */

	onChange({
		detail,
		currentTarget: {
			dataset: { index }
		}
	}: {
		detail: string
		currentTarget: {
			dataset: { index: number }
		}
	}): void {
		this.localForms[index].value = detail
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 关闭弹窗
	 * @param {*}
	 * @returns {*}
	 */

	onClose(): void {
		this.setDatePopVisible(false)
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 弹窗控制方法
	 * @param {boolean} datePopVisible
	 * @returns {*}
	 */
	setDatePopVisible(datePopVisible: boolean): void {
		this.setData({ datePopVisible })
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 时间选择确认
	 * @param {*}
	 * @returns {*}
	 */

	onDateConfirm(): void {
		let time = ''
		const dateUtil = new DateUtil(this.currentValue)
		switch (this.currentFrom.type) {
			case 'date':
				time = dateUtil.getYMDs()
				break
			case 'time':
				time = this.currentValue as any
				break
			case 'datetime':
				time = dateUtil.getYMDHMSs()
		}

		this.setData({
			[`localForms[${this.currentIndex}].value`]: this.currentValue,
			[`localForms[${this.currentIndex}].time`]: time
		})
		this.setDatePopVisible(false)
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 拍照
	 * @param {*}
	 * @returns {*}
	 */

	handleTakePhoto({
		currentTarget: {
			dataset: { index }
		}
	}: {
		currentTarget: {
			dataset: { index: number }
		}
	}): void {
		uni.chooseImage({
			count: 1,
			sizeType: ['original', 'compressed'],
			sourceType: ['album', 'camera'],
			success: ({ tempFilePaths: [url] }) => {
				uploadFile(url)
					.then((res) => {
						this.localForms[index].value = res
					})
					.catch(() => {
						this.$Popup.toast('上传失败')
					})
			}
		})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: date表单变化
	 * @param {number} detail
	 * @returns {*}
	 */

	onDateInput({ detail: currentValue }: { detail: number }): void {
		this.setData({ currentValue })
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 部分表单验证
	 * @param {CustomFormType} form
	 * @returns {*}
	 */
	verificationField(form: CustomFormType): boolean {
		switch (form.type) {
			case 'id':
				return this.$global.isIDnumber(form.value)
			case 'phone':
				return this.$global.isPhone(form.value)
			case 'number':
				return this.$global.isNumber(form.value)
			default:
				return true
		}
	}

	/** 验证表单 */
	/**
	 * @LastEditors: chuyinlong
	 * @description: 验证表单
	 * @param {*}
	 * @returns {*}
	 */

	verificationForm(): boolean {
		const emptyItem = this.localForms
			.filter((item) => item.required)
			.find((item) => !item.value)

		if (emptyItem) {
			this.$Popup.toast(`${emptyItem.key}不可为空`)
			return false
		}

		const errorItem = this.localForms.find(
			(item) => !!item.value && !this.verificationField(item)
		)

		if (errorItem) {
			this.$Popup.toast(`请填写正确的${errorItem.key}`)
			return false
		}

		return true
	}
}
</script>
<style scoped></style>
