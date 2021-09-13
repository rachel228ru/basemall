<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-26 09:23:18
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-27 10:33:05
-->
<template>
  <div class="SignFlow">
    <div id="container"></div>
    <div class="SignFlow__tabs">
      <div class="SignFlow__tabs--title">FREE SIGNUP</div>
      <div class="SignInForm__SignUp" @click="changeSignType">
        <span>已有账号？</span>
        <el-button type="text">马上登录</el-button>
      </div>
    </div>
    <el-form
      :model="queryForm"
      :rules="queryFormRules"
      ref="queryFormRef"
      hide-required-asterisk
      style="margin-top: 30px;"
      label-position="left"
      label-width="85px"
      class="SignInForm"
    >
      <el-form-item
        label="手机号码"
        prop="phone"
        @keyup.enter.native="submitForm"
      >
        <el-input
          v-model="queryForm.phone"
          @focus="handleFocusEvent"
          readonly
          placeholder="请输入手机号"
        ></el-input>
      </el-form-item>
      <el-form-item label="验证码" prop="phoneCode">
        <div class="SignInForm__code">
          <el-input
            v-model="queryForm.phoneCode"
            @focus="handleFocusEvent"
            readonly
            size="small"
            @blur="verifyCode"
            placeholder="请填写短信验证码"
          >
          </el-input>
          <el-button
            type="primary"
            plain
            round
            class="mb8"
            @click="getVertifyCode('queryForm', 1002)"
            :disabled="queryForm.isDisabled"
            size="small"
            >{{ queryForm.buttonName }}
          </el-button>
        </div>
      </el-form-item>
      <el-form-item label="初始密码" prop="password">
        <div class="SignInForm__password">
          <el-input
            :type="inputType ? 'password' : 'text'"
            v-model="queryForm.password"
            @focus="handleFocusEvent"
            readonly
            placeholder="8-16位必须包含数字和字母"
            auto-complete="off"
          />
          <span @click="inputType = !inputType" class="mb8">
            <i class="iconfont iconeye" v-if="inputType"></i>
            <i class="iconfont iconicon_password_eye_on" v-if="!inputType"></i>
          </span>
        </div>
      </el-form-item>
      <el-form-item label="地区" prop="region">
        <div class="SignInForm__region">
          <el-cascader
            size="large"
            placeholder="请选择地区"
            class="SignInForm__region--cascader"
            :options="options"
            v-model="queryForm.region"
            @change="addressChange"
          >
          </el-cascader>
        </div>
      </el-form-item>
      <el-form-item label="住址" prop="address">
        <div class="SignInForm__address">
          <el-input
            v-model="queryForm.address"
            placeholder="请填写详细地址"
            class="mb8"
          />
        </div>
      </el-form-item>
      <div class="SignInForm__option">
        <el-checkbox v-model="queryForm.agree"></el-checkbox>
        <span
          >我已阅读并同意
          <span @click="goProtocol" class="pointer primary"
            >《<u>用户协议</u>》</span
          >
          和
          <span @click="goPrivacy" class="pointer primary"
            >《 <u> 隐私政策</u>》</span
          >
        </span>
      </div>
      <el-button
        class="SignInForm__button"
        type="primary"
        :disabled="submitDisabled"
        @click="submitForm"
        >立即注册</el-button
      >
    </el-form>
  </div>
</template>

<script lang="ts">
import { Vue, Component, Ref, Emit } from "vue-property-decorator";
import {
  getCode,
  registerAccount,
  analysisLatAndLon,
  verifyTheCode,
  checkoutAccount,
} from "@/api/sign";
import { Map } from "@/map";
import { regionData, CodeToText } from "element-china-area-data";
import { login, modifySignStatus } from "@/libs";
import { getShopInfo } from "@/api/businessCenter/setting";
import { ElForm } from "element-ui/types/form";

@Component
export default class SignIn extends Vue {
  @Ref() readonly queryFormRef!: ElForm;

  // 密码输入框类型 true "password" false "text"
  inputType = true;

  options = regionData;

  submitDisabled = false;

  queryForm = {
    address: "",
    certificate: "",
    password: "",
    phoneCode: "",
    phone: "",
    region: [] as string[],
    code: "",
    agree: "",
    buttonName: "获取验证码",
    isDisabled: false,
    time: 60,
  };

