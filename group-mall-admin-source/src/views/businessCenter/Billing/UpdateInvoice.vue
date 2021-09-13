<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:09
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-27 11:10:20
-->
<template>
  <el-dialog :visible.sync="dialogVisible" width="600px" title="发票抬头">
    <div class="invoice-container">
      <el-form
        :model="dataForm"
        ref="invoiceRef"
        class="dataForm"
        :rules="rules"
        hide-required-asterisk
        label-width="120px"
        label-position="left"
      >
        <el-form-item label="抬头类型" prop="headType">
          <el-radio-group v-model="dataForm.headType">
            <el-radio :label="1">个人或事业单位</el-radio>
            <el-radio :label="2">企业</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="发票抬头" prop="invoiceRiseName">
          <el-input
            placeholder="抬头名称"
            class="el-input-width"
            v-model="dataForm.invoiceRiseName"
          >
          </el-input>
        </el-form-item>
        <el-form-item label="纳税人识别号" prop="invoiceTaxpayerNum">
          <el-input
            placeholder="纳税人识别号或社会统一征信代码"
            class="el-input-width"
            v-model="dataForm.invoiceTaxpayerNum"
          >
          </el-input>
        </el-form-item>
        <el-form-item prop="defaultStatus">
          <el-checkbox
            :true-label="1"
            :false-label="0"
            v-model="dataForm.defaultStatus"
            >设为默认</el-checkbox
          >
        </el-form-item>
      </el-form>
    </div>
    <span slot="footer" class="dialog--footer">
      <el-button @click="closeHandle">取 消</el-button>
      <el-button type="primary" @click="confirmClickHandle">保存</el-button>
    </span>
  </el-dialog>
</template>

<script lang="ts">
import {
  Vue,
  Component,
  PropSync,
  Watch,
  Prop,
  Ref,
} from "vue-property-decorator";
import { createInvoice, updateInvoice } from "@/api/businessCenter/invoice";

@Component
export default class GiveCoupons extends Vue {
  @Ref() readonly invoiceRef!: HTMLFormElement;

  @PropSync("visible", {
    type: Boolean,
    default: false,
  })
  dialogVisible!: boolean;

  @Prop({
    type: Object,
    default() {
      return {};
    },
  })
  invoice: any;

  /** 抬头表单 */
  dataForm = {
    accountId: "",
    defaultStatus: 0,
    headType: 1,
    id: 0,
    invoiceRiseName: "",
    invoiceTaxpayerNum: "",
    remark: "",
  };

  rules = {
    headType: [
      { required: true, message: "请选择抬头类型", trigger: "change" },
    ],
    invoiceRiseName: [
      { required: true, message: "请输入抬头名称", trigger: "change" },
    ],
    invoiceTaxpayerNum: [
      { required: true, message: "请输入纳税人识别号", trigger: "change" },
    ],
  };

  @Watch("dialogVisible")
  handleVisibleChange(v: boolean) {
    if (v) {
      Object.assign(this.dataForm, JSON.parse(JSON.stringify(this.invoice)));
    } else {
      this.invoiceRef.resetFields();
    }
  }

  async confirmClickHandle() {
    try {
      const result = await this.invoiceRef.validate();
      if (!result) {
        return;
      }
      const response = this.dataForm.id
        ? await updateInvoice(this.dataForm)
        : await createInvoice(this.dataForm);
      const { code } = response;
      if (code === 200) {
        this.$message({
          type: "success",
          message: `${this.dataForm.id ? "更新" : "增加"}发票抬头成功`,
        });
        this.dialogVisible = false;
        this.$emit("refreshDataList", 1);
      } else {
        this.$message({
          type: "warning",
          message: "请重新填写信息！",
        });
      }
    } catch (e) {
      console.log(e);
    }
  }

  /**
   * 关闭弹窗
   */
  async closeHandle() {
    this.dialogVisible = false;
  }
}
</script>

<style lang="scss" scoped>
.invoice-container {
  padding: 15px 35px;
}

.form-card {
  padding: 35px;
  border: 1px solid #dcdfe6;
}
</style>
