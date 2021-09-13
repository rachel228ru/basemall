<!--
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:10
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-25 13:58:34
-->
<template>
  <el-dialog
    :visible.sync="dialogVisible"
    width="690px"
    style="margin-top: 60px"
    :before-close="handleClose"
  >
    <div slot="title" class="digTitle">选择区域</div>
    <div class="areaStyle">
      <div v-for="(item, index) in addressProv" :key="index">
        <div class="areaStyle__item">
          <el-checkbox
            v-model="item.isCheck"
            :indeterminate="item.isIndeterminate"
            :disabled="item.disabled"
            @change="selectPro(item)"
          ></el-checkbox>
          <div style="margin-left: 10px">{{ item.label }}</div>
          <el-popover trigger="hover" placement="right">
            <div style="height: 60px; overflow: auto">
              <div v-for="(v, i) in item.ownCity" :key="i">
                <div class="areaStyle__item--city">
                  <el-checkbox
                    v-model="v.isCheck"
                    @change="selectCity(item, v)"
                    :disabled="v.disabled"
                  ></el-checkbox>
                  {{ v.label }}
                </div>
              </div>
            </div>
            <div class="areaStyle__item--close">关闭</div>
            <span slot="reference">
              <i class="el-icon-arrow-right"></i>
            </span>
          </el-popover>
        </div>
      </div>
    </div>
    <el-checkbox
      :indeterminate="isIndeterminate"
      v-model="checkAll"
      @change="handleCheckAllChange"
      >全选</el-checkbox
    >
    <!-- <span style="margin-left:20px;">ps：港澳台暂不支持物流配送</span> -->
    <span slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取 消</el-button>
      <el-button type="primary" @click="selectArea">确 定</el-button>
    </span>
  </el-dialog>
</template>

<script lang="ts">
import { Vue, Component, PropSync, Watch, Prop } from "vue-property-decorator";
import AddModel from "./AddModel.vue";
import address from "@/views/goods/common/city.js";
import {
  AssignFreightInfoType,
  BaseFreightInfoType,
  LogisticsRegionType,
} from "@/views/goods/marketModel/goodType";
import { RegionType } from "@/views/goods/goodManage/supplierManageType";
export interface NewRegionType extends RegionType {
  isCheck?: boolean;
  disabled?: boolean;
  ownCity?: Array<{
    isCheck?: boolean | any;
    disabled?: boolean | any;
    label: any;
    value: any;
  }>;
  hasNum?: number | null;
  cityList?: Array<{ value: string }>;
  isIndeterminate?: boolean;
  newlabel?: string;
}
@Component
export default class AreaSelect extends Vue {
  @Prop({
    type: Number,
    default: 0,
    required: true,
  })
  modelIndex!: number;

  @Prop({
    type: Array,
    required: true,
    default: [],
  })
  getRegion!: LogisticsRegionType[];

  @Prop({
    type: Number,
    default: 0,
  })
  modelType!: number;

  @PropSync("visible", {
    type: Boolean,
    default: false,
  })
  dialogVisible!: boolean;

  @Watch("dialogVisible")
  watchVisiable() {
    if (this.dialogVisible) {
      this.initArea();
      this.dealArea();
      document.body.appendChild(this.$el);
    }
  }

  /** 省数组 */
  addressProv: Array<NewRegionType> = address.getProvince();

  /** 市数组 */
  addressCity: Array<NewRegionType> = [];

  /** 可视弹窗 */
  popVisiable = false;

  /** 编辑时已选择过的省集合 */
  hasCheckPro = {
    pro: [] as Array<string | number>,
    city: [] as Array<string>,
  };

  /** 全选状态 */
  isIndeterminate = false;

  checkAll = false;

  lengthList: Array<string> = [];

  mounted() {
    this.initArea();
    this.dealArea();
  }

