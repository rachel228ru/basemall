<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:10
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-27 16:37:27
-->
<template>
  <!-- 商品列表 -->
  <div style="margin-top: 20px">
    <m-table
      :data.sync="goodList"
      :selection="true"
      :checked-item.sync="tableCheckedItem"
      slot="content"
      class="imgView"
    >
      <m-table-column
        prop="userName"
        label="商品"
        :showsSlection="true"
        width="230"
      >
        <template v-slot="{ row }">
          <div class="goodList">
            <div style="width: 70px; height: 70px">
              <img :src="row.pic" alt />
            </div>
            <div class="goodList__msg" @click="currentGoodClick(row)">
              <GoodNameEdit :good-name="row.name" @change="updateGoodName" />
              <GoodPriceEdit
                :sku-stocks="row.skuStocks"
                @change-price="updateGoodPrice"
              />
              <div class="goodList__msg--apply">
                供应商:{{ row.providerName }}
              </div>
            </div>
          </div>
        </template>
      </m-table-column>
      <m-table-column prop="userName" label="库存">
        <template v-slot="{ row }">
          <span class="stockWarn center">
            {{ row.stock }}
            <StockWarn :stock-item="row"></StockWarn>
          </span>
        </template>
      </m-table-column>
      <m-table-column prop="userName" label="创建时间" width="140">
        <template v-slot="{ row }">
          <span>{{ row.createTime }}</span>
        </template>
      </m-table-column>
      <m-table-column prop="userName" label="评分">
        <template v-slot="{ row }">
          <span>{{ row.score }}</span>
        </template>
      </m-table-column>
      <m-table-column prop="userName" label="状态">
        <template v-slot="{ row }">
          <div class="upDown center">
            <span
              class="upDown__goodUp"
              :style="{
                color: row.status === 1 ? '#80C269' : '#F3A07E',
                'background-color': row.status === 1 ? '#F5FAF3' : '#FEF7F4',
              }"
              >{{ row.status === 1 ? "已上架" : "已下架" }}</span
            >
          </div>
        </template>
      </m-table-column>
      <m-table-column prop="userName" label="转移专区">
        <template v-slot="{ row }">
          <el-dropdown
            trigger="click"
            @command="transWay($event, row)"
            placement="bottom-start"
          >
            <span style="display: flex; font-size: 12px">
              {{ currentRegion }}
              <i class="el-icon-arrow-down el-icon--right"></i>
            </span>
            <el-dropdown-menu
              slot="dropdown"
              :class="regionList.length > 4 ? 'commandClass' : ''"
            >
              <el-dropdown-item
                v-for="item in regionList"
                :key="item.command"
                :disabled="item.disabled"
                :command="item.command"
                >{{ item.modeName }}</el-dropdown-item
              >
            </el-dropdown-menu>
          </el-dropdown>
        </template>
      </m-table-column>
      <m-table-column prop="userName" label="操作">
        <template v-slot="{ row }">
          <div class="center">
            <set-drop
              setName="编辑"
              :dropdownList="itemDropList(row)"
              @setClick="edit(row)"
              @command="getDropdown($event, row)"
            />
            <el-dialog
              title="商品码"
              :visible.sync="dialogVisible"
              width="350px"
            >
              <div style="display: flex; justify-content: center">
                <img :src="codeImg" alt style="width: 180px; height: 180px" />
              </div>
              <span slot="footer" class="dialog-footer">
                <el-button @click="dialogVisible = false">取 消</el-button>
                <el-button type="primary" @click="dialogVisible = false"
                  >确 定</el-button
                >
              </span>
            </el-dialog>
          </div>
        </template>
      </m-table-column>
    </m-table>
  </div>
</template>

<script lang="ts">
import { Vue, Component, Watch, Prop } from "vue-property-decorator";
import SetDrop from "@/views/customer/common/SetDrop.vue";
import GoodNameEdit from "@/components/GoodNameEdit.vue"; // 编辑商品名称
import GoodPriceEdit from "@/components/GoodPriceEdit.vue"; // 编辑商品价格
import StockWarn from "./goodsComp/StockWarn.vue"; // 库存警告
import { GoodListState } from "./goodListType";
import { SearchKeyType } from "./searchType";
import {
  getProList,
  GoodUpDown,
  updateGood,
  GoodDel,
  updateGoodsApart,
  getImgCode,
  getAllRegionList,
} from "@/api/good/goods";
import { Loading } from "element-ui";
import { ApiSkuType, GoodDetailInfo } from "../goodType";
import { ApiSpecArea } from "../marketType";
import { PickApiSkuType } from "@/components/componentType/goodPriceEditType";
@Component({
  components: {
    GoodNameEdit,
    GoodPriceEdit,
    StockWarn,
    SetDrop,
  },
})
export default class GoodsList extends Vue implements GoodListState {
  @Prop({})
  changeId!: string;

