<!--
 * @description: 
 * @Author: vikingShip
 * @Date: 2021-08-21 09:28:28
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-27 14:36:10
-->
<template>
  <div class="setting">
    <el-tabs v-model="comName" @tab-click="handleClick" ref="tabs">
      <el-tab-pane label="店铺设置" name="Store"></el-tab-pane>
      <el-tab-pane label="交易设置" name="Order"></el-tab-pane>
      <el-tab-pane label="支付配置" name="Payment"></el-tab-pane>
      <el-tab-pane label="打印设置" name="PrintSetting"></el-tab-pane>
      <el-tab-pane label="消息设置" name="NoteSetting"></el-tab-pane>
      <el-tab-pane label="OSS设置" name="OSSSettings"></el-tab-pane>
      <el-tab-pane label="系统模板" name="SystemTemplate"></el-tab-pane>
    </el-tabs>
    <component :is="comName"></component>
  </div>
</template>

<script lang="ts">
import { Vue, Component } from "vue-property-decorator";
import Order from "./modules/Order.vue";
import Store from "@/views/setting/modules/Store.vue";
import PrintSetting from "./modules/PrintSetting.vue";
import NoteSetting from "./modules/NoteSetting.vue";
import Payment from "@/views/setting/modules/Payment.vue";
import OSSSettings from "./modules/OSSSettings.vue";
import SystemTemplate from "./modules/SystemTemplate.vue";

@Component({
  components: {
    Store,
    Payment,
    PrintSetting,
    NoteSetting,
    OSSSettings,
    Order,
    SystemTemplate,
  },
})
export default class SettingIndex extends Vue {
  comName = "Store";

  created() {
    if (this.$route.query.tabName) {
      this.comName = this.$route.query.tabName as string;
    }
  }

  handleClick(e: { name: string }) {
    this.comName = e.name;
    history.pushState(
      {},
      "",
      `${location.origin}${location.pathname}?tabName=${this.comName}`,
    );
  }
}
</script>

<style lang="scss" scoped>
/deep/ .tip {
  vertical-align: center;
  background-color: rgba(246, 248, 250, 1);
  padding: 15px 15px 15px 30px;
  margin-bottom: 30px;

  .tip__title {
    margin-left: 12px;
    color: #586884;
    font-weight: 700;
  }

  .tip__lump {
    display: inline-block;
    width: 3px;
    height: 12px;
    background-color: rgba(255, 153, 0, 1);
  }
}
</style>
