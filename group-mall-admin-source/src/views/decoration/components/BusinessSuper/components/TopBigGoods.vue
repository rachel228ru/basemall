<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:09
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-23 20:27:09
-->
<template>
  <div :class="['top__biggoods-page', getBigGoodsPageClass]">
    <!-- 大图模式 -->
    <template v-if="formData.listStyle === 1">
      <div
        v-for="(item, index) in +3"
        :key="`B${index}`"
        :class="['spellpre__goods', getGoodsClass]"
        :style="getGoodsStyle"
      >
        <div :class="['spellpre__goods--box', getGoodsBoxClass]">
          <div class="spellpre__goodsB--icon">
            <div class="ipic">
              <img :src="formData.goods.img" alt="" />
            </div>
          </div>
          <div class="detail__box">
            <div :class="['spellpre__goodsB--name', getGoodsNameClass]">
              {{ formData.goods.name }}
            </div>
            <div class="spellpre__goods--delivery">
              提货时间：06月24日 14:00
            </div>
            <div class="spellpre__goodsB--buy">
              <span class="spellpre__goodsB--prinum">{{
                `¥${formData.goods.price}`
              }}</span>
              <s class="spellpre__goodsB--guaid">
                {{ `¥${formData.goods.guide}` }}
              </s>
              <div
                v-if="formData.cartButton === 3"
                class="spellpre__goods--cart spellpre__goods--cart1"
              >
                <img
                  src="https://oss-tencent.bgniao.cn//gruul/20200710/810700b67e264a7b82bba347528b9eb0.png"
                  alt=""
                />
              </div>
              <div
                v-if="formData.cartButton === 4"
                class="spellpre__goods--cart spellpre__goods--cart2"
              >
                <img
                  src="https://oss-tencent.bgniao.cn//gruul/20200710/3de00f3d8099459c83771b1f4decb197.png"
                  alt=""
                />
              </div>
              <!-- <div
                :class="[
                  'spellpre__goods--cart',
                  `spellpre__goods--cart${formData.cartButton}`,
                ]"
              >
                立即购买
              </div> -->
            </div>
          </div>
        </div>
      </div>
    </template>

    <!-- 列表模式 -->
    <template v-if="formData.listStyle === 2">
      <div
        v-for="(item, index) in +5"
        :key="`F${index}`"
        :class="['spellpre__goods', getGoodsClass]"
        :style="getGoodsStyle"
      >
        <div :class="['spellpre__goods--box', getGoodsBoxClass]">
          <div class="spellpre__goodsL--content">
            <div class="spellpre__goodsL--icon">
              <div class="ipic">
                <img :src="formData.goods.img" alt="" />
              </div>
            </div>
            <div class="spellpre__goodsL--info">
              <div :class="['spellpre__goodsL--name', getGoodsNameClass]">
                {{ formData.goods.name }}
              </div>
              <div class="spellpre__goods--delivery">
                提货时间：06月24日 14:00
              </div>
              <div class="spellpre__goodsL--buy">
                <span class="spellpre__goodsL--prinum">{{
                  `¥${formData.goods.price}`
                }}</span>
                <div
                  v-if="formData.cartButton === 3"
                  class="spellpre__goods--cart spellpre__goods--cart1"
                >
                  <img
                    src="https://oss-tencent.bgniao.cn//gruul/20200710/810700b67e264a7b82bba347528b9eb0.png"
                    alt=""
                  />
                </div>
                <div
                  v-if="formData.cartButton === 4"
                  class="spellpre__goods--cart spellpre__goods--cart2"
                >
                  <img
                    src="https://oss-tencent.bgniao.cn//gruul/20200710/3de00f3d8099459c83771b1f4decb197.png"
                    alt=""
                  />
                </div>
                <!-- <div
                  :class="[
                    'spellpre__goods--cart',
                    `spellpre__goods--cart${formData.cartButton}`,
                  ]"
                >
                  立即购买
                </div> -->
                <div>
                  <s class="spellpre__goodsL--guaid">{{
                    `¥${formData.goods.guide}`
                  }}</s>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </template>
  </div>
</template>
<script lang="ts">
import { Vue, Prop, Component } from "vue-property-decorator";
import BusinessSuper from "./../BusinessSuper";

@Component
export default class TopBigGoods extends Vue {
  @Prop()
  formData!: BusinessSuper;

  /**
   * 设置商品class值
   */
  get getBigGoodsPageClass() {
    // 1顶部模式 2侧边栏模式 1大图样式 2列表样式
    const { selectMode, listStyle } = this.formData;
    const list = [];
    selectMode === 1 && list.push("page__goods-addPadding");
    selectMode === 2 && listStyle === 2 && list.push("left__Lgoods-style");
    return `${list.join(" ")}`;
  }

  /**
   * 设置商品class值
   */
  get getGoodsClass() {
    const { listStyle } = this.formData;
    // 1列表模式 2大图模式
    const styles = ["spellpre__goodsL", "spellpre__goodsB"];
    return styles[+listStyle - 1];
  }

  /**
   * 动态设置商品style
   */
  get getGoodsStyle() {
    const padding = `${+this.formData.goodsPadding / 2}px 0px`;
    return { padding };
  }

  /**
   * 获取商品边框样式
   */
  get getGoodsBoxClass() {
    const { goodsStyle = 1, doodsAngle = 1 } = {};
    // 商品样式 1无底白边 2卡片投影 3描边白底
    const gstyles = [
      "spellpre__goods--boxWB",
      "spellpre__goods--boxCP",
      "spellpre__goods--boxSW",
    ];
    // 图片倒角 1直角 2圆角
    const astyles = ["spellpre__goods--corners", "spellpre__goods--angle"];
    const gs = gstyles[+goodsStyle - 1];
    const as = astyles[+doodsAngle - 1];
    return `${gs} ${as}`;
  }

  /**
   * 获取商品名称文本样式
   */
  get getGoodsNameClass() {
    const { textStyle = 1 } = {};
    // 1常规体 2加粗体
    const styles = ["spellpre__goods--nameF", "spellpre__goods--nameB"];
    return styles[+textStyle - 1];
  }
}
</script>

<style lang="scss" scoped>
@import "@/assets/styles/decoration/businessGoods.scss";
</style>
