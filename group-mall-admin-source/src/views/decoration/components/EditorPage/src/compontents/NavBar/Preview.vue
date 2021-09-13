<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:10
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-23 16:06:44
-->
<template>
  <div class="bottom_bar">
    <div
      v-for="(item, index) in formData.menuList"
      :key="index"
      :class="
        formData.codeStyle == 2 &&
        (formData.menuList.length == 1 ||
          (formData.menuList.length == 3 && index == 1) ||
          (formData.menuList.length == 5 && index == 2))
          ? 'bottom_bar_item_big'
          : 'bottom_bar_item'
      "
    >
      <img
        class="bottom_bar_item_icon"
        v-if="item.iconType === 1"
        :src="item.defIcon"
      />
      <img class="bottom_bar_item_icon" v-else :src="item.iconPath" />
      <span class="bottom_bar_item_text">{{ item.text }}</span>
    </div>
  </div>
</template>

<script lang="ts">
import { Vue, Component, Prop } from "vue-property-decorator";
import NavBar from "./NavBar";
@Component
export default class NavBarPreview extends Vue {
  @Prop()
  formData!: NavBar;
}
</script>
<style lang="scss" scoped>
@import "@/assets/styles/decoration/navBar";
</style>
