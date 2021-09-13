<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:06
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-27 11:08:25
-->
<template>
  <!-- 商超商品 -->
  <div>
    <div class="search-wrap">
      <el-button @click="onClear">刷新</el-button>
      <el-select
        v-model="showVal"
        style="width:120px;"
        placeholder="全部专区"
        @clear="onClear"
        clearable
      >
        <el-option-group v-for="group in showList" :key="group.index">
          <el-option
            :label="group.modeName"
            :value="group.modeName"
            @click.native="selectTem(group)"
          ></el-option>
          <!-- <el-option
            v-for="item in group.showCategoryVos"
            @click.native="selectTem(item)"
            :key="item.name"
            :label="item.name"
            :value="item.name"
            style="margin-left:20px"
          ></el-option> -->
        </el-option-group>
      </el-select>
      <el-select
        v-model="showSecVal"
        placeholder="请选择分类"
        style="width:120px;"
      >
        <el-option-group v-for="group in showSecList" :key="group.index">
          <el-option-group
            :label="group.name"
            :value="group.name"
            :key="group.id"
          ></el-option-group>
          <el-option
            v-for="item in group.showCategoryVos"
            :key="item.name"
            :label="item.name"
            :value="item.name"
            style="margin-left:20px"
            @click.native="selectGood(item)"
          ></el-option>
        </el-option-group>
      </el-select>
      <div class="demo-input-suffix">
        价格：
        <el-input
          placeholder="最小值"
          v-model="search.minPrice"
          class="mr10"
          type="number"
        ></el-input>
        <span>-</span>
        <el-input
          placeholder="最大值"
          v-model="search.maxPrice"
          class="ml10"
          type="number"
        ></el-input>
      </div>
      <el-input
        v-model="search.name"
        placeholder="商品名称查询"
        class="search-wrap-input"
        @click="onClear"
        clearable
      >
        <el-button
          slot="append"
          icon="el-icon-search"
          @click="searchByInput"
        ></el-button>
      </el-input>
    </div>
    <el-table v-loading="loading" :data="tableData" height="369">
      <el-table-column label="商品名称" prop="name"></el-table-column>
      <el-table-column label="价格">
        <template slot-scope="scope">
          <div v-if="scope.row.limitType === 0">
            <span v-if="scope.row.minPrice > 0"
              >￥{{ scope.row.minPrice }}</span
            >
            <span v-else>￥{{ scope.row.maxPrice }}</span>
          </div>
          <div v-else>
            ￥{{ scope.row.minPrice }}~￥{{ scope.row.maxPrice }}
          </div>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="100px">
        <template slot-scope="scope">
          <el-radio
            v-model="selectId"
            @change="selectHandle"
            :label="scope.row.id"
          >
            <span></span>
          </el-radio>
        </template>
      </el-table-column>
    </el-table>
    <PageManage
      :pageSize="pageSize"
      :pageNum="pageNum"
      :total="total"
      @handleSizeChange="handleSizeChange"
      @handleCurrentChange="handleCurrentChange"
    />
  </div>
</template>

<script lang="ts">
import { Vue, Component, Prop, PropSync, Watch } from "vue-property-decorator";
import PageManage from "@/components/PageManage.vue";
import { getCouponsGoods } from "@/api/couponActivity/coupon";
import { getAllRegionList, getList } from "@/api/good/goods";
import LinkSelectItem, { typeNameMap } from "./LinkSelectItem";
/** 商超商品 */
@Component({
  components: {
    PageManage,
  },
})
export default class Goods extends Vue {
  @PropSync("link", {
    type: Object,
    default: () => {
      return {
        id: null,
        type: null,
        name: "",
        url: "",
        append: "",
      };
    },
  })
  linkSelectItem!: LinkSelectItem;

  @Prop({
    type: Boolean,
  })
  visible = false;

  name = "Goods";

  selectId = 0;

  loading = false;

  tableData: LinkSelectItem[] = [];

  pageNum = 1;

