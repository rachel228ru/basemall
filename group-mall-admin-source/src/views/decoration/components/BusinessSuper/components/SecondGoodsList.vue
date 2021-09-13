<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:09
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-23 20:26:38
-->
<template>
  <div class="second__goodsList-page">
    <div class="con__item-box" v-for="(item, index) in +5" :key="index">
      <div class="img__pic">
        <img :src="formData.goods.img" alt="" />
      </div>
      <div class="detail__box">
        <div class="goodsB--name">
          {{ formData.goods.name }}
        </div>
        <div class="goodsB--buy">
          <span class="goodsB--prinum">{{ `¥${formData.goods.price}` }}</span>
          <s class="goodsB--guaid">
            {{ `¥${formData.goods.guide}` }}
          </s>
          <div :class="['goods--cart', `goods--cart${formData.cartButton}`]">
            立即购买
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script lang="ts">
import { Vue, Prop, Component } from "vue-property-decorator";
import BusinessSuper from "./../BusinessSuper";

@Component({
  components: {},
})
export default class SecondGoodsList extends Vue {
  @Prop()
  formData!: BusinessSuper;
}
</script>

<style lang="scss">
@import "@/assets/styles/decoration/topBigGoods.scss";

.second__goodsList-page {
  background-color: #f5f5f5;
  height: 100%;
  overflow-y: scroll;
  box-sizing: border-box;
  padding: 5px 8px;

  .con__item-box {
    display: inline-block;
    width: calc((100% - 5px) / 2);
    background-color: #fff;
    border-radius: 10px;
    overflow: hidden;
    margin-bottom: 6px;

    .detail__box {
      box-sizing: border-box;
      padding: 10px 8px;
    }

    .img__pic {
      height: 169px;
      width: 100%;
      display: flex;
      align-items: center;
      justify-content: center;
      background-color: #e9f7fd;

      img {
        display: inline-block;
        width: 45px;
        height: 45px;
      }
    }

    .goodsB--name {
      text-overflow: ellipsis;
      overflow: hidden;
      white-space: nowrap;
      font-size: 14px;
      font-size: 14px;
      line-height: 26px;
      height: 26px;
      color: #0e0e0e;
    }

    .goodsB--buy {
      width: 100%;
      height: 40px;
      line-height: 40px;
      box-sizing: border-box;
    }

    .goodsB--prinum {
      display: inline-block;
      font-size: 19px;
      font-weight: 500;
      color: #fe4e63;
      height: 40px;
      line-height: 40px;
    }

    .goodsB--guaid {
      font-size: 12px;
      color: #aaaaaa;
      display: inline-block;
      margin-left: 5px;
      height: 40px;
      line-height: 40px;
    }

    .goods--cart {
      float: right;
      height: 25px;
      width: 25px;
      box-sizing: border-box;
      border-radius: 50%;
      display: flex;
      justify-content: center;
      align-items: center;
      margin-top: 10px;
    }

    .goods--cart3 {
      border: 1px solid rgba(252, 98, 63, 1);
      width: auto;
      padding: 0 5px;
      color: rgba(252, 98, 63, 1);
      font-size: 12px;
      border-radius: 12px;
      height: 22px;
      line-height: 22px;
    }

    .goods--cart4 {
      border: 1px solid rgba(252, 98, 63, 1);
      background-color: rgba(252, 98, 63, 1);
      width: auto;
      padding: 0 5px;
      color: #fff;
      font-size: 12px;
      border-radius: 12px;
      height: 22px;
      line-height: 22px;
    }
  }

  .con__item-box:nth-of-type(2n) {
    margin-left: 5px;
  }
}
</style>
