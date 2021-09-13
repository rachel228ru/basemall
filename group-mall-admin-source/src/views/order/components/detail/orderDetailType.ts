/*
 * @description:
 * @Author: vikingShip
 * @Date: 2021-08-19 23:10:33
 * @LastEditors: vikingShip
 * @LastEditTime: 2021-08-20 09:24:11
 */
import {RegionType} from '@/views/goods/goodManage/supplierManageType'
/**
 * @LastEditors: vikingShip
 * @description: 订单地址form
 * @param addressForm 订单地址form
 * @param areaData 订单区域form
 * @param province 省
 * @param city 市
 * @param region 区
 * @param visible 显隐标识
 * @param tableData 表格数据
 */
export interface OrderDetailState {
  addressForm: OrderAddressType;
  areaData:AreaDataType
  province:RegionType[]
  city:RegionType[]
  region:RegionType[]
  visible:boolean
}

export interface OrderAddressType {
  orderId: number | null;
  receiverDetailAddress: string;
  receiverName: string;
  receiverPhone: string;
  receiverPostCode: string;
  receiverProvince: string;
  receiverCity: string;
  receiverRegion: string;
  province:RegionType[]
}

export type AreaDataType = Pick<
  OrderAddressType,
  "receiverProvince" | "receiverCity" | "receiverRegion" | "receiverPostCode"
>;
