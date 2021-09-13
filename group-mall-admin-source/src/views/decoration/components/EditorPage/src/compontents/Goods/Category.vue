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
    <el-select v-model="activeName" style="width:120px;" placeholder="全部专区">
      <el-option-group v-for="group in list" :key="group.index">
        <el-option
          :label="group.modeName"
          :value="group.modeName"
          @click.native="selectTem(group)"
        ></el-option>
      </el-option-group>
    </el-select>
    <div class="page--top row-wrap">
      <el-table
        ref="multipleTable"
        :data="goodList"
        style="width: 100%"
        max-height="350"
        @selection-change="handleGoodIdsChange"
      >
        <el-table-column type="selection" width="55"> </el-table-column>
        <el-table-column prop="name" label="分类名称"> </el-table-column>
        <el-table-column prop="productNumber" label="商品数量" width="140">
        </el-table-column>
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
import { getEffectiveCategory, getAllRegionList } from "@/api/good/goods";
import PageManage from "@/components/PageManage.vue";
import Goods, { ICategoryItem, ICategoryMode } from "./Goods";
import { ElTable } from "element-ui/types/table";
import { ApiSpecArea } from "@/views/goods/marketModel/marketType";

@Component({
  components: { PageManage },
})
export default class ShowCategory extends Vue {
  /** 设置更换模板弹框 */
  @PropSync("dialogShow", { type: Boolean, default: false })
  syncDialogShow!: boolean;

  @Prop()
  subForm!: Goods;

  chooseItem: ICategoryItem[] = [];

  /** 最后已选数据 */
  emitList: ICategoryItem[] = [];

  /** 所有专区的数据 */
  list: ApiSpecArea[] = [];

  /** 选中专区 */
  activeName = "";

  /** 选中专区id */
  saleMode = "";

  productName = "";

  /** 分页参数 */
  pageSize = 10;

  pageIndex = 1;

  total = 0;

  goodList: ICategoryItem[] = [];

  @Ref() readonly multipleTable!: ElTable;

  mounted() {
    this.getCategoryList();
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 获取分类列表
   */

  async getCategoryList() {
    try {
      const res = await getAllRegionList({});
      this.list = res.data;
      this.activeName = res.data[0].modeName;
      this.saleMode = String(res.data[0].id);
      this.getUseEffectiveCategory();
    } catch (e) {
      console.log(e);
    }
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 根据saleMode查询对应展示分类
   */

  async getUseEffectiveCategory() {
    const res = await getEffectiveCategory(this.saleMode);
    this.goodList = res.data || [];
    this.goodList.forEach(i => {
      i.saleMode = this.saleMode;
      i.productNumber = i.showCategoryVos.reduce((a, b) => {
        a += Number(b.productNumber);
        return a;
      }, 0);
      if (i.productNumber > 50) i.productNumber = 50;
    });
    this.$nextTick(() => {
      const { firstCatList } = this.subForm;
      this.goodList.forEach(i => {
        if (firstCatList.find(k => k.id === i.id)) {
          this.multipleTable.toggleRowSelection(i);
        }
      });
    });
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 顶部专区选择
   * @param {ICategoryMode} item
   */

  selectTem(item: ICategoryMode) {
    this.saleMode = item.id;
    this.getUseEffectiveCategory();
  }

  /**
   * @LastEditors: chuyinlong
   * @description: 勾选商品
   * @param {ICategoryItem[]} item
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
    // this.addSelectedCategory();
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
