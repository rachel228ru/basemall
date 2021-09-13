/*
 * @description:
 * @Author: vikingShip
 * @Date: 2021-08-13 15:38:58
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-27 14:26:41
 */

/**
 * @LastEditors: vikingShip
 * @description: globalstore的state
 * @param sounds 播报列表
 * @param timer 轮训定时器
 * @param setting 店铺设置
 * @param recover 回收设置
 * @param isOpend 播报是否打开
 */

export interface GlobalState {
  sounds: ISound[];
  timer: number;
  setting: OrderSettingType;
  recover: RecycleSetting;
  isOpend: boolean;
}
interface ISound {
  tenantId: string;
  createTime: string;
  updateTime: string;
  deleted: boolean;
  id: string;
  shopId: string;
  times: number;
  startTime: string;
  endTime: string;
  orderId: string;
  templateUrl: string;
  isOpen: number;
}
/**
 * @LastEditors: vikingShip
 * @description: 订单设置
 * @param ceiling 顶底抵扣上限额度百分比
 * @param customFrom 自定义表单
 * @param kdAppId 快递AppId
 * @param kdAppKey 快递AppKey
 * @param orderNotify 是否开启下单通知：0->关闭；1->开启
 * @param paymentModel 支付方式：1->微信支付;
 * @param shopId 商铺id
 * @param afsApplyNumber 最大售后申请次数
 * @param commentOvertime 订单完成后自动好评时间（天）
 * @param confirmOvertime 发货后自动确认收货时间（天）
 * @param deleted 删除状态：0->未删除；1->已删除
 * @param finishOvertime 自动完成交易时间，不能申请售后（天）
 * @param flashOrderOvertime 秒杀订单超时关闭时间(分)
 * @param openEvaluate 是否开启订单评论
 * @param tenantId 租户ID
 * @param userReturnOvertime 用户最大退货期限(天)
 * @param merchantConfirmOvertime 商家最大审核期限(天)
 * @param normalOrderOvertime 正常订单超时时间(分)
 */
export interface OrderSettingType {
  ceiling: number;
  customFrom: string;
  id: number;
  kdAppId: string;
  kdAppKey: string;
  orderNotify: true;
  paymentModel: string;
  shopId: string;
  supportAllowedDay: number;
  supportAuditDay: number;
  type: string;
  afsApplyNumber: number;
  commentOvertime: number;
  confirmOvertime: number;
  deleted: boolean;
  finishOvertime: number;
  flashOrderOvertime: number;
  userReturnOvertime: number;
  merchantConfirmOvertime: number;
  normalOrderOvertime: number;
  openEvaluate: boolean;
  tenantId: string;
}
/**
 * @LastEditors: vikingShip
 * @description: 回收配置相关
 * @param activityDesc 活动说明
 * @param assCouponId 关联卡券id
 * @param btnStyles 按钮样式
 * @param buyBgImg 购买成功展示背景图地址
 * @param entranceState 商品详情回收入口是否开启 true开启 false 关闭
 * @param fundQuota 基金额度
 * @param goodsFundState 商品基金是否开启 true开启 false 关闭
 * @param homePromptBgImg 首页未寄出提示背景图地址
 * @param pageBgImg 页面背景图片地址
 * @param pageCarousels 页面轮播图
 * @param recyclingState 回收券功能是否开启 true开启 false 关闭
 * @param submitImg 提交按钮
 */

export interface RecycleSetting {
  id: number | null;
  activityDesc: string;
  assCouponId: string;
  btnStyles: [
    {
      btnName: string;
      color: string;
      sort: number;
    },
  ];
  buyBgImg: string;
  entranceState: boolean;
  fundQuota: number;
  goodsFundState: boolean;
  homePromptBgImg: string;
  pageBgImg: string;
  pageCarousels: string;
  recyclingState: boolean;
  submitImg: string;
}