  @Watch("changeId")
  getSaleMode() {
    this.searchType = {
      current: 1,
      size: 10,
      saleMode: "",
    };
    this.saleMode = this.changeId;
    this.getProduct();
    this.getApartList();
  }

  @Watch("goodIds")
  onGoodIdsChanged(val: number[]) {
    this.$emit("goodId", val);
  }

  saleMode = "";

  goodList: Array<GoodDetailInfo> = [];

  hasList = false;

  cateFlag = false;

  loading = false;

  checkAll = false;

  isIndeterminate = false;

  goodIds: Array<number> = [];

  tableCheckedItem: Array<GoodDetailInfo> = [];

  currentGood: GoodDetailInfo | null = null;

  get itemDropList() {
    return (row: GoodDetailInfo) => {
      return [
        {
          text: "商品码",
          command: "3",
          show: true,
          disabled: false,
        },
        {
          text: "删除",
          command: "4",
          show: true,
          disabled: false,
        },
        {
          text: "复制链接",
          command: "5",
          show: true,
          disabled: false,
        },
        {
          text: "上架",
          command: "6",
          show: row.status === 0,
          disabled: false,
        },
        {
          text: "下架",
          command: "7",
          show: row.status === 1,
          disabled: false,
        },
      ];
    };
  }

  total = 0;

  searchType = {
    current: 1,
    size: 10,
    saleMode: "",
  } as SearchKeyType;

  dialogVisible = false;

  codeImg = "";

  regionList: Array<ApiSpecArea> = [];

  currentRegion = "";

  mounted() {
    this.saleMode = this.changeId;
    this.getProduct();
    this.getApartList();
  }

  /**
   * 获取所有专区
   */
  getApartList() {
    getAllRegionList({}).then(res => {
      res.data.forEach(item => {
        item.text = `移至${item.modeName}专区`;
        item.show = true;
        item.disabled = false;
        item.command = item.id;
        item.isCustom = false;
        if (item.id === this.saleMode) {
          item.isCustom = true;
          this.currentRegion = item.modeName;
        }
      });

      this.regionList = res.data;
    });
  }

  /**
   * 获取商品列表
   */
  async getProduct() {
    const param = this.getProListParams();
    this.loading = true;
    try {
      const res = await getProList(param);
      const goodList = res.data.list;
      this.total = res.data.total;
      this.hasList = res.data.list.length === 0 ? true : false;
      goodList.forEach(item => {
        item.stock = this.getGoodStock(item.skuStocks);
      });
      this.goodList = goodList;
      this.checkAll = false;
      this.handleCheckAllChange();
    } catch (error) {
      console.log(error);
    }
    this.loading = false;
    this.$emit("getShowProList", this.goodList);
  }

  /**
   * 获取商品列表查询参数
   */
  getProListParams() {
    this.searchType.saleMode = this.saleMode;
    return this.searchType;
  }

  /**
   * 获取商品库存
   */
  getGoodStock(skuStocks: ApiSkuType[]): number {
    let stock = 0;
    skuStocks.forEach(v => {
      stock += Number(v.stock);
    });
    return stock;
  }

  /**
   * 全选|取消全选 商品
   */
  handleCheckAllChange() {
    this.goodIds = this.checkAll
      ? this.goodList.map(item => item.id as number)
      : [];
    this.isIndeterminate = false;
  }

  /**
   * 勾选商品
   */
  handleGoodIdsChange(goodId: number) {
    const goodIds = this.goodIds;
    const index = goodIds.findIndex(id => id === goodId);
    index === -1 ? goodIds.push(goodId) : goodIds.splice(index, 1);

    const checkedCount = goodIds.length;
    this.checkAll = checkedCount === this.goodList.length;
    this.isIndeterminate =
      checkedCount > 0 && checkedCount < this.goodList.length;
  }

