<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:10
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-23 16:02:40
-->
<template>
  <div class="module">
    <el-dialog
      title="更换模板"
      width="1150px"
      top="15px"
      :visible.sync="syncDialogShow"
      :before-close="handleClose"
    >
      <Module :visible.sync="syncDialogShow" @hideDialog="handleClose" />
    </el-dialog>
  </div>
</template>
<script lang="ts">
import { Vue, Component, PropSync } from "vue-property-decorator";
import Module from "../../../../../Module2.vue";
@Component({
  components: {
    Module,
  },
})
export default class ModuleDialog extends Vue {
  /** 设置更换模板弹框 */
  @PropSync("dialogShow", { type: Boolean, default: false })
  syncDialogShow!: boolean;

  /**
   * @LastEditors: chuyinlong
   * @description: 关闭更换模板弹框
   */

  handleClose() {
    this.syncDialogShow = false;
  }
}
</script>
<style lang="scss" scoped></style>
