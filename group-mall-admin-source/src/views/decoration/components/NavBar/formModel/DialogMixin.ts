/*
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:10
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-24 15:14:52
 */
import Vue from "vue";
import Component from "vue-class-component";

// You can declare a mixin as the same style as components.
@Component
export default class DialogMixin extends Vue {
  dialogVisible = false;

  /**
   * 关闭弹窗
   */
  async closeHandle() {
    try {
      const result = await this.confirmHandle();
      if (result === "confirm") {
        this.dialogVisible = false;
      }
    } catch (e) {
      console.log(e);
    }
  }

  async beforeCloseHandle(done: () => void) {
    try {
      const result = await this.confirmHandle();
      if (result === "confirm") {
        done();
      }
    } catch (e) {
      console.log(e);
    }
  }

  /**
   * 关闭确认
   */
  async confirmHandle() {
    const result = await this.$confirm(
      "确定退出该窗口？退出后，数据可能不保留！",
      "提示",
      {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      },
    );
    return result;
  }
}
