declare namespace After {
  /** 申请售后表单 */
  export interface Form {
    description: string;
    images: string;
    /** 退货或者换货数量 */
    productQuantity: string;
    productSkuId: string;
    refundAmount: string;
    templateId: string;
    type:
      | "POINT_EXCHANGE"
      | "POINT_REFUND"
      | "REFUND"
      | "EXCHANGE"
      | "RETURN_REFUND";
    /** 售后标题 */
    title?: string;
    /** url上级带来的商品数据 */
    goods?: string;
    userType?: "group" | "user";
  }
}
