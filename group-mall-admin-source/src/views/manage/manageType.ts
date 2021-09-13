/*
 * @description:
 * @Author: vikingShip
 * @Date: 2021-08-26 13:42:49
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-09-02 14:17:55
 */

export interface ManageState {
  dateValue: string[];
  dayQuery: ManageQueryType;
  orderQuery: any;
}
/**
 * @LastEditors: vikingShip
 * @description:
 * @param trade 每日交易详情
 * @param tradeLineVos 交易数组
 * @param yesterdayTrade 昨日交易详情
 */
export interface ManageQueryType {
  updateTime: string;
  trade: ManageTrade;
  tradeLineVos: Array<ManageTrade>;
  yesterdayTrade: ManageTrade;
}

/**
 * @LastEditors: vikingShip
 * @description:
 * @param date 日期
 * @param transactionVolume 交易量
 * @param turnover 交易额
 */
interface ManageTrade {
  date?: string;
  transactionVolume: number;
  turnover: number;
  views?: number;
}

export type ManageOrderQuery = Record<
  "shipped" | "waitForPay" | "waitForPickup" | "waitForSend",
  number
>;

export type ManageAccountQuery = Record<"pv" | "totalUv" | "uv", number>;
