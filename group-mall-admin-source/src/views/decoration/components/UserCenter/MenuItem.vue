<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:10
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-27 11:39:00
-->
<template>
  <div class="item">
    <img :src="imgUrl" class="item__img" />
    <span class="item__text">
      <slot></slot>
    </span>
    <i :class="'el-collapse-item__arrow ' + rightIcon"></i>
  </div>
</template>

<script lang="ts">
import { Vue, Component, Prop } from "vue-property-decorator";

@Component
export default class MenuItem extends Vue {
  @Prop({ default: "" })
  imgUrl!: string;

  @Prop({ default: "el-icon-arrow-right" })
  rightIcon!: string;
}
</script>

<style lang="scss">
@import "../../../../assets/styles/decoration/menuitem";
</style>
