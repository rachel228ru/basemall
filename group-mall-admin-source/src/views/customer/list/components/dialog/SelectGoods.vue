<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-25 14:50:58
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-27 13:26:43
-->
<template>
  <el-dialog
    :visible.sync="dialogVisible"
    width="50%"
    append-to-body
    :before-close="cancelOption"
  >
    <div>
      <div slot="title">选择商品</div>
      <div class="digGoods">
        <div class="digGoods__box">
          <div class="digGoods__box--top">
            <span
              style="
                line-height: 32px;
                margin: 0px 0px 0px 65px;
                color: #a1a1a1;
                width: 60px;
              "
              >价格</span
            >
            <el-input style="width: 60px" v-model="search.minPrice"></el-input>
            <span style="line-height: 32px; margin: 0px 5px">-</span>
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
          <div class="digGoods__box--content">
            <div
              v-for="(item, index) in goodsList"
              :key="index"
              :class="
                `digGoods__box--content--good ${allIds.some(
                  v => v == item.id,
                ) && 'not-select-good'}`
              "
              @click="
                allIds.some(v => v == item.id) ? () => {} : chooseGood(item)
              "
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
    <span slot="footer" class="dialog-footer">
      <el-button @click="cancelOption">取 消</el-button>
      <el-button type="primary" @click="sureDig">确 定</el-button>
    </span>
  </el-dialog>
</template>

<script lang="ts">
import { Vue, Component, Prop, Watch } from "vue-property-decorator";
import PageManage from "@/components/PageManage.vue";
import { getCouponsGoods } from "@/api/couponActivity/coupon";
import { IAddGoodsPointList } from "@/views/decoration/components/EditorPage/src/interfaces/component";
import { GoodCategroyType } from "@/views/goods/marketModel/goodType";

@Component({
  components: {
    PageManage,
  },
})
export default class SelectGoods extends Vue {
  @Prop({
    type: String,
    default() {
      return "";
    },
  })
  /** 获取活动类型 */
  actType!: string;

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
  dialogVisible!: boolean;

  @Prop({
    type: Array,
    default() {
      return [];
    },
  })
  allIds!: any[];

  /** 弹框商品 */
  goodsList: Array<IAddGoodsPointList> = [];

  selectpointGoodsList: Array<IAddGoodsPointList> = [];

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

  cancelOption() {
    this.dialogVisible = false;
    this.search = {
      maxPrice: "",
      minPrice: "",
      name: "",
      current: 1,
      size: 10,
      saleMode: "",
      showCategoryId: "",
    };
    this.goodsList.map(item => {
      return (item.isCheck = false);
    });
  }

  sureDig() {
    this.dialogVisible = false;
    this.$emit(
      "onSureGoods",
      this.selectpointGoodsList.map(v => v.id),
    );
  }

  @Watch("dialogVisible")
  watchList() {
    if (this.dialogVisible) {
      this.dealPointList();
    }
  }

  init() {
    this.getGoods();
    this.dialogVisible = true;
    this.selectpointGoodsList = this.pointGoodsList;
    this.allChecked = false;
  }

  /**
   * 编辑是获取已选择过的数据
   */
  dealPointList() {
    this.goodsList.forEach(item => {
      item.isCheck = false;
      this.selectpointGoodsList.forEach(pointItem => {
        if (item.id === pointItem.id) {
          item.isCheck = true;
        }
      });
    });
  }

  /** 样式选择 */
  borderStyle = {
    borderGet: "2px solid #2D8CF0",
    borderNoGet: "2px solid #f2f2f2",
  };

  getGoods() {
    getCouponsGoods(this.search).then(res => {
      res.data.list.forEach(item => {
        item.isCheck = false;
      });
      this.goodsList = res.data.list;
      this.total = res.data.total;
      this.dealPointList();
    });
  }

  /**
   * 选择商品状态
   */
  selectStatus(e: string) {
    this.search.saleMode = e !== "3" ? e : "";
    this.getGoods();
  }

  /**
   * 选择分类
   */
  selectTem(item: GoodCategroyType) {
    this.search.showCategoryId = item.showCategoryId;
    this.getGoods();
  }

  /**
   * 选择输入框
   */
  searchByInput() {
    this.getGoods();
  }

  /** 选择商品 */
  chooseGood(item: IAddGoodsPointList) {
    item.isCheck = !item.isCheck;
    if (item.isCheck === true) {
      this.selectpointGoodsList.push(item);
    } else {
      this.selectpointGoodsList = this.selectpointGoodsList.filter(
        v => v.id !== item.id,
      );
    }
  }

  /** 全选操作 */
  getAll() {
    this.goodsList.map(item => {
      if (this.allChecked) {
        !this.selectpointGoodsList.some(v => v.id === item.id) &&
          this.selectpointGoodsList.push(item);
      } else {
        this.selectpointGoodsList.findIndex(v => v.id === item.id) >= 0 &&
          this.selectpointGoodsList.splice(
            this.selectpointGoodsList.findIndex(v => v.id === item.id),
            1,
          );
      }
      return (item.isCheck = !this.allChecked ? false : true);
    });
  }

  /**
   * @method handleSizeChange
   * @description 每页 条
   */
  handleSizeChange(val: number) {
    this.search.size = val;
    this.getGoods();
  }

  /**
   * @method handleCurrentChange
   * @description 当前页
   */
  handleCurrentChange(val: number) {
    this.search.current = val;
    this.getGoods();
  }
}
</script>

<style lang="scss" scoped>
@include b(digGoods) {
  border-top: 1px solid #d7d7d7;
  padding-top: 10px;
  @include e(box) {
    background-color: #f2f2f2;
    padding: 10px;
    @include m(top) {
      display: flex;
      justify-content: flex-end;
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

.not-select-good {
  position: relative;
  &::after {
    content: "";
    position: absolute;
    display: block;
    width: 100%;
    height: 100%;
    cursor: not-allowed;
    background: rgba(0, 0, 0, 0.1);
    top: 0;
    left: 0;
  }
}
</style>
