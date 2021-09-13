<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:10
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-23 15:11:34
-->
<template>
  <!-- 轮播图 -->
  <div class="homeSwiperForm">
    <el-form label-position="left" label-width="70px">
      <el-form-item label="添加图片">
        <span style="color: #999;">最多添加5个广告，鼠标拖拽调整广告顺序</span>
      </el-form-item>
    </el-form>
    <div
      v-for="(item, index) in formModel.swiperList"
      :key="index"
      @click.capture="swiperItemClick(item)"
      class="homeSwiperForm-item"
      :draggable="true"
      @dragstart="handleDragstart(index)"
      @dragover="handleDragover"
      @drop="handleDrop(index)"
    >
      <i
        class="el-icon-remove-outline remove__swiper--item"
        @click="deleteSwiperItem(index)"
      ></i>
      <div class="homeSwiperForm-item__right">
        <el-form :model="item" label-width="70px">
          <el-form-item label="图片标题">
            <el-input v-model="item.title" placeholder="图片标题"></el-input>
          </el-form-item>
          <el-form-item label="图片上传">
            <el-upload
              class="homeSwiperForm-item__uploader"
              action
              :show-file-list="false"
              :auto-upload="false"
              :on-change="handleItemChange"
            >
              <img
                v-if="item.img"
                :src="item.img"
                class="homeSwiperForm-item__img"
              />
              <i v-else class="el-icon-plus homeSwiperForm-item__plus"></i>
            </el-upload>
          </el-form-item>
          <el-form-item label="导航链接" v-if="item.link">
            <LinkSelect :link="item.link" />
          </el-form-item>
          <el-form-item v-if="item.link && item.link.type !== 6">
            <span style="color: #9797A1">{{ item.link.name }}</span>
          </el-form-item>
        </el-form>
      </div>
    </div>

    <el-upload
      v-if="formModel.swiperList && formModel.swiperList.length < 5"
      class="homeSwiperForm-add"
      action
      multiple
      :auto-upload="false"
      :show-file-list="false"
      :on-change="handleChange"
    >
      <span>+ 添加背景图</span>
      <p style="margin-top: 5px;">建议宽度750像素</p>
    </el-upload>

    <div class="homeSwiperForm-form">
      <el-form label-position="left" :model="formModel" label-width="70px">
        <el-form-item label="边距">
          <el-row :gutter="15">
            <el-slider
              v-model="formModel.padding"
              :show-tooltip="false"
              :show-input="true"
              :max="30"
            ></el-slider>
          </el-row>
        </el-form-item>
        <el-form-item label="高度">
          <el-row :gutter="15">
            <el-slider
              v-model="formModel.height"
              :show-tooltip="false"
              :show-input="true"
              :max="360"
              :min="140"
            ></el-slider>
          </el-row>
        </el-form-item>
        <el-form-item label="图片样式">
          <el-radio-group v-model="formModel.imageStyle">
            <el-radio :label="1">常规</el-radio>
            <el-radio :label="2">投影</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="图片倒角">
          <el-radio-group v-model="formModel.imageAngle">
            <el-radio :label="1">直角</el-radio>
            <el-radio :label="2">圆角</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="指示器">
          <el-radio-group v-model="formModel.indicator">
            <el-radio :label="1">样式一</el-radio>
            <el-radio :label="2">样式二</el-radio>
            <el-radio :label="3">样式三</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script lang="ts">
import { Vue, Component, PropSync } from "vue-property-decorator";
import HomeSwiperFormData, { SwiperListItem } from "./HomeSwiper";
import LinkSelect from "@/components/LinkSelect";
import { upLoad } from "@/api/index";
import LinkSelectItem from "@/components/LinkSelect/src/components/LinkSelectItem";

/** 轮播图 */
@Component({
  components: {
    LinkSelect,
  },
})
export default class HomeSwiperForm extends Vue {
  @PropSync("formData", {
    type: Object,
    default() {
      return null;
    },
  })
  formModel!: HomeSwiperFormData;

  currentSwiperItem: SwiperListItem = {} as SwiperListItem;

  /** 拖动的组件的下角标 */
  dragStarIndex = -1;

  /**
   * @LastEditors: chuyinlong
   * @description: 选择图片上传
   * @param {*} file
   */
  async handleChange(file: any) {
    try {
      const img = await this.upLoadFile(file);
      const swiperListItem: SwiperListItem = {
        title: "",
        img,
        link: {
          id: "",
          type: "",
          name: "",
          url: "",
          append: "",
        },
      };
      this.formModel.swiperList.push(swiperListItem);
    } catch (error) {
      console.log(error);
    }
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 替换轮播图片
   * @param {*} file
   */

  async handleItemChange(file: any) {
    try {
      this.currentSwiperItem.img = await this.upLoadFile(file);
    } catch (error) {
      console.log(error);
    }
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 点击获取需要编辑的轮播项
   * @param {SwiperListItem} e
   */

  swiperItemClick(e: SwiperListItem) {
    this.currentSwiperItem = e;
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 选择链接
   * @param {LinkSelectItem} e
   */

  handleLinkSelect(e: LinkSelectItem) {
    this.currentSwiperItem.link = e;
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

  /**
   * @LastEditors: chuyinlong
   * @description: 删除轮播组件中的图片
   * @param {number} idx
   */

  deleteSwiperItem(idx: number) {
    this.formModel.swiperList.splice(idx, 1);
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 开始拖动，记录拖动的组件下角标
   * @param {number} i
   */

  handleDragstart(i: number) {
    this.dragStarIndex = i;
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 阻止默认行为，否则drop事件不会触发
   * @param {*} e
   */

  handleDragover(e: { preventDefault: () => void }) {
    e.preventDefault();
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 被放置的容器触发事件，交换两个组件的位置
   * @param {number} i
   */

  handleDrop(i: number) {
    if (this.dragStarIndex === i) {
      return false;
    }
    const temp = this.formModel.swiperList.splice(
      this.dragStarIndex,
      1,
      this.formModel.swiperList[i],
    );
    this.formModel.swiperList.splice(i, 1, ...temp);
  }
}
</script>

<style lang="scss">
@include b(homeSwiperForm-item) {
  position: relative;
  display: flex;
  border: 1px solid #e4e4e4;
  padding: 10px;
  margin-bottom: 10px;
  @include e(right) {
    flex: 1;
    padding-left: 10px;
  }
  @include e(uploader) {
    width: 80px;
    height: 80px;
    display: block;
    .el-upload {
      border: 1px dashed #d9d9d9;
      border-radius: 6px;
      cursor: pointer;
      position: relative;
      overflow: hidden;
    }
    .el-upload:hover {
      border-color: #409eff;
    }
  }

  @include e(img) {
    width: 80px;
    height: 80px;
    display: block;
  }

  @include e(plus) {
    font-size: 28px;
    color: #8c939d;
    width: 80px;
    height: 80px;
    line-height: 80px;
    text-align: center;
  }

  .remove__swiper--item {
    display: none;
  }

  &:hover {
    .remove__swiper--item {
      display: block;
      position: absolute;
      font-size: 20px;
      right: -8px;
      top: -8px;
      width: 20px;
      height: 20px;
      z-index: 20;
      color: #e4e4e4;
      background-color: #fff;
      cursor: pointer;
    }
  }
}

@include b(homeSwiperForm-add) {
  width: 100%;
  height: 100px;
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

@include b(homeSwiperForm-form) {
  border: 1px solid #e4e4e4;
  padding: 10px;
}
</style>
