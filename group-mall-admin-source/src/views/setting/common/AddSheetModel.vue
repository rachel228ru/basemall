<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:11
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-25 14:34:30
-->
<template>
  <el-form
    :model="sheetForm"
    :rules="sheetRules"
    ref="sheetRef"
    label-width="100px"
    class="demo-ruleForm"
  >
    <el-form-item label="设备类型" prop="deviceType">
      <el-select
        v-model="sheetForm.deviceType"
        placeholder="选择设备"
        style="width:250px"
      >
        <el-option
          v-for="item in sheetList"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        ></el-option>
      </el-select>
    </el-form-item>
    <el-form-item label="设备型号" prop="deviceModel">
      <el-select
        v-model="sheetForm.deviceModel"
        placeholder="选择设备"
        style="width:250px"
      >
        <el-option
          v-for="item in valueList"
          :key="item.value"
          :label="item.label"
          :value="item.label"
        ></el-option>
      </el-select>
    </el-form-item>
    <el-form-item label="设置名称" prop="deviceName" style="width:500px">
      <el-input
        v-model="sheetForm.deviceName"
        style="width:250px"
        placeholder="填写设备备注名称"
      ></el-input>
    </el-form-item>
    <el-form-item label="打印机身号" prop="deviceNo">
      <el-input
        v-model="sheetForm.deviceNo"
        style="width:250px"
        placeholder="填写打印机机身号"
      ></el-input>
    </el-form-item>
    <el-form-item label="打印机KEY" prop="deviceKey">
      <el-input
        v-model="sheetForm.deviceKey"
        style="width:250px"
        placeholder="填写打印机Key"
      ></el-input>
    </el-form-item>
  </el-form>
</template>

<script lang="ts">
import { LogisticsPrinterVos } from "@/views/marketing/integralMall/integralOrder/components/product/productTableType";
import { ElForm } from "element-ui/types/form";
import { Vue, Component, Prop, Watch, Ref } from "vue-property-decorator";
import PrintSetting from "../modules/PrintSetting.vue";

@Component
export default class AddSheetModel extends Vue {
  @Prop({
    type: Boolean,
    default: {},
  })
  addDialog!: boolean;

  @Prop({
    type: Object,
    default: {},
  })
  editCurrent!: LogisticsPrinterVos;

  @Watch("addDialog")
  getAddDialog() {
    if (this.editCurrent.id) {
      this.editData();
    } else {
      this.initData();
    }
  }

  @Ref()
  readonly sheetRef?: ElForm;

  sheetForm = {
    deviceType: "打印机",
    deviceModel: "热敏条码云打印机 356 M2",
    deviceName: "",
    deviceNo: "",
    deviceKey: "",
    status: "1",
    id: "",
  };

  sheetRules = {
    deviceType: [
      { required: true, message: "请选择设备类型", trigger: "change" },
    ],
    deviceModel: [
      { required: true, message: "请选择设备型号", trigger: "change" },
    ],
    deviceName: [
      { required: true, message: "设备名称不能为空", trigger: "blur" },
    ],
    deviceNo: [
      { required: true, message: "打印机机身号不能为空", trigger: "blur" },
    ],
    deviceKey: [
      { required: true, message: "打印机key不能为空", trigger: "blur" },
    ],
  };

  /** 设备数组 */
  sheetList = [
    {
      value: "打印机",
      label: "打印机",
    },
  ];

  /** 设备型号 */
  valueList = [
    {
      value: "选项一",
      label: "热敏条码云打印机 356 M2",
    },
    {
      value: "选项二",
      label: "映美标签打印机",
    },
  ];

  mounted() {
    if (this.editCurrent.id) {
      this.editData();
    } else {
      this.initData();
    }
  }

  /**
   * 初始话数据
   */
  initData() {
    this.sheetForm = {
      deviceType: "打印机",
      deviceModel: "热敏条码云打印机 356 M2",
      deviceName: "",
      deviceNo: "",
      deviceKey: "",
      status: "1",
      id: "",
    };
  }

  /**
   * 编辑时处理数据
   */
  editData() {
    this.sheetForm = JSON.parse(JSON.stringify(this.editCurrent));
    const parentHtml = this.$parent as PrintSetting;
    parentHtml.editCurrent = {};
  }

  /**
   * 对整个表单进行校验
   */
  validate() {
    return (this.sheetRef as ElForm).validate();
  }

  /**
   * 移除表单项的校验结果
   */
  clearValidate() {
    (this.sheetRef as ElForm).clearValidate();
  }
}
</script>

<style lang="scss" scoped>
.demo-ruleForm {
  padding-left: 30px;
}
</style>
