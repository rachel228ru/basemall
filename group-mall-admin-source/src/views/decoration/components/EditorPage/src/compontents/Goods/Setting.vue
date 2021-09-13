<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:10
 * @LastEditors: vikingShip
 * @LastEditTime: 2021-08-25 16:03:04
-->
<template>
  <!-- 商品 -->
  <el-form :model="subForm" label-width="70px" class="goods__page--set">
    <el-form-item label="展示分类">
      <el-radio-group v-model="subForm.ponentType" @change="changePonentType">
        <el-radio :label="1">是</el-radio>
        <el-radio :label="2">否</el-radio>
      </el-radio-group>
    </el-form-item>
    <el-form-item label="商品分组" v-if="subForm.ponentType === 1">
      <el-button
        @click="chooseCategory"
        style="margin-left: 10px; border-radius: 7px; padding: 6px 9px"
        >添加分类</el-button
      >
      <span class="select__box-tips">最多添加30个商品分组</span>
    </el-form-item>
    <div
      class="class__box"
      v-show="
        subForm.ponentType === 1 &&
          subForm.firstCatList &&
          subForm.firstCatList.length > 0
      "
    >
      <div class="info__tip-box">
        <span class="t_one">分类展示</span>
        <span class="t_two">商品数量</span>
        <span class="t_three">操作</span>
      </div>
      <div style="height: 147px; overflow: scroll">
        <div v-for="(group, index) in subForm.firstCatList" :key="index">
          <div
            class="list__item"
            v-if="group.id !== '-1'"
            :draggable="true"
            @dragstart="handleDragClass(index)"
            @dragover="handleDragoverClass"
            @drop="handleDropClass(index)"
          >
            <span class="show__name">{{ group.name }}</span>
            <i
              class="el-icon-delete click__btn"
              @click="handleGoodGroupDel(group.id)"
            ></i>
            <el-input-number
              size="mini"
              :min="1"
              :max="50"
              step-strictly
              :step="1"
              :controls="false"
              v-model="group.productNumber"
              class="input__pro-num"
              @change="productNumberChange(group)"
            ></el-input-number>
          </div>
        </div>
      </div>
    </div>
    <el-form-item label="添加商品" v-if="subForm.ponentType === 2">
      <span style="color: #999">鼠标拖拽调整顺序，小程序端商品按顺序显示</span>
    </el-form-item>
    <el-form-item
      v-if="subForm.ponentType === 2"
      label="商品"
      style="border: 1px dotted #999; padding: 12px 12px 12px 0px"
    >
      <ul class="goods-view">
        <li
          v-for="(item, idx) in subForm.goods"
          :key="idx"
          :draggable="true"
          @dragstart="handleDragstart(idx)"
          @dragover="handleDragover"
          @drop="handleDrop(idx)"
          class="goods-view__item"
        >
          <img class="goods-view__img" :src="item.img" />
          <i
            @click="removeGoods(idx)"
            class="goods-view__del el-icon-circle-close"
          ></i>
        </li>
        <li @click="addGoods" class="goods-view__item goods-view__add">
          <i class="el-icon-plus"></i>
        </li>
      </ul>
    </el-form-item>
    <el-form-item v-if="subForm.ponentType === 1" label="分类样式">
      <el-radio-group v-model="subForm.titleStyle">
        <el-radio :label="1">样式1</el-radio>
        <el-radio :label="2">样式2</el-radio>
      </el-radio-group>
    </el-form-item>
    <el-form-item label="列表样式">
      <el-radio-group v-model="subForm.listStyle">
        <el-radio
          v-for="(item, idx) in listStyles"
          :key="item.value"
          :class="[idx > 1 ? 'goods-view__addmart' : '']"
          :label="item.value"
          >{{ item.label }}</el-radio
        >
      </el-radio-group>
    </el-form-item>
    <el-form-item label="页面边距">
      <el-slider
        v-model="subForm.pagePadding"
        :show-tooltip="false"
        :show-input="true"
        :max="30"
      ></el-slider>
    </el-form-item>
    <el-form-item label="商品间距">
      <el-slider
        v-model="subForm.goodPadding"
        :show-tooltip="false"
        :show-input="true"
        :max="30"
      ></el-slider>
    </el-form-item>
    <el-form-item label="商品样式">
      <el-radio-group v-model="subForm.goodsStyle">
        <el-radio
          v-for="item in goodsStyles"
          :key="item.value"
          :label="item.value"
          >{{ item.label }}</el-radio
        >
      </el-radio-group>
    </el-form-item>
    <el-form-item label="图片倒角">
      <el-radio-group v-model="subForm.angle">
        <el-radio
          v-for="item in angleStyles"
          :key="item.value"
          :label="item.value"
          >{{ item.label }}</el-radio
        >
      </el-radio-group>
    </el-form-item>
    <el-form-item label="文本样式">
      <el-radio-group v-model="subForm.textStyle">
        <el-radio
          v-for="item in textStyles"
          :key="item.value"
          :label="item.value"
          >{{ item.label }}</el-radio
        >
      </el-radio-group>
    </el-form-item>
    <el-form-item label="显示内容">
      <div>
        <el-checkbox v-model="subForm.showContent.nameShow" disabled
          >商品名称</el-checkbox
        >
      </div>
      <div>
        <el-checkbox v-model="subForm.showContent.priceShow"
          >商品价格</el-checkbox
        >
      </div>
      <div>
        <el-checkbox v-model="subForm.showContent.buttonShow"
          >购物按钮</el-checkbox
        >
        <el-radio-group
          style="margin-left: 24px"
          v-model="subForm.showContent.buttonStyle"
          v-if="subForm.showContent.buttonShow"
          @change="bottonRadioChange"
        >
          <el-radio
            v-for="(item, idx) in buttonStyles"
            :key="item.value"
            :class="[idx > 2 ? 'goods-view__addmart' : '']"
            :label="idx + 1"
            :disabled="idx > 1 && buttonTypeDisable"
            >{{ item.label }}</el-radio
          >
          <el-input
            v-if="[3, 4].includes(subForm.showContent.buttonStyle)"
            title="自定义购买按钮文案，最多输入五个字"
            v-model="subForm.showContent.buttonText"
            maxlength="5"
            class="goods__button-text"
          >
          </el-input>
        </el-radio-group>
      </div>
      <div>
        <el-checkbox v-model="subForm.showContent.tagShow"
          >商品角标</el-checkbox
        >
        <el-radio-group
          style="margin-left: 24px"
          v-model="subForm.showContent.tagStyle"
          v-if="subForm.showContent.tagShow"
        >
          <el-radio
            v-for="(item, idx) in tagStyles"
            :key="item.value"
            :class="[idx > 2 ? 'goods-view__addmart' : '']"
            :label="idx + 1"
            >{{ item.label }}</el-radio
          >
        </el-radio-group>
      </div>
    </el-form-item>

    <el-dialog
      :visible.sync="goodsVisible"
      width="50%"
      append-to-body
      :before-close="cancelOption"
    >
      <SelectGoods
        ref="selectGoods"
        :pointGoodsList="pointGoodsList"
        :goodsVisible="goodsVisible"
      ></SelectGoods>
      <span slot="footer" class="dialog-footer">
        <el-button @click="cancelOption">取 消</el-button>
        <el-button type="primary" @click="sureDig">确 定</el-button>
      </span>
    </el-dialog>
    <Category
      v-if="dialogShow"
      :dialogShow.sync="dialogShow"
      :subForm="subForm"
      @choose="selectedCategory"
      ref="Category"
    />
  </el-form>
