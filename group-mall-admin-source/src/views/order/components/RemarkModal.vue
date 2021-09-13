<!--
 * @description: 
 * @Author: vikingShip
 * @Date: 2021-07-17 13:29:52
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-25 14:11:02
-->
<template>
  <el-dialog :title="title" :visible.sync="dialogVisible" width="30%">
    <div style="margin-bottom:15px" v-if="orderIds.length > 1">
      你选择了{{ orderIds.length }}记录
    </div>
    <el-input
      type="textarea"
      :rows="2"
      placeholder="请输入内容"
      v-model="form.note"
    />
    <div style="margin-top:10px">
      <el-checkbox v-model="form.over">覆盖已有备注内容</el-checkbox>
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button @click="onCancel">取 消</el-button>
      <el-button @click="onOk" type="primary">确 定</el-button>
    </span>
  </el-dialog>
</template>

<script lang="ts">
import { Vue, Component, Prop, Watch } from "vue-property-decorator";
import { DeliveryOrderList } from "../orderType";

@Component
export default class RemarkModal extends Vue {
  @Prop({ default: "商家回复" })
  title!: string;

  @Prop()
  value!: boolean;

  @Prop({
    default() {
      return [];
    },
  })
  currentOrder!: DeliveryOrderList;

  @Prop({
    default() {
      return [];
    },
  })
  orderIds!: DeliveryOrderList[];

  get dialogVisible() {
    return this.value;
  }

  set dialogVisible(v) {
    if (!v) {
      this.resetForm();
    }
    this.$emit("input", v);
  }

  form = { note: "", over: false };

  @Watch("currentOrder")
  handleCurrentOrderChange(v: DeliveryOrderList) {
    if (v) {
      Object.assign(this.form, { note: v.note });
    }
  }

  resetForm() {
    this.form = { note: "", over: false };
  }

  onOk() {
    this.$emit("onOk", this.form);
  }

  onCancel() {
    this.$emit("onCancel");
  }
}
</script>

<style lang="scss" scoped></style>
