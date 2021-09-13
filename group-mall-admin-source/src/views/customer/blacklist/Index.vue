<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:09
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-09-02 17:16:00
-->
<template>
  <div class="blacklist">
    <el-row class="filter" type="flex" justify="space-between">
      <el-button @click="batchDelete" :disabled="multipleSelection.length === 0"
        >批量移除</el-button
      >
      <div class="filter__right">
        <el-select
          class="filter__right--select"
          v-model="dataForm.permission"
          placeholder="全部权限"
          style="width: 150px"
          @change="getDataList(1)"
        >
          <el-option
            v-for="item in permissionOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          >
          </el-option>
        </el-select>
        <el-input
          class="filter__right--input"
          v-model="dataForm.fuzzy"
          @keyup.enter.native="getDataList(1)"
          clearable
          style="width: 200px"
          placeholder="输入关键词"
        >
          <el-button
            slot="append"
            icon="el-icon-search"
            @click="getDataList(1)"
          ></el-button>
        </el-input>
      </div>
    </el-row>
    <m-table
      :data="dataList"
      :selection="true"
      needHoverBorder
      ref="blackList"
      :checked-item.sync="multipleSelection"
      class="blackList--table"
    >
      <m-table-column label="客户信息" v-slot="{ row }" width="240" height="80">
        <div class="info">
          <img class="info__img" :src="row.avatarUrl" alt="" />
          <div class="info__msg">
            <div class="info__msg--name">{{ row.nikeName }}</div>
            <div class="info__msg--phone">{{ row.phone }}</div>
          </div>
        </div>
      </m-table-column>
      <m-table-column
        prop="consumeTotleMoney"
        label="消费金额"
      ></m-table-column>
      <m-table-column label="消费次数" prop="consumeNum"></m-table-column>
      <m-table-column label="限制权限" v-slot="{ row }">
        {{ getBlackType(row.blacklistType) }}
      </m-table-column>
      <m-table-column label="操作" v-slot="{ row }">
        <set-drop
          :dropdownList="dropdownList"
          @setClick="SetBlackList(row)"
          @command="handleCommand($event, row)"
        ></set-drop>
      </m-table-column>
    </m-table>
    <div class="blacklist__footer">
      <el-button
        @click="batchDelete"
        class="blacklist__footer--batchDel"
        :disabled="multipleSelection.length === 0"
        >批量移除</el-button
      >
      <PageManage
        :pageSize="pageSize"
        :pageNum="pageNum"
        :total="total"
        class="PageManage"
        @handleSizeChange="handleSizeChange"
        @handleCurrentChange="handleCurrentChange"
      ></PageManage>
    </div>
    <set-black-list
      v-if="blackVisible"
      ref="setBlack"
      @refreshDataList="getDataList(1)"
    ></set-black-list>
  </div>
</template>

<script lang="ts">
import { Vue, Component, Ref } from "vue-property-decorator";
import PageManage from "@/components/PageManage.vue";
import SetBlackList from "@/views/customer/common/SetBlackList.vue";
import SetDrop from "@/views/customer/common/SetDrop.vue";
import {
  BlackListState,
  ApiBlackList,
  BlackListQueryType,
} from "./blackListType";
import { getBlackList, batchSetBlackList } from "@/api/customer/customer";
import { ApiCustomerList, CustomerDropItem } from "../list/customerListType";
@Component({
  components: {
    PageManage,
    SetBlackList,
    SetDrop,
  },
})
export default class Index extends Vue implements BlackListState {
  @Ref() readonly setBlack!: SetBlackList;

  dataList: Array<ApiBlackList> = [];

  dataForm = {
    permission: null,
    fuzzy: "",
  } as BlackListQueryType;

  multipleSelection: Array<ApiBlackList> = [];

  dropdownList: Array<CustomerDropItem> = [
    {
      command: "delete",
      disabled: false,
      show: true,
      text: "移除",
    },
  ];

  blackVisible = false;

  permissionOptions: Array<{ value: string | number; label: string }> = [
    {
      value: "",
      label: "全部权限",
    },
    {
      value: 1,
      label: "下单",
    },
    {
      value: 2,
      label: "评论",
    },
  ];

  pageSize = 10;

  pageNum = 1;

  total = 0;

  mounted() {
    this.getDataList(1);
  }

  /**
   * 获取黑名单列表
   */
  getDataList(pageNum: number) {
    this.pageNum = pageNum;
    const param = {
      page: this.pageNum,
      size: this.pageSize,
      permission: this.dataForm.permission,
      fuzzy: this.dataForm.fuzzy,
    };
    getBlackList(param).then(res => {
      this.dataList = res.data.list;
      this.pageSize = res.data.size;
      this.pageNum = res.data.current;
      this.total = res.data.total;
    });
  }

  batchDelete() {
    if (this.multipleSelection.length <= 0) {
      return;
    }
    const dataForm = {
      option: 2,
      rejectInteger: [],
      userIds: this.multipleSelection.map(item => item.userId),
    };
    this.$confirm(
      "黑名单移除后将同时移除所有限制行为，确定移除吗？",
      "黑名单移除",
      {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
      },
    )
      .then(() => {
        batchSetBlackList(dataForm).then(() => {
          this.$message({
            message: "已成功移除黑名单",
            type: "success",
          });
          this.getDataList(1);
        });
      })
      .catch(() => {
        this.$message({
          type: "info",
          message: "移除失败",
        });
      });
  }

  SetBlackList(row: ApiCustomerList) {
    this.blackVisible = true;
    this.$nextTick(() => {
      this.setBlack.init([row], 3);
    });
  }

  /**
   * 点击的下拉项
   */
  handleCommand(command: string, row: ApiBlackList) {
    if (command === "delete") {
      const dataForm = {
        option: 2,
        rejectInteger: [],
        userIds: [row.userId],
      };
      this.$confirm(
        "黑名单移除后将同时移除所有限制行为，确定移除吗？",
        "黑名单移除",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
        },
      )
        .then(() => {
          batchSetBlackList(dataForm).then(() => {
            this.$message({
              message: "已成功移除黑名单",
              type: "success",
            });
            this.getDataList(1);
          });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "移除失败",
          });
        });
    }
  }

  getBlackType(list: number[]) {
    return list.map(i => (i === 1 ? "下单" : "评论")).join(",");
  }

  /**
   * @method handleSizeChange
   * @description 每页 条
   */
  handleSizeChange(val: number) {
    this.pageSize = val;
    this.getDataList(1);
  }

  /**
   * @method handleCurrentChange
   * @description 当前页
   */
  handleCurrentChange(val: number) {
    this.pageNum = val;
    this.getDataList(val);
  }
}
</script>

<style lang="scss" scoped>
@import "../../../assets/styles/cutomer/blacklist";
</style>
