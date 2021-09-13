<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:10
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-27 13:18:26
-->
<template>
  <el-dialog
    :visible.sync="dialogVisible"
    :before-close="beforeCloseHandle"
    anpend-to-body
    :modal-append-to-body="false"
    width="450px"
    title="订单栏图标编辑"
  >
    <el-form ref="form" :model="orderItem" label-width="120px">
      <el-form-item label="订单名称">
        {{ orderItem.name }}
      </el-form-item>
      <el-form-item label="订单图标">
        <upload-file :img-url.sync="orderItem.url"></upload-file>
        <div class="el-upload__tip" style="color: #9797A1;">
          (图片格式支持尺寸15×15px,大小不超过180kb,格式 png、jpg、jpeg，gif。)
        </div>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog--footer">
      <el-button @click="closeHandle">取 消</el-button>
      <el-button type="primary" @click="comfirmClickHandle">确 定</el-button>
    </span>
  </el-dialog>
</template>

<script lang="ts">
import { Component, PropSync, Prop, Watch, Emit } from "vue-property-decorator";
import { mixins } from "vue-class-component";
import {
  OrderItem,
  OptionType,
} from "@/views/decoration/interfaces/UserCenter";
import DialogMixin from "./formModel/DialogMixin";
import UploadFile from "@/views/decoration/components/UserCenter/UploadFile.vue";

@Component({
  components: {
    UploadFile,
  },
})
export default class OrderDialog extends mixins(DialogMixin) {
  @PropSync("orderVisible", {
    type: Boolean,
    default: false,
  })
  Visible!: boolean;

  @Prop({
    type: Object,
    default() {
      return {};
    },
  })
  orderProp!: OrderItem & OptionType;

  orderItem: OrderItem & OptionType = {
    key: "",
    name: "",
    url: "",
  };

  @Watch("Visible")
  handleVisibleChange(v: boolean) {
    this.dialogVisible = v;
    if (v === true) {
      this.orderItem.key = this.orderProp.key;
      this.orderItem.name = this.orderProp.name;
      this.orderItem.url = this.orderProp.url;
    }
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 点击确定
   */
  comfirmClickHandle() {
    this.emitUpdateMenu();
    this.dialogVisible = false;
  }

  /**
   * 触发更新菜单事件
   */
  @Emit("updateOrder")
  emitUpdateMenu() {
    return this.orderItem;
  }
}
</script>

<style scoped lang="scss">
.form--item {
  display: flex;
  justify-content: flex-start;
  align-content: flex-end;
  padding: 10px;

  .form-item__label {
    width: 120px;
    text-align: left;
    vertical-align: middle;
    float: left;
    font-size: 14px;
    color: #606266;
    line-height: 14px;
    box-sizing: border-box;
  }
}

.el-upload__tip {
  color: #9797a1;
}
</style>
