<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:11
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-25 14:05:11
-->
<template>
  <div class="delivery">
    <el-row :gutter="10" class="delivery__field">
      <el-col :span="24">
        <b>物流信息</b>
      </el-col>
      <el-col :span="24">
        收货地址： {{ deliveryInfo.receiverProvince }}
        {{ deliveryInfo.receiverCity }}
        {{ deliveryInfo.receiverRegion }}
        {{ deliveryInfo.receiverDetailAddress }}
      </el-col>
      <el-col :span="24">物流公司：{{ deliveryInfo.deliveryCompany }}</el-col>
      <el-col :span="24">物流单号：{{ deliveryInfo.deliverySn }}</el-col>
    </el-row>
    <div class="delivery__detail">
      <b>物流详情</b>
      <div class="empty" v-if="!info.length">暂无物流详情信息</div>
      <el-steps v-else direction="vertical" :active="acitve">
        <el-step v-for="(item, i) of info" :key="i">
          <div slot="icon">{{ info.length - i }}</div>
          <div slot="title">{{ item.context }}</div>
          <div slot="description">{{ item.time }}</div>
        </el-step>
      </el-steps>
    </div>
  </div>
</template>

<script lang="ts">
import { Vue, Component, Watch, Prop } from "vue-property-decorator";
import { ApiOrderDeliveryType } from "../../orderType";
import { getdeliveryInfo } from "@/api/order/index";

@Component
export default class Delivery extends Vue {
  @Prop({
    default() {
      return {};
    },
  })
  deliveryInfo!: ApiOrderDeliveryType;

  info = [];

  get acitve() {
    return 0;
  }

  @Watch("deliveryInfo")
  handleChnage(v: ApiOrderDeliveryType) {
    if (v) {
      this.getDetail(v);
    } else {
      this.info = [];
    }
  }

  mounted() {
    this.getDetail(this.deliveryInfo);
  }

  getDetail(v: ApiOrderDeliveryType) {
    getdeliveryInfo({
      tracesId: v.deliverySn,
      companyName: v.deliveryCompany,
      deliveryCode: v.deliveryCode,
    })
      .then(res => {
        this.info = JSON.parse(res.data) || [];
      })
      .catch(() => {
        this.info = [];
      });
  }
}
</script>

<style lang="scss" scoped>
@include b(delivery) {
  @include e(field) {
    font-size: 13px;
    border-bottom: 1px solid #ccc;

    .el-col-24 {
      margin-bottom: 10px;
    }
  }

  @include e(detail) {
    margin-top: 15px;

    .el-steps {
      margin-top: 10px;
    }

    .el-step {
      min-height: 80px;
    }

    .empty {
      width: 100%;
      height: 34px;
      line-height: 34px;
      background-color: rgba(233, 243, 251, 1);
      box-shadow: none;
      color: #586884;
      text-align: left;
      margin-top: 10px;
      padding-left: 10px;
    }
  }
}
</style>
