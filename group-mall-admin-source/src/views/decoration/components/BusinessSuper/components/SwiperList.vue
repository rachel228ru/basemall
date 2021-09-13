<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:09
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-23 20:26:57
-->
<template>
  <div class="swiper__list-page">
    <van-swipe
      :show-indicators="false"
      :autoplay="3000"
      @change="onChange"
      style="height: 110px;"
    >
      <van-swipe-item
        v-for="(item, index) in formData.currentFirstCategory.swiperList"
        :key="index"
      >
        <div class="swiper-con__image">
          <van-image
            width="100%"
            height="100%"
            :src="item.img"
            :class="[
              'swiper-con__imgItem',
              'swiper-con__boxNM',
              'swiper-con__corners',
            ]"
            contain
          />
        </div>
        <div v-if="true" class="swiper-con__indicator swiper-con__icat1">
          <span
            v-for="(ix, idx) in formData.currentFirstCategory.swiperList.length"
            :key="ix"
            :class="[curent === idx ? 'swiper-con__icat1--active' : '']"
          ></span>
        </div>
        <div v-if="false" class="swiper-con__indicator swiper-con__icat3">
          <span
            v-for="(ix, idx) in formData.currentFirstCategory.swiperList.length"
            :key="ix"
            :class="[curent === idx ? 'swiper-con__icat1--active' : '']"
          ></span>
        </div>
      </van-swipe-item>
    </van-swipe>
  </div>
</template>
<script lang="ts">
import { Vue, Prop, Component } from "vue-property-decorator";
import BusinessSuper from "./../BusinessSuper";
import { Swipe, SwipeItem, Image } from "@/components/packages/vant";
import "@/components/packages/vant/lib/swipe/style";
import "@/components/packages/vant/lib/swipe-item/style";
import "@/components/packages/vant/lib/image/style";

@Component({
  components: {
    VanSwipe: Swipe,
    VanSwipeItem: SwipeItem,
    VanImage: Image,
  },
})
export default class SwiperList extends Vue {
  @Prop()
  formData!: BusinessSuper;

  /** 轮播序列号 */
  curent = 0;

  /**
   * @LastEditors: chuyinlong
   * @description: 轮播切换
   * @param {number} e
   */

  onChange(e: number) {
    this.curent = e;
  }
}
</script>

<style lang="scss">
.swiper__list-page {
  margin-bottom: 30px;

  @include b(swiper-con) {
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
        border-radius: 4px;
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

  .swiper-con__corners {
    img {
      border-radius: 4px;
    }
  }
}
</style>
