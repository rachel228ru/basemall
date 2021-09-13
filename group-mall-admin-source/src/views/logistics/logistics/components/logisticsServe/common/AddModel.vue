<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:10
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-25 13:58:50
-->
<template>
  <div class="diaLog">
    <div class="diaLog__content">
      <span class="diaLog__content--left">快递公司:</span>
      {{ serveOption.name }}
    </div>
    <div class="diaLog__content">
      <span class="diaLog__content--left">
        <span class="star">*</span>发货地址:
      </span>
      <el-select
        v-model="serveOption.addressId"
        placeholder="请选择发货地址"
        style="width:250px"
      >
        <el-option
          v-for="item in addList"
          :key="item.id"
          :label="item.address"
          :value="item.id"
        ></el-option>
      </el-select>
      <el-button type="text" style="margin-left:20px" @click="jumpClick"
        >前往设置</el-button
      >
    </div>
    <div class="diaLog__content">
      <span class="diaLog__content--left">
        <span class="star">*</span>客户号:
      </span>
      <el-input
        v-model="serveOption.customerName"
        placeholder="请输入客户号"
        style="width:320px"
      ></el-input>
    </div>
    <div class="diaLog__content">
      <span class="diaLog__content--left">
        <span class="star">*</span>客户号密码:
      </span>
      <el-input
        v-model="serveOption.customerPassword"
        placeholder="请输入客户号密码"
        style="width:320px"
      ></el-input>
    </div>
    <div class="diaLog__content">
      <span class="diaLog__content--left">
        <span class="star">*</span>负责人:
      </span>
      <el-input
        v-model="serveOption.linkName"
        placeholder="请填写改发货地址的负责人姓名"
        style="width:320px"
      ></el-input>
    </div>
    <div class="diaLog__content">
      <span class="diaLog__content--left">
        <span class="star">*</span>联系电话:
      </span>
      <el-input
        v-model="serveOption.linkTel"
        placeholder="请填写负责人手机号"
        style="width:320px"
        maxlength="11"
      ></el-input>
    </div>
  </div>
</template>

<script lang="ts">
import { Vue, Component, Prop, Watch } from "vue-property-decorator";
import { getAllShopAddress } from "@/api/logistics/logistics";
import { ApiLogisticsCompanyList } from "../../../logisticType";

@Component
export default class AddModel extends Vue {
  @Prop({
    type: Boolean,
    default: {},
  })
  addDialog!: boolean;

  @Prop({
    type: Object,
    default: {},
  })
  editCurrent!: ApiLogisticsCompanyList;

  @Watch("addDialog")
  getAddDialog() {
    if (this.addDialog) {
      this.getLogAddress();
    }
    if (this.editCurrent.getAddress) {
      this.dealOption();
    } else {
      this.init();
    }
  }

  serveOption = {
    addressId: "",
    customerName: "",
    customerPassword: "",
    linkName: "",
    linkTel: "",
    code: "sf",
    status: 1,
    phone: 91199,
    name: "顺丰快递",
    id: "",
  };

  /** 发货地址选择 */
  addList = [];

  mounted() {
    this.getLogAddress();
    if (this.editCurrent.getAddress) {
      this.dealOption();
    } else {
      this.init();
    }
  }

  /**
   * 初始化数据
   */
  init() {
    const chooseCompany = (this.$parent.$parent as any).chooseCompany;
    this.serveOption = {
      addressId: "",
      customerName: "",
      customerPassword: "",
      linkName: "",
      linkTel: "",
      code: chooseCompany.code, // 物流公司code
      status: 1,
      phone: 91199,
      name: chooseCompany.name,
      id: "",
    };
  }

  /**
   * 处理编辑数据
   */
  dealOption() {
    const editCurrent = JSON.parse(JSON.stringify(this.editCurrent));
    this.serveOption.code = editCurrent.code;
    this.serveOption.customerName = editCurrent.getAddress.customerName;
    this.serveOption.customerPassword = editCurrent.getAddress.customerPassword;
    this.serveOption.linkName = editCurrent.getAddress.linkName;
    this.serveOption.linkTel = editCurrent.getAddress.linkTel;
    this.serveOption.name = editCurrent.name;
    this.serveOption.id = editCurrent.getAddress.cid;
    this.serveOption.addressId = editCurrent.getAddress.id;
    // this.isAgree = true;
  }

  /**
   * 前往设置
   */
  jumpClick() {
    (this.$parent.$parent.$parent as any).comName = "AddressProgram";
  }

  /**
   * 获取所有地址
   */
  getLogAddress() {
    getAllShopAddress({}).then(res => {
      this.addList = res.data;
    });
  }
}
</script>

<style lang="scss" scoped>
.star {
  color: #f56c84;
  margin-right: 3px;
}

@include b(diaLog) {
  @include e(content) {
    margin-bottom: 20px;
    display: flex;
    align-items: center;
    @include m(left) {
      width: 100px;
      display: flex;
      justify-content: flex-end;
      margin-right: 40px;
    }
  }

  @include e(agree) {
    width: 100%;
    display: flex;
    // justify-content: center;
    margin-left: 140px;
    @include m(open) {
      color: #1e7fff;
      cursor: pointer;
    }
  }
}
</style>