  queryFormRules = {
    phone: [{ required: true, validator: this.validPhone, trigger: "blur" }],
    password: [{ required: true, message: "请输入密码", trigger: "blur" }],
    certificate: [{ required: true, message: "请输入验证码", trigger: "blur" }],
    region: [{ required: true, message: "请选择地区", trigger: "blur" }],
    address: [{ required: true, message: "请输入地址", trigger: "blur" }],
  };

  created() {
    if (this.$route.query.code && this.$route.query.type === "SignUpByPhone") {
      this.queryForm.code = this.$route.query.code as string;
    }
    this.baiduMap();
  }

  /** 根据经纬度设置省市区 */
  async setAddressByLatAndLon(site: string) {
    const response = await analysisLatAndLon({ site });
    const { data } = response;
    /** 省市区赋值 */
    this.queryForm.region = [
      data.province.value,
      data.city.value,
      data.district.value,
    ];
  }

  @Emit("change")
  changeSignType() {
    return "SignInByPhone";
  }

  // 手机号校验
  async checkPhoneAccount() {
    if (!this.isvalidPhone(this.queryForm.phone)) {
      this.$message({
        type: "warning",
        message: "请输入正确的手机号",
      });
      return false;
    }
    try {
      await checkoutAccount({
        phone: this.queryForm.phone,
        type: 2,
      });
      return true;
    } catch (e) {
      this.$message({
        type: "warning",
        message: e,
      });
      return false;
    }
  }

  // 获取验证码
  async getVertifyCode(formName: string, type: number) {
    const isValue = await this.checkPhoneAccount();
    if (!isValue) {
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

  async verifyCode() {
    if (!this.queryForm.phoneCode) {
      return;
    }
    const response = await verifyTheCode({
      code: this.queryForm.phoneCode,
      phone: this.queryForm.phone,
      type: 1002,
    });
    const { code, data } = response;
    if (code === 200) {
      this.queryForm.certificate = data;
    } else {
      console.log("e");
    }
  }

  async submitForm() {
    if (!this.queryForm.agree) {
      this.$message({
        type: "warning",
        message: "请阅读并确认同意协议",
      });
      return;
    }
    await this.queryFormRef.validate();
    this.submitDisabled = true;
    try {
      const form = {
        code: this.queryForm.code,
        phone: this.queryForm.phone,
        certificate: this.queryForm.certificate,
        password: this.queryForm.password,
        address: this.queryForm.address,
        region:
          CodeToText[this.queryForm.region[0]] +
          CodeToText[this.queryForm.region[1]] +
          CodeToText[this.queryForm.region[2]],
      };
      const response = await registerAccount(form);
      const { code, data } = response;
      if (code === 200) {
        this.$message({
          type: "success",
          message: "注册成功!",
        });
        modifySignStatus(data.token, data);
        login(data.token, data);
        this.submitDisabled = false;
        // 更新最新的店铺信息
        await getShopInfo();
        this.$router.push("/console");
      }
      setTimeout(() => {
        this.submitDisabled = false;
      }, 10000);
    } catch (e) {
      if (e === "certificate:校验码错误") {
        e = "验证码不正确";
      }
      this.$message({
        type: "warning",
        message: e.replace(/[A-Z|a-z|:|：]/g, ""),
      });
      this.submitDisabled = false;
    }
  }

  baiduMap() {
    // 调用map.js中Map()方法，引入百度地图秘钥作为参数
    Map("hLVK1XQMUtAtmsFa5g78guDRSmHQHirH").then(BMap => {
      const geolocation = new BMap.Geolocation();
      geolocation.getCurrentPosition(
        (position: { longitude: string; latitude: string }) => {
          const site = `${position.longitude}, ${position.latitude}`;
          this.setAddressByLatAndLon(site);
        },
        (e: any) => {
          console.log(e);
        },
        { provider: "baidu" },
      );
    });
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
    const reg = /^1[0-9][0-9]\d{8}$/;
    return reg.test(str);
  }

  handleFocusEvent(event: {
    target: { removeAttribute: (arg0: string) => void };
  }) {
    if (event.target) {
      event.target.removeAttribute("readonly");
    }
  }

  goProtocol() {
    const route = this.$router.resolve({
      name: "protocol",
    });
    window.open(route.href, "_blank");
  }

  goPrivacy() {
    const route = this.$router.resolve({
      name: "privacy",
    });
    window.open(route.href, "_blank");
  }
}
</script>

<style lang="scss" scoped>
.SignInForm__region--cascader {
  display: block;
}

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
  top: 0px;
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