  pageSize = 20;

  total = 0;

  /** 分类选择 */
  showList: any[] = [];

  /** 默认选择状态 */
  showVal = "";

  showSecList: any[] = [];

  /** 二级分类 */
  showSecVal = "";

  saleMode = "";

  search = {
    maxPrice: "",
    minPrice: "",
    showCategoryId: "",
    name: "",
    saleMode: "",
  };

  mounted() {
    this.getTableData();
    getAllRegionList({}).then(res => {
      this.showList = res.data;
      this.saleMode = String(res.data[0].id);
    });
  }

  async getUseEffectiveCategory() {
    const res = await getList({ saleMode: this.saleMode });
    this.showSecList = res.data.list;
    this.getTableData();
  }

  // 监听父弹窗显隐
  @Watch("visible")
  handleVisibleChange() {
    this.getTableData();
  }

  /**
   * 获取商品
   */
  private async getTableData() {
    let param = {
      current: this.pageNum,
      size: this.pageSize,
    } as any;
    param = Object.assign(this.search, param);
    if (Number(param.maxPrice) < Number(param.minPrice)) {
      this.$message({
        showClose: true,
        message: "请填写正确价格区间",
        type: "warning",
      });
      return;
    }
    this.loading = true;
    try {
      const { data } = await getCouponsGoods(param);
      if (data) {
        const goodsList = data.list || [];
        this.tableData = goodsList
          // .filter(item => {
          //   const stock = item.skuStocks.some(sku => sku.stock > 0);
          //   return stock && item.status === 1 && item.saleMode === 0;
          // })
          .map(item => {
            return {
              id: Number(item.id),
              type: 1,
              name: item.name,
              url: "/subcontract/pages/detail/detail",
              append: "",
              limitType: item.limitType,
              maxPrice: item.maxPrice,
              minPrice: item.minPrice,
            };
          });
        this.total = data.total;
        // 初始相同设为选中状态
        if (this.sameJudge()) {
          this.selectId = +this.linkSelectItem.id;
        }
      }
    } catch (error) {
      console.log(error);
    }
    this.loading = false;
  }

  handleSizeChange(val: number) {
    this.pageNum = 1;
    this.pageSize = val;
    this.getTableData();
  }

  handleCurrentChange(val: number) {
    this.pageNum = val;
    this.getTableData();
  }

  selectHandle() {
    const selectId = this.selectId;
    const tableData = JSON.parse(JSON.stringify(this.tableData));
    const currentItem: LinkSelectItem = tableData.find(
      (item: { id: number }) => item.id === selectId,
    );
    Object.assign(this.linkSelectItem, currentItem);
  }

  sameJudge(): boolean {
    return (
      typeNameMap[this.linkSelectItem.type] &&
      typeNameMap[this.linkSelectItem.type].name === this.name
    );
  }

  /**
   * 选择输入框
   */
  searchByInput() {
    this.getTableData();
  }

  /**
   * 选择二级分类后获取商品
   */
  selectGood(item: { id: string }) {
    this.search.showCategoryId = item.id;
    this.search.saleMode = this.saleMode;
    this.getTableData();
  }

  /**
   * 选择分类
   */
  selectTem(item: { id: string }) {
    this.saleMode = item.id;
    this.search.saleMode = this.saleMode;
    this.showSecList = [];
    this.search.showCategoryId = "";
    this.showSecVal = "";
    this.getUseEffectiveCategory();
  }

  onClear() {
    this.search.saleMode = "";
    this.showSecList = [];
    this.showSecVal = "";
    this.search.showCategoryId = "";
    this.getTableData();
  }
}
</script>
<style scoped>
.demo-input-suffix .el-input {
  width: 98px !important;
}
.ml10 {
  margin-left: 10px;
}
.mr10 {
  margin-right: 10px;
}
.search-wrap {
  display: flex;
  justify-content: space-between;
  justify-items: center;
}
.search-wrap-input {
  width: 180px;
}
</style>
