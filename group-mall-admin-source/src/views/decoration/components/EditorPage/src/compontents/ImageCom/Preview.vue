<!--
 * @description: 
 * @Author: chuyinlong
 * @Date: 2021-04-30 14:05:47
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-09-01 15:39:19
 * 123
-->
<template>
  <div
    style="margin-top: 5px"
    :style="{ height: `${upLoadImg.boxHeight}px`, width: '100%' }"
  >
    <VueDragResize
      :w="vw === 0 && upLoadImg.img ? 200 : vw"
      :h="vh === 0 && upLoadImg.img ? 120 : vh"
      :parentLimitation="true"
      v-on:resizing="resize"
      v-on:dragging="resize"
    >
      <div class="box" :style="{ width: +vw + 'px', height: +vh + 'px' }">
        <img
          v-if="showType"
          :src="upLoadImg.img"
          style="width: 200px; height: 120px"
          :style="{
            width: vw === 0 ? '200px' : +vw + 'px',
            height: vh === 0 ? '120px' : +vh + 'px',
            marginTop: moveType ? '' : intTop + 'px',
            marginLeft: moveType ? '' : intLeft + 'px',
          }"
        />
      </div>
    </VueDragResize>
  </div>
</template>

<script lang="ts">
import { Vue, Component, PropSync, Watch } from "vue-property-decorator";
import ImageCom from "./ImageCom";
import VueDragResize from "vue-drag-resize";

@Component({
  components: {
    VueDragResize,
  },
})
export default class ImageComPreview extends Vue {
  @PropSync("formData", {
    type: Object,
    default() {
      return null;
    },
  })
  upLoadImg!: ImageCom;

  created() {
    if (this.formData.img) {
      this.upLoadImg = this.formData;
      this.showType = true;
      this.intTop = Number(this.upLoadImg.top);
      this.intLeft = Number(this.upLoadImg.left);
      this.vw = Number((this.upLoadImg.width.match(/(\S*)px/) || [0, 0])[1]);
      this.vh = Number((this.upLoadImg.height.match(/(\S*)px/) || [0, 0])[1]);
    }
  }

  @Watch("upLoadImg.img")
  getUploadImg() {
    this.showType = true;
  }

  showType = false;

  /** 初始化图片位置 */
  intTop = 0;

  intLeft = 0;

  moveType = false;

  /** 移动后图片位置 */
  vw = 0;

  vh = 0;

  top = 0;

  left = 0;

  resize(newRect: {
    width: number;
    height: number;
    top: number;
    left: number;
  }) {
    this.moveType = true;
    this.intTop = 0;
    this.intLeft = 0;
    this.vw = newRect.width;
    this.vh = newRect.height;
    this.top = newRect.top;
    this.left = newRect.left;
    this.upLoadImg.top = newRect.top;
    this.upLoadImg.left = newRect.left;
    this.upLoadImg.width = newRect.width + "px";
    this.upLoadImg.height = newRect.height + "px";
  }
}
</script>

<style lang="scss" scoped></style>
