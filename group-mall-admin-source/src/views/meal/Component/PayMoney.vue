<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:10
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-27 11:18:19
-->
<template>
  <el-dialog :visible.sync="dialogVisible" width="700px" title="套餐">
    <div class="payment-container" v-if="!validate">
      <el-form
        :model="dataForm"
        class="dataForm"
        label-width="100px"
        label-position="left"
      >
        <el-form-item label="登录账号">
          <span>{{ userInfo.phone }}</span>
        </el-form-item>
        <el-form-item label="套餐名称">
          <span>{{ shopInfo.packageName || meal.name }}</span>
        </el-form-item>
        <el-form-item label="支付金额">
          <span>{{ price }}元</span>
        </el-form-item>
        <el-form-item label="支付方式">
          <el-radio-group v-model="dataForm.payType">
            <el-radio :label="1">余额支付</el-radio>
            <el-radio :label="2">微信支付</el-radio>
            <el-radio :label="3">支付宝支付</el-radio>
            <el-radio :label="4">汇款支付</el-radio>
          </el-radio-group>
          <div>
            <span v-if="dataForm.payType === 4">
              （请勿使用支付宝汇款支付，支付宝请选择扫码支付）
            </span>
            <span
              v-if="
                dataForm.payType === 1 &&
                  Number(price) > Number(userInfo.balance)
              "
              >（剩余 {{ userInfo.balance || 0 }}元，余额不足请先充值）
            </span>
          </div>
        </el-form-item>
        <div class="remittance" v-if="dataForm.payType === 4">
          <div class="remittance__info">
            <p>
              你需汇款
              {{ price }} 元至以下账户，汇款成功后上传凭证信息，审核通过后到账
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
      <div class="alipay-code qrcode" v-if="dataForm.payType === 3">
        <QrCode id="alipay-code" :option="alipayOption" />
        <div class="msg">
          请使用 <span>支付宝</span> 扫一扫 <br />
          二维码完成支付
        </div>
      </div>
      <div class="remittance" v-if="dataForm.payType === 4">
        <el-form
          :model="payInfo"
          class="payInfo"
          :rules="rules"
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
        v-if="!validate && dataForm.payType === 1"
        >确认支付</el-button
      >
      <el-button
        type="primary"
        @click="next"
        v-if="!validate && [2, 3].includes(dataForm.payType)"
        >下一步</el-button
      >
      <el-button
        type="primary"
        @click="next"
        v-if="!validate && [4].includes(dataForm.payType)"
        >已完成支付，填写付款信息</el-button
      >
      <el-button
        type="primary"
        @click="confirmClickHandle"
        v-if="validate && dataForm.payType === 4"
        >信息确认</el-button
      >
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
import QrCode from "@/views/meal/Component/QrCode.vue";
import { buyMeal, checkPayStatus, getPayConfig } from "@/api/meal/meal";
import storage from "@/libs/storage";
import { getAccountInfo } from "@/api/sign";
import { testBankCard } from "@/libs/validate";
import { ShopInfoType } from "@/store/modulesType/userType";
import { mealList } from "../Order/mealOrderType";

@Component({
  components: {
    QrCode,
  },
})
export default class PayMoney extends Vue {
  @Ref() readonly payInfoRef!: HTMLFormElement;

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
  form!: any;

  @Prop({
    type: Object,
    default() {
      return {};
    },
  })
  meal!: mealList;

  @Prop({
    type: Object,
    default() {
      return {};
    },
  })
  shopInfo!: ShopInfoType;

  @Prop({
    type: String,
    default: 0,
  })
  price!: string;

  /** 套餐表单 */
  dataForm = {
    agreeProtocol: 0,
    autoDeduct: 0,
    buyPeriod: 0,
    optionType: 0,
    packageId: 0,
    payInfo: "",
    payType: 1,
    shopId: 0,
  };

