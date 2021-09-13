<!--
 * @description: 
 * @Author: vikingShip
 * @Date: 2021-07-17 13:29:52
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-24 15:13:45
-->
<template>
  <!-- 组件、控件、页面菜单页面 -->
  <div class="editor_tab">
    <div class="editor_tab_title"></div>
    <div class="left_menu">
      <span class="slider—span" :style="{ top: `${sliderTop}px` }"></span>
      <div
        :class="['tab_item', nowTab === 0 ? 'tab_item_active' : '']"
        @click="selectTab(0)"
      >
        <span class="tab-bg__icon tab-bg__icon1"></span>
        <span class="tab_item_title">组件</span>
      </div>
      <div
        :class="['tab_item', nowTab === 1 ? 'tab_item_active' : '']"
        @click="selectTab(1)"
      >
        <span class="tab-bg__icon tab-bg__icon2"></span>
        <span class="tab_item_title">控件</span>
      </div>
      <div
        :class="['tab_item', nowTab === 2 ? 'tab_item_active' : '']"
        @click="selectTab(2)"
      >
        <span class="tab-bg__icon tab-bg__icon3"></span>
        <span class="tab_item_title">页面</span>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { IComponent } from "./interfaces/component";
import { Vue, Component, Prop, Emit } from "vue-property-decorator";
import setting from "./compontents/index/setting";
@Component({
  components: {
    ...setting,
  },
})
export default class EditorForm extends Vue {
  /** 滑块距离顶部的值 */
  sliderTop = 54;

  /** 是否展开 */
  isCollapse = false;

  /** 当前选中tab */
  nowTab = 0;

  @Prop({
    default() {
      return null;
    },
  })
  currentComponent!: IComponent;

  /**
   * @LastEditors: chuyinlong
   * @description: 切换左侧tab
   * @param {number} num
   */

  @Emit("change")
  selectTab(num: number) {
    this.sliderTop = 50 + num * 96;
    this.nowTab = num;
    this.$STORE.decorationStore.setIsuercenter(num);
    return this.nowTab;
  }
}
</script>
<style lang="scss">
@import "@/assets/styles/decoration/editorPage";
</style>
