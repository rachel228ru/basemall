<!--
 * @description: 
 * @Author: vikingShip
 * @Date: 2021-07-17 13:29:51
 * @LastEditors: vikingShip
 * @LastEditTime: 2021-08-25 13:44:40
-->
<template>
  <div class="pagination">
    <el-pagination
      :page-sizes="_pageSizes"
      :page-size="_pageSize"
      :current-page="_pageNum"
      :total="_total"
      layout="total, prev, pager, next, sizes"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    ></el-pagination>
  </div>
</template>

<script lang="ts">
import { Vue, Component, Prop } from "vue-property-decorator";

@Component
export default class PageManage extends Vue {
  @Prop({ type: Number, default: 20 })
  pageSize!:number;

  @Prop({ type: Number, default: 1 })
  pageNum!: number;

  @Prop({
    type: Array,
    default() {
      return [10, 20, 50, 100];
    },
  })
  pageSizes!: number[];

  @Prop({ type: Number, default: 0 })
  total!: number;

  // TODO 调试￥ref是否可以用
  get _pageSize() {
    return this.$parent.$refs.pageSize || this.pageSize;
  }

  get _pageNum() {
    return this.$parent.$refs.pageNum || this.pageNum;
  }

  get _pageSizes() {
    return this.$parent.$refs.pageSizes || this.pageSizes;
  }

  get _total() {
    return this.$parent.$refs.total || this.total;
  }

  handleSizeChange(val:number) {
    this.$emit("handleSizeChange", val);
  }

  handleCurrentChange(val:number) {
    this.$emit("handleCurrentChange", val);
  }
}
</script>
    
<style lang="scss" scoped>
.pagination {
  display: flex;
  justify-content: flex-end;
  margin-top: 20px;
}

/deep/ .el-pagination {
  padding: 0px;
  position: relative;
  right: -15px;
}
</style>
