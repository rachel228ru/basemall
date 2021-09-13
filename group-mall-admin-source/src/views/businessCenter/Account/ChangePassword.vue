<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:09
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-25 13:23:35
-->
<template>
  <div class="change-password">
    <div class="logo" @click="goBack">
      <img
        class="pointer"
        src="https://medusa-small-file.oss-cn-hangzhou.aliyuncs.com/gruul/20200407/logo.png"
      />
    </div>
    <div class="resetBox">
      <div>
        <div class="middle">
          <i class="el-icon-lock"></i>
          <span v-if="type === 'forget'">忘记密码</span>
          <span v-else>修改密码</span>
        </div>
        <div class="text-info">请输入您的登录手机号，以进行密码重设</div>
        <div class="resetPass" v-if="!validate">
          <el-form :model="phoneForm">
            <el-form-item>
              <el-input
                :maxlength="11"
                v-model="phoneForm.phone"
                placeholder="请输入您的手机号"
                :disabled="true"
              ></el-input>
            </el-form-item>
            <el-form-item>
              <div class="phoneCode">
                <el-input
                  v-model="phoneForm.code"
                  class="phoneCode--item"
                  size="small"
                  placeholder="验证码"
                >
                </el-input>
                <el-button
                  @click="getVertifyCode('phoneForm', 1004)"
                  class="phoneCode--item"
                  :disabled="phoneForm.isDisabled"
                  size="small"
                  type="primary"
                  >{{ phoneForm.buttonName }}
                </el-button>
              </div>
            </el-form-item>
            <el-form-item style="margin-top: 50px;">
              <el-button
                type="primary"
                @click="nextClickHandle"
                style="width: 100%;height: 40px;"
                >下一步
              </el-button>
            </el-form-item>
          </el-form>
        </div>
      </div>
      <div class="resetPass" v-if="validate">
        <el-form :model="passForm" ref="passFormRef">
          <el-form-item label="输入新密码" prop="password">
            <el-input
              type="password"
              v-model="passForm.password"
              placeholder="请输入新密码"
            ></el-input>
          </el-form-item>
          <el-form-item label="再次输入新密码" prop="dbpass">
            <el-input
              type="password"
              v-model="passForm.dbpass"
              placeholder="请输入新密码"
              @blur="checkPassword"
            ></el-input>
          </el-form-item>
          <el-form-item style="margin-top: 50px;">
            <el-button
              type="primary"
              :disabled="resetDisabled"
              @click="changePassword"
              style="width: 100%;height: 40px;"
              >确定
            </el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>
    <div class="text-center copyright">
      Copyright © MEDUSA 2017. All Rights Rrserved.宁波启山科技有限公司 版权所有
    </div>
  </div>
</template>

<script lang="ts">
import { Vue, Component } from "vue-property-decorator";
import {
  changePassword,
  forgetPassword,
  getCode,
  getSystemConfig,
  verifyTheCode,
} from "@/api/sign";
import { logout } from "@/libs";

@Component
export default class ChangePassword extends Vue {
  /** 身份验证状态值 */
  validate = false;

  // 手机表单
  phoneForm = {
    phone: this.$STORE.userStore.iphone,
    code: null,
    certificate: null,
    buttonName: "获取验证码",
    isDisabled: false,
    time: 60,
  };

  // 密码表单
  passForm = {
    password: "",
    dbpass: "",
  };

  type = "";

  created() {
    if (this.$route.query.type && this.$route.query.type === "forget") {
      this.type = "forget";
    } else {
      this.type = "";
    }
    this.validate = false;
    this.phoneForm = {
      phone: this.$STORE.userStore.iphone,
      code: null,
      certificate: null,
      buttonName: "获取验证码",
      isDisabled: false,
      time: 60,
    };
    this.passForm = {
      password: "",
      dbpass: "",
    };
  }

  changePassword() {
    if (this.type === "forget") {
      this.forgetSubmit();
    } else {
      this.restPass();
    }
  }

  // 用户修改密码
  async restPass() {
    try {
      const form = {
        phone: this.phoneForm.phone,
        certificate: this.phoneForm.certificate,
        passwd: this.passForm.dbpass,
      };
      const response = await changePassword(form);
      const { code } = response;
      if (code === 200) {
        this.$message({
          type: "success",
          message: "更改密码成功！",
        });
        await this.goBack();
      } else {
        this.$message({
          type: "warning",
          message: "更改密码失败！",
        });
      }
    } catch (e) {
      console.log(e);
    }
  }

  // 忘记密码表单提交
  async forgetSubmit() {
    try {
      const form = {
        phone: this.phoneForm.phone,
        certificate: this.phoneForm.certificate,
        passwd: this.passForm.dbpass,
      };
      const response = await forgetPassword(form);
      const { code } = response;
      if (code === 200) {
        this.$message({
          type: "success",
          message: "更改密码成功！",
        });
        this.goBack();
      } else {
        this.$message({
          type: "warning",
          message: "更改密码失败！",
        });
      }
    } catch (e) {
      console.log(e);
    }
  }

  // 获取验证码
  async getVertifyCode(formName, type) {
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
        phone: this.phoneForm.phone,
        code: this.phoneForm.code,
        type: 1004,
      };
      const response = await verifyTheCode(form);
      const { code, data } = response;
      if (code === 200) {
        this.phoneForm.certificate = data;
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

  get resetDisabled() {
    debugger;
    let isValidate = false;
    // 忘记密码并且填入新密码
    if (this.type === "forget" && this.passForm.dbpass) {
      isValidate = true;
    }
    // 修改密码
    if (!this.passForm.password || !this.passForm.dbpass) {
      isValidate = true;
    }
    if (this.passForm.password !== this.passForm.dbpass) {
      isValidate = true;
    }
    return isValidate;
  }

  checkPassword() {
    if (this.type === "forget" && !this.passForm.dbpass) {
      this.$message({
        type: "warning",
        message: "请输入新密码",
      });
      return;
    }
    if (this.passForm.password !== this.passForm.dbpass) {
      this.$message({
        type: "warning",
        message: "请输入相同的密码",
      });
    }
  }

  async goBack() {
    logout();
    try {
      const response = await getSystemConfig();
      const { code, data } = response;
      if (code === 200 && data.systemConfig && data.systemConfig.consoleUrl) {
        open(`${data.systemConfig.consoleUrl}`, "_top");
      }
    } catch (e) {
      this.$message({
        type: "warning",
        message: e,
      });
    }
  }
}
</script>

<style lang="scss" scoped>
.change-password {
  background-color: #eef1f6;
  color: #515a6e;
}

.resetBox {
  width: 80%;
  height: 445px;
  margin: 0 auto;
  background-color: #fff;
  padding: 50px 40px 0;

  .middle {
    font-size: 28px;
    padding-bottom: 15px;
    border-bottom: 1px solid #ededed;

    i {
      color: #409eff;
    }

    span {
      color: #303030;
      padding: 0 15px;
    }
  }

  .text-info {
    padding-top: 15px;
    font-size: 14px;
    color: #606266;
  }
}

.logo {
  width: 184px;
  margin: 0 auto 40px;
  padding-top: 128px;

  .pointer {
    width: 100%;
    height: auto;
  }
}

.resetPass {
  width: 420px;
  margin: 0 auto;

  .phoneCode {
    display: flex;
    justify-content: space-between;

    .phoneCode--item {
      flex: 0 0 49%;
    }
  }
}

.copyright {
  padding: 200px 0 100px;
  text-align: center;
}
</style>
