<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:10
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-23 13:29:08
-->
<template>
  <!-- 商品 -->
  <div class="goods__ponent-page">
    <div
      v-if="
        (formData.ponentType === 1 &&
          !(formData.firstCatList && formData.firstCatList.length)) ||
          (formData.ponentType === 2 && !formData.goods.length)
      "
      class="no__goods-item"
    >
      <img src="https://qiniu-app.qtshe.com/u391.png" alt />
    </div>
    <div
      class="tab__bar-box"
      v-if="
        formData.ponentType === 1 &&
          formData.firstCatList &&
          formData.firstCatList.length
      "
    >
      <div class="con">
        <div
          v-for="(item, idx) in formData.firstCatList"
          :key="idx"
          :class="[
            'class__item',
            item.id === formData.currentCategoryId
              ? ['', 'class__active', 'class__new-active'][formData.titleStyle]
              : '',
          ]"
          @click="activeClass(item.id)"
        >
          <span class="item__name">{{ item.name }}</span>
        </div>
      </div>
    </div>
    <div
      v-if="
        (formData.ponentType === 1 &&
          formData.firstCatList &&
          formData.firstCatList.length) ||
          (formData.ponentType === 2 && formData.goods.length)
      "
      class="goods"
      :class="formData.listStyle"
      :style="pageStyle"
    >
      <div
        v-for="(item, idx) in goodsList"
        :key="idx"
        :class="[formData.angle, formData.goodsStyle, 'goods-item']"
        :style="idx === formData.goods.length - 1 ? goodStyle2 : goodStyle"
      >
        <div
          v-if="formData.showContent.tagShow"
          :class="['goods-item__coner', getGoodsCornerMark.class]"
        >
          <img :src="getGoodsCornerMark.url" alt />
        </div>
        <div class="goods-item__icon">
          <div class="ipic">
            <img
              :class="[formData.ponentType === 1 ? 'show__mall' : 'show__big']"
              src="https://qiniu-app.qtshe.com/u391.png"
              alt
            />
          </div>
        </div>

        <div class="goods-item__foot" style="padding: 0 7px">
          <div
            v-if="formData.showContent.nameShow"
            :class="formData.textStyle"
            class="goods-item__name"
          >
            {{ item.name || "商品名称" }}
          </div>
          <div
            v-if="formData.showContent.pointShow"
            class="spellpre__goods--delivery"
          >
            <span class="i_box">次日达</span>
          </div>
          <div
            v-if="
              formData.showContent.priceShow || formData.showContent.buttonShow
            "
            class="goods-item__bottom"
          >
            <div class="goods-item__price">
              <span v-if="formData.showContent.priceShow"
                >￥{{ item.price || 99 }}</span
              >
            </div>
            <div
              v-if="
                formData.showContent.buttonShow &&
                  formData.showContent.buttonStyle === 1
              "
              class="goods-item__cart goods-item__cart1"
            >
              <img
                src="https://oss-tencent.bgniao.cn//gruul/20200710/810700b67e264a7b82bba347528b9eb0.png"
                alt
              />
            </div>
            <div
              v-if="
                formData.showContent.buttonShow &&
                  formData.showContent.buttonStyle === 2
              "
              class="goods-item__cart goods-item__cart2"
            >
              <img
                src="https://oss-tencent.bgniao.cn//gruul/20200710/3de00f3d8099459c83771b1f4decb197.png"
                alt
              />
            </div>
            <div
              v-if="
                formData.showContent.buttonShow &&
                  (formData.showContent.buttonStyle === 3 ||
                    formData.showContent.buttonStyle === 4)
              "
              :class="[
                'goods-item__cart',
                `goods-item__cart${formData.showContent.buttonStyle}`,
              ]"
            >
              {{ formData.showContent.buttonText || "立即购买" }}
            </div>
          </div>
          <div
            v-if="formData.listStyle === 'goods-style--one'"
            :class="[
              'goods-item__delivery',
              formData.listStyle === 'goods-style--four'
                ? 'goods-item__delivery3'
                : '',
            ]"
          ></div>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { Vue, Component, Prop, Watch } from "vue-property-decorator";
import Goods, { ICategoryItem, ISubFormGoods } from "./Goods";

@Component
export default class GoodsPreview extends Vue {
  @Prop()
  formData!: Goods;

  goodsList: number | ISubFormGoods[] = [];

  get pageStyle() {
    return {
      padding: `0px ${this.formData.pagePadding}px`,
      minHeight: "261px",
    };
  }

