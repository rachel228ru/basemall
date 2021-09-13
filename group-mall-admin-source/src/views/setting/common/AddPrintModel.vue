<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:11
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-25 09:29:51
-->
<template>
  <el-form
    :model="printForm"
    :rules="printRules"
    ref="printRef"
    label-width="100px"
    class="demo-ruleForm"
  >
    <el-form-item label="设备品牌" prop="equipment">
      <el-radio-group v-model="printForm.equipment" style="width:400px">
        <el-radio label="1">365云打印</el-radio>
        <el-radio label="2">飞鹅</el-radio>
        <el-radio label="3">易联云(不支持K1/K2/K3)</el-radio>
      </el-radio-group>
    </el-form-item>
    <el-form-item label="打印纸宽" prop="papeWidth">
      <el-radio label="1" v-model="printForm.papeWidth">58mm</el-radio>
    </el-form-item>
    <el-form-item label="设备名称" prop="name" style="width:500px">
      <el-input
        v-model="printForm.name"
        style="width:380px"
        placeholder="如结账单打印机、后厨打印机"
      ></el-input>
    </el-form-item>
    <el-form-item label="设备号码" prop="bodyNumber">
      <el-input
        v-model="printForm.bodyNumber"
        style="width:380px"
        placeholder="填写打印机底部的机器号"
      ></el-input>
    </el-form-item>
    <el-form-item label="设备秘钥" prop="bodyKey">
      <el-input
        v-model="printForm.bodyKey"
        style="width:380px"
        placeholder="填写打印机底部的秘钥"
      ></el-input>
    </el-form-item>
    <el-form-item label="一次打印" prop="onPrint">
      <el-radio-group v-model="printForm.onPrint" style="width:400px">
        <el-radio label="1">1张</el-radio>
        <el-radio label="2">2张</el-radio>
        <el-radio label="3">3张</el-radio>
        <el-radio label="4">4张</el-radio>
      </el-radio-group>
    </el-form-item>
  </el-form>
</template>

<script lang="ts">
import { ElForm } from "element-ui/types/form";
import { Vue, Component, Prop, Ref } from "vue-property-decorator";

@Component
export default class AddPrintModel extends Vue {
  @Prop({
    type: Boolean,
    default: {},
  })
  addPrintDialog!: boolean;

  @Ref()
  readonly printRef?: ElForm;

  printForm = {
    equipment: "1",
    papeWidth: "1",
    name: "",
    bodyNumber: "",
    bodyKey: "",
    onPrint: "1",
  };

  printRules = {
    equipment: [
      { required: true, message: "请选择设备型号", trigger: "change" },
    ],
    papeWidth: [
      { required: true, message: "请选择设备型号", trigger: "change" },
    ],
    name: [{ required: true, message: "设备名称不能为空", trigger: "blur" }],
    bodyNumber: [
      { required: true, message: "打印机机身号不能为空", trigger: "blur" },
    ],
    bodyKey: [
      { required: true, message: "打印机key不能为空", trigger: "blur" },
    ],
    onPrint: [{ required: true, message: "请选择打印张数", trigger: "change" }],
  };

  /**
   * 对整个表单进行校验
   */
  validate() {
    return (this.printRef as ElForm).validate();
  }

  /**
   * 移除表单项的校验结果
   */
  clearValidate() {
    (this.printRef as ElForm).clearValidate();
  }
}
</script>

<style lang="scss" scoped>
.demo-ruleForm {
  padding-left: 20px;
}
</style>
