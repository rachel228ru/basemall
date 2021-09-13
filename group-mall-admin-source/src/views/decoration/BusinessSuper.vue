<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:09
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-09-01 15:45:24
-->
<template>
  <div class="decoration__page">
    <!-- 菜单 -->
    <slot name="EditorControlPage"></slot>

    <!-- 预览 -->
    <Preview :formData="formData"></Preview>

    <!-- 设置 -->
    <Setting :formData="formData"></Setting>
  </div>
</template>

<script lang="ts">
import { Vue, Component } from "vue-property-decorator";
import EditorControlPage from "@/views/decoration/components/EditorPage/src/EditorControlPage.vue";
import Preview from "./components/BusinessSuper/Preview.vue";
import Setting from "./components/BusinessSuper/Setting.vue";
import BusinessSuper from "./components/BusinessSuper/BusinessSuper";

@Component({
  components: {
    EditorControlPage,
    Preview,
    Setting,
  },
})
export default class Page extends Vue {
  /** 配置数据  */
  formData: BusinessSuper = new BusinessSuper();
}
</script>

<style lang="scss" scoped>
@import "../../assets/styles/decoration/userCenter";
@import "@/assets/styles/decoration/editorPage";
@import "@/assets/styles/decoration/businessSuper";
</style>
