export interface CustomStyle {
    /** 背景图片 */
    backgroundImage: string;
    /** 卡面颜色 */
    cardColor: string;
    /** 文字颜色 */
    textColor: string;
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
}

export interface OptionType {
    optionType?: number;
    key?: "";
}