  /**
   * 初始化省市信息
   */
  initArea() {
    this.addressProv.forEach((item, index) => {
      if (
        item.value === "710000" ||
        item.value === "810000" ||
        item.value === "820000"
      ) {
        this.addressProv.splice(index, 1);
      }
      item.disabled = false;
      item.isCheck = false;
      item.cityList = [];
      item.isIndeterminate = false;
      item.ownCity = [];
      const addressCity = address.getCity(item.value);
      item.ownCity = addressCity;
      item.ownCity.forEach(ownItem => {
        ownItem.disabled = false;
        ownItem.isCheck = false;
      });
      item.hasNum = item.ownCity.length;
    });
    this.addressProv = JSON.parse(JSON.stringify(this.addressProv));
    this.checkAll = false;
    this.getCheckAll();
  }

  handleClose() {
    this.$confirm("退出后不会保留选择操作！", "提示", {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning",
    }).then(() => {
      this.dialogVisible = false;
      this.initArea();
    });
  }

  /**
   * 全选
   */
  handleCheckAllChange(e: boolean) {
    this.addressProv.forEach(item => {
      item.isCheck = e;
      item.cityList = [];
      if (item.ownCity) {
        item.ownCity.forEach(ownItem => {
          ownItem.isCheck = e;
          if (ownItem.isCheck && item.cityList) {
            item.cityList.push({
              value: ownItem.value,
            });
          }
        });
        item.isIndeterminate =
          item.cityList.length === item.hasNum ? false : true;
        if (!e) {
          item.isIndeterminate = false;
        }
      }
    });
    this.getCheckAll();
  }

  /**
   * 选择省
   */
  selectPro(item: NewRegionType) {
    item.cityList = [];
    if (item.ownCity) {
      item.ownCity.forEach(cityItem => {
        cityItem.isCheck = item.isCheck;
        if (cityItem.isCheck && item.cityList) {
          item.cityList.push({
            value: item.value,
          });
        }
      });
      item.hasNum = item.ownCity.length;
      item.isIndeterminate =
        item.cityList.length > 0 && item.cityList.length < item.ownCity.length;
      this.getCheckAll();
    }
  }

  /**
   * 选择市
   */
  selectCity(pro: NewRegionType) {
    pro.cityList = [];
    if (pro.ownCity) {
      pro.ownCity.forEach(item => {
        if (item.isCheck && pro.cityList) {
          pro.cityList.push({
            value: item.value,
          });
        }
      });
      pro.isIndeterminate =
        pro.cityList.length > 0 && pro.cityList.length < pro.ownCity.length;
      pro.isCheck = pro.cityList.length === pro.ownCity.length;
      pro.hasNum = pro.ownCity.length;
    }
  }

  /**
   * 新增时设置已选区域不可再选
   */
  dealArea() {
    this.getNewCity();
    const getRegion = JSON.parse(
      JSON.stringify(this.getRegion),
    ) as LogisticsRegionType[];
    this.hasCheckPro.pro.forEach(proItem => {
      this.addressProv.forEach((addItem: any) => {
        if (proItem === addItem.value) {
          addItem.cityList = [];
          addItem.ownCity.forEach((ownItem: any) => {
            this.hasCheckPro.city.forEach(cityItem => {
              if (cityItem === ownItem.value) {
                ownItem.isCheck = true;
                ownItem.disabled = true;
                addItem.cityList.push({
                  value: ownItem.value,
                });
              }
            });
          });
          addItem.isCheck =
            addItem.hasNum > 0 && addItem.hasNum === addItem.cityList.length;
          addItem.isIndeterminate =
            addItem.cityList.length > 0 &&
            addItem.cityList.length < addItem.hasNum;
          addItem.disabled = true;
        }
      });
      if (getRegion.length > 0) {
        getRegion.forEach(gtItem => {
          this.addressProv.forEach((addItem: any) => {
            addItem.cityList = [];
            if (gtItem.fatherid === addItem.value) {
              addItem.ownCity.forEach((ownItem: any) => {
                gtItem.list.forEach((cityItem: any) => {
                  if (cityItem.value === ownItem.value) {
                    ownItem.isCheck = true;
                    ownItem.disabled = false;
                    if (ownItem.isCheck) {
                      addItem.cityList.push({
                        value: ownItem.value,
                      });
                    }
                  }
                });
              });
              addItem.isCheck =
                addItem.hasNum > 0 &&
                addItem.hasNum === addItem.cityList.length;
              addItem.isIndeterminate =
                addItem.cityList.length > 0 &&
                addItem.cityList.length < addItem.hasNum;
              addItem.disabled = false;
            }
          });
        });
      }
    });
    this.getCheckAll();
  }

