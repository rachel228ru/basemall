<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:06
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-09-01 15:42:11
-->
<template>
  <div :id="id"></div>
</template>

<script lang="ts">
import { Vue, Component, Prop } from "vue-property-decorator";
import { createQrCode } from "@/libs";
import { getScanCodeUrl } from "@/api/sign";

@Component
export default class ScanQrcode extends Vue {
  @Prop({ default: "qrcode" }) id!: string;

  @Prop({ default: "400px" }) height!: string;

  @Prop({
    default: "redirect",
    required: true,
  })
  redirectUrl!: string;

  @Prop({
    default: "AccountSwitching",
    required: true,
  })
  scenes!: string;

  @Prop({
    default: null,
  })
  shopInfoId!: number;

  url = "";

  async created() {
    await this.getCompleteUrl();
    createQrCode({
      id: this.id,
      url: this.url,
      height: this.height,
    });
  }

  async getCompleteUrl() {
    try {
      const redirectUrl = process.env.VUE_APP_DOMAIN + this.redirectUrl;
      // const redirectUrl = "http://localhost:8081/copartner" + this.redirectUrl;

      const response = await getScanCodeUrl({
        shopInfoId: this.shopInfoId,
        appId: process.env.VUE_APP_APPID,
        redirectUrl,
        scenes: this.scenes,
      });
      const { data, code } = response;
      if (code === 200) {
        this.url = data;
      } else {
        this.$message({
          type: "warning",
          message: "获取跳转链接失败！",
        });
      }
    } catch (e) {
      this.$message({
        type: "warning",
        message: e,
      });
    }
  }
}
</script>

<style></style>
