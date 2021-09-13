<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:11
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-09-01 15:48:36
-->
<template>
  <div class="SignFlow">
    <div class="SignFlow__tabs">
      <div class="SignFlow__tabs--title">LOG IN</div>
      <div class="SignFlow__tabs--switch" @click="changeSignType">
        <div class="toolTip-right">
          密码登录
        </div>
        <i class="iconfont iconhalf_pc"></i>
      </div>
    </div>
    <div class="SignFlow__QrCode ScanQrCode__container">
      <ScanQrCode
        :redirectUrl="`/0.1/sign?type=SignInByQrCode`"
        scenes="AccountLoggin"
      />
    </div>
  </div>
</template>

<script lang="ts">
import { Vue, Component, Emit } from "vue-property-decorator";
import ScanQrCode from "@/components/ScanQrCode.vue";
import { unionAccountLogin } from "@/api/sign";
import { login, modifySignStatus } from "@/libs";
import { getShopInfo } from "@/api/businessCenter/setting";

@Component({
  components: {
    ScanQrCode,
  },
})
export default class SignIn extends Vue {
  created() {
    if (this.$route.query.code && this.$route.query.type === "SignInByQrCode") {
      const cd = this.$route.query.code;
      history.pushState(
        {},
        "",
        `/0.1/sign?type=SignInByQrCode&code=${this.$route.query.code}`,
      );
      this.submitForm(cd);
    }
  }

  async submitForm(cd: string | (string | null)[]) {
    try {
      const form = {
        loginType: 3,
        certificate: "",
        phone: "",
        password: "",
        code: cd,
      };
      const response = await unionAccountLogin(form);
      const { data, code } = response;
      if (code === 200) {
        modifySignStatus(data.token, data);
        login(data.token, data);
        // 更新最新的店铺信息
        await getShopInfo();
        this.$router.push("/console");
      }
    } catch (e) {
      this.$message({
        type: "warning",
        message: e,
      });
    }
  }

  @Emit("change")
  changeSignType() {
    return "SignInByPhone";
  }
}
</script>
