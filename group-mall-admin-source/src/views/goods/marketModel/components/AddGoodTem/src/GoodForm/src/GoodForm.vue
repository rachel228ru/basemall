<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:10
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-09-01 15:46:18
-->
<template>
  <div class="goodForm">
    <el-steps :active="setpIndex" simple>
      <el-step title="1.编辑基本信息" icon="none"></el-step>
      <el-step title="2.编辑销售信息" icon="none"></el-step>
      <el-step title="3.编辑商品信息" icon="none"></el-step>
    </el-steps>
    <!--  -->
    <keep-alive>
      <component
        ref="componentRef"
        :good-detail="goodDetail"
        :is="currentStep"
        :logisticsId="logisticsId"
      ></component>
    </keep-alive>
    <!--  -->
    <div class="next">
      <el-button v-if="prevStep !== ''" @click="handlePrev" type="primary"
        >上一步</el-button
      >
      <el-button v-if="nextStep !== ''" @click="handleNext" type="primary"
        >下一步</el-button
      >
      <template v-if="currentStep === 'GoodDetailInfo'">
        <el-button @click="putAway" type="primary">上架</el-button>
        <el-button @click="cancelputAway">取消</el-button>
      </template>
    </div>
  </div>
</template>

<script lang="ts">
import { Vue, Component, Ref } from "vue-property-decorator";
import { editGood, updateGood, putWayGood } from "@/api/good/goods";
import GoodBaseInfo from "./GoodBaseInfo.vue";
import GoodSellInfo from "./GoodSellInfo.vue";
import GoodDetailInfo from "./GoodDetailInfo.vue";
import storage from "@/libs/storage";
import { ApiSkuType } from "@/views/goods/marketModel/goodType";

Component.registerHooks(["beforeRouteLeave"]);
interface StepIndicator {
  /** 上一步 */
  prev: string;
  /** 下一步 */
  next: string;
  /** 当前进度 */
  setpIndex: number;
}

@Component({
  components: {
    GoodBaseInfo,
    GoodSellInfo,
    GoodDetailInfo,
  },
})
export default class GoodForm extends Vue {
  /** 总数据 */

  allFoorm = {};

  /**
   * 商品添加步骤
   */
  stepIndicator: {
    [key: string]: StepIndicator;
  } = {
    GoodBaseInfo: {
      prev: "",
      next: "GoodSellInfo",
      setpIndex: 0,
    },
    GoodSellInfo: {
      prev: "GoodBaseInfo",
      next: "GoodDetailInfo",
      setpIndex: 1,
    },
    GoodDetailInfo: {
      prev: "GoodSellInfo",
      next: "",
      setpIndex: 2,
    },
  };

  /** 当前步骤 */
  currentStep = "GoodBaseInfo";

  /** 需要提交的数据 */
  goodFormModel = {
    id: null as string | null,
    /** 状态(默认上架，0–下架（仓库中），1–上架，2–已售完) */
    status: 1,
    /** 销售专区 0商超 2秒杀 */
    saleMode: "0",
  };

  /** 商品详情 */
  goodDetail = null;

  /** 监听选择的运费模板 */
  logisticsId = "";

  /** 是否需要显示重量 */
  isWeight = false;

  checkFlag = true;

  @Ref()
  readonly componentRef!: HTMLFormElement;

  get currentStepIndicator(): StepIndicator {
    return this.stepIndicator[this.currentStep];
  }

  get setpIndex(): number {
    return this.currentStepIndicator.setpIndex;
  }

  get prevStep(): string {
    return this.currentStepIndicator.prev;
  }

  get nextStep(): string {
    return this.currentStepIndicator.next;
  }

  mounted() {
    this.getGoodDetail();
  }

  /**
   * 上一步
   */
  handlePrev() {
    // this.putAway(true);
    const prevStep = this.prevStep;
    if (prevStep !== "") {
      this.currentStep = prevStep;
    }
  }

  /**
   * 下一步
   */
  async handleNext() {
    const nextStep = this.nextStep;
    if (nextStep !== "") {
      await this.getGoodFormModel();
      if (this.checkFlag) {
        this.currentStep = nextStep;
      }
    }
  }

