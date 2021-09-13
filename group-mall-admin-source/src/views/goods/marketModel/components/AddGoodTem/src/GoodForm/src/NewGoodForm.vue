<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:10
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-09-01 15:47:16
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
        @changeLogisticsId="changeLogisticsId"
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
      <template v-if="currentStep === 'NewGoodDetailInfo'">
        <el-button @click="putAway" type="primary">上架</el-button>
        <el-button @click="cancelputAway">取消</el-button>
      </template>
    </div>
  </div>
</template>

<script lang="ts">
import { Vue, Component, Ref, Prop } from "vue-property-decorator";
import NewGoodBaseInfo from "./NewGoodBaseInfo.vue";
import NewGoodSellInfo from "./NewGoodSellInfo.vue";
import NewGoodDetailInfo from "./NewGoodDetailInfo.vue";
import AddGood from "../../../../../AddGood.vue";
import { StepIndicator, AddSubmitFormType, NewGoodState } from "./newGoodType";
import { editGood, updateGood, putWayGood } from "@/api/good/goods";
import storage from "@/libs/storage";
import { ApiSkuType } from "@/views/goods/marketModel/goodType";

Component.registerHooks(["beforeRouteLeave"]);

@Component({
  components: {
    NewGoodBaseInfo,
    NewGoodSellInfo,
    NewGoodDetailInfo,
  },
})
export default class NewGoodForm extends Vue implements NewGoodState {
  @Prop({
    type: String,
    default() {
      return "";
    },
  })
  from!: string;

  allFoorm = {} as AddSubmitFormType;

  /**
   * 商品添加步骤
   */
  stepIndicator: {
    [key: string]: StepIndicator;
  } = {
    NewGoodBaseInfo: {
      prev: "",
      next: "NewGoodSellInfo",
      setpIndex: 0,
    },
    NewGoodSellInfo: {
      prev: "NewGoodBaseInfo",
      next: "NewGoodDetailInfo",
      setpIndex: 1,
    },
    NewGoodDetailInfo: {
      prev: "NewGoodSellInfo",
      next: "",
      setpIndex: 2,
    },
  };

  currentStep = "NewGoodBaseInfo";

  /** 需要提交的数据 */
  goodFormModel = {
    albumPics: "",
    attribute: "",
    attributeId: "",
    distributionMode: 0,
    distributionSets: [],
    dominoState: 2,
    freightTemplateId: "",
    giftPoint: "0",
    id: null,
    limitType: 0,
    name: "",
    saleDescribe: "",
    pic: "",
    productAttributes: [],
    productShowCategorys: [],
    productSn: "",
    sale: "0",
    saleMode: "0",
    saleName: "",
    serviceIds: "",
    skuStocks: [],
    status: 1,
    unit: "克",
    videoUrl: "",
    weight: "",
  } as Partial<AddSubmitFormType>;

  goodDetail: AddSubmitFormType | null = null;

  logisticsId = "";

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

  changeLogisticsId() {
    this.logisticsId = "";
  }

  /**
   * 上一步
   */
  async handlePrev() {
    this.$nextTick(() => {
      this.$el.scrollTop = 0;
    });
    const res = await this.componentRef.getNewForm();
    Object.assign(this.allFoorm, res);
    const prevStep = this.prevStep;
    if (prevStep !== "") {
      this.currentStep = prevStep;
    }
  }

  /**
   * 下一步
   */
  async handleNext() {
    this.$nextTick(() => {
      this.$el.scrollTop = 0;
    });
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
    this.goodFormModel.distributionMode = 1;
    const goodId = this.$route.query.id || this.$route.params.id;
    if (!goodId) {
      return;
    }
    const { data } = await editGood(goodId);
    this.goodDetail = data;
    this.goodFormModel.id = Number(goodId);
  }

  async getGoodFormModel() {
    try {
      const res = await this.componentRef.getFormModel();
      Object.assign(this.goodFormModel, res);
      const goodFormModel = this.goodFormModel;
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
      if (Number(item.originalPrice) === 0) {
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
      if (this.prevStep === "NewGoodBaseInfo") {
        if (Number(item.price) === 0) {
          this.$message.error("实售价不能为0");
          this.checkFlag = false;
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
    if (this.currentStep === "NewGoodSellInfo") {
      if (this.goodFormModel.limitType === 1) {
        const isImg = skuStock.some(item => {
          return item.pic === "" || item.pic === undefined || item.pic === null;
        });
        if (isImg) {
          this.$message.error("请输入规格sku图");
          this.checkFlag = false;
          return;
        }
      }
    }
    if (this.prevStep === "NewGoodBaseInfo") {
      if (this.isWeight) {
        const isWeight = skuStock.some(v => {
          return Number(v.weight) === 0 || v.weight === "";
        });
        if (isWeight) {
          this.$message.error("请输入规格重量");
          this.checkFlag = false;
          return;
        }
      }
      const skuItem = skuStock.some(
        item => item.stock === "0" || !regs.test(String(item.stock)),
      );
      if (skuItem) {
        this.$message.error("请输入大于0库存");
        this.checkFlag = false;
        return;
      }
      skuStock.forEach(item => {
        if (item.stock === "0") {
          this.$message.error("请输入大于0库存");
          this.checkFlag = false;
          return;
        }
        if (!regs.test(String(item.stock))) {
          this.$message.error("请输入大于0库存");
          this.checkFlag = false;
          return;
        }
      });
    }
  }

  /**
   * 上架
   */
  async putAway() {
    const parentHtml = this.$parent.$parent.$parent.$parent as AddGood;
    parentHtml.saveFlag = true;
    await this.getGoodFormModel();
    storage.delete("formModel");
    const goodFormModel = this.goodFormModel;
    goodFormModel.status = 1;
    goodFormModel.place = 0;
    if (goodFormModel.skuStocks) {
      const hasSome = goodFormModel.skuStocks.some(cItem => {
        return cItem.memberType === 3;
      });
      if (hasSome) {
        goodFormModel.skuStocks.map(sItem => {
          return (sItem.memberType = 3);
        });
      }
    }
    const saleMode = parentHtml.saleMode as string;
    const res = goodFormModel.id
      ? await updateGood(goodFormModel)
      : await putWayGood(goodFormModel);

    if (res.code === 200) {
      this.$message.success(`${goodFormModel.id ? "编辑成功" : "上架成功"}`);
      this.$router.push({
        name: this.from === "csv" ? "csvList" : "Goods",
        params: { saleMode: this.from === "csv" ? "" : saleMode },
      });
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
  // height: 740px;
  overflow: auto;
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
    // overflow: auto;
    // overflow-x: hidden;
    // height: 800px;
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
}

.goodForm::-webkit-scrollbar {
  display: none;
}

.next {
  width: 1010px;
  position: fixed;
  bottom: 10px;
  padding: 15px 0px;
  display: flex;
  justify-content: center;
  box-shadow: 0 0px 10px 0px #d5d5d5;
  background-color: white;
  margin-left: -14px;
  z-index: 100;
}

.w-e-text-container {
  height: 532px !important; /*!important是重点，因为原div是行内样式设置的高度300px*/
}
</style>
