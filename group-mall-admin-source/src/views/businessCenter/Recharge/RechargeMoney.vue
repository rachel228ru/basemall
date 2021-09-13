<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:09
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-27 11:12:25
-->
<template>
  <el-dialog :visible.sync="dialogVisible" width="700px" title="充值">
    <div class="recharge-container" v-if="!validate">
      <el-form
        :model="dataForm"
        ref="rechargeRef"
        class="dataForm"
        :rules="rules"
        label-width="100px"
        label-position="left"
      >
        <el-form-item label="充值金额" prop="balance">
          <el-input-number
            placeholder="最低充值金额10.00元"
            class="el-input-width"
            v-model.number="dataForm.balance"
            :controls="false"
            :min="10"
            :max="1000000000000"
          >
            <template slot="append">元</template>
          </el-input-number>
          <div>当前可用余额：{{ userInfo.balance || 0 }}元</div>
        </el-form-item>
        <el-form-item label="支付方式" prop="payType">
          <el-radio-group v-model="dataForm.payType">
            <el-radio :label="2">微信支付</el-radio>
            <el-radio :label="1">支付宝支付</el-radio>
            <el-radio :label="3">汇款支付</el-radio>
          </el-radio-group>
          <div v-if="dataForm.payType === 3">
            <span>（请勿使用支付宝汇款支付，支付宝请选择扫码支付）</span>
          </div>
        </el-form-item>
        <div class="remittance" v-if="dataForm.payType === 3">
          <div class="remittance__info">
            <p>
              你需汇款
              {{ dataForm.balance }}
              元至以下账户，汇款成功后上传凭证信息，审核通过后到账
            </p>
            <p>收款方户名： {{ payConfig.name }}</p>
            <p>收款方开户行： {{ payConfig.bankOfDeposit }}</p>
            <p>收款方账户： {{ payConfig.bankCard }}</p>
          </div>
        </div>
      </el-form>
    </div>
    <div class="recharge-container" v-if="validate">
      <div class="wechat-code qrcode" v-if="dataForm.payType === 2">
        <QrCode id="wechat-code" :option="wechatOption" />
        <div class="msg">
          请使用 <span>微信</span> 扫一扫 <br />
          二维码完成支付
        </div>
      </div>
      <div class="alipay-code qrcode" v-if="dataForm.payType === 1">
        <QrCode id="alipay-code" :option="alipayOption" />
        <div class="msg">
          请使用 <span>支付宝</span> 扫一扫 <br />
          二维码完成支付
        </div>
      </div>
      <div class="remittance" v-if="dataForm.payType === 3">
        <el-form
          :model="payInfo"
          class="payInfo"
          :rules="rules2"
          ref="payInfoRef"
          hide-required-asterisk
          label-width="100px"
          label-position="left"
        >
          <el-form-item label="付款方信息"></el-form-item>
          <div class="form-card">
            <el-form-item label="付款户名：" prop="name">
              <el-input
                placeholder="请输入付款户名"
                class="el-input-width"
                v-model="payInfo.name"
              >
              </el-input>
            </el-form-item>
            <el-form-item label="付款账号：" prop="account">
              <el-input
                :minlength="15"
                :maxlength="19"
                placeholder="请输入付款账号"
                class="el-input-width"
                v-model="payInfo.account"
              >
              </el-input>
            </el-form-item>
            <el-form-item label="付款时间：" prop="paymentTime">
              <el-date-picker
                class="el-input-width"
                value-format="yyyy-MM-dd HH:mm:ss"
                :picker-options="pickerOptions"
                v-model="payInfo.paymentTime"
                type="date"
                placeholder="年/月/日"
              >
              </el-date-picker>
            </el-form-item>
          </div>
        </el-form>
      </div>
    </div>
    <span slot="footer" class="dialog--footer">
      <el-button @click="closeHandle">取 消</el-button>
      <el-button
        type="primary"
        @click="next"
        v-if="!validate && [1, 2].includes(dataForm.payType)"
        >下一步</el-button
      >
      <el-button
        type="primary"
        @click="next"
        v-if="!validate && [3].includes(dataForm.payType)"
        >已完成支付，填写付款信息</el-button
      >
      <el-button
        type="primary"
        @click="confirmClickHandle"
        v-if="validate && dataForm.payType === 3"
        >信息确认</el-button
      >
    </span>
  </el-dialog>
