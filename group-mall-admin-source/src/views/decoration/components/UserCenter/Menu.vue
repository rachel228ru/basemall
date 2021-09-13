<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:10
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-24 15:51:48
-->
<template>
  <div class="menu">
    <slot></slot>
  </div>
</template>

<script lang="ts">
import { Vue, Component } from "vue-property-decorator";

@Component
export default class Menu extends Vue {}
</script>

<style lang="scss" scoped>
.menu {
  border-radius: 10px;
  margin-bottom: 10px;
}
</style>
