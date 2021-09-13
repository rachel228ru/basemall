<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:11
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-25 14:10:53
-->
<template>
  <div>
    <div class="order__control--top">
      <div class="control--l">
        <el-button-group class="fix" v-if="controlOption">
          <el-button plain @click="emitFun('remark')" type="primary"
            >批量备注</el-button
          >
          <el-button
            v-if="controlOption && controlOption.length"
            plain
            class="dropdown__fix more"
            type="primary"
          >
            <el-dropdown
              size="mini"
              v-if="controlOption"
              :disabled="!checkedItem.length"
              trigger="click"
            >
              <span class="dropdown__fix--more">...</span>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item
                  v-for="(item, i) of controlOption"
                  :key="i"
                  @click.native="emitFun(item.value, null, item.ignore)"
                  >{{ item.label }}</el-dropdown-item
                >
              </el-dropdown-menu>
            </el-dropdown>
          </el-button>
        </el-button-group>
      </div>
    </div>

    <m-table
      :data="data"
      :selection="true"
      :custom="true"
      :columns="columns"
      :needHoverBorder="true"
      :checked-item.sync="tableCheckedItem"
      multipleKey="itemVoList"
    >
      <template v-slot:header="{ row }">
        <div
          class="order--header"
          :class="isClose(row.status) && 'order__close'"
        >
          <el-tag class="header__tag" v-if="getAfterName(row.type)"
            >{{ getAfterName(row.type) }}单
          </el-tag>
          <el-tag
            class="header__tag"
            v-if="row.privileges && row.privileges.includes('5')"
            >优先发货
          </el-tag>
          <span style="margin-right: 50px">订单号：{{ row.orderId }}</span>
          <span style="margin-right: 50px">
            创建时间：{{ row.createTime }}
          </span>
          <span
            ><el-popover
              placement="bottom-start"
              width="200"
              trigger="hover"
              :disabled="!row.note"
            >
              <div>备注：{{ row.note }}</div>
              <i
                slot="reference"
                class="iconfont iconPC-beizhu"
                :class="{ red: row.note }"
                @click="emitFun('remark', row)"
              ></i> </el-popover
          ></span>
        </div>
      </template>

      <template v-slot:custom-body="{ row }">
        <template v-for="(goods, i) of row.itemVoList">
          <tr :key="i">
            <td>
              <div class="table__goods">
                <div class="table__goods--image">
                  <img :src="goods.productPic" :alt="goods.productName" />
                </div>
                <div class="table__goods--info">
                  <div class="goods--name">
                    {{ goods.productName | strFilter }}
                  </div>
                  <div class="goods--specs">
                    <span class="l">{{ goods.specs }}</span>
                  </div>
                  <div class="goods--price">
                    <span> ￥{{ goods.productPrice.toFixed(2) }} </span>
                    <span class="specs">
                      <span class="r">×{{ goods.productQuantity }}</span>
                    </span>
                  </div>
                </div>
              </div>
            </td>

            <td v-if="i === 0" :rowspan="row.itemVoList.length">
              <div class="table__user">
                <div class="user--name">
                  <span>{{ row.userName }}</span> <br />
                  ({{ row.receiverName }},{{ row.receiverPhone }})
                </div>
              </div>
            </td>

            <td
              v-if="i === 0"
              :rowspan="row.itemVoList.length"
              class="text--center"
            >
              ￥{{ row.payAmount }}
            </td>

            <td
              v-if="i === 0"
              :rowspan="row.itemVoList.length"
              class="text--center"
            >
              <el-tag :type="getTypeIcon(row.status)">
                {{ getOrderStatusName(row.status) }}</el-tag
              >
            </td>

            <td
              v-if="i === 0"
              :rowspan="row.itemVoList.length"
              class="text--center"
            >
              <span v-html="formatSendBillName(row.sendBillName)"></span>
            </td>

            <td v-if="i === 0" :rowspan="row.itemVoList.length">
              <el-button-group class="fix">
                <el-button plain @click="emitFun('detail', row)" type="primary"
                  >查看详情</el-button
                >
                <el-button
                  v-if="row.status === 'WAIT_FOR_PAY'"
                  plain
                  class="dropdown__fix more"
                  type="primary"
                >
                  <el-dropdown
                    size="mini"
                    :disabled="!checkedItem.length"
                    trigger="hover"
                  >
                    <span class="dropdown__fix--more">...</span>
                    <el-dropdown-menu slot="dropdown">
                      <el-dropdown-item
                        v-if="row.status === 'WAIT_FOR_PAY'"
                        @click.native="emitFun('close', row)"
                        >关闭订单
                      </el-dropdown-item>
                    </el-dropdown-menu>
                  </el-dropdown>
                </el-button>
              </el-button-group>
            </td>
          </tr>
        </template>
      </template>
    </m-table>
    <div class="order__control--bottom fixed">
      <div>
        <el-button-group class="fix" v-if="controlOption">
          <el-button plain @click="emitFun('remark')" type="primary"
            >批量备注</el-button
          >
          <el-button
            v-if="controlOption && controlOption.length"
            plain
            class="dropdown__fix"
            type="primary"
          >
            <el-dropdown
              size="mini"
              v-if="controlOption"
              :disabled="!checkedItem.length"
              trigger="hover"
            >
              <span class="dropdown__fix--more">...</span>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item
                  v-for="(item, i) of controlOption"
                  :key="i"
                  @click.native="emitFun(item.value, null, item.ignore)"
                  >{{ item.label }}</el-dropdown-item
                >
              </el-dropdown-menu>
            </el-dropdown>
          </el-button>
        </el-button-group>
        <el-button
          v-else
          type="primary"
          @click="emitFun('remark')"
          :disabled="!checkedItem.length"
          >批量备注</el-button
        >
      </div>
      <el-pagination
        small
        layout="total,  prev, pager, next,  sizes"
        :current-page.sync="form.current"
        :size.sync="form.size"
        :page-size.sync="form.size"
        :page-sizes="[10, 20, 50, 100]"
        :total.sync="form.total"
      >
      </el-pagination>
    </div>
    <HistoricalInvoice
      :historyDialog.sync="historyDialog"
      @showDetail="emitShowDetail"
      :show-export="false"
    >
    </HistoricalInvoice>
  </div>
