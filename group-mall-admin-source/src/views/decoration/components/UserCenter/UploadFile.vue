<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:10
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-27 11:17:09
-->
<template>
  <div class="upload-container">
    <el-upload
      action=""
      ref="uploadFile"
      class="avatar-uploader"
      :class="{ gray: disabled }"
      :disabled="disabled"
      :auto-upload="true"
      :show-file-list="false"
      :before-upload="beforeAvatarUpload"
      :http-request="uploadImage"
    >
      <img
        v-if="imageUrl"
        :src="imageUrl"
        class="avatar"
        :height="avatarHeight"
        alt="img"
      />
      <i v-else class="el-icon-plus avatar-uploader-icon"></i>
      <slot name="el-upload__tip" slot="tip"></slot>
    </el-upload>
    <el-button v-if="defaultIcon !== ''" @click="imageUrl = defaultIcon"
      >恢复默认</el-button
    >
  </div>
</template>

<script lang="ts">
import { Vue, Component, PropSync, Prop } from "vue-property-decorator";
import { upLoad } from "@/api";

@Component
export default class PickCode extends Vue {
  @PropSync("imgUrl", { type: String, default: "" })
  imageUrl!: string;

  @Prop({
    type: Array,
    default() {
      return ["image"];
    },
  })
  typeList!: string[];

  @Prop({
    type: Number,
    default: 100,
  })
  size!: number;

  @Prop({
    type: String,
    default: "",
  })
  defaultIcon!: string;

  @Prop({
    type: Number,
    default: 120,
  })
  avatarHeight!: number;

  @Prop({
    type: Boolean,
    default: false,
  })
  disabled!: boolean;

  /**
   * @LastEditors: chuyinlong
   * @description: 描述 文件上传
   * @param {*} file
   */

  async uploadImage(file: { file: any }) {
    const res = await upLoad({
      file: file.file,
    });
    this.imageUrl = res.data;
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 文件上传之前校验
   * @param {*} file
   */

  beforeAvatarUpload(file: { type: string | string[]; size: number }) {
    const isType = this.typeList.some(type => {
      return file.type.indexOf(type) !== -1;
    });
    const isSize = file.size / 1024 / 1024 < this.size;

    if (!isType) {
      this.$message.error("上传图片的格式不正确，请重新上传！");
    }
    if (!isSize) {
      const sizeText =
        this.size < 1 ? this.size * 1024 + "kb" : this.size + "M";
      this.$message.error(`上传图片不能超过${sizeText}！`);
    }
    return isType && isSize;
  }
}
</script>

<style lang="scss">
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}

.avatar-uploader .el-upload:hover {
  border-color: #409eff;
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 120px;
  height: 120px;
  line-height: 120px;
  text-align: center;
}

.avatar {
  width: 120px;
  display: block;
}

.upload-container {
  display: flex;
  align-items: flex-end;

  .el-button {
    margin-left: 15px;
  }
}

.gray {
  opacity: 0.5;
}

.gray.avatar-uploader .el-upload {
  cursor: not-allowed;
}
</style>
