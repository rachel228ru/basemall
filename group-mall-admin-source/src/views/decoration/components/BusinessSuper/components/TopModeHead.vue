<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:09
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-23 20:28:26
-->
<template>
  <div class="top__mode-page">
    <span
      v-for="(item, idx) in getHeadList"
      :key="idx"
      :class="['tab__item']"
      :style="item.id === activeId ? styleType.style1 : styleType.style2"
      @click="changeActiveItem(item)"
    >
      {{ item.name }}
    </span>
  </div>
</template>
<script lang="ts">
import { Vue, Prop, Component } from "vue-property-decorator";
import BusinessSuper, { bus } from "./../BusinessSuper";

@Component
export default class TopModeHead extends Vue {
  @Prop()
  formData!: BusinessSuper;

  activeId = "-1";

  get getHeadList() {
    this.activeId = "-1";
    const list = this.formData.firstCatList.map(i => {
      if (i.isSelected) this.activeId = i.category.id;
      return {
        id: i.category.id,
        name: i.category.name,
      };
    });
    return [
      {
        id: "-1",
        name: "全部",
      },
      ...list,
    ];
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

  /**
   * @LastEditors: chuyinlong
   * @description: 修改当前激活tab
   * @param {id: string;name: string;} item
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
    if (selectMode !== 1) return;
    const item = this.formData.firstCatList.find(i => i.isSelected);
    if (item) return;
    this.activeId = "-1";
  }

  mounted() {
    if (
      this.formData.firstCatList.length &&
      this.formData.firstCatList.find(i => i.isSelected)
    ) {
      this.formData.firstCatList[0].isSelected = true;
      this.activeId = this.formData.firstCatList[0].category.id;
    } else {
      this.activeId = "-1";
    }
    bus.$on("selectedFirstCategory", () => {
      this.initCurruntFirstCategory();
    });
  }
}
</script>
<style lang="scss" scoped>
.top__mode-page {
  width: 100%;
  height: 50px;
  overflow-x: scroll;
  white-space: nowrap;
  box-sizing: border-box;
  padding: 0px 5px;

  .tab__item {
    display: inline-block;
    padding: 6px 16px;
    border-radius: 40px;
    font-size: 15px;
    color: #333;
    background-color: #fff;
    cursor: pointer;
    user-select: none;
    margin-top: 10px;
  }

  .tab__active {
    background-color: rgba(253, 78, 100, 1);
    color: #fff;
  }
}
</style>
