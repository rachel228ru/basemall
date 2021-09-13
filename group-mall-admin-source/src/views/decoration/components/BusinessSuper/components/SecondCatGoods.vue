<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:09
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-23 20:26:19
-->
<template>
  <div class="second__cat-page">
    <div class="header__box">
      <div class="search__box">
        <span class="input__iocn"></span>
        <div class="icon__box" @click="changeStatus">
          <img
            src="https://oss-tencent.bgniao.cn//gruul/20200626/26bffe66093f4086b501a2fce01e9388.png"
            alt
          />
          <span>{{
            formData.secondCatGoodsStyle === 1 ? "矩形" : "列表"
          }}</span>
        </div>
      </div>
    </div>
    <div class="tab__con">
      <span
        v-for="(i, d) in tabList"
        :key="d"
        :class="[activeTab === d + 1 ? 'active__tab' : '']"
        @click="changeActiveTab(d + 1)"
      >
        {{ i }}
      </span>
    </div>
    <div class="list__page">
      <SecondGoodsList
        v-if="formData.secondCatGoodsStyle === 1"
        :formData="formData"
      ></SecondGoodsList>
      <SecondGoodsDetail
        v-if="formData.secondCatGoodsStyle === 2"
        :formData="formData"
      ></SecondGoodsDetail>
    </div>
  </div>
</template>
<script lang="ts">
import { Vue, Prop, Component } from "vue-property-decorator";
import BusinessSuper from "./../BusinessSuper";
import SecondGoodsList from "./SecondGoodsList.vue";
import SecondGoodsDetail from "./SecondGoodsDetail.vue";

@Component({
  components: {
    SecondGoodsList,
    SecondGoodsDetail,
  },
})
export default class SecondCatGoods extends Vue {
  @Prop()
  formData!: BusinessSuper;

  /** 当前激活tab */
  activeTab = 1;

  /** tab列表 */
  tabList = ["综合", "销量", "新品", "价格"];

  /**
   * @LastEditors: chuyinlong
   * @description: 修改列表状态
   */

  changeStatus() {
    this.formData.secondCatGoodsStyle =
      this.formData.secondCatGoodsStyle === 1 ? 2 : 1;
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 修改激活tab
   * @param {number} tab
   */

  changeActiveTab(tab: number) {
    this.activeTab = tab;
  }
}
</script>

<style lang="scss">
.second__cat-page {
  height: 100%;

  .header__box {
    box-sizing: border-box;

    .search__box {
      height: 40px;
      display: flex;
      padding: 0px 10px;

      .input__iocn {
        flex: 1;
        height: 30px;
        background-color: rgba(228, 228, 228, 1);
        border-radius: 20px;
        margin-top: 3px;
        cursor: pointer;
      }

      .icon__box {
        width: 50px;
        height: 100%;
        position: relative;
        cursor: pointer;

        img {
          float: right;
          width: 20px;
          height: 20px;
          margin-right: 4px;
        }

        span {
          width: 60px;
          line-height: 18px;
          height: 18px;
          text-align: right;
          position: absolute;
          top: 22px;
          right: 0;
          color: #999;
          font-size: 13px;
        }
      }
    }
  }

  .tab__con {
    display: flex;

    span {
      flex: 1;
      text-align: center;
      font-size: 14px;
      cursor: pointer;
      height: 40px;
      line-height: 40px;
    }

    .active__tab {
      color: #fc425a;
    }
  }

  .list__page {
    height: calc(100% - 80px);
  }
}
</style>