  /**
   * 动态显示商品边框样式
   */
  get goodStyle() {
    let margin = "";
    let width = "";
    const { listStyle, goodPadding = 0 } = this.formData;
    switch (listStyle) {
      case "goods-style--four":
        margin = `0px ${goodPadding}px 0px 0px`;
        break;
      case "goods-style--two":
        margin = `0px 0px ${goodPadding}px 0px`;
        width = `calc(50% - ${goodPadding / 2}px)`;
        break;
      default:
        margin = `0px 0px ${goodPadding}px 0px`;
        break;
    }

    return { margin, width };
  }

  /**
   * 如果是模式2调整边距
   */
  get goodStyle2() {
    let margin = "";
    const { listStyle } = this.formData;
    switch (listStyle) {
      case "goods-style--two":
        margin = `0px 0px ${this.formData.goodPadding}px 0px`;
        break;
    }
    return { margin };
  }

  /**
   * 获取商品角标地址及样式
   */
  get getGoodsCornerMark() {
    const { tagStyle } = this.formData.showContent;
    // 商品角标 1新品 2热卖 3抢购
    const srcs = [
      "https://medusa-small-file-1253272780.cos.ap-shanghai.myqcloud.com/gruul/20201026/37a28e49acb448fc8618d5e72851b027.png",
      "https://medusa-small-file-1253272780.cos.ap-shanghai.myqcloud.com/gruul/20201026/1622a28ef72040f9a2f367a2efa7c32d.png",
      "https://medusa-small-file-1253272780.cos.ap-shanghai.myqcloud.com/gruul/20201026/0f33444422b14a8e980cc50d0011e2d0.png",
    ];
    const styles = [
      "goods-item__coner1",
      "goods-item__coner2",
      "goods-item__coner3",
    ];
    return {
      url: srcs[+tagStyle - 1],
      class: styles[+tagStyle - 1],
    };
  }

  @Watch("formData", { deep: true })
  handleFormDataChange() {
    this.getGoodsList();
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 显示的商品数量
   */

  getGoodsList() {
    const {
      ponentType = 1,
      currentCategoryId = "",
      firstCatList = [],
      goods = [],
    } = this.formData;

    let productNumber: ISubFormGoods[] | number | string = [];
    // 选择商品数量时对应显示预览效果
    if (ponentType === 1) {
      const item =
        firstCatList.find(i => i.id === currentCategoryId) ||
        ({} as ICategoryItem);
      productNumber = item.productNumber || productNumber;
      this.goodsList = Number(productNumber);
    } else if (ponentType === 2) {
      productNumber = goods;
      this.goodsList = productNumber;
    }
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 切换一级分类
   * @param {string} id
   */

  activeClass(id: string) {
    this.formData.currentCategoryId = id;
  }

  mounted() {
    this.getGoodsList();
  }
}
</script>

<style scoped lang="scss">
.goods__ponent-page {
  .tab__bar-box {
    height: 50px;
    width: 100%;
    overflow: hidden;

    .con {
      width: auto;
      height: 60px;
    }

    .class__item {
      display: inline-block;
      height: 50px;
      position: relative;
      color: #6b6b6b;
      font-size: 14px;
      padding: 0px 12px;
      padding-top: 15px;
      cursor: pointer;
    }

    .class__active {
      color: #000000;
      font-size: 17px;
      padding-top: 8px;
      background: linear-gradient(#e5382e, #fd4e26) no-repeat;
      background-size: calc(100% - 22px) 8px;
      background-position: 11px 1.16em;
    }

    .class__new-active {
      color: #000000;
      font-size: 17px;
      padding-top: 8px;

      span {
        display: inline-block;
        padding-bottom: 4px;
        border-bottom: 2px solid red;
      }
    }

    .first__class {
      padding-left: 0;
      background-size: calc(100% - 11px) 8px;
      background-position: 0px 1.16em;
    }
  }

  .goods-item__icon {
    width: 100%;
    height: 180px;

    .ipic {
      display: inline-block;
      width: 100%;
      height: 180px;
      background-color: rgba(233, 247, 253, 1);
      display: flex;
      justify-content: center;
      align-items: center;

      .show__mall {
        display: inline-block;
        width: 44px;
        height: 46px;
      }

      .show__big {
        display: inline-block;
        width: 100%;
        height: 100%;
      }
    }
  }

  .goods-style--three {
    .goods-item__icon {
      height: 128px;
      width: 128px;
      margin-right: 10px;
      flex: none;

      .ipic {
        height: 128px;
      }
    }
  }

  .spellpre__goods--delivery {
    color: #a3a3a3;
    font-size: 12px;
    font-weight: 400;
    padding-top: 10px;
  }
}
</style>
