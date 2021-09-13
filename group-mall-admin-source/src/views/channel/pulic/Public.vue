<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:09
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-25 13:27:38
-->
<template>
  <div v-loading="loading">
    <PublicUnauth v-if="!authStatus && !loading" />
    <PublicAuth :config="mpConfig" v-if="authStatus && !loading" />
  </div>
</template>

<script lang="ts">
import { Vue, Component } from "vue-property-decorator";
import PublicUnauth from "@/views/channel/pulic/PublicUnauth.vue";
import PublicAuth from "@/views/channel/pulic/PublicAuth.vue";
import { MpSettingType } from "../channelType";
import { getMiniBaseInfo, miniAuthorizationError } from "@/api/channel/channel";
import storage from "@/libs/storage";
import merge from "webpack-merge";

@Component({
  components: {
    PublicUnauth,
    PublicAuth,
  },
})
export default class Public extends Vue {
  authStatus = false;

  loading = true;

  mpConfig: MpSettingType = {
    miniName: "",
    logo: "",
    qrcode: "",
    signature: "",
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
    auditingVersionSummitTime: "",
    auditingComeToNothingReason: "",
    auditingVersionEndTime: "",
    serviceTypeInfo: null,
    verifyTypeInfo: null,
    alias: "",
    authInfo: [],
  };

  async created() {
    if (
      this.$route.query.auth &&
      storage.get("userInfo").shopInfoVo.platformShopId
    ) {
      try {
        this.$router.push({
          query: merge(this.$route.query, { auth: "" }),
        });
        const platformShopId = storage.get("userInfo").shopInfoVo
          .platformShopId;
        const response = await miniAuthorizationError({
          platformShopId,
        });
        const { code, msg } = response;
        if (code !== 200) {
          this.$message({
            type: "warning",
            message: msg as string,
          });
        }
      } catch (e) {
        this.$message({
          type: "warning",
          message: e,
        });
      }
    } else {
      await this.getPublicConfig();
    }
  }

  async getPublicConfig() {
    try {
      const response = await getMiniBaseInfo({
        type: 1,
      });
      const { code, data } = response;
      if (code === 200) {
        if (data.appid) {
          this.authStatus = true;
          this.loading = false;
        }
        this.mpConfig = data;
      } else {
        this.authStatus = false;
        this.loading = false;
      }
    } catch (e) {
      this.$message({
        type: "error",
        message: e,
      });
    }
    this.loading = false;
  }
}
</script>
