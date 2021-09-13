<!--
 * @description: 
 * @Author: vikingShip
 * @Date: 2021-07-17 13:29:52
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-27 09:28:52
-->
<template>
  <div>
    <AddGoodTem v-if="saleMode"></AddGoodTem>
  </div>
</template>

<script lang="ts">
import { Vue, Component } from "vue-property-decorator";
import AddGoodTem from "./components/AddGoodTem/index.js";
import storage from "@/libs/storage";

Component.registerHooks(["beforeRouteLeave"]);
@Component({
  components: {
    AddGoodTem,
  },
})
export default class AddGood extends Vue {
  saveFlag = false;

  saleMode = "";

  mounted() {
    this.saleMode = this.$route.query.saleMode as string;
  }

  beforeRouteLeave(_to: any, _from: any, next: () => void) {
    if (this.saveFlag) {
      next();
      return;
    }
    this.$confirm(
      `确定退出${
        this.$route.params.id ? "编辑" : "发布"
      }商品页面?退出后，未保存的信息将不会保留!`,
      "提示",
      {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      },
    ).then(() => {
      storage.delete("formModel");
      storage.delete("allFoorm");
      next();
    });
  }
}
</script>

<style lang="scss" scoped></style>
