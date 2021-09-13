/*
 * @description: 
 * @Author: vikingShip
 * @Date: 2021-08-12 17:34:39
 * @LastEditors: vikingShip
 * @LastEditTime: 2021-08-14 10:31:32
 */
import Vue from "vue";
import store from "./store";
import App from "./App.vue";
import router from "./routes";
import { installPlugin } from "@/libs/plugin";
import "./assets/styles/common.scss";
import { DecorationStore, decorationStore } from "@/store/modules/decoration"
import { EditorStore, editorStore } from '@/store/modules/editor'
import { GlobalStore, globalStore } from '@/store/modules/global'
import { MenuStore, menuStore } from '@/store/modules/menu'
import { OrderStore, orderStore } from '@/store/modules/order'
import { UserStore, userStore } from '@/store/modules/user'
export interface RootState {
  decorationStore: DecorationStore
  editorStore: EditorStore
  globalStore: GlobalStore
  menuStore: MenuStore
  orderStore: OrderStore
  userStore: UserStore
}
Vue.config.productionTip = false;
Vue.prototype.$STORE = {
  decorationStore,
  editorStore,
  globalStore,
  menuStore,
  orderStore,
  userStore
}

installPlugin(Vue);

window.history.scrollRestoration = "manual";

const vm = new Vue({
  render: h => h(App),
  router,
  store
}).$mount("#app");


export default vm;
