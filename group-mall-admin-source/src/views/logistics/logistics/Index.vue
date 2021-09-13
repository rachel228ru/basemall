<!--
 * @description: 
 * @Author: vikingShip
 * @Date: 2021-08-21 09:28:28
 * @LastEditors: vikingShip
 * @LastEditTime: 2021-08-21 16:32:00
-->
<template>
  <div class="logIndex">
    <el-tabs v-model="comName" @tab-click="handleClick">
      <el-tab-pane label="运费模板" name="LogisticsAttr"></el-tab-pane>
      <el-tab-pane label="地址管理" name="AddressProgram"></el-tab-pane>
      <el-tab-pane label="物流服务" name="LogisticsServe"></el-tab-pane>
    </el-tabs>
    <component :is="comName"></component>
  </div>
</template>

<script lang="ts">
import { Vue, Component } from "vue-property-decorator";
import LogisticsAttr from "./components/logisticsAttr/LogisticsAttr.vue";
import AddressProgram from "./components/addressProgram/AddressProgram.vue";
import LogisticsServe from "./components/logisticsServe/LogisticsServe.vue";

@Component({
  components: {
    LogisticsAttr,
    AddressProgram,
    LogisticsServe,
  },
})
export default class Index extends Vue {
  /** 当前tab */
  comName = "LogisticsAttr";

  /**
   * 切换组件
   */
  handleClick(tab: { name: string }) {
    this.comName = tab.name;
  }
}
</script>

<style lang="scss" scoped>
.logIndex {
  padding: 0px;
}
</style>
