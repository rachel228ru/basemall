<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:11
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-09-02 10:45:18
-->
<template>
  <div>
    <m-container
      class="order"
      :pagination-visible="false"
      :current.sync="query.current"
      :size.sync="query.size"
      :total.sync="query.total"
    >
      <DeliveryOrderFrom v-model="query" slot="form" />

      <template slot="content">
        <el-tabs v-model="query.orderStatus" @tab-click="handleTabClick">
          <el-tab-pane name="-1">
            <span slot="label">
              <div class="order__quickly">
                <span class="name">{{ quicklyName }}</span>
                <el-dropdown trigger="click" @click.native.stop>
                  <i class="el-icon-arrow-down el-icon--right"></i>
                  <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item
                      v-for="item of quicklyOption"
                      @click.native="setQuicklyDate(item.value)"
                      :key="item.value"
                      >{{ item.label }}</el-dropdown-item
                    >
                  </el-dropdown-menu>
                </el-dropdown>
              </div>
            </span>
          </el-tab-pane>
          <el-tab-pane label="待付款" name="0"> </el-tab-pane>
          <el-tab-pane label="待发货" name="1"> </el-tab-pane>
          <el-tab-pane label="待提货" name="3"> </el-tab-pane>
          <el-tab-pane label="已完成" name="4"> </el-tab-pane>
          <el-tab-pane label="已关闭" name="5"> </el-tab-pane>
        </el-tabs>
        <DeliveryOrderTable
          @table-function="tableFunction"
          :data="orders"
          :query.sync="query"
          :checked-item.sync="checkedItem"
          :controlOption="getControlOption(query.orderStatus)"
        />
      </template>
    </m-container>

    <OrderDetailModal
      v-model="detailVisible"
      :detail="orderDetail"
      :need-tabs="true"
      :is-delivery="true"
      :type="detailType"
      @reset="handleSeeDetail"
    />

    <RemarkModal
      v-model="remarklVisible"
      title="备注"
      :current-order="currentOrder"
      :orderIds="orderIds"
      @onOk="handleRemarks"
      @onCancel="toggleRemarklVisible"
    />

    <DeliverySendModal
      v-model="sendModalVisible"
      :current-order="currentOrder"
      :logisticsPrinterVos="logisticsPrinterVos"
      :logistics-company="logisticsCompany"
      @reset="getLogisticsCompany"
      @onConfirm="confirm"
    />

    <DeliveryPrintModal
      v-model="printModalVisible"
      :logisticsPrinterVos="logisticsPrinterVos"
      :logistics-company="logisticsCompany"
      @reset="getLogisticsCompany"
      @onConfirm="confirm"
    />
  </div>
</template>

<script lang="ts">
import { Vue, Component, Watch } from "vue-property-decorator";
import DeliveryOrderFrom from "./components/DeliveryOrderFrom.vue";
import DeliveryOrderTable from "./components/DeliveryOrderTable.vue";
import OrderDetailModal from "./components/detail/Index.vue";
import DeliverySendModal from "./components/DeliverySendModal.vue";
import RemarkModal from "./components/RemarkModal.vue";
import DeliveryPrintModal from "./components/DeliveryPrintModal.vue";
import {
  DeliveryState,
  DeliveryOrderQueryType,
  DeliveryToolOptions,
  DeliveryOrderList,
  ApiOrderDetail,
  LogisticsCompanyType,
  LogisticsPrinterVos,
} from "./orderType";

import { deliveryOrderQuery, filterEmpty } from "./common/order";
import { getLogisticsCompany } from "@/api/logistics/logistics";

import {
  getOrders,
  getOrderDetail,
  remarks,
  close,
  printDeliver,
  getLogisticsWait,
} from "@/api/order";
import cloneDeep from "lodash/cloneDeep";
import { isClose } from "./common/order";
import { Dictionary } from "vue-router/types/router";

Component.registerHooks(["beforeRouteEnter", "beforeRouteUpdate"]);

@Component({
  components: {
    DeliveryOrderFrom,
    OrderDetailModal,
    RemarkModal,
    DeliveryOrderTable,
    DeliverySendModal,
    DeliveryPrintModal,
  },
})
export default class DeliveryOrder extends Vue implements DeliveryState {
  query = { ...deliveryOrderQuery } as DeliveryOrderQueryType;

