<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:10
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-23 18:04:03
-->
<template>
  <!-- 分隔符 -->
  <el-form :model="formData">
    <el-form-item label="颜色">
      <el-color-picker
        v-model="formData.borderColor"
        @change="colorSelectChange"
      ></el-color-picker>
    </el-form-item>
    <el-form-item label="边距">
      <el-radio-group v-model="formData.hasMargin">
        <el-radio :label="false">无边距</el-radio>
        <el-radio :label="true">左右留边</el-radio>
      </el-radio-group>
    </el-form-item>
    <el-form-item label="样式">
      <el-radio-group v-model="formData.borderStyle">
        <el-radio
          v-for="item in borderStyles"
          :key="item.value"
          :label="item.value"
          >{{ item.label }}</el-radio
        >
      </el-radio-group>
    </el-form-item>
  </el-form>
</template>

<script lang="ts">
import { Vue, Component, Prop } from "vue-property-decorator";
import Separator from "./Separator";

interface Sytles {
  label: string;
  value: string;
}

@Component
export default class SeparatorForm extends Vue {
  @Prop()
  formData!: Separator;

  borderStyles: Sytles[] = [
    {
      label: "实线",
      value: "solid",
    },
    {
      label: "虚线",
      value: "dashed",
    },
    {
      label: "点线",
      value: "dotted",
    },
  ];

  /**
   * @LastEditors: chuyinlong
   * @description: 颜色选择切换
   */

  colorSelectChange() {
    if (!this.formData.borderColor) {
      this.formData.borderColor = "#EEEEEE";
    }
  }
}
</script>
