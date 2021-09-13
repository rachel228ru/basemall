/*
 * @description:
 * @Author: vikingShip
 * @Date: 2021-08-18 09:31:15
 * @LastEditors: vikingShip
 * @LastEditTime: 2021-08-18 09:45:19
 */
import { AddSubmitFormType } from "./newGoodType";
/**
 * @LastEditors: vikingShip
 * @description: newGoodDetail/state
 * @param detailText 商品详情
 * @param configEditor 富文本编辑器配置
 * @param temModelexImg  图片本地路径
 * @param modelValue  select选中下标绑定值
 * @param modelOptions  详情模板
 * @param showTemModel  显示示例
 */
export interface NewGoodDetailState {
  detailText: string;
  configEditor: any;
  formModel: Partial<AddSubmitFormType>;
  exImg:string
  temModel:string
  modelValue:number
  modelOptions:Array<{
      label:string
      value:number
  }>
  showTemModel:boolean
}
