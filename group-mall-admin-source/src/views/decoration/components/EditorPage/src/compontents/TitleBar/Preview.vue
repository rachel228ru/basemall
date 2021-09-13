<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:10
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-23 18:09:02
-->
<template>
  <!-- 标题栏 -->
  <div class="titleBar" :style="inlineStyle">
    <div
      v-if="formData.showStyle === 'is-style-one'"
      class="titleBar__item"
      :class="formData.showStyle"
      :style="{ color: formData.color, fontWeight: 600 }"
    >
      {{ formData.titleName }}
    </div>
    <div
      v-if="formData.showStyle === 'is-style-two'"
      class="titleBar__item"
      :class="formData.showStyle"
      :style="{ color: formData.color }"
    >
      <div style="font-weight: 600;">{{ formData.titleName }}</div>
      <div>查看更多></div>
    </div>
  </div>
</template>

<script lang="ts">
import { Vue, Component, Prop } from "vue-property-decorator";
import TitleBar from "./TitleBar";

@Component
export default class TitleBarPreview extends Vue {
  @Prop()
  formData!: TitleBar;

  get inlineStyle() {
    const { backgroundColor } = this.formData;
    return {
      "background-color": backgroundColor,
    };
  }
}
</script>
