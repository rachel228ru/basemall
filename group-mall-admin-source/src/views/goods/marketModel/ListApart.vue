<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:10
 * @LastEditors: vikingShip
 * @LastEditTime: 2021-08-26 10:28:10
-->
<template>
  <div>
    <!-- 顶部搜索条件 -->
    <Search @searchBy="getSearch" ref="Search" :changeId="changeId"></Search>
    <div class="topLine">
      <div class="topLine__left">
        <el-button size="mini" type="primary" @click="publishGoods"
          >发布商品</el-button
        >
        <!-- 设置分类 -->
        <SetClassify
          @command="commandVal"
          ref="setClass"
          :goodIds="goodIds"
          :showGetList="showGetList"
          @changeIds="getGoodList"
          style="margin-left: 20px"
          :is-item="false"
          :is-value="false"
          >设置分类</SetClassify
        >
      </div>
    </div>
    <!-- 商品列表 -->
    <GoodsList
      @goodId="getGoodId"
      ref="goodsList"
      @getShowProList="getShowProList"
      :changeId="changeId"
      v-if="changeId"
    ></GoodsList>
    <div class="listBottom">
      <!-- 设置分类 -->
      <SetClassify
        @command="commandVal"
        ref="setClass"
        :goodIds="goodIds"
        :showGetList="showGetList"
        @changeIds="getGoodList"
        :is-item="false"
      />
      <PageManage
        :pageSize="pageSize"
        :pageNum="pageNum"
        :total="total"
        @handleSizeChange="handleSizeChange"
        @handleCurrentChange="handleCurrentChange"
        style="margin-top: 0px"
      ></PageManage>
    </div>
  </div>
</template>

<script lang="ts">
import { Vue, Component, Prop, Watch, Ref } from "vue-property-decorator";

import GoodsList from "./components/GoodsList.vue";
import SetClassify from "./components/goodsComp/SetClassify.vue";
import Search from "./components/Search.vue";
import PageManage from "@/components/PageManage.vue";
import { ListApartState } from "./marketType";
import { GoodDetailInfo } from "./goodType";
import { SearchKeyType } from "./components/searchType";

import { GoodDel, GoodUpDown } from "@/api/good/goods";

@Component({
  components: {
    GoodsList,
    SetClassify,
    Search,
    PageManage,
  },
})
export default class ListApart extends Vue implements ListApartState {
  @Prop({})
  saleMode!: string;

  changeId = "";

  @Watch("saleMode")
  getNewModeId() {
    this.changeId = this.saleMode;
  }

  @Ref()
  readonly setClass!: SetClassify;

  /** 获取商品数组信息 */
  @Ref()
  readonly goodsList!: GoodsList;

  goodIds: Array<string> = [];

  showGetList: Array<GoodDetailInfo> = [];

  searchType = {} as SearchKeyType;

  pageSize = 0;

  pageNum = 0;

  total = 0;

  idList: Array<string | number> = [];

  /** 配送设置内容 */
  rections = {
    autoShippingState: 1 /** 设置新增提货点是否自动设置送货上门,0false=禁用,1true=启用 */,
    goodPointIdsPercentage: "",
  };

  mounted() {
    this.changeId = this.saleMode;
  }

  /**
   * 发布商品
   */
  publishGoods() {
    this.$router.push({
      name: "AddGood",
      query: { saleMode: this.changeId },
    });
  }

  /** 根据选择批量操作 */
  commandVal(val: string, type = false) {
    const list = (this.$refs.goodsList as GoodsList).tableCheckedItem;
    list.forEach(item => {
      this.goodIds.push(item.id as string);
    });
    if (this.goodIds.length === 0) {
      this.$message.error("请先选择商品");
      return;
    }
    if (type) {
      this.transApart(val);
      return;
    }
    switch (val) {
      case "8":
        this.delByIds();
        break;
      case "6":
      case "7":
        this.upDownGoods(val);
        break;
    }
  }

  /**
   * 批量删除
   */
  async delByIds() {
    try {
      await this.$confirm("确定要删除选中商品吗？", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      });
      const { code } = await GoodDel(this.goodIds, 0, {});
      if (code === 200) {
        this.$message.success("删除成功");
        this.getGoodList();
      }
    } catch (error) {
      console.log(error);
    }
  }

  /**
   * 批量上下架操作
   */
  async upDownGoods(val: string) {
    const list = this.goodsList.goodList;
    const hasNoType = list.some(item => item.productShowCategorys.length === 0);
    if (hasNoType) {
      this.$message.error("选择商品中有未编辑分类商品，请先编辑后在进行操作");
      return;
    }
    const { code } = await GoodUpDown(val === "6" ? 1 : 0, 0, this.goodIds);
    if (code === 200) {
      this.$message.success(`商品已${val === "6" ? "上架" : "下架"}`);
      this.getGoodList();
    }
  }

  /**
   * 获取选中商品ids数组
   */
  getGoodId(data: string[]) {
    this.goodIds = data;
  }

  /** 展示分类获取已选择的分类  */
  getShowProList(data: GoodDetailInfo[]) {
    this.showGetList = data || [];
    // this.goodsList = this.$refs.goodsList as GoodsList;
    this.total = this.goodsList.total;
    this.pageSize = this.goodsList.searchType.size as number;
    this.pageNum = this.goodsList.searchType.current as number;
  }

  /**
   * 获取商品列表
   */
  getGoodList() {
    this.goodsList.searchType = Object.assign(
      this.goodsList.searchType,
      this.searchType,
    );
    this.goodsList.getProduct();
  }

  /**
   * 获取搜索条件
   */
  getSearch(data: SearchKeyType) {
    this.searchType = data;
    this.searchType.current = 1;
    this.getGoodList();
  }

  /**
   * @method handleSizeChange
   * @description 每页 条
   */
  handleSizeChange(val: number) {
    this.goodsList.searchType.size = val;
    this.goodsList.getProduct();
  }

  /**
   * @method handleCurrentChange
   * @description 当前页
   */
  handleCurrentChange(val: number) {
    this.goodsList.searchType.current = val;
    this.goodsList.getProduct();
  }
}
</script>

<style lang="scss" scoped>
@import "../../../assets/styles/goods/index.scss";

.topLine {
  display: flex;
  justify-content: space-between;
  align-items: center;
  &__left {
    display: flex;
  }
  &__right {
    width: 450px;
    display: flex;
    justify-content: space-around;
  }
}

.listBottom {
  margin-top: 20px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  position: fixed;
  bottom: 10px;
  width: 990px !important;
  background-color: white;
  padding: 10px 0px;
  z-index: 10;
}
</style>
