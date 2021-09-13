<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:11
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-25 14:37:19
-->
<template>
  <div class="auth payment">
    <div class="tip">
      <div class="tip__lump"></div>
      <span class="tip__title">支付通道设置</span>
    </div>
    <el-form
      ref="configRef"
      :rules="rules"
      :model="paymentConfig"
      label-position="left"
      label-width="180px"
    >
      <el-form-item label="支付方式" prop="paymentModel">
        <el-row :gutter="15">
          <el-col :span="12" v-if="!editting">
            <td v-if="paymentConfig.payType === 1">微信支付</td>
            <td v-if="paymentConfig.payType === 2">环迅支付</td>
            <td v-if="paymentConfig.payType === 3">随行付</td>
            <td v-if="paymentConfig.payType === 4">盛付通</td>
          </el-col>
          <el-col :span="20" v-if="editting">
            <el-radio-group v-model="paymentConfig.payType">
              <el-radio :label="1">微信支付(6‰费率)</el-radio>
              <el-radio :label="4">盛付通(3.8‰费率)</el-radio>
              <!--              <el-radio :label="2">环迅支付(3.8‰费率)</el-radio>-->
              <el-radio :label="3">随行付(3.8‰费率)</el-radio>
            </el-radio-group>
          </el-col>
        </el-row>
      </el-form-item>
      <el-form-item label="微信商户号" prop="mchId">
        <el-row :gutter="15">
          <el-col :span="12" v-if="!editting">
            {{ paymentConfig.mchId }}
          </el-col>
          <el-col :span="12" v-if="editting">
            <el-input
              v-model="paymentConfig.mchId"
              placeholder="请输入商户号"
            ></el-input>
          </el-col>
        </el-row>
      </el-form-item>
      <el-form-item label="微信商户秘钥" prop="mchKey">
        <el-row :gutter="15">
          <el-col :span="12" v-if="!editting">
            {{ paymentConfig.mchKey }}
          </el-col>
          <el-col :span="12" v-if="editting">
            <el-input
              v-model="paymentConfig.mchKey"
              placeholder="请输入商户秘钥"
            ></el-input>
          </el-col>
        </el-row>
      </el-form-item>
      <el-form-item
        label="微信支付证书"
        v-if="paymentConfig.payType === 1"
        prop="certificatesState"
      >
        <el-row :gutter="15">
          <el-col :span="12">
            <el-upload
              action=""
              ref="uploadFile"
              :http-request="uploadFile"
              :auto-upload="true"
              :show-file-list="false"
            >
              <el-button
                type="text"
                style="color: #67C23A;"
                size="small"
                v-if="paymentConfig.certificatesState"
              >
                已上传
              </el-button>
              <el-button
                type="text"
                size="small"
                v-if="!paymentConfig.certificatesState"
                >未上传
              </el-button>
              <el-button type="primary" v-if="editting">上传</el-button>
              <div slot="tip" class="el-upload__tip">
                微信商户平台，微信支付API证书
              </div>
            </el-upload>
          </el-col>
        </el-row>
      </el-form-item>
      <template v-if="paymentConfig.payType === 2">
        <el-form-item label="环迅商户账户编号" prop="ipsAccCode">
          <el-row :gutter="15">
            <el-col :span="12" v-if="!editting">
              {{ paymentConfig.ipsAccCode }}
            </el-col>
            <el-col :span="12" v-if="editting">
              <el-input
                v-model="paymentConfig.ipsAccCode"
                placeholder="请输入环迅商户账户编号"
              ></el-input>
            </el-col>
          </el-row>
        </el-form-item>
        <el-form-item label="环迅AES秘钥" prop="ipsAes">
          <el-row :gutter="15">
            <el-col :span="12" v-if="!editting">
              {{ paymentConfig.ipsAes }}
            </el-col>
            <el-col :span="12" v-if="editting">
              <el-input
                v-model="paymentConfig.ipsAes"
                placeholder="请输入环迅AES秘钥"
              ></el-input>
            </el-col>
          </el-row>
        </el-form-item>
        <el-form-item label="环迅证书密码" prop="ipsCertificatePsw">
          <el-row :gutter="15">
            <el-col :span="12" v-if="!editting">
              {{ paymentConfig.ipsCertificatePsw }}
            </el-col>
            <el-col :span="12" v-if="editting">
              <el-input
                v-model="paymentConfig.ipsCertificatePsw"
                placeholder="请输入环迅证书密码"
              ></el-input>
            </el-col>
          </el-row>
        </el-form-item>
        <el-form-item label="环迅商户号" prop="ipsMerCode">
          <el-row :gutter="15">
            <el-col :span="12" v-if="!editting">
              {{ paymentConfig.ipsMerCode }}
            </el-col>
            <el-col :span="12" v-if="editting">
              <el-input
                v-model="paymentConfig.ipsMerCode"
                placeholder="请输入环迅商户号"
              ></el-input>
            </el-col>
          </el-row>
        </el-form-item>
        <el-form-item label="环迅私钥" prop="ipsRsaPrivateKey">
          <el-row :gutter="15">
            <el-col :span="12" v-if="!editting">
              {{ paymentConfig.ipsRsaPrivateKey }}
            </el-col>
            <el-col :span="12" v-if="editting">
              <el-input
                v-model="paymentConfig.ipsRsaPrivateKey"
                placeholder="请输入环迅私钥"
              ></el-input>
            </el-col>
          </el-row>
        </el-form-item>
        <el-form-item label="环迅公钥" prop="ipsRsaPublicKey">
          <el-row :gutter="15">
            <el-col :span="12" v-if="!editting">
              {{ paymentConfig.ipsRsaPublicKey }}
            </el-col>
            <el-col :span="12" v-if="editting">
              <el-input
                v-model="paymentConfig.ipsRsaPublicKey"
                placeholder="请输入环迅公钥"
              ></el-input>
            </el-col>
          </el-row>
        </el-form-item>
        <el-form-item label="环迅SHA公钥" prop="ipsSha">
          <el-row :gutter="15">
            <el-col :span="12" v-if="!editting">
              {{ paymentConfig.ipsSha }}
            </el-col>
            <el-col :span="12" v-if="editting">
              <el-input
                v-model="paymentConfig.ipsSha"
                placeholder="请输入环迅SHA公钥"
              ></el-input>
            </el-col>
          </el-row>
        </el-form-item>
      </template>

      <template v-if="paymentConfig.payType === 3">
        <el-form-item label="随行付商户号入驻商户编号" prop="sxfAccCode">
          <el-row :gutter="15">
            <el-col :span="12" v-if="!editting">
              {{ paymentConfig.sxfAccCode }}
            </el-col>
            <el-col :span="12" v-if="editting">
              <el-input
                v-model="paymentConfig.sxfAccCode"
                placeholder="请输入随行付商户号入驻商户编号"
              ></el-input>
            </el-col>
          </el-row>
        </el-form-item>
        <el-form-item label="随行付商户号证书密码" prop="sxfCertificatePsw">
          <el-row :gutter="15">
            <el-col :span="12" v-if="!editting">
              {{ paymentConfig.sxfCertificatePsw }}
            </el-col>
            <el-col :span="12" v-if="editting">
              <el-input
                v-model="paymentConfig.sxfCertificatePsw"
                placeholder="请输入随行付商户号证书密码"
              ></el-input>
            </el-col>
          </el-row>
        </el-form-item>
        <el-form-item label="随行付合作机构id" prop="sxfOrgId">
          <el-row :gutter="15">
            <el-col :span="12" v-if="!editting">
              {{ paymentConfig.sxfOrgId }}
            </el-col>
            <el-col :span="12" v-if="editting">
              <el-input
                v-model="paymentConfig.sxfOrgId"
                placeholder="请输入随行付合作机构id"
              ></el-input>
            </el-col>
          </el-row>
        </el-form-item>
        <el-form-item label="随行付商户号秘钥" prop="sxfPrivateKey">
          <el-row :gutter="15">
            <el-col :span="12" v-if="!editting">
              {{ paymentConfig.sxfPrivateKey }}
            </el-col>
            <el-col :span="12" v-if="editting">
              <el-input
                v-model="paymentConfig.sxfPrivateKey"
                placeholder="请输入随行付商户号秘钥"
              ></el-input>
            </el-col>
          </el-row>
        </el-form-item>
        <el-form-item label="随行付商户号公钥" prop="sxfPublic">
          <el-row :gutter="15">
            <el-col :span="12" v-if="!editting">
              {{ paymentConfig.sxfPublic }}
            </el-col>
            <el-col :span="12" v-if="editting">
              <el-input
                v-model="paymentConfig.sxfPublic"
                placeholder="请输入随行付商户号公钥"
              ></el-input>
            </el-col>
          </el-row>
        </el-form-item>
      </template>

      <template v-if="paymentConfig.payType === 4">
        <el-form-item label="盛付通终端号" prop="sftTerminalId">
          <el-row :gutter="15">
            <el-col :span="12" v-if="!editting">
              {{ paymentConfig.sftTerminalId }}
            </el-col>
            <el-col :span="12" v-if="editting">
              <el-input
                v-model="paymentConfig.sftTerminalId"
                placeholder="请输入盛付通终端号"
              ></el-input>
            </el-col>
          </el-row>
        </el-form-item>
        <el-form-item label="盛付通商户号" prop="sftSubMerchantNo">
          <el-row :gutter="15">
            <el-col :span="12" v-if="!editting">
              {{ paymentConfig.sftSubMerchantNo }}
            </el-col>
            <el-col :span="12" v-if="editting">
              <el-input
                v-model="paymentConfig.sftSubMerchantNo"
                placeholder="请输入盛付通线下交易子商户号"
              ></el-input>
            </el-col>
          </el-row>
        </el-form-item>
      </template>

      <el-form-item>
        <el-row :gutter="15">
          <el-col :span="12">
            <div class="auth__buttons">
              <el-button type="primary" @click="editHandle" v-if="!editting"
                >编辑
              </el-button>
              <el-button @click="editting = false" v-if="editting"
                >返回
              </el-button>
              <el-button type="primary" v-if="editting" @click="submit"
                >保存
              </el-button>
            </div>
          </el-col>
        </el-row>
      </el-form-item>
    </el-form>
    <IdentityVerification
      :visible.sync="identityVisible"
      :shopInfoId="shopId"
    />
  </div>
