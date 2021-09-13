<template>
  <div class="account">
    <table class="account__manage">
      <tr>
        <td class="ptd" style="width: 120px;">头像</td>
        <td style="width: 280px;">
          <img :src="userInfo.avatarUrl" class="avatarUrl" />
        </td>
        <td style="width: 70px;">
          <el-button type="primary" plain @click="changeInfoHandle"
            >换绑</el-button
          >
        </td>
      </tr>
      <tr>
        <td class="ptd">昵称</td>
        <td>{{ userInfo.nikeName }}</td>
      </tr>
      <tr>
        <td class="ptd">注册账号</td>
        <td>{{ userInfo.phone }}</td>
        <td>
          <el-button type="primary" plain @click="changePhoneHandle"
            >换绑</el-button
          >
        </td>
      </tr>
      <tr>
        <td class="ptd">密码</td>
        <td>******</td>
        <td>
          <el-button
            type="primary"
            plain
            @click="routerLinkHandle('/changepass')"
            >修改</el-button
          >
        </td>
      </tr>
    </table>

    <ChangeInfo
      :visible.sync="accountVisible"
      :userInfo="userInfo"
      @refreshDataList="getRecentAccountInfo"
    />
    <ChangePhone
      :visible.sync="phoneVisible"
      :userInfo="userInfo"
      @refreshDataList="getRecentAccountInfo"
    />
  </div>
</template>

<script lang="ts">
import { Vue, Component } from "vue-property-decorator";
import storage from "@/libs/storage";
import { getAccountInfo, getSystemConfig, verifyStateResult } from "@/api/sign";
import ChangeInfo from "@/views/businessCenter/Component/dialog/ChangeInfo.vue";
import ChangePhone from "@/views/businessCenter/Component/dialog/ChangePhone.vue";
import { logout } from "@/libs";

@Component({
  components: {
    ChangeInfo,
    ChangePhone,
  },
})
export default class AccountManage extends Vue {
  /* 用户信息 */
  userInfo: any = {};

  /* 换绑微信弹窗 */
  accountVisible = false;

  /* 换绑手机弹窗 */
  phoneVisible = false;

  async created() {
    if (this.$route.query.code) {
      const cd = this.$route.query.code;
      history.pushState({}, "", "/business");
      const response = await verifyStateResult({
        code: cd,
      });
      const { code } = response;
      if (code === 200) {
        this.goBack();
      }
    }
    await this.getRecentAccountInfo();
  }

  async getRecentAccountInfo() {
    if (storage.get("token")) {
      const response = await getAccountInfo();
      const { code, data } = response;
      if (code === 200) {
        this.userInfo = data;
        this.$STORE.userStore.setPhone(data.phone)
      }
    }
  }

  changeInfoHandle() {
    this.accountVisible = true;
  }

  changePhoneHandle() {
    this.phoneVisible = true;
  }

  routerLinkHandle(path) {
    this.$router.push(path);
  }

  back() {
    this.$router.back();
  }

  async goBack() {
    logout();
    try {
      const response = await getSystemConfig();
      const { code, data } = response;
      if (code === 200 && data.systemConfig && data.systemConfig.consoleUrl) {
        open(`${data.systemConfig.consoleUrl}`, "_top");
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

<style lang="scss">
.account {
  width: 100%;
  height: 100%;
  color: #515a6e;
  font-size: 13px;
  background-color: #ffffff;

  .account__manage {
    padding-top: 50px;
  }

  .avatarUrl {
    width: 60px;
    border-radius: 100%;
  }
}

.ptd {
  padding: 20px 10px;
}
</style>
