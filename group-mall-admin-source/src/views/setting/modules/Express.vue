<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:11
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-25 14:34:43
-->
<template>
  <div>
    <el-form ref="form" :model="form" label-width="120px">
      <el-form-item label="快递商户id">
        <el-row>
          <el-col :span="11">
            <el-input v-model="form.kdAppId"></el-input>
          </el-col>
        </el-row>
      </el-form-item>
      <el-form-item label="快递API KEY">
        <el-row>
          <el-col :span="11">
            <el-input v-model="form.kdAppKey"></el-input>
          </el-col>
        </el-row>
      </el-form-item>
    </el-form>
    <el-button @click="setOrderSetting" style="margin-left:120px;"
      >保存</el-button
    >
  </div>
</template>

<script lang="ts">
import { Vue, Component } from "vue-property-decorator";
import { getLogisticsSetting, logisticsSetting } from "@/api/finance/index";

@Component
export default class Express extends Vue {
  form = {
    kdAppId: "",
    kdAppKey: "",
  };

  mounted() {
    this.getInit();
  }

  getInit() {
    getLogisticsSetting({})
      .then(res => {
        this.form = res.data;
      })
      .catch(err => {
        this.$message({
          type: "warning",
          message: err || "网络错误",
        });
      });
  }

  setOrderSetting() {
    if (!this.form.kdAppId) {
      this.$message({
        type: "warning",
        message: "快递商户id不能为空",
      });
      return;
    }
    if (!this.form.kdAppKey) {
      this.$message({
        type: "warning",
        message: "快递API KEY不能为空",
      });
      return;
    }
    logisticsSetting(this.form)
      .then(() => {
        this.$message.success("保存成功");
      })
      .catch(err => {
        this.$message({
          type: "warning",
          message: err || "网络错误",
        });
      });
  }
}
</script>

<style lang="scss" scoped></style>
