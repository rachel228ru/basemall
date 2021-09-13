<!--
 * @description: 
 * @Author: vikingShip
 * @Date: 2021-08-06 08:44:09
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-30 14:12:18
-->
<script lang="ts">
import Vue from 'vue'
export default Vue.extend({
	mpType: 'app',
	globalData: {
		isTrans: false
	},
	async onLaunch() {
		this.$storage.clear()
		if (
			this.$global.compareVersion(
				uni.getSystemInfoSync().SDKVersion,
				process.env.VUE_APP_BASE_LIB_VERSION || ''
			) < 0
		) {
			this.$Popup.alert({
				title: '温馨提示',
				content: '当前基础库版本较低，为了更好的体验，请先升级微信'
			})
		}
		this.$STORE.userStore.getTenId()
		await this.$STORE.userStore.login()
		await this.$STORE.shopSetStore.getshopset()
		await this.$STORE.setStore.getDecorationAllList()
		await this.$STORE.setStore.getGuideList()
		await this.$STORE.setStore.getGuideVisible()
		const res = await this.$STORE.setStore.getSetting()
		this.$STORE.messageStore.getTemplates(res)
		this.$STORE.setStore.getAllUserCenterData()
		this.$STORE.setStore.setIsReady(true)
		this.$Pubsub.emit('app-launch')
	},

	onShow() {
		console.log('App onShow')
	},

	onHide() {
		console.log('App Hide')
	}
})
</script>

<style lang="scss">
/*每个页面公共css */
@import '@/assets/styles/font/iconfont.scss';
@import '@/assets/styles/common.scss';
</style>
