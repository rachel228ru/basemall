<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-26 17:20:49
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-26 18:25:37
-->
<template>
  <div class="admin">
    <div class="admin__header">
      <slot name="logo"></slot>
      <slot name="user"></slot>
    </div>
    <div class="admin__content admin__center">
      <div class="admin__main">
        <div class="admin__main--content" :class="noPadding && 'no--padding'">
          <slot name="content"></slot>
          <slot name="footer"></slot>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { Vue, Component } from "vue-property-decorator";

@Component
export default class MLayout extends Vue {
  static componentName = "MLayout";

  get noPadding() {
    return this.$route.meta && this.$route.meta.noPadding;
  }
}
</script>

<style lang="scss" scoped>
@import "src/assets/styles/addShop/layout/layout.scss";
</style>
