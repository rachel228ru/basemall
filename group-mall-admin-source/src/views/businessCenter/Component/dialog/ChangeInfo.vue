<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:09
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-27 11:10:51
-->
<template>
  <el-dialog
    :visible.sync="dialogVisible"
    class="dialog"
    width="600px"
    title="换绑微信"
  >
    <div class="verify-account" v-if="!validate">
      <el-tag class="tag"
        ><i class="el-icon-info" />
        为了您的账号安全，进行敏感操作前须先验证身份。</el-tag
      >
      <el-form
        :model="queryForm"
        class="queryForm"
        label-width="120px"
        label-position="left"
      >
        <el-form-item label="验证方式">
          手机验证
        </el-form-item>
        <el-form-item label="绑定手机">
          {{ queryForm.phone }}
        </el-form-item>
        <el-form-item label="验证码">
          <div class="phoneCode">
            <el-input
              v-model="queryForm.code"
              size="small"
              style="width: 120px;margin-right: 15px;"
              placeholder="6位数字验证码"
            >
            </el-input>
            <el-button
              @click="getVertifyCode('queryForm', 1005)"
              :disabled="queryForm.isDisabled"
              size="small"
              type="primary"
              >{{ queryForm.buttonName }}
            </el-button>
          </div>
        </el-form-item>
      </el-form>
    </div>
    <div class="scan-code" v-if="validate">
      <ScanQrCode redirectUrl="/business" scenes="AccountSwitching" />
    </div>
    <span slot="footer" class="dialog--footer" v-if="!validate">
      <el-button @click="closeHandle">取 消</el-button>
      <el-button type="primary" @click="comfirmClickHandle">下一步</el-button>
    </span>
  </el-dialog>
</template>

<script lang="ts">
import { Vue, Component, Prop, PropSync, Watch } from "vue-property-decorator";
import { getCode, verifyTheCode } from "@/api/sign";
import ScanQrCode from "@/components/ScanQrCode.vue";

@Component({
  components: {
    ScanQrCode,
  },
})
export default class GiveCoupons extends Vue {
  @PropSync("visible", {
    type: Boolean,
    default: false,
  })
  dialogVisible!: boolean;

  @Prop({
    type: Object,
    default: () => {
      return {};
    },
  })
  userInfo!: any;

  queryForm = {
    phone: null,
    code: null,
    buttonName: "获取验证码",
    isDisabled: false,
    time: 60,
  };

  validate = false;

  @Watch("dialogVisible")
  handleVisibleChange(v: any) {
    if (v) {
      this.queryForm.phone = this.userInfo.phone;
    }
  }

  // 获取验证码
  async getVertifyCode(formName: string | number, type: any) {
    const response = await getCode({
      phone: this[formName].phone,
      type,
    });
    const { code } = response;
    if (code === 200) {
      this.$message({
        type: "success",
        message: "验证码已发送",
      });
    } else {
      this.$message({
        message: "获取失败,请重新获取",
        type: "warning",
        center: true,
      });
    }
    this[formName].isDisabled = true;
    const interval = window.setInterval(() => {
      this[formName].buttonName = `${this[formName].time}秒后重新发送`;
      --this[formName].time;
      if (this[formName].time < 0) {
        this[formName].buttonName = "重新发送";
        this[formName].time = 60;
        this[formName].isDisabled = false;
        window.clearInterval(interval);
      }
    }, 1000);
  }

  async comfirmClickHandle() {
    try {
      const response = await verifyTheCode({
        phone: this.queryForm.phone,
        code: this.queryForm.code,
        type: 1005,
      });
      const { code } = response;
      if (code === 200) {
        this.validate = true;
      } else {
        this.$message({
          type: "warning",
          message: "请重新验证信息！",
        });
      }
    } catch (e) {
      this.$message({
        type: "warning",
        message: e,
      });
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

<style scoped lang="scss">
.el-tag {
  display: block;
  padding: 15px;
  height: auto;
  margin-bottom: 20px;
}
</style>
