<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:11
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-09-01 15:48:17
-->
<template>
  <div>
    <m-container
      class="order"
      :current.sync="query.current"
      :size.sync="query.size"
      :total.sync="query.total"
    >
      <OrderForm slot="form" v-model="query" />
      <template slot="content">
        <el-tabs v-model="query.deliverType" @tab-click="handleAreaTabClick">
          <el-tab-pane label="物流售后" name="102"></el-tab-pane>
        </el-tabs>
        <el-tabs v-model="query.status" type="card" @tab-click="handleTabClick">
          <el-tab-pane label="全部" name="-1"></el-tab-pane>
          <el-tab-pane label="待处理" name="0"></el-tab-pane>
          <el-tab-pane label="处理中" name="1"></el-tab-pane>
          <el-tab-pane label="已完成" name="2"></el-tab-pane>
          <el-tab-pane label="已关闭" name="3"></el-tab-pane>
        </el-tabs>

        <order-table
          @table-function="tableFunction"
          :data="orders"
          :query.sync="query"
          :checked-item.sync="checkedItem"
          :sendBillVisible="sendBillVisible"
          :controlOption="[]"
        ></order-table>
      </template>
    </m-container>

    <OrderDetailModal
      v-model="detailVisible"
      :detail="orderDetail"
      :afterInfo="afterInfo"
      :type="detailType"
      :isGroup="true"
      :isAfter="true"
      @reset="handleSeeDetail"
    />

    <RemarkModal
      v-model="remarklVisible"
      :current-order="currentOrder"
      :orderIds="orderIds"
      @onOk="handleRemarks"
      @onCancel="toggleRemarklVisible"
    />
  </div>
</template>

<script lang="ts">
/* eslint-disable indent */
import { Vue, Component, Watch } from "vue-property-decorator";
import OrderForm from "./components/AfterOrderForm.vue";
import OrderTable from "./components/AfterOrderTable.vue";
import OrderDetailModal from "./components/detail/Index.vue";

import {
  ApiAfterDetail,
  ApiAfterListType,
  DeliveryOrderQueryType,
} from "./orderType";
import { RegionType } from "../goods/goodManage/supplierManageType";

import RemarkModal from "./components/RemarkModal.vue";
import { filterEmpty, isClose } from "./common/order";

import { getOrderDetail } from "@/api/order";
import { getAfterList, note } from "@/api/order/afterSale";

export interface NewDeliveryOrderQueryType extends DeliveryOrderQueryType {
  assName: string;
  productName: string;
  status: string;
}

Component.registerHooks(["beforeRouteEnter", "beforeRouteUpdate"]);

/** 订单默认页 */
@Component({
  components: {
    OrderForm,
    OrderTable,
    OrderDetailModal,
    RemarkModal,
  },
})
export default class OrderHome extends Vue {
  query = {
    orderStatus: "",
    area: "",
    assName: "",
    current: 1,
    deliverType: "102",
    endTime: "",
    startTime: "",
    keyword: "",
    lineId: "",
    orderId: "",
    pointName: "",
    productName: "",
    receiverName: "",
    size: 20,
    //  -1：所有订单, 0.待处理, 1.处理中, 2.已完成, 3.已关闭
    status: "-1",
    quicklyDate: "",
  } as NewDeliveryOrderQueryType;

  quicklyOption: RegionType[] = [
    { label: "近一个月订单", value: "0" },
    { label: "近三个月订单", value: "1" },
    { label: "全部订单", value: "2" },
  ];

  /** 表格数据 */
  data: Array<ApiAfterListType> = [];

  /** 选择的订单ID组 */
  orderIds: Array<ApiAfterListType | string | any> = [];

  /** 订单详情modal */
  detailVisible = false;

  /** 留言modal */
  remarklVisible = false;

  detailType = "4";

  /** 订单详情 */
  orderDetail = {} as ApiAfterDetail;

  /** 售后详情 */
  afterInfo = {};

  /** 表格选中条目 */
  checkedItem: Array<ApiAfterListType> = [];

  /** 当前选中的订单 */
  currentOrder = {} as ApiAfterListType;

  /** 移除发货单显示隐藏 */
  removeVisible = false;

  get orders() {
    return this.data.map(item => {
      item.close = isClose(item.status);
      return item;
    });
  }

  /** 表格选中条目ID组 */
  get selectedIds() {
    return this.checkedItem.map(item => item.id);
  }

