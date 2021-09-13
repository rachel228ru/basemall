<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:10
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-09-01 15:38:31
-->
<template>
  <div>
    <el-upload
      class="homeSwiperForm-add"
      action
      multiple
      :auto-upload="false"
      :show-file-list="false"
      :on-change="handleChange"
    >
      <div v-if="upLoadiImg.img" class="imgContent">
        <img :src="upLoadiImg.img" style="width: 200px; height: 120px" />
      </div>
      <div v-else>
        <span>请上传图片</span>
        <p style="margin-top: 5px">可在左侧自定义图片大小</p>
      </div>
    </el-upload>

    <div class="homeSwiperForm-form">
      <el-form label-position="left" :model="upLoadiImg" label-width="70px">
        <el-form-item label="图片上传">
          <el-upload
            action
            :show-file-list="false"
            :auto-upload="false"
            :on-change="handleChange"
          >
            <el-button>点击上传图片</el-button>
          </el-upload>
        </el-form-item>
        <el-form-item label="导航链接">
          <LinkSelect :link="link" @select="select" />
        </el-form-item>
        <el-form-item v-if="currentItem.type !== 6">
          <span style="color: #9797a1">{{ currentItem.name }}</span>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script lang="ts">
import { Vue, Component, PropSync, Watch } from "vue-property-decorator";
import LinkSelect from "@/components/LinkSelect";
import { upLoad } from "@/api/index";
import LinkSelectItem from "@/components/LinkSelect/src/components/LinkSelectItem";
import ImageCom from "./ImageCom";

@Component({
  components: {
    LinkSelect,
  },
})
export default class ImageComSetting extends Vue {
  @PropSync("formData", {
    type: Object,
    default() {
      return {};
    },
  })
  formModel!: ImageCom;

  upLoadiImg: ImageCom = {} as ImageCom;

  link = {};

  currentItem: LinkSelectItem = {} as LinkSelectItem;

  mounted() {
    this.upLoadiImg = this.formModel;
    this.link = this.upLoadiImg.link;
    this.currentItem = this.upLoadiImg.link;
  }

  @Watch("formModel")
  setUploadImg() {
    this.upLoadiImg = this.formModel;
    this.link = this.upLoadiImg.link;
    this.currentItem = this.upLoadiImg.link;
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 选择导航链接
   * @param {LinkSelectItem} item
   */
  select(item: LinkSelectItem) {
    this.currentItem = item;
    this.upLoadiImg.link = item;
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 选择图片上传
   * @param {*} file
   */

  async handleChange(file: any) {
    try {
      const img = await this.upLoadFile(file);
      this.upLoadiImg.img = img;
    } catch (error) {
      console.log(error);
    }
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 上传图片
   * @param {*} file
   */
  async upLoadFile(file: { size: number; raw: { type: string } }) {
    try {
      const whiteList = ["image/jpeg", "image/jpg", "image/png", "image/gif"];
      const isLt2M = file.size < 2 * 1024 * 1024;
      if (!whiteList.includes(file.raw.type)) {
        this.$message.error("上传文件只能是 GIF、JPG或PNG 格式!");
        return;
      }
      if (!isLt2M) {
        this.$message.error("上传文件大小不能超过 2MB!");
        return;
      }
      const res = await upLoad({
        file: file.raw,
      });
      return res.data;
    } catch (error) {
      console.log(error);
    }
  }
}
</script>

<style lang="scss" scoped>
@include b(homeSwiperForm-add) {
  margin-top: 20px;
  width: 100%;
  height: 180px;
  border: 1px solid #e4e4e4;
  margin-bottom: 10px;
  .el-upload {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    width: 100%;
    height: 100%;
  }
  span {
    color: #3088f0;
    cursor: pointer;
  }
  p {
    font-size: 12px;
    color: #a7a7a7;
  }
}

.imgContent {
  display: flex;
  justify-content: center;
  align-items: center;
}

@include b(homeSwiperForm-form) {
  border: 1px solid #e4e4e4;
  padding: 10px;
}
</style>
