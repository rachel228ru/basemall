<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:09
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-09-02 17:04:56
-->
<template>
  <div class="customer">
    <m-card class="form" :needToggle="true">
      <el-form
        class="customer__dataForm"
        ref="dataFormRef"
        :model="dataForm"
        label-width="100px"
      >
        <el-row :gutter="40">
          <el-col :span="10">
            <el-form-item label="微信昵称">
              <el-input
                v-model="dataForm.nikeName"
                clearable
                placeholder="请输入会员昵称"
                @keyup.enter="getDataList"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="标  签">
              <el-select
                v-model="dataForm.tagId"
                placeholder="请选择标签"
                style="width: 256px"
                clearable
              >
                <el-option label="全部" :value="null" />
                <el-option
                  v-for="tag in allTagList"
                  :key="tag.tagId"
                  :label="tag.tagName"
                  :value="tag.tagId"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="40">
          <el-col :span="10">
            <el-form-item label="上次交易时间">
              <el-date-picker
                v-model="dataForm.orderSuccessTime"
                :default-time="['00:00:00', '23:59:59']"
                value-format="yyyy-MM-dd HH:mm:ss"
                style="width: 256px"
                type="daterange"
                range-separator="-"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
              ></el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </m-card>
    <el-row class="customer__filterForm" type="flex" justify="space-between">
      <el-col :span="4">
        <el-select
          v-model="dataForm.sortType"
          placeholder="请选择排序方式"
          @change="handleCurrentChange(1)"
        >
          <el-option
            v-for="item in sortOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          ></el-option>
        </el-select>
      </el-col>
    </el-row>

    <m-table
      :data.sync="dataList"
      :selection="true"
      :checked-item.sync="multipleSelection"
      slot="content"
      needHoverBorder
      ref="customerListRef"
      class="customerList"
    >
      <m-table-column
        prop="userName"
        label="客户信息"
        :showsSlection="true"
        width="300"
      >
        <template v-slot="{ row }">
          <div class="info">
            <img class="info__img" :src="row.avatarUrl" alt />
            <div class="info__msg">
              <div class="info__msg--text">
                <div style="width: 350px">{{ row.nikeName }}</div>
                <div v-if="row.phone" style="width: 350px">
                  ({{ row.phone }})
                </div>
              </div>
              <div class="info__msg--tags">
                <span
                  v-if="row.userTagVos !== null && row.userTagVos.length > 0"
                  @click="setLabel(row)"
                  class="pointer"
                >
                  <el-button type="text">
                    {{
                      row.userTagVos
                        .map(tag => tag.tagName)
                        .slice(0, 2)
                        .join(" ; ")
                    }}
                  </el-button>
                  <span>等共{{ row.userTagVos.length }}个标签</span>
                </span>
                <el-button
                  type="text"
                  @click="setLabel(row)"
                  v-if="
                    row.userTagVos === null ||
                      (row.userTagVos !== null && row.userTagVos.length === 0)
                  "
                  >请选择所属标签</el-button
                >
                <i
                  class="el-icon-caret-bottom pointer"
                  @click="setLabel(row)"
                />
              </div>
            </div>
          </div>
        </template>
      </m-table-column>
      <m-table-column prop="userName" label="购次">
        <template v-slot="{ row }">
          <span>{{ row.consumeNum }}</span>
        </template>
      </m-table-column>
      <m-table-column prop="userName" label="交易总额">
        <template v-slot="{ row }">
          <span>{{ row.consumeTotleMoney }}</span>
        </template>
      </m-table-column>
      <m-table-column prop="userName" label="上次交易时间" width="160">
        <template v-slot="{ row }">
          <span>{{ row.orderLastDealTime }}</span>
        </template>
      </m-table-column>
      <m-table-column prop="userName" label="操作" width="150">
        <template v-slot="{ row }">
          <set-drop
            setName="加入黑名单"
            :dropdownList="dropdownList(row)"
            @setClick="itemClick(row)"
            @command="itemCommand($event, row)"
          />
        </template>
      </m-table-column>
    </m-table>
    <PageManage
      :pageSize="pageSize"
      :pageNum="pageNum"
      :total="total"
      class="PageManage"
      @handleSizeChange="handleSizeChange"
      @handleCurrentChange="handleCurrentChange"
    />
    <black-list
      ref="blackListDialogRef"
      v-if="blackListVisible"
      @refreshDataList="getDataList"
    />
    <set-label
      ref="labelDialogRef"
      v-if="labelVisible"
      :allTags="allTagList"
      @refreshDataList="getDataList(1)"
    />
    <set-label
      ref="labelDialogRef"
      v-if="labelVisible"
      :allTags="allTagList"
      @refreshDataList="getDataList(1)"
    />
  </div>
</template>

