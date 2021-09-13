/*
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:11
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-09-02 10:44:52
 */
import fromPairs from "lodash/fromPairs";

/** 订单状态 */
export const getOrderStatusName = (key: string) => {
  return {
    WAIT_FOR_PAY: "待付款",
    WAIT_FOR_SEND: "待发货",
    WAIT_FOR_COMMENT: "交易完成",
    WAIT_FOR_PICKUP: "等待客户提货",
    COMPLETE: " 已完成",
    REFUNDED: "已退款",
    PART_REFUNDED: "已完成",
    BUYER_PAY_TIMEOUT_CLOSE: "超时关闭",
    BUYER_CANCEL_CLOSE: "客户关闭",
    SELLER_CANCEL_CLOSE: "卖家关闭",
    EXCHANGE_SUCCESS_CLOSE: "换货成功",
    /**
     * 换货关闭
     */
    EXCHANGE_CANCEL_CLOSE: "关闭",
  }[key];
};

/** 订单状态 */
export const getDeliverOrderStatusName = (key: string) => {
  return {
    WAIT_FOR_PAY: "待付款",
    WAIT_FOR_SEND: "待发货",
    WAIT_FOR_PICKUP: "待提货",
    WAIT_FOR_COMMENT: "交易完成",
    COMPLETE: " 已完成",
    REFUNDED: "已退款",
    PART_REFUNDED: "已完成",
    BUYER_PAY_TIMEOUT_CLOSE: "超时关闭",
    BUYER_CANCEL_CLOSE: "客户关闭",
    SELLER_CANCEL_CLOSE: "卖家关闭",
    EXCHANGE_SUCCESS_CLOSE: "换货成功",
    /**
     * 换货关闭
     */
    EXCHANGE_CANCEL_CLOSE: "关闭",
  }[key];
};

export const getTypeIcon = (type: any) => {
  switch (type) {
    case "WAIT_FOR_PAY":
      return "warn";
    case "PART_REFUNDED":
    case "WAIT_FOR_SEND":
      return "success";
    default:
      return "info";
  }
};

/** 是否为关闭状态 */
export const isClose = (v: string) => {
  return [
    "BUYER_PAY_TIMEOUT_CLOSE",
    "BUYER_CANCEL_CLOSE",
    "SELLER_CANCEL_CLOSE",
    "EXCHANGE_SUCCESS_CLOSE",
  ].includes(v);
};

/** 配送类型 */
export const getDeliveryTypeName = (key: string) => {
  return {
    LOGISTICS: "物流配送",
  }[key];
};

/** 过滤空 */
export const filterEmpty = (data: { [s: string]: any } | ArrayLike<any>) => {
  return fromPairs(
    Object.entries(data).filter(([, value]) => {
      return !!value;
    }),
  ) as any;
};

/** 订单查询条件 */
export const orderQuery = {
  current: 1,
  deliverType: "102",
  goodsName: "",
  groupLeaderName: "",
  keyword: "",
  lineId: "",
  userName: "",
  orderId: "",
  // -1：所有订单, 0.待付款（待买家付款）, 1.待发货（买家已付款）, 2.已发货（卖家已发货）,  4.已完成（用户已经签收）, 5.已关闭
  orderStatus: "-1",
  payTime: "",
  pointName: "",
  // 近一个月->0; 近三个月->1; 全部->2;
  quicklyDate: "0",
  receiverName: "",
  size: 20,
  deliverySn: "",
  remarkType: "0",
  sendBillType: "",
  startTime: "",
  endTime: "",
  area: "2",
};

/** 物流订单查询条件 */
export const deliveryOrderQuery = {
  current: 1,
  deliverType: "102",
  goodsName: "",
  groupLeaderName: "",
  keyword: "",
  lineId: "",
  userName: "",
  orderId: "",
  orderStatus: "-1",
  payTime: "",
  quicklyDate: "0",
  receiverName: "",
  size: 20,
  deliverySn: "",
  remarkType: "0",
  area: "0",
};

/**
 * 订单评价查询条件
 */
export const evaluationQuery = {
  current: 1,
  goodsName: "",
  orderId: "",
  payTime: "",
  rate: "",
  total: 0,
  size: 20,
};

export const strFilter = (str: string) => {
  if (str.length <= 10) return str;
  return str.substring(0, 10) + "...";
};