  get quicklyName() {
    return (this.quicklyOption.find(
      item => item.value === this.query.quicklyDate,
    ) as RegionType).label;
  }

  get sendBillVisible() {
    return this.query.orderStatus === "-1" || this.query.orderStatus === "1";
  }

  beforeDestroy() {
    this.$STORE.orderStore.setShowDeliverId("");
  }

  /** 监听query变化，刷新数据 */
  @Watch("query", { deep: true })
  queryChange(v: NewDeliveryOrderQueryType) {
    this.getAfterList(v);
  }

  /** 获取订单 */
  created() {
    this.query.deliverType = "102";
    getAfterList(filterEmpty(this.query))
      .then(res => {
        const { list, ...other } = res.data;
        Object.assign(this.query, other);
        this.data = list;
      })
      .catch(() => {
        alert("列表获取失败,请稍后再试");
      });
  }

  /** 获取订单列表 */
  getAfterList(q: NewDeliveryOrderQueryType) {
    getAfterList(q)
      .then(res => {
        const { list, ...other } = res.data;
        Object.assign(this.query, other);
        this.data = list;
        this.$nextTick(() => {
          this.$scrollTop(".admin__main--content");
        });
      })
      .catch(err => {
        this.$message.warning(err || "订单列表获取失败");
      });
  }

  /**
   * 监听table传来的事件
   * @param {name} 事件名 remark | close | detail
   */
  tableFunction(name: string, data: any) {
    switch (name) {
      case "remark":
        return this.triggerRemark(data);
      case "close":
        console.log("点击关闭");
      case "detail":
        this.detailType = "1";
        return this.handleSeeDetail(data);
      case "remove":
        console.log("remove");
      case "goodsRemove":
        return this.toggleRemoveVisible();
      case "review":
        return this.handleReview(data);
    }
  }

  /** 处理审核 */
  handleReview(row: any) {
    this.detailType = "4";
    this.handleSeeDetail(row);
  }

  /**
   * 处理tab点击事件
   * @param orderStatus 订单状态
   */
  handleTabClick({ name: orderStatus }: { name: string }) {
    this.checkedItem = [];
    Object.assign(this.query, {
      orderStatus,
      current: 1,
      deliverId: "",
    });
  }

  /** 处理专区tab点击事件 */
  handleAreaTabClick({ name: deliverType }: { name: string }) {
    Object.assign(this.query, {
      deliverType,
      current: 1,
      deliverId: "",
    });
  }

  /**
   * 查看详情
   * @param {orderData} 订单数据 如果没有参数为批量操作
   */
  handleSeeDetail(orderData: ApiAfterDetail) {
    if (orderData.orderId || orderData.id) {
      this.toggleDetailModal();
    }

    if (this.detailVisible) {
      this.afterInfo = orderData;
    }

    this.$nextTick(() => {
      if (!this.detailVisible) this.getAfterList(this.query);
    });

    getOrderDetail(orderData.orderId || orderData.id)
      .then(res => {
        this.orderDetail = res.data;
      })
      .catch(err => {
        this.$message.warning(err || "订单详情获取失败");
      });
  }

  /**
   * 触发备注
   * @param orderData 订单数据
   */
  triggerRemark(orderData: ApiAfterListType) {
    this.currentOrder = orderData;
    this.orderIds = orderData ? [this.currentOrder.id] : [...this.selectedIds];

    this.toggleRemarklVisible();
  }

  /**
   * 备注
   * @param {orderData} 订单数据 如果没有参数为批量操作
   */
  handleRemarks(form?: any) {
    note({ afsIds: this.orderIds, ...form })
      .then(() => {
        this.getAfterList(Object.assign({}, this.query, this.$route.query));
        this.toggleRemarklVisible();
        this.$message.success("备注成功");
      })
      .catch(err => {
        this.$message.warning(err || "备注失败");
      });
  }

  setQuicklyDate(v: string) {
    this.query.quicklyDate = v;
  }

  toggleDetailModal() {
    this.detailVisible = !this.detailVisible;
    if (!this.detailVisible) {
      this.detailType = "4";
    }
  }

  toggleRemarklVisible() {
    this.remarklVisible = !this.remarklVisible;
  }

  toggleRemoveVisible() {
    this.removeVisible = !this.removeVisible;
  }
}
</script>

<style lang="scss" scoped>
@import "@/assets/styles/order/order.scss";
</style>