  defaultContorlOption: Array<DeliveryToolOptions> = [
    {
      label: "批量备注",
      value: "remark",
    },
  ];

  waitContorlOption: Array<DeliveryToolOptions> = [
    {
      label: "批量备注",
      value: "remark",
    },
  ];

  waitPayContorlOption: Array<DeliveryToolOptions> = [
    {
      label: "批量备注",
      value: "remark",
    },
    {
      label: "批量关闭",
      value: "close",
    },
  ];

  quicklyOption: Array<DeliveryToolOptions> = [
    { label: "近一个月订单", value: "0" },
    { label: "近三个月订单", value: "1" },
    { label: "全部订单", value: "2" },
  ];

  data: Array<DeliveryOrderList> = [];

  orderIds: Array<string | DeliveryOrderList | any> = [];

  detailVisible = false;

  remarklVisible = false;

  sendModalVisible = false;

  printModalVisible = false;

  orderDetail = {} as ApiOrderDetail;

  checkedItem: Array<{ orderId: string }> = [];

  currentOrder = {} as DeliveryOrderList;

  needTabs = false;

  detailType = "1";

  logisticsCompany: Array<LogisticsCompanyType> = [];

  logisticsPrinterVos: Array<LogisticsPrinterVos> = [];

  get orders() {
    return this.data.map(item => {
      item.close = isClose(item.status);
      return item;
    });
  }

  /** 表格选中条目ID组 */
  get selectedIds() {
    return this.checkedItem.map(item => item.orderId);
  }

  get quicklyName() {
    const quicklyOption = this.quicklyOption.find(
      item => item.value === this.query.quicklyDate,
    );
    return quicklyOption ? quicklyOption.label : "";
  }

  /** 监听query变化 */
  @Watch("query", { deep: true })
  handleQueryChange(v: DeliveryOrderQueryType) {
    this.getOrders(filterEmpty({ ...this.$route.query, ...v }));
  }

  /** 获取订单 */
  beforeRouteEnter(
    to: { query: DeliveryOrderQueryType },
    _form: any,
    next: (
      arg0: (vm: {
        query: DeliveryOrderQueryType;
        data: DeliveryOrderList[];
      }) => void,
    ) => void,
  ) {
    const query = filterEmpty(
      Object.assign(cloneDeep(deliveryOrderQuery), to.query),
    );
    getOrders(query)
      .then(res => {
        console.log(res);
        const { list = [], ...other } = res.data;
        next(vm => {
          Object.assign(vm.query, query, other);
          vm.data = list;
        });
      })
      .catch(() => {
        alert("列表获取失败,请稍后再试");
      });
  }

  created() {
    this.getLogisticsCompany();
  }

  /** 获取物流公司及打印机 */
  getLogisticsCompany() {
    getLogisticsCompany()
      .then(res => {
        this.logisticsCompany = res.data.logisticsCompanyVos;
        this.logisticsPrinterVos = res.data.LogisticsPrinterVos;
      })
      .catch(err => {
        this.$message.warning(err);
      });
  }

  /** 确认批量发货 */
  confirm(data: { orderIds: string | any[] }) {
    if (!data.orderIds.length) {
      data.orderIds = this.checkedItem.map(item => item.orderId);
    }

    printDeliver(data)
      .then(() => {
        this.$message.success("发货成功");
        this.getOrders(Object.assign({}, this.query, this.$route.query));
        this.hideModal();
      })
      .catch(err => {
        this.$message.warning(err || "发货失败");
      });
  }

  /** 查看物流 */
  viewLogistics() {
    this.detailType = "2";
    this.detailVisible = true;
  }

  /** 获取按钮选项 */
  getControlOption(type: string | number) {
    return (
      {
        ["-1"]: this.waitContorlOption,
        0: this.waitPayContorlOption,
        1: this.waitContorlOption,
      }[type] || this.defaultContorlOption
    );
  }

