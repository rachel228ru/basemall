/*
 * @description:
 * @Author: vikingShip
 * @Date: 2021-07-28 09:25:02
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-20 14:02:47
 */
import Vue from 'vue'
import App from './App.vue'
import store from '@/store'
import { global, Popup, setData } from '@/utils/helper'
import Pubsub from '@/utils/pubsub'
import storage, { Storage } from '@/utils/storage'
import { setStore, SettingStore } from '@/store/modules/setting'
import { shopSetStore, ShopSet } from '@/store/modules/shopset'
import { userStore, UserStore } from '@/store/modules/user'
import { messageStore, MessageStore } from '@/store/modules/message'

export interface RootState {
	messageStore: MessageStore
	setStore: SettingStore
	userStore: UserStore
	shopSetStore: ShopSet
}

Vue.config.productionTip = false
Vue.prototype.$global = new global()
Vue.prototype.$Pubsub = new Pubsub()
Vue.prototype.$Popup = new Popup()
Vue.prototype.$storage = storage
Vue.prototype.setData = setData
Vue.prototype.$STORE = {
	setStore,
	shopSetStore,
	userStore,
	messageStore
}

new App({
	store
}).$mount()

declare module 'vue/types/vue' {
	interface Vue {
		$global: global
		$Pubsub: Pubsub
		$Popup: Popup
		$storage: Storage
		setData: (obj: { [x: string]: any }, callback?: () => void) => void
		$STORE: RootState
	}
}