  /**
   * 获取商品详情
   */
  async getGoodDetail() {
    const goodId = this.$route.params.id;
    if (!goodId) {
      return;
    }
    const { data } = await editGood(goodId);
    this.goodDetail = data;
    this.goodFormModel.id = goodId;
  }

  async getGoodFormModel() {
    try {
      const res = await this.componentRef.getFormModel();
      Object.assign(this.goodFormModel, res);
      const goodFormModel = this.goodFormModel as any;
      if (goodFormModel.skuStocks) {
        this.checkSku(goodFormModel.skuStocks);
      }
      return;
    } catch (error) {
      this.$message.warning("请将信息填写完整");
      return Promise.reject(error);
    }
  }

  /**
   * 规格内容的二次验证
   */
  checkSku(skuStock: ApiSkuType[]) {
    this.checkFlag = true;
    skuStock.forEach(item => {
      if (String(item.originalPrice) === "0") {
        this.$message.error("指导价不能为0");
        this.checkFlag = false;
      } else {
        if (String(item.originalPrice).split(".")[1]) {
          const length = String(item.originalPrice).split(".")[1].length;
          if (length > 2) {
            this.$message.error("指导价最多保留两位小数");
            this.checkFlag = false;
          }
        }
      }
      if (String(item.price).split(".")[1]) {
        const length = String(item.price).split(".")[1].length;
        if (length > 2) {
          this.$message.error("实售价最多保留两位小数");
          this.checkFlag = false;
        }
      }
      if (Number(item.price) > Number(item.originalPrice)) {
        this.$message.error("实售价不能大于指导价");
        this.checkFlag = false;
      }
    });
    if (this.checkFlag) {
      this.checkStock(skuStock);
    }
  }

  checkStock(skuStock: ApiSkuType[]) {
    const regs = /^[0-9]*[1-9][0-9]*$/;
    if (skuStock.length > 1) {
      const isImg = skuStock.some(item => {
        return item.pic === "" || item.pic === undefined;
      });
      if (isImg) {
        this.$message.error("请输入规格sku图");
        this.checkFlag = false;
        return;
      }
    }
    skuStock.forEach(item => {
      if (item.stock === "0") {
        this.$message.error("库存不能为0");
        this.checkFlag = false;
      }
      if (!regs.test(item.stock as string)) {
        this.$message.error("不能输入非数字库存");
        this.checkFlag = false;
        return;
      }
    });
  }

  /**
   * 上架
   */
  async putAway() {
    (this.$parent.$parent.$parent as any).saveFlag = true;
    await this.getGoodFormModel();
    const goodId = this.$route.params.id;
    storage.delete("formModel");
    const res = goodId
      ? await updateGood(this.goodFormModel)
      : await putWayGood(this.goodFormModel);
    if (res.code === 200) {
      this.$message.success(`${goodId ? "编辑成功" : "上架成功"}`);
      this.$router.push({ name: "Goods" });
    }
  }

  /**
   * 取消上架
   */
  cancelputAway() {
    this.$router.push({ name: "Goods" });
  }
}
</script>

