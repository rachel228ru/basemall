<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:06
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-27 11:05:55
-->
<template>
  <!-- 展示分类 -->
  <div>
    <div class="search-wrap">
      <el-button @click="onClear">刷新</el-button>
      <div class="search-wrap-input">
        <el-input v-model="searchName" placeholder="请输入关键词">
          <el-button
            slot="append"
            icon="el-icon-search"
            @click="searchClick"
          ></el-button>
        </el-input>
      </div>
    </div>
    <el-table v-loading="loading" :data="tableData" height="369">
      <el-table-column label="专区名称" prop="name"></el-table-column>
      <el-table-column label="操作" width="100px">
        <template slot-scope="scope">
          <el-radio
            v-model="selectId"
            @change="selectHandle"
            :label="scope.row.id"
          >
            <span></span>
          </el-radio>
        </template>
      </el-table-column>
    </el-table>
    <PageManage
      :pageSize="pageSize"
      :pageNum="pageNum"
      :total="total"
      @handleSizeChange="handleSizeChange"
      @handleCurrentChange="handleCurrentChange"
    />
  </div>
</template>

<script lang="ts">
import { Vue, Component, Prop, PropSync, Watch } from "vue-property-decorator";
import PageManage from "@/components/PageManage.vue";
import { getRegionList } from "@/api/good/goods";
// import { getDecList } from "@/api/decoration/decoration";
import LinkSelectItem, { typeNameMap } from "./LinkSelectItem";

/** 展示分类 */
@Component({
  components: {
    PageManage,
  },
})
export default class DisplayClassIfy extends Vue {
  @PropSync("link", {
    type: Object,
    default: () => {
      return {
        id: null,
        type: null,
        name: "",
        url: "",
        append: "",
      };
    },
  })
  linkSelectItem!: LinkSelectItem;

  @Prop({
    type: Boolean,
  })
  visible = false;

  name = "DisplayClassIfy";

  selectId: number | string = "";

  loading = false;

  tableData: LinkSelectItem[] = [];

  pageNum = 1;

  pageSize = 20;

  total = 0;

  searchName = "";

  list: any[] = [];

  mounted() {
    this.getRegionList();
  }

  // 监听父弹窗显隐
  @Watch("visible")
  handleVisibleChange() {
    this.getRegionList();
  }

  onClear() {
    this.getRegionList();
  }

  /**
   * 获取所有专区
   */
  async getRegionList() {
    const param = {
      modeName: this.searchName,
      current: this.pageNum,
      size: this.pageSize,
    };
    const res = await getRegionList(param);
    this.list = res.data.list;
    this.total = res.data.total;
    this.tableData = this.list.map(item => {
      return {
        id: Number(item.id),
        type: 2,
        name: item.modeName,
        url: "/pages/index/index",
        append: "mall",
      };
    });
    // 初始相同设为选中状态
    if (this.sameJudge()) {
      this.selectId = this.linkSelectItem.id;
    }
    // this.getTableData();
  }

  searchClick() {
    this.getRegionList();
  }

  handleSizeChange(val: number) {
    this.pageNum = 1;
    this.pageSize = val;
    this.getRegionList();
  }

  handleCurrentChange(val: number) {
    this.pageNum = val;
    this.getRegionList();
  }

  selectHandle() {
    const selectId = this.selectId;
    const currentItem: LinkSelectItem =
      this.tableData.find(item => item.id === selectId) ||
      ({} as LinkSelectItem);
    Object.assign(this.linkSelectItem, currentItem);
  }

  sameJudge(): boolean {
    return (
      typeNameMap[this.linkSelectItem.type] &&
      typeNameMap[this.linkSelectItem.type].name === this.name
    );
  }
}
</script>

<style scoped>
.search-wrap {
  display: flex;
  justify-content: space-between;
  justify-items: center;
}
.search-wrap-input {
  width: 180px;
}
</style>
