/*
 * @description: 抽离开源版本
 * @Author: chuyinlong
 * @Date: 2021-08-20 17:29:06
 * @LastEditors: chuyinlong
 * @LastEditTime: 2021-08-27 14:22:27
 */
export default interface LinkSelectItem {
  id: number | string;
  /** 链接类型 0 -功能页面 FunctionPage 1-商超商品 Goods 2 -展示分类 DisplayClassify 3-自定义页面 CustomPage */
  type: number;
  /** 链接名称 */
  name: string;
  /** 链接地址 */
  url: string;
  /** 附加参数 */
  append?: string;
}

interface ITypeNameMap {
  [x: number]: {
    text: string;
    name: string;
  };
}

export const typeNameMap: ITypeNameMap = {
  0: {
    text: "功能页面",
    name: "FunctionPage",
  },
  1: {
    text: "商品",
    name: "Goods",
  },
  2: {
    text: "商品专区",
    name: "DisplayClassIfy",
  },
  5: {
    text: "自定义页面",
    name: "CustomPage",
  },
};
