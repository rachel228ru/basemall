<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:09
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-27 11:12:15
-->
<template>
  <el-dialog :visible.sync="dialogVisible" width="600px" title="订购详情">
    <el-form class="dataForm" label-width="100px" label-position="left">
      <el-form-item label="订单编号">
        <span>{{ order.orderNum }}</span>
      </el-form-item>
      <el-form-item label="店铺名称">
        <span>{{ order.shopName }}</span>
      </el-form-item>
      <el-form-item label="店铺类型">
        <span>{{ order.templateName }}</span>
      </el-form-item>
      <el-form-item label="订购套餐">
        <span>{{ order.packageName }}</span>
      </el-form-item>
      <el-form-item label="订购周期">
        <span>{{ order.packageTime }}天</span>
      </el-form-item>
      <el-form-item label="支付金额">
        <span>{{ order.paidPayable }}元</span>
      </el-form-item>
      <el-form-item label="支付方式">
        <span
          v-if="
            (order.payType === 5 &&
              (order.orderSource === 1 || order.orderSource === 3)) ||
              order.orderSource === 4
          "
          >管理员支付</span
        >
        <span v-else>
          <span v-if="order.payType === 1">余额支付</span>
          <span v-if="order.payType === 2">微信</span>
          <span v-if="order.payType === 3">支付宝</span>
          <span v-if="order.payType === 4">汇款支付</span>
          <span v-if="order.payType === 5 && order.orderSource === 2"
            >系统支付</span
          >
        </span>
      </el-form-item>
      <el-form-item label="订购时间">
        <span>{{ order.createTime }}</span>
      </el-form-item>
      <el-form-item label="订单状态">
        <span v-if="order.status === 0" type="info">待处理</span>
        <span v-if="order.status === 1">处理中</span>
        <span v-if="order.status === 2" type="success">已完成</span>
        <span v-if="order.status === 3" type="warning">已关闭</span>
      </el-form-item>
      <el-form-item
        label="付款方信息"
        v-if="order.payType === 4 && (order.status === 2 || order.status === 3)"
      >
        <div>付款户名：{{ payInfo.name }}</div>
        <div>付款账号：{{ payInfo.account }}</div>
        <div>付款时间：{{ payInfo.paymentTime }}</div>
      </el-form-item>
      <el-form-item
        label="审核时间"
        v-if="order.payType === 4 && !!order.auditTime"
      >
        <span>{{ order.auditTime }}</span>
      </el-form-item>
    </el-form>
  </el-dialog>
</template>

<script lang="ts">
import { Vue, Component, Prop, PropSync } from "vue-property-decorator";

@Component
export default class GiveCoupons extends Vue {
  @PropSync("visible", {
    type: Boolean,
    default: false,
  })
  dialogVisible!: boolean;

  @Prop({
    type: Object,
    required: true,
    default() {
      return {
        auditTime: "",
        auditorStatus: 0,
        id: 0,
        invoiceOrderApplyVo: {
          amount: "",
          auditTime: "",
          createTime: "",
          email: "",
          invoiceRiseName: "",
          invoiceRiseType: null,
          invoiceTaxpayerNum: "",
          orderId: null,
          status: null,
        },
        invoiceStatus: null,
        name: "",
        orderNum: "",
        packageData: "",
        packageTime: null,
        paidPayable: null,
        payInfo: "",
        payType: null,
        shopName: "",
        status: null,
        templateName: "",
      };
    },
  })
  order!: any;

  // 套餐信息
  get packageData() {
    if (this.order.packageData && typeof this.order.packageData === "string") {
      return JSON.parse(this.order.packageData);
    } else {
      return {
        createTime: "",
        deleted: false,
        id: null,
        level: null,
        name: "",
        openState: null,
        packagePrice: "",
        packagePriceUnit: null,
        remark: "",
        templateId: null,
        templateVersionId: null,
      };
    }
  }

  get payInfo() {
    if (this.order.payInfo && typeof this.order.payInfo === "string") {
      return JSON.parse(this.order.payInfo);
    } else {
      return { name: "", paymentTime: "", account: "" };
    }
  }
}
</script>
