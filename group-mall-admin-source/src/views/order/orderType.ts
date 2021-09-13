/*
 * @description:
 * @Author: vikingShip
 * @Date: 2021-08-19 14:30:05
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-27 14:24:23
 */
/**
 * @LastEditors: vikingShip
 * @description: DeliveryState
 * @param defaultContorlOption  操作栏选项
 * @param query 物流订单查询条件
 * @param waitPayContorlOption 待付款操作选项
 * @param quicklyOption 订单状态栏筛选时间选项
 * @param data 订单数组
 * @param orderIds 选择的订单ID组
 * @param detailVisible 订单详情model标识
 * @param remarklVisible 留言modal标识
 * @param sendModalVisible 发货modal显示隐藏
 * @param printModalVisible 打印modal显示隐藏
 * @param orderDetail 订单详情
 * @param checkedItem 表格选中条目
 * @param currentOrder 当前选中的订单
 * @param needTabs 详情是否显示tab
 * @param detailType 显示类型，主要用去驱动详情modal的ui变化
 * @param logisticsCompany 物流公司
 * @param logisticsPrinterVos 打印机列表
 */
export interface DeliveryState {
  query: DeliveryOrderQueryType;
  defaultContorlOption: DeliveryToolOptions[];
  waitPayContorlOption: DeliveryToolOptions[];
  quicklyOption: DeliveryToolOptions[];
  data: DeliveryOrderList[];
  orderIds: DeliveryOrderList[];
  detailVisible: boolean;
  remarklVisible: boolean;
  sendModalVisible: boolean;
  printModalVisible: boolean;
  orderDetail: ApiOrderDetail;
  checkedItem: Array<{ orderId: string }>;
  currentOrder: DeliveryOrderList | null;
  needTabs: boolean;
  detailType: string;
  logisticsCompany: LogisticsCompanyType[];
  logisticsPrinterVos: LogisticsPrinterVos[];
}

/**
 * @LastEditors: vikingShip
 * @description: 物流公司信息
 * @param logisticsCompanyId 店铺物流公司绑定表主键
 * @param name 物流公司名称
 * @param code 物流公司code
 * @param isDefault 是否为默认物流公司
 * @param logisticsExpressAddressVos 物流公司发货地址
 */
export interface LogisticsCompanyType {
  logisticsCompanyId: string;
  name: string;
  code: string;
  isDefault: number;
  logisticsExpressAddressVos: LogisticsExpressAddressVos[];
}

/**
 * @LastEditors: vikingShip
 * @description: 设备信息
 * @param deviceType 设备类型
 * @param deviceModel 设备型号
 * @param deviceName 设备名称
 * @param deviceNo 打印机机身号
 * @param deviceKey 打印机KEY
 * @param status 状态 0-停用 1-启用
 */
export interface LogisticsPrinterVos {
  id: number;
  deviceType: string;
  deviceModel: string;
  deviceName: string;
  deviceNo: string;
  deviceKey: string;
  status: string;
}

/**
 * @LastEditors: vikingShip
 * @description:
 * @param expressId expressId
 * @param expressName 快递公司名称
 * @param expressCode 快递公司code
 * @param addressId 发货地址id 关联物流发货地址表id
 * @param customerName 客户id
 * @param customerPassword 客户密匙
 * @param province 发货人所在省
 * @param city 发货人所在市
 * @param country 发货人所在国家
 * @param address 发货人地址
 * @param receiveName 发货人名称
 * @param receivePhone 发货人电话
 * @param zipCode 发货人邮编
 */
export interface LogisticsExpressAddressVos {
  expressId: number;
  expressName: string;
  expressCode: string;
  addressId: number;
  customerName: string;
  customerPassword: string;
  province: string;
  city: string;
  country: string;
  address: string;
  receiveName: string;
  receivePhone: string;
  zipCode: string;
}
/**
 * @LastEditors: vikingShip
 * @description: 物流订单查询条件
 * @param orderStatus -1：所有订单, 0.待付款（待买家付款）, 1.待发货（买家已付款）, 2.已发货（卖家已发货）,  4.已完成（用户已经签收）, 5.已关闭
 * @param quicklyDate 近一个月->0; 近三个月->1; 全部->2;
 */