  payInfo = {
    name: "",
    paymentTime: "",
    account: "",
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

  userInfo = {
    balance: null,
    phone: null,
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

  pickerOptions = {
    disabledDate: (time: Date) => {
      return (
        time.getTime() < new Date(new Date().toLocaleDateString()).getTime()
      ); // 返回所有时间小于当前时间的值
    },
  };

  // 定时器
  timer: number | null = null;

  validate = false;

  rules = {
    name: [{ required: true, message: "请输入付款户名", trigger: "blur" }],
    account: [{ required: true, validator: testBankCard, trigger: "blur" }],
    paymentTime: [
      { required: true, message: "请输入付款时间", trigger: "blur" },
    ],
  };

  @Watch("dialogVisible")
  async handleVisibleChange(v: boolean) {
    if (v) {
      this.init();
      await this.getConfig();
      await this.getRecentAccountInfo();
      // 继续填充表单
      Object.assign(this.dataForm, this.form);
    } else {
      this.timer && clearTimeout(this.timer);
      this.$emit("refreshDataList", 1);
    }
  }

  // 初始化数据
  init() {
    // 初始化表单
    this.dataForm = {
      agreeProtocol: 0,
      autoDeduct: 0,
      buyPeriod: 0,
      optionType: 0,
      packageId: 0,
      payInfo: "",
      payType: 1,
      shopId: 0,
    };
    // 初始化线下付款数据
    this.payInfo = {
      name: "",
      paymentTime: "",
      account: "",
    };
    this.validate = false;
  }

  // 调用支付
  async buy(callback?: any) {
    const loading = this.$loading({
      lock: true,
      text: "Loading",
      spinner: "el-icon-loading",
      background: "rgba(0, 0, 0, 0.7)",
    });
    try {
      this.dataForm.shopId = this.shopInfo.platformShopId;
      this.dataForm.payInfo = JSON.stringify(this.payInfo);
      const response = await buyMeal(this.dataForm);
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
      loading.close();
      return false;
    }
  }

  async next() {
    // 余额支付
    if (this.dataForm.payType === 1) {
      await this.balancePayment();
    }
    // 微信支付
    if (this.dataForm.payType === 2) {
      await this.wechatPayment();
    }
    // 支付宝支付
    if (this.dataForm.payType === 3) {
      await this.aliPayment();
    }
    // 汇款支付
    if (this.dataForm.payType === 4) {
      await this.offlinePayment();
    }
  }

  // 支付成功后处理
  async reload() {
    // 余额支付
    if (this.dataForm.payType === 1) {
      this.goBusiness();
    }
    // 微信支付
    if (this.dataForm.payType === 2) {
      this.goBusiness();
    }
    // 支付宝支付
    if (this.dataForm.payType === 3) {
      this.goBusiness();
    }
    // 汇款支付
    if (this.dataForm.payType === 4) {
      this.dialogVisible = false;
      const res = await this.$confirm(
        "汇款凭证已提交，将在3个工作日内完成审核，通过后自动完成支付!",
        "提示",
        {
          confirmButtonText: "我知道了",
          cancelButtonText: "取消",
          type: "warning",
        },
      );
      if (res === "confirm") {
        this.goBusiness();
      }
    }
  }

  // 余额支付
  async balancePayment() {
    if (Number(this.price) <= Number(this.userInfo.balance)) {
      await this.buy(() => {
        this.reload();
      });
    } else {
      this.$message({
        type: "warning",
        message: "余额不足请先充值！",
      });
    }
  }

  // 微信支付
  async wechatPayment() {
    const info = await this.buy();
    if (info) {
      Object.assign(this.wechatOption, info);
      this.$nextTick(() => {
        this.validate = true;
        this.pollingPayResult(info.orderId);
      });
    }
  }

  // 轮询支付结果
  async pollingPayResult(orderId: number) {
    try {
      const response = await checkPayStatus(orderId);
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
      this.$message({
        type: "warning",
        message: "支付失败",
      });
      this.timer && clearTimeout(this.timer);
    }
  }

  // 支付宝支付
  async aliPayment() {
    const info = await this.buy();
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
      await this.buy(async () => {
        await this.reload();
      });
    } catch (e) {
      console.log(e);
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

  // 进入套餐订购列表页面
  goBusiness() {
    this.$router.push({
      path: "/business",
      query: {
        tabName: "Order",
        status: "success",
      },
    });
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
/deep/ .remittance {
  font-weight: 400;
  font-style: normal;
  font-size: 13px;
  letter-spacing: normal;
  line-height: normal;
  text-transform: none;

  &__info {
    background: rgba(246, 248, 250, 1);
    color: #aeb9ca;
    padding: 15px;
    margin-bottom: 50px;
  }

  p:not(:last-child) {
    margin-bottom: 15px;
  }
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

.el-input-width {
  width: 320px;
}
</style>
