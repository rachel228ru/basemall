/*
 * @description:
 * @Author: vikingShip
 * @Date: 2021-08-16 13:42:15
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-25 13:44:33
 */
import { ApiSpecArea } from "@/views/goods/marketModel/marketType";
import {
  GoodCategroyType,
  GoodSupplierType,
  GoodAttributeTempType,
  FreightTempType,
  GoodDetailInfo,
} from "@/views/goods/marketModel/goodType";
/**
 * @LastEditors: vikingShip
 * @description: newGoodBaseInfo/state
 * @param formModel 提交数据
 * @param showAttributes 显隐属性标识
 * @param saleApartList 销售专区数组
 * @param temShowList 展示分类数组
 * @param temSupList 供应商数组
 * @param temAttsList 属性模板数组
 * @param logModel 物流模板数组
 * @param productImgList 上传商品图片
 * @param productImgIndex 上传图片下标
 * @param showSale 是否导入商品
 */
export interface NewGoodBaseState {
  formModel: Partial<AnyMixinGoodDetailInfo>;
  widePic: string;
  showAttributes: boolean;
  saleApartList: ApiSpecArea[];
  temShowList: GoodCategroyType[];
  temSupList: GoodSupplierType[];
  temAttsList: GoodAttributeTempType[];
  logModel: FreightTempType[];
  productImgList: Array<{ img: string }>;
  productImgIndex: number;
  showSale: boolean;
  saleMode: string | number | null;
}

export interface AnyMixinGoodDetailInfo extends GoodDetailInfo {
  [x: string]: any;
}
