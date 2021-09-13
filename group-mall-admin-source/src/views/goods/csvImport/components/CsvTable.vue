<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:10
 * @LastEditors: vikingShip
 * @LastEditTime: 2021-08-25 15:12:56
-->
<template>
  <div>
    <el-table
      ref="multipleTable"
      :data="goodList"
      tooltip-effect="dark"
      style="width: 100%;margin-top:20px"
      @selection-change="handleSelectionChange"
      :header-cell-style="{
        background: '#f6f8fa',
        'font-weight': 'normal',
        color: 'black',
      }"
      :row-style="{ height: '50px' }"
    >
      <template slot="empty">
        <div class="emptyLine">
          暂无数据~
        </div>
      </template>
      <el-table-column type="selection" width="50"></el-table-column>
      <el-table-column label="商品素材" width="450">
        <template slot-scope="scope">
          <div class="goodBox">
            <img :src="scope.row.pic" class="goodBox__imgStyle" />
            <div class="goodBox__content">
              <div class="goodBox__content--name">{{ scope.row.name }}</div>
              <a
                :href="scope.row.csvUrl"
                target="view_window"
                class="goodBox__content--link"
                >{{ scope.row.csvUrl }}</a
              >
              <div class="goodBox__content--price">
                {{ scope.row.goodPrice }}
              </div>
            </div>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="导入时间" width="300">
        <template slot-scope="scope">{{ scope.row.createTime }}</template>
      </el-table-column>
      <el-table-column label="操作" width="180">
        <template slot-scope="scope">
          <div class="dealWay">
            <span style="color:#2D8CF0" @click="edit(scope.row)"
              >编辑并上架</span
            >
            <span style="color:#FA4E74" @click="delCsvItem(scope.row)"
              >删除</span
            >
          </div>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script lang="ts">
import { Vue, Component, Prop, Watch } from "vue-property-decorator";
import { GoodDetailInfo } from "@/views/goods/marketModel/goodType";
interface NewGoodDetailInfo extends GoodDetailInfo {
  goodPrice: string;
}
@Component
export default class extends Vue {
  @Prop({
    type: Array,
    default() {
      return [];
    },
  })
  csvList!: NewGoodDetailInfo[];

  @Watch("csvList")
  watchCsvList() {
    this.goodList = JSON.parse(JSON.stringify(this.csvList));
    this.goodList.forEach(item => {
      if (item.skuStocks.length === 1 && item.skuStocks.length>0 && item.skuStocks[0].originalPrice) {
        item.goodPrice = `￥${item.skuStocks[0].originalPrice.toFixed(2)}`;
      } else {
        const priceList = item.skuStocks.map(item => Number(item.originalPrice));
        item.goodPrice = `￥${Math.min(...priceList).toFixed(2)} ~ ￥${Math.max(
          ...priceList,
        ).toFixed(2)}`;
      }
    });
  }

  goodList: Array<NewGoodDetailInfo> = [];

  idList: number[] = [];

  /**
   * 选择商品
   */
  handleSelectionChange(val:NewGoodDetailInfo[]) {
    this.idList = [];
    val.forEach(item => {
      this.idList.push(Number(item.id));
    });
    this.$emit("chooseId", this.idList);
  }

  edit(item:NewGoodDetailInfo) {
    this.$router.push({
      name: "editGood",
      params: { id:String(item.id), from: "csv" },
      query: { from:"csv" }
    });
  }

  /**
   * 删除单个商品
   */
  delCsvItem(item:NewGoodDetailInfo) {
    this.$emit("delChoose", item.id);
  }
}
</script>

<style lang="scss" scoped>
@include b(goodBox) {
  display: flex;
  align-items: center;
  @include e(imgStyle) {
    width: 60px;
    height: 60px;
  }
  @include e(content) {
    margin-left: 20px;
    padding-top: 4px;
    @include m(name) {
      font-size: 14px;
      font-family: Hiragino Sans GB;
      color: rgba(89, 88, 88, 1);
      width: 280px;
      overflow: hidden;
      white-space: nowrap;
      text-overflow: ellipsis;
    }
    @include m(link) {
      text-decoration: none;
      color: #2e99f3;
      font-size: 14px;
      cursor: pointer;
    }
    @include m(price) {
      font-size: 14px;
      color: #f61c1c;
    }
  }
}

.dealWay {
  display: flex;
  cursor: pointer;
  width: 100px;
  justify-content: space-between;
}

.emptyLine {
  width: 100%;
  height: 80px;
  background-color: white;
  display: flex;
  -webkit-box-align: center;
  -ms-flex-align: center;
  align-items: center;
  -webkit-box-pack: center;
  justify-content: center;
  margin-left: -15px;
  font-size: 14px;
  color: #b3b3b3;
}
</style>
