<template>
  <div class="setting">
    <el-tabs v-model="comName" @tab-click="handleClick">
      <el-tab-pane label="账户管理" name="Account"></el-tab-pane>
      <el-tab-pane label="充值" name="Recharge"></el-tab-pane>
      <el-tab-pane label="订购订单" name="Order"></el-tab-pane>
      <el-tab-pane label="开票信息" name="Billing"></el-tab-pane>
    </el-tabs>
    <component :is="comName"></component>
  </div>
</template>

<script lang="ts">
import { Vue, Component } from "vue-property-decorator";
import Account from "@/views/businessCenter/Account/Account.vue";
import Recharge from "@/views/businessCenter/Recharge/Recharge.vue";
import Order from "@/views/businessCenter/Order/Order.vue";
import Billing from "@/views/businessCenter/Billing/Billing.vue";

@Component({
  components: {
    Account,
    Recharge,
    Order,
    Billing,
  },
})
export default class SettingIndex extends Vue {
  comName = "Account";

  created() {
    if (this.$route.query.tabName) {
      this.comName = this.$route.query.tabName as string;
    }
  }

  handleClick(e) {
    this.comName = e.name;
    history.pushState(
      {},
      null,
      `${location.origin}${location.pathname}?tabName=${this.comName}`,
    );
  }
}
</script>

<style lang="scss" scoped>
@import "src/assets/styles/businessCenter/businessCenter.scss";
</style>
