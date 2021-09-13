/*
 * @description: 客户列表接口集合
 * @Author: vikingShip
 * @Date: 2021-08-21 09:34:28
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-27 14:19:22
 */
export interface CustomerListState {
  dataForm: CustomerQueryForm;
  dataList: ApiCustomerList[];
}

/**
 * @LastEditors: vikingShip
 * @description: 客户列表筛选条件
 * @param {*}
 */
export interface CustomerQueryForm {
  nikeName: string;
  becomeMemberTime: string[];
  orderSuccessTime: string[];
  memberNumber: number | null;
  rankCode: number | null;
  sortType: number | null;
  tagId: number | null;
}

/**
 * @LastEditors: vikingShip
 * @description: 客户列表数组
 * @param avatarUrl 头像url
 * @param consumeNum 消费次数
 * @param consumeTotleMoney 交易总额
 * @param firstLoginTime 首次登陆小程序时间
 * @param memberRegisterTime 会员注册时间
 * @param nikeName 用户名称
 * @param orderLastDealTime 最后交易时间
 * @param phone 手机号码
 * @param userId 店铺用户id
 */
export interface ApiCustomerList extends CustomerController {
  avatarUrl: string;
  consumeNum: number;
  consumeTotleMoney: number;
  firstLoginTime: string;
  memberRegisterTime: string;
  nikeName: string;
  orderLastDealTime: string;
  phone: string;
  userId: string;
  userTagVos: Array<CustomerTagList>;
  selectionList: Array<CustomerTagList>;
  multipleSelection: Array<CustomerTagList>;
  sortOptions: Array<{ value: number; label: string }>;
  couponDropList: Array<CustomerDropItem>;
  rankOptions: Array<CustomerRank>;
  pageSize: number;
  pageNum: number;
  total: number;
  blacklistType?: number[];
}

/**
 * @LastEditors: vikingShip
 * @description: 客户列表tag标签
 * @param option 是否被选中
 */
export interface CustomerTagList {
  integral: number;
  userId: string | number;
  option?: boolean;
  tagId: number;
  tagName: string;
}

type CustomerController = Record<
  | "couponVisible"
  | "chargeVisible"
  | "detailVisible"
  | "balanceVisible"
  | "managerVisible"
  | "blackListVisible"
  | "labelVisible"
  | "visible",
  boolean
>;

export interface CustomerDropItem {
  command: string;
  disabled: boolean;
  show: boolean;
  text: string;
}
export type CustomerRank = Record<"rankCode" | "rankName", string>;
