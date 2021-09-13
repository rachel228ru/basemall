<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:10
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-09-01 15:47:19
-->
<template>
  <div class="pageRoll">
    <el-form
      :model="formModel"
      :rules="formRules"
      ref="formRef"
      label-width="100px"
    >
      <div class="navLine">规格设置</div>
      <div class="spec">规格类型</div>
      <div style="margin-left: 91px; margin-top: -12px; margin-bottom: 10px">
        <el-radio v-model="formModel.limitType" :label="0" class="spec__left"
          >统一规格</el-radio
        >
      </div>
      <div v-if="radioMore">
        <el-radio
          v-model="formModel.limitType"
          :label="1"
          style="margin-left: 100px"
          >多规格</el-radio
        >
      </div>
      <div v-if="!radioMore">
        <el-radio
          v-model="formModel.limitType"
          :label="2"
          style="margin-left: 100px"
          >多规格</el-radio
        >
      </div>
      <div v-if="formModel.limitType == 0">
        <el-form-item
          label="指导价"
          prop="originalPrice"
          style="margin-top: 10px"
        >
          <el-input-number
            :min="0"
            :controls="false"
            v-model="formModel.originalPrice"
            placeholder="0"
            style="width: 500px"
            class="input_number"
          ></el-input-number>
        </el-form-item>
        <el-form-item label="实售价" prop="price">
          <el-input-number
            :min="0"
            :controls="false"
            v-model="formModel.price"
            placeholder="0"
            style="width: 500px"
            class="input_number"
          ></el-input-number>
        </el-form-item>
        <el-form-item label="商品库存" prop="stock">
          <el-input-number
            :step="1"
            v-model="formModel.stock"
            placeholder="0"
            style="width: 500px"
            :min="0"
            step-strictly
            :controls="false"
            :precision="0"
            class="input_number"
          ></el-input-number>
        </el-form-item>
        <el-form-item label="商品限购">
          <el-input-number
            :step="1"
            v-model="formModel.perLimit"
            placeholder="0"
            style="width: 500px"
            :min="0"
            step-strictly
            :controls="false"
            :precision="0"
            class="input_number"
          ></el-input-number>
        </el-form-item>
        <el-form-item label="商品重量" v-if="isWeight" prop="weight">
          <el-input-number
            :min="0"
            :controls="false"
            v-model="formModel.weight"
            placeholder="0"
            style="width: 500px"
          ></el-input-number>
        </el-form-item>
      </div>
      <div v-if="formModel.limitType !== 0">
        <div class="specitem">
          <el-radio
            v-model="formModel.limitType"
            :label="1"
            @change="radioMore = true"
            >统一限购</el-radio
          >
          <div v-if="formModel.limitType !== 2" style="margin-left: 20px">
            <div class="specitem__inp">
              限购
              <el-input-number
                :min="0"
                :step="1"
                step-strictly
                :controls="false"
                class="specitem__inp--inside"
                v-model="unifyLimitNum"
                style=" ;"
              ></el-input-number
              >件
            </div>
            <div class="valueName" style="width: 700px">
              <div class="flex" style="justify-content: space-between">
                <div style="width: 110px">规格值</div>
                <div style="width: 60px">指导价</div>
                <div style="width: 60px">实售价</div>
                <div style="width: 90px">库存</div>
                <div style="width: 60px" v-if="isWeight">重量</div>
                <div style="width: 40px">sku图</div>
                <div style="width: 30px">操作</div>
              </div>
              <div class="valueName__noList" v-if="!showAttributes">
                暂无数据~
              </div>
              <div
                class="valueName__List middle"
                v-for="(item, index) in formModel.skuStocks"
                :key="index"
                v-else
              >
                <el-input
                  v-model="item.specs"
                  style="width: 120px"
                  maxlength="20"
                ></el-input>
                <el-input-number
                  :min="0"
                  :controls="false"
                  v-model="item.originalPrice"
                  style="width: 70px"
                ></el-input-number>
                <el-input-number
                  :min="0"
                  :controls="false"
                  v-model="item.price"
                  style="width: 70px"
                ></el-input-number>
                <el-input-number
                  :min="0"
                  step-strictly
                  :step="1"
                  :controls="false"
                  v-model="item.stock"
                  style="width: 100px"
                ></el-input-number>
                <el-input-number
                  :min="0"
                  :controls="false"
                  v-model="item.weight"
                  style="width: 70px"
                  v-if="isWeight"
                ></el-input-number>
                <el-upload
                  class="productImg__uploader product__productImg"
                  :auto-upload="false"
                  :on-change="uploadSkuImg"
                  action
                  :show-file-list="false"
                  style="width: 50px; height: 50px"
                >
                  <i
                    style="width: 50px; height: 50px; line-height: 50px"
                    class="el-icon-plus product__productImg--uploader--icon"
                    @click="productSkuIndex = index"
                    v-if="item.pic === ''"
                  ></i>
                  <img
                    :src="item.pic"
                    alt=""
                    style="width: 50px; height: 50px"
                    @click="productSkuIndex = index"
                    v-else
                  />
                </el-upload>
                <div @click="formModel.skuStocks.splice(index, 1)">删除</div>
              </div>
              <el-button
                type="primary"
                plain
                style="margin-top: 10px"
                size="small"
                @click="addSku"
                >+ 添加规格</el-button
              >
            </div>
          </div>
        </div>
        <div class="specitem">
          <el-radio
            v-model="formModel.limitType"
            :label="2"
            class="specitem__inp"
            @change="radioMore = false"
            >规格限购</el-radio
          >
          <div v-if="formModel.limitType === 2" style="margin-left: 20px">
            <div class="valueName" style="width: 700px">
              <div class="flex" style="justify-content: space-between">
                <div style="width: 150px">规格值</div>
                <div style="width: 100px">指导价</div>
                <div style="width: 100px">实售价</div>
                <div style="width: 100px">库存</div>
                <div style="width: 100px">限购</div>
                <div style="width: 100px" v-if="isWeight">重量</div>
                <div style="width: 70px">sku图</div>
                <div style="width: 40px">操作</div>
              </div>
              <div class="valueName__noList" v-if="!showAttributes">
                暂无数据~
              </div>
              <div
                class="valueName__List middle"
                v-for="(item, index) in formModel.skuStocks"
                :key="index"
                v-else
              >
                <el-input
                  v-model="item.specs"
                  style="width: 110px"
                  maxlength="20"
                ></el-input>
                <el-input-number
                  :min="0"
                  :controls="false"
                  v-model="item.originalPrice"
                  style="width: 70px"
                ></el-input-number>
                <el-input-number
                  :min="0"
                  :controls="false"
                  v-model="item.price"
                  style="width: 70px"
                ></el-input-number>
                <el-input-number
                  :step="1"
                  :min="0"
                  step-strictly
                  :controls="false"
                  v-model="item.stock"
                  style="width: 70px"
                ></el-input-number>
                <el-input-number
                  :step="1"
                  :min="0"
                  step-strictly
                  :controls="false"
                  v-model="item.perLimit"
                  style="width: 70px"
                ></el-input-number>
                <el-input-number
                  :min="0"
                  :controls="false"
                  v-model="item.weight"
                  style="width: 70px"
                  v-if="isWeight"
                ></el-input-number>
                <el-upload
                  class="productImg__uploader product__productImg"
                  :auto-upload="false"
                  :on-change="uploadSkuImg"
                  action
                  :show-file-list="false"
                  style="width: 50px; height: 50px"
                >
                  <i
                    style="width: 50px; height: 50px; line-height: 50px"
                    class="el-icon-plus product__productImg--uploader--icon"
                    @click="productSkuIndex = index"
                    v-if="item.pic === ''"
                  ></i>
                  <img
                    :src="item.pic"
                    alt
                    v-else
                    @click="productSkuIndex = index"
                    style="width: 50px; height: 50px"
                  />
                </el-upload>
                <div @click="formModel.skuStocks.splice(index, 1)">删除</div>
              </div>
              <el-button
                type="primary"
                plain
                style="margin-bottom: 20px"
                size="small"
                @click="addSku"
                >+ 添加属性</el-button
              >
            </div>
          </div>
        </div>
      </div>
      <div class="navLine">其他信息</div>
      <el-form-item label="初始销量">
        <el-input-number
          :step="1"
          v-model="formModel.sale"
          placeholder="0"
          style="width: 500px"
          :min="0"
          step-strictly
          :controls="false"
          class="input_number"
        ></el-input-number>
      </el-form-item>
      <el-form-item label="服务保障">
        <div v-for="item in serviceAssuranceList" :key="item.name">
          <el-checkbox v-model="item.state">
            <div class="serveMsg">
              <span style="width: 120px">{{ item.name }}</span>
              <span style="color: #c6c6c6">{{ item.text }}</span>
            </div>
          </el-checkbox>
        </div>
      </el-form-item>
    </el-form>
  </div>
