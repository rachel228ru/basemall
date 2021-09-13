<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:10
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-27 13:49:25
-->
<template>
  <div>
    <div class="shopBox" v-for="(item, index) in shopList" :key="index">
      <img :src="item.pic" alt="" class="shopBox__img" />
      <div class="shopBox__content">
        <div class="shopBox__content--shopName">{{ item.name }}</div>
        <!-- <div class="shopBox__content--shopSku" v-if="item.skuStocks.length > 1">
          <div
            v-for="(n, i) in item.skuStocks"
            :key="i"
            class="shopBox__content--shopSku--item"
          >{{ n.specs }}</div>
        </div>-->

        <div class="shopBox__content--price">
          <div class="shopBox__content--price--left">
            <span>
              评分:
              <span style="color:#FA6465">{{
                item.score ? item.score : 5
              }}</span>
            </span>
            <span>
              销量:
              <span style="color:#FA6465">{{ item.allSale }}</span>
            </span>
          </div>
          <div style="color:#FA6465">
            ￥{{ item.minPrice }}
            <span v-if="item.skuStocks.length > 1"
              >~ ￥{{ item.maxPrice }}</span
            >
          </div>
        </div>
        <div class="shopBox__content--shopName" style="margin-top:10px">
          商品状态：{{ item.status === 1 ? "已上架" : "已下架" }}
        </div>
      </div>
    </div>
    <div class="null" v-if="shopList.length == 0">暂无商品~</div>
    <PageManage
      :pageSize="pageSize"
      :pageNum="pageNum"
      :total="total"
      @handleSizeChange="handleSizeChange"
      @handleCurrentChange="handleCurrentChange"
    ></PageManage>
  </div>
</template>

<script lang="ts">
import { Vue, Component, Prop } from "vue-property-decorator";
import { ShowSupPro } from "@/api/good/goods";
import PageManage from "@/components/PageManage.vue";

@Component({
  components: {
    PageManage,
  },
})
export default class SupProduct extends Vue {
  @Prop({
    type: Object,
    default() {
      return null;
    },
  })
  readonly supProduct!: any;

  /** 分页 */
  pageSize = 10;

  /** 页码 */
  pageNum = 1;

  /** 总数 */
  total = 0;

  /** 商品数组 */
  shopList = [];

  mounted() {
    this.getShowList();
  }

  /** 获取供应商名下商品 */
  async getShowList() {
    const param = {
      size: this.pageSize,
      current: this.pageNum,
      providerId: this.supProduct.id,
    };
    const res = await ShowSupPro(param);
    res.data.list.forEach(
      (item: {
        allSale: number;
        skuStocks: any;
        minPrice: number;
        maxPrice: number;
      }) => {
        item.allSale = 0;
        const skuStocks = item.skuStocks;
        if (skuStocks.length === 1) {
          item.minPrice = skuStocks[0].price;
        }

        if (skuStocks.length > 1) {
          const priceList = skuStocks.map((v: { price: any }) => v.price);
          item.minPrice = Math.min(...priceList);
          item.maxPrice = Math.max(...priceList);
        }
        skuStocks.forEach((v: { sale: any }) => {
          item.allSale += v.sale;
        });
      },
    );
    this.shopList = res.data.list;
    this.total = res.data.total;
  }

  /**
   * @method handleSizeChange
   * @description 每页 条
   */
  handleSizeChange(val: number) {
    this.pageSize = val;
    this.getShowList();
  }

  /**
   * @method handleCurrentChange
   * @description 当前页
   */
  handleCurrentChange(val: number) {
    this.pageNum = val;
    this.getShowList();
  }
}
</script>

<style lang="scss" scoped>
@include b(shopBox) {
  border-top: 1px solid #f2f2f2;
  padding: 10px;
  display: flex;
  @include e(img) {
    width: 100px;
    height: 100px;
  }
  @include e(content) {
    width: 100%;
    margin-left: 20px;
    @include m(shopName) {
      padding-top: 10px;
      display: flex;
      justify-content: flex-start;
    }
    @include m(shopSku) {
      padding-top: 10px;
      display: flex;
      justify-content: flex-start;
      @include m(item) {
        width: 50px;
        height: 20px;
        background-color: #f2f2f2;
        margin-right: 20px;
        display: flex;
        justify-content: center;
        align-items: center;
      }
    }
    @include m(price) {
      display: flex;
      justify-content: space-between;
      margin-top: 20px;
      @include m(left) {
        display: flex;
        justify-content: space-between;
        align-items: flex-end;
        width: 120px;
      }
    }
  }
}
.null {
  width: 100%;
  text-align: center;
}
</style>
