<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:10
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-23 15:07:55
-->
<template>
  <div class="homeSwiper" :style="{ height: `${formModel.height / 2}px` }">
    <van-swipe
      class="homeSwiper-swiper"
      :show-indicators="false"
      @change="onChange"
      v-if="formModel.swiperList.length"
      :autoplay="3000"
    >
      <van-swipe-item
        class="homeSwiper-swiper__item"
        v-for="(item, index) in formModel.swiperList"
        :key="index"
      >
        <div
          class="homeSwiper-swiper__image"
          :style="{ padding: `0px ${formModel.padding}px` }"
        >
          <van-image
            width="100%"
            height="100%"
            :src="item.img"
            :class="['homeSwiper-swiper__imgItem', getImageClass]"
            contain
          />
        </div>
        <div
          v-if="formModel.indicator === 1"
          class="homeSwiper-swiper__indicator homeSwiper-swiper__icat1"
        >
          <span
            v-for="(ix, idx) in formModel.swiperList.length"
            :key="ix"
            :class="[curent === idx ? 'homeSwiper-swiper__icat1--active' : '']"
          ></span>
        </div>
        <div
          v-if="formModel.indicator === 3"
          class="homeSwiper-swiper__indicator homeSwiper-swiper__icat3"
        >
          <span
            v-for="(ix, idx) in formModel.swiperList.length"
            :key="ix"
            :class="[curent === idx ? 'homeSwiper-swiper__icat1--active' : '']"
          ></span>
        </div>
      </van-swipe-item>
    </van-swipe>
    <div class="homeSwiper-swiper__nullImg" v-if="!formModel.swiperList.length">
      <img src="https://qiniu-app.qtshe.com/u391.png" alt="" />
    </div>
    <div
      v-if="formModel.indicator === 2"
      class="homeSwiper-swiper__indicator homeSwiper-swiper__icat2"
      :style="{ right: `${formModel.padding + 5}px` }"
    >
      <span>{{ `${curent + 1}/${formModel.swiperList.length}` }}</span>
    </div>
  </div>
</template>

<script lang="ts">
import { Vue, Component, PropSync } from "vue-property-decorator";
import { Swipe, SwipeItem, Image } from "@/components/packages/vant";
import "@/components/packages/vant/lib/swipe/style";
import "@/components/packages/vant/lib/swipe-item/style";
import "@/components/packages/vant/lib/image/style";
import HomeSwiperFormData from "./HomeSwiper";

/** 轮播图 */
@Component({
  components: {
    VanSwipe: Swipe,
    VanSwipeItem: SwipeItem,
    VanImage: Image,
  },
})
export default class HomeSwiperPreview extends Vue {
  @PropSync("formData", {
    type: Object,
    default() {
      return null;
    },
  })
  formModel!: HomeSwiperFormData;

  /** 轮播序列号 */
  curent = 0;

  /**
   * 获取图片样式
   */
  get getImageClass() {
    const { imageStyle, imageAngle } = this.formModel;
    // 1常规 2投影
    const styles = ["homeSwiper-swiper__boxNM", "homeSwiper-swiper__boxCP"];
    // 图片倒角 1直角 2圆角
    const angles = ["homeSwiper-swiper__corners", "homeSwiper-swiper__angle"];
    const gs = styles[+imageStyle - 1];
    const as = angles[+imageAngle - 1];
    return `${gs} ${as}`;
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 轮播切换
   * @param {*} e
   */

  onChange(e: number) {
    this.curent = e;
  }
}
</script>

<style lang="scss">
@include b(homeSwiper) {
  background-color: #fff;
  position: relative;
}

@include b(homeSwiper-swiper) {
  height: 100%;
  @include e(item) {
    height: 100%;
  }
  @include e(image) {
    width: 100%;
    height: 100%;
    position: relative;
  }
  @include m(custom) {
    border-radius: 0;
  }
  @include m(fillet) {
    border-radius: 10px;
    overflow: hidden;
  }

  @include e(imgItem) {
    box-sizing: border-box;
    padding-top: 2px;

    img {
      height: calc(100% - 2px);
    }
  }

  @include e(nullImg) {
    box-sizing: border-box;
    overflow: hidden;
    height: 100%;
    background-color: rgba(233, 247, 253, 1);
    display: flex;
    justify-content: center;
    align-items: center;
    img {
      display: inline-block;
      width: 44px;
      height: 46px;
    }
  }

  @include e(boxNM) {
    img {
      box-shadow: none;
    }
  }

  @include e(boxCP) {
    img {
      box-shadow: 0 0 4px rgba(0, 0, 0, 0.2);
    }
  }

  @include e(corners) {
    img {
      border-radius: 0px;
    }
  }

  @include e(angle) {
    img {
      border-radius: 7px;
    }
  }

  @include e(indicator) {
    position: absolute;
    display: flex;
    align-items: center;
    justify-content: center;
    bottom: 10px;
    // border: 1px solid red;
    height: 20px;
    width: 100%;
    box-sizing: border-box;
  }

  @include e(icat1) {
    span {
      display: inline-block;
      width: 8px;
      height: 8px;
      background-color: #ebedf0;
      opacity: 0.3;
      margin-right: 6px;
      border-radius: 50%;
    }

    @include m(active) {
      opacity: 0.8 !important;
      background-color: #fff !important;
    }
  }

  @include e(icat2) {
    position: absolute;
    right: 0;
    bottom: 10px;
    display: block;

    span {
      float: right;
      box-sizing: border-box;
      padding: 5px 12px;
      background-color: rgba(0, 0, 0, 0.2);
      color: #fff;
      font-size: 13px;
      border-radius: 16px;
    }
  }

  @include e(icat3) {
    span {
      display: inline-block;
      width: 22px;
      height: 3px;
      background-color: #ebedf0;
      opacity: 0.3;
      margin-right: 6px;
      border-radius: 4px;
    }

    @include m(active) {
      opacity: 0.8 !important;
      background-color: #fff !important;
    }
  }
}
</style>
