<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:09
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-23 20:25:27
-->
<template>
  <div class="left__mode-page" :style="styleType.style2">
    <div
      :class="['tab__item']"
      :style="item.id === activeId ? styleType.style1 : styleType.style2"
      v-for="item in getHeadList"
      :key="item.id"
      @click="changeActiveItem(item)"
    >
      <span
        :style="
          item.id === activeId
            ? { backgroundColor: '#FC425A' }
            : styleType.style2
        "
      ></span>
      {{ item.name }}
    </div>
  </div>
</template>
<script lang="ts">
import { Vue, Prop, Component, Watch } from "vue-property-decorator";
import BusinessSuper, { bus, FirstCategory } from "./../BusinessSuper";

@Component
export default class LeftTabbar extends Vue {
  @Prop()
  formData!: BusinessSuper;

  activeId = "-1";

  get getHeadList() {
    this.activeId = "-1";
    return this.formData.firstCatList.map(i => {
      if (i.isSelected) this.activeId = i.category.id;
      return {
        id: i.category.id,
        name: (i.category.name || "").substr(0, 4),
      };
    });
  }

  get styleType() {
    const { fontColor, fontBg, fontNotColor, fontNotBg } = this.formData;
    const style1 = {
      color: fontColor,
      backgroundColor: fontBg,
    };
    const style2 = {
      color: fontNotColor,
      backgroundColor: fontNotBg,
    };
    return {
      style1,
      style2,
    };
  }

  get getCurrentFirstCategory() {
    const { currentFirstCategory } = this.formData;
    return currentFirstCategory;
  }

  @Watch("getCurrentFirstCategory", { deep: true })
  currentFirstCategoryChange(v: FirstCategory) {
    if (v && v.category && v.category.id) {
      this.$nextTick(() => {
        this.activeId = v.category.id;
      });
    }
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 修改当前激活tab
   * @param {{ id: string; name: string; }}item
   */

  changeActiveItem(item: { id: string; name: string }) {
    this.formData.firstCatList.forEach(i => {
      i.isSelected = i.category.id === item.id;
    });
    this.activeId = item.id;
    bus.$emit("firstCategoryId", item.id);
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 选择完成一级分类需要初始化当前编辑的一级分类
   */

  initCurruntFirstCategory() {
    const { selectMode } = this.formData;
    if (selectMode !== 2) return;
    const item = this.formData.firstCatList.find(i => i.isSelected);
    if (item || !this.formData.firstCatList.length) return;
    this.formData.firstCatList[0].isSelected = true;
    bus.$emit("firstCategoryId", this.formData.firstCatList[0].category.id);
  }

  mounted() {
    if (
      this.formData.firstCatList.length &&
      !this.formData.firstCatList.find(i => i.isSelected)
    ) {
      this.formData.firstCatList[0].isSelected = true;
      this.activeId = this.formData.firstCatList[0].category.id;
      bus.$emit("firstCategoryId", this.activeId);
    }
    bus.$on("selectedFirstCategory", () => {
      this.initCurruntFirstCategory();
    });
  }
}
</script>
<style lang="scss" scoped>
.left__mode-page {
  width: 100%;
  height: 100%;
  box-sizing: border-box;
  background-color: rgba(242, 242, 242, 1);

  .tab__item {
    display: block;
    height: 40px;
    line-height: 40px;
    font-size: 14px;
    color: #333;
    background-color: rgba(242, 242, 242, 1);
    cursor: pointer;
    user-select: none;
    box-sizing: border-box;
    padding-left: 12px;
    position: relative;
    overflow: hidden;
    box-sizing: border-box;
    padding-right: 6px;

    span {
      width: 4px;
      height: 15px;
      background-color: rgba(242, 242, 242, 1);
      top: 12px;
      position: absolute;
      left: 0;
    }
  }

  .tab__active {
    background-color: #fff;

    span {
      background-color: rgba(252, 66, 90, 1);
    }
  }
}
</style>
