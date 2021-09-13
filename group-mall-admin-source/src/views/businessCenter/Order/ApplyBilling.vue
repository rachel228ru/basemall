<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:09
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-27 11:11:34
-->
<template>
  <el-dialog
    :visible.sync="dialogVisible"
    :title="titleList[auditorStatus]"
    :width="widthList[auditorStatus]"
  >
    <el-form
      class="dataForm"
      label-width="80px"
      label-position="left"
      v-if="auditorStatus === 0"
    >
      <el-form-item label="发票类型">
        增值税专用发票
      </el-form-item>
      <el-form-item label="抬头类型">
        <span v-if="headType === 1">个人或事业单位</span>
        <span v-if="headType === 2">企业</span>
      </el-form-item>
      <el-form-item label="发票抬头">
        <el-select v-model="applyForm.invoiceRiseId" placeholder="发票抬头">
          <el-option
            v-for="item in invoiceList"
            :key="item.id"
            :label="item.invoiceRiseName"
            :value="item.id"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="税号">
        <span>{{ invoiceTaxpayerNum }}</span>
      </el-form-item>
      <el-form-item label="发票金额">
        <span>{{ order.paidPayable }}</span>
        <span class="paidPayableTitle">发票金额为实际付款金额</span>
      </el-form-item>
      <el-form-item label="电子邮件">
        <span>{{ $store.state.user.userInfo.email }}</span>
      </el-form-item>
    </el-form>

    <el-form
      class="dataForm"
      label-width="80px"
      label-position="left"
      v-if="auditorStatus === 1"
    >
      <el-form-item label="发票类型">
        增值税专用发票
      </el-form-item>
      <el-form-item label="发票抬头">
        <span>{{ billing.invoiceRiseName }}</span>
      </el-form-item>
      <el-form-item label="税号">
        <span>{{ billing.invoiceTaxpayerNum }}</span>
      </el-form-item>
      <el-form-item label="发票金额">
        <span>{{ billing.amount }}</span>
      </el-form-item>
      <el-form-item label="申请日期">
        <span>{{ billing.createTime }}</span>
      </el-form-item>
    </el-form>

    <div v-if="auditorStatus === 2">
      <div class="el-message-box__container">
        <div class="el-message-box__status el-icon-warning"></div>
        <div class="el-message-box__message">
          该订单发票已申请成功，请前往邮箱{{
            $store.state.user.userInfo.email
          }}下载
        </div>
      </div>
    </div>
    <div v-if="auditorStatus === 3">
      <div class="el-message-box__container">
        <div class="el-message-box__status el-icon-warning"></div>
        <div class="el-message-box__message">
          该订单发票在线申请已超时，请联系
          <el-button type="text" size="normal">客服</el-button>
        </div>
      </div>
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button @click="dialogVisible = false" v-if="auditorStatus !== 1"
        >取消</el-button
      >
      <el-button
        type="primary"
        @click="dialogVisible = false"
        v-if="auditorStatus === 2 || auditorStatus === 3"
        >确定</el-button
      >
      <el-button
        type="primary"
        @click="dialogVisible = false"
        v-if="auditorStatus === 1"
        >我知道了</el-button
      >
      <el-button type="primary" @click="applyHandle" v-if="auditorStatus === 0"
        >提交申请</el-button
      >
    </span>
  </el-dialog>
</template>

<script lang="ts">
import { Vue, Component, Prop, PropSync, Watch } from "vue-property-decorator";
import { getInvoiceList, applyInvoice } from "@/api/businessCenter/invoice";

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
      return {};
    },
  })
  order: any;

  applyForm = {
    invoiceRiseId: "",
    orderId: "",
    orderType: 2,
    type: 1,
  };

  titleList = [
    "申请开票",
    "发票申请中，商家正在处理如有问题请咨询商家",
    "提示",
    "提示",
  ];

  widthList = ["600px", "450px", "450px", "600px"];

  invoiceList: any[] = [];

  @Watch("dialogVisible")
  async handleVisibleChange(v: boolean) {
    if (v) {
      await this.getInvoice();
    } else {
      this.$emit("refreshDataList", 1);
    }
  }

  get billing() {
    if (this.order && this.order.invoiceOrderApplyVo) {
      return this.order.invoiceOrderApplyVo;
    } else {
      return {};
    }
  }

  // 0 未申请过开票 1 申请过开票并未审核(开票中) 2 申请过开票并已审核 3 已过期
  get auditorStatus() {
    if (this.order.invoiceStatus === 0) {
      const now = new Date().getTime();
      const createTime = new Date(this.order.createTime).getTime();
      // 超过30未申请开票 就无法开票
      if (Math.floor((now - createTime) / 86400000) > 30) {
        return 3;
      } else {
        return 0;
      }
    }
    if (this.order.invoiceStatus === 1) {
      return 1;
    }
    if (this.order.invoiceStatus === 2) {
      return 2;
    }
    // 订单开票异常默认允许开票
    return 0;
  }

  // 版号
  get invoiceTaxpayerNum() {
    if (
      !!this.invoiceList &&
      this.invoiceList.length > 0 &&
      this.applyForm.invoiceRiseId
    ) {
      const select = this.invoiceList.find(invoice => {
        return invoice.id === this.applyForm.invoiceRiseId;
      });
      if (!!select) {
        return select.invoiceTaxpayerNum;
      } else {
        return "";
      }
    } else {
      return "";
    }
  }

  get headType() {
    if (
      !!this.invoiceList &&
      this.invoiceList.length > 0 &&
      this.applyForm.invoiceRiseId
    ) {
      const select = this.invoiceList.find(invoice => {
        return invoice.id === this.applyForm.invoiceRiseId;
      });
      if (!!select) {
        return select.headType;
      } else {
        return "";
      }
    } else {
      return "";
    }
  }

  // 抬头列表
  async getInvoice() {
    try {
      const response = await getInvoiceList();
      const { code, data } = response;
      if (code === 200) {
        this.invoiceList = data;
      } else {
        this.$message({
          type: "warning",
          message: "获取发票抬头失败！",
        });
      }
    } catch (e) {
      console.log(e);
    }
  }

  // 抬头列表
  async applyHandle() {
    try {
      if (!this.$STORE.userStore.userInfo.email) {
        this.$message({
          type: "warning",
          message: "请确认已保存邮箱！",
        });
      }
      const form = {
        invoiceRiseId: this.applyForm.invoiceRiseId,
        orderId: this.order.id,
        orderType: 2,
        type: 1,
      };
      const response = await applyInvoice(form);
      const { code } = response;
      if (code === 200) {
        this.$message({
          type: "success",
          message: "申请发票成功！",
        });
        this.dialogVisible = false;
        this.$emit("refreshDataList", 1);
      } else {
        this.$message({
          type: "warning",
          message: "申请发票失败！",
        });
      }
    } catch (e) {
      this.$message({
        type: "warning",
        message: "申请发票异常！",
      });
    }
  }
}
</script>

<style lang="scss" scoped>
.el-message-box__container {
  padding-left: 50px;
}
.paidPayableTitle {
  padding-left: 15px;
  color: #c1c7d4;
}
</style>
