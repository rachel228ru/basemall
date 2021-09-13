<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:10
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-23 18:02:02
-->
<template>
  <!-- 搜索 -->
  <div class="search" :class="searchFormData.showStyle">
    <i class="search__iconfont el-icon-search"></i>
    <span>{{ searchFormData.keyWord }}</span>
  </div>
</template>

<script lang="ts">
import { Vue, Component, PropSync } from "vue-property-decorator";
import Search from "./Search";

@Component
export default class SearchPreview extends Vue {
  @PropSync("formData", {
    type: Object,
    default() {
      return null;
    },
  })
  searchFormData!: Search;
}
</script>
