<template>
  <div>
    <div class="upLoad">
      <span class="upLoad__tag">*</span>上传CSV:
      <el-upload
        class="productImg__uploader product__productImg"
        :auto-upload="false"
        :on-change="uploadCsv"
        action=""
        :show-file-list="false"
      >
        <div class="upLoad__select">
          选择文件
          <div class="upLoad__select--fileName" v-if="csvName">
            {{ csvName }}
          </div>
        </div>
      </el-upload>
      <div class="upLoad__desc">最多支持1MB CSV的文件</div>
    </div>
    <div class="textLine">
      请使用最新版的淘宝助手导出CSV文件。最多支持1MB CSV的文件。
    </div>
    <div class="textLine">
      请将配套的图片文件包压缩为Zip格式压缩包并且导入(图片需在压缩包根目录下)
    </div>
    <div class="textLine">导入后的商品将出现在“素材导入”列表。</div>
    <div class="textLine">
      导入商品后需要一段时间同步商品信息，之后才会出现，请耐心等待。
    </div>
    <div class="sureBtn">
      <el-button type="primary" @click="sureUpload" :loading="loadType"
        >确认上传</el-button
      >
    </div>
  </div>
</template>

<script lang="ts">
import { Vue, Component } from "vue-property-decorator";
import { importGood } from "@/api/good/goods";
import { ElUploadInternalFileDetail } from "element-ui/types/upload";

@Component
export default class ImportDialog extends Vue {
  /** 上传的csv文件信息 */
  csvName = "";

  /** 上传的文件 */
  csvFile:any =""

  /** 加载状态 */
  loadType = false;

  async uploadCsv(file:ElUploadInternalFileDetail) {
    const fileName = file.name.substring(file.name.lastIndexOf(".") + 1);
    if (fileName !== "csv") {
      this.$message.error("请上传csv文件");
      return;
    }
    const isLt1M = file.size < 1 * 1024 * 1024;
    if (!isLt1M) {
      this.$message.error("上传文件大小不能超过 1MB!");
      return;
    }
    this.csvName = file.name;
    this.csvFile = file.raw;
  }

  async sureUpload() {
    if (!this.csvName) {
      this.$message.error("请上传csv文件");
      return;
    }
    this.loadType = true;
    const res = await importGood({
      file: this.csvFile,
    });
    if (res.code === 200) {
      this.$message.success("上传成功");
      this.$emit("close");
      this.loadType = false;
    }
  }
}
</script>

<style lang="scss" scoped>
@include b(upLoad) {
  display: flex;
  margin-bottom: 20px;
  padding-left: 20px;

  @include e(tag) {
    color: #ff0000;
  }
  @include e(select) {
    color: #2e99f3;
    font-size: 14px;
    margin-left: 20px;
    cursor: pointer;
    display: flex;
    @include m(fileName) {
      margin-left: 20px;
      color: #6a6969;
    }
  }
  @include e(desc) {
    color: #b4b4b4;
    font-size: 12px;
    margin-left: 20px;
  }
}

.textLine {
  font-size: 12px;
  color: rgba(89, 88, 88, 1);
  line-height: 22px;
  margin-left: 100px;
}

.sureBtn {
  width: 100%;
  display: flex;
  justify-content: flex-end;
  padding-right: 20px;
  margin-top: 20px;
}
</style>