  /** 获取订单列表 */
  getOrders(
    q: DeliveryOrderQueryType & Dictionary<string | (string | null)[]>,
    cb?: (() => void) | undefined,
  ) {
    getOrders(q)
      .then(res => {
        const { list, ...other } = res.data;
        Object.assign(this.query, other);
        this.data = list;
        if (cb) {
          cb();
        }
      })
      .catch(err => {
        this.$message.warning(err || "订单列表获取失败");
      });
  }

  /**
   * 监听table传来的事件
   * @param {name} 事件名 remark | close | detail
   */
  tableFunction(name: string, data: DeliveryOrderList, isLogistics: boolean) {
    switch (name) {
      case "remark":
        return this.triggerRemark(data);
      case "close":
        return this.handleClose(data);
      case "detail":
        return this.handleSeeDetail(data, isLogistics);
      case "print":
        return this.togglePrintisible();
      case "send":
        this.send(data);
        return;
    }
  }

  /** 发货 */
  async send(data: DeliveryOrderList) {
    try {
      const { data: len } = await getLogisticsWait();

      if (!len) {
        return this.$message.info("当前暂无待发货订单");
      }

      const orderIds = this.checkedItem.map(item => item.orderId).join(",");

      const waitLen = this.orders.filter(
        item =>
          orderIds.includes(item.orderId) && item.status === "WAIT_FOR_SEND",
      ).length;

      if (!waitLen && !data) {
        return this.$message.info("您没有选择待发货订单");
      }

      data
        ? this.toggleSendVisible(data)
        : this.$router.push(`/order/delivery/send?orderIds=${orderIds}`);
    } catch (err) {
      this.$message.warning("未发货订单状态获取失败");
    }
  }

  /**
   * 查看详情
   */
  async handleSeeDetail(orderData: DeliveryOrderList, isLogistics: boolean) {
    if (isLogistics) {
      this.needTabs = true;
      this.detailType = "2";
    } else {
      this.detailType = "1";
      this.needTabs = false;
    }

    if (orderData.orderId) {
      this.toggleDetailModal();
    }

    getOrderDetail(orderData.orderId || orderData.id)
      .then(res => {
        this.orderDetail = res.data;
      })
      .catch(err => {
        this.$message.warning(err || "订单详情获取失败");
      });
    //
  }

  /**
   * 关闭
   * @param {orderData} 订单数据 如果没有参数为批量操作
   */
  handleClose(orderData?: any) {
    this.$confirm("确定关闭订单？", "提示", {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning",
    })
      .then(() => {
        const orderIds = orderData
          ? [orderData.orderId]
          : [...this.selectedIds];
        close(orderIds)
          .then(() => {
            this.getOrders(Object.assign({}, this.query, this.$route.query));
            this.$message.success("关闭成功");
          })
          .catch(err => {
            this.$message.warning(err || "关闭失败");
          });
      })
      .catch(() => {
        //
      });
  }

  /**
   * 备注
   * @param {orderData} 订单数据 如果没有参数为批量操作
   */
  handleRemarks(form: { note: string; over: boolean }) {
    remarks({ orderIds: this.orderIds, ...form })
      .then(() => {
        this.getOrders(Object.assign({}, this.query, this.$route.query));
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
      this.detailType = "1";
    }
  }

  toggleRemarklVisible() {
    this.remarklVisible = !this.remarklVisible;
  }

  hideModal() {
    this.sendModalVisible = false;

    this.printModalVisible = false;
  }

  toggleSendVisible(data: DeliveryOrderList) {
    this.currentOrder = data;
    this.sendModalVisible = !this.sendModalVisible;
  }

  togglePrintisible() {
    this.printModalVisible = !this.printModalVisible;
  }

  handleTabClick({ name: orderStatus }: { name: string }) {
    Object.assign(this.query, {
      ...deliveryOrderQuery,
      orderStatus,
      current: 1,
      deliverId: "",
      quicklyDate: this.query.quicklyDate,
    });
  }

  triggerRemark(orderData: DeliveryOrderList) {
    this.currentOrder = orderData;
    this.orderIds = this.currentOrder
      ? [this.currentOrder.orderId]
      : [...this.selectedIds];

    this.toggleRemarklVisible();
  }
}
</script>

<style lang="scss" scoped>
@import "@/assets/styles/order/order.scss";
</style>
