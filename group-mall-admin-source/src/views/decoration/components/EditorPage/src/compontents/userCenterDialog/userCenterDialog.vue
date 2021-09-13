<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:10
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-24 16:41:58
-->
<template>
  <div class="userCenterDialog">
    <el-dialog
      title="用户中心"
      width="1050px"
      top="15px"
      :visible.sync="syncDialogShow"
      :before-close="handleClose"
    >
      <user-center></user-center>
    </el-dialog>
  </div>
</template>
<script lang="ts">
import { Vue, Component, PropSync } from "vue-property-decorator";
import UserCenter from "@/views/decoration/UserCenter.vue";
@Component({
  components: {
    UserCenter,
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
