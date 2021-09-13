<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:11
 * @LastEditors: vikingShip
 * @LastEditTime: 2021-08-26 13:18:26
-->
<template>
  <div class="meal-form-container">
    <el-form
      ref="mealRef"
      class="dataForm"
      label-width="120px"
      label-position="left"
    >
      <el-form-item label="操作类型">
        <el-radio-group v-model="dataForm.optionType" @change="computePrice">
          <el-radio :label="2">续费</el-radio>
          <el-radio :label="3" :disabled="remainDay <= 7">升级</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="店铺名称">
        {{ shopInfo.shopName }}
      </el-form-item>
      <el-form-item label="店铺类型">
        {{ shopInfo.templateName }}
      </el-form-item>
      <el-form-item label="当前套餐">
        {{ shopInfo.packageName }}
      </el-form-item>
      <el-form-item label="剩余时长"> {{ remainDay }}天</el-form-item>
      <el-form-item label="升级套餐" v-if="dataForm.optionType === 3">
        <div class="meal-container" v-for="meal in dataList" :key="meal.id">
          <div
            class="meal-title"
            v-if="meal.openState"
            @click="selectMeal(meal)"
            :class="{
              select: dataForm.packageId === meal.id && remainDay !== 0,
              lower: meal.level <= shopInfo.level || remainDay === 0,
            }"
          >
            <span>{{ meal.name }}</span>
          </div>
        </div>
      </el-form-item>
      <el-form-item label="套餐单价">
        <span v-if="renewMeal.packagePrice && dataForm.optionType === 2"
          >{{ renewMeal.packagePrice }}元/{{
            packagePriceUnitMap[renewMeal.packagePriceUnit - 1]
          }}</span
        >
        <span v-if="selectedMeal.packagePrice && dataForm.optionType === 3"
          >{{ selectedMeal.packagePrice }}元/{{
            packagePriceUnitMap[selectedMeal.packagePriceUnit - 1]
          }}</span
        >
      </el-form-item>
      <el-form-item label="套餐周期" v-if="dataForm.optionType === 2">
        <el-slider
          v-model="dataForm.buyPeriod"
          :min="
            minDayMap[
              dataForm.optionType === 2
                ? renewMeal.packagePriceUnit
                : selectedMeal.packagePriceUnit
            ]
          "
          :max="1095"
          :step="
            dataForm.optionType === 2
              ? stepMap[renewMeal.packagePriceUnit]
              : stepMap[selectedMeal.packagePriceUnit]
          "
          show-tooltip
          show-input
          :marks="marks"
        >
        </el-slider>
      </el-form-item>
    </el-form>
    <div class="checkbox-list">
      <div v-if="dataForm.optionType === 3">
        升级后套餐周期为原来剩余时长，升级过程中不影响小程序正常营业使用，升级成功后自动小程序及后台自动更新
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
      <el-button
        type="primary"
        @click="payMoney"
        :disabled="dataForm.optionType === 3 && remainDay <= 7"
        >确认</el-button
      >
    </div>

    <PayMoney
      :visible.sync="paymentVisible"
      :price="realPrice"
      :shop-info="shopInfo"
      :meal="dataForm.optionType === 2 ? renewMeal : selectedMeal"
      :form="dataForm"
    />
  </div>
</template>

<script lang="ts">
import { Vue, Component, Prop, Ref, Watch } from "vue-property-decorator";
import PayMoney from "@/views/meal/Component/PayMoney.vue";
import { calculateMealPrice } from "@/api/meal/meal";
import { ElForm } from "element-ui/types/form";
import { mealList } from "../Order/mealOrderType";
import { ShopInfoType } from "@/store/modulesType/userType";

@Component({
  components: {
    PayMoney,
  },
})
export default class GiveCoupons extends Vue {
  @Ref() readonly mealRef!:ElForm;

  // 可购买套餐列表
  @Prop({
    type: Array,
    required: true,
    default() {
      return [];
    },
  })
  dataList!:mealList[];

