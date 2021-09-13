<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:10
 * @LastEditors: vikingShip
 * @LastEditTime: 2021-08-25 15:13:08
-->
<template>
  <div style="min-height: 800px !important;">
    <el-tabs
      v-model="comName"
      class="top--tag"
      @tab-click="selectType"
      v-if="!showType"
    >
      <el-tab-pane label="展示分类" name="ShowClass"></el-tab-pane>
    </el-tabs>
    <component :is="comName"></component>
  </div>
</template>

<script lang="ts">
import { Vue, Component } from "vue-property-decorator";
import ShowClass from "./components/ShowClass.vue";
@Component({
  components: {
    ShowClass,
  },
})
export default class GoodClass extends Vue {
  /** 组件名称 */
  comName = "ShowClass";

  /** 是否商品发布进入 */
  showType = false;

  /** 设置专区进入 */
  showClassType = false;

  mounted() {
    this.showType = false;
    this.showClassType = false;

    this.comName = this.$route.query.options === "2" ? "PickShow" : "ShowClass";
    if (this.$route.query.options) {
      this.showType = true;
    }

    if (this.$route.query.options === "classSet") {
      this.showType = false;
    }
  }

  /**
   * 选择顶部分类
   */
  selectType(tab:{index:string}) {
    const index = tab.index;
    this.comName = index === "0" ? "ShowClass" : "PickShow";
  }
}
</script>

<style lang="scss" scoped>
/deep/ .el-tabs__nav-wrap::after {
  content: "";
  position: absolute;
  left: 0;
  bottom: 0;
  width: 100%;
  height: 2px;
  background-color: #f5f5f5;
  z-index: 1;
}
</style>
