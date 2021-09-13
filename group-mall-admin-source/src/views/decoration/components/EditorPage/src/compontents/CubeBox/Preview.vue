<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:10
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-23 09:53:28
-->
<template>
  <div class="rc-design-react-preview rc-design-component-default-preview">
    <div class="rc-design-component-default-preview__text" v-if="!showCubeList">
      点击编辑魔方
    </div>
    <div class="cap-cube-wrap" v-else>
      <div
        class="cap-cube"
        :style="{
          width: pageMarginStyle.width + 'px',
          height: pageMarginStyle.height + 'px',
          margin: pageMarginStyle.margin + 'px',
        }"
      >
        <div
          class="cap-cube__item"
          v-for="(item, index) in showCubeListWrap"
          :key="index"
          :style="{
            width: item.width,
            height: item.height,
            top: item.top,
            left: item.left,
            backgroundImage: `url(${item.img})`,
            margin: item.borderWidth,
          }"
        >
          <img
            class="cap-cube__table-image cap-cube__table-image--invisible"
            :src="item.img"
            v-if="item.img"
          />
        </div>
      </div>
    </div>
  </div>
</template>
<script lang="ts">
import { Vue, Component, Prop, Watch } from "vue-property-decorator";
import { ICubeListWrap, CubeBoxFormData } from "./CubeBox";
@Component
export default class CubeBoxPreview extends Vue {
  @Prop()
  formData!: CubeBoxFormData;

  pageMarginStyle = {
    width: 0,
    height: 0,
    margin: 0,
  };

  showCubeListWrap: ICubeListWrap[] = [];

  showCubeList = false;

  mounted() {
    if (!!this.formData) {
      this.showCubeList = this.showTxt();
      if (this.showCubeList) {
        this.drawCube();
      }
    }
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 是否已正在编辑
   * @returns {boole}
   */
  showTxt(): boolean {
    return !!this.formData.subEntry.filter(item => !!item.img).length;
  }

  @Watch("formData", { deep: true })
  drawCube() {
    if (!!this.formData) {
      this.showCubeList = this.showTxt();
      const perviewLayoutWidth = 370;
      const item = this.formData;
      const layoutWidth = item.layoutWidth;
      const layoutHeight = item.layoutHeight;
      // perviewLayoutWidth = perviewLayoutWidth - item.pageMargin * 2;
      const wrapWith =
        perviewLayoutWidth + item.borderWidth - item.pageMargin * 2;
      const styleWidth = wrapWith / layoutWidth;
      const styleHeight =
        layoutHeight !== 1 ? perviewLayoutWidth / layoutHeight : styleWidth;
      this.drawCubeWrap(styleWidth, styleHeight, wrapWith);
    }
  }

  /**
   * 根据每个对应数据绘画出各个位置
   */
  drawCubeWrap(divWidth: number, divHeight: number, wrapWith: number) {
    const item = this.formData;
    const subEntry = item.subEntry;
    this.showCubeListWrap = [];
    let maxY = 0,
      maxIndex = 0,
      maxHeght = 0;
    if (subEntry.length) {
      for (let i = 0; i < subEntry.length; i++) {
        const a = subEntry[i];
        const coverDiv = {
          top: a.y * divHeight + "px",
          left: a.x * divWidth + item.pageMargin + "px",
          width: divWidth * a.width - item.borderWidth + "px",
          height: divHeight * a.height - item.borderWidth + "px",
          paddingTop: (divHeight * a.height) / 2 + "px",
          img: a[`img`] ? a[`img`] : "",
          borderWidth: item.borderWidth / 2 + "px",
        };
        if (maxY <= a.y) {
          maxY = a.y;
          maxIndex = i;
        }
        this.showCubeListWrap.push(coverDiv);
      }

      maxHeght =
        maxY + subEntry[maxIndex].height < item.layoutHeight
          ? maxY + subEntry[maxIndex].height
          : item.layoutHeight;
      this.pageMarginStyle = {
        width: wrapWith,
        height: divHeight * maxHeght,
        margin: -item.borderWidth / 2,
      };
    }
  }
}
</script>
<style lang="scss" scope>
@import "@/assets/styles/decoration/cubeBox.scss";
</style>
