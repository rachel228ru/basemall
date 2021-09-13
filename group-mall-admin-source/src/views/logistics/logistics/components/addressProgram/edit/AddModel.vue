<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:10
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-25 13:58:01
-->
<template>
  <div class="diaLog">
    <div class="diaLog__content">
      <span class="diaLog__content--left">
        <span class="star">*</span>联系人：
      </span>
      <el-input
        v-model="addressOption.name"
        style="width: 450px"
        maxlength="10"
        placeholder="最多输入10个字符"
      ></el-input>
    </div>
    <div class="diaLog__content">
      <span class="diaLog__content--left">
        <span class="star">*</span>联系电话：
      </span>
      <el-input
        v-model="addressOption.phone"
        style="width: 450px"
        maxlength="11"
        placeholder="请输入手机号"
      ></el-input>
    </div>
    <div class="diaLog__content">
      <span class="diaLog__content--left">
        <span class="star">*</span>邮政编号：
      </span>
      <el-input
        v-model="addressOption.zipCode"
        style="width: 450px"
        maxlength="6"
        placeholder="请输入邮政编号"
      ></el-input>
    </div>
    <div class="diaLog__content">
      <span class="diaLog__content--left">
        <span class="star">*</span>地区选择：
      </span>
      <div class="diaLog__content--area">
        <el-select
          placeholder="省份选择"
          @change="selectChang($event, 'prov')"
          v-model="addressOption.provinceId"
          style="margin-right: 5px"
        >
          <el-option
            v-for="item in addressProv"
            :value="item.value"
            :label="item.label"
            :key="item.value"
            >{{ item.label }}</el-option
          >
        </el-select>
        <el-select
          placeholder="城市选择"
          not-found-text="请先选择省份"
          @change="selectChang($event, 'city')"
          v-model="addressOption.cityId"
          style="margin-right: 5px"
        >
          <el-option
            v-for="item in addressCity"
            :value="item.value"
            :label="item.label"
            :key="item.value"
            >{{ item.label }}</el-option
          >
        </el-select>
        <el-select
          placeholder="区县选择"
          not-found-text="请先选择城市"
          @change="selectChang($event, 'area')"
          v-model="countryId"
        >
          <el-option
            v-for="item in addressArea"
            :value="item.value"
            :label="item.label"
            :key="item.value"
            >{{ item.label }}</el-option
          >
        </el-select>
      </div>
    </div>
    <div class="diaLog__content">
      <span class="diaLog__content--left">
        <span class="star">*</span>详细地址：
      </span>
      <el-input
        v-model="addressOption.address"
        type="textarea"
        :rows="3"
        style="width: 450px"
      ></el-input>
    </div>
  </div>
</template>

<script lang="ts">
import { Vue, Component, Prop, Watch } from "vue-property-decorator";
import address from "@/views/goods/common/city.js";
import { ApiAddressType } from "../../../logisticType";
import { RegionType } from "@/views/goods/goodManage/supplierManageType";

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
  currentItem!: ApiAddressType;

  @Watch("addDialog")
  getAddDialog() {
    if (this.addDialog) {
      this.getData();
    }
  }

  addressOption: Partial<ApiAddressType> = {
    name: "",
    address: "",
    zipCode: "",
    phone: "",
    isShow: false,
    cityId: "",
    provinceId: "",
    area: "",
    province: "",
    city: "",
    country: "",
  };

  countryId = "";

  /** 省数组 */
  addressProv = address.getProvince();

  /** 市数组 */
  addressCity: Array<RegionType> = [];

  /** 区数组 */
  addressArea: Array<RegionType> = [];

  /** 是否同意协议 */
  isAgree = false;

  mounted() {
    this.getData();
  }

  /**
   * 编辑时获取地址信息
   */
  getData() {
    this.addressOption = this.currentItem
      ? JSON.parse(JSON.stringify(this.currentItem))
      : this.addressOption;
    this.addressProv = address.getProvince();
    this.addressCity = address.getCity(this.addressOption.provinceId);
    this.addressArea = address.getArea(
      this.addressOption.provinceId,
      this.addressOption.cityId,
    );
    this.countryId = this.currentItem.countryId
      ? JSON.parse(JSON.stringify(this.currentItem.countryId))
      : "";
  }

  /**
   * 选择省市区
   */
  selectChang(value: string, type: string) {
    if (type === "prov" && value) {
      this.addressCity = address.getCity(value);
      this.addressArea = [];
      this.addressOption.cityId = "";
      this.countryId = "";
    }
    if (type === "city" && value) {
      this.addressArea = address.getArea(this.addressOption.provinceId, value);
      this.countryId = "";
    }

    if (type === "area" && value) {
      this.countryId = JSON.parse(JSON.stringify(value));
    }

    if (
      this.addressOption.provinceId &&
      this.addressOption.cityId &&
      this.addressOption.province &&
      this.addressOption.city
    ) {
      this.addressOption.province = this.getAddressName(
        this.addressProv,
        this.addressOption.provinceId,
      );
      this.addressOption.city = this.getAddressName(
        this.addressCity,
        this.addressOption.cityId,
      );
      this.addressOption.country = this.getAddressName(
        this.addressArea,
        this.countryId,
      );
      this.addressOption.area =
        this.addressOption.province +
        this.addressOption.city +
        this.addressOption.country;
    }
  }

  /**
   * 获取省市区所有数据
   */
  getAddressName(list: RegionType[], value: string) {
    let name = "";
    if (list.length > 0) {
      for (const item of list) {
        if (item.value === value) {
          name = item.label;
          break;
        }
      }
    }
    return name;
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
      width: 80px;
      display: flex;
      justify-content: flex-end;
      margin-right: 10px;
    }
    @include m(area) {
      display: flex;
      flex: 1;
      justify-content: space-between;
    }
  }

  @include e(agree) {
    width: 100%;
    display: flex;
    justify-content: center;
    @include m(open) {
      color: #1e7fff;
      cursor: pointer;
    }
  }
}
</style>
