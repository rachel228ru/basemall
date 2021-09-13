<!--
 * @description: 
 * @Author: vikingShip
 * @Date: 2021-08-21 09:28:28
 * @LastEditors: vikingShip
 * @LastEditTime: 2021-08-26 13:13:06
-->
<template>
  <div class="order">
    <Form :shopInfo="shopInfo" :data-list="dataList" />
  </div>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import Form from "@/views/meal/Update/Form.vue";
import { getShopInfo } from "@/api/businessCenter/setting";
import { getMealList } from "@/api/meal/meal";
import { mealList } from "../Order/mealOrderType";
import { ShopInfoType } from "@/store/modulesType/userType";

@Component({
  components: {
    Form,
  },
})
export default class Index extends Vue {
  // 套餐列表
  dataList:mealList[] = [];

  // 店铺信息
  shopInfo = {} as ShopInfoType;

  created() {
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
}
</script>

<style lang="scss" scoped></style>