  /**
   * 转移专区
   */
  transWay(val: string | number, row: GoodDetailInfo) {
    const selectItem = this.regionList.filter(item => item.id === val);
    const modeType = selectItem[0].modeType === "GROUP" ? "GROUP" : 0;
    const id = [];
    id.push(Number(row.id));
    updateGoodsApart(val, modeType, id)
      .then(res => {
        if (res.code === 200) {
          this.$message.success(`商品已${selectItem[0].text}`);
          this.getProduct();
        }
      })
      .catch(e => {
        this.$message.error(e || "网络错误");
      });
  }

  /**
   * 获取下拉框
   */
  getDropdown(val: string | number, row: GoodDetailInfo) {
    if (Number(val) > 9) {
      this.transWay(val, row);
      return;
    }
    switch (val) {
      case "2":
        const id = [];
        id.push(row.id);
        updateGoodsApart(val, 0, id)
          .then(res => {
            if (res.code === 200) {
              this.$message.success(`商品已移至商超专区`);
              this.getProduct();
            }
          })
          .catch(e => {
            this.$message.error(e || "网络错误");
          });
        break;
      case "3":
        this.dealShopCode(Number(row.id));
        break;
      case "4":
        this.delProduct(Number(row.id));
        break;
      case "5":
        if (row.status === 0) {
          this.$message.error("请先上架商品");
          return;
        }
        const path = `subcontract/pages/detail/detail.html` + "?id=" + row.id;
        this.$copyText(path);
        this.$message.success("复制成功");
        break;
      case "6":
      case "7":
        this.changeStatus(row);
        break;
    }
  }

  /**
   * 获取商品码
   */
  dealShopCode(id: number) {
    const wxaGetwxacode = {
      path: `subcontract/pages/detail/detail?id=${id}`,
      width: 0,
    };
    let loadingInstance = Loading.service({ target: ".imgView" });
    getImgCode(wxaGetwxacode)
      .then(res => {
        this.codeImg = res.data;
        0;
        loadingInstance.close();
        this.dialogVisible = true;
      })
      .catch(() => {
        loadingInstance.close();
      });
  }

  /**
   * 删除商品
   */
  async delProduct(ids: string | number | string[]) {
    await this.$confirm("确定要删除选中商品吗？", "提示", {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning",
    });
    const { code } = await GoodDel(ids, 0, {});
    if (code === 200) {
      this.$message.success("删除成功");
      this.getProduct();
    }
  }

  /**
   *
   */
  currentGoodClick(val: GoodDetailInfo | null) {
    this.currentGood = val;
  }

  /**
   * 更新商品名称
   */
  async updateGoodName(newName: string) {
    const currentGood = this.currentGood as GoodDetailInfo;
    const param = {
      ...this.currentGood,
      name: newName,
    };
    await this.updateGoodInfo(param);
    currentGood.name = newName;
  }

  /**
   * 更新商品价格
   */
  async updateGoodPrice(prices: PickApiSkuType) {
    const currentGood = this.currentGood as GoodDetailInfo;
    const skuStocks = currentGood.skuStocks.map(item => {
      return {
        ...item,
        price: prices[item.id],
      };
    });
    const param = {
      ...this.currentGood,
      skuStocks,
    };
    await this.updateGoodInfo(param);
    currentGood.skuStocks = skuStocks;
  }

  /**
   * 更新商品信息
   */
  async updateGoodInfo(param: Partial<GoodDetailInfo>) {
    try {
      const { code } = await updateGood(param);
      if (code === 200) {
        this.$message.success("修改成功");
        return Promise.resolve(true);
      }
    } catch (error) {
      console.log(error);
    }
    return Promise.reject(false);
  }

  /**
   * 上下架商品
   */
  changeStatus(item: GoodDetailInfo) {
    console.log(item);
    if (item.productShowCategorys.length === 0) {
      this.$message.error(
        `商品${item.status === 0 ? "上架" : "下架"}失败,请重新编辑商品分类信息`,
      );
      return;
    }
    const id = [];
    id.push(item.id);
    GoodUpDown(item.status === 1 ? 0 : 1, item.saleMode, id).then(res => {
      if (res.code === 200) {
        this.$message.success(`商品已${item.status === 0 ? "上架" : "下架"}`);
        this.getProduct();
      }
    });
  }

