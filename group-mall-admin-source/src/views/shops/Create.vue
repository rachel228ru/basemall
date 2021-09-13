<template>
  <div class="create">
    <el-form
      :model="createForm"
      :rules="createRules"
      ref="createFormRef"
      label-position="left"
      label-width="85px"
      class="createForm"
    >
      <el-form-item label="店铺logo" prop="logoUrl">
        <div class="create__avatar">
          <el-image :src="createForm.logoUrl" class="create__avatar--image">
            <div slot="placeholder" class="image-slot">
              <i class="el-icon-picture-outline"></i>
            </div>
            <div slot="error" class="image-slot">
              <i class="el-icon-picture-outline"></i>
            </div>
          </el-image>
          <el-upload
            class="create__avatar--uploader"
            action=""
            :auto-upload="true"
            :show-file-list="false"
            :before-upload="beforeAvatarUpload"
            :http-request="uploadAvatar"
          >
            <span class="primary">上传</span>
          </el-upload>
        </div>
      </el-form-item>
      <el-form-item label="店铺名称" prop="shopName">
        <div class="create__ShopName">
          <el-input
            class="create__ShopName--input"
            v-model="createForm.shopName"
            @input="getShopName"
          ></el-input>
          <span>最多16个字符</span>
        </div>
      </el-form-item>
      <el-form-item label="店铺类型" prop="shopTemplateId">
        <div class="create__type">
          <div
            v-for="template in templateList"
            :key="template.id"
            class="type__item"
            :class="{
              'type__item--active': createForm.shopTemplateId === template.id,
            }"
            @click="chooseType(template.id)"
          >
            <div class="title">{{ template.name }}</div>
            <div class="info">突破传统拼团模式</div>
            <div class="description">{{ template.description }}</div>
          </div>
        </div>
      </el-form-item>
      <el-form-item prop="agree">
        <el-checkbox v-model="createForm.agree"></el-checkbox>
        <span
          >我已阅读并同意<span class="primary pointer" @click="goOrder"
            >《启山智软软件订购及服务协议》</span
          >和
          <span @click="goRegister" class="primary pointer"
            >《启山智软支付开户及服务协议》</span
          ></span
        >
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="createShop">创建店铺</el-button>
      </el-form-item>
      <div class="create__footer">
        不知道选哪个？欢迎拨打开店咨询热线：180-5850-5737
      </div>
    </el-form>
  </div>
</template>

<script lang="ts">
import { Vue, Component, Ref } from "vue-property-decorator";
import { upLoad, getTemplateList } from "@/api/index";
import { createShop } from "@/api/shop";
import { ElForm } from "element-ui/types/form";

const publicPath = process.env.VUE_APP_PUBLICPATH;

@Component
export default class Create extends Vue {
  @Ref() readonly createFormRef!: ElForm;

  createForm: {
    shopTemplateId: string | number;
    agree: boolean;
    logoUrl: string;
    shopName: string;
  } = {
    shopTemplateId: "",
    agree: false,
    logoUrl:
      "https://medusa-small-file.oss-cn-hangzhou.aliyuncs.com/gruul/20200407/logo.png",
    shopName: "",
  };

  templateList = [];

  createRules = {
    shopName: [{ required: true, message: "请输入店铺名称", trigger: "blur" }],
    logoUrl: [{ required: true, message: "请上传logo", trigger: "blur" }],
    type: [{ required: true, message: "请选择店铺类型", trigger: "blur" }],
  };

  async created() {
    await this.getTemplateDropList();
    // 路由跳转默认赋值
    if (this.$route.query.form) {
      const form = JSON.parse(String(this.$route.query.form));
      Object.assign(this.createForm, form);
    }
  }

  chooseType(id: string | number) {
    if (this.createForm.shopTemplateId === id) {
      this.createForm.shopTemplateId = "";
    } else {
      this.createForm.shopTemplateId = id;
    }
  }

  /**
   * 描述 文件上传
   */
  async uploadAvatar(file: any) {
    const response = await upLoad({
      file: file.file,
    });
    const { data } = response;
    this.createForm.logoUrl = data;
  }

  beforeAvatarUpload(file: any) {
    const typeList = ["image"];
    const isType = typeList.some(type => {
      return file.type.indexOf(type) !== -1;
    });
    const isSize = file.size / 1024 / 1024 < 2;

    if (!isType) {
      this.$message.error("上传图片的格式不正确，请重新上传！");
    }
    if (!isSize) {
      this.$message.error(`上传图片不被不能超过2M，请重新上传！`);
    }
    return isType && isSize;
  }

  /**
   * 获取模板列表
   */
  async getTemplateDropList() {
    try {
      const response = await getTemplateList({
        type: 1,
        page: 1,
        size: 100,
      });
      const { code, data } = response;
      if (code === 200) {
        this.templateList = data.list;
      }
    } catch (e) {
      this.$message({
        message: e,
        type: "warning",
      });
    }
  }

  getShopName() {
    let charLength = 0;
    const shopName = this.createForm.shopName;
    for (let i = 0; i < shopName.length; i++) {
      const a = shopName.charAt(i);
      if (a.match(/[^\x00-\xff]/gi) != null) {
        if (charLength + 2 >= 16) {
          this.createForm.shopName = shopName.substr(0, i + 1);
          return;
        }
        charLength += 2;
      } else {
        if (charLength + 1 >= 16) {
          this.createForm.shopName = shopName.substr(0, i + 1);
          return;
        }
        charLength += 1;
      }
    }
  }

  async createShop() {
    const loading = this.$loading({
      lock: true,
      text: "Loading",
      spinner: "el-icon-loading",
      background: "rgba(0, 0, 0, 0.7)",
    });
    try {
      await this.createFormRef.validate();
      if (!this.createForm.agree) {
        this.$message({
          message: "请确定已同意协议",
          type: "warning",
        });
        loading.close();
        return;
      }
      const form = {
        logoUrl: this.createForm.logoUrl,
        shopName: this.createForm.shopName,
        shopTemplateId: this.createForm.shopTemplateId,
      };
      const response = await createShop(form);
      const { code } = response;
      if (code === 200) {
        this.$message({
          message: "创建店铺成功",
          type: "success",
        });
        await this.$router.push(publicPath);
      }
      loading.close();
    } catch (e) {
      const msg = e.replace("shopTemplateId:未选择模板", "请选择店铺模板");
      this.$message({
        message: msg,
        type: "warning",
      });
      loading.close();
    }
  }

  goOrder() {
    open(`${location.origin}${publicPath}/order`);
  }

  goRegister() {
    open(`${location.origin}${publicPath}/register`);
  }
}
</script>

<style lang="scss" scoped>
@import "@/assets/styles/shops/create.scss";
</style>
