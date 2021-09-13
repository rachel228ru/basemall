<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:10
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-25 13:47:42
-->
<template>
  <span class="goodCategory">
    <span v-if="numType === '1'">
      <el-popover
        v-model="popoverVisible"
        placement="right"
        width="260"
        trigger="click"
      >
        <span class="pop__text">注：一级分类不可勾选，禁用状态</span>
        <el-tree
          :data="categoryList"
          :props="customProps"
          show-checkbox
          :default-expand-all="false"
          :default-checked-keys="checkedKeys"
          @check="nodeClick"
          node-key="showCategoryId"
          style="margin-top:10px"
        ></el-tree>
        <div class="pop--button">
          <el-button type="primary" size="mini" @click="saveChange"
            >确认</el-button
          >
          <el-button @click="popoverVisible = false" size="mini"
            >取消</el-button
          >
        </div>
        <span class="goodCategory__content" slot="reference">
          <div
            v-for="(v, i) in categorySeconds.productShowCategorys"
            :key="i"
            style="display:flex"
          >
            <div
              v-for="(n, iIndex) in v.productShowCategorySeconds"
              :key="iIndex"
            >
              {{ n.name }};
            </div>
          </div>
          <i class="el-icon-caret-bottom"></i>
        </span>
      </el-popover>
    </span>
    <span v-if="numType === '2'">
      <span class="pop__text">注：一级分类不可勾选，禁用状态</span>
      <el-tree
        :data="categoryList"
        :props="customProps"
        show-checkbox
        :default-expand-all="false"
        :default-checked-keys="checkedKeys"
        @check="nodeClick"
        node-key="showCategoryId"
        style="margin-top:10px"
      ></el-tree>
      <div class="pop--button">
        <el-button type="primary" size="mini" @click="saveChange"
          >确认</el-button
        >
        <el-button @click="cancel" size="mini">取消</el-button>
      </div>
    </span>
  </span>
</template>

<script lang="ts">
import { Vue, Component, Prop, Watch } from "vue-property-decorator";
import ListApart from "../../ListApart.vue";
import { getList } from "@/api/good/goods";
import cloneDeep from "lodash/cloneDeep";
import {
  GoodCategroyChildType,
  GoodCategroyType,
  GoodDetailInfo,
} from "../../goodType";
@Component
export default class GoodCategory extends Vue {
  name = "GoodCategory";

  @Prop({
    type: Object,
    default() {
      return {};
    },
  })
  // FIXME 待完善类型
  readonly categorySeconds?: GoodDetailInfo;

  @Prop({
    type: String,
    default() {
      return "";
    },
  })
  readonly cateType!: string;

  @Prop({
    type: Boolean,
    default() {
      return false;
    },
  })
  popVisible!: boolean;

  @Prop({
    type: Array,
    default() {
      return [];
    },
  })
  showGetList!: GoodDetailInfo[];

  popoverVisible = false;

  categoryList: GoodCategroyType[] = [];

  customProps = {
    label: "name",
    children: "showCategoryVos",
  };

  checkedKeys: Array<number> = [];

  get numType() {
    return this.cateType;
  }

  @Watch("popoverVisible")
  popoverVisibleChange(nVal: boolean) {
    if (nVal) {
      this.getAllCategory();
    }
  }

  @Watch("popVisible")
  popVisibleChange(val: boolean) {
    if (val) {
      this.getAllCategory();
    }
  }

  cancel() {
    this.$emit("chPopVisible", this.popVisible);
  }

  /**
   * 获取展示分类信息
   */
  async getAllCategory() {
    const parentHtml = this.$parent.$parent as ListApart;
    const param = {
      saleMode: parentHtml.saleMode,
    };
    const { data } = await getList(param);
    this.checkedKeys = [];
    this.categoryList = data.list;
    this.categoryList.map(v => {
      return (v.disabled = true);
    });
    if (this.numType === "1" && this.categorySeconds) {
      this.categorySeconds.productShowCategorys.forEach(v => {
        if (v.productShowCategorySeconds)
          v.productShowCategorySeconds.forEach(n => {
            this.checkedKeys.push(n.showCategoryId);
          });
      });
    } else {
      this.dealShowCate();
    }
  }

  /**
   * 所有已选择展示分类的数据
   */
  dealShowCate() {
    const parentHtml = this.$parent.$parent as ListApart;
    this.showGetList.forEach(item => {
      parentHtml.idList.find(goodItem => {
        if (goodItem === item.id) {
          item.productShowCategorys.forEach(v => {
            if (
              v.productShowCategorySeconds &&
              v.productShowCategorySeconds.length > 0
            ) {
              v.productShowCategorySeconds.forEach(n => {
                this.checkedKeys.push(n.showCategoryId);
              });
            }
          });
        }
      });
    });
  }

  /**
   * 批量修改展示分类保存
   */
  saveChange() {
    const arr: GoodCategroyChildType[] = [];
    const temCategoryList = cloneDeep(this.categoryList);
    temCategoryList.forEach(item => {
      this.checkedKeys.forEach(v => {
        item.showCategoryVos.forEach(n => {
          if (v === n.showCategoryId) {
            arr.push(n);
          }
        });
      });
    });
    temCategoryList.forEach(item => {
      item.showCategoryVos = [];
      arr.forEach(n => {
        if (n.parentId === item.showCategoryId) {
          item.showCategoryVos.push(n);
        }
      });
    });
    this.$emit("getNewCateList", temCategoryList, this.categorySeconds);
    this.popoverVisible = false;
  }

  /**
   * 获取重新选择编辑分类ID
   */
  nodeClick(data: GoodCategroyChildType) {
    const index = this.checkedKeys.findIndex(item => {
      return item === data.showCategoryId;
    });
    if (index > -1) {
      this.checkedKeys.splice(index, 1);
    } else {
      this.checkedKeys.push(data.showCategoryId);
    }
    this.checkedKeys = Array.from(new Set(this.checkedKeys));
  }
}
</script>

<style lang="scss">
.goodCategory {
  margin-left: 10px;
  flex-wrap: wrap;
  display: flex;
  width: 100px;
  &__content {
    cursor: pointer;
    flex-wrap: wrap;
    display: flex;
    width: 120px;
  }
}

.pop__text {
  margin-left: 10px;
  font-size: 14px;
}

.pop--button {
  display: flex;
  justify-content: center;
  margin-top: 30px;
}
</style>
