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
    </div>
    <div class="unauth__info">
      <div class="unauth__info--tips">
        <div class="left grey">
          温馨提示
        </div>
        <ul class="right">
          <li class="grey">一个微信小程序仅可授权给一个商户</li>
          <li class="grey">
            为保证所有功能正常，授权时请保持默认选择，把权限统一授权给启山智软
          </li>
          <li class="grey">新授权的小程序需与已授权的小程序及公众主体一致</li>
          <li class="grey">
            您在该入口授权的公众账号类型为小程序，请在授权页面正确选择
          </li>
        </ul>
      </div>
      <div class="unauth__info--tips">
        <div class="left grey">
          小程序类型
        </div>
        <ul class="right">
          <li class="grey">
            已认证账号可使用微信支付权限，个人类型账号暂不支持微信认证
          </li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { Vue, Component } from "vue-property-decorator";
import storage from "@/libs/storage";
import { authPreauthcode } from "@/api/channel/channel";

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
      authType: 2,
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
}
</script>

<style lang="scss" scoped></style>
