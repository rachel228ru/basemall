<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:09
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-23 20:25:36
-->
<template>
  <div class="sear__page-page">
    <div class="search__box">
      <div class="search-icon">
        <i class="el-icon-search"></i>
      </div>
    </div>
  </div>
</template>
<script lang="ts">
import { Vue, Prop, Component } from "vue-property-decorator";
import BusinessSuper from "./../BusinessSuper";

@Component({
  components: {},
})
export default class SearchPage extends Vue {
  @Prop()
  formData!: BusinessSuper;
}
</script>

<style lang="scss">
.sear__page-page {
  .search__box {
    height: 60px;
    box-sizing: border-box;
    padding: 12px 20px;

    .search-icon {
      height: 35px;
      background-color: rgba(242, 242, 242, 1);
      border-radius: 6px;

      i {
        float: right;
        margin-top: 10px;
        margin-right: 10px;
      }
    }
  }
}
</style>
