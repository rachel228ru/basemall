<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:11
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-09-01 15:48:30
-->
<template>
  <div class="share">
    <div class="share__tip">
      <div class="share__tip--lump" style="background: #08cc00"></div>
      <span class="share__tip--title">引导页设置</span>
      <el-switch
        v-model="openGuide"
        @change="openChange"
        class="share__tip--switch"
      ></el-switch>
    </div>
    <el-form label-width="80px" v-if="openGuide">
      <el-form-item label="引导图">
        <div class="guide">
          <div
            class="guide__item"
            v-for="(item, i) of guideList"
            :key="item.id"
          >
            <div class="item__l" @click="itemClick(item)">
              <el-upload
                class="item__uploader"
                :action="action"
                :show-file-list="false"
                :on-success="handleItemSuccess"
                :before-upload="beforeItemUpload"
              >
                <img v-if="item.url" :src="item.url" class="uploader__image" />
                <i v-else class="el-icon-plus uploader-icon"></i>
              </el-upload>
              <!-- <img :src="item.url"> -->
            </div>
            <i class="el-icon-error" @click="delGuiteItem(i)"></i>
            <div class="item__r">
              <div>
                <LinkSelect
                  ref="LinkSelect"
                  :type="item.type"
                  :link.sync="item.link"
                  :noProTab="true"
                  :limitProTab="false"
                >
                  <div slot="radio">
                    <el-radio v-model="item.type" :label="0">系统连接</el-radio>
                    <el-radio v-model="item.type" :label="1">小程序</el-radio>
                  </div>
                  <template v-if="item.type === 0">
                    <el-input
                      slot="button"
                      v-model="item.link.name"
                      placeholder="请选择url"
                      readonly
                    ></el-input>
                  </template>
                  <template v-else>
                    <el-input
                      slot="input"
                      v-model="item.appId"
                      placeholder="请输入appId"
                    ></el-input>
                  </template>
                  <!-- <el-input slot="button"
                            readonly
                  placeholder="请输入内容"></el-input>-->
                </LinkSelect>
              </div>
              <div></div>
            </div>
          </div>
          <div
            class="guide__item btn"
            @click="addGuideItem"
            v-if="guideList.length < 5"
          >
            <i class="el-icon-plus"></i> 添加
          </div>

          <div class="tips">
            尺寸建议设置750*1134的jpg图片，建议1M以下
            <el-button size="mini" style="margin-left: 10px" @click="resetGuide"
              >恢复默认</el-button
            >
          </div>
        </div>
      </el-form-item>
    </el-form>
    <el-form label-width="80px">
      <el-form-item>
        <el-button type="primary" @click="handleSave">保 存</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script lang="ts">
import { Vue, Component } from "vue-property-decorator";
import {
  getShareSetting,
  setShareSetting,
  setGuideSetting,
  getGuide,
  getGuideSetting,
  setGuide,
  getDefGuideSetting,
} from "@/api/global";
import LinkSelect from "@/components/LinkSelect/src/LinkSelect.vue";
import cloneDeep from "lodash/cloneDeep";
import isString from "lodash/isString";

@Component({
  components: {
    LinkSelect,
  },
})
export default class Share extends Vue {
  form = { background: "", defaultValue: false, title: "" };

  imageUrl = "";

  /** 图片上传地址 */
  action = process.env.VUE_APP_UPLOADURL + "/upload";

  link = {} as any;

  guideList = [];

  currentItem = {} as any;

  /** 是否开启引导 */
  openGuide = false;

  openChange(v) {
    setGuideSetting(v)
      .then(() => {
        this.openGuide = v;
      })
      .catch(() => {
        this.$message.warning("设置失败");
      });
  }

  handleSave() {
    Promise.all([this.setGuide(), this.setShareSetting()]).then(arr => {
      if (arr.every(i => i === null)) {
        this.$emit("submit");
      }
    });
  }

  resetGuide() {
    getDefGuideSetting()
      .then(res => {
        this.guideList = cloneDeep(res.data).map(item => {
          if (item.link) {
            item.link = JSON.parse(item.link);

            if (isString(item.link)) {
              item.link = JSON.parse(item.link);
            }
          } else {
            item.link = {
              url: "",
              path: "pages/index/index",
              link: '{"id": null,"type":0,"name":"","url":"","append":""}',
              appId: null,
              type: 0,
            };
          }

          return item;
        });
        this.$message.success("恢复成功");
      })
      .catch(() => {
        this.$message.warning("恢复失败");
      });
  }

  created() {
    this.getShareSetting();
    this.getGuide();
    this.getGuideSetting();
  }

  linkChange(v, item) {
    item.path = this.link.url;
    console.log(this.link, item);
  }

  itemClick(item) {
    this.currentItem = item;
  }