<style lang="scss">
@import "@/assets/styles/goods/index.scss";
.goodForm {
  .topTag {
    display: flex;
    height: 40px;
    border-bottom: 1px solid #eee;
    font-size: 14px;
    font-weight: bold;
    margin-bottom: 40px;
    li {
      padding-left: 20px;
      height: 38px;
      display: flex;
      align-items: center;
      margin-right: 30px;
      cursor: pointer;
      span {
        line-height: 38px;
        display: block;
        padding: 0 20px;
      }
      &.topTag--active span {
        border-bottom: #2d8cf0 2px solid;
        color: #2d8cf0;
      }
    }
  }

  .pageRoll {
    overflow: auto;
    overflow-x: hidden;
    height: 800px;
    padding-bottom: 80px;
  }

  .pageRoll::-webkit-scrollbar {
    width: 0 !important;
    height: 0 !important;
  }

  .navLine {
    margin-top: 25px;
    margin-bottom: 25px;
    height: 40px;
    display: flex;
    align-items: center;
    background-color: #f8f8f8;
    padding-left: 15px;
    font-weight: bold;
  }

  .spec {
    margin-left: 40px;
    &__left {
      margin-left: 8px;
    }
  }

  .specitem {
    margin-left: 124px;
    margin-top: 10px;
    &__inp {
      display: flex;
      align-items: flex-end;
      margin-top: 10px;
      &--inside {
        width: 50px;
        margin: 0px 10px;
      }
    }
  }

  .dialogTitle {
    display: flex;
    justify-content: center;
    font-weight: bold;
    font-size: 16px;
  }

  .dialog__line {
    display: flex;
    padding-left: 20px;
    padding-top: 10px;
    justify-content: flex-start;
    align-items: flex-start;
    &__fir {
      width: 100%;
      text-align: right;
      display: flex;
      align-items: center;
    }
    &__sec {
      width: 100%;
      display: flex;
      margin-top: 10px;
      &--head {
        padding: 10px;
        display: flex;
        justify-content: space-between;
        font-size: 12px;
        background-color: #f6f6f6;
      }
      &--body {
        width: 100%;
        display: flex;
        justify-content: space-between;
        align-items: center;
        border-bottom: 1px solid #999;
        padding: 10px;
      }
    }
  }

  .add--text {
    line-height: 30px;
    cursor: pointer;
    color: #2d8cf0;
    margin-left: 90px;
    margin-top: 10px;
  }

  .pick--dia {
    display: flex;
    justify-content: space-between;
    padding: 10px 30px;
    background-color: #e9f3fb;
  }

  .pick--input {
    width: 100%;
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 10px 30px;
  }

  .valueName {
    margin-top: 10px;
    width: 620px;
    padding: 20px 20px;
    border: 1px solid #d7d7d7;
    &__noList {
      display: flex;
      justify-content: center;
      color: #d7d7d7;
      font-size: 15px;
      margin-left: 20px;
      margin-top: 20px;
    }
    &__List {
      display: flex;
      justify-content: space-between;
      margin-bottom: 10px;
      div {
        color: red;
        cursor: pointer;
      }
    }
  }

  .product {
    margin-top: 10px;
  }

  .productVideo__uploader {
    display: flex;
    align-items: flex-end;
  }

  .productImg__uploader {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
  }

  .productImg__uploader:hover {
    border-color: #409eff;
  }

  .product__productImg {
    width: 100px;
    height: 100px;
    display: inline-block;
  }

  .product__productVideo {
    width: 250px;
    height: 120px;
    display: inline-block;
  }

  .product__productVideo--uploader--icon {
    font-size: 28px;
    color: #8c939d;
    width: 250px;
    height: 120px;
    line-height: 120px;
    text-align: center;
  }

  .product__productImg--uploader--icon {
    font-size: 28px;
    color: #8c939d;
    width: 100px;
    height: 100px;
    line-height: 80px;
    text-align: center;
  }

  .product__productImg--uploader--add {
    position: absolute;
    top: 55px;
    left: 20px;
    color: #7f7f7f;
  }

  .productImg-uploader-clear {
    position: absolute;
    right: -0.375rem;
    top: -0.375rem;
    font-size: 20px;
    cursor: pointer;
  }

  .productImg-text {
    position: absolute;
    right: 0;
    bottom: 0;
    font-size: 12px;
    text-align: center;
    width: 100%;
    background-color: rgba(0, 0, 0, 0.3);
    border-radius: 0px 0px 6px 6px;
    color: #ffffff;
  }

  .productImg-icon {
    position: absolute;
    right: -8px;
    top: -5px;
    font-size: 20px;
    text-align: center;
    color: #7f7f7f;
    background-color: white;
    border-radius: 50px;
  }

  .codeOne {
    display: flex;
    margin-left: 40px;
    align-items: center;
    margin-top: 20px;
  }

  .serveMsg {
    width: 400px;
    display: flex;
    // justify-content: space-between;
  }

  .shopMsg {
    display: flex;
    padding-top: 10px;
    // margin-left: 20px;
    &__right {
      margin-left: 20px;
      &--wed {
        display: flex;
        align-items: center;
        margin-top: 10px;
        height: 40px;
        &--text {
          margin-top: 10px;
          margin-right: 20px;
        }
      }
    }
  }

  .next {
    padding: 15px 0px;
    display: flex;
    justify-content: center;
    box-shadow: 0 0px 10px 0px #d5d5d5;
  }
}

.w-e-text-container {
  height: 532px !important; /*!important是重点，因为原div是行内样式设置的高度300px*/
}
</style>
