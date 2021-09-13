<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:11
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-27 13:29:06
-->
<template>
  <m-card :needToggle="true" hide-text="展开搜索条件" show-text="收起搜索条件">
    <el-form ref="form" :model="form" label-width="90px">
      <el-row>
        <el-col :span="12">
          <el-form-item label="商品名称">
            <el-input
              v-model="form.goodsName"
              placeholder="请输入商品名称"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="订单号">
            <el-input
              v-model="form.orderId"
              placeholder="请输入订单号"
            ></el-input>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="12">
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
        <el-col :span="12">
          <el-form-item label="评价星级">
            <el-select
              style="width: 100%"
              v-model="form.rate"
              placeholder="请选择评价星级"
            >
              <el-option
                v-for="(star, i) of starOption"
                :key="i"
                :label="star.label"
                :value="star.value"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item>
        <el-button type="primary" @click="emitSearch">搜索</el-button>
      </el-form-item>
    </el-form>
  </m-card>
</template>

<script lang="ts">
import { Vue, Component, Watch, Prop } from "vue-property-decorator";
import { evaluationQuery } from "../common/order";
import { cloneDeep } from "lodash";
import { EvaluateQueryType } from "../orderType";

/** 订单顶部查询表单 */
@Component
export default class OrderFrom extends Vue {
  @Prop({
    default() {
      return {};
    },
  })
  value!: any;

  form: EvaluateQueryType = cloneDeep(evaluationQuery);

  payTime: Date[] = [];

  get starOption() {
    const list = Array.apply(null, Array(5)).map((item, i) => {
      return {
        label: `${i + 1}星`,
        value: i + 1,
      };
    }) as any[];
    list.unshift({
      label: "全部",
      value: "",
    });
    return list;
  }

  @Watch("payTime")
  handlePayTimehange(v: Date[]) {
    Object.assign(this.form, {
      payTimeStart: v ? v[0] : "",
      payTimeEnd: v ? v[1] : "",
    });
  }

  @Watch("value", { immediate: true })
  handleValueChange(v: boolean) {
    this.form = Object.assign({}, this.form, v);
  }

  emitSearch() {
    this.$emit("input", this.form);
  }
}
</script>
