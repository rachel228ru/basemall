<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:10
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-23 18:03:36
-->
<template>
  <!-- 分隔符 -->
  <div :style="inlineBox">
    <div :style="inlineStyle"></div>
  </div>
</template>

<script lang="ts">
import { Vue, Component, Prop } from "vue-property-decorator";
import Separator from "./Separator";

@Component
export default class SeparatorPreview extends Vue {
  @Prop()
  formData!: Separator;

  get inlineBox() {
    const { hasMargin } = this.formData;
    return `box-sizing: border-box; padding: 10px ${hasMargin ? 15 : 0}px`;
  }

  get inlineStyle() {
    const { borderColor, borderStyle } = this.formData;
    return `border-bottom:1px ${borderStyle} ${borderColor};`;
  }
}
</script>