export interface DeliveryOrderQueryType {
  current: number;
  deliverType: string;
  goodsName: string;
  groupLeaderName: string;
  keyword: string;
  lineId: string;
  userName: string;
  orderId: string;
  orderStatus: string;
  payTime: string;
  quicklyDate: string;
  receiverName: string;
  size: number;
  deliverySn: string;
  remarkType: string;
  area: string;
  pointName: string;
  sendBillType: string;
  startTime: string;
  endTime: string;
}

/**
 * @LastEditors: vikingShip
 * @description: 订单评价查询条件
 */
export interface EvaluateQueryType
  extends Pick<
    DeliveryOrderQueryType,
    "goodsName" | "current" | "orderId" | "payTime"
  > {
  pageNum?: number;
  rate: string;
  total: number;
  size: number;
}

export type DeliveryToolOptions = Record<"label" | "value", string>;

/**
 * @LastEditors: vikingShip
 * @description: 订单数组
 * @param createTime 创建时间
 * @param expireTime 过期时间
 * @param itemVoList 单个商品详情
 * @param orderId 订单id
 * @param originalOrderId 原订单id，只要换货单有此属性
 * @param payAmount 订单支付金额
 * @param payType 支付类型 UNPAID, BALANCE, WECHAT, FRIEND, WECHAT_H5, FREE
 * @param status 订单状态 WAIT_FOR_PAY, WAIT_FOR_SEND, SHIPPED, WAIT_FOR_PICKUP, WAIT_FOR_COMMENT, COMPLETE, REFUNDED, PART_REFUNDED, BUYER_PAY_TIMEOUT_CLOSE, BUYER_CANCEL_CLOSE, SELLER_CANCEL_CLOSE, EXCHANGE_SUCCESS_CLOSE, EXCHANGE_CANCEL_CLOSE
 * @param type 订单类型 MALL, GROUP, SEC_KILL, EXCHANGE, REPLENISH
 */
export interface DeliveryOrderList {
  note: string;
  id: string;
  createTime: string;
  expireTime: string;
  itemVoList: DeliveryOrderGoodItem[];
  orderId: string;
  originalOrderId: number;
  payAmount: number;
  payType: string;
  status: string;
  type: string;
  close?: boolean;
  deliveryType?: string;
}

/**
 * @LastEditors: vikingShip
 * @description: 单个商品详情
 * @param id 订单详情id
 * @param orderId 订单ID
 * @param productId 商品id
 * @param productName 商品名
 * @param productOriginalPrice 指导价（划线价）
 * @param productPic 商品图片
 * @param productPrice 销售价格
 * @param productQuantity 购买数量
 * @param productSkuId 商品sku编号
 * @param realAmount 实际价格
 * @param specs 商品规格
 * @param afs 售后工单
 */
export interface DeliveryOrderGoodItem {
  id: number;
  orderId: string;
  productId: number;
  productName: string;
  productOriginalPrice: number;
  productPic: string;
  productPrice: number;
  productQuantity: number;
  productSkuId: number;
  realAmount: number;
  specs: string;
  afs: ApiDeliveryOrderAfs;
}

