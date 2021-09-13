<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:10
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-25 13:41:14
-->
<template>
  <div>
    <el-table
      ref="multipleTable"
      :data="regionList"
      tooltip-effect="dark"
      style="width: 100%; margin-top: 20px"
      row-key="id"
      :header-cell-style="{
        background: '#f6f8fa',
        'font-weight': 'normal',
        color: 'black',
      }"
      :row-style="{ height: '50px', cursor: 'move' }"
    >
      <template slot="empty">
        <div class="emptyLine">暂无数据~</div>
      </template>
      <el-table-column label="专区名称" width="300">
        <template slot-scope="scope">
          <span style="color: #2d8cf0">{{ scope.row.modeName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="商品数量" width="300">
        <template slot-scope="scope">
          <div style="width: 50px; display: flex; justify-content: center">
            {{ scope.row.productNumber }}
          </div>
        </template>
      </el-table-column>
      <el-table-column label="操作">
        <template slot-scope="scope">
          <div class="dealWay">
            <span style="color: #2d8cf0" @click="gotoClassSet(scope.row)"
              >设置分类</span
            >
            <span
              style="color: #2d8cf0"
              v-if="scope.row.modeType !== 'GROUP'"
              @click="editDailog(scope.row)"
              >编辑</span
            >
            <span
              style="color: #fa4e74"
              v-if="scope.row.modeType !== 'GROUP'"
              @click="deleteRegion(scope.row)"
              >删除</span
            >
          </div>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script lang="ts">
import { Vue, Component, Prop } from "vue-property-decorator";
import { ApiSpecArea } from "@/views/goods/marketModel/marketType";
import GoodRegion from "../GoodRegion.vue";
import Sortable from "sortablejs";

@Component
export default class ClassCom extends Vue {
  @Prop({})
  regionList!: ApiSpecArea[];

  mounted() {
    const table = document.querySelector(".el-table__body-wrapper tbody");
    Sortable.create(table, {
      animation: 150,
      onEnd: ent => {
        const oldIndex = ent.oldIndex;
        const newIndex = ent.newIndex;
        if (oldIndex === 0 || newIndex === 0) {
          this.sort();
          return;
        }
        let length = this.regionList.length;
        if (length > 0) {
          const currRow = this.regionList.splice(oldIndex, 1)[0];
          this.regionList.splice(newIndex, 0, currRow);
        }
        this.sort();
      },
    });
  }

  /**
   * 排序功能
   */
  sort() {
    const parentHtml = this.$parent as GoodRegion;
    const list = JSON.parse(JSON.stringify(this.regionList));
    parentHtml.sort(list);
  }

  /**
   * 设置分类
   */
  gotoClassSet(item) {
    const id = item.id;
    this.$router.push({
      name: "class",
      params: { options: "classSet", id },
      query: { options: "classSet", id },
    });
  }

  /**
   * 编辑专区弹窗
   */
  editDailog(item) {
    const parentHtml = this.$parent as GoodRegion;
    const newItem = JSON.parse(JSON.stringify(item));
    parentHtml.editFucntion(newItem);
  }

  /**
   * 删除专区
   */
  deleteRegion(item) {
    const parentHtml = this.$parent as GoodRegion;
    parentHtml.deleteRegionFunction(item);
  }
}
</script>

<style lang="scss" scoped>
.dealWay {
  display: flex;
  cursor: pointer;
  width: 150px;
  justify-content: space-between;
}
</style>
