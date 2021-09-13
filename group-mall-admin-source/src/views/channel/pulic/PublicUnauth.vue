<template>
  <div class="unauth">
    <div class="unauth__empty">
      <span>暂未授权</span>
      <el-button
        type="primary"
        class="unauth__empty--button"
        @click="authPublic"
        >立即授权</el-button
      >
      <div>
        <span class="grey">未注册公众号,</span>
        <el-button
          type="text"
          class="unauth__empty--button"
          @click="applyHandle"
          >立即申请</el-button
        >
      </div>
    </div>
    <div class="unauth__info">
      <div class="unauth__info--tips">
        <div class="left grey">
          温馨提示
        </div>
        <ul class="right">
          <li class="grey">一个微信公众号只能和一个店铺绑定</li>
          <li class="error">
            为保证所有功能正常，授权时请保持默认选择，把权限统一授权给启山智软
          </li>
        </ul>
      </div>
      <div class="unauth__info--table">
        <div class="left grey">
          公众号类型
        </div>
        <table cellspacing="0">
          <tr>
            <td></td>
            <td>未认证订阅号</td>
            <td>认证订阅号</td>
            <td>未认证服务号</td>
            <td>认证服务号</td>
          </tr>
          <tr>
            <td>消息自动回复</td>
            <td><i class="el-icon-check select" /></td>
            <td><i class="el-icon-check select" /></td>
            <td><i class="el-icon-check select" /></td>
            <td><i class="el-icon-check select" /></td>
          </tr>
          <tr>
            <td>微信自定义菜单</td>
            <td></td>
            <td><i class="el-icon-check select" /></td>
            <td><i class="el-icon-check select" /></td>
            <td><i class="el-icon-check select" /></td>
          </tr>
          <tr>
            <td>群发/定时群发</td>
            <td></td>
            <td><i class="el-icon-check select" /></td>
            <td></td>
            <td><i class="el-icon-check select" /></td>
          </tr>
          <tr>
            <td>高级客户管理</td>
            <td></td>
            <td>部分功能</td>
            <td></td>
            <td><i class="el-icon-check select" /></td>
          </tr>
          <tr>
            <td>可申请微信支付</td>
            <td></td>
            <td></td>
            <td></td>
            <td><i class="el-icon-check select" /></td>
          </tr>
        </table>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { Vue, Component } from "vue-property-decorator";
import { authPreauthcode } from "@/api/channel/channel";
import storage from "@/libs/storage";

@Component
export default class PublicUnauth extends Vue {
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

  // 立即申请
  applyHandle() {
    open("https://mp.weixin.qq.com/");
  }
}
</script>

<style lang="scss" scoped></style>
