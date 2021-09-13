<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:11
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-27 10:37:05
-->
<template>
  <div class="stroe">
    <el-form
      :model="queryForm"
      :rules="queryRules"
      class="queryForm"
      ref="queryFormRef"
      label-width="120px"
      label-position="left"
    >
      <div class="store__tip">
        <div class="store__tip--lump orange"></div>
        <span class="store__tip--title">店铺设置</span>
      </div>
      <el-form-item label="店铺logo" prop="logoUrl">
        <UploadFile
          default-icon="https://medusa-small-file-1253272780.cos.ap-shanghai.myqcloud.com/gruul/20201114/55f9da2cc5254da2a90433e0acd77f66.jpg"
          :size="1"
          :avatar-height="60"
          :img-url.sync="queryForm.logoUrl"
        >
        </UploadFile>
      </el-form-item>
      <el-form-item label="店铺名称" prop="shopName">
        <el-input
          type="text"
          v-model="queryForm.shopName"
          style="width: 350px"
          placeholder="请填写店铺名称"
        />
      </el-form-item>
      <el-form-item label="营业时间" prop="businessHours">
        <el-time-picker
          is-range
          v-model="queryForm.businessHours"
          value-format="HH:mm:ss"
          range-separator="-"
          start-placeholder="开始时间"
          end-placeholder="结束时间"
          placeholder="选择营业时间"
        >
        </el-time-picker>
      </el-form-item>
      <el-form-item label="店铺电话" prop="shopPhone">
        <el-input
          type="text"
          v-model="queryForm.shopPhone"
          style="width: 350px"
          placeholder="请填写店铺电话"
        />
      </el-form-item>
      <div class="store__tip">
        <div class="store__tip--lump green"></div>
        <span class="store__tip--title">版权设置</span>
        <span
          class="store__tip--text"
          v-if="!queryForm.level || (queryForm.level && queryForm.level < 2)"
          >该功能限企业版及以上版本使用，<el-button type="text" @click="goOrder"
            >前往升级</el-button
          >版本。</span
        >
      </div>
      <el-form-item label="店铺底部logo" prop="miniBottomLog">
        <UploadFile
          default-icon="https://medusa-small-file.oss-cn-hangzhou.aliyuncs.com/gruul/20200407/logo.png"
          :size="1"
          :disabled="
            !queryForm.level || (queryForm.level && queryForm.level < 2)
          "
          :avatar-height="60"
          :img-url.sync="queryForm.miniBottomLog"
        >
          <template slot="el-upload__tip">
            <div class="el-upload__tip">
              尺寸建议330*90的PNG图片，大小1M以下
            </div>
          </template>
        </UploadFile>
      </el-form-item>
    </el-form>
    <Share @submit="confirmClickHandle"></Share>
  </div>
</template>

<script lang="ts">
import { Vue, Component, Ref } from "vue-property-decorator";
import { getShopInfo, updateShopInfo } from "@/api/businessCenter/setting";
import UploadFile from "@/views/decoration/components/UserCenter/UploadFile.vue";
import Share from "@/views/setting/modules/Share.vue";
import storage from "@/libs/storage";
import { ElForm } from "element-ui/types/form";

@Component({
  components: {
    UploadFile,
    Share,
  },
})
export default class Commission extends Vue {
  @Ref() readonly queryFormRef?: ElForm;

  /** 业务基础库表单 */
  queryForm = {
    businessHours: ["00:00:00", "23:59:59"],
    dueTime: "",
    isDue: 0,
    level: 0,
    logoUrl: "",
    miniBottomLog: "",
    orderSource: 0,
    packageName: "",
    platformShopId: 0,
    shopName: "",
    shopPhone: "",
    status: 0,
    templateName: "",
  };

  queryRules = {
    shopName: [{ required: true, message: "请填写店铺名称", trigger: "blur" }],
    businessHours: [
      { required: true, message: "请填写营业时间", trigger: "blur" },
    ],
    shopPhone: [
      { required: true, validator: this.validPhone, trigger: "blur" },
    ],
    miniBottomLog: [
      { required: true, message: "请上传底部版权标志", trigger: "change" },
    ],
    logoUrl: [{ required: true, message: "请上传店铺logo", trigger: "change" }],
  };

  created() {
    this.getInfo();
  }

  async getInfo() {
    try {
      this.queryForm = await getShopInfo();
      if (!this.queryForm.miniBottomLog) {
        this.queryForm.miniBottomLog =
          "https://medusa-small-file.oss-cn-hangzhou.aliyuncs.com/gruul/20200407/logo.png";
      }
      if (!this.queryForm.logoUrl) {
        this.queryForm.logoUrl =
          "https://medusa-small-file-1253272780.cos.ap-shanghai.myqcloud.com/gruul/20201114/55f9da2cc5254da2a90433e0acd77f66.jpg";
      }
      if (!this.queryForm.businessHours) {
        this.queryForm.businessHours = ["00:00:00", "23:59:59"];
      }
    } catch (e) {
      this.$message({
        type: "warning",
        message: e,
      });
    }
  }

  async confirmClickHandle() {
    try {
      await (this.queryFormRef as ElForm).validate();
      const platformShopId =
        storage.get("userInfo").shopInfoVo.platformShopId || "";
      let businessHours: string | string[] = ["00:00:00", "23:59:59"];
      if (typeof this.queryForm.businessHours !== "string") {
        businessHours = JSON.stringify(this.queryForm.businessHours);
      }
      const form = {
        shopName: this.queryForm.shopName,
        businessHours,
        shopPhone: this.queryForm.shopPhone,
        miniBottomLog: this.queryForm.miniBottomLog,
        logoUrl: this.queryForm.logoUrl,
      };
      const response = await updateShopInfo(platformShopId, form);
      const { code } = response;
      if (code === 200) {
        this.$message({
          type: "success",
          message: "修改成功",
        });
        await getShopInfo();
      }
    } catch (e) {
      console.log(e);
    }
  }

  validPhone(_rule: any, value: string, callback: (arg?: Error) => void) {
    if (!value) {
      callback(new Error("请输入电话号码"));
    } else if (!this.isValidPhone(value)) {
      callback(new Error("请输入正确的11位手机号码"));
    } else {
      callback();
    }
  }

  isValidPhone(str: string) {
    const reg = /^1[3|4|5|7|8][0-9]\d{8}$/;
    return reg.test(str);
  }

  // 前往订购/续费/升级
  goOrder() {
    if (!this.queryForm.level) {
      this.$router.push("/meal");
    } else {
      this.$router.push("/meal/update");
    }
  }
}
</script>

<style lang="scss" scoped>
.stroe {
  font-size: 13px;

  .store__tip {
    vertical-align: center;
    background-color: rgba(246, 248, 250, 1);
    padding: 15px 15px 15px 30px;
    margin-bottom: 30px;

    .store__tip--title {
      margin-left: 12px;
      color: #586884;
      font-weight: 700;
    }

    .store__tip--text {
      padding-left: 15px;
      color: rgba(102, 102, 102, 0.458);
    }
  }
}

.store__tip--lump {
  display: inline-block;
  width: 3px;
  height: 12px;
  background-color: rgba(255, 153, 0, 1);

  &.green {
    background-color: rgb(0, 255, 8);
  }
}
</style>
