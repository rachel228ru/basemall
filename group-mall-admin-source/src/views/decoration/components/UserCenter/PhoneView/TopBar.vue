<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-27 13:16:40
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-27 13:16:50
-->
<template>
  <div class="topBar" :style="{ backgroundColor: bgcolor, color: color }">
    <div class="topBar__one">
      <span>
        <i class="iconfont iconxinhao" />
        <i class="iconfont iconxinhao3-copy-copy-copy" />
        <i class="iconfont icondianliang" />
      </span>
      <span>{{ now }}</span>
    </div>
  </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from "vue-property-decorator";

@Component
export default class PickCode extends Vue {
  @Prop({
    type: String,
    default: "#ffffff",
  })
  color!: string;

  @Prop({
    type: String,
    default: "transparent",
  })
  bgcolor!: string;

  get now() {
    const date = new Date();
    const hour = date.getHours() > 9 ? date.getHours() : "0" + date.getHours();
    const minutes =
      date.getMinutes() > 9 ? date.getMinutes() : "0" + date.getMinutes();
    return `${hour}:${minutes}`;
  }
}
</script>

<style lang="scss" scoped>
/* 手机状态栏 */
@include b(topBar) {
  width: 100%;
  padding: 5px 10px 5px;

  @include e(one) {
    display: flex;
    justify-content: flex-end;
    align-items: center;
    margin-bottom: 10px;

    span,
    i.iconfont {
      padding: 0 2px;
      font-size: 14px;
    }
  }
}
</style>