</template>

<script lang="ts">
import { Vue, Component, PropSync, Watch, Ref } from "vue-property-decorator";
import QrCode from "@/views/meal/Component/QrCode.vue";
import { rechargeMoney, confirmRecharge } from "@/api/businessCenter/recharge";
import storage from "@/libs/storage";
import { getAccountInfo } from "@/api/sign";
import { getPayConfig } from "@/api/meal/meal";
import { testBankCard } from "@/libs/validate";
import { ElForm } from "element-ui/types/form";

@Component({
  components: {
    QrCode,
  },
})
export default class RechargeMoney extends Vue {
  @Ref() readonly rechargeRef!: ElForm;

  @Ref() readonly payInfoRef!: ElForm;

  @PropSync("visible", {
    type: Boolean,
    default: false,
  })
  dialogVisible!: boolean;

  /** 充值表单 */
  dataForm = {
    balance: null,
    payInfo: "",
    payType: 2,
  };

  payConfig = {
    alipayAppId: "",
    alipayPrivateKey: "",
    alipayPublicKey: null,
    bankCard: "",
    bankOfDeposit: "",
    name: "",
    wxAppId: "",
    wxMchId: "",
    wxMchKey: "",
  };

  // 微信二维码配置
  wechatOption = {
    codeUrl: "",
    width: 250,
    height: 250,
    orderId: "",
  };

  // 支付宝二维码配置
  alipayOption = {
    codeUrl: "",
    width: 250,
    height: 250,
    orderId: "",
  };

  payInfo = {
    name: "",
    paymentTime: "",
    account: "",
  };

  userInfo = {
    balance: 0,
  };

  pickerOptions = {
    disabledDate: (time: { getTime: () => number }) => {
      return (
        time.getTime() < new Date(new Date().toLocaleDateString()).getTime()
      ); // 返回所有时间小于当前时间的值
    },
  };

  rules = {
    balance: [
      { required: true, validator: this.checkBalance, trigger: "blur" },
    ],
    payType: [
      { required: true, message: "请选择一种支付方式", trigger: "change" },
    ],
  };

  rules2 = {
    name: [{ required: true, message: "请输入付款户名", trigger: "blur" }],
    account: [{ required: true, validator: testBankCard, trigger: "blur" }],
    payTime: [{ required: true, message: "请输入付款时间", trigger: "blur" }],
  };

  // 定时器
  timer = 0;

  validate = false;

  checkBalance(_rule: any, value: number, callback: (arg0?: Error) => void) {
    if (!value) {
      return callback(new Error("充值金额不能为空"));
    }
    if (!Number.isInteger(value)) {
      callback(new Error("请输入数字值"));
    } else {
      if (value < 10) {
        callback(new Error("最低充值金额10.00元"));
      } else {
        callback();
      }
    }
  }

  @Watch("dialogVisible")
  async handleVisibleChange(v: any) {
    if (v) {
      this.init();
      await this.getConfig();
      await this.getRecentAccountInfo();
    } else {
      this.timer && clearTimeout(this.timer);
    }
  }

  init() {
    // 初始化表单
    this.dataForm = {
      balance: null,
      payInfo: "",
      payType: 2,
    };
    this.validate = false;
  }

  async next() {
    try {
      const result = await this.rechargeRef.validate();
      if (!result) {
        return;
      }
      // 微信支付
      if (this.dataForm.payType === 2) {
        await this.wechatPayment();
      }
      // 支付宝支付
      if (this.dataForm.payType === 1) {
        await this.aliPayment();
      }
      // 汇款支付
      if (this.dataForm.payType === 3) {
        await this.offlinePayment();
      }
    } catch (e) {
      console.log(e);
    }
  }

