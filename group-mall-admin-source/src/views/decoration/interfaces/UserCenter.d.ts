export interface CustomStyle {
  /** 背景图片 */
  backgroundImage: string;
}

export interface OrderItem {
  name: string;
  url: string;
}

export interface OrderInfo {
  /** 待提货 */
  afterSaleIcon: OrderItem;
  /** 待付款 */
  waitIcon: OrderItem;
  /** 待提货 */
  waitPickingIcon: OrderItem;
  /** 配送中 */
  deliveryIcon: OrderItem;
  /** 评价中心 */
  evaluateIcon: OrderItem;
  waitDelivered: OrderItem;
  [x: string]: OrderItem;
}

export interface OptionType {
  optionType?: number;
  key?: string;
}
