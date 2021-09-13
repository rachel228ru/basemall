<!--
 * @description: 
 * @Author: vikingShip
 * @Date: 2021-08-21 09:28:28
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-30 09:28:17
-->
<template>
  <div class="template-container m__page">
    <el-tabs v-model="activeName" class="template-tabs">
      <el-tab-pane label="系统模板" name="System" class="business-tab">
        <component :is="activeName" :templateType="1" ref="componentRef" />
      </el-tab-pane>
    </el-tabs>
    <!--        todo 如果后续验收完成系统模板和定制模板结构仍然一样，可抽成公共组件-->
  </div>
</template>

<script lang="ts">
import { Vue, Component, Ref } from "vue-property-decorator";
import System from "../common/System.vue";

@Component({
  components: {
    System,
  },
})
export default class SystemTemplate extends Vue {
  @Ref() readonly componentRef!: HTMLElement;

  activeName = "System";
}
</script>

<style lang="scss">
.m__page {
  background-color: #ffffff;
  padding: 15px;
}
</style>
