<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:11
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-27 10:32:45
-->
<template>
  <div class="SignFlow" v-loading="loading">
    <div class="SignFlow__tabs">
      <div class="SignFlow__tabs--title">LOG IN</div>
      <div
        class="SignFlow__tabs--switch"
        @click="changeSignType('SignInByQrCode')"
      >
        <div class="toolTip-right">
          扫码登录
        </div>
        <i class="iconfont iconQR_Code"></i>
      </div>
    </div>
    <el-form
      :model="signForm"
      :rules="signRules"
      ref="signFormRef"
      hide-required-asterisk
      label-position="left"
      label-width="85px"
      class="SignInForm"
    >
      <div class="SignInForm__tabs">
        <div
          @click="switchStatus(1)"
          class="SignInForm__tab"
          :class="{ 'SignInForm__tab--active': signForm.loginType === 1 }"
        >
          密码登录
        </div>
        <div
          @click="switchStatus(2)"
          class="SignInForm__tab"
          :class="{ 'SignInForm__tab--active': signForm.loginType === 2 }"
        >
          验证码登录
        </div>
      </div>
      <el-form-item label="手机号码" prop="phone">
        <el-input
          v-model="signForm.phone"
          @keyup.enter.native="submitForm"
          @focus="handleFocusEvent"
          autocomplete="on"
        >
        </el-input>
      </el-form-item>
      <el-form-item
        label="登录密码"
        prop="password"
        v-if="signForm.loginType === 1"
      >
        <div class="SignInForm__password">
          <el-input
            :type="inputType ? 'password' : 'text'"
            @focus="handleFocusEvent"
            v-model="signForm.password"
            autocomplete="on"
          ></el-input>
          <span @click="inputType = !inputType" class="mb8">
            <i class="iconfont iconeye" v-if="inputType"></i>
            <i class="iconfont iconicon_password_eye_on" v-if="!inputType"></i>
          </span>
        </div>
      </el-form-item>
      <el-form-item
        label="验证码"
        prop="phoneCode"
        v-if="signForm.loginType === 2"
      >
        <div class="SignInForm__code">
          <el-input
            v-model="signForm.phoneCode"
            size="small"
            placeholder="输入验证码"
          >
          </el-input>
          <el-button
            type="primary"
            plain
            round
            class="mb8"
            @click="getVertifyCode('signForm', 1001)"
            :disabled="signForm.isDisabled"
            size="small"
            >{{ signForm.buttonName }}
          </el-button>
        </div>
      </el-form-item>
      <div class="SignInForm__option">
        <el-checkbox v-model="signForm.savePassword">记住密码</el-checkbox>
        <el-button type="text" @click="forgotPassword">忘记密码 ?</el-button>
      </div>
      <el-button class="SignInForm__button" type="primary" @click="submitForm"
        >登录</el-button
      >
      <div class="SignInForm__SignUp" @click="changeSignType('SignUpByQrCode')">
        <span>还没账号？</span>
        <el-button type="text">免费注册</el-button>
      </div>
    </el-form>
  </div>
</template>

<script lang="ts">
import { Vue, Component, Ref, Emit } from "vue-property-decorator";
import { unionAccountLogin, getCode, verifyTheCode } from "@/api/sign";
import { login, modifySignStatus } from "@/libs";
import { getShopInfo } from "@/api/businessCenter/setting";

@Component
export default class SignIn extends Vue {
  @Ref() readonly signFormRef!: HTMLFormElement;

  // 密码输入框类型 true "password" false "text"
  inputType = true;

  signForm = {
    // 登录类型 1-密码登录  2-验证码登录 3-扫码登录
    loginType: 1,
    // 登录账号
    phone: "",
    // 密码
    password: "",
    phoneCode: "",
    // 校验码凭证
    certificate: "",
    code: "",
    buttonName: "获取验证码",
    isDisabled: false,
    time: 60,
    savePassword: false,
  };

  loading = false;

  signRules = {
    phone: [
      {
        required: true,
        validator: this.validPhone,
        trigger: "blur",
      },
    ],
    phoneCode: [
      {
        required: true,
        message: "请输入验证码",
        trigger: "blur",
      },
    ],
    password: [
      {
        required: true,
        message: "请输入登录密码",
        trigger: "blur",
      },
    ],
  };

  created() {
    document.onkeydown = (e: KeyboardEvent) => {
      // 验证在登录界面和按得键是回车键enter
      if (
        this.$route.path.lastIndexOf("login") &&
        (e.code === "Enter" || e.code === "enter")
      ) {
        this.submitForm();
      }
    };
  }

  switchStatus(status: number) {
    this.signForm.loginType = status;
  }

  @Emit("change")
  changeSignType(value: string) {
    return value;
  }

  // 获取验证码
  async getVertifyCode(formName: string, type: number) {
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

  async submitForm() {
    await this.signFormRef.validate();
    this.loading = true;
    try {
      if (this.signForm.loginType === 2) {
        const res = await verifyTheCode({
          code: this.signForm.phoneCode,
          phone: this.signForm.phone,
          type: 1001,
        });
        if (res.code === 200) {
          this.signForm.certificate = res.data.data;
        }
      }
      const form = {
        loginType: this.signForm.loginType,
        certificate: this.signForm.certificate,
        phone: this.signForm.phone,
        password: this.signForm.password,
        code: this.signForm.phoneCode,
      };
      const response = await unionAccountLogin(form);
      const { data, msg, code } = response;
      if (code === 200) {
        modifySignStatus(data.token, data);
        login(data.token, data);
        // 更新最新的店铺信息
        await getShopInfo();
        this.$router.push("/console");
        this.loading = false;
      } else {
        this.$message({
          message: String(msg),
          type: "warning",
          center: true,
        });
        this.loading = false;
      }
    } catch (e) {
      this.$message({
        message: e,
        type: "warning",
        center: true,
      });
      this.loading = false;
    }
  }

  validPhone(_rule: any, value: string, callback: (arg?: Error) => void) {
    if (!value) {
      callback(new Error("请输入手机号码"));
    } else if (!this.isvalidPhone(value)) {
      callback(new Error("请输入正确的11位手机号码"));
    } else {
      callback();
    }
  }

  isvalidPhone(str: string) {
    const reg = /^1[0-9][0-9]\d{8}$/;
    return reg.test(str);
  }

  // 忘记密码
  forgotPassword() {
    this.$router.push({
      name: "changePass",
      query: {
        type: "forget",
      },
    });
  }

  handleFocusEvent(event: {
    target: { removeAttribute: (arg0: string) => void };
  }) {
    if (event.target) {
      event.target.removeAttribute("readonly");
    }
  }
}
</script>

<style lang="scss" scoped>
// 覆盖样式
/deep/ .el-input__inner {
  border-color: transparent;
}

/deep/ .el-form-item {
  border-bottom: 1px solid #f8f5f9;
}

/deep/ .el-checkbox__inner {
  width: 12px;
  height: 12px;
}

/deep/ .el-checkbox__inner::after {
  height: 6px;
  left: 3px;
  top: 0;
}

/deep/ .el-checkbox__label {
  color: #909399;
  padding-left: 6px;
  font-size: 12px;
}

/deep/ .SignInForm__option .el-button--text {
  color: #909399;
}
</style>
