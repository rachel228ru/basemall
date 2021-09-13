<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-12 15:36:20
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-30 14:14:49
 * 123
-->
<template>
	<view>
		<van-popup
			:show="authType"
			position="bottom"
			@close="cancel"
			custom-style="height: 200px; width:100%;border-top-right-radius: 20px;border-top-left-radius: 20px"
		>
			<view class="authContent">
				<view class="authContent__top">登录后可继续当前操作</view>
				<button
					v-if="!canIUseGetUserProfile"
					open-type="getUserInfo"
					@getuserinfo="getUserInfo"
					class="authContent__button"
				>
					<m-icon
						name="icon75"
						size="22px"
						style="margin-right:5px;margin-top:2px"
					></m-icon>
					立即登录
				</button>
				<button v-else @tap="getUserProfile" class="authContent__button">
					<m-icon
						name="icon75"
						size="22px"
						style="margin-right:5px;margin-top:2px"
					></m-icon>
					立即登录
				</button>
				<view class="authContent__noContent" @tap="cancel">暂不登录</view>
			</view>
		</van-popup>
	</view>
</template>
<script lang="ts">
import { Component, Vue, Prop } from 'vue-property-decorator'
@Component
export default class Auth extends Vue {
	@Prop()
	authType!: boolean
	@Prop()
	state!: string
	canIUseGetUserProfile: boolean = false

	mounted() {
		this.$STORE.setStore.setTabVisible(!this.authType)
		if (uni.getUserProfile as any) {
			this.canIUseGetUserProfile = true
		}
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 取消授权
	 */

	cancel(): void {
		const state = this.state
		if (state === 'home') {
			this.$emit('getLocation', false)
			return
		}
		if (state === 'me') {
			this.$emit('cancel', false)
			return
		}
		this.$emit('cancel', false)
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 老版本登录
	 * @param {UniApp.GetUserInfoRes} detail
	 */

	async getUserInfo({ detail }: { detail: UniApp.GetUserInfoRes }) {
		await this.$STORE.userStore.updateUser(detail.userInfo)
		this.$emit('authClick', {}, {})
		const state = this.state
		if (state === 'me') {
			this.$emit('getNew', detail.userInfo)
		}

		if (state === 'select') {
			this.$emit('getNew', {})
		}
	}

	/**
	 * @LastEditors: chuyinlong
	 * @description: 新版本登录
	 */

	async getUserProfile() {
		uni.getUserProfile({
			desc: '用于完善会员资料',
			success: async (detail) => {
				await this.$STORE.userStore.updateUser(detail.userInfo)
				this.$emit('authClick', {}, {})
				const state = this.state
				if (state === 'me') {
					this.$emit('getNew', detail.userInfo, {})
				}

				if (state === 'select') {
					this.$emit('getNew', {}, {})
				}
			}
		})
	}
}
</script>
<style lang="scss" scoped>
@include b(authContent) {
	display: flex;
	justify-content: center;
	flex-wrap: wrap;

	@include e(top) {
		height: 84rpx;
		width: 100%;
		display: flex;
		justify-content: center;
		padding-top: 40rpx;
		font-size: 30rpx;
	}

	@include e(button) {
		border-radius: 100rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		margin-top: 60rpx;
		width: 80%;
		height: 90rpx;
		background-color: #fb5362;
		color: white;
		font-size: 30rpx;
	}

	@include e(noContent) {
		font-size: 30rpx;
		width: 400rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		margin-top: 40rpx;
	}
}
</style>
