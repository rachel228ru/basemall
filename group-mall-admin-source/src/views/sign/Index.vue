<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:11
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-27 09:45:17
-->
<template>
  <div class="SignFlowHomepage">
    <img
      @click="linkTo"
      src="https://medusa-small-file.oss-cn-hangzhou.aliyuncs.com/gruul/20200407/logo.png"
      class="SignFlowHomepage__logo pointer"
    />
    <div class="SignFlowHomepage__content">
      <div class="Card SignContainer">
        <img
          class="SignContainer__img"
          src="https://medusa-small-file.oss-cn-hangzhou.aliyuncs.com/gruul/20200318/index.jpg"
        />
        <div class="SignContainer__content">
          <SignInByPhone
            v-if="currenType === 'SignInByPhone'"
            @change="changeLoginType"
          />
          <SignInByQrCode
            v-if="currenType === 'SignInByQrCode'"
            @change="changeLoginType"
          />
          <SignUpByPhone
            v-if="currenType === 'SignUpByPhone'"
            @change="changeLoginType"
          />
          <SignUpByQrCode
            v-if="currenType === 'SignUpByQrCode'"
            @change="changeLoginType"
          />
        </div>
      </div>
    </div>

    <footer class="SignFlowHomepage__footer">
      <p class="medusa__rights">
        Copyright © 2020 启山智软. All Rights Reserved
        宁波启山科技有限公司版权所有
      </p>
      <p>
        <img
          style="width: 14px;"
          src="https://medusa-small-file.oss-cn-hangzhou.aliyuncs.com/gruul/20200321/gongan.jpg"
        />
        <span @click="goPolice" class="pointer"
          >浙公网安备33020502000496号</span
        >
      </p>
    </footer>
  </div>
</template>

<script lang="ts">
import { Vue, Component } from "vue-property-decorator";
import SignInByPhone from "@/views/sign/SignInByPhone.vue";
import SignInByQrCode from "@/views/sign/SignInByQrCode.vue";
import SignUpByPhone from "@/views/sign/SignUpByPhone.vue";
import SignUpByQrCode from "@/views/sign/SignUpByQrCode.vue";
import { getSystemConfig } from "@/api/sign";

@Component({
  components: {
    SignInByPhone,
    SignUpByPhone,
    SignUpByQrCode,
    SignInByQrCode,
  },
})
export default class Sign extends Vue {
  currenType = "SignInByPhone";

  mounted() {
    if (this.$route.query.type) {
      this.changeLoginType(String(this.$route.query.type));
    }
  }

  changeLoginType(type: string) {
    this.currenType = type;
  }

  async linkTo() {
    try {
      const response = await getSystemConfig();
      const { code, data } = response;
      if (code === 200) {
        if (data.systemConfig && data.systemConfig.consoleLog) {
          open(`${data.systemConfig.consoleLog}`, "_top");
        }
      }
    } catch (e) {
      console.log(e);
    }
  }

  goPolice() {
    open(
      `http://www.beian.gov.cn/portal/registerSystemInfo?recordcode=33020502000496`,
    );
  }
}
</script>

<style lang="scss">
@import "../../assets/styles/sign/sign";
</style>
