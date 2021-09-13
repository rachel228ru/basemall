<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:10
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-25 13:40:47
-->
<template>
  <div>
    <AddGoodTem :from="from"></AddGoodTem>
  </div>
</template>

<script lang="ts">
import { Vue, Component } from "vue-property-decorator";
import AddGoodTem from "@/views/goods/marketModel/components/AddGoodTem/index.js";
import storage from "@/libs/storage";

@Component({
  components: {
    AddGoodTem,
  },
})
export default class EditGood extends Vue {
  /** 编辑商品来源 */
  from: string | string[] = "";

  saveFlag = false;

  mounted() {
    this.from = this.$route.query.from;
  }

  beforeRouteLeave(to, from, next) {
    if (this.saveFlag) {
      next();
      return;
    }
    this.$confirm(
      `确定退出编辑导入商品页面?退出后，未保存的信息将不会保留!`,
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
