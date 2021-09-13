<!--
 * @description: 
 * @Author: vikingShip
 * @Date: 2021-07-17 13:29:51
 * @LastEditors: vikingShip
 * @LastEditTime: 2021-08-14 11:28:07
-->
<template>
  <div id="app">
    <vue-progress-bar />
    <router-view />
  </div>
</template>

<script lang="ts">
import { Vue, Component } from "vue-property-decorator";
@Component
export default class App extends Vue {
  /** 播报计时器 */
  timer = null;

  mounted() {
    this.$STORE.globalStore.initSocket()
  }

  clearTimer() {
    clearInterval(this.timer);
    this.timer = null;
  }

  /**
   * 轮询
   */
  
  training() {
    return new Promise(resolve => {
      try {
        const { shopInfoVo } = JSON.parse(
          window.localStorage.getItem("userInfo"),
        );
        this.timer = setInterval(() => {
          clearInterval(this.timer);
          resolve(shopInfoVo.shopId);
        }, 1000);
      } catch (err) {
        return null;
      }
    });
  }
}
</script>

