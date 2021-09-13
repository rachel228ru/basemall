<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-12 15:36:20
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-30 14:22:56
 * 123
-->
<template>
	<view class="stepperBox">
		<view class="stepperBox__redu" @tap.stop="reduceNum">
			<m-icon name="icondelete" v-if="num <= 1" size="12px"></m-icon>
			<view class="stepperBox__redu--del" v-else></view>
		</view>
		<view class="stepperBox__input">
			<input
				maxlength="8"
				style="padding-left:12px;"
				@blur="inputBlur"
				v-model="num"
				type="number"
			/>
		</view>
		<m-icon
			name="icongouwuche-jia"
			size="23px"
			style="color:#FC425A"
			@tap="addNum"
		></m-icon>
	</view>
</template>
<script lang="ts">
import { Component, Vue, Prop, Watch } from 'vue-property-decorator'
import mIcon from '@/components/m-icon/m-icon.vue'
import { StepperState, StepperGood } from './stepperType'
@Component({
	components: {
		mIcon
	}
})
export default class Stepper extends Vue implements StepperState {
	@Prop()
	good: StepperGood
	@Prop()
	index: number

	num: number = 0
	stockNum: number = 0
	perLimit: number = 0
	value: string = ''
	canClick: boolean = false

	created() {
		this.goodChange()
	}
	/**
	 * @LastEditors: chuyinlong
	 * @description: 监听good数据变动
	 * @param {*}
	 * @returns {*}
	 */

	@Watch('good', { deep: true })
	goodChange(): void {
		if (this.good) {
			const good = this.good
			let perLimit = this.perLimit
			let stockNum = this.stockNum

			good.skuStocks.forEach((item) => {
				if (item.id === good.skuId) {
					good.maxNum = !item.perLimit
						? item.stock
						: item.perLimit < item.stock
						? item.perLimit
						: item.stock
					perLimit = !item.perLimit ? item.stock : item.perLimit
					stockNum = item.stock
				}
			})
			this.good = good
			this.perLimit = perLimit
			this.stockNum = stockNum
			this.num = Number(good.goodsNumber)
		}
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 监听输入框输入同步处理数据
	 * @param {number} value
	 */

	inputBlur(e: { detail: { value: number } }): void {
		const value = Number(e.detail.value)
		let code = 0
		if (value < 1) {
			this.num = 1
		} else {
			this.num = value
		}
		const initNum = this.num
		const perLimit = this.perLimit
		const stockNum = this.stockNum
		const selectType = stockNum > perLimit
		if (selectType) {
			if (perLimit === 0) {
				if (value >= stockNum + 1) {
					code = 200
					this.changeNum(perLimit, code)
					this.num = initNum
					return
				}
			} else {
				if (value >= perLimit + 1) {
					uni.showToast({
						title: '您已达限购数量'
					})
					code = 100
					this.changeNum(perLimit, code)
					this.num = perLimit
					return
				}
			}
		} else {
			if (value >= stockNum + 1) {
				uni.showToast({
					title: '库存不足'
				})
				code = 200
				this.changeNum(stockNum, code)
				this.num = stockNum
				return
			}
		}
		if (value < 1) {
			this.changeNum(1, code)
			this.num = 1
		} else {
			this.changeNum(value, code)
			this.num = value
		}
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 减少数量
	 * @param {*}
	 * @returns {*}
	 */

	reduceNum(): void {
		if (this.canClick) {
			// 加载过程中阻止用户再次点击
			return
		}
		this.canClick = true
		const newNum = this.num - 1
		if (newNum === 0) {
			this.delcGood()
			this.canClick = false
			return
		}
		this.changeNum(newNum, 0)
		this.num = newNum
		this.canClick = false
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 增加数量
	 * @param {*}
	 * @returns {*}
	 */

	addNum(): void {
		if (this.canClick) {
			// 加载过程中阻止用户再次点击
			return
		}
		this.canClick = true

		const newNum = +this.num + 1
		const perLimit = this.perLimit
		const stockNum = this.stockNum
		let code = 0
		if (perLimit === 0) {
			if (newNum >= stockNum + 1) {
				code = 200
				this.changeNum(stockNum, code)
				this.canClick = false
				return
			}
		} else {
			if (newNum >= perLimit + 1) {
				code = 100
				this.changeNum(perLimit, code)
				this.canClick = false
				return
			}
		}
		this.changeNum(newNum, code)

		this.num = newNum
		this.canClick = false
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description:
	 * @param {number} newNum
	 * @param {number} code
	 * @returns {*}
	 */

	changeNum(newNum: number, code: number): void {
		let index = this.index
		this.$emit('changeNum', {
			num: Number(newNum) <= 0 ? 1 : newNum,
			code,
			index
		})
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 删除商品
	 * @param {*}
	 * @returns {*}
	 */

	delcGood(): void {
		this.$emit('delGood', this.index)
	}
}
</script>
<style lang="scss" scoped>
.stepperBox {
	display: flex;
	align-items: center;

	&__redu {
		width: 21px;
		height: 21px;
		border-radius: 50px;
		border: 1px solid #7d7d7d;
		display: flex;
		justify-content: center;
		align-items: center;
		padding-bottom: 2px;

		&--del {
			width: 10px;
			height: 2px;
			background-color: #fd435b;
		}
	}

	&__input {
		width: 40px;
	}
}
</style>
