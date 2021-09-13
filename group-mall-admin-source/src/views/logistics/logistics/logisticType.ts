/*
 * @description:
 * @Author: vikingShip
 * @Date: 2021-08-21 16:35:15
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-25 13:59:10
 */

import { FreightTempType } from "@/views/goods/marketModel/goodType";

/**
 * @LastEditors: vikingShip
 * @description: LogisticsState
 * @param logDialog 编辑物流弹窗
 * @param editCurrent 编辑的模板信息
 * @param titleType 弹窗标题
 * @param logisModelList 运费模板数组
 * @param hasList 是否有list
 * @param pageType 是否发布商品页面进入
 * @param query 分页信息
 */
export interface LogisticsState {
  logDialog: boolean;
  editCurrent: Partial<FreightTempType>;
  titleType: boolean;
  logisModelList: FreightTempType[];
  hasList: boolean;
  pageType: string;
  query: PageQueryType;
}

export type PageQueryType = Partial<
  Record<"current" | "size" | "total", number>
>;

/**
 * @LastEditors: vikingShip
 * @description: 收发货地址
 * @param address 地址
 * @param city 市
 * @param cityId 市id
 * @param country 县
 * @param countryId 县id
 * @param defReceive 是否为默认退货地址: 0=不是,1=是
 * @param defSend 是否为默认发货地址: 0=不是,1=是
 * @param lat 纬度
 * @param lng 经度
 * @param name 联系人名称
 * @param phone 联系电话
 * @param province 省
 * @param provinceId 省id
 * @param zipCode 邮编
 */
export interface ApiAddressType {
  address: string;
  city: string;
  cityId: string;
  country: string;
  countryId: string;
  defReceive: number;
  defSend: number;
  id: number;
  lat: string;
  lng: string;
  name: string;
  phone: string;
  province: string;
  provinceId: string;
  zipCode: string;
  isShow?: boolean;
  area?: string;
}

/**
 * @LastEditors: vikingShip
 * @description: 物流公司信息列表
 * @param addressId 地址id 关联物流发货地址表id
 * @param code 快递公司code
 * @param customerName 客户名称
 * @param customerPassword 客户密匙
 * @param linkName 负责人
 * @param linkTel 负责人电话
 * @param name 快递公司名称
 * @param phone 快递公司客服电话
 * @param status 状态
 */
export interface ApiLogisticsCompanyList {
  addressId: number | string;
  code: string;
  customerName: string;
  customerPassword: string;
  id: number | string;
  linkName: string;
  linkTel: string;
  logisticsAddressVos: Array<MixinsAddress>;
  name: string;
  phone: string;
  status: string;
  number?: string | number;
  addressList?: Array<MixinsAddress>;
  cid?: string | number;
  getAddress: MixinsAddress;
}

export type MixinsAddress = Exclude<ApiLogisticsCompanyList, ApiAddressType>;
