/*
 * @description:
 * @Author: vikingShip
 * @Date: 2021-08-14 13:15:26
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-27 14:17:24
 */

/**
 * @LastEditors: vikingShip
 * @description: 商品所有字段信息
 * @param albumPics 画册图片，连产品图片限制为6张，以逗号分割
 * @param attribute 销售属性
 * @param attributeId 属性模版ID
 * @param attributeName 属性模板名称
 * @param detail 商品详情
 * @param distributionMode 配送方式(0–商家配送，1–快递配送 2–同城配送)
 * @param freightTemplateId 运费模板ID
 * @param limitType 限购类型(默认统一规格，0–统一规格，1–统一限购，2–规格限购)
 * @param name 商品名称
 * @param openSpecs 规格是否展开
 * @param pic 图片地址
 * @param productSn 货号
 * @param providerId 供应商id
 * @param providerName 供应商名称
 * @param sale 销量
 * @param saleMode 销售专区(默认商超系统，0–商超系统)
 * @param score 评分
 * @param serviceIds 以逗号分割的产品服务：1->无忧退货；2->快速退款；3->免费包邮
 * @param status 状态(默认上架，0–下架，1–上架)
 * @param widePic 宽屏展示图片
 * @param saleDescribe 卖点描述
 */

export interface GoodDetailInfo {
  albumPics: string;
  attribute: string;
  attributeId: number | string;
  attributeName: string;
  detail: string;
  distributionMode: number;
  freightTemplateId: number | string;
  id: number | string | string[] | null;
  limitType: number;
  name: string;
  openSpecs: boolean;
  pic: string;
  productAttributes: Array<Partial<ProductAttributeType>>;
  productSn: string;
  providerId: number;
  providerName: string;
  sale: number | string;
  saleMode: number | string | null;
  score: number;
  serviceIds: string;
  skuStocks: ApiSkuType[];
  status: number;
  useMemberPrice: number;
  widePic: string;
  saleDescribe: string;
  productShowCategorys: Omit<
    GoodCategroyType,
    "showCategoryVos" | "sort" | "saleMode"
  >[];
  stock?: number | string;
}
/**
 * @LastEditors: vikingShip
 * @description: 商品属性
 * @param name 属性名称
 * @param productId 产品id
 * @param value 属性值
 */
export interface ProductAttributeType {
  id: number | string;
  name: string;
  productId: number | string;
  value: string;
}

/**
 * @LastEditors: vikingShip
 * @description: 商品sku
 * @param id
 * @param lowStock 预警库存
 * @param originalPrice 指导价
 * @param perLimit 限购数量
 * @param pic 展示图片
 * @param price 实售价
 * @param productId 产品id
 * @param sale 销量
 * @param skuCode sku编码
 * @param specs 商品规格
 * @param stock 库存
 * @param version 版本号
 * @param weight 商品sku重量
 */

export interface ApiSkuType {
  id: number | string;
  lowStock: number | string;
  originalPrice: number | null;
  perLimit: number | string | null;
  pic: string;
  price: number | null;
  productId: string;
  sale: number | string;
  skuCode: string;
  specs: string;
  stock: number | string;
  version: number;
  weight: string;
  maxNum: number;
  getType?: boolean;
  memPrice?: number | string;
}
/**
 * @LastEditors: vikingShip
 * @description: 产品分类
 * @param id
 * @param name 分类名称
 * @param parentId 上机分类的编号：0表示一级分类
 * @param saleMode 销售专区
 * @param showCategoryId 展示分类id(数据传输用)
 * @param showCategoryVos 分类子集
 * @param sort 排序
 */
export interface GoodCategroyType {
  id: string;
  name: string;
  parentId: string;
  saleMode: string;
  showCategoryId: string;
  showCategoryVos: GoodCategroyChildType[];
  productShowCategorySeconds?: Omit<
    GoodCategroyType,
    "showCategoryVos" | "sort" | "saleMode"
  >[];
  sort: number;
  disabled?: boolean;
}

/**
 * @LastEditors: vikingShip
 * @description: 分类子集
 * @param id
 * @param productNumber 商品数量
 */
