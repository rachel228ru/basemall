<!--
 * @description: 
 * @Author: vikingShip
 * @Date: 2021-07-28 09:25:02
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-30 14:09:17
-->
<template>
	<view class="data__empty--order">
		<image
			class="order--image"
			src="http://medusa-small-file.oss-cn-hangzhou.aliyuncs.com/images/empty__order.png"
		>
		</image>
		<view class="order--tips">{{ tips || '暂无任何订单记录~~' }}</view>
		<block v-if="infoVisible">
			<view class="order--btn" @tap="goIndex">去下单</view>
		</block>
	</view>
</template>

<script lang="ts">
import { Component, Vue, Prop } from 'vue-property-decorator'

@Component({})
export default class DataEmpty extends Vue {
	@Prop({
		default: true
	})
	infoVisible!: Boolean
	@Prop({
		default: ''
	})
	tips!: string

	goIndex() {
		this.$STORE.setStore.backHome()
	}
}
</script>

<style lang="scss" scoped>
@include b(data) {
	@include e(empty) {
		@include m(order) {
			@include flex(flex-start, center);

			width: 100vw;
			height: calc(100vh - 40px);
			min-height: 470px;
			flex-direction: column;
			background: #fff;
			padding-top: 30px;

			.order--img {
				width: 180px;
			}

			.order--tips {
				color: #4f4f4f;
				margin: 30px 0;
			}

			.order--btn {
				$c: #ec5b54;
				width: 85px;
				height: 45px;
				border: 1px solid $c;
				color: #4f4f4f;
				text-align: center;
				line-height: 45px;
				border-radius: 35px;
				color: $c;
			}
		}
	}
}
</style>
