<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:10
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-23 16:10:29
-->
<template>
  <!-- 富文本组件 -->
  <div class="rc-design-react-preview rc-design-component-default-preview">
    <div
      class="rc-design-component-default-preview__text"
      v-if="!formData.text"
    >
      编辑文本
    </div>
    <div class="cap-cube-wrap" v-else v-html="formData.text"></div>
  </div>
</template>
<script lang="ts">
import { Vue, Component, Prop } from "vue-property-decorator";
import RichText from "./RichText";
@Component
export default class CubeBoxPreview extends Vue {
  @Prop()
  formData!: RichText;
}
</script>
<style lang="scss" scoped>
@import "@/assets/styles/decoration/cubeBox.scss";
.cap-cube-wrap {
  width: 100%;
  overflow: hidden;
  word-wrap: break-word;
}
</style>