  addGuideItem() {
    this.guideList.push({
      url: "",
      path: "pages/index/index",
      link: { id: null, type: 0, name: "", url: "", append: "" },
      appId: null,
      type: 0,
    });
  }

  delGuiteItem(index) {
    // 删除选中下标的对象，并push一个新的空对象，带原对象id
    // let delItem = {} as any;
    // delItem = this.guideList[index];
    this.guideList.splice(index, 1);
    // this.guideList.push({
    //   id: delItem.id,
    //   appId: null,
    //   link: {},
    //   path: null,
    //   type: 0,
    //   url: "",
    // });
  }

  setGuide() {
    const data = cloneDeep(this.guideList).map(item => {
      if (item.link) {
        item.link = JSON.stringify(item.link);
      } else {
        item.link = {
          url: "",
          path: "pages/index/index",
          link: '{"id": null,"type":0,"name":"","url":"","append":""}',
          appId: null,
          type: 0,
        };
      }
      return item;
    });
    return setGuide(data)
      .then(() => null)
      .catch(() => {
        this.$message.warning("更新失败");
      });
  }

  getGuideSetting() {
    getGuideSetting().then(res => {
      this.openGuide = res.data;
    });
  }

  getGuide() {
    getGuide()
      .then(res => {
        this.guideList = res.data.map(item => {
          if (item.link) {
            item.link = JSON.parse(item.link);
          } else {
            item.link = { id: null, type: 0, name: "", url: "", append: "" };
          }
          return item;
        });
      })
      .catch(() => null);
  }

  handleItemSuccess(res) {
    this.currentItem.url = res.data;
  }

  beforeItemUpload(file) {
    const isJPG = file.type === "image/jpeg" || "image/png" || "image/jpg";

    if (!isJPG) {
      this.$message.error("上传图片只能是 png、jpg、jpeg 格式!");
    }

    return isJPG;
  }

  /** 图片回显及上传 */
  handleSuccess(res) {
    this.form.background = res.data;
    this.$message.success(`上传成功！`);
  }

  beforeUpload(file) {
    const isJPG = file.type === "image/jpeg" || "image/png" || "image/jpg";

    if (!isJPG) {
      this.$message.error("上传图片只能是 png、jpg、jpeg 格式!");
    }

    return isJPG;
  }

  reset() {
    setShareSetting({ ...this.form, defaultValue: true })
      .then(() => {
        this.$message.success("重置成功");
        this.getShareSetting();
      })
      .catch(err => {
        this.$message.warning(err);
      });
  }

  getShareSetting() {
    getShareSetting()
      .then(res => {
        this.form = res.data || {
          background: "",
          defaultValue: false,
          title: "",
        };
      })
      .catch(err => {
        this.$message.warning(err);
      });
  }

  setShareSetting() {
    return setShareSetting(this.form)
      .then(() => null)
      .catch(err => {
        this.$message.warning(err);
      });
  }
}
</script>

<style lang="scss">
.item__uploader {
  @include flex;

  width: 100%;
  height: 100%;
  overflow: hidden;

  img {
    width: 100%;
    height: 100%;
  }
}

.uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}

.uploader .el-upload:hover {
  border-color: #409eff;
}

.uploader .uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 177px;
  height: 150px;
  line-height: 150px;
  text-align: center;
}

.uploader .uploader__image {
  width: 177px;
  height: 150px;
  display: block;
}

.uploader .uploader__reset {
  position: absolute;
  margin-left: 200px;
  margin-top: -40px;
}

@include b(guide) {
  width: 400px;
  position: relative;

  .tips {
    color: rgb(211, 211, 211);
    margin-bottom: 30px;
  }

  .btn {
    background: #fff;
    @include flex;
    cursor: pointer;
  }

  @include e(item) {
    @include flex(flex-start);
    position: relative;

    .item__l {
      width: 120px;
      height: 80px;
      margin-right: 10px;

      img {
        width: 100%;
        height: 100%;
      }
    }

    .el-icon-error {
      position: absolute;
      right: -3px;
      top: -3px;
      cursor: pointer;
    }

    width: 100%;
    padding: 10px;
    background: #ebebeb;
    border: 1px dashed #bebebe;
    border-radius: 5px;
    margin-bottom: 15px;
  }
}

@include b(share) {
  @include e(tip) {
    vertical-align: center;
    background-color: rgba(246, 248, 250, 1);
    padding: 15px 15px 15px 30px;
    margin-bottom: 30px;

    @include m(title) {
      margin-left: 12px;
      color: #586884;
      font-weight: 700;
    }

    @include m(switch) {
      float: right;
    }

    @include m(lump) {
      display: inline-block;
      width: 3px;
      height: 12px;
      background-color: rgba(255, 153, 0, 1);
    }
  }

  .tips {
    color: rgb(139, 139, 139);
  }
}
</style>
