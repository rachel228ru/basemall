<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:06
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-25 13:12:52
-->
<template>
  <div class="admin">
    <div class="admin__header">
      <div class="admin__center admin__center--flex">
        <slot name="logo"></slot>
        <slot name="user"></slot>
      </div>
    </div>
    <div class="admin__center admin__main--breadcrumb-wrap">
      <div class="admin__main--breadcrumb">
        <slot name="breadcrumb"></slot>
      </div>
    </div>
    <div class="admin__center_wrap">
      <el-scrollbar :noresize="false" viewStyle="width:100vw" class="h100">
        <div class="admin__content admin__center">
          <div class="admin__aside">
            <slot name="aside"></slot>
          </div>
          <el-main class="admin__main">
            <div
              class="admin__main--content"
              :class="noPadding && 'no--padding'"
              ref="admin_content"
              :style="{
                'min-height': minHeight + `px`,
                padding: !changeBC ? '20px 15px' : '0px',
              }"
            >
              <!-- , 'height': !changeBC ? '' : '900px' -->
              <slot name="view"></slot>
            </div>
          </el-main>
        </div>
      </el-scrollbar>
    </div>
    <slot name="notify"></slot>
  </div>
</template>

<script lang="ts">
import { Vue, Component, Watch } from "vue-property-decorator";
import { dropdownHack } from "@/libs/index";

@Component
export default class MLayout extends Vue {
  static componentName = "MLayout";

  get noPadding() {
    return this.$route.meta && this.$route.meta.noPadding;
  }

  minHeight = 780;

  changeBC = false;

  mounted() {
    this.$nextTick(() => {
      dropdownHack();
    });
    this.minHeight = document.documentElement.clientHeight - 84;
    this.changeBC = this.$route.name === "OverView" ? true : false;
  }

  @Watch("$route")
  onChangeValue() {
    if (document.querySelector(".el-scrollbar__wrap")) {
      document.querySelector(".el-scrollbar__wrap").scrollTop = 0;
    }
    this.changeBC = this.$route.name === "OverView" ? true : false;
  }
}
</script>

<style lang="scss">
@import "../styles/m-layout.scss";
.side-nav-wrap-main {
  ::-webkit-scrollbar {
    display: none;
  }
}
</style>
