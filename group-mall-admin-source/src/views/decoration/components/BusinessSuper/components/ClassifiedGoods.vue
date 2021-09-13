<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:09
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-23 20:23:05
-->
<template>
  <div class="classified__goods-page" v-if="isShowPage">
    <div
      v-for="(nav, inx) in navigationList.reverse()"
      :key="inx"
      v-show="nav.name"
    >
      <div
        class="navi__box1"
        v-if="
          formData.currentFirstCategory.navigationStyle === 1 &&
            isShowTitle &&
            nav.class.length
        "
      >
        <b></b>
        <span>{{ nav.name }}</span>
        <b></b>
      </div>
      <div
        class="navi__box2"
        v-if="
          formData.currentFirstCategory.navigationStyle === 2 &&
            isShowTitle &&
            nav.class.length
        "
      >
        <b></b>
        <span>{{ nav.name }}</span>
      </div>
      <div
        :class="['class-item', idx % 3 === 0 ? 'remove__margin' : '']"
        v-for="(item, idx) in nav.chartList"
        :key="idx"
        @click="showSecondCategory(item)"
      >
        <img :src="item.img" />
        <span>{{ item.category.name }}</span>
      </div>
    </div>
  </div>
</template>
<script lang="ts">
import { Vue, Prop, Component, Watch } from "vue-property-decorator";
import BusinessSuper, { Navigation } from "./../BusinessSuper";

@Component({
  components: {},
})
export default class ClassifiedGoods extends Vue {
  @Prop()
  formData!: BusinessSuper;

  isShowPage = true;

  get getValue() {
    const { navigation = [] } = this.formData.currentFirstCategory;
    return JSON.parse(JSON.stringify(navigation));
  }

  /**
   * 获取当前的分类导航
   * 如果长度大于2则将其他调到最后一个
   */
  get navigationList() {
    let { navigation = [] } = this.formData.currentFirstCategory;
    navigation = JSON.parse(JSON.stringify(navigation));
    if (navigation.length > 1) {
      const temp = navigation.splice(0, 1);
      navigation.push(...temp);
    }
    return navigation;
  }

  /**
   * 判断是否显示标题
   * 如果长度小于2，说明没有或者只有一个其他导航，则不显示
   */
  get isShowTitle() {
    const navigation = this.navigationList || [];
    return navigation.length > 1;
  }

  @Watch("getValue", { deep: true })
  navigationChange(v: Navigation[]) {
    if (v) {
      this.isShowPage = false;
      this.$nextTick(() => {
        this.isShowPage = true;
      });
    }
  }
}
</script>

<style lang="scss">
.classified__goods-page {
  .class-item {
    width: 71px;
    height: 111px;
    display: inline-block;
    margin-left: calc((100% - 215px) / 2);
    margin-bottom: 8px;
    vertical-align: top;

    img {
      display: block;
      width: 100%;
      height: 71px;
      border-radius: 3px;
    }

    span {
      display: block;
      height: 40px;
      line-height: 40px;
      width: 100%;
      color: #333;
      text-align: center;
    }
  }

  .remove__margin {
    margin-left: 0px;
  }

  .navi__box1 {
    height: 50px;
    text-align: center;

    span,
    b {
      display: inline-block;
    }

    span {
      height: 50px;
      line-height: 40px;
      padding: 0 15px;
      vertical-align: middle;
    }

    b {
      width: 52px;
      height: 1px;
      background-color: rgba(236, 236, 236, 1);
      vertical-align: super;
    }
  }

  .navi__box2 {
    height: 50px;
    position: relative;

    b {
      height: 13px;
      left: 0;
      top: 14px;
      width: 2px;
      background-color: #333;
      position: absolute;
    }

    span {
      display: inline-block;
      height: 50px;
      line-height: 40px;
      padding-left: 15px;
    }
  }
}
</style>