</template>

<script lang="ts">
import { Vue, Component, Ref, Prop, Watch } from "vue-property-decorator";
import NewGoodForm from "./NewGoodForm.vue";
import { NewGoodSellInfoState, NewGoodSellFrom } from "./newGoodSellInfoType";
import { ApiSkuType } from "@/views/goods/marketModel/goodType";
import { upLoad } from "@/api/index";
import storage from "@/libs/storage";
import { ElForm } from "element-ui/types/form";
import { ElUploadInternalFileDetail } from "element-ui/types/upload";

const checkPriceField = (message: string) => {
  return (_rule: any, value: string, callback: (arg?: Error) => void) => {
    if (!value) {
      return callback(new Error(message));
    }
    const priceReg = /(^[1-9]\d*(\.\d{1,2})?$)|(^0(\.\d{1,2})?$)/;

    if (!priceReg.test(value)) {
      return callback(new Error("请输入正确的价格"));
    }
    callback();
  };
};

const checkNumberField = (message?: string) => {
  return (_rule: any, value: string, callback: (arg?: Error) => void) => {
    if (!value) {
      return message ? callback(new Error(message)) : callback();
    }

    if (!Number.isSafeInteger(value)) {
      return callback(new Error("请输入正确的数字"));
    }
    callback();
  };
};

