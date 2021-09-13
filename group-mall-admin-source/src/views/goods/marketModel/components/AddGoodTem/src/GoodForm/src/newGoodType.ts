/*
 * @description:
 * @Author: vikingShip
 * @Date: 2021-08-16 11:17:32
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-27 16:50:08
 */
import { GoodDetailInfo } from "@/views/goods/marketModel/goodType";
/**
 * @LastEditors: vikingShip
 * @description: newGoodForm组件state
 * @param allFoorm 总数据
 * @param currentStep 当前步骤
 * @param logisticsId 运费模板id
 * @param isWeight 是否需要显示重量
 * @param checkFlag 表单检测标识
 */
export interface NewGoodState {
  allFoorm: AddSubmitFormType;
  currentStep: string;
  goodFormModel: Partial<AddSubmitFormType>;
  goodDetail: AddSubmitFormType | null;
  logisticsId: string;
  isWeight: boolean;
  checkFlag: boolean;
}

/**
 * @LastEditors: vikingShip
 * @description: 步骤
 * @param prev 上一步
 * @param next 下一步
 * @param setpIndex 当前进度
 */
export interface StepIndicator {
  prev: string;
  next: string;
  setpIndex: number;
}

/**
 * @LastEditors: vikingShip
 * @description: 新增商品提交表单
 * @param distributionSets 商品分销设置
 * @param dominoState 参与接龙 0–未参加，1–已参加
 * @param saleName 销售名称 导入用
 * @param unit 重量单位
 * @param videoUrl 商品视频地址
 * @param weight 商品长度
 */
export interface AddSubmitFormType extends GoodDetailInfo {
  distributionSets: any[];
  dominoState: number;
  saleName: string;
  unit: string;
  videoUrl: string;
  weight: string;
  showCategoryIds: string[] | number[];
  originalPrice: number | null;
  price: number | null;
  place?: number;
}
