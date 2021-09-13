/*
 * @description:
 * @Author: vikingShip
 * @Date: 2021-08-21 17:08:11
 * @LastEditors: vikingShip
 * @LastEditTime: 2021-08-21 18:01:00
 */

import {
  AssignFreightInfoType,
  BaseFreightInfoType,
  FreightTempType,
} from "@/views/goods/marketModel/goodType";

/**
 * @LastEditors: vikingShip
 * @description: AddModelState
 * @param modelIndex 点击的数组定位
 * @param modelRegion 点击时所已经选择的地址
 * @param freeArea 是否点击包邮
 * @param modelValidateForm 新增修改运费模板信息form
 * @param selectOptions 包邮选项
 * @param areaDialog 地址选择弹窗
 */
export interface AddModelState {
  modelIndex: number;
  modelRegion: any[];
  freeArea: boolean;
  modelValidateForm: Partial<AddLogisticsSubForm>;
  selectOptions: FreeShippingOptions[];
  areaDialog: boolean;
}

/**
 * @LastEditors: vikingShip
 * @description: modelValidateForm 新增修改运费模板信息form
 * @param logisticsShippingModelDtos  基础运送方式信息
 * @param logisticsIncludePostageDtos  指定包邮运送方式信息
 */

export interface AddLogisticsSubForm extends FreightTempType {
  logisticsShippingModelDtos: BaseFreightInfoType[];
  logisticsIncludePostageDtos: AssignFreightInfoType[];
}

export interface FreeShippingOptions {
  value: string;
  label: string;
  show?: boolean;
}
