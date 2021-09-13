<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:11
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-09-01 15:48:25
-->
<template>
  <div>
    <div class="order__control--top">
      <div class="control--l">
        <el-button-group class="fix">
          <el-button
            plain
            @click="emitFun('remark')"
            type="primary"
            :disabled="!checkedItem.length"
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
      <div class="control--r">
        <slot> </slot>
      </div>
    </div>

    <m-table
      :data="data"
      :selection="true"
      :custom="true"
      :columns="columns"
      :needHoverBorder="true"
      :checked-item.sync="tableCheckedItem"
    >
      <template v-slot:header="{ row }">
        <div
          class="order--header"
          :class="isClose(row.status) && 'order__close'"
        >
          <el-tag class="header__tag">{{ getAfterName(row.type) }}单 </el-tag>
          <span style="margin-right: 50px">订单号：{{ row.no }}</span>
          <span style="margin-right: 50px">
            创建时间：{{ row.createTime }} </span
          ><span>
            <el-popover
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
              ></i>
            </el-popover>
          </span>
        </div>
      </template>

      <template v-slot:custom-body="{ row }">
        <tr>
          <td>
            <div class="table__goods">
              <div class="table__goods--image">
                <img :src="row.productPic" :alt="row.productName" />
              </div>
              <div class="table__goods--info">
                <div class="goods--name">
                  {{ row.productName | strFilter }}
                </div>
                <div class="goods--specs">
                  <span class="l">{{ row.specs }}</span>
                </div>
                <div class="goods--price">
                  <span> ￥{{ row.productPrice.toFixed(2) }} </span>
                  <span class="specs">
                    <span class="r">×{{ row.productQuantity }}</span>
                  </span>
                </div>
              </div>
            </div>
          </td>

          <td>
            <div class="table__user">
              <div class="user--name">
                <span>{{ row.userName }}</span> <br />
                ({{ row.receiverName }},{{ row.receiverPhone }})
              </div>
            </div>
          </td>

          <td>
            <div>
              {{ getDeliveryTypeName(row.deliveryType) }}，
              {{ row.receiverName }},{{ row.receiverPhone }}
              {{ row.receiverProvince }}
              {{ row.receiverCity }}
              {{ row.receiverRegion }}
              {{ row.receiverDetailAddress }}
            </div>
          </td>

          <td class="text--center">￥{{ row.refundAmount }}</td>

          <td class="text--center">
            <!-- <el-tag :type="getTypeIcon(row.status)">
              {{ getAfterStatusName(row.status) }}</el-tag> -->
            <p class="after__status">
              {{ getOrderStatusName(row.orderStatus) }}
            </p>
            <p>
              {{ getAfterName(row.type) }} {{ getAfterStatusName(row.status) }}
            </p>
          </td>

          <td>
            <template v-if="row.status === 'WAIT_FOR_BUSINESS_APPROVED'">
              <el-button-group class="fix">
                <el-button plain @click="emitFun('review', row)" type="primary"
                  >审核</el-button
                >
                <el-button plain class="dropdown__fix more" type="primary">
                  <el-dropdown
                    size="mini"
                    :disabled="!checkedItem.length"
                    trigger="hover"
                  >
                    <span class="dropdown__fix--more">...</span>
                    <el-dropdown-menu slot="dropdown">
                      <el-dropdown-item @click.native="emitFun('detail', row)"
                        >查看详情</el-dropdown-item
                      >
                    </el-dropdown-menu>
                  </el-dropdown>
                </el-button>
              </el-button-group>
            </template>
            <template v-else>
              <el-button plain @click="emitFun('detail', row)" type="primary"
                >查看详情</el-button
              >
            </template>
          </td>
        </tr>
      </template>
    </m-table>
    <div class="order__control--bottom" style="position: absolute">
      <div>
        <el-button-group class="fix" v-if="controlOption">
          <el-button
            plain
            @click="emitFun('remark')"
            type="primary"
            :disabled="!checkedItem.length"
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
    </div>
  </div>
</template>

<script lang="ts">
import { Vue, Component, Prop } from "vue-property-decorator";
import {
  isClose,
  getTypeIcon,
  strFilter,
  getDeliveryTypeName,
  getOrderStatusName,
} from "../common/order";

import { getAfterStatusName, getAfterName } from "../common/afterSale";
import { ApiAfterListType } from "../orderType";
import { NewDeliveryOrderQueryType } from "../AfterSaleOrder.vue";

@Component({
  filters: {
    strFilter,
  },
})
export default class OderTable extends Vue {
  /** 表格数据 */
  @Prop({
    type: Array,
    default() {
      return [];
    },
  })
  data!: ApiAfterListType[];

  /** 表格内查询按钮 */
  @Prop()
  controlOption!: any[];

  /** 查询条件 */
  @Prop()
  query!: NewDeliveryOrderQueryType;

  /** 父级已选条码 */
  @Prop({
    default() {
      return [];
    },
  })
  checkedItem!: ApiAfterListType[];

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

  // emitShowDetail(data) {
  //   this.form.sendBillId = data.deliverId;
  //   this.historyDialog = false;
  // }

  // onDeliverIdChange(v) {
  //   if (v === "-2") {
  //     this.historyDialog = true;
  //   } else {
  //     this.form.sendBillId = v;
  //   }
  // }

  /** 表头 */
  get columns() {
    let list = [
      {
        label: "商品",
        width: 200,
        coustomStyle: "text-align: left;",
      },
      {
        label: "客户",
        width: 200,
        coustomStyle: "text-align: left;",
      },
      {
        label: "地址",
        width: 112,
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
    return list;
  }

  /** 触发父级方法 */
  emitFun(name: string, data: ApiAfterListType[], ignore: boolean) {
    // 阻止未选中元素的批量操作
    if (!data && !this.checkedItem.length && !ignore) {
      return this.$message.info("请先选择条目");
    }
    this.$emit("table-function", name, data);
  }

  getOrderStatusName = getOrderStatusName;

  getDeliveryTypeName = getDeliveryTypeName;

  isClose = isClose;

  getTypeIcon = getTypeIcon;

  getAfterStatusName = getAfterStatusName;

  getAfterName = getAfterName;

  // removeBtnVisible(row) {
  //   return (
  //     row.status === "WAIT_FOR_SEND" &&
  //     row.sendBillName &&
  //     row.deliveryType !== "LOGISTICS"
  //   );
  // }
}
</script>

<style scoped>
.header__tag {
  border-radius: 0;
  margin-right: 10px;
}

.after__status {
  color: orange;
  margin-top: 10px;
}
</style>