</template>

<script lang="ts">
import Goods, { ICategoryItem, ISubFormGoods } from "./Goods";
import { Vue, Component, Ref, Watch, PropSync } from "vue-property-decorator";
import SelectGoods from "./../addGoods/index.vue";
import Category from "./Category.vue";
import { discountList } from "@/api/good/goods";
import { getAllCategory } from "@/api/decoration/decoration";
import { IAddGoodsList, IAddGoodsPointList } from "../../interfaces/component";

interface Sytles {
  label: string;
  value: string;
}

@Component({
  components: {
    SelectGoods,
    Category,
  },
})
export default class GoodsForm extends Vue {
  @PropSync("formData", {
    type: Object,
    default() {
      return {};
    },
  })
  subForm!: Goods;

  @Ref()
  readonly selectGoods!: SelectGoods;

  @Watch("subForm", { deep: true })
  handleFormDataChange() {
    if (!this.subForm.firstCatList) this.$set(this.subForm, "firstCatList", []);
    if (!this.subForm.currentCategoryId === undefined)
      this.$set(this.subForm, "currentCategoryId", "-1");
  }

  /** 选择商品分类 */
  dialogShow = false;

  /** 类目拖动下角标 */
  classDragIndex = -1;

  /** 列表样式 */
  listStyles: Sytles[] = [
    {
      label: "大图模式",
      value: "goods-style--one",
    },
    {
      label: "一行两个",
      value: "goods-style--two",
    },
    {
      label: "详细列表",
      value: "goods-style--three",
    },
    {
      label: "横向滑动",
      value: "goods-style--four",
    },
  ];