  // 店铺信息
  @Prop({
    type: Object,
    default() {
      return {};
    },
  })
  shopInfo!: ShopInfoType;

  // 支付弹窗
  paymentVisible = false;

  priceLoading = false;

  /** 套餐表单 */
  dataForm = {
    agreeProtocol: null,
    autoDeduct: null,
    buyPeriod: 0,
    optionType: 2,
    packageId: 0,
    payInfo: "",
    payType: 1,
    shopId: null,
  };

  // 升级选中套餐
  selectedMeal = {
    functionDesc: "",
    id: 0,
    level: null,
    name: "",
    openState: null,
    packagePrice: null,
    packagePriceUnit: 1,
    remark: "",
    discountsJson: [],
  } as mealList;

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

  // 剩余时长
  get remainDay() {
    if (this.shopInfo.dueTime) {
      const now = new Date().getTime();
      const remain = new Date(this.shopInfo.dueTime).getTime();
      if (now <= remain) {
        return Math.floor((remain - now) / 86400000);
      } else {
        return 0;
      }
    } else {
      return 0;
    }
  }

  // 续费的套餐
  get renewMeal() {
    const meal = this.dataList.find(m => {
      return m.level === this.shopInfo.level;
    });
    if (meal) {
      return meal;
    } else {
      return {
        functionDesc: "",
        id: null,
        level: null,
        name: "",
        openState: null,
        packagePrice: null,
        packagePriceUnit: 1,
        remark: "",
        discountsJson: [],
      };
    }
  }

  @Watch("dataForm.buyPeriod")
  handlePeriodChange() {
    this.computePrice();
  }

  payMoney() {
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
    // 续费
    if (this.dataForm.optionType === 2) {
      const meal = this.dataList.find(m => {
        return m.level === this.shopInfo.level;
      }) as mealList;
      if (!meal.openState) {
        this.$message({
          type: "warning",
          message: "购买的套餐已下架",
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
      this.dataForm.packageId = this.renewMeal.id as number;
    }

    if (this.dataForm.optionType === 3) {
      if (!this.dataForm.packageId) {
        this.$message({
          type: "warning",
          message: "请选择要购买的套餐",
        });
        return;
      }
      this.dataForm.packageId = this.selectedMeal.id as number;
    }
    this.paymentVisible = true;
  }

  selectMeal(meal:mealList) {
    if (this.remainDay === 0) {
      this.$message({
        type: "warning",
        message: "请先续费当前套餐时长",
      });
      return;
    }
    if (Number(meal.level )> this.shopInfo.level) {
      this.dataForm.packageId = meal.id as number;
      this.selectedMeal = meal;
      this.computePrice();
    } else {
      this.$message({
        type: "warning",
        message: "请选择更高等级套餐",
      });
    }
  }

  // 返回接口计算价格
  async computePrice() {
    const buyPeriod =
      this.dataForm.optionType === 2 ? this.dataForm.buyPeriod : this.remainDay;
    const packageId =
      this.dataForm.optionType === 2 ? this.renewMeal.id : this.selectedMeal.id;
    if (!buyPeriod || !packageId || !this.shopInfo.platformShopId) {
      this.totalPrice = "0.00";
      this.realPrice = "0.00";
      return;
    }
    try {
      this.priceLoading = true;
      const form = {
        agreeProtocol: null,
        autoDeduct: null,
        buyPeriod,
        optionType: this.dataForm.optionType,
        packageId,
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

  // 协议
  goOrder() {
    this.$router.push({
      path: "/static/order",
    });
  }

  cancel() {
    history.go(-1);
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
    border: 1px solid #a8adb7;
    margin-right: 10px;
    cursor: pointer;
  }

  .meal-title.select {
    color: #007cff;
    border: 1px solid #007cff;
  }

  .meal-title.lower {
    color: #a8adb7;
    cursor: not-allowed;
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
  -webkit-box-shadow: 0 0 10px 0 #d5d5d5;
  box-shadow: 0 0 10px 0 #d5d5d5;
  background-color: white;
  margin-left: -14px;
  z-index: 100;
}
</style>
