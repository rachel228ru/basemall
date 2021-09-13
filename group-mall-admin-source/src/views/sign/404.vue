<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:11
 * @LastEditors: vikingShip
 * @LastEditTime: 2021-08-26 14:31:57
-->
<template>
  <div class="__nuxt-error-page">
    <div class="error">
      <svg
        xmlns="http://www.w3.org/2000/svg"
        width="90"
        height="90"
        fill="#DBE1EC"
        viewBox="0 0 48 48"
      >
        <path
          d="M22 30h4v4h-4zm0-16h4v12h-4zm1.99-10C12.94 4 4 12.95 4 24s8.94 20 19.99 20S44 35.05 44 24 35.04 4 23.99 4zM24 40c-8.84 0-16-7.16-16-16S15.16 8 24 8s16 7.16 16 16-7.16 16-16 16z"
        />
      </svg>
      <div class="title">404</div>
      <p class="description">
        <router-link class="error-link" :to="publicPath"
          >Back to the home page</router-link
        >
      </p>
      <img
        @click="linkTo"
        src="https://medusa-small-file.oss-cn-hangzhou.aliyuncs.com/gruul/20200407/logo.png"
        class="SignFlowHomepage__logo"
      />
    </div>
  </div>
</template>

<script lang="ts">
import { Vue, Component } from "vue-property-decorator";
import { getSystemConfig } from "@/api/sign";

const publicPath = process.env.VUE_APP_PUBLICPATH;

@Component
export default class ChangePassword extends Vue {
  publicPath = publicPath;

  async linkTo() {
    try {
      const response = await getSystemConfig();
      const { code, data } = response.data;
      if (code === 200) {
        if (data.systemConfig && data.systemConfig.consoleLog) {
          open(`${data.systemConfig.consoleLog}`, "_top");
        }
      }
    } catch (e) {
      console.log(e);
    }
  }
}
</script>