  /** 商品样式 */
  goodsStyles: Sytles[] = [
    {
      label: "无底白边",
      value: "is-none",
    },
    {
      label: "卡片投影",
      value: "is-shadow",
    },
    {
      label: "描底白边",
      value: "is-border",
    },
  ];

  /** 图片倒角 */
  angleStyles: Sytles[] = [
    {
      label: "直角",
      value: "is-straight",
    },
    {
      label: "圆角",
      value: "is-circle",
    },
  ];

  /** 文本样式 */
  textStyles: Sytles[] = [
    {
      label: "常规",
      value: "is-normal",
    },
    {
      label: "加粗",
      value: "is-bold",
    },
  ];

  /** 购物按钮样式 */
  buttonStyles: Sytles[] = [
    {
      label: "样式一",
      value: "1",
    },
    {
      label: "样式二",
      value: "button-style--two",
    },
    {
      label: "样式三",
      value: "button-style--three",
    },
    {
      label: "样式四",
      value: "button-style--four",
    },
  ];

  /** 商品角标样式 */
  tagStyles: Sytles[] = [
    {
      label: "新品",
      value: "tag-style--one",
    },
    {
      label: "热卖",
      value: "tag-style--two",
    },
    {
      label: "抢购",
      value: "tag-style--three",
    },
  ];

  /** 选择商品弹窗 */
  goodsVisible = false;

  /** 选择商品数组 */
  pointGoodsList: IAddGoodsPointList[] = [];

  dragStarIndex = 0;

  /**
   * 列表样式改变 只有大图和列表样式可以选择购买按钮3/4
   */
  get buttonTypeDisable() {
    const list = ["goods-style--three", "goods-style--four"];
    const { listStyle } = this.subForm;
    const flag = list.includes(listStyle);
    if (flag) {
      const value = this.subForm.showContent.buttonStyle;
      this.subForm.showContent.buttonStyle = value > 2 ? 1 : value;
    }
    return flag;
  }

  get goodsIds() {
    return (this.subForm.goods || []).map(i => i.id).join(",");
  }

  @Watch("goodsIds")
  getAddDialog(v = "", o = "") {
    if (v && v.length !== o.length) {
      this.discountList(v, o);
    }
  }

