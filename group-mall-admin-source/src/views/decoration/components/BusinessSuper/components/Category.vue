<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:09
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-23 20:21:25
-->
<template>
  <el-dialog
    :visible.sync="syncDialogShow"
    title="选择展示分类"
    append-to-body
    top="10vh"
    lock-scroll
    :close-on-click-modal="false"
    width="480px"
    class="select__category-dialog"
  >
    <div class="row-wrap" style="margin-bottom: 20px;" v-if="false">
      <el-button @click="getCategoryList">刷新</el-button>
      <el-input
        placeholder="请输入搜索内容"
        v-model="productName"
        class="search__category-input"
      />
    </div>
    <div class="page--top row-wrap">
      <el-table
        ref="multipleTable"
        :data="goodList"
        height="260"
        style="width: 100%"
        @selection-change="handleGoodIdsChange"
      >
        <el-table-column type="selection" width="55"> </el-table-column>
        <el-table-column prop="name" label="分类名称"> </el-table-column>
      </el-table>
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button @click="syncDialogShow = false">取 消</el-button>
      <el-button type="primary" @click="submit">确 定</el-button>
    </span>
  </el-dialog>
</template>
<script lang="ts">
import { Vue, PropSync, Component, Ref, Prop } from "vue-property-decorator";
import { getList } from "@/api/good/goods";
import PageManage from "@/components/PageManage.vue";
import BusinessSuper, { ICategoryItem } from "./../BusinessSuper";
import { ElTable } from "element-ui/types/table";

@Component({
  components: { PageManage },
})
export default class ShowCategory extends Vue {
  /** 设置更换模板弹框 */
  @PropSync("dialogShow", { type: Boolean, default: false })
  syncDialogShow!: boolean;

  @Prop()
  formData!: BusinessSuper;

  chooseItem: ICategoryItem[] = [];

  get saleMode() {
    return this.$STORE.decorationStore.saleMode;
  }

  /** 最后已选数据 */
  emitList: ICategoryItem[] = [];

  productName = "";

  /** 分页参数 */
  pageSize = 10;

  pageIndex = 1;

  total = 0;

  goodList: ICategoryItem[] = [];

  @Ref() readonly multipleTable!: ElTable;

  mounted() {
    this.getUseEffectiveCategory();
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 获取分类列表
   */

  async getUseEffectiveCategory() {
    const param = {
      saleMode: this.saleMode,
    };
    const res = await getList(param);
    this.goodList = res.data.list || [];
    this.$nextTick(() => {
      const { firstCatList } = this.formData;
      this.goodList.forEach(i => {
        if (firstCatList.find(k => k.category.id === i.id)) {
          this.multipleTable.toggleRowSelection(i);
        }
      });
    });
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 勾选分类
   * @param {ICategoryItem[]} item
   * @returns {*}
   */

  handleGoodIdsChange(item: ICategoryItem[] = []) {
    this.chooseItem = item;
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 添加已选中分类
   */

  addSelectedCategory() {
    this.emitList = this.emitList.concat(this.chooseItem);
    this.chooseItem = [];
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 勾选当前选中行
   */

  selectedRow() {
    this.goodList.forEach(i => {
      if (this.emitList.find(k => k.id === i.id)) {
        this.multipleTable.toggleRowSelection(i);
      }
    });
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 保存
   */

  submit() {
    this.$emit("choose", this.chooseItem);
    this.syncDialogShow = false;
  }
}
</script>
<style lang="scss" scoped>
/deep/.el-pagination {
  float: right;
}

.row-wrap {
  padding: 0 20px;
  position: relative;
  &-btn {
    height: 32px;
    border: 1px solid #000;
  }
  &-input {
    width: 200px;
    height: 33px;
    position: absolute;
    top: 0;
    right: 24px;
    /deep/.el-input__inner {
      outline: none;
      border: 1px solid #000;
    }
  }
  &-selected {
    height: 40px;
    line-height: 40px;
    color: rgb(199, 22, 22);
    text-align: right;
    user-select: none;
  }
}
.page--top {
  .pro__list {
    width: 100%;
    &__tr2 {
      height: 62px;
      line-height: 62px;
      border-top: 1px solid #f2f2f2;
      &__td {
        position: relative;
        padding-left: 20px;
        &__check {
          position: absolute;
          top: -4px;
          left: 0px;
        }
      }
      td {
        width: 210px;
      }
    }
    &__tr1 {
      background: #f2f2f2;
      height: 40px;
      line-height: 40px;
      td {
        width: 210px;
      }
    }
  }
}
</style>
<style lang="scss">
.select__category-dialog {
  .search__category-input {
    float: right;
    width: 160px;
  }

  .el-dialog__body {
    padding: 15px 20px;
  }
}
</style>
