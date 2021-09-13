<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:10
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-27 13:28:39
-->
<template>
  <div class="meal-form-container">
    <el-form
      ref="mealRef"
      class="dataForm"
      label-width="120px"
      label-position="left"
    >
      <el-form-item label="店铺名称">
        {{ shopInfo.shopName }}
      </el-form-item>
      <el-form-item label="店铺类型">
        {{ shopInfo.templateName }}
      </el-form-item>
      <el-form-item label="订购套餐">
        <div class="meal-container" v-for="meal in dataList" :key="meal.id">
          <div
            class="meal-title"
            v-if="meal.openState"
            @click="selectMeal(meal)"
            :class="{ select: dataForm.packageId === meal.id }"
          >
            <span>{{ meal.name }}</span>
          </div>
        </div>
      </el-form-item>
      <el-form-item label="套餐单价">
        <span v-if="selectedMeal.packagePrice"
          >{{ selectedMeal.packagePrice }}元/{{
            packagePriceUnitMap[selectedMeal.packagePriceUnit - 1]
          }}</span
        >
      </el-form-item>
      <el-form-item label="套餐周期">
        <el-slider
          v-model="dataForm.buyPeriod"
          @change="computePrice"
          :min="minDayMap[selectedMeal.packagePriceUnit]"
          :step="stepMap[selectedMeal.packagePriceUnit]"
          :max="1095"
          show-tooltip
          show-input
          :marks="marks"
        >
        </el-slider>
      </el-form-item>
    </el-form>
    <div class="checkbox-list">
      <div class="checkbox-item">
        <el-checkbox
          v-model="dataForm.autoDeduct"
          :true-label="1"
          :false-label="0"
        >
        </el-checkbox>
        <span class="checkbox-span"
          >到期账户余额自动扣除，小程序如需正常经营请保持余额充足可前往
          <el-button type="text" @click="recharge" size="normal">充值</el-button
          >，以免造成损失</span
        >
      </div>
      <div class="checkbox-item">
        <el-checkbox
          v-model="dataForm.agreeProtocol"
          :true-label="1"
          :false-label="0"
        ></el-checkbox>
        <span class="checkbox-span">
          我已阅读并同意
          <el-button type="text" @click="goOrder" size="normal"
            >《启山智软商城软件订购及服务协议》</el-button
          >
          <el-button type="text" @click="goOrder" size="normal"
            >《启山智软商城微信小程序订购及服务协议》</el-button
          >
        </span>
      </div>
    </div>
    <div class="meal-price">
      <div class="meal-price-total" v-loading="priceLoading">
        合计：￥{{ totalPrice }}
      </div>
      <div class="meal-price-real" v-loading="priceLoading">
        应付款：<span>￥{{ realPrice }}</span>
      </div>
    </div>
    <div class="footer">
      <el-button @click="cancel">取消</el-button>
      <el-button type="primary" @click="payMoney">确认</el-button>
    </div>

    <PayMoney
      :visible.sync="paymentVisible"
      :price="realPrice"
      :shop-info="shopInfo"
      :meal="selectedMeal"
      :form="dataForm"
    />
  </div>
</template>

<script lang="ts">
import { Vue, Component, Prop, Ref } from "vue-property-decorator";
import PayMoney from "@/views/meal/Component/PayMoney.vue";
import { calculateMealPrice } from "@/api/meal/meal";
import { mealList } from "./mealOrderType";
import { ShopInfoType } from "@/store/modulesType/userType";

@Component({
  components: {
    PayMoney,
  },
})
export default class GiveCoupons extends Vue {
  @Ref() readonly mealRef!: HTMLFormElement;

  @Prop({
    type: Array,
    required: true,
    default() {
      return [];
    },
  })
  dataList!: Array<mealList>;

  @Prop({
    type: Object,
    default() {
      return {};
    },
  })
  shopInfo!: ShopInfoType;

  /** 套餐表单 */
  dataForm = {
    agreeProtocol: null,
    autoDeduct: 1,
    buyPeriod: 0,
    optionType: 1,
    packageId: 0,
    payInfo: "",
    payType: 1,
    shopId: null,
  };

  selectedMeal = {
    functionDesc: "",
    id: null,
    level: null,
    name: "",
    openState: null,
    packagePrice: null,
    packagePriceUnit: 1,
    remark: "",
    discountsJson: [],
  } as mealList;

  priceLoading = false;

  paymentVisible = false;

  // 最终价格
  realPrice = "0.00";

  totalPrice = "0.00";

  // 套餐单价单位
  packagePriceUnitMap = ["天", "月", "年"];

  // 套餐周期最小值表
  minDayMap = {
    1: 0,
    2: 30,
    3: 365,
  };