/**
 * @LastEditors: vikingShip
 * @description: 售后工单
 * @param closeTime 关闭时间
 * @param closeType 关闭原因：USER_CANCEL->用户撤销;POINT_CANCEL->提货点撤销;POINT_NOT_RECEIVED->提货点未收到退货;RE_EXCHANGE->重新申请换货;SELLER_REFUSE->卖家拒绝
 * @param createTime 创建时间
 * @param deadline 过期时间
 * @param deleted 删除状态：0->未删除；1->已删除
 * @param deliveryCode 物流公司编号
 * @param deliveryCompany 物流公司
 * @param deliverySn 物流单号
 * @param description 说明
 * @param id 工单id
 * @param images 照片
 * @param isLogistics 是否物流配送
 * @param no 工单编号
 * @param note 备注
 * @param orderId 售后生成的订单
 * @param productSkuId 商品sku编号
 * @param reason 申请的原因
 * @param receiptBillId 申请售后的签收单ID
 * @param refundAmount 退款金额
 * @param refusalReason 拒绝原因
 * @param refusalTime 拒绝时间
 * @param returnTemplateId 退货消息订阅ID
 * @param shopId 商铺ID
 * @param status 审核状态：WAIT_FOR_BUSINESS_APPROVED->待商家审核;WAIT_FOR_RETURN->待退货; WAIT_FOR_POINT_RECEIPT->待提货点确认收货;WAIT_FOR_SEND->待发货;WAIT_FOR_PICKUP->待提货;SHIPPED->配送中;SUCCESS->成功;CLOSE->已关闭
 * @param successTime 成功时间
 * @param templateId 消息订阅ID
 * @param tenantId 租户ID
 * @param type 工单类型：POINT_EXCHANGE->提货点换货;POINT_REFUND->提货点退款;REFUND->退款;EXCHANGE->换货;RETURN_REFUND->退货退款;
 * @param updateTime 更新时间
 * @param userAvatarUrl 用户头像
 * @param userId 申请人ID
 * @param userName 用户帐号
 */
interface ApiDeliveryOrderAfs {
  closeTime: string;
  closeType: string;
  createTime: string;
  deadline: string;
  deleted: boolean;
  deliveryCode: string;
  deliveryCompany: string;
  deliverySn: string;
  description: string;
  id: number;
  images: string;
  isLogistics: boolean;
  no: string;
  note: string;
  orderId: string;
  productSkuId: number;
  reason: string;
  receiptBillId: number;
  refundAmount: number;
  refusalReason: string;
  refusalTime: string;
  returnTemplateId: string;
  shopId: string;
  status: string;
  successTime: string;
  templateId: string;
  tenantId: string;
  type: string;
  updateTime: string;
  userAvatarUrl: string;
  userId: string;
  userName: string;
}

/**
 * @LastEditors: vikingShip
 * @description: 订单详情总集合
 */
export interface ApiOrderDetail
  extends ApiOrderAssemble,
    ApiOrderDetailFullAssemble,
    ApiOrderDetailIntegralAssemble,
    ApiOrderDetailGoodAssemble,
    ApiOrderCouponAssemble,
    ApiOrderUserAssemble {}
/**
 * @LastEditors: vikingShip
 * @description: 订单详情满减集合
 * @param fullScaleAmount 满减优惠金额
 * @param fullScaleId 满减活动ID
 */
export interface ApiOrderDetailFullAssemble {
  fullScaleAmount: number;
  fullScaleId: number;
}
/**
 * @LastEditors: vikingShip
 * @description: 订单详情积分集合
 * @param giftIntegration 赠送的积分
 * @param integration 使用的积分
 */
export interface ApiOrderDetailIntegralAssemble {
  giftIntegration: number;
  integration: number;
}

/**
 * @LastEditors: vikingShip
 * @description: 订单详情商品集合
 * @param productId 商品id
 * @param productName 商品名
 * @param productOriginalPrice 指导价（划线价），商品的指导价
 * @param productPic 商品图片
 * @param productPrice 销售价格,商品的原价
 * @param productQuantity 购买数量
 * @param productSkuCode 商品sku条码
 * @param productSkuId 商品sku编号
 * @param productSn 商品编号
 * @param productTotalQuantity 商品总数量
 * @param promotionAmount 促销优化金额（促销价）
 * @param providerId 供应商id
 * @param realAmount 多个商品经过优惠后的最终金额
 * @param specs 商品的销售属性
 */

