<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-26 09:23:18
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-26 14:36:43
-->
<template>
  <div class="SignFlow">
    <div class="SignFlow__tabs">
      <div class="SignFlow__tabs--title">FREE SIGNUP</div>
      <div class="SignInForm__SignUp" @click="changeSignType">
        <span>已有账号？</span>
        <el-button type="text">马上登录</el-button>
      </div>
    </div>
    <div class="SignFlow__QrCode">
      <ScanQrCode
        redirectUrl="/0.1/sign?type=SignUpByPhone"
        scenes="AccountRegister"
      />
    </div>
  </div>
</template>

<script lang="ts">
import { Vue, Component, Emit } from "vue-property-decorator";
import ScanQrCode from "@/components/ScanQrCode.vue";
import { verifyStateResult } from "@/api/sign";

@Component({
  components: {
    ScanQrCode,
  },
})
export default class SignIn extends Vue {
  @Emit("change")
  changeSignType() {
    return "SignInByPhone";
  }

  created() {
    if (this.$route.query.code && this.$route.query.type === "SignUpByQrCode") {
      this.verifyState();
    }
  }

  async verifyState() {
    try {
      await verifyStateResult({
        code: this.$route.query.code,
      });
      history.pushState(
        {},
        "",
        `/0.1/sign?type=SignUpByPhone&code=${this.$route.query.code}`,
      );
      open(location.href, "_top");
    } catch (e) {
      this.$message({
        type: "warning",
        message: e,
      });
      history.pushState({}, "", `/login`);
    }
  }
}
</script>
