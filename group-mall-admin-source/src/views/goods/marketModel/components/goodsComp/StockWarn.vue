<!--
 * @description: 
 * @Author: vikingShip
 * @Date: 2021-07-17 13:29:52
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-25 13:47:57
-->
<template>
  <div>
    <el-popover placement="top-start" trigger="hover">
      <div v-if="stock.stock === 0">商品已售罄</div>
      <div v-if="someStock && stock.skuStocks.length > 1 && stock.stock != 0">
        部分商品已售罄
      </div>
      <span slot="reference">
        <i
          class="el-icon-warning"
          v-if="stock.stock === 0 || someStock"
          style="color: #ff0000; margin-left: 5px"
        ></i>
      </span>
    </el-popover>
  </div>
</template>

<script lang="ts">
import { Vue, Component, Prop, Watch } from "vue-property-decorator";
import { GoodDetailInfo } from "@/views/goods/marketModel/goodType";
@Component
export default class StockWarn extends Vue {
  /** 库存信息 */
  @Prop({ type: Object })
  readonly stockItem!: GoodDetailInfo;

  @Watch("stockItem")
  getItem() {
    this.updatedStock();
  }

  /** 商品信息 */
  get stock() {
    return this.stockItem;
  }

  /** 部分商品是否售罄 */
  someStock = false;

  mounted() {
    this.someStock = false;
    this.updatedStock();
  }

  updatedStock() {
    this.someStock = this.stock.skuStocks.some(item => {
      return item.stock === 0;
    });
  }
}
</script>

<style lang="scss" scoped></style>
