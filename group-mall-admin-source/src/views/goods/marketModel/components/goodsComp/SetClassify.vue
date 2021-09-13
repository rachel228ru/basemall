<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:10
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-25 13:47:50
-->
<template>
  <!-- 设置分类 -->
  <div class="setClassify">
    <div @click="handleClick" class="setClassify__title">
      <el-popover
        v-model="popVisible"
        placement="bottom"
        width="260"
        trigger="hover"
        v-if="!isItem"
        @show="showGetId"
      >
        <GoodCategory
          :cate-type="'2'"
          :pop-visible="popVisible"
          @getNewCateList="getNewCateList"
          @chPopVisible="popVisible = false"
          :showGetList="showGetList"
          ref="goodCate"
          style="width: 260px"
        >
        </GoodCategory>
        <span slot="reference">
          <slot>设置分类</slot>
        </span>
      </el-popover>
      <el-popover
        v-model="popVisible"
        placement="bottom"
        width="260"
        trigger="click"
        v-if="isItem"
        @show="showGetId"
      >
        <span slot="reference">
          <slot>编辑</slot>
        </span>
      </el-popover>
    </div>
    <el-dropdown
      @command="handleCommand"
      @visible-change="showChange"
      trigger="hover"
      v-if="!isItem"
    >
      <div class="setClassify__icon">
        <div style="margin-top: -4px">...</div>
      </div>
      <el-dropdown-menu slot="dropdown" class="commandClass">
        <el-dropdown-item
          v-for="item in dropdownList"
          :key="item.command"
          :disabled="item.disabled"
          :command="{ command: item.command, item: item }"
          >{{ item.text }}</el-dropdown-item
        >
      </el-dropdown-menu>
    </el-dropdown>
  </div>
</template>

<script lang="ts">
import { Vue, Component, Prop } from "vue-property-decorator";
import GoodCategory from "./GoodCategory.vue"; // 编辑商品分类
import GoodList from "../GoodsList.vue";
import {
  GoodCategroyType,
  GoodDetailInfo,
} from "@/views/goods/marketModel/goodType";
import ListApart from "../../ListApart.vue";
import { updateGoodShow, getAllRegionList } from "@/api/good/goods";
import { ApiSpecArea } from "../../marketType";

@Component({
  components: {
    GoodCategory,
  },
})
export default class SetClassify extends Vue {
  name = "SetClassify";

  @Prop()
  isItem!: boolean;

  @Prop()
  goodIds!: number[];

  @Prop()
  showGetList!: GoodDetailInfo[];

  idList: string[] = [];

  regionList: Array<Partial<ApiSpecArea>> = [];

  popVisible = false;

  dropdownList: Array<Partial<ApiSpecArea>> = [
    {
      text: "批量上架",
      command: "6",
      disabled: false,
    },
    {
      text: "批量下架",
      command: "7",
      disabled: false,
    },
    {
      text: "删除",
      command: "8",
      disabled: false,
    },
  ];

  dropdownItemList = [
    {
      text: "商品码",
      command: "3",
      disabled: false,
    },
    {
      text: "删除",
      command: "4",
      disabled: false,
    },
  ];

  initData() {
    getAllRegionList({}).then(res => {
      res.data.forEach(item => {
        item.text = `移至${item.modeName}专区`;
        item.show = true;
        item.disabled = false;
        item.command = item.id;
      });

      this.regionList = res.data;
      const parentHtml = this.$parent as ListApart;
      this.regionList.forEach((v, i) => {
        if (v.id === parentHtml.saleMode) {
          res.data.splice(i, 1);
        }
      });
      this.dropdownList = this.regionList.concat(this.dropdownList);
    });
  }

  /**
   * 获取选择商品id
   */
  showGetId() {
    const list = (this.$parent.$refs.goodsList as GoodList).tableCheckedItem;
    this.idList = [];
    list.forEach(item => {
      this.idList.push(String(item.id));
    });
  }

  /**
   * 点击按钮
   */
  handleClick(e: Event) {
    this.$emit("click", e);
  }

  /**
   * 点击的下拉项
   */
  handleCommand(val: any) {
    let type = false;
    if (val.item.modeName) {
      type = true;
    }
    this.$emit("command", val.command, type);
  }

  showChange(e: boolean) {
    if (e) {
      this.regionList = [];
      this.dropdownList = [
        {
          text: "批量上架",
          command: "6",
          disabled: false,
        },
        {
          text: "批量下架",
          command: "7",
          disabled: false,
        },
        {
          text: "删除",
          command: "8",
          disabled: false,
        },
      ];
      this.initData();
    }
  }

  /**
   * 商品单独操作
   */
  handleItemCommand(val: number) {
    this.$emit("handleItemCommand", val);
  }

  getNewCateList(List: GoodCategroyType[]) {
    if (this.idList.length === 0) {
      this.$message.error("请先选择商品");
      return;
    }
    const temList: GoodCategroyType[] = [];
    List.forEach(v => {
      if (v.showCategoryVos.length > 0) {
        temList.push(v);
      }
    });
    updateGoodShow(
      this.idList,
      JSON.parse(
        JSON.stringify(temList).replace(
          /showCategoryVos/g,
          "productShowCategorySeconds",
        ),
      ),
    ).then(res => {
      if (res.code === 200) {
        this.$message.success(`编辑成功`);
        this.popVisible = false;
        this.$emit("changeIds");
      }
    });
  }
}
</script>

<style lang="scss">
@import "@/assets/styles/mixins/mixins.scss";

@include b(setClassify) {
  display: flex;
  line-height: 30px;
  // border: 1px solid #dcdfe6;
  overflow: hidden;
  margin-left: 20px;
  border-radius: 50px;
  background-color: #eaf5fe;
  color: #309af3;
  border: 1px solid #eaf5fe;
  position: relative;

  @include e(title) {
    text-align: center;
    padding: 0 20px;
    cursor: pointer;
  }

  @include e(icon) {
    width: 40px;
    text-align: center;
    cursor: pointer;
    vertical-align: middle;
    color: #309af3;
    font-weight: bold;
  }
}

.setClassify__icon::after {
  color: #309af3;
  content: "|";
  position: absolute;
  left: -5px;
  bottom: 1px;
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
</style>