  mounted() {
    if (this.subForm.ponentType === 1) {
      this.filterCategory();
    } else if (this.subForm.ponentType === 2) {
      if (this.goodsIds) {
        this.discountList(this.goodsIds, "");
      }
    }
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 过滤不可用的商品
   * @param {string} ids
   * @param {string} old
   */

  discountList(ids: string, old: string) {
    discountList(ids)
      .then(res => {
        if (res.code === 200) {
          const temp: ISubFormGoods[] = [];
          res.data.forEach((item: IAddGoodsList) => {
            temp.push({
              id: item.id,
              img: item.pic || item.img,
              name: item.name,
              price: this.dealGoodsPrice(item),
              albumPics: item.albumPics,
              saleMode: item.saleMode,
            });
          });
          this.subForm.goods = old
            ? this.dealGoodsSort(temp, old.split(","))
            : this.dealGoodsSort(temp, ids.split(","));
        }
      })
      .catch(err => {
        this.$message.error(err.msg || "网络异常，请稍后重试");
      });
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 处理商品顺序
   * @param {ISubFormGoods[]} goods
   * @param {string[]} ids
   * @returns {ISubFormGoods[]}
   */

  dealGoodsSort(
    goods: ISubFormGoods[] = [],
    ids: string[] = [],
  ): ISubFormGoods[] {
    const temp: ISubFormGoods[] = [];
    ids.forEach(i => {
      const e = goods.find(t => t.id === i);
      e && temp.push(e);
    });
    goods.forEach(i => {
      !ids.includes(i.id) && temp.push(i);
    });
    return temp;
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 初始化数据，需要过滤掉不可用掉一级分类
   */

  async filterCategory() {
    try {
      const res = await getAllCategory({});
      const list: ICategoryItem[] = res.data || [];
      list.forEach(i => {
        i.productNumber = i.showCategoryVos.reduce(
          (a: number, b: { productNumber: any }) => {
            a += Number(b.productNumber);
            return a;
          },
          0,
        );
        if (i.productNumber > 50) i.productNumber = 50;
      });
      const { firstCatList = [] } = this.subForm;
      const value: ICategoryItem[] = [];
      firstCatList.forEach(i => {
        const category = list.find((k: { id: string }) => k.id === i.id);
        if (category) {
          category.saleMode = i.saleMode;
          value.push(category);
        }
      });
      this.$set(this.subForm, "currentCategoryId", value[0].id);
    } catch (e) {
      console.log(e);
    }
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 选择商品分组
   */

  chooseCategory() {
    this.dialogShow = true;
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 移除商品
   * @param {number} index
   */

  removeGoods(index: number) {
    this.subForm.goods.splice(index, 1);
    this.subForm.goodsCount--;
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 增加商品
   */

  addGoods() {
    this.pointGoodsList = [];
    this.subForm.goods.forEach(i => {
      this.pointGoodsList.push({
        ...i,
        pic: i.img || "",
        minPrice: i.price || "",
        maxPrice: "",
        saleMode: i.saleMode,
      });
    });
    this.goodsVisible = true;
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 商品数量改变
   * @param {string|number} val
   */

  goodsCountChange(val: string | number) {
    const count = +val;
    if (isNaN(count)) {
      this.subForm.goodsCount = 1;
    }
    if (count > 16) {
      this.subForm.goodsCount = 16;
    }
    if (count < 0) {
      this.subForm.goodsCount = 1;
    }
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 取消操作
   */

  cancelOption() {
    this.$confirm(
      "确定要退出选择商品页面? 退出后，未保存的信息将不会保留!",
      "提示",
      {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      },
    ).then(() => {
      this.goodsVisible = false;
      this.selectGoods.search = {
        maxPrice: "",
        minPrice: "",
        name: "",
        current: 1,
        size: 10,
        saleMode: "",
        showCategoryId: "",
      };
      this.selectGoods.goodsList.map(item => {
        return (item.isCheck = false);
      });
    });
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 确认选择商品
   */

  sureDig() {
    const list = this.selectGoods.tempGoods;
    const temp: ISubFormGoods[] = [];
    this.goodsVisible = false;
    list.forEach(item => {
      temp.push({
        id: item.id,
        img: item.pic || item.img || "",
        name: item.name,
        price: this.dealGoodsPrice(item),
        albumPics: item.albumPics,
        saleMode: item.saleMode,
      });
    });
    this.subForm.goods = temp;
    this.subForm.goodsCount = temp.length;
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 处理商品价格
   * @param {ISubFormGoods} item
   * @returns {string|number}
   */

  dealGoodsPrice(item: IAddGoodsList): string | number {
    let price: string | number = 0;
    if (item.minPrice === item.maxPrice || item.minPrice) {
      price = item.minPrice;
    } else if (!item.minPrice && item.maxPrice) {
      price = item.maxPrice;
    }
    return !isNaN(Number(price)) ? Number(price).toFixed(2) : price;
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 开始拖动，记录拖动的组件下角标
   * @param {number} i
   */

  handleDragstart(i: number) {
    this.dragStarIndex = i;
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 阻止默认行为，否则drop事件不会触发
   * @param {*} e
   */
  handleDragover(e: { preventDefault: () => void }) {
    e.preventDefault();
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 被放置的容器触发事件，交换两个组件的位置
   * @param {number} i
   */

  handleDrop(i: number) {
    if (this.dragStarIndex === i) {
      return;
    }
    const temp = this.subForm.goods.splice(
      this.dragStarIndex,
      1,
      this.subForm.goods[i],
    );
    this.subForm.goods.splice(i, 1, ...temp);
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 购买按钮文案
   */

  bottonRadioChange() {
    switch (this.subForm.showContent.buttonStyle) {
      case 3:
        this.subForm.showContent.buttonText = "立即购买";
        break;
      case 4:
        this.subForm.showContent.buttonText = "加购物车";
        break;
      default:
        this.subForm.showContent.buttonText = "";
    }
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 被放置的容器触发事件，交换两个组件的位置
   * @param {number} i
   */

  handleDropClass(i: number) {
    if (this.classDragIndex === i) {
      return;
    }
    const temp = this.subForm.firstCatList.splice(
      this.classDragIndex,
      1,
      this.subForm.firstCatList[i],
    );
    this.subForm.firstCatList.splice(i, 1, ...temp);
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 阻止默认行为，否则drop事件不会触发
   * @param {*} e
   */

  handleDragoverClass(e: { preventDefault: () => void }) {
    e.preventDefault();
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 开始拖动，记录拖动的组件下角标
   * @param {number} i
   */

  handleDragClass(i: number) {
    this.classDragIndex = i;
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 选择一级分类
   * @param {ICategoryItem[]} list
   */

  selectedCategory(list: ICategoryItem[] = []) {
    const ls = this.subForm.firstCatList || [];
    list.forEach(i => {
      const temp = ls.find(t => t.id === i.id);
      if (!temp) {
        ls.push(i);
      }
    });
    this.subForm.firstCatList = JSON.parse(JSON.stringify(ls));
    this.subForm.currentCategoryId = list[0].id;
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 删除一级分类
   * @param {string} id
   */

  handleGoodGroupDel(id: string) {
    const firstCatList = this.subForm.firstCatList || [];
    const index = firstCatList.findIndex(item => item.id === id);
    firstCatList.splice(index, 1);
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 切换显示类型
   */

  changePonentType() {
    const { ponentType = 1 } = this.subForm;
    if (ponentType === 1) {
      this.subForm.goods = [];
    }
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 商品数量数据切换
   * @param {ICategoryItem} group
   */

  productNumberChange(group: ICategoryItem) {
    if (!/^\d{1,2}$/.test(String(group.productNumber))) {
      group.productNumber = 5;
    }
  }
}
</script>
<style lang="scss">
.goods__button-text {
  width: 100px;
  display: inline-block;
  height: 24px;
  line-height: 24px;

  .el-input__inner {
    height: 24px !important;
    line-height: 24px !important;
  }
}
.goods__page--set {
  .select__box-tips {
    font-size: 13px;
    color: #999;
    margin-left: 30px;
  }

  .class__box {
    background-color: #fff;
    border: 1px solid #ccc;
    max-height: 220px;
    overflow-y: scroll;
    border-radius: 4px;
    margin-bottom: 22px;
    padding-bottom: 10px;
  }

  .info__tip-box {
    height: 40px;
    background-color: #f5f5f5;
    margin-bottom: 3px;
    position: relative;
    z-index: 1000;

    span {
      display: inline-block;
      height: 40px;
      line-height: 40px;
    }

    .t_one {
      width: 190px;
      padding-left: 18px;
    }

    .t_two {
      width: 143px;
    }

    .t_three {
      width: 40px;
    }
  }

  .list__item {
    height: 35px;
    line-height: 35px;
    font-size: 14px;
    box-sizing: border-box;
    padding: 5px 10px;

    .show__name {
      display: inline-block;
      width: 135px;
      padding: 0px 10px;
      background-color: #f2f2f2;
      font-size: 12px;
      height: 27px;
      line-height: 27px;
      border-radius: 6px;
      white-space: nowrap;
      text-overflow: ellipsis;
      color: #333;
      border: 1px solid #ddd;
    }

    .click__btn {
      float: right;
      color: #333;
      height: 35px;
      line-height: 35px;
      cursor: pointer;
      float: right;
      margin-right: 28px;
    }

    .input__pro-num {
      width: 80px;
      float: right;
      margin-right: 70px;
    }
  }
}
</style>
