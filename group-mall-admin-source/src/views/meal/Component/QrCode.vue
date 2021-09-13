<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:10
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-27 11:18:25
-->
<template>
  <div :id="id" ref="id"></div>
</template>

<script lang="ts">
import { Vue, Component, Prop } from "vue-property-decorator";
import QRCode from "qrcodejs2";

@Component
export default class GiveCoupons extends Vue {
  @Prop({
    type: Object,
    default() {
      return {};
    },
  })
  option!: any;

  @Prop({
    type: String,
    required: true,
    default: "",
  })
  id!: string;

  mounted() {
    this.qrcode();
  }

  qrcode() {
    new QRCode(this.id, {
      width: this.option.width || 100,
      height: this.option.height || 100, // 高度
      text: this.option.codeUrl || "", // 二维码内容
      render: "canvas", // 设置渲染方式（有两种方式 table和canvas，默认是canvas）
    });
  }
}
</script>

<style lang="scss" scoped></style>