export interface ApiOrderDetailGoodAssemble {
  productId: number;
  productName: string;
  productOriginalPrice: number;
  productPic: string;
  productPrice: number;
  productQuantity: number;
  productSkuCode: string;
  productSkuId: number;
  productSn: string;
  productTotalQuantity: number;
  promotionAmount: number;
  providerId: number;
  realAmount: number;
  specs: string;
}
/**
 * @LastEditors: vikingShip
 * @description: 订单详情订单相关集合
 * @param id 订单id
 * @param closeTime 关闭时间
 * @param commentTime 评价时间
 * @param completeTime 完成时间
 * @param activityId 所属活动id
 * @param activityIds 参与的活动
 * @param activityProductId 所属活动商品id
 * @param createTime 创建时间
 * @param customForm 自定义字段:[{"key":"IDCard","value":"332022199001010011"},{"key":"Phone","value":"13956852259"}]
 * @param deleted 删除状态：0->未删除；1->已删除
 * @param discountsAmount 实际金额
 * @param estimatedDeliveryTime 预计到货时间
 * @param expireDate 过期时间
 * @param expireTime 过期时间
 * @param freightAmount 运费金额
 * @param note 订单备注
 * @param orderDelivery 订单收货信息表
 * @param orderId 订单ID
 * @param orderItemList 单个订单商品详情
 * @param payAmount 应付金额（实际支付金额）
 * @param payTime 支付时间
 * @param payType 支付方式  WECHAT
 * @param refundAmount 退款金额
 * @param refundQuantity 退款数量
 * @param refundTransactionId 退款支付流水号
 * @param shopId 商铺ID
 * @param sourceType 订单来源
 * @param status 订单状态
 * @param tenantId 租户ID
 * @param totalAmount  订单总金额
 * @param transactionId  支付流水号
 * @param type   订单类型
 */
export interface ApiOrderAssemble {
  id: number;
  closeTime: string;
  commentTime: string;
  completeTime: string;
  afs: ApiDeliveryOrderAfs;
  activityId: number;
  activityIds: string;
  activityProductId: number;
  createTime: string;
  customForm: string;
  deleted: boolean;
  discountsAmount: number;
  estimatedDeliveryTime: string;
  expireDate: string;
  expireTime: string;
  freightAmount: number;
  note: string;
  orderDelivery: ApiOrderDeliveryType;
  orderId: number;
  orderItemList: DeliveryOrderGoodItem[];
  payAmount: number;
  payTime: string;
  payType: string;
  refundAmount: number;
  refundQuantity: number;
  refundTransactionId: string;
  shopId: string;
  sourceType: string;
  status: string;
  tenantId: string;
  totalAmount: number;
  transactionId: string;
  type: string;
}

/**
 * @LastEditors: vikingShip
 * @description: 订单详情优惠券集合
 * @param couponId 优惠券id
 * @param couponAmount 优惠券抵扣金额
 */

export interface ApiOrderCouponAssemble {
  couponId: number;
  couponAmount: number;
}
/**
 * @LastEditors: vikingShip
 * @description: 订单详情用户相关
 * @param userAvatarUrl 用户头像
 * @param userId 用户id
 * @param userName 用户帐号
 * @param userNote 用户备注
 */
export interface ApiOrderUserAssemble {
  userAvatarUrl: string;
  userId: string;
  userName: string;
  userNote: string;
}

/**
 * @LastEditors: vikingShip
 * @description: 订单物流相关信息
 * @param deleted 删除状态：0->未删除；1->已删除
 * @param deliveryCode  物流公司编码
 * @param deliveryCompany  物流公司
 * @param deliverySn  物流单号
 * @param deliveryTemplateId  发货消息订阅
 * @param deliveryTime  发货时间
 * @param deliveryType  配送方式 SELF_PICKUP, HOME_DELIVERY, LOGISTICS
 * @param orderId  订单物流id
 * @param packageCode 集包地code
 * @param packageName 集包地名称
 * @param receiveTime 确认收货时间
 * @param received 确认收货状态：0->未确认；1->已确认
 * @param receiverCity 城市
 * @param receiverDetailAddress 详细地址
 * @param receiverName 收货人姓名
 * @param receiverPhone 收货人电话
 * @param receiverPostCode 收货人邮编
 * @param receiverProvince  省份/直辖市
 * @param receiverRegion  区
 * @param shopId  商铺ID
 * @param sortingCode  分拣码
 * @param tenantId  租户ID
 */
