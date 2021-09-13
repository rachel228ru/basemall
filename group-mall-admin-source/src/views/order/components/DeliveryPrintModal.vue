<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:11
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-27 11:18:58
-->
<template>
  <el-dialog
    :visible.sync="visible"
    title="商品发货"
    class="send"
    width="450px"
  >
    <el-row :gutter="10" class="send__field">
      <el-col :span="24">
        <el-form ref="form" :model="form" label-width="80px">
          <el-form-item label="打印机" required>
            <el-select
              v-model="form.printCode"
              placeholder="选择打印机"
              style="width:100%"
            >
              <el-option
                v-for="item of logisticsPrinterVos"
                :key="item.deviceKey"
                :label="item.deviceName"
                :value="item.deviceKey"
              ></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="物流公司" required>
            <el-select
              v-model="form.deliverCode"
              placeholder="选择物流公司"
              style="width:100%"
            >
              <el-option
                v-for="item of logisticsCompany"
                :key="item.code"
                :label="item.name"
                :value="item.code"
              ></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="发货地址" required>
            <el-select
              v-model="form.expressId"
              :disabled="!logisticsAddressVos.length"
              placeholder="选择发货地址"
              style="width:100%"
            >
              <el-option
                v-for="item of logisticsAddressVos"
                :key="item.code"
                :label="item.address"
                :value="item.expressId"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-form>
      </el-col>
    </el-row>
    <span slot="footer" class="dialog-footer">
      <el-button @click="toggleVisible">取 消</el-button>
      <el-button type="primary" @click="onConfirm">确 定</el-button>
    </span>
  </el-dialog>
</template>

<script lang="ts">
import { Vue, Component, Prop, Watch } from "vue-property-decorator";
import {
  DeliveryOrderList,
  LogisticsCompanyType,
  LogisticsPrinterVos,
} from "../orderType";

@Component
export default class DeliverySendModal extends Vue {
  @Prop({ default: true })
  value!: boolean;

  @Prop()
  currentOrder!: DeliveryOrderList;

  @Prop()
  logisticsCompany!: LogisticsCompanyType[];

  @Prop()
  logisticsPrinterVos!: LogisticsPrinterVos[];

  // @Prop()
  // logisticsAddressVos;

  get logisticsAddressVos() {
    try {
      const { logisticsExpressAddressVos } = this.logisticsCompany.find(
        item => item.code === this.form.deliverCode,
      ) as LogisticsCompanyType;
      return logisticsExpressAddressVos;
    } catch (err) {
      return [];
    }
  }

  /** 模态框显示隐藏 */
  get visible() {
    return this.value;
  }

  set visible(v) {
    this.$emit("input", v);
  }

  /** 默认物流公司 */
  get defLogisticsCompany() {
    return (
      this.logisticsCompany.find(item => item.isDefault === 1) || { code: "" }
    );
  }

  /** 监听弹窗变化 派发对应事件 */
  @Watch("visible")
  handleVisibleChange(v: boolean) {
    if (v) {
      this.form.deliverCode = this.defLogisticsCompany.code;
    } else {
      this.$nextTick(() => {
        this.resetForm();
      });
    }
  }

  /** 物流公司变化 设置发货地址列表 */
  @Watch("form.deliverCode")
  onDeliverCodeChnage() {
    this.form.expressId = "";
  }

  form = {
    deliverCode: "",
    expressId: "",
    orderIds: [],
    printCode: "",
    shopId: "",
  };

  toggleVisible() {
    this.visible = !this.visible;
  }

  resetForm() {
    this.form = {
      deliverCode: "",
      expressId: "",
      orderIds: [],
      printCode: "",
      shopId: "",
    };
  }

  /** 确认发货 */
  onConfirm() {
    this.$emit("onConfirm", this.form);
  }
}
</script>

<style lang="scss" scoped>
@include b(send) {
  @include e(field) {
    font-size: 13px;

    .el-form-item {
      margin-bottom: 10px;
    }
  }
}
</style>