<script lang="ts">
import { Vue, Component, Ref } from "vue-property-decorator";
import PageManage from "@/components/PageManage.vue";
import BlackList, { NewCustomerTagList } from "../common/SetBlackList.vue";
import SetDrop from "@/views/customer/common/SetDrop.vue";
import SetLabel from "@/views/customer/list/components/dialog/SetTags.vue";

import {
  CustomerListState,
  ApiCustomerList,
  CustomerTagList,
  CustomerRank,
} from "./customerListType";
import { ElForm } from "element-ui/types/form";

import { getCustomerList, getAllTags } from "@/api/customer/customer";
import { ElTable } from "element-ui/types/table";

@Component({
  components: {
    PageManage,
    BlackList,
    SetDrop,
    SetLabel,
  },
})
export default class Index extends Vue implements CustomerListState {
  @Ref()
  readonly dataFormRef!: ElForm;

  @Ref()
  readonly customerListRef!: ElTable;

  @Ref()
  readonly blackListDialogRef!: BlackList;

  @Ref()
  readonly labelDialogRef!: SetLabel;

  dataForm = {
    nikeName: "",
    becomeMemberTime: [],
    orderSuccessTime: [],
    memberNumber: null,
    rankCode: null,
    sortType: 1,
    tagId: null,
  };

  dataList: Array<ApiCustomerList> = [];

  allTagList: Array<CustomerTagList> = [];

  selectionList: Array<ApiCustomerList | NewCustomerTagList> = [];

  multipleSelection: Array<ApiCustomerList | NewCustomerTagList> = [];

  managerVisible = false;

  blackListVisible = false;

  labelVisible = false;

  visible = true;

  sortOptions = [
    {
      value: 1,
      label: "按交易总额降序",
    },
    {
      value: 2,
      label: "按交易总额升序",
    },
  ];

  rankOptions: Array<CustomerRank> = [];

  /** 分页条数 */
  pageSize = 10;

  /** 分页页码 */
  pageNum = 1;

  /** 数据长度 */
  total = 0;

  mounted() {
    this.getDataList(1);
  }

  /**
   * 获取用户列表
   */
  getDataList(pageNum: number) {
    const form = this.dataForm;
    const param = {
      page: pageNum,
      size: this.pageSize,
      sortType: form.sortType,
      memberNumber: form.memberNumber,
      rankCode: form.rankCode,
      nikeName: form.nikeName,
      tagId: form.tagId,
      becomeMemberStartTime:
        form.becomeMemberTime !== null ? form.becomeMemberTime[0] : "",
      becomeMemberEndTime:
        form.becomeMemberTime !== null ? form.becomeMemberTime[1] : "",
      orderSuccessStartTime:
        form.orderSuccessTime !== null ? form.orderSuccessTime[0] : "",
      orderSuccessEndTime:
        form.orderSuccessTime !== null ? form.orderSuccessTime[1] : "",
    };
    getCustomerList(param).then(res => {
      this.dataList = res.data.list;
      this.pageSize = res.data.size;
      this.pageNum = res.data.current;
      this.total = res.data.total;
    });
    this.getTags();
  }

  /**
   * @method getTags
   * @description 获取所有标签
   */
  getTags() {
    getAllTags().then(res => {
      this.allTagList = res.data;
    });
  }

  get dropdownList() {
    return () => {
      /** 操作项下拉菜单 */
      const drownList = [
        {
          command: "加入黑名单",
          disabled: false,
          show: true,
          text: "加入黑名单",
        },
      ];
      return drownList;
    };
  }

  batchCouponCommand(command: string) {
    if (this.multipleSelection.length > 0) {
      switch (command) {
        case "设置标签":
          this.labelVisible = true;
          this.$nextTick(() => {
            this.labelDialogRef.init(
              this.multipleSelection as ApiCustomerList[],
            );
          });
          break;
        case "加入黑名单":
          this.blackListVisible = true;
          this.$nextTick(() => {
            this.blackListDialogRef.init(
              this.multipleSelection as ApiCustomerList[],
              1,
            );
          });
          break;
      }
    } else {
      this.$message.info("请至少选择一个客户");
    }
  }

  itemClick(row: ApiCustomerList) {
    this.blackListVisible = true;
    this.$nextTick(() => {
      this.blackListDialogRef.init([row], 1);
    });
  }

  itemCommand(command: string, row: ApiCustomerList) {
    switch (command) {
      case "加入黑名单":
        this.blackListVisible = true;
        this.$nextTick(() => {
          this.blackListDialogRef.init([row], 1);
        });
        break;
    }
  }

  setLabel(row: ApiCustomerList) {
    this.labelVisible = true;
    this.$nextTick(() => {
      this.labelDialogRef.init([row]);
    });
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
@import "../../../assets/styles/cutomer/customer";
</style>
