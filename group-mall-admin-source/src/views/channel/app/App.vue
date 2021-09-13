<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:09
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-25 13:27:08
-->
<template>
  <div v-loading="loading">
    <AppUnauth v-if="!authStatus && !loading" @refreshData="getPublicConfig" />
    <AppAuth
      :miniInfo="miniInfo"
      @refreshData="getPublicConfig"
      v-if="authStatus && !loading"
    />
  </div>
</template>

<script lang="ts">
import { Vue, Component } from "vue-property-decorator";
import AppAuth from "@/views/channel/app/AppAuth.vue";
import AppUnauth from "@/views/channel/app/AppUnauth.vue";
import {
  getMiniBaseInfo,
  getwxacode,
  miniAuthorizationError,
} from "@/api/channel/channel";
import storage from "@/libs/storage";
import merge from "webpack-merge";
import { MpSettingType } from "../channelType";

@Component({
  components: {
    AppAuth,
    AppUnauth,
  },
})
export default class App extends Vue {
  authStatus = false;

  loading = true;

  // 更新小程序信息表单
  miniInfo: MpSettingType = {
    miniName: "",
    mchId: "",
    certificatesState: false,
    logo: "",
    qrcode: "",
    signature: "",
    codeVersionVos: [],
    principalName: "",
    serviceClass: "",
    appid: "",
    runFlag: "",
    authorizerFlag: "",
    auditStatus: null,
    currentVersionNumName: "",
    currentVersionSendTime: "",
    versionUpdateNumName: "",
    versionUpdateTime: "",
    auditingVersionNumName: "",
    auditingTemplateDetailMinisId: "",
    auditingVersionSummitTime: "",
    auditingComeToNothingReason: "",
    auditingVersionEndTime: "",
    serviceTypeInfo: null,
    verifyTypeInfo: null,
    alias: "",
  };

  async created() {
    if (
      this.$route.query.auth &&
      storage.get("userInfo").shopInfoVo.platformShopId
    ) {
      try {
        await this.$router.push({
          query: merge(this.$route.query, { auth: "" }),
        });
        const platformShopId = storage.get("userInfo").shopInfoVo
          .platformShopId;
        await miniAuthorizationError({
          platformShopId,
        });
      } catch (e) {
        this.$message({
          type: "warning",
          message: e,
        });
        await this.getPublicConfig();
      }
    } else {
      await this.getPublicConfig();
    }
  }

  async getPublicConfig() {
    try {
      const response = await getMiniBaseInfo({
        type: 2,
      });
      const { code, data } = response;
      if (code === 200) {
        if (data.appid) {
          this.miniInfo = data;
          this.authStatus = true;
          this.loading = false;
          try {
            if (this.miniInfo.authorizerFlag) {
              data.qrcode = await this.getCode();
            }
          } catch (e) {
            this.$message({
              type: "error",
              message: e,
            });
          }
        }
      } else {
        this.authStatus = false;
        this.loading = false;
      }
    } catch (e) {
      this.$message({
        type: "warning",
        message: e,
      });
    }
    this.loading = false;
  }

  async getCode() {
    let result = "";
    const response = await getwxacode({
      path: "pages/index/index",
      width: 400,
    });
    const { code, data } = response;
    if (code === 200) {
      result = data;
    }
    return result;
  }
}
</script>
