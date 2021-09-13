<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:10
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-25 13:40:27
-->
<template>
  <div>
    <div class="importTop">
      <span
        >尽量在服务器空闲时间来操作，会占用大量内存与带宽，在获取过程中，请不要进行任何操作！</span
      >
    </div>
    <div class="importLine">
      <div>
        <el-button type="primary" @click="importGood">导入素材</el-button>
        <el-button @click="delSome" :disabled="canClick">批量删除</el-button>
      </div>
      <div class="couponTop__left">
        <el-input
          class="filter__right--input"
          v-model="searchText"
          clearable
          style="width: 250px;"
          placeholder="请输入商品名称关键词"
        >
          <el-button
            slot="append"
            icon="el-icon-search"
            @click="search"
          ></el-button>
        </el-input>
      </div>
    </div>
    <CsvTable
      :csvList="csvList"
      @delChoose="delChoose"
      ref="tableContent"
      @chooseId="chooseId"
    ></CsvTable>
    <PageManage
      :pageSize="pageSize"
      :pageNum="pageNum"
      :total="total"
      @handleSizeChange="handleSizeChange"
      @handleCurrentChange="handleCurrentChange"
    ></PageManage>
    <el-dialog :visible.sync="uploadDialog" width="650px">
      <div slot="title" class="diaTitle">上传文件</div>
      <ImportDialog @close="closeDialog"></ImportDialog>
    </el-dialog>
  </div>
</template>

<script lang="ts">
import { Vue, Component, Ref } from "vue-property-decorator";
import CsvTable from "./components/CsvTable.vue";
import PageManage from "@/components/PageManage.vue";
import ImportDialog from "./components/ImportDialog.vue";
import { GoodDetailInfo } from "@/views/goods/marketModel/goodType";
import { getCsvProList, GoodDel } from "@/api/good/goods";

@Component({
  components: {
    CsvTable,
    PageManage,
    ImportDialog,
  },
})
export default class CsvIndex extends Vue {
  @Ref()
  readonly tableContent!: CsvTable;

  searchText = "";

  csvList: Array<GoodDetailInfo> = [];

  pageSize = 10;

  pageNum = 1;

  total = 20;

  uploadDialog = false;

  canClick = true;

  mounted() {
    this.init();
  }

  /**
   * 获取列表信息
   */
  async init() {
    const parma = {
      current: this.pageNum,
      size: this.pageSize,
      place: 1,
      name: this.searchText,
    };
    const res = await getCsvProList(parma);
    this.csvList = res.data.list;
    this.pageSize = res.data.size;
    this.pageNum = res.data.current;
    this.total = res.data.total;
  }

  /**
   * 搜索商品
   */
  search() {
    this.init();
  }

  /**
   * 删除选中商品
   */
  async delChoose(ids:number[]) {
    await this.$confirm("确定要删除选中商品吗？", "提示", {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning",
    });
    const { code } = await GoodDel(ids, 0, {});
    if (code === 200) {
      this.$message.success("删除成功");
      this.init();
    }
  }

  /**
   * 批量删除
   */
  delSome() {
    this.delChoose(this.tableContent.idList);
  }

  /**
   * 子组件选择id
   */
  chooseId(ids:number[]) {
    this.canClick = ids.length === 0 ? true : false;
  }

  /**
   * 导入素材
   */
  importGood() {
    this.uploadDialog = true;
  }

  /**
   * 关闭弹窗
   */
  closeDialog() {
    this.uploadDialog = false;
    this.init();
  }

  /**
   * @method handleSizeChange
   * @description 每页 条
   */
  handleSizeChange(val:number) {
    this.pageSize = val;
    this.init();
  }

  /**
   * @method handleCurrentChange
   * @description 当前页
   */
  handleCurrentChange(val:number) {
    this.pageNum = val;
    this.init();
  }
}
</script>

<style lang="scss" scoped>
@include b(importTop) {
  width: 100%;
  height: 50px;
  background-color: #fce9e6;
  display: flex;
  align-items: center;
  padding-left: 15px;
  font-size: 14px;
  font-weight: 400;
  color: rgba(246, 28, 28, 1);
  line-height: 22px;
}

@include b(importLine) {
  display: flex;
  justify-content: space-between;
  height: 60px;
  align-items: center;
}

.diaTitle {
  display: flex;
  justify-content: flex-start;
  font-size: 16px;
}
</style>
