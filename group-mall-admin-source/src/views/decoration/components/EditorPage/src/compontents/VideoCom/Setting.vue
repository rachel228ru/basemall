<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:10
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-23 18:11:39
-->
<template>
  <div>
    <div class="homeSwiperForm-form">
      <el-form label-position="left" :model="upLoadiVideo" label-width="70px">
        <el-form-item label="视频来源">
          <el-radio-group v-model="upLoadiVideo.radio">
            <el-radio :label="1">本地视频</el-radio>
            <el-radio :label="2">外部视频</el-radio>
          </el-radio-group>
          <el-upload
            action
            v-if="upLoadiVideo.radio === 1 && !upLoadiVideo.video"
            :show-file-list="false"
            :auto-upload="false"
            :on-change="handleChange"
          >
            <div class="uploadBox">
              <i class="el-icon-plus"></i>
            </div>
          </el-upload>

          <div
            style="display:flex"
            v-else-if="upLoadiVideo.radio === 1 && upLoadiVideo.video"
          >
            <div class="uploadLink" @click="gotoVideo">{{ linkString }}</div>
            <el-upload
              action
              :show-file-list="false"
              :auto-upload="false"
              :on-change="handleChange"
            >
              <div class="modify">修改</div>
            </el-upload>
          </div>

          <div v-else-if="upLoadiVideo.radio === 2">
            <el-input
              class="uploadInput"
              v-model="upLoadiVideo.videoLink"
            ></el-input>
          </div>
        </el-form-item>
      </el-form>
      <el-form
        label-position="left"
        :model="upLoadiVideo"
        label-width="70px"
        v-if="upLoadiVideo.video || upLoadiVideo.radio === 2"
      >
        <el-form-item label="封面图">
          <el-upload
            action
            :show-file-list="false"
            :auto-upload="false"
            :on-change="handleImgChange"
          >
            <div class="imgContent" v-if="!upLoadiVideo.poster">
              <i class="el-icon-plus"></i>
            </div>
            <div v-if="upLoadiVideo.poster" class="imgContent">
              <img
                :src="upLoadiVideo.poster"
                style="width: 200px; height: 120px"
              />
            </div>
          </el-upload>
        </el-form-item>
      </el-form>

      <el-form
        v-if="upLoadiVideo.video || upLoadiVideo.radio === 2"
        label-position="left"
        :model="upLoadiVideo"
        label-width="70px"
      >
        <el-form-item label="播放比例">
          <el-radio-group v-model="upLoadiVideo.radioBL">
            <el-radio :label="1">16:9</el-radio>
            <el-radio :label="2">1:1</el-radio>
            <el-radio :label="3">自适应高度</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>

      <el-form
        v-if="upLoadiVideo.video || upLoadiVideo.radio === 2"
        label-position="left"
        :model="upLoadiVideo"
        label-width="70px"
      >
        <el-form-item label="视频填充 ">
          <el-radio-group v-model="upLoadiVideo.radioTC">
            <el-radio :label="1">填充</el-radio>
            <el-radio :label="2">周边留白</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script lang="ts">
import { Vue, Component, PropSync } from "vue-property-decorator";
import { upLoad } from "@/api/index";
// import LinkSelectItem from "@/components/LinkSelect/src/components/LinkSelectItem";
import VideoCom from "./VideoCom";
import { Loading } from "element-ui";

@Component({
  components: {},
})
export default class VideoComSetting extends Vue {
  @PropSync("formData", {
    type: Object,
    default() {
      return null;
    },
  })
  upLoadiVideo!: VideoCom;

  linkString = "";

  gotoVideo() {
    window.open(this.upLoadiVideo.video, "_blank");
  }

  mounted() {
    if (this.upLoadiVideo.video) {
      this.linkString = String(this.upLoadiVideo.video);
    }
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 上传修改视频
   * @param {*} e
   */

  async handleChange(e: { raw: any }) {
    let loadingInstance = Loading.service({ target: ".homeSwiperForm-form" });
    const file = e.raw;
    const fileSize = file.size / 1024 / 1024 < 10;
    if (
      [
        "video/mp4",
        "video/ogg",
        "video/flv",
        "video/avi",
        "video/wmv",
        "video/rmvb",
        "video/mov",
      ].indexOf(file.type) == -1
    ) {
      this.$message.error("请上传正确的视频格式");
      return false;
    }
    if (!fileSize) {
      this.$message.error("视频大小不能超过10MB");
      return false;
    }

    upLoad({
      file: file,
    })
      .then(res => {
        this.upLoadiVideo.video = res.data;
        this.linkString = String(res.data);
        loadingInstance.close();
      })
      .catch(err => {
        this.$message.error(err);
        loadingInstance.close();
      });
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 上传修改图片
   * @param {*} file
   */

  async handleImgChange(file: { size: number; raw: { type: string } }) {
    try {
      const img = await this.upLoadFile(file);
      this.upLoadiVideo.poster = img;
    } catch (error) {
      console.log(error);
    }
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 上传方法
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
.uploadBox {
  width: 200px;
  height: 80px;
  display: flex;
  justify-content: center;
  align-items: center;
  border: 1px dashed #e4e4e4;
  margin-top: 20px;
}

@include b(homeSwiperForm-form) {
  border: 1px solid #e4e4e4;
  padding: 10px;
}

.uploadLink {
  color: #409eff;
  font-size: 13px;
  display: inline-block;
  flex-wrap: nowrap;
  width: 250px;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
  cursor: pointer;
}

.uploadInput {
  width: 250px;
  height: 60px;
  margin-top: 20px;
}

.modify {
  cursor: pointer;
  color: #409eff;
  font-size: 13px;
}

.imgContent {
  width: 200px;
  height: 120px;
  border: 1px dashed #e4e4e4;
  margin-top: 10px;
  display: flex;
  justify-content: center;
  align-items: center;
  line-height: 120px;
}
</style>
