<!--
 * @description: 
 * @Author: vikingShip
 * @Date: 2021-07-17 13:29:52
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-27 10:40:08
-->
<template>
  <div>
    <el-tabs v-model="activeName" @tab-click="handleClick">
      <el-tab-pane
        v-for="(item, index) in list"
        :key="index"
        :label="item.modeName"
        :name="item.modeName"
        :id="item.id"
      ></el-tab-pane>
    </el-tabs>
    <ListApart :saleMode="chooseId" ref="ListApart"></ListApart>
  </div>
</template>

<script lang="ts">
import { Vue, Component, Ref } from "vue-property-decorator";
import ListApart from "./ListApart.vue";
import { GoodState, ApiSpecArea } from "./marketType";
import { getAllRegionList } from "@/api/good/goods";

@Component({
  components: {
    ListApart,
  },
})
export default class Goods extends Vue implements GoodState {
  @Ref()
  readonly ListApart?: HTMLFormElement;

  list: Array<ApiSpecArea> = [];

  activeName = "";

  chooseId: string | number = "";

  fromSaleMode = "";

  mounted() {
    this.fromSaleMode = this.$route.params.saleMode;
    this.init();
  }

  /**
   * 获取所有专区
   */
  init() {
    getAllRegionList({}).then(res => {
      if (!res.data.length) {
        this.$message.error("请先添加商品专区");
        this.$router.push("/goodRegion");
        return;
      }
      this.list = res.data;
      this.activeName = res.data[0].modeName || "";
      this.chooseId = res.data[0].id || "";
      if (this.fromSaleMode) {
        const chooseItem = res.data.filter(
          item => item.id === this.fromSaleMode,
        );
        this.activeName = chooseItem[0].modeName;
        this.chooseId = chooseItem[0].id || "";
      }
    });
  }

  /**
   * 顶部专区选择
   */
  handleClick(tab: { index: number }) {
    this.chooseId = this.list[tab.index].id || "";
  }
}
</script>

<style lang="scss" scoped></style>
