<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-26 18:32:43
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-27 10:32:55
-->
<template>
  <div class="sign">
    <div class="header">
      <span class="header__title">验证码登录</span>
    </div>
    <el-form
      :model="loginForm"
      status-icon
      :rules="loginRules"
      ref="loginFormRef"
      label-width="85px"
      class="sign__loginForm"
      label-position="left"
    >
      <el-form-item label="手机号码" prop="phone">
        <el-input
          v-model="loginForm.phone"
          @blur="checkAccount"
          placeholder="注册时的手机号"
        >
        </el-input>
      </el-form-item>
      <el-form-item label="验证码" prop="code">
        <div class="sign__loginForm--loginCode">
          <el-input
            v-model="loginForm.code"
            size="small"
            placeholder="输入验证码"
          >
          </el-input>
          <el-button
            @click="getVertifyCode('loginForm', 1)"
            :disabled="loginForm.isDisabled"
            size="small"
            type="primary"
            class="ml10"
            >{{ loginForm.buttonName }}
          </el-button>
        </div>
      </el-form-item>
      <div class="sign__loginForm--changeType">
        <el-button type="text" @click="changeLoginType('phone')"
          >账号登录</el-button
        >
        <el-button type="text" @click="changeLoginType('qrcode')"
          >扫码登录</el-button
        >
      </div>
    </el-form>
    <el-button
      class="sign__loginForm--loginButton"
      type="primary"
      @click="submitForm"
      >登录</el-button
    >
  </div>
</template>

<script lang="ts">
import { Vue, Component, Ref, PropSync } from "vue-property-decorator";
import { loginByCode, checkoutAccount, getCode } from "@/api/sign";
import storage from "@/libs/storage";
import { ElForm } from "element-ui/types/form";

@Component
export default class SignIn extends Vue {
  @PropSync("type", {
    type: String,
    required: true,
    default: "code",
  })
  loginType!: string;

  @Ref() readonly loginFormRef!: ElForm;

  // 登陆表单
  loginForm: any = {
    phone: null,
    code: null,
    buttonName: "获取验证码",
    isDisabled: false,
    time: 60,
  };

  loginRules = {
    phone: [{ required: true, validator: this.validPhone, trigger: "blur" }],
    code: [{ required: true, message: "输入验证码", trigger: "blur" }],
  };

  async submitForm() {
    try {
      await this.loginFormRef.validate();
      const form = {
        phone: this.loginForm.phone,
        code: this.loginForm.code,
        type: 1,
      };
      const { data } = await loginByCode(form);
      const time = new Date();
      storage.set("token", data.token);
      storage.set("time", time.getTime());
      this.$STORE.userStore.setToken(data.token);
      this.$STORE.userStore.setUserInfo(data);
      this.$router.push("/console");
    } catch (e) {
      this.$message({
        type: "warning",
        message: e,
      });
      console.log(e);
    }
  }

  checkAccount(event: { target: { value: string } }) {
    if (!this.isvalidPhone(event.target.value)) {
      return;
    }
    checkoutAccount({
      phone: event.target.value,
    })
      .then(res => {
        if (res.code === 200) {
          this.loginForm.isDisabled = false;
        } else {
          this.loginForm.isDisabled = true;
          this.$message({
            type: "warning",
            message: "账号不存在",
          });
        }
      })
      .catch(e => {
        this.$message({
          type: "warning",
          message: e,
        });
        this.loginForm.isDisabled = true;
      });
  }

  changeLoginType(type: string) {
    this.loginType = type;
  }

  // 获取验证码
  getVertifyCode(formName: string, type: number) {
    getCode({
      phone: this[formName].phone,
      type,
    }).then(res => {
      if (res.code === 200) {
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
    });
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

  validPhone(_rule: any, value: string, callback: (arg?: Error) => void) {
    if (!value) {
      callback(new Error("请输入电话号码"));
    } else if (!this.isvalidPhone(value)) {
      callback(new Error("请输入正确的11位手机号码"));
    } else {
      callback();
    }
  }

  isvalidPhone(str: string) {
    const reg = /^1[3|4|5|7|8][0-9]\d{8}$/;
    return reg.test(str);
  }
}
</script>