</template>

<script lang="ts">
import { Vue, Component, Ref, Watch } from "vue-property-decorator";
import {
  getPaymentConfig,
  updatePaymentConfig,
  uploadCertificate,
} from "@/api/businessCenter/setting";
import IdentityVerification from "@/views/setting/common/IdentityVerification.vue";
import { verifyStateResult } from "@/api/sign";
import storage from "@/libs/storage";
import { ElForm } from "element-ui/types/form";
import { HttpRequestOptions } from "element-ui/types/upload";

@Component({
  components: {
    IdentityVerification,
  },
})
export default class Payment extends Vue {
  @Ref() readonly configRef?: ElForm;

  @Watch("editting")
  handleVisibleChange(v: boolean) {
    if (v && this.code) {
      this.getConfig(2, this.code);
    } else {
      this.getConfig(1);
      this.code = "";
    }
  }

  // 更新小程序信息表单
  paymentConfig = {
    certificatesPath: "",
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
    // 盛付通终端号
    sftTerminalId: "",
    // 盛付通秘钥
    sftMd5: "",
    // 盛付通代理商商户编号
    sftChannelID: "30318953",
    // 盛付通线下交易子商户号
    sftSubMerchantNo: "",
  };

  rules = {
    payType: [
      { required: true, message: "请选择一种支付方式", trigger: "change" },
    ],
    ipsAccCode: [
      { required: true, message: "环迅商户账户编号不能为空", trigger: "blur" },
    ],
    ipsAes: [
      { required: true, message: "环迅AES秘钥不能为空", trigger: "blur" },
    ],
    ipsCertificatePsw: [
      { required: true, message: "环迅证书密码不能为空", trigger: "blur" },
    ],
    ipsMerCode: [
      { required: true, message: "环迅商户号不能为空", trigger: "blur" },
    ],
    ipsRsaPrivateKey: [
      { required: true, message: "环迅私钥不能为空", trigger: "blur" },
    ],
    ipsRsaPublicKey: [
      { required: true, message: "环迅公钥不能为空", trigger: "blur" },
    ],
    ipsSha: [
      { required: true, message: "环迅SHA公钥不能为空", trigger: "blur" },
    ],
    sxfAccCode: [
      {
        required: true,
        message: "随行付商户号入驻商户编号不能为空",
        trigger: "blur",
      },
    ],
    sxfCertificatePsw: [
      {
        required: true,
        message: "随行付商户号证书密码不能为空",
        trigger: "blur",
      },
    ],
    sxfOrgId: [
      { required: true, message: "随行付合作机构id不能为空", trigger: "blur" },
    ],
    sxfPrivateKey: [
      { required: true, message: "随行付商户号秘钥不能为空", trigger: "blur" },
    ],
    sxfPublic: [
      { required: true, message: "随行付商户号公钥不能为空", trigger: "blur" },
    ],
    mchId: [{ required: true, message: "商户号不可为空", trigger: "change" }],
    mchKey: [
      { required: true, message: "支付秘钥不可为空", trigger: "change" },
    ],
    sftTerminalId: [
      { required: true, message: "盛付通终端号不可为空", trigger: "change" },
    ],
    sftSubMerchantNo: [
      {
        required: true,
        message: "盛付通商户号不可为空",
        trigger: "change",
      },
    ],
  };

