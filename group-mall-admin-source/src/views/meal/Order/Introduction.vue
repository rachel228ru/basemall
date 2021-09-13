<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:11
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-27 13:28:47
-->
<template>
  <div class="introduction-container">
    <div class="meal__item" v-for="meal in dataList" :key="meal.id">
      <div class="meal__item--header">
        <div class="meal__item--title">
          <span>{{ meal.name }}</span>
        </div>
        <div class="meal__item--detail">
          <span>{{ meal.remark }}</span>
        </div>
      </div>
      <div
        class="meal__item--content"
        v-for="(func, f) in meal.functionDesc"
        :key="f"
      >
        <div class="introduction" v-if="f < 5">
          <i class="el-icon-check"></i>
          <span class="text">{{ func }}</span>
        </div>
      </div>
      <div class="meal__item--footer">
        <div class="meal__item--button" @click="selectMeal(meal)">
          {{ meal.packagePrice }}元/{{
            packagePriceUnitMap[meal.packagePriceUnit - 1]
          }}
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { Component, Vue, Prop } from "vue-property-decorator";
import { mealList } from "./mealOrderType";

@Component
export default class Index extends Vue {
  @Prop({
    type: Array,
    required: true,
    default() {
      return [];
    },
  })
  dataList!: Array<mealList>;

  packagePriceUnitMap = ["天", "月", "年"];

  /**
   * 获取用户列表
   */
  selectMeal(meal: mealList) {
    this.$emit("selectMeal", meal);
  }
}
</script>

<style lang="scss" scoped>
.introduction-container {
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;
  align-items: center;
}

.meal__item {
  width: 250px;
  height: 475px;
  border: 1px solid #007cff;
  border-radius: 10px;
  box-shadow: 0 0 20px #dbe6f1, 0 0 20px #dbe6f1 inset;
  margin: 35px;

  .meal__item--header {
    height: 155px;
    background-color: #d3edff4d;
    padding: 25px;

    .meal__item--title {
      font-size: 16px;
      font-weight: bolder;
      text-align: center;
    }

    .meal__item--detail {
      font-size: 13px;
      margin-top: 30px;
      color: #4a4a4a;
      line-height: 20px;
    }
  }

  .meal__item--content {
    min-height: 130px;
    margin: 30px 45px 45px 45px;

    .introduction {
      padding: 5px 0;

      .text {
        padding-left: 15px;
      }

      .el-icon-check {
        font-size: 15px;
        color: #007cff;
        font-weight: bolder;
      }
    }
  }

  .meal__item--footer {
    .meal__item--button {
      border: 1px solid #007cff;
      text-align: center;
      margin: 0 auto;
      padding: 15px 50px;
      width: 180px;
      color: #007cff;
    }
  }
}
</style>
