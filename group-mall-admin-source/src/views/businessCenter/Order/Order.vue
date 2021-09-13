<template>
  <div class="customer">
    <div class="form">
      <el-select
        clearable
        v-model="dataForm.packageId"
        placeholder="选择订购套餐"
        class="form-item"
        @change="getDataList"
      >
        <el-option label="全部" value=""></el-option>
        <el-option
          v-for="item in mealOptions"
          :key="item.id"
          :label="item.name"
          :value="item.id"
        ></el-option>
      </el-select>
      <el-select
        v-model="dataForm.status"
        placeholder="订单状态"
        @change="getDataList"
        class="form-item select"
      >
        <el-option
          v-for="item in statusOptions"
          :key="item.id"
          :label="item.label"
          :value="item.id"
        ></el-option>
      </el-select>
      <el-select
        v-model="dataForm.platfromType"
        placeholder="平台类型"
        @change="getDataList"
        class="form-item select"
      >
        <el-option label="全部平台" value=""></el-option>
        <el-option
          v-for="item in platformList"
          :key="item.id"
          :label="item.label"
          :value="item.id"
        ></el-option>
      </el-select>
      <span class="activeTop__sel form-item">订购时间</span>
      <el-date-picker
        v-model="dataForm.time"
        type="daterange"
        :default-time="['00:00:00', '23:59:59']"
        value-format="yyyy-MM-dd HH:mm:ss"
        range-separator="-"
        start-placeholder="开始日期"
        end-placeholder="结束日期"
        @change="getDataList"
        class="form-item"
      ></el-date-picker>
    </div>

    <m-table
      :data.sync="dataList"
      slot="content"
      needHoverBorder
      ref="orderListRef"
      class="orderList"
    >
      <template v-slot:header="{ row }">
        <div style="width: 100%">
          <span style="margin-right: 50px">单号：{{ row.orderNum }}</span>
          <span>订购时间：{{ row.createTime }}</span>
        </div>
      </template>
      <m-table-column prop="shopName" label="店铺名称">
        <template v-slot="{ row }">
          <span>{{ row.shopName }}</span>
        </template>
      </m-table-column>
      <m-table-column prop="templateName" label="店铺类型">
        <template v-slot="{ row }">
          <span>{{ row.templateName }}</span>
        </template>
      </m-table-column>
      <m-table-column prop="packageName" label="订购套餐">
        <template v-slot="{ row }">
          <span>{{ row.packageName }}</span>
        </template>
      </m-table-column>
      <m-table-column prop="packageTime" label="套餐周期(天)">
        <template v-slot="{ row }">
          <span>{{ row.packageTime }}</span>
          <!--          <span>{{ row.packageTime / stepMap[packageData(row.packageData).packagePriceUnit] }}{{packagePriceUnitMap[packageData(row.packageData).packagePriceUnit]}}</span>-->
        </template>
      </m-table-column>
      <m-table-column prop="paidPayable" label="订购金额（元）" width="120">
        <template v-slot="{ row }">
          <span>{{ row.paidPayable }}</span>
        </template>
      </m-table-column>
      <m-table-column prop="payType" label="支付方式">
        <template v-slot="{ row }">
          <span
            v-if="
              (row.payType === 5 &&
                (row.orderSource === 1 || row.orderSource === 3)) ||
                row.orderSource === 4
            "
            >管理员支付</span
          >
          <span v-else>
            <span v-if="row.payType === 1">余额支付</span>
            <span v-if="row.payType === 2">微信</span>
            <span v-if="row.payType === 3">支付宝</span>
            <span v-if="row.payType === 4">汇款支付</span>
            <span v-if="row.payType === 5 && row.orderSource === 2"
              >系统支付</span
            >
          </span>
        </template>
      </m-table-column>
      <m-table-column prop="status" label="状态">
        <template v-slot="{ row }">
          <el-tag v-if="row.status === 0" type="info">待处理</el-tag>
          <el-tag v-if="row.status === 1">处理中</el-tag>
          <el-tag v-if="row.status === 2" type="success">已完成</el-tag>
          <el-tag v-if="row.status === 3" type="warning">已关闭</el-tag>
        </template>
      </m-table-column>
      <m-table-column label="操作" width="150">
        <template v-slot="{ row }">
          <set-drop
            setName="查看详情"
            :dropdownList="dropdownList(row)"
            @setClick="itemClick(row)"
            @command="itemCommand($event, row)"
          />
        </template>
      </m-table-column>
    </m-table>
    <PageManage
      :pageSize="pageSize"
      :pageNum="pageIndex"
      :total="totalPage"
      @handleSizeChange="handleSizeChange"
      @handleCurrentChange="handleCurrentChange"
    />
    <ApplyBilling
      :visible.sync="applyVisible"
      :order="currentOrder"
      @refreshDataList="getDataList"
    />
    <OrderDetail
      :visible.sync="detailVisible"
      :order="currentOrder"
      @refreshDataList="getDataList"
    />
  </div>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import ApplyBilling from "@/views/businessCenter/Order/ApplyBilling.vue";
