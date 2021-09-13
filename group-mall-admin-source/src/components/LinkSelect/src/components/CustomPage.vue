<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:06
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-27 11:25:02
-->
<template>
  <!-- 商超商品 -->
  <div>
    <el-button @click="onClear">刷新</el-button>
    <el-table v-loading="loading" :data="tableData" height="369">
      <el-table-column label="页面名称" prop="name"></el-table-column>
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
import LinkSelectItem, { typeNameMap } from "./LinkSelectItem";
import { getPageList } from "@/api/group/group";
import { getTemplate } from "@/api/decoration/decoration";

/** 自定义列表 */
@Component({
  components: {
    PageManage,
  },
})
export default class Goods extends Vue {
  @PropSync("link", {
    type: Object,
    default: () => {
      return {
        id: "",
        type: 0,
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

  name = "CustomPage";

  selectId: number | string = 0;

  templateId = "";

  loading = false;

  tableData: LinkSelectItem[] = [];

  pageNum = 1;

  pageSize = 20;

  total = 0;

  mounted() {
    if (this.sameJudge()) {
      this.selectId = this.linkSelectItem.id;
    }
    this.getTemplateList();
  }

  // 监听父弹窗显隐
  @Watch("visible")
  handleVisibleChange() {
    this.getPageList();
  }

  /** 获取自定义页面数据 */
  async getPageList() {
    if (!this.templateId) return;
    this.loading = true;
    const data = {
      templateId: this.templateId,
      current: this.pageNum,
      size: this.pageSize,
    };
    const res = await getPageList(data);
    res.data.list.forEach((item: { type: any }, index: any) => {
      if (item.type) {
        res.data.list.splice(index, 1);
      }
    });
    try {
      if (res.code === 200) {
        const list = res.data.list.filter(
          (i: { type: string }) => i.type !== "1" && i.type !== "2",
        );
        const filterList = res.data.list.filter(
          (i: { type: string }) => i.type === "1" || i.type === "2",
        );
        this.pageList = list;
        this.tableData = list.map((item: { id: any; pageName: any }) => {
          return {
            id: Number(item.id),
            type: 5, // 自定义页面
            name: item.pageName,
            url: "/pages/index/custom/custom",
            append: Number(item.id),
          };
        });
        this.total = res.data.total - filterList.length;
      } else {
        this.$message.warning(`获取数据失败`);
      }
    } catch (e) {
      console.log(e);
    }
    this.loading = false;
  }

  /**
   * 刷新页面
   */
  onClear() {
    this.getPageList();
  }

  handleSizeChange(val: number) {
    this.pageNum = 1;
    this.pageSize = val;
    this.getPageList();
  }

  handleCurrentChange(val: number) {
    this.pageNum = val;
    this.getPageList();
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

  /**
   * 获取模板列表
   * isDevTemplate 默认模板 1是 0否
   * 模板是否使用中 0 否, 1 是
   * 自定义没有
   */
  async getTemplateList() {
    const getPageListData = {
      onlineStatus: 1,
      isAll: 0,
    };
    const res = await getTemplate(getPageListData);
    try {
      if (res.code === 200) {
        /**  获取非默认模板（即为正在使用中的模板） */
        if (res.data.length > 0) {
          /** 如果有，即为使用中模板的curd */
          this.templateId = res.data[0].id;
          this.getPageList();
        }
      } else {
        this.$message.warning(`获取数据失败`);
      }
    } catch (e) {
      console.log(e);
    }
  }
}
</script>
