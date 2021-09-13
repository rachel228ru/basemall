<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:09
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-27 13:26:58
-->
<template>
  <div>
    <div class="title">选择商品</div>
    <div class="digGoods">
      <div class="digGoods__box">
        <div class="digGoods__box--top">
          <el-select
            v-model="showVal"
            style="width: 120px"
            placeholder="全部专区"
          >
            <el-option-group v-for="group in modelList" :key="group.index">
              <el-option
                :label="group.modeName"
                :value="group.modeName"
                @click.native="selectTem(group)"
              ></el-option>
            </el-option-group>
          </el-select>
          <el-select
            v-model="showCategory"
            style="width: 120px"
            placeholder="全部分类"
          >
            <el-option-group v-for="group in showList" :key="group.index">
              <el-option
                :label="group.name"
                :value="group.name"
                @click.native="selectItem(group)"
              ></el-option>
              <el-option
                v-for="item in group.showCategoryVos"
                @click.native="selectItem(item)"
                :key="item.name"
                :label="item.name"
                :value="item.name"
                style="margin-left: 20px"
              ></el-option>
            </el-option-group>
          </el-select>
          <span
            style="
              margin: 0px 0px 0px 65px;
              color: #a1a1a1;
              width: 60px;
              line-height: 32px;
            "
            >价格</span
          >
          <el-input style="width: 60px" v-model="search.minPrice"></el-input>
          <span style="margin: 0px 5px; line-height: 32px">-</span>
          <el-input style="width: 60px" v-model="search.maxPrice"></el-input>
          <el-input
            style="width: 140px; margin-left: 10px"
            v-model="search.name"
            placeholder="请输入关键词"
          ></el-input>
          <span class="serachBtn">
            <i class="el-icon-search" @click="searchByInput"></i>
          </span>
        </div>
        <div class="digGoods__box--content" v-if="goodsList.length > 0">
          <div
            v-for="(item, index) in goodsList"
            :key="index"
            class="digGoods__box--content--good"
            @click="chooseGood(item)"
            :style="{
              border: item.isCheck
                ? borderStyle.borderGet
                : borderStyle.borderNoGet,
            }"
          >
            <img
              :src="item.pic"
              alt
              class="digGoods__box--content--good--img"
            />
            <div
              class="digGoods__box--content--good--imgShadow"
              v-if="item.isCheck"
            >
              <i
                class="el-icon-check"
                style="color: white; font-size: 40px"
              ></i>
            </div>
            <div class="digGoods__box--content--good--shopName">
              <div>{{ item.name }}</div>
              <div v-if="item.limitType === 0">
                <span v-if="item.minPrice > 0">￥{{ item.minPrice }}</span>
                <span v-else>￥{{ item.maxPrice }}</span>
              </div>
              <div v-else>￥{{ item.minPrice }}~￥{{ item.maxPrice }}</div>
            </div>
          </div>
        </div>
        <div
          class="digGoods__box--content"
          style="
            display: flex;
            justify-content: center;
            align-items: center;
            height: 250px;
          "
          v-if="goodsList.length === 0"
        >
          暂无相关商品信息，请选择其他分类
        </div>
        <div class="digGoods__box--bottom">
          <el-checkbox
            v-model="allChecked"
            style="margin-top: 40px"
            @change="getAll"
            >全选</el-checkbox
          >
          <PageManage
            :pageSize="search.size"
            :pageNum="search.current"
            :total="total"
            @handleSizeChange="handleSizeChange"
            @handleCurrentChange="handleCurrentChange"
          ></PageManage>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { Vue, Component, Prop, Watch } from "vue-property-decorator";
import PageManage from "@/components/PageManage.vue";
import { getCouponsGoods } from "@/api/couponActivity/coupon";
import { getAllCategory, getAllRegionList } from "@/api/good/goods";
import {
  IAddGoodsCategoryList,
  IAddGoodsList,
  IAddGoodsPointList,
} from "../../interfaces/component";
import { ApiSpecArea } from "@/views/goods/marketModel/marketType";

@Component({
  components: {
    PageManage,
  },
})
export default class SelectGoods extends Vue {
  @Prop({
    type: Array,
    default() {
      return [];
    },
  })
  pointGoodsList!: IAddGoodsPointList[];

  @Prop({
    type: Boolean,
    default() {
      return false;
    },
  })
  goodsVisible!: boolean;

  /** 弹框商品 */
  goodsList: IAddGoodsList[] = [];

  /** 选择的商品列表 */
  tempGoods: IAddGoodsList[] = [];

  /** 销售专区 0商超 */
  modelList: Array<Partial<ApiSpecArea>> = [
    {
      modeType: "3",
      modeName: "全部专区",
    },
    {
      modeType: "0",
      modeName: "商超",
    },
  ];

  /** 分类选择 */
  showList: IAddGoodsCategoryList[] = [];

  saleMode = "";

  /** 默认选择状态 */
  showVal = "";

  /** 默认选择分类 */
  showCategory = "";

  /** 全选 */
  allChecked = false;

  /** 分页信息 */
  total = 0;

  search = {
    maxPrice: "",
    minPrice: "",
    name: "",
    current: 1,
    size: 10,
    saleMode: "",
    showCategoryId: "",
  };

  /** 样式选择 */
  borderStyle = {
    borderGet: "2px solid #2D8CF0",
    borderNoGet: "2px solid #f2f2f2",
  };

  @Watch("goodsVisible")
  watchList() {
    if (this.goodsVisible) {
      this.resetDate();
      this.tempGoods = [...this.pointGoodsList];
      this.dealPointList();
      this.getGoods();
    }
  }