  editting = false;

  code = "";

  shopId = "";

  identityVisible = false;

  async created() {
    try {
      if (this.$route.query.code) {
        this.code = this.$route.query.code as string;
        // 扫码时传过来的shopInfoId
        const shopInfoId = this.$route.query.shopInfoId as string;
        const userInfo = this.$STORE.userStore.userInfo;
        const isValid =
          userInfo && userInfo.shopInfoVo && userInfo.shopInfoVo.platformShopId;
        if (isValid && userInfo.shopInfoVo.platformShopId === shopInfoId) {
          history.pushState(
            {},
            "",
            `${location.href.replace(/&?code=.*&?$/, "")}`,
          );
          const response = await verifyStateResult({
            code: this.code,
          });
          const { code } = response;
          this.editting = code === 200;
        } else {
          this.editting = false;
          await this.getConfig(1);
        }
      } else {
        this.editting = false;
        await this.getConfig(1);
      }
    } catch (e) {
      this.$message({
        message: e,
        type: "warning",
      });
      this.editting = false;
      await this.getConfig(1);
    }
  }

  async getConfig(type: number, cd?: string) {
    try {
      const response = await getPaymentConfig({
        code: cd || "",
        type,
      });
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

  /**
   * 上传支付证书
   */
  async uploadFile(file: HttpRequestOptions) {
    try {
      const response = await uploadCertificate({
        file: file.file,
      });
      const { code, data } = response;
      if (code === 200) {
        this.$message({
          message: "上传成功",
          type: "success",
        });
        this.paymentConfig.certificatesState = true;
        this.paymentConfig.certificatesPath = data;
      }
    } catch (e) {
      this.$message({
        message: e,
        type: "warning",
      });
    }
  }

  // 编辑表单
  editHandle() {
    const userInfo = this.$STORE.userStore.userInfo;
    let isValid =
      userInfo && userInfo.shopInfoVo && userInfo.shopInfoVo.platformShopId;
    const userInfoStorage = storage.get("userInfo");
    let isStorageValid =
      userInfoStorage &&
      userInfoStorage.shopInfoVo &&
      userInfoStorage.shopInfoVo.platformShopId;
    if (
      isValid &&
      isStorageValid &&
      userInfo.shopInfoVo.platformShopId ===
        userInfoStorage.shopInfoVo.platformShopId
    ) {
      this.shopId = userInfo.shopInfoVo.platformShopId;
      this.identityVisible = true;
    } else {
      this.$message({
        message: "请确认店铺信息",
        type: "warning",
      });
    }
  }

  async submit() {
    try {
      if (
        !this.paymentConfig.certificatesState &&
        this.paymentConfig.payType === 1
      ) {
        this.$message({
          message: "请上传微信支付API证书",
          type: "warning",
        });
        return;
      }
      await (this.configRef as ElForm).validate();
      const { code } = await updatePaymentConfig(
        Object.assign(this.paymentConfig, { sftChannelId: "30318953" }),
      );
      if (code === 200) {
        this.$message({
          message: "支付配置成功",
          type: "success",
        });
        await this.getConfig(2, this.code);
        this.editting = false;
      }
    } catch (e) {
      console.log(e);
    }
  }
}
</script>