export interface GoodCategroyChildType
  extends Omit<GoodCategroyType, "showCategoryVos"> {
  productNumber: number;
}

/**
 * @LastEditors: vikingShip
 * @description: 供应商查询返回信息
 * @param address 地区
 * @param area 完整地址
 * @param city 市
 * @param comeFrom 注册来源，0–后台注册，1–小程序
 * @param country 国家
 * @param createTime 创建时间
 * @param id
 * @param mobile 手机号码
 * @param name 供应商名称
 * @param productInfo 产品信息
 * @param province 省
 * @param score 评分（默认5.0）状态(默认待审核，0–已关闭，1–已审核，2–待审核，3–禁用中)评分（默认5.0）
 * @param supplierSn 供应商识别号
 */
export interface GoodSupplierType {
  address: string;
  area: string;
  city: string;
  comeFrom: number;
  country: string;
  createTime: string;
  id: number | string;
  mobile: string;
  name: string;
  productInfo: string;
  province: string;
  score: number;
  status: number | string;
  supplierSn: string;
}

/**
 * @LastEditors: vikingShip
 * @description: 属性模板查询返回信息
 * @param content 内容
 * @param name 模板名称
 * @param id
 * @param parentId 模板id
 */
export interface GoodAttributeTempType extends GoodComAtrType {
  attributeTemplates: GoodComAtrType[];
}

interface GoodComAtrType {
  content: string;
  name: string;
  id: number;
  parentId: number;
}

/**
 * @LastEditors: vikingShip
 * @description: 物流接口获取运费模板
 * @param choiceConditionPostage 是否指定包邮条件: 0=未指定,1=已指定
 * @param logisticsIncludePostageVos 指定包邮运送方式信息
 * @param name 模板名称
 * @param valuationModel 计价方式: 1=按件数,2=按重量
 */
export interface FreightTempType {
  choiceConditionPostage: number | boolean;
  id: number | string;
  logisticsIncludePostageVos: AssignFreightInfoType[];
  logisticsShippingModelVos: BaseFreightInfoType[];
  name: string;
  valuationModel: string;
}
/**
 * @LastEditors: vikingShip
 * @description: 指定包邮运送方式信息
 * @param amountNum 包邮金额 单位元
 * @param logisticsId 模板 id,新增运费模板的时候可不传
 * @param pieceNum 包邮 件数
 * @param region 包邮地区 格式: {"provinceid": [“cityId","cityId2”]}
 * @param type 包邮条件类型: 0= 按件数包邮,1=按重量包邮,2=按金额包邮,3=件数+金额 4=重量+金额 包邮
 * @param weight 包邮重量 单位: 千克(kg)
 */
export interface AssignFreightInfoType {
  amountNum: number | string;
  id: number | string;
  logisticsId?: number | string;
  pieceNum: number | string;
  region: string | LogisticsRegionType[];
  type: number | string;
  weight: number | string;
  area?: string;
  value?: string;
}
/**
 * @LastEditors: vikingShip
 * @description: 基础运送方式信息
 * @param firstAmount 首费 单位元
 * @param firstPiece 首件数
 * @param firstWeight 首重 单位: 千克(kg)
 * @param logisticsId 模板 id,新增的运费模板时候 不传
 * @param region 运送地区 json 格式 {"provinceid": [“cityId","cityId2”]}
 * @param secondAmount 续费, 单位: 元
 * @param secondPiece 续件数量
 * @param secondWeight 续重 单位 千克/kg
 * @param valuationModel 计价方式: 1=按件数,2=按重量
 */
export interface BaseFreightInfoType {
  firstAmount: number | string;
  firstPiece: number | string;
  firstWeight: number | string;
  id: number | string;
  logisticsId: number | string;
  region: string | LogisticsRegionType[];
  secondAmount: number | string;
  secondPiece: number | string;
  secondWeight: number | string;
  valuationModel: number | string;
  area?: string;
}

export interface LogisticsRegionType {
  list: Array<{ value: string }>;
  fatherid: string | number;
}
