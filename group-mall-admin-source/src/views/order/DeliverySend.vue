<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:11
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-25 14:14:33
-->
<template>
  <m-container class="send" :pagination-visible="false">
    <template slot="form">
      <div class="send__search">
        <div>
          <el-form ref="form" :model="lQuery" label-width="80px">
            <el-form-item label="发货方式" required>
              <el-radio-group v-model="lQuery.type">
                <el-radio label="手动发货"></el-radio>
                <el-radio label="打印快递单并发货"></el-radio>
                <el-radio label="无需物流"></el-radio>
              </el-radio-group>
            </el-form-item>

            <template v-if="!isNotLogistics">
              <el-form-item label="物流公司" required>
                <el-select
                  style="width:250px"
                  v-model="lQuery.deliverCode"
                  placeholder="请选择"
                >
                  <el-option
                    v-for="item of company"
                    :label="item.name"
                    :key="item.code"
                    :value="item.code"
                  >
                    {{ item.name }}
                  </el-option>
                </el-select>
                <el-link
                  v-if="!isManual"
                  type="primary"
                  :underline="false"
                  style="margin-left:4px"
                  @click="setDefaultAddress"
                  :disabled="deliverCodeBtnStatus"
                  >设为默认</el-link
                >
              </el-form-item>
              <template v-if="!isManual">
                <el-form-item label="打印机" required>
                  <el-select
                    style="width:250px"
                    v-model="lQuery.printCode"
                    placeholder="选择打印机"
                  >
                    <el-option
                      v-for="item of logisticsPrinterVos"
                      :key="item.deviceKey"
                      :label="item.deviceName"
                      :value="item.deviceKey"
                    ></el-option>
                  </el-select>
                </el-form-item>
                <el-form-item label="发货地址" required>
                  <el-select
                    v-model="lQuery.expressId"
                    :disabled="!logisticsAddressVos.length"
                    placeholder="选择发货地址"
                    style="width:250px"
                  >
                    <el-option
                      v-for="item of logisticsAddressVos"
                      :key="item.code"
                      :label="item.address"
                      :value="item.expressId"
                    ></el-option>
                  </el-select>
                </el-form-item>
              </template>
            </template>
          </el-form>
        </div>
        <div>
          <el-link type="primary" :underline="false" @click="handleImportAll"
            >导入全部待发货订单</el-link
          >
        </div>
      </div>
    </template>

    <m-table :data="tableData" :custom="true" :columns="columns" slot="content">
      <template v-slot:header="{ row, index }">
        <div style="width:100%">
          <span style="margin-right:50px">订单号：{{ row.orderId }}</span>
          <span style="margin-right:50px">
            创建时间：{{ row.createTime }}
          </span>
          <el-link
            type="primary"
            style="float:right"
            :underline="false"
            @click="handleRemove(index)"
            >移除</el-link
          >
        </div>
      </template>
      <template v-slot:custom-body="{ row, index }">
        <template v-for="(goods, i) of row.itemVoList">
          <tr :key="i">
            <td>
              <div class="table__goods">
                <div class="table__goods--image">
                  <img :src="goods.productPic" :alt="goods.productName" />
                </div>
                <div class="table__goods--info">
                  <div class="goods--name">
                    {{ goods.productName }}
                  </div>
                  <div class="goods--specification">
                    <span class="l">{{ goods.specs }}</span>
                    <span class="r">*{{ goods.productQuantity }}</span>
                  </div>
                  <div class="goods--price">
                    ￥{{ goods.productPrice.toFixed(2) }}
                  </div>
                </div>
              </div>
            </td>

            <td v-if="i === 0" :rowspan="row.itemVoList.length">
              <div class="table__user">
                <div class="user--head">
                  <img :src="row.userAvatarUrl" :alt="row.userName" />
                </div>
                <div class="user--name">{{ row.userName }}</div>
              </div>
            </td>

            <td v-if="i === 0 && isManual" :rowspan="row.itemVoList.length">
              <el-form class="send__form" label-width="80px">
                <el-form-item label="选择物流" required>
                  <el-select
                    v-model="row.localLogisticsBatchDeliverDtos.deliverCode"
                    @input="onItemChange($event, 'deliverCode', index)"
                    placeholder="选择物流"
                    style="width:100%"
                  >
                    <el-option
                      v-for="item of logisticsCompany"
                      :key="item.code"
                      :label="item.name"
                      :value="item.code"
                    ></el-option>
                  </el-select>
                </el-form-item>
                <el-form-item label="运单号码" required>
                  <el-input
                    v-model="row.localLogisticsBatchDeliverDtos.logisticsCode"
                    @input="onItemChange($event, 'logisticsCode', index)"
                  ></el-input>
                </el-form-item>
              </el-form>
            </td>
          </tr>
        </template>
      </template>
    </m-table>
    <template slot="other">
      <div
        class="send__bottom"
        :style="{ left: bottomLeft + 'px' }"
        v-if="bottomLeft"
      >
        <el-button @click="cancel">取消</el-button>
        <el-button @click="confirm" type="primary" style="margin-left:10px"
          >确认</el-button
        >
      </div>
    </template>
  </m-container>