  // 充值成功后处理
  async reload() {
    // 支付宝支付
    if (this.dataForm.payType === 1) {
      this.dialogVisible = false;
      this.$emit("refreshDataList");
    }
    // 微信支付
    if (this.dataForm.payType === 2) {
      this.dialogVisible = false;
      this.$emit("refreshDataList");
    }
    // 汇款支付
    if (this.dataForm.payType === 3) {
      this.dialogVisible = false;
      const res = await this.$confirm(
        "汇款凭证已提交，将在3个工作日内完成审核，通过后自动完成充值！",
        "提示",
        {
          confirmButtonText: "我知道了",
          cancelButtonText: "取消",
          type: "warning",
        },
      );
      if (res === "confirm") {
        this.$emit("refreshDataList");
      }
    }
  }

  // 微信支付
  async wechatPayment() {
    const info = await this.recharge();
    if (info) {
      Object.assign(this.wechatOption, info);
      this.$nextTick(() => {
        this.validate = true;
        this.pollingPayResult(info.orderId);
      });
    }
  }

  // 支付宝支付
  async aliPayment() {
    const info = await this.recharge();
    if (info) {
      Object.assign(this.alipayOption, info);
      this.$nextTick(() => {
        this.validate = true;
        this.pollingPayResult(info.orderId);
      });
    }
  }

  // 汇款支付
  offlinePayment() {
    this.validate = true;
  }

  async confirmClickHandle() {
    try {
      await this.payInfoRef.validate();
      await this.recharge(async () => {
        await this.reload();
      });
    } catch (e) {
      console.log(e);
    }
  }

  // 调用支付
  async recharge(callback?: { (): Promise<void>; (): any }) {
    const loading = this.$loading({
      lock: true,
      text: "Loading",
      spinner: "el-icon-loading",
      background: "rgba(0, 0, 0, 0.7)",
    });
    try {
      this.dataForm.payInfo = JSON.stringify(this.payInfo);
      const response = await rechargeMoney(this.dataForm);
      const { code, data } = response;
      if (code === 200) {
        loading.close();
        callback && callback();
        return data;
      } else {
        loading.close();
        this.$message({
          type: "warning",
          message: "支付失败！",
        });
        return false;
      }
    } catch (err) {
      this.$message({
        type: "warning",
        message: err,
      });
      loading.close();
      return false;
    }
  }

  // 轮询支付结果
  async pollingPayResult(orderId: any) {
    try {
      const response = await confirmRecharge(orderId);
      const { code, data } = response;
      if (code === 200 && data) {
        this.timer && clearTimeout(this.timer);
        await this.reload();
      } else {
        this.timer = setTimeout(() => {
          this.pollingPayResult(orderId);
        }, 3000);
      }
    } catch (e) {
      this.timer && clearTimeout(this.timer);
    }
  }

  // 根据请求token获取当前用户最新的信息
  async getRecentAccountInfo() {
    if (storage.get("token")) {
      const response = await getAccountInfo();
      const { code, data } = response;
      if (code === 200) {
        this.userInfo = data || this.userInfo;
      }
    }
  }

  // 获取支付配置（线下收款）
  async getConfig() {
    try {
      const response = await getPayConfig();
      const { code, data } = response;
      if (code === 200) {
        this.payConfig = data;
      } else {
        this.$message({
          type: "warning",
          message: "获取收款配置失败！",
        });
      }
    } catch (e) {
      console.log(e);
    }
  }

  /**
   * 关闭弹窗
   */
  closeHandle() {
    this.dialogVisible = false;
  }
}
</script>

<style lang="scss" scoped>
.recharge-container {
  padding: 15px 35px;
}

.form-card {
  padding: 35px;
  border: 1px solid #dcdfe6;
}

.qrcode {
  width: 100%;
  height: 400px;
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;

  .msg {
    margin-top: 10px;
    text-align: center;

    span {
      color: #ff6700;
      cursor: pointer;
    }
  }
}
</style>