  mounted() {
    this.getGoods();

    getAllRegionList({}).then(res => {
      this.modelList = res.data;
      // this.saleMode = res.data[0].id;
      if (this.goodsVisible) {
        this.tempGoods = [...this.pointGoodsList];
        this.dealPointList();
      }
      this.getCategoryList();
    });
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 获取分类列表
   */
  getCategoryList() {
    const param = {
      saleMode: this.saleMode,
    };
    getAllCategory(param).then(res => {
      this.showList = res.data;
    });
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 重置数据
   */

  resetDate() {
    this.search.showCategoryId = "";
    this.search.saleMode = "";
    this.search.name = "";
    this.search.maxPrice = "";
    this.search.minPrice = "";
    this.allChecked = false;
    this.showVal = "";
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 编辑时获取已选择过的数据
   */

  dealPointList() {
    let checkAll = true;
    this.goodsList.forEach(item => {
      const flag = this.checkIsSelected(item.id);
      item.isCheck = flag;
      checkAll = checkAll ? flag : checkAll;
    });
    this.allChecked = checkAll;
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 获取商品
   */

  getGoods() {
    this.search.saleMode = this.saleMode;
    getCouponsGoods(this.search).then(res => {
      let checkAll = true;
      res.data.list.forEach((item: IAddGoodsList) => {
        const flag = this.checkIsSelected(item.id);
        item.isCheck = flag;
        checkAll = checkAll ? flag : checkAll;
      });
      this.allChecked = checkAll;
      this.goodsList = res.data.list;
      this.total = res.data.total;
    });
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 选择商品状态
   * @param {string} e
   */

  selectStatus(e: string) {
    this.search.saleMode = e !== "3" ? e : "";
    this.getGoods();
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 选择专区
   * @param {*} item
   */

  selectTem(item: { id: string }) {
    this.saleMode = item.id;
    this.getCategoryList();
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 选择专区后选择分类
   * @param {*} item
   */

  selectItem(item: IAddGoodsCategoryList) {
    this.search.showCategoryId = item.showCategoryId;
    this.getGoods();
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 选择输入框
   */

  searchByInput() {
    this.getGoods();
  }

  /**
   * @LastEditors: chuyinlong
   * @description:选择商品
   * @param {IAddGoodsList} item
   */

  chooseGood(item: IAddGoodsList) {
    item.isCheck = !item.isCheck;
    if (this.tempGoods.length >= 16) {
      this.$message.error("最多选择16个商品");
    } else if (item.isCheck) {
      this.tempGoods.push(item);
    } else {
      const idx = this.tempGoods.findIndex(i => i.id === item.id);
      if (idx !== -1) {
        this.tempGoods.splice(idx, 1);
      }
    }
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 检测当前商品是否已经在选择过的列表中
   * @param {string} id
   * @returns {boole}
   */

  checkIsSelected(id: string): boolean {
    const idx = this.tempGoods.findIndex(i => i.id === id);
    return idx !== -1;
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 全选操作
   */

  getAll() {
    let flag = false;
    this.goodsList.map(item => {
      if (this.allChecked) {
        if (this.tempGoods.length >= 16) {
          return (flag = true);
        }
        if (!this.tempGoods.find(t => t.id === item.id)) {
          this.tempGoods.push(item);
        }
      }
      return (item.isCheck = this.allChecked);
    });
    if (flag) return this.$message.error("自多选择16个商品");
    if (!this.allChecked) {
      this.goodsList.forEach(t => {
        const idx = this.tempGoods.findIndex(i => i.id === t.id);
        if (idx !== -1) {
          this.tempGoods.splice(idx, 1);
        }
      });
    }
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 每页 条
   * @param {number} val
   */

  handleSizeChange(val: number) {
    this.search.size = val;
    this.getGoods();
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 当前页
   * @param {number} val
   */

  handleCurrentChange(val: number) {
    this.search.current = val;
    this.allChecked = false;
    this.getGoods();
  }
}
</script>

<style lang="scss" scoped>
.title {
  font-size: 15px;
  font-weight: bold;
  display: flex;
  margin-bottom: 20px;
  margin-top: -40px;
}

@include b(digGoods) {
  border-top: 1px solid #d7d7d7;
  padding-top: 10px;
  @include e(box) {
    background-color: #f2f2f2;
    padding: 10px;
    @include m(top) {
      display: flex;
      justify-content: space-between;
    }
    @include m(content) {
      margin-top: 10px;
      background-color: white;
      border-radius: 5px;
      display: flex;
      flex-wrap: wrap;
      padding: 5px;
      @include m(good) {
        width: 33%;
        margin-left: 2px;
        margin-bottom: 4px;
        height: 80px;

        border-radius: 5px;
        padding: 5px;
        display: flex;
        @include m(img) {
          width: 65px;
          height: 65px;
          position: relative;
        }
        @include m(imgShadow) {
          width: 65px;
          height: 65px;
          position: absolute;
          background-color: rgba(0, 0, 0, 0.6);
          @include flex(center, center);
        }
        @include m(shopName) {
          margin-left: 10px;
          padding-top: 5px;
          font-size: 12px;
          align-content: space-between;
        }
      }
    }
    @include m(bottom) {
      display: flex;
      justify-content: space-between;
      align-items: center;
    }
  }
}

.serachBtn {
  width: 32px;
  height: 32px;
  display: flex;
  justify-content: center;
  align-items: center;
  border: 1px solid #dcdfe6;
  background-color: white;
  cursor: pointer;
  border-left: none;
  border-radius: 4px;
}
</style>
