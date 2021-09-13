<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:09
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-23 20:23:13
-->
<template>
  <div class="left__biggoods-page">
    <div class="con__box" v-for="item in getCategoryVo" :key="item.title">
      <div class="i__title">{{ item.name }}</div>
      <TopBigGoods :formData="formData"></TopBigGoods>
    </div>
  </div>
</template>
<script lang="ts">
import { Vue, Prop, Component } from "vue-property-decorator";
import BusinessSuper from "./../BusinessSuper";
import TopBigGoods from "./TopBigGoods.vue";

@Component({
  components: {
    TopBigGoods,
  },
})
export default class LeftBigGoods extends Vue {
  @Prop()
  formData!: BusinessSuper;

  get getCategoryVo() {
    const { category = {} as any } = this.formData.currentFirstCategory;
    return category.showCategoryVos || [];
  }
}
</script>

<style lang="scss">
.left__biggoods-page {
  .i__title {
    height: 30px;
    box-sizing: border-box;
    line-height: 30px;
    border-bottom: 1px solid rgba(242, 242, 242, 0.5);
    color: #999999;
  }

  .spellpre__goodsB--icon {
    height: 130px;

    .ipic {
      height: 130px;
      border-radius: 8px;
    }
  }

  .spellpre__goodsL--icon {
    height: 90px;
    width: 90px;

    .ipic {
      height: 90px;
      width: 90px;
      border-radius: 8px;
    }
  }

  .spellpre__goodsL--info {
    overflow-x: hidden;
  }

  .spellpre__goodsL--name {
    width: 100%;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }
}
</style>
