<!--
 * @description: 
 * @Author: vikingShip
 * @Date: 2021-08-21 09:28:28
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-25 14:34:49
-->
<template>
  <div class="message">
    <div class="message__info">
      <p>使用须知:</p>
      <p>
        1、使用商家通知需先绑定认证服务号，并于服务号后台开通“模板消息”功能并并从“模板库”中添加相应模板ID至“我的模板”中；
      </p>
      <p>
        2、使用买家通知消息需先绑定小程序，并从“模板库”中添加相应模板ID至“我的模板”中；
      </p>
      <p>
        3、每个认证服务号或小程序最多可同时启用25个模板ID（公众号相同的模板ID计为1个；小程序相同的模板ID且字段及顺序相同时计为1个）；公众号或小程序已添加的模板一旦达到25个，可能无法正常推送消息，此时请商家进入微信公众号后台删除部分不启用的模板。
      </p>
    </div>
    <el-tabs
      v-model="activeName"
      @tab-click="tabClickHandle"
      class="message-tabs"
    >
      <el-tab-pane
        label="买家通知"
        name="Buyer"
        class="Buyer-tab"
      ></el-tab-pane>
      <el-tab-pane
        label="云短信"
        name="Exception"
        class="Merchant-tab"
      ></el-tab-pane>
    </el-tabs>
    <component :is="activeName" />
  </div>
</template>

<script lang="ts">
import { Vue, Component } from "vue-property-decorator";
import Buyer from "@/views/setting/common/buyer/Buyer.vue";
import Merchant from "@/views/setting/common/merchant/Merchant.vue";
import Exception from "@/components/Exception.vue";

@Component({
  components: {
    Exception,
    Merchant,
    Buyer,
  },
})
export default class NoteSetting extends Vue {
  activeName: any = "Buyer";

  created() {
    if (this.$route.query.tabName) {
      this.activeName = this.$route.query.tabName as string;
    }
  }

  // tab跳转路由添加参数tabName
  tabClickHandle(tab: { name: string }) {
    history.pushState(
      {},
      "",
      `${process.env.VUE_APP_BASEPATH}/setting?tabName=${tab.name}`,
    );
  }
}
</script>

<style lang="scss">
@import "src/assets/styles/message/message.scss";
</style>
