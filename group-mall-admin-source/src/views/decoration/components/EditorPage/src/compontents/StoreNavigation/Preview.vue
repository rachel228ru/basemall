<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:10
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-23 18:04:16
-->
<template>
  <!-- 店铺导航 -->
  <div>
    <ul class="storeNavigation-item">
      <li
        v-for="(storeNavigationItem, index) in formData.storeNavigations"
        :key="index"
        :style="{
          width:
            formData.storeNavigations.length % 5 === 0 ||
            formData.storeNavigations.length > 8
              ? '20%'
              : '25%',
        }"
      >
        <img
          class="storeNavigation-item__img"
          :src="storeNavigationItem.navIcon"
        />
        <div :style="{ color: storeNavigationItem.fontColor }">
          {{ storeNavigationItem.navName }}
        </div>
      </li>
    </ul>
  </div>
</template>

<script lang="ts">
import { Vue, Component, Prop } from "vue-property-decorator";
import StoreNavigation from "./StoreNavigation";

@Component
export default class StoreNavigationPreview extends Vue {
  @Prop()
  formData!: StoreNavigation;
}
</script>
<style lang="scss" scoped>
@import "@/assets/styles/decoration/storeNavigation";
</style>
