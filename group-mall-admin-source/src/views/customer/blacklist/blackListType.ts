/*
 * @description:
 * @Author: vikingShip
 * @Date: 2021-08-21 15:32:50
 * @LastEditors: vikingShip
 * @LastEditTime: 2021-08-21 15:49:39
 */
import { ApiCustomerList, CustomerDropItem } from "../list/customerListType";
/**
 * @LastEditors: vikingShip
 * @description: BlackListState
 * @param dataList 黑名单列表
 * @param dataForm 黑名单筛选
 * @param dropdownList 操作下拉列表
 * @param blackVisible 弹窗显隐标识符
 */
export interface BlackListState {
  dataList: ApiBlackList[];
  dataForm: BlackListQueryType;
  multipleSelection: ApiBlackList[];
  dropdownList: Array<CustomerDropItem>;
  blackVisible: boolean;
  permissionOptions: Array<{ value: string | number; label: string }>;
  pageSize: number;
  pageNum: number;
  total: number;
}
type PickCustomerList =
  | "avatarUrl"
  | "becomeMemberDayNumber"
  | "blacklistType"
  | "consumeNum"
  | "consumeTotleMoney"
  | "integral"
  | "memberNumber"
  | "memberRegisterTime"
  | "nikeName"
  | "phone"
  | "supplyBonus"
  | "userId";

export type ApiBlackList = Pick<ApiCustomerList, PickCustomerList>;

export interface BlackListQueryType {
  permission: number | null;
  fuzzy: string;
}