</template>

<script lang="ts">
import { Vue, Component, Prop } from "vue-property-decorator";
import {
  getOrderStatusName,
  getDeliveryTypeName,
  isClose,
  getTypeIcon,
  strFilter,
} from "../common/order";
import HistoricalInvoice from "@/views/order/directInvoice/components/HistoricalInvoice.vue";
import { getAfterName } from "../common/afterSale";

@Component({
  filters: {
    strFilter,
  },
  components: {
    HistoricalInvoice,
  },
})
export default class OderTable extends Vue {
  /** 表格数据 */
  @Prop({
    type: Array,
  })
  data: any[];

  /** 表格内查询按钮 */
  @Prop()
  controlOption: any[];

  /** 查询条件 */
  @Prop()
  query: any;

  /** 父级已选条码 */
  @Prop()
  checkedItem: any;

  /** 本地form 主要用户页码切换 */
  get form() {
    return this.query;
  }

  set form(v) {
    this.$emit("update:query", v);
  }

  /** 已选表格选项 */
  get tableCheckedItem() {
    return this.checkedItem || [];
  }

  set tableCheckedItem(v) {
    this.$emit("update:checked-item", v);
  }

  historyDialog = false;

  emitShowDetail(data) {
    this.form.sendBillId = data.deliverId;
    this.historyDialog = false;
  }

  onDeliverIdChange(v) {
    if (v === "-2") {
      this.historyDialog = true;
    } else {
      this.form.sendBillId = v;
    }
  }

  /** 表头 */
  columns = [
    {
      label: "商品",
      width: 270,
      coustomStyle: "text-align: left;",
    },
    {
      label: "客户",
      width: 220,
      coustomStyle: "text-align: left;",
    },
    {
      label: "交易额",
      width: 94,
    },
    {
      label: "订单状态",
      width: 115,
    },
    {
      label: "操作",
      width: 140,
    },
  ];

  /** 触发父级方法 */
  emitFun(name, data, ignore) {
    // 阻止未选中元素的批量操作
    if (!data && !this.checkedItem.length && !ignore) {
      return this.$message.info("请先选择条目");
    }
    this.$emit("table-function", name, data);
  }

  /** 物流按钮显示隐藏 */
  logisticsBtnVisible(item) {
    const other = ["WAIT_FOR_PAY", "WAIT_FOR_SEND", "COMPLETE"];
    return (
      !isClose(item.status) &&
      !other.includes(item.status) &&
      item.deliveryType === "LOGISTICS"
    );
  }

  getOrderStatusName = getOrderStatusName;

  getDeliveryTypeName = getDeliveryTypeName;

  isClose = isClose;

  getTypeIcon = getTypeIcon;

  getAfterName = getAfterName;
}
</script>

<style lang="scss">
.header__tag {
  border-radius: 0;
  margin-right: 10px;
}

.fixed {
  @include flex(space-between);
  position: fixed;
  bottom: 10px;
  width: 990px !important;
  z-index: 10;
  background: #fff;
  padding: 10px 0;
}
</style>
