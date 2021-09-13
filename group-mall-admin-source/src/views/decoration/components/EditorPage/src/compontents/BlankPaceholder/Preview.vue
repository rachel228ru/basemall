<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:10
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-23 09:39:33
-->
<template>
  <!-- 空白占位 -->
  <div :style="inlineStyle"></div>
</template>

<script lang="ts">
import { Vue, Component, PropSync } from "vue-property-decorator";
import BlankPaceholder from "./BlankPaceholder";

@Component
export default class BlankPaceholderPreview extends Vue {
  @PropSync("formData", {
    type: Object,
    default() {
      return null;
    },
  })
  BlankPaceholderFormData!: BlankPaceholder;

  get inlineStyle() {
    return `height:${this.BlankPaceholderFormData.height}px`;
  }
}
</script>
