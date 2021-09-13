<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:10
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-23 18:09:58
-->
<template>
  <div
    class="videoCom"
    :style="{ padding: upLoadiVideo.radioTC === 2 ? '10px' : '' }"
  >
    <img
      src="https://qiniu-app.qtshe.com/u391.png"
      style="width:100%;height:200px"
    />
    <i
      class="iconfont iconzhibo1 videoIcon"
      :style="{
        bottom: upLoadiVideo.radioTC === 2 ? '25px' : '10px',
        left: upLoadiVideo.radioTC === 2 ? '20px' : '5px',
      }"
    ></i>
  </div>
</template>

<script lang="ts">
import { Vue, Component, PropSync } from "vue-property-decorator";
import VideoCom from "./VideoCom";

@Component({
  components: {},
})
export default class VideoComPreview extends Vue {
  @PropSync("formData", {
    type: Object,
    default() {
      return null;
    },
  })
  upLoadiVideo!: VideoCom;

  showType = false;
}
</script>

<style lang="scss" scoped>
.videoCom {
  position: relative;
}

.videoIcon {
  position: absolute;
  font-size: 26px;
  color: #a1a1a1;
}
</style>