  /**
   * 编辑商品
   */
  edit(item: { id: string; saleMode: string }) {
    this.$router.push({
      name: "AddGood",
      query: {
        id: item.id,
        saleMode: item.saleMode,
      },
      params: {
        id: item.id,
        saleMode: item.saleMode,
      },
    });
  }
}
</script>

<style lang="scss" scoped>
.page--top {
  margin-top: 20px;
}

.user__list {
  border-collapse: collapse;

  thead tr td {
    // border-bottom: 1px solid #eee;
    // border-top: 1px solid #eee;
    padding: 10px 5px;
    background-color: #f6f8fa;

    text-align: center;

    &:first-child {
      width: 1%;
    }

    &:nth-child(2) {
      width: 50px;
      text-align: left !important;
    }

    &:last-child {
      width: 90px;
    }
  }

  tbody tr:nth-child(2) td {
    background-color: #ecf6ff;
    height: 40px;
    padding: 5px 5px;
    // border-right: 1px solid #ecf6ff;
    // border-left: 1px solid #ecf6ff;
    font-size: 12px !important;

    .checkItem {
      margin-left: 15px;
    }
  }

  tbody tr:nth-child(3) td {
    // border-bottom: 1px solid #ecf6ff;
    padding: 15px 9px;
    text-align: center;
    height: 75px;
    border-right: 1px solid #ecf6ff;

    &:first-child {
      // border-left: 1px solid #ecf6ff;
    }

    &:nth-child(2) {
      height: 100px;
      display: flex;
      align-items: center;
      flex-wrap: wrap;
      justify-content: center;
    }
  }

  &__good {
    width: 280px;
    display: flex;
    text-align: justify;
    padding-right: 20px;

    img {
      width: 70px;
      height: 70px;
    }

    &--msg {
      margin-left: 10px;
      display: flex;
      flex-wrap: wrap;
      align-content: space-between;

      &--price {
        color: #ff7417;
        margin-top: 10px;
      }
    }
  }

  .stockWarn {
    @include flex(center, center);
  }

  .upDown {
    display: flex;
    align-items: center;
    justify-content: center;

    &--goodUp {
      display: flex;
      width: 50px;
      height: 30px;
      justify-content: center;
      align-items: center;
      border-radius: 50px;
      color: white;
    }

    &--goodDown {
      margin-left: 10px;
      color: #2d8cf0;
      cursor: pointer;
    }
  }
}

.mouseEnter {
  // background-color: red;
  border: 1px solid #ecf6ff;
}

.mouseEnter:hover {
  // background-color: green;
  border: 1px solid #d7e0e8;
}

.pop--button {
  display: flex;
  justify-content: flex-end;
  margin-right: 10px;
}

.goodList {
  width: 280px;
  display: flex;
  text-align: justify;
  padding-right: 20px;

  img {
    width: 70px;
    height: 70px;
  }

  &__msg {
    margin-left: 10px;
    display: flex;
    flex-wrap: wrap;
    align-content: space-between;
    padding: 8px 0px;

    &--apply {
      width: 120px;
      overflow: hidden;
      white-space: nowrap;
      text-overflow: ellipsis;
    }
  }
}

.upDown {
  display: flex;
  align-items: center;
  justify-content: center;

  &__goodUp {
    display: flex;
    width: 50px;
    height: 20px;
    justify-content: center;
    align-items: center;
    border-radius: 4px;
    color: white;
    margin-right: 10px;
  }

  &__goodDown {
    margin-left: 10px;
    color: #2d8cf0;
    cursor: pointer;
  }
}

.commandClass {
  height: 150px;
  overflow: overlay;
}

.commandClass::-webkit-scrollbar {
  width: 4px;
  height: 4px;
}

.commandClass::-webkit-scrollbar-thumb {
  border-radius: 10px;
  -webkit-box-shadow: inset 0 0 5px rgba(0, 0, 0, 0.2);
  background: rgba(0, 0, 0, 0);
}

.commandClass::-webkit-scrollbar-track {
  -webkit-box-shadow: inset 0 0 5px rgba(0, 0, 0, 0.2);
  border-radius: 0;
  background: rgba(0, 0, 0, 0);
}

.center {
  display: flex;
  justify-content: center;
}

.digTitle {
  font-size: 17px;
  font-weight: bold;
}
</style>