  /**
   * 编辑时获取所有数据
   */
  getNewCity() {
    const pros: Array<string | number> = [];
    const citys: Array<string> = [];
    // 获取最外层模板数组里的所有已选择过地区 勿删
    // const logisModelList = this.$parent.$parent.$parent.logisModelList;
    // 获取外层模板数组里已选择过地区
    const parentHtml = this.$parent as AddModel;
    const logisModelHasList = (!parentHtml.freeArea
      ? parentHtml.modelValidateForm.logisticsShippingModelDtos
      : parentHtml.modelValidateForm.logisticsIncludePostageDtos) as
      | BaseFreightInfoType[]
      | AssignFreightInfoType[];

    logisModelHasList.forEach(hsItem => {
      hsItem.region =
        typeof hsItem.region === "object"
          ? hsItem.region
          : JSON.parse(hsItem.region);
      (hsItem.region as LogisticsRegionType[]).forEach(laItem => {
        pros.push(laItem.fatherid);
        laItem.list.forEach(listItem => {
          citys.push(listItem.value);
        });
      });
    });
    this.hasCheckPro.pro = [...new Set(pros)];
    this.hasCheckPro.city = [...new Set(citys)];
  }

  /**
   * 获取全选状态
   */
  getCheckAll() {
    this.lengthList = [];
    this.addressProv.forEach(o => {
      if (o.isCheck) {
        this.lengthList.push(o.label);
      }
    });
    this.checkAll = this.addressProv.length === this.lengthList.length;
    this.isIndeterminate =
      this.lengthList.length > 0 &&
      this.addressProv.length > this.lengthList.length;
  }

  /**
   * 确认选择地区
   */
  selectArea() {
    const region: Array<LogisticsRegionType> = [];
    const conList: Array<NewRegionType> = [];
    this.addressProv.forEach(item => {
      item.cityList = [];
      if (item.ownCity) {
        item.ownCity.forEach(ownItem => {
          if (ownItem.isCheck && !ownItem.disabled && item.cityList) {
            item.cityList.push({
              value: ownItem.value,
            });
          }
        });
        if (item.cityList.length > 0) {
          conList.push(item);
          region.push({
            fatherid: item.value,
            list: item.cityList,
          });
        }
      }
    });
    if (region.length === 0) {
      this.$message.error("请至少选择一个区域");
      return;
    }
    const parentHtml = this.$parent as AddModel;
    this.dialogVisible = false;
    this.$emit(
      "getConList",
      region,
      conList,
      this.modelIndex,
      parentHtml.freeArea,
    );
    this.initArea();
  }
}
</script>

<style lang="scss" scoped>
.digTitle {
  font-size: 17px;
}

@include b(areaStyle) {
  display: flex;
  flex-wrap: wrap;
  font-size: 14px;
  @include e(item) {
    width: 142px;
    margin-right: 20px;
    margin-bottom: 20px;
    font-size: 13px;
    display: flex;
    @include m(city) {
      margin-bottom: 3px;
      font-size: 13px;
    }
    @include m(close) {
      margin-top: 10px;
      color: rgb(45, 140, 240);
      font-size: 12px;
      cursor: pointer;
    }
  }
}
</style>