import OrderDetail from "@/views/businessCenter/Order/OrderDetail.vue";
import PageManage from "@/components/PageManage.vue";
import SetDrop from "@/views/customer/common/SetDrop.vue";
import { getMealList, getOwnMeal } from "@/api/meal/meal";

Component.registerHooks(["beforeRouteEnter", "beforeRouteUpdate"]);

@Component({
  components: {
    ApplyBilling,
    OrderDetail,
    PageManage,
    SetDrop,
  },
})
export default class Index extends Vue {

  dataForm = {
    packageId: null,
    status: 0,
    time: [],
    platfromType: "",
  };

  dataList = [];

  // 当前操作的订单
  currentOrder = {};

  applyVisible = false;

  detailVisible = false;

  // 套餐版本下拉列表
  mealOptions = [];

  // 订单状态下拉列表
  statusOptions = [
    {
      id: 0,
      label: "全部状态",
    },
    {
      id: 1,
      label: "订购中",
    },
    {
      id: 2,
      label: "订购成功",
    },
    {
      id: 3,
      label: "订单关闭",
    },
  ];

  /** 平台类型 */
  platformList = [
    {
      id: 1,
      label: "系统管理员",
    },
    {
      id: 2,
      label: "商家",
    },
  ];

  // 套餐单价单位
  packagePriceUnitMap = ["天", "月", "年"];

  // 套餐周期最小值表
  stepMap = {
    1: 1,
    2: 30,
    3: 365,
  };

  pageIndex = 1;

  pageSize = 10;

  totalPage = 0;

  created() {
    if (this.$route.query.status === "success") {
      this.$message({
        message: "套餐订购成功",
        type: "success",
      });
      this.$router.push({
        path: "/business",
        query: {
          tabName: "Order",
        },
      });
    }
  }

  mounted() {
    this.getList();
    this.getDataList();
  }

  /**
   * 获取用户列表
   */
  async getDataList() {
    try {
      const param = {
        page: this.pageIndex,
        size: this.pageSize,
        packageId: this.dataForm.packageId || "",
        platfromType: this.dataForm.platfromType,
        status: this.dataForm.status,
        payStartTime:
          this.dataForm.time && this.dataForm.time.length > 0
            ? this.dataForm.time[0]
            : "",
        payEndTime:
          this.dataForm.time && this.dataForm.time.length > 0
            ? this.dataForm.time[1]
            : "",
        selectType: 2
      };
      const response = await getOwnMeal(param);
      const { code, data } = response;
      if (code === 200) {
        this.dataList = data.list || [];
        this.pageSize = data.size;
        this.totalPage = data.total;
      } else {
        this.$message({
          type: "warning",
          message: "获取订购列表失败！",
        });
      }
    } catch (err) {
      console.log(err);
    }
  }

  async getList() {
    try {
      const response = await getMealList();
      const { code, data } = response;
      if (code === 200) {
        this.mealOptions = data;
        this.mealOptions.forEach(meal => {
          if (meal.functionDesc && typeof meal.functionDesc === "string") {
            meal.functionDesc = meal.functionDesc.split(",");
          }
          if (meal.discountsJson && typeof meal.discountsJson === "string") {
            meal.discountsJson = JSON.parse(meal.discountsJson);
          }
        });
      } else {
        this.$message({
          type: "warning",
          message: "获取套餐信息失败！",
        });
      }
    } catch (err) {
      console.log(err);
    }
  }

  get dropdownList() {
    return row => {
      return [
        {
          command: "申请开票",
          show: row.status === 2,
          disabled: false,
          text: "申请开票",
        },
      ];
    };
  }

  // 套餐信息
  get packageData() {
    return packageData => {
      if (packageData && typeof packageData === "string") {
        return JSON.parse(packageData);
      } else {
        return {
          createTime: "",
          deleted: false,
          id: null,
          level: null,
          name: "",
          openState: null,
          packagePrice: "",
          packagePriceUnit: null,
          remark: "",
          templateId: null,
          templateVersionId: null,
        };
      }
    };
  }

  itemClick(row) {
    this.currentOrder = row;
    this.detailVisible = true;
  }

  itemCommand(command, row) {
    switch (command) {
      case "申请开票":
        this.currentOrder = row;
        this.applyVisible = true;
        break;
    }
  }

  /**
   * @method handleSizeChange
   * @description 每页 条
   */
  handleSizeChange(val) {
    this.pageSize = val;
    this.getDataList();
  }

  /**
   * @method handleCurrentChange
   * @description 当前页
   */
  handleCurrentChange(val) {
    this.pageIndex = val;
    this.getDataList();
  }
}
</script>

<style lang="scss" scoped>
.form {
  margin: 15px 0 25px 0;

  .form-item {
    padding-right: 15px;

    &.select {
      width: 120px;
    }
  }
}
</style>
