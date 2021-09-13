<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:11
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-25 14:05:17
-->
<template>
  <el-dialog :visible.sync="visible" class="detail__modal">
    <el-tabs :value="type" v-if="needTabs">
      <el-tab-pane v-if="detail" label="订单信息" name="1">
        <Info :detail="detail" @reset="reset" />
      </el-tab-pane>
      <el-tab-pane v-if="deliveryVisible && detail" label="物流信息" name="2">
        <Delivery :delivery-info="detail.orderDelivery" />
      </el-tab-pane>
      <el-tab-pane v-if="isAfter" label="售后信息" name="4">
        <AfterSaleInfo :detail="detail" :afterInfo="afterInfo" @reset="reset" />
      </el-tab-pane>
    </el-tabs>
    <Info :detail="detail" v-else />
  </el-dialog>
</template>

<script lang="ts">
import { Vue, Component, Prop } from "vue-property-decorator";
import Info from "./Info.vue";
import Delivery from "./Delivery.vue";
import AfterSaleInfo from "./AfterSaleInfo.vue";
import {
  DeliveryOrderList,
  ApiOrderDetail,
  ApiAfterListType,
} from "../../orderType";

@Component({
  components: {
    Info,
    Delivery,
    AfterSaleInfo,
  },
})
export default class OrderDetailModal extends Vue {
  /** 订单详情 */
  @Prop({
    default() {
      return {};
    },
  })
  detail!: ApiOrderDetail;

  /** 售后详情 */
  @Prop({
    default() {
      return {};
    },
  })
  afterInfo?: any;

  /** 是否需要tab */
  @Prop({
    default: true,
  })
  needTabs?: boolean;

  /**
   * 当前组件是否在快递订单页
   */
  @Prop({
    default: false,
  })
  isDelivery?: boolean;

  /** 当前是否所处售后工单 */
  @Prop({
    default: false,
  })
  isAfter?: boolean;

  @Prop()
  value!: boolean;

  @Prop({
    default: "1",
  })
  type!: string;

  @Prop({
    default: false,
  })
  isGroup?: boolean;

  get deliveryVisible() {
    const detail = this.detail;
    return detail && detail.orderDelivery && detail.orderDelivery.deliverySn;
  }

  /** 模态框显示隐藏 */
  get visible() {
    return this.value;
  }

  set visible(v) {
    this.$emit("input", v);
  }

  reset(data: DeliveryOrderList | ApiAfterListType) {
    this.$emit("reset", data);
  }
}
</script>

<style lang="scss">
.detail__modal {
  .el-dialog__body {
    padding: 10px 20px 30px;
  }
}
</style>