  // 套餐周期最小值表
  stepMap = {
    1: 1,
    2: 30,
    3: 365,
  };

  marks = {
    0: {
      style: {
        fontSize: "13px",
      },
      label: "0",
    },
    60: {
      style: {
        fontSize: "13px",
      },
      label: "60天",
    },
    180: {
      style: {
        fontSize: "13px",
      },
      label: "180天",
    },
    365: {
      style: {
        fontSize: "13px",
      },
      label: "一年",
    },
    730: {
      style: {
        fontSize: "13px",
      },
      label: "二年",
    },
    1095: {
      style: {
        fontSize: "13px",
        width: "40px",
      },
      label: "三年",
    },
  };

  // 返回接口计算价格
  async computePrice() {
    if (
      !this.dataForm.buyPeriod ||
      !this.selectedMeal.id ||
      !this.shopInfo.platformShopId
    ) {
      this.totalPrice = "0.00";
      this.realPrice = "0.00";
      return;
    }
    try {
      this.priceLoading = true;
      const form = {
        agreeProtocol: null,
        autoDeduct: null,
        buyPeriod: this.dataForm.buyPeriod,
        optionType: 1,
        packageId: this.selectedMeal.id,
        payInfo: "",
        payType: null,
        shopId: this.shopInfo.platformShopId,
      };
      const response = await calculateMealPrice(form);
      const { code, data } = response;
      if (code === 200) {
        this.priceLoading = false;
        this.totalPrice = data.actualPrice.toFixed(2);
        this.realPrice = data.preferentialPrice.toFixed(2);
      } else {
        this.$message({
          type: "warning",
          message: "获取套餐信息失败！",
        });
        this.priceLoading = false;
        this.totalPrice = "0.00";
        this.realPrice = "0.00";
      }
    } catch (err) {
      this.priceLoading = false;
      this.totalPrice = "0.00";
      this.realPrice = "0.00";
    }
  }

  payMoney() {
    if (!this.dataForm.packageId) {
      this.$message({
        type: "warning",
        message: "请选择要购买的套餐",
      });
      return;
    }
    if (!this.dataForm.buyPeriod) {
      this.$message({
        type: "warning",
        message: "请选择正确的套餐周期",
      });
      return;
    }
    if (!this.dataForm.agreeProtocol) {
      this.$message({
        type: "warning",
        message: "请先阅读并同意相关协议",
      });
      return;
    }
    if (!Number(this.realPrice)) {
      this.$message({
        type: "warning",
        message: "该状态无法进行支付",
      });
      return;
    }
    this.paymentVisible = true;
  }

  selectMeal(meal: mealList) {
    this.dataForm.packageId = meal.id as number;
    if (meal.level === 1) {
      this.dataForm.buyPeriod = 30;
    }
    this.selectedMeal = meal;
    this.$nextTick(() => {
      this.computePrice();
    });
  }

  // 协议
  goOrder() {
    this.$router.push({
      path: "/static/order",
    });
  }

  // 充值
  recharge() {
    this.$router.push({
      path: "/business",
      query: {
        tabName: "Recharge",
      },
    });
  }

  cancel() {
    this.$emit("back");
  }
}
</script>

<style lang="scss" scoped>
.meal-form-container {
  color: #606266;
}

.meal-container {
  display: flex;
  align-items: center;

  .meal-title {
    width: 150px;
    padding: 5px;
    text-align: center;
    color: #a8adb7;
    border: 1px solid #a8adb7;
    margin-right: 10px;
    cursor: pointer;
  }

  .meal-title.select {
    color: #007cff;
    border: 1px solid #007cff;
  }
}

.checkbox-list {
  margin-top: 250px;

  .checkbox-item {
    margin: 15px 0;

    .checkbox-span {
      padding-left: 5px;
    }
  }
}

.meal-price {
  margin: 35px 0 100px 0;
  text-align: right;

  .meal-price-total {
    font-size: 14px;
    margin: 10px 0;
  }

  .meal-price-real {
    font-size: 14px;
    margin: 10px 0;
  }

  .meal-price-real span {
    font-size: 20px;
    color: #fd7c25;
  }
}

.footer {
  width: 1008px;
  position: fixed;
  bottom: 10px;
  padding: 15px 0px;
  display: -webkit-box;
  display: -ms-flexbox;
  display: flex;
  -webkit-box-pack: center;
  -ms-flex-pack: center;
  justify-content: center;
  -webkit-box-shadow: 0 0 10px 0px #d5d5d5;
  box-shadow: 0 0 10px 0 #d5d5d5;
  background-color: white;
  margin-left: -14px;
  z-index: 100;
}
</style>
