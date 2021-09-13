<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:11
 * @LastEditors: vikingShip
 * @LastEditTime: 2021-08-26 11:26:15
-->
<template>
  <div class="order">
    <Form
      :shopInfo="shopInfo"
      :data-list="dataList"
      v-if="selected"
      @back="selected = false"
      ref="mealFormRef"
    />
    <Introduction
      :data-list="dataList"
      @selectMeal="selectMeal"
      v-else
    ></Introduction>
  </div>
</template>

<script lang="ts">
import { Component, Ref, Vue } from "vue-property-decorator";

import Introduction from "@/views/meal/Order/Introduction.vue";
import Form from "@/views/meal/Order/Form.vue";
import {mealList} from './mealOrderType'

import { getShopInfo } from "@/api/businessCenter/setting";
import { getMealList } from "@/api/meal/meal";
import { ShopInfoType } from "@/store/modulesType/userType";

@Component({
  components: {
    Introduction,
    Form,
  },
})
export default class Index extends Vue {
  @Ref() readonly mealFormRef!: HTMLFormElement;

  dataList:Array<mealList> = [];

  // 店铺信息
  shopInfo = {} as ShopInfoType;

  selected = false;

  created() {
    this.selected = false;
    this.getDataList();
    this.getInfo();
  }

  /**
   * 获取套餐列表
   */
  async getDataList() {
    try {
      const response = await getMealList();
      const { code, data } = response;
      if (code === 200) {
        this.dataList = data;
        this.dataList.forEach(meal => {
          if (meal.functionDesc && typeof meal.functionDesc === "string") {
            meal.functionDesc = meal.functionDesc.split(",");
          }
          if (meal.discountsJson && typeof meal.discountsJson === "string") {
            meal.discountsJson = JSON.parse(meal.discountsJson);
          }
        });
      } else {
        this.$message({
          type: "warning",
          message: "获取套餐信息失败！",
        });
      }
    } catch (err) {
      console.log(err);
    }
  }

  async getInfo() {
    try {
      this.shopInfo = await getShopInfo();
    } catch (e) {
      this.$message({
        type: "warning",
        message: e,
      });
    }
  }

  selectMeal(meal:mealList) {
    this.selected = true;
    this.$nextTick(() => {
      this.mealFormRef.selectMeal(meal);
    });
  }
}
</script>

<style lang="scss" scoped></style>
