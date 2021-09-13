<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:11
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-25 14:09:41
-->
<template>
  <m-card
    class="form"
    hide-text="展开搜索条件"
    show-text="收起搜索条件"
    :needToggle="true"
  >
    <el-form ref="form" :model="form" label-width="90px">
      <el-row>
        <el-col :span="8">
          <el-form-item label="商品名称">
            <el-input
              v-model="form.goodsName"
              placeholder="请输入商品名称"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="买家昵称">
            <el-input
              v-model="form.userName"
              placeholder="请输入买家昵称"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="收货人姓名">
            <el-input
              v-model="form.receiverName"
              placeholder="请输入收货人姓名"
            ></el-input>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="8">
          <el-form-item label="订单号">
            <el-input
              v-model="form.orderId"
              placeholder="请输入订单号"
            ></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="8">
          <el-form-item label="备注状态">
            <el-select
              v-model="form.remarkType"
              placeholder="请选择备注状态"
              style="width:223px"
            >
              <el-option
                v-for="item of remarkOption"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="配送方式">
            <el-select
              v-model="form.deliverType"
              placeholder="请选择配方式"
              style="width:223px"
            >
              <el-option
                v-for="item of deliverTypes"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="8">
          <el-form-item label="销售专区">
            <el-select
              v-model="form.area"
              placeholder="请选择销售专区"
              style="width:223px"
            >
              <el-option
                v-for="item of areas"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>

        <el-col :span="8">
          <el-form-item label="成交时间">
            <el-date-picker
              v-model="payTime"
              type="daterange"
              value-format="yyyy-MM-dd"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
            >
            </el-date-picker>
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item>
        <el-button type="primary" @click="emitSeach">搜索</el-button>
      </el-form-item>
    </el-form>
  </m-card>
</template>

<script lang="ts">
import { Vue, Component, Watch, Prop } from "vue-property-decorator";
import { orderQuery } from "../common/order";
import cloneDeep from "lodash/cloneDeep";

/** 订单顶部查询表单 */
@Component
export default class OrderFrom extends Vue {
  @Prop()
  value;

  form = cloneDeep(orderQuery);

  payTime = [];

  /** 配送类型 */
  deliverTypes = [
    {
      label: "物流配送",
      value: "102",
    },
  ];

  /** 备注选项 */
  remarkOption = [
    {
      label: "全部状态",
      value: "0",
    },
    {
      label: "有备注",
      value: "1",
    },
    {
      label: "无备注",
      value: "2",
    },
  ];

  areas = [
    {
      label: "商超",
      value: "2",
    },
  ];

  /** 监听数据变化 维护本地查询条件 */
  @Watch("value", { immediate: true, deep: true })
  handleFormChange(v) {
    this.form = cloneDeep(v);
  }

  @Watch("payTime")
  handlePayTimehange(v) {
    Object.assign(this.form, { startTime: v[0], endTime: v[1] });
  }

  /** 触发父级查询 */
  emitSeach() {
    this.$emit("input", this.form);
    // this.$emit("search", this.form);
  }
}
</script>

<style lang="scss" scoped>
@include b(form) {
  transform-origin: left top;
  overflow: hidden;
  position: relative;
  transition: all 0.3s ease 0s;

  &.show {
    height: 304px;
  }

  &.hide {
    height: 50px;

    .form__btn {
      width: 940px;
      height: 50px;
      background: #f9f9f9;
      line-height: 50px;
      // margin-top: 20px
    }
  }

  @include e(btn) {
    width: 100%;
    position: absolute;
    bottom: 0;
    text-align: center;
    padding-bottom: 20px;

    span {
      cursor: pointer;
    }
  }
}
</style>