</template>

<script lang="ts">
import { Vue, Component, Watch } from "vue-property-decorator";
import { searchLogistics, printDeliver, logisticsSend } from "@/api/order";
import { getDeliveryTypeName } from "./common/order";
import { getLogisticsCompany, bulkShipment } from "@/api/logistics/logistics";
import { setDefaultAddress } from "@/api/logistics/logistics";
import cloneDeep from "lodash/cloneDeep";

@Component
export default class DeliverySend extends Vue {
  /** 物流公司列表 */
  logisticsCompany = [];

  /**
   * 处理物流公司列表，增加默认标记
   */
  get company() {
    const logisticsCompany = this.logisticsCompany;
    return cloneDeep(logisticsCompany).map(item => {
      if (item.isDefault === 1) {
        item.name = item.name + (this.isManual ? "" : "(默认)");
      }
      return item;
    });
  }

  /** 物流公司map */
  get logisticsCompanyMap() {
    const map = {};

    this.logisticsCompany.forEach(item => {
      map[item.code] = item.name;
    });

    return map;
  }

  /** 表格数据 */
  tableData = [];

  /**
   * 打印机
   */
  logisticsPrinterVos = [];

  /** 是否为手动发货 */
  get isManual() {
    return this.lQuery.type === "手动发货";
  }

  /**
   * 是否为无需物流发货
   */
  get isNotLogistics() {
    return this.lQuery.type === "无需物流";
  }

  /** 打印快递单并发货form */
  lQuery = {
    type: "手动发货",
    printCode: "",
    deliverCode: "",
    expressId: "",
  };

  bottomLeft = 0;

  /** 表头 */
  get columns() {
    const columns = [
      {
        label: "商品",
        coustomStyle: "text-align: left;",
      },
      {
        label: "客户",
        coustomStyle: "text-align: left;",
        width: 245,
      },
    ];

    if (this.isManual) {
      return [
        ...columns,
        {
          label: "操作",
          width: 255,
        },
      ];
    }
    return columns;
  }

  /** 设置默认按钮状态 */
  get deliverCodeBtnStatus() {
    return (
      !this.lQuery.deliverCode ||
      (this.defLogisticsCompany &&
        this.defLogisticsCompany.code === this.lQuery.deliverCode)
    );
  }

  /** 默认发货地址 */
  get logisticsAddressVos() {
    try {
      const { logisticsExpressAddressVos } = this.logisticsCompany.find(
        item => item.code === this.lQuery.deliverCode,
      );
      return logisticsExpressAddressVos;
    } catch (err) {
      return [];
    }
  }

  /** 默认物流公司 */
  get defLogisticsCompany() {
    return this.logisticsCompany.find(item => item.isDefault === 1) || {};
  }

  /** 监听deliverCode变化，批量设置列表deliverCode */
  @Watch("lQuery.deliverCode")
  handleCompanyChage(v) {
    this.tableData = this.tableData.map(item => {
      item.localLogisticsBatchDeliverDtos.deliverCode = v;
      return item;
    });
  }

  /** 监听物流公司列表变化，设置form中的默认物流公司 */
  @Watch("logisticsCompany", { immediate: true })
  handleVisibleChange(v) {
    if (v) {
      this.lQuery.deliverCode = this.defLogisticsCompany.code;
    }
  }

  created() {
    this.searchDeliveryOrders();
    this.getLogisticsCompany();
  }

  mounted() {
    this.setCtlPosition();
    window.onresize = () => this.setCtlPosition();
  }

  beforeDestroy() {
    window.onresize = null;
  }

  /** 设置操作栏UI位置 */
  setCtlPosition() {
    const el = document.querySelector(".send") as HTMLElement;
    this.bottomLeft = el.offsetLeft - 15;
  }

