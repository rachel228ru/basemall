<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:09
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-27 11:11:25
-->
<template>
  <el-dialog
    :visible.sync="dialogVisible"
    class="dialog"
    :width="validate ? '400px' : '600px'"
    title="换绑手机号"
  >
    <div class="verify-phone" v-if="!validate">
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
              @click="getVertifyCode('queryForm', 1003)"
              :disabled="queryForm.isDisabled"
              size="small"
              type="primary"
              >{{ queryForm.buttonName }}
            </el-button>
          </div>
        </el-form-item>
      </el-form>
    </div>
    <div class="change-phone" v-if="validate">
      <div style="margin-bottom: 20px;">
        <el-input
          v-model="phoneForm.phone"
          size="small"
          @blur="checkPhoneAccount('phoneForm')"
          placeholder="请输入手机号"
        >
        </el-input>
      </div>
      <div class="phoneCode">
        <el-input
          v-model="phoneForm.code"
          size="small"
          placeholder="请输入验证码"
        >
        </el-input>
        <el-button
          @click="getVertifyCode('phoneForm', 1003)"
          :disabled="phoneForm.isDisabled"
          size="small"
          type="primary"
          >{{ phoneForm.buttonName }}
        </el-button>
      </div>
    </div>
    <span slot="footer" class="dialog--footer">
      <el-button @click="closeHandle">取 消</el-button>
      <el-button type="primary" @click="nextClickHandle" v-if="!validate"
        >下一步</el-button
      >
      <el-button type="primary" @click="comfirmClickHandle" v-if="validate"
        >确定</el-button
      >
    </span>
  </el-dialog>
</template>

<script lang="ts">
import {
  Vue,
  Component,
  Prop,
  PropSync,
  Watch,
  Emit,
} from "vue-property-decorator";
import {
  changePhone,
  checkoutAccount,
  getCode,
  verifyTheCode,
} from "@/api/sign";

@Component
export default class ChangePhone extends Vue {
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

  /** 身份验证表单 */
  queryForm = {
    phone: null,
    code: null,
    oneCertificate: null,
    buttonName: "获取验证码",
    isDisabled: false,
    time: 60,
  };

  /** 手机换绑表单 */
  phoneForm = {
    phone: null,
    code: null,
    twoCertificate: null,
    buttonName: "获取验证码",
    isDisabled: false,
    time: 60,
  };

  /** 身份验证状态值 */
  validate = false;

  @Watch("dialogVisible")
  handleVisibleChange(v: boolean) {
    if (v) {
      this.initStatus();
      this.queryForm.phone = this.userInfo.phone;
    }
  }

  initStatus() {
    this.queryForm = {
      phone: null,
      code: null,
      oneCertificate: null,
      buttonName: "获取验证码",
      isDisabled: false,
      time: 60,
    };
    this.phoneForm = {
      phone: null,
      code: null,
      twoCertificate: null,
      buttonName: "获取验证码",
      isDisabled: false,
      time: 60,
    };
    this.validate = false;
  }

  // 获取验证码
  async getVertifyCode(formName: string, type: any) {
    const status = await this.checkPhoneAccount(formName, false);
    if (!status) {
      return;
    }
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

  async nextClickHandle() {
    try {
      const form = {
        phone: this.queryForm.phone,
        code: this.queryForm.code,
        type: 1003,
      };
      const response = await verifyTheCode(form);
      const { code, data } = response;
      if (code === 200) {
        this.queryForm.oneCertificate = data;
        this.validate = true;
      }
    } catch (e) {
      this.$message({
        type: "warning",
        message: e,
      });
    }
  }

  async checkPhoneAccount(formName: string, showMessage = true) {
    if (!this.isvalidPhone(this[formName].phone)) {
      this.$message({
        type: "warning",
        message: "请输入正确的手机号",
      });
      return false;
    }
    try {
      const res = await checkoutAccount({
        phone: this[formName].phone,
        // 账号存在 500 账号不存在 200
        type: 2,
      });
      if (res.code === 200) {
        if (formName === "queryForm") {
          this[formName].isDisabled = true;
          return false;
        }
        if (formName === "phoneForm") {
          this[formName].isDisabled = false;
          return true;
        }
      }
    } catch (e) {
      if (showMessage) {
        this.$message({
          type: "warning",
          message: e,
        });
      }
      if (formName === "queryForm") {
        this[formName].isDisabled = false;
        return true;
      }
      if (formName === "phoneForm") {
        this[formName].isDisabled = true;
        return false;
      }
    }
  }

  @Emit("refreshDataList")
  async comfirmClickHandle() {
    try {
      const phoneForm = {
        phone: this.phoneForm.phone,
        code: this.phoneForm.code,
        type: 1003,
      };
      const phoneResult = await verifyTheCode(phoneForm);
      const { code, data } = phoneResult;
      if (code === 200) {
        this.phoneForm.twoCertificate = data;
      }
      const form = {
        oldPhone: this.queryForm.phone,
        oneCertificate: this.queryForm.oneCertificate,
        newPhone: this.phoneForm.phone,
        twoCertificate: this.phoneForm.twoCertificate,
      };
      const res = await changePhone(form);
      if (res.code === 200) {
        this.$message({
          type: "success",
          message: "换绑手机成功！",
        });
        this.dialogVisible = false;
      }
    } catch (e) {
      this.$message({
        type: "warning",
        message: e,
      });
    }
  }

  isvalidPhone(str: string) {
    const reg = /^1[3|4|5|7|8][0-9]\d{8}$/;
    return reg.test(str);
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

.phoneCode {
  display: flex;

  .el-button {
    margin-left: 15px;
  }
}
</style>
