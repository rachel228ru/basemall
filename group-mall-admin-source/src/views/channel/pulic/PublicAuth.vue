<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:09
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-25 13:27:41
-->
<template>
  <div class="auth publicAuth">
    <div class="auth__info">
      <span class="auth__info--title">{{ config.miniName }}</span>
      <el-tag style="margin-left: 8px;">
        <span v-if="config.verifyTypeInfo < 0">未认证</span>
        <span v-if="config.verifyTypeInfo >= 0">已认证</span>
        <span v-if="config.serviceTypeInfo <= 1">订阅号</span>
        <span v-if="config.serviceTypeInfo === 2">服务号</span>
        <span v-if="config.serviceTypeInfo === 3">小程序</span>
      </el-tag>
    </div>
    <table class="auth__table">
      <tr>
        <td colspan="2" class="auth__table--text">
          <div class="lump"></div>
          <span>基本信息</span>
        </td>
      </tr>
      <tr>
        <td>Appid：</td>
        <td>{{ config.appid }}</td>
      </tr>
      <tr>
        <td>微信号：</td>
        <td>{{ config.alias }}</td>
      </tr>
      <tr>
        <td>主体名称：</td>
        <td>{{ config.principalName }}</td>
      </tr>
      <tr>
        <td>授权管理状态</td>
        <td v-if="config.authorizerFlag">
          <span>已授权</span>
        </td>
        <td v-if="!config.authorizerFlag">
          <span>未授权</span>
          <el-button type="primary" @click="authPublic">重新授权</el-button>
        </td>
      </tr>
      <td>
        <el-image
          style="width: 100px; height: 100px"
          :src="config.logo"
          fit="cover"
        />
      </td>
    </table>

    <div class="auth__buttons">
      <el-button type="text" @click="authListClick">已授权列表</el-button>
    </div>
    <el-dialog
      class="list"
      title="授权权限列表"
      width="600px"
      :visible.sync="dialogVisible"
    >
      <div class="auth__list">
        <div
          class="auth__list__item"
          v-for="(auth, index) in config.authInfo"
          :key="index"
        >
          <span class="check">✓</span>
          <span>{{ auth }}</span>
        </div>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="dialogVisible = false"
          >确 定</el-button
        >
      </span>
    </el-dialog>
  </div>
</template>

<script lang="ts">
import { Vue, Component, Prop } from "vue-property-decorator";
import storage from "@/libs/storage";
import { authPreauthcode } from "@/api/channel/channel";
import { MpSettingType } from "../channelType";

@Component
export default class PublicUnauth extends Vue {
  @Prop({
    default: () => {
      return {};
    },
  })
  config!: MpSettingType;

  dialogVisible = false;

  // 立刻授权
  async authPublic() {
    let platformShopId;
    if (
      storage.get("userInfo") &&
      storage.get("userInfo").shopInfoVo.platformShopId
    ) {
      platformShopId = storage.get("userInfo").shopInfoVo.platformShopId;
    } else {
      return;
    }
    const response = await authPreauthcode({
      // 要授权的帐号类型：1-公众号、2-小程序
      authType: 1,
      // 平台店铺id
      platformShopId,
      // 授权成功后跳转地址,前端地址
      successPage: window.location.href,
    });
    const { code, data } = response;
    if (code === 200) {
      open(data, "_top");
    }
  }

  authListClick() {
    if (this.config.authInfo && this.config.authInfo.length > 0) {
      this.dialogVisible = true;
    } else {
      this.$message({
        type: "warning",
        message: "暂无公众号授权信息",
      });
    }
  }
}
</script>

<style lang="scss">
.list {
  .el-dialog__header {
    border-bottom: 1px solid #cccccc;
  }
}

.auth__list {
  display: flex;
  flex-wrap: wrap;
  .auth__list__item {
    flex: 0 0 50%;

    .check {
      font-weight: bolder;
      font-size: 16px;
      color: #0e76ff;
    }
  }
}

.publicAuth {
  margin-left: 20px;
  .auth__info {
    margin-top: 20px;
  }
}
</style>