  /** 设置默认发货公司 */
  setDefaultAddress() {
    const { logisticsCompanyId } = this.logisticsCompany.find(
      item => item.code === this.lQuery.deliverCode,
    );
    setDefaultAddress(logisticsCompanyId)
      .then(() => {
        this.getLogisticsCompany();
        this.$message.success("设置成功");
      })
      .catch(err => {
        this.$message.warning(err);
      });
  }

  /** 移除 */
  handleRemove(index) {
    this.tableData.splice(index, 1);
  }

  /** 导入所有未发货订单 */
  handleImportAll() {
    this.searchDeliveryOrders(
      {
        orderIds: "",
      },
      () => {
        this.$message.success("导入成功");
      },
    );
  }

  /** 查询订单 */
  searchDeliveryOrders(
    query = {
      orderIds: this.$route.query.orderIds,
    },
    cb?: any,
  ) {
    searchLogistics(query)
      .then(res => {
        this.tableData = this.addLogisticsData(res.data);
        cb && cb();
      })
      .catch(err => {
        this.$message.warning(err);
      });
  }

  /**
   * 获取物流公司与打印机
   */
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

  /** 处理条漫物流公司变化 */
  onItemChange(value, key, index) {
    this.tableData[index].localLogisticsBatchDeliverDtos[key] = value;
  }

  /**
   * 添加物流的默认数据
   * @param arr 订单列表
   */
  addLogisticsData(arr: any) {
    return arr.map(item => {
      item.localLogisticsBatchDeliverDtos = {
        deliverCode: this.lQuery.deliverCode,
        deliverName: "",
        logisticsCode: "",
        orderId: item.orderId,
      };
      return item;
    });
  }

  /** 取消批量发货 */
  cancel() {
    this.$router.back();
  }

  /** 确认按钮点击事件 */
  confirm() {
    if (this.isManual) return this.manualSend();
    if (this.isNotLogistics) return this.logisticsSend();
    this.printSend();
  }

  goBack() {
    this.$router.replace("/order/delivery");
  }

  /** 打印发货 */
  printSend() {
    const data = Object.assign({}, this.lQuery, {
      orderIds: this.tableData.map(item => item.orderId),
    });

    printDeliver(data)
      .then(() => {
        this.$message.success("发货成功");
        this.goBack();
      })
      .catch(err => {
        this.$message.warning(err || "发货失败");
      });
  }

  /** 无需物流发货 */
  logisticsSend() {
    const data = this.tableData.map(item => item.orderId).join(",");

    logisticsSend(data)
      .then(() => {
        this.$message.success("发货成功");
        this.goBack();
      })
      .catch(err => {
        console.log(err);

        this.$message.warning(err || "发货失败");
      });
  }

  /** 手动发货 */
  manualSend() {
    const logisticsBatchDeliverDtos = this.tableData
      .map(item => item.localLogisticsBatchDeliverDtos)
      .map(item => {
        item.deliverName = this.logisticsCompanyMap[item.deliverCode];
        return item;
      });

    const hasDataLen = logisticsBatchDeliverDtos.filter(
      item => !!item.logisticsCode,
    ).length;

    if (hasDataLen === 0) {
      return this.$message.warning("请填写单号");
    }

    const request = () =>
      bulkShipment(logisticsBatchDeliverDtos.filter(item => item.logisticsCode))
        .then(() => {
          this.$message.success("发货成功");
          this.goBack();
        })
        .catch(err => {
          this.$message.warning(err || "批量发货失败");
        });

    if (hasDataLen !== logisticsBatchDeliverDtos.length) {
      return this.$confirm("存在订单未填写单号，是否继续", "提示").then(() => {
        request();
      });
    }
    request();
  }

  getDeliveryTypeName = getDeliveryTypeName;
}
</script>

<style lang="scss" scoped>
@import "@/assets/styles/order/order.scss";
@include b(send) {
  padding-bottom: 50px;
  min-height: 720px;
  padding: 10px 15px 100px;

  \deep\  .el-input-group__prepend .el-select {
    min-width: 100px;
  }

  \deep\ .el-pagination__sizes {
    position: relative !important;
    top: -5px !important;
  }

  @include e(search) {
    @include flex(space-between);
  }

  @include e(form) {
    .el-form-item {
      margin: 5px 0;
    }
  }

  .send__bottom {
    @include flex();

    width: 1010px;
    padding: 15px 0px;
    box-shadow: 0 0px 10px 0px #d5d5d5;
    position: fixed;
    // bottom: -20px;
    // left: -15px;
    z-index: 9;
    bottom: 10px;
    background: #fff;
  }
}

.m__table--body .body--content td:first-child {
  border-right: 1px solid #d8eaf9 !important;
}
</style>
