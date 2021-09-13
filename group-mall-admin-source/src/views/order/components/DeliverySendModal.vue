<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:11
 * @LastEditors: vikingShip
 * @LastEditTime: 2021-08-25 15:58:14
-->
<template>
  <el-dialog
    :visible.sync="visible"
    title="商品发货"
    class="send"
    width="550px"
  >
    <el-row :gutter="10" class="send__field">
      <el-col :span="24" v-if="currentOrder">
        <el-form label-width="80px">
          <el-form-item label="订单号">
            {{ currentOrder.orderId }}
          </el-form-item>
          <el-form-item label="创建时间">
            {{ currentOrder.createTime }}
          </el-form-item>
          <el-form-item label="地址">
            {{ currentOrder.receiverName }},{{ currentOrder.receiverPhone }}
            {{ currentOrder.receiverProvince }}
            {{ currentOrder.receiverCity }}
            {{ currentOrder.receiverRegion }}
            {{ currentOrder.receiverDetailAddress }}
          </el-form-item>
        </el-form>
      </el-col>
      <el-col :span="24">
        <el-form ref="form" :model="form" label-width="80px">
          <el-form-item label="打印机" required>
            <el-select
              v-model="form.printCode"
              placeholder="选择打印机"
              style="width: 370px"
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
              style="width: 370px"
            >
              <el-option
                v-for="item of company"
                :key="item.code"
                :label="item.name"
                :value="item.code"
              >
                {{ item.name }}
              </el-option>
            </el-select>
            <el-link
              type="primary"
              :underline="false"
              style="margin-left: 4px"
              @click="setDefaultAddress"
              :disabled="deliverCodeBtnStatus"
              >设为默认</el-link
            >
          </el-form-item>
          <el-form-item label="发货地址" required>
            <el-select
              v-model="form.expressId"
              :disabled="!logisticsAddressVos.length"
              placeholder="选择发货地址"
              style="width: 370px"
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
import { DeliveryOrderList, LogisticsCompanyType, LogisticsPrinterVos } from "../orderType";
import { setDefaultAddress } from "@/api/logistics/logistics";

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

  get company() {
    return this.logisticsCompany.map(item => {
      if (item.isDefault === 1) {
        item.name = item.name + "(默认)";
      }
      return item;
    });
  }

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

  get deliverCodeBtnStatus() {
    return (
      !this.form.deliverCode ||
      (this.defLogisticsCompany &&
        this.defLogisticsCompany.code === this.form.deliverCode)
    );
  }

  /** 监听弹窗变化 派发对应事件 */
  @Watch("value")
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

  resetForm() {
    this.form = {
      deliverCode: "",
      expressId: "",
      orderIds: [],
      printCode: "",
      shopId: "",
    };
  }

  toggleVisible() {
    this.visible = !this.visible;
  }

  /** 设置默认发货公司 */
  setDefaultAddress() {
    const { logisticsCompanyId } = this.logisticsCompany.find(
      item => item.code === this.form.deliverCode,
    ) as LogisticsCompanyType;
    setDefaultAddress(logisticsCompanyId)
      .then(() => {
        this.$emit("reset");
        this.$message.success("设置成功");
      })
      .catch(err => {
        this.$message.warning(err);
      });
  }

  onConfirm() {
    if (!this.form.deliverCode) {
      this.$message.warning("请选择物流公司");
      return;
    }
    this.$emit(
      "onConfirm",
      Object.assign(this.form, { orderIds: [this.currentOrder.orderId] }),
    );
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
