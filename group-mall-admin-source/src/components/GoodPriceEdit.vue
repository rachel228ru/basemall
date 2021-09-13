<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:06
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-09-01 15:41:50
-->
<template>
  <!-- 编辑商品价格 -->
  <div class="goodPriceEdit">
    <div v-if="isOnePrice">
      <span @click="showInput" v-if="!inputVisible">
        <span class="goodPriceEdit__price">￥{{ minPrice }}</span>
        <i class="el-icon-edit" style="color: #2d8cf0"></i>
      </span>
      <el-input-number
        v-if="inputVisible"
        ref="savePriceInput"
        style="width: 120px"
        placeholder="请输入价格"
        @blur="hindInpt"
        v-model="editPrice"
        :min="0"
        :controls="false"
      ></el-input-number>
    </div>
    <div v-else>
      <el-popover v-model="popoverVisible" placement="bottom" trigger="click">
        <div v-for="item in skuStocksEdit" :key="item.id">
          <div style="margin-bottom: 10px">
            <span>{{ item.specs }}：</span>
            <el-input-number
              style="width: 120px"
              placeholder="请输入价格"
              v-model="item.price"
              :min="0"
              :controls="false"
            ></el-input-number>
          </div>
        </div>
        <div class="pop--button">
          <el-button @click="submitSkuStockPrice" type="primary" size="mini"
            >确认</el-button
          >
          <el-button @click="popoverVisible = false" size="mini"
            >取消</el-button
          >
        </div>
        <span slot="reference">
          <span class="goodPriceEdit__price"
            >￥{{ minPrice }}~￥{{ maxPrice }}</span
          >
          <i class="el-icon-edit" style="color: #2d8cf0"></i>
        </span>
      </el-popover>
    </div>
  </div>
</template>

<script lang="ts">
import { Vue, Component, Prop, Watch, Emit } from "vue-property-decorator";
import { ApiSkuType } from "@/views/goods/marketModel/goodType";
import {
  GoodPriceState,
  PickApiSkuType,
} from "./componentType/goodPriceEditType";
@Component
export default class GoodPriceEdit extends Vue implements GoodPriceState {
  name = "GoodPriceEdit";

  @Prop({
    type: Array,
    default() {
      return [];
    },
  })
  readonly skuStocks!: ApiSkuType[];

  inputVisible = false;

  popoverVisible = false;

  minPrice = 0;

  maxPrice = 0;

  editPrice = 0;

  /** 编辑的规格价格 */
  skuStocksEdit: Array<PickApiSkuType> = [];

  /** 是否有两个价格 */
  get isOnePrice() {
    return this.skuStocks.length === 1;
  }

  @Watch("popoverVisible")
  popoverVisibleChange(nVal: boolean) {
    if (nVal) {
      this.setSkuStocksEdit();
    }
  }

  @Watch("skuStocks")
  skuStocksChange() {
    this.initPrice();
  }

  mounted() {
    this.initPrice();
  }

  /**
   * 初始化价格
   */
  initPrice() {
    const skuStocks = this.skuStocks;
    if (skuStocks.length === 1) {
      this.minPrice =
        skuStocks[0].price === 0
          ? skuStocks[0].originalPrice
          : skuStocks[0].price;
      this.minPrice = this.minPrice ? Number(this.minPrice.toFixed(2)) : 0;
    }

    if (skuStocks.length > 1) {
      const priceList = skuStocks.map(item =>
        item.price !== 0 ? item.price : item.originalPrice,
      );
      this.minPrice = Math.min(...priceList);
      this.maxPrice = Math.max(...priceList);
      this.minPrice = this.minPrice ? Number(this.minPrice.toFixed(2)) : 0;
      this.maxPrice = this.maxPrice ? Number(this.maxPrice.toFixed(2)) : 0;
    }
  }

  /**
   * 设置需要编辑的规格价格
   */
  setSkuStocksEdit() {
    this.skuStocksEdit = this.skuStocks.map(item => {
      return {
        id: item.id,
        specs: item.specs,
        price: item.price,
        originalPrice: item.originalPrice,
      };
    });
  }

  /**
   * 显示输入框
   */
  showInput() {
    this.inputVisible = true;
    this.editPrice = this.minPrice;
    this.setSkuStocksEdit();
    this.$nextTick(() => {
      const savePriceInput = this.$refs.savePriceInput as HTMLFormElement;
      savePriceInput.$refs.input.focus();
    });
  }

  /**
   * 隐藏输入框
   */
  hindInpt() {
    this.inputVisible = false;
    const editPrice = this.editPrice;
    const skuStocksEdit = this.skuStocksEdit;
    if (this.minPrice === editPrice) {
      return;
    }
    if (!this.validatePrice(String(editPrice))) {
      this.$message.warning("请填写正确的价格");
      return;
    }

    if (!skuStocksEdit[0].originalPrice) {
      return;
    }
    if (editPrice > skuStocksEdit[0].originalPrice) {
      this.$message.warning("修改价格不能高于指导价");
      return;
    }
    skuStocksEdit[0].price = editPrice;
    this.changePrice();
  }

  /**
   * 提交商品规格价格
   */
  submitSkuStockPrice() {
    const skuStocksEdit = this.skuStocksEdit;
    const validFlag = skuStocksEdit.every(item => {
      return this.validatePrice(String(item.price));
    });

    if (!validFlag) {
      this.$message.warning("请填写正确的价格");
      return;
    }
    const validprice = skuStocksEdit.every(item => {
      if (!item.price) return false;
      if (!item.originalPrice) return false;
      return item.price <= item.originalPrice;
    });
    if (!validprice) {
      this.$message.warning("修改价格不能高于指导价");
      return;
    }
    const priceList = [];
    skuStocksEdit.forEach(item => {
      priceList.push(item.price);
    });
    this.changePrice();
    this.popoverVisible = false;
  }

  @Emit()
  changePrice() {
    const skuStocksEdit = this.skuStocksEdit;
    const temp = {} as PickApiSkuType;
    skuStocksEdit.forEach(item => {
      temp[item.id] = Number.parseFloat(String(item.price));
    });
    return temp;
  }

  /**
   * 校验价格是否正确
   */
  validatePrice(price: string) {
    const reg = /(^[1-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^(0){1}$)|(^[0-9]\.[0-9]([0-9])?$)/;
    return reg.test(price);
  }
}
</script>

<style lang="scss">
.goodPriceEdit {
  width: 150px;
  cursor: pointer;
  &__price {
    color: #ff7417;
  }
  i {
    margin-left: 4px;
    display: none;
  }
  &:hover {
    i {
      display: inline-block;
    }
  }
}
</style>