/**
 * 编辑销售信息
 */
@Component({})
export default class NewGoodSellInfo extends Vue
  implements NewGoodSellInfoState {
  @Prop({
    type: Object,
    default() {
      return null;
    },
  })
  readonly goodDetail!: NewGoodSellFrom;

  @Prop({
    type: String,
    default() {
      return "";
    },
  })
  logisticsId!: string;

  @Watch("logisticsId")
  watchLogisticsId() {
    this.getLogMsg();
  }

  @Watch("formModel.skuStocks")
  getShowAttributes() {
    this.showAttributes = this.formModel.skuStocks
      ? this.formModel.skuStocks.length > 0
        ? true
        : false
      : false;
  }

  radioMore = true;

  showAttributes = false;

  formModel = {} as NewGoodSellFrom;

  isWeight = false;

  formRules = {
    originalPrice: [
      {
        required: true,
        message: "请输入商品指导价",
        trigger: "blur",
        validator: checkPriceField("请输入商品指导价"),
      },
    ],
    price: [
      {
        required: true,
        message: "请输入商品实售价",
        trigger: "blur",
        validator: checkPriceField("请输入商品实售价"),
      },
    ],
    stock: [{ required: true, message: "请输入商品库存", trigger: "blur" }],
    checkNumberField: [{ trigger: "blur", validator: checkNumberField() }],
    weight: [{ required: true, message: "请输入商品重量", trigger: "blur" }],
  };

  rulevalidate(callback: () => void) {
    callback();
    return;
  }

  productSkuIndex = 0;

  unifyLimitNum = "0";

  normsList: Array<ApiSkuType> = [];

  serviceAssuranceList = [
    {
      name: "全场包邮",
      state: false,
      text: "所有商品均无条件包邮",
    },
    {
      name: "7天退换",
      state: false,
      text: "商家承诺7天无理由退换货",
    },
    {
      name: "48小时发货",
      state: false,
      text: "商家承诺订单在48小时内发布",
    },
    {
      name: "假一赔十",
      state: false,
      text: "若收到商品是假冒品牌，可获得十倍赔偿",
    },
    {
      name: "正品保证",
      state: false,
      text: "商家承诺商品正品质量",
    },
  ];

  @Ref()
  readonly formRef!: ElForm;

  mounted() {
    if (storage.get("allFoorm")) {
      this.formModel = JSON.parse(JSON.stringify(storage.get("allFoorm")));
      // storage.delete("formModel");
    } else {
      this.getModel();
    }
    this.setFormModel();
    this.getLogMsg();
  }

  getModel() {
    if (!this.goodDetail) {
      this.formModel = JSON.parse(JSON.stringify(this.$parent.goodFormModel));
      this.formModel.skuStocks = [
        {
          specs: "",
          originalPrice: 0,
          price: 0,
          stock: 0,
          perLimit: 0,
          pic: "",
          sale: "0",
          weight: "0",
          id: "",
          lowStock: "",
          memberPrices: [],
          skuRebates: [],
          productId: "",
          skuCode: "",
        },
      ];
    }
  }

  /**
   * 获取物流模板
   */
  getLogMsg() {
    const parentHtml = this.$parent as NewGoodForm;
    this.isWeight = parentHtml.isWeight;
  }

  /**
   * 还原表单数据
   */
  setFormModel() {
    let goodDetail = this.goodDetail;
    const parentHtml = this.$parent as NewGoodForm;
    if (storage.get("allFoorm") !== null) {
      if (Object.keys(storage.get("allFoorm")).length !== 0) {
        goodDetail = storage.get("allFoorm");
      }
    }
    if (goodDetail) {
      const formModel = goodDetail;
      for (const key of Object.keys(formModel)) {
        const value = goodDetail[key];
        if (value) {
          formModel[key] = value;
        }
      }
      formModel.weight = goodDetail.weight;
      formModel.skuStocks = goodDetail.skuStocks;
      this.setServiceAssuranceList(goodDetail.serviceIds);
      if (goodDetail.limitType === 0 && goodDetail.skuStocks.length > 0) {
        const skuStockItem = formModel.skuStocks[0];
        formModel.originalPrice = skuStockItem.originalPrice
          ? skuStockItem.originalPrice
          : null;
        formModel.price = skuStockItem.price ? skuStockItem.price : null;
        formModel.stock = skuStockItem.stock as number;
        formModel.perLimit = skuStockItem.perLimit
          ? skuStockItem.perLimit
          : null;
        formModel.weight = skuStockItem.weight ? skuStockItem.weight : "";
      }
      this.formModel = JSON.parse(JSON.stringify(formModel));
    } else {
      this.formModel = JSON.parse(JSON.stringify(parentHtml.goodFormModel));
    }
  }

  getNewForm() {
    if (this.formModel.limitType === 0) {
      const temStock = this.formModel.skuStocks;
      this.formModel.skuStocks = [];
      this.formModel.skuStocks.push({
        originalPrice: this.formModel.originalPrice
          ? this.formModel.originalPrice
          : null,
        price: this.formModel.price ? this.formModel.price : null,
        stock: this.formModel.stock,
        weight: this.formModel.weight,
        perLimit: this.formModel.perLimit,
        id: temStock[0] && temStock[0].id ? temStock[0].id : "",
        lowStock: 0,
        memberPrices: temStock[0] ? temStock[0].memberPrices : [],
        skuRebates: temStock[0] ? temStock[0].skuRebates : [],
        memberType: temStock[0] ? temStock[0].memberType : null,
        rebateType: temStock[0] ? temStock[0].rebateType : null,
        productId: temStock[0].productId,
      });
    }
    this.formModel.serviceIds = this.getServiceIds();
    return JSON.parse(JSON.stringify(this.formModel));
  }

  async getFormModel() {
    try {
      await this.validate();
      await this.getSkuStocks();
      const formModel = this.formModel;
      return {
        unit: "克", // 商品重量
        weight: "0", // 商品长度
        skuStocks: formModel.skuStocks,
        sale: formModel.sale,
        serviceIds: this.getServiceIds(),
        limitType: formModel.limitType,
      };
    } catch (error) {
      return Promise.reject(error);
    }
  }

  /**
   * 获取商品sku
   */
  getSkuStocks() {
    const limitType = this.formModel.limitType === 0;
    if (limitType) {
      const formModel = this.formModel as any;
      const skuItem = {
        specs: "",
        originalPrice: formModel.originalPrice,
        price: formModel.price,
        stock: formModel.stock,
        perLimit: formModel.perLimit,
        pic: formModel.pic,
        // sale: formModel.sale,
        id:
          formModel.skuStocks[0] && formModel.skuStocks[0].id
            ? formModel.skuStocks[0].id
            : "",
        lowStock: formModel.lowStock,
        memberPrices: formModel.skuStocks[0]
          ? formModel.skuStocks[0].memberPrices
          : [],
        skuRebates: formModel.skuStocks[0]
          ? formModel.skuStocks[0].skuRebates
          : [],
        memberType: formModel.skuStocks[0]
          ? formModel.skuStocks[0].memberType
          : null,
        rebateType: formModel.skuStocks[0]
          ? formModel.skuStocks[0].rebateType
          : null,
        productId: formModel.productId,
        skuCode: "",
        weight: formModel.weight,
      };
      this.formModel.skuStocks = [skuItem];
    }

    if (!limitType) {
      //   const normsList = this.normsList;
      const isUnifyLimit = this.formModel.limitType === 1; // 是否为统一限购
      const unifyLimitNum = this.unifyLimitNum;
      this.formModel.skuStocks = this.formModel.skuStocks.map(item => {
        return {
          ...item,
          perLimit: isUnifyLimit ? unifyLimitNum : item.perLimit,
        };
      });
      //   return skuStocks;
    }
  }

  /**
   * 对整个表单进行校验
   */
  validate() {
    return this.formModel.limitType === 0
      ? this.formRef.validate()
      : this.validateNormsList();
  }

  validateNormsList() {
    // const isUnifyLimit = this.formModel.limitType === 1; // 是否为统一限购
    // const unifyLimitNum = this.unifyLimitNum;
    // if (isUnifyLimit && !unifyLimitNum) {
    //   return Promise.reject(false);
    // }

    const skuStocks = this.formModel.skuStocks;
    let flag = skuStocks.every(item => {
      const { specs, originalPrice, price, stock } = item;
      return specs && originalPrice && price && stock;
    });

    if (flag && skuStocks.length > 0) {
      return true;
    } else {
      return Promise.reject(false);
    }
  }

  /**
   * 移除表单项的校验结果
   */
  clearValidate() {
    this.formRef.clearValidate();
  }

  /**
   * 获取服务保障
   */
  getServiceIds(): string {
    return this.serviceAssuranceList
      .filter(item => item.state)
      .map(item => item.name)
      .join(",");
  }

  /**
   * 还原服务保障
   */
  setServiceAssuranceList(serviceIds = "") {
    if (serviceIds) {
      this.serviceAssuranceList.forEach(item => {
        item.state = serviceIds.indexOf(item.name) !== -1;
      });
    }
  }

  /**
   * 添加规格
   */
  addSku() {
    this.formModel.skuStocks.push({
      specs: "",
      originalPrice: 0,
      price: 0,
      stock: "0",
      perLimit: "0",
      pic: "",
      sale: "0",
      weight: "0",
      id: "",
      lowStock: "",
      memberPrices: [],
      skuRebates: [],
      productId: "",
      skuCode: "",
    });
  }

  /**
   * 上传sku
   */
  async uploadSkuImg(file: ElUploadInternalFileDetail) {
    const whiteList = ["image/jpeg", "image/jpg", "image/png"];

    const isLt1M = file.size < 2 * 1024 * 1024;
    if (!whiteList.includes(file.raw.type)) {
      this.$message.error("上传文件只能是 JPG或PNG 格式!");
      return;
    }
    if (!isLt1M) {
      this.$message.error("上传文件大小不能超过 2MB!");
      return;
    }

    const res = await upLoad({
      file: file.raw,
    });
    this.formModel.skuStocks[this.productSkuIndex].pic = res.data;
  }
}
</script>
<style lang="scss">
.input_number {
  .el-input__inner {
    text-align: left;
  }
}
</style>
