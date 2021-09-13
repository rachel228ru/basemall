<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-12 15:36:20
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-16 09:44:13
 * 123
-->
<template>
	<view>
		<van-popup
			:show="shoptype"
			custom-style="height: 44%; width:75%;border-radius: 10px;top:40%"
		>
			<view class="shopContent">
				<image
					class="shopContent__img"
					src="http://medusa-small-file.oss-cn-hangzhou.aliyuncs.com/api/proofing.png"
				>
				</image>
				<view class="shopContent__explain" v-if="shopStatus === 5"
					>店铺已打烊，于明日{{ shopTime }}正常营业</view
				>
				<view class="shopContent__explain" v-if="shopStatus === 4"
					>程序系统维护中</view
				>
				<view class="shopContent__explain" v-if="shopStatus === 3"
					>店铺已打烊，于明日{{ shopTime }}正常营业</view
				>
				<!-- 1部署中 2正常 ，3已打烊，4禁用, 5时间到了关门 -->
			</view>
		</van-popup>
	</view>
</template>

<script lang="ts">
import { Component, Vue, Prop } from 'vue-property-decorator'

@Component({})
export default class ShopManage extends Vue {
	@Prop({
		type: Boolean,
		default: false
	})
	shoptype: Boolean
	@Prop({
		type: String
	})
	shopTime: string = ''
	@Prop({
		type: Number || String
	})
	shopStatus: Number | String
}
</script>

<style></style>
