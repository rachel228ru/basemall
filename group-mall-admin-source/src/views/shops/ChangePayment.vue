<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-26 15:24:21
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-27 10:59:45
-->
<template>
  <el-dialog
    :visible.sync="dialogVisible"
    class="dialog"
    width="400px"
    title="切换支付服务商"
  >
    <el-form :model="paymentConfig" class="createForm">
      <el-form-item>
        <el-radio-group v-model="paymentConfig.payType">
          <el-radio :label="1" v-if="paymentConfig.payType === 1"
            >微信支付</el-radio
          >
          <el-radio :label="2" v-if="paymentConfig.payType === 2"
            >环迅支付</el-radio
          >
          <el-radio :label="3" v-if="paymentConfig.payType === 3"
            >随行付</el-radio
          >
        </el-radio-group>
      </el-form-item>
      <el-form-item>
        如果你想要其他支付，请联系<el-button
          type="text"
          size="normal"
          @click="goService"
          >客服</el-button
        >
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog--footer">
      <el-button @click="closeHandle">取 消</el-button>
      <el-button type="primary" @click="comfirmClickHandle">确定</el-button>
    </span>
  </el-dialog>
</template>

<script lang="ts">
import { Vue, Component, PropSync, Watch } from "vue-property-decorator";
import { getPaymentConfig } from "@/api/index";

@Component
export default class Create extends Vue {
  @PropSync("visible", {
    type: Boolean,
    default: false,
  })
  Visible!: boolean;

  dialogVisible = false;

  // 更新小程序信息表单
  paymentConfig = {
    // 支付证书上传状态 false-未上传 true-已上传
    certificatesState: false,
    // 环迅商户账户编号
    ipsAccCode: "",
    // 环迅AES秘钥
    ipsAes: "",
    // 环迅证书密码
    ipsCertificatePsw: "",
    // 环迅商户号
    ipsMerCode: "",
    // 环迅私钥
    ipsRsaPrivateKey: "",
    // 环迅公钥
    ipsRsaPublicKey: "",
    // 环迅SHA公钥
    ipsSha: "",
    // 微信支付商户号
    mchId: "",
    // 支付秘钥
    mchKey: "",
    // 支付类型 1-微信支付 2-环迅支付 3-随行支付
    payType: 1,
    // 随行付商户号入驻商户编号
    sxfAccCode: "",
    // 随行付商户号证书密码
    sxfCertificatePsw: "",
    // 随行付合作机构id
    sxfOrgId: "",
    // 随行付商户号秘钥
    sxfPrivateKey: "",
    // 随行付商户号公钥
    sxfPublic: "",
  };

  @Watch("Visible")
  handleVisibleChange(v: any) {
    this.dialogVisible = this.Visible;
    if (v) {
      this.getConfig();
    }
  }

  async getConfig() {
    try {
      const response = await getPaymentConfig({});
      const { code, data } = response;
      if (code === 200) {
        Object.assign(this.paymentConfig, data);
      }
    } catch (e) {
      this.$message({
        message: e,
        type: "warning",
      });
    }
  }

  async comfirmClickHandle() {
    try {
      this.dialogVisible = false;
    } catch (e) {
      this.$message({
        message: e,
        type: "warning",
      });
    }
  }

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

  /**
   * 关闭确认
   */
  async confirmHandle() {
    return await this.$confirm(
      "确定退出该窗口？退出后，数据可能不保留！",
      "提示",
      {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      },
    );
  }

  goService() {
    open(
      "https://cschat-ccs.aliyun.com/index.htm?tntInstId=_1o8FgSO&scene=SCE00006595",
    );
  }
}
</script>
