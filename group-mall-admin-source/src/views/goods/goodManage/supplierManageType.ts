/*
 * @description:
 * @Author: vikingShip
 * @Date: 2021-08-18 10:28:14
 * @LastEditors: vikingShip
 * @LastEditTime: 2021-08-18 13:37:02
 */
import { GoodSupplierType } from "@/views/goods/marketModel/goodType";
/**
 * @LastEditors: vikingShip
 * @description: SupplierManageState
 * @param showType 是否发布商品进入
 * @param searchMsg 搜索条件
 * @param searchType 供货商状态
 * @param addressProv 省数组
 * @param addressCity 市数组
 * @param addressArea 区数组
 * @param list 供应商数组
 * @param hasList 供应商是否存在
 * @param isIndeterminate 是否有勾选中供应商
 * @param seletAll 全选状态
 * @param selelctList 选择数组列表
 * @param isModelAdd 弹窗状态
 * @param editOrAdd 编辑状态
 * @param pageSize 分页条数
 * @param pageNum 分页页码
 * @param total 数据长度
 * @param isDeal 是否是用户审核
 * @param currItem 对应供应商信息
 * @param selelctData 选中数据
 */
export interface SupplierManageState {
  showType: boolean;
  searchMsg: SearchValue;
  searchType: string;
  addressProv: RegionType[];
  addressCity: RegionType[];
  addressArea: RegionType[];
  list: AddGoodSupplierType[];
  hasList: boolean;
  isIndeterminate: boolean;
  seletAll: boolean;
  selelctList:(string | number)[];
  isModelAdd: boolean;
  editOrAdd: boolean;
  pageSize: number;
  pageNum: number;
  total: number;
  isDeal: boolean;
  currItem: Partial<AddGoodSupplierType>;
  selelctData: AddGoodSupplierType[]
  selectName: string[]
}
/**
 * @LastEditors: vikingShip
 * @description: 搜索关键词
 * @param name 姓名输入框
 * @param mobile 手机号输入框
 * @param supplierSn id输入框
 */
export type SearchValue = Record<"name" | "mobile" | "supplierSn", string>;

export type RegionType = Record<"label" | "value", string>;

export interface AddGoodSupplierType extends GoodSupplierType{
    provinceId:string 
    cityId:string 
    countryId:string 
}