export interface ApiOrderDeliveryType {
  deleted: boolean;
  deliveryCode: string;
  deliveryCompany: string;
  deliverySn: string;
  deliveryTemplateId: string;
  deliveryTime: string;
  deliveryType: string;
  orderId: number;
  packageCode: string;
  packageName: string;
  receiveTime: string;
  received: boolean;
  receiverCity: string;
  receiverDetailAddress: string;
  receiverName: string;
  receiverPhone: string;
  receiverPostCode: string;
  receiverProvince: string;
  receiverRegion: string;
  shopId: string;
  sortingCode: string;
  tenantId: string;
}

/**
 * @LastEditors: vikingShip
 * @description: 售后协商
 * @param applyUserType 申请用户的类型
 * @param deleted 删除状态：0->未删除；1->已删除
 * @param id 工单id
 * @param image 图片
 * @param info 详情
 */

export interface NegotiateHistoryType
  extends ApiOrderUserAssemble,
    Pick<ApiOrderDeliveryType, "shopId" | "tenantId" | "orderId"> {
  applyUserType: number;
  deleted: boolean;
  id: number;
  image: string;
  info: string;
}

/**
 * @LastEditors: vikingShip
 * @description: 评价列表
 * @param itemList
 * @param orderId 订单Id
 * @param rate 评分
 * @param shopRate 评分
 * @param userAvatarUrl 用户头像
 */
export interface ApiEvaluateListType extends ApiOrderUserAssemble {
  itemList: ApiEvaluteItemList[];
  orderId: number;
  rate: number;
  shopRate: number;
}

/**
 * @LastEditors: vikingShip
 * @description: 产品评价
 * @param choice 是否精选
 * @param comment 评论内容
 * @param deleted 删除状态：0->未删除；1->已删除
 * @param reply 卖家回复
 * @param shopId 商铺ID
 * @param specs 商品的销售属性
 * @param tenantId 租户ID
 */
export interface ApiEvaluteItemList extends DeliveryOrderGoodItem {
  choice: boolean;
  comment: string;
  deleted: boolean;
  rate: number;
  reply: string;
  shopId: string;
  specs: string;
  tenantId: string;
}

type PickApiDeliveryOrderAfs = Pick<
  ApiDeliveryOrderAfs,
  "no" | "note" | "orderId"
>;
type PickDeliveryOrderGoodItem = Pick<
  DeliveryOrderGoodItem,
  "productName" | "productPic" | "productPrice" | "productQuantity"
>;
/**
 * @LastEditors: vikingShip
 * @description: 售后订单数组
 * @param id 工单id
 * @param orderStatus 订单状态
 * @param receiverCity 城市
 * @param receiverDetailAddress 详细地址
 * @param receiverName 收货人姓名
 * @param receiverPhone 收货人电话
 * @param receiverProvince 省份/直辖市
 * @param receiverRegion 区
 */
export interface ApiAfterListType
  extends PickApiDeliveryOrderAfs,
    PickDeliveryOrderGoodItem {
  id: number;
  orderStatus: string;
  receiverCity: string;
  receiverDetailAddress: string;
  receiverName: string;
  receiverPhone: string;
  receiverProvince: string;
  receiverRegion: string;
  close: boolean;
  status: string;
}
/**
 * @LastEditors: vikingShip
 * @description: 售后订单详情
 * @param images 照片
 * @param orderId 申请售后的订单ID
 * @param productQuantity 商品数量
 * @param productSkuId 商品sku编号
 * @param reason
 * @param refundAmount 退款金额
 * @param returnTemplateId 退款金额
 * @param templateId 退款金额
 * @param type 工单类型：POINT_EXCHANGE->提货点换货; POINT_REFUND->提货点退款; REFUND->退款; EXCHANGE->换货 RETURN_REFUND->退货退款
 */
export interface ApiAfterDetail {
  id: number;
  images: string;
  orderId: number;
  productQuantity: number;
  productSkuId: number;
  reason: string;
  refundAmount: number;
  returnTemplateId: string;
  templateId: string;
  type: string;
}
