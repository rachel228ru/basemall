<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:10
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-23 09:40:42
-->
<template>
  <!-- 空白占位 -->
  <el-form :model="formData" label-width="80px">
    <el-form-item label="空白高度">
      <el-row type="flex" align="middle" :gutter="10">
        <el-col :span="18">
          <el-slider
            v-model="formData.height"
            :max="100"
            :min="10"
            :show-tooltip="false"
          ></el-slider>
        </el-col>
        <el-col :span="6">{{ formData.height }}像素</el-col>
      </el-row>
    </el-form-item>
  </el-form>
</template>

<script lang="ts">
import { Vue, Component, Prop } from "vue-property-decorator";
import BlankPaceholder from "./BlankPaceholder";

@Component
export default class BlankPaceholderForm extends Vue {
  @Prop()
  formData?: BlankPaceholder;
}
</script>
