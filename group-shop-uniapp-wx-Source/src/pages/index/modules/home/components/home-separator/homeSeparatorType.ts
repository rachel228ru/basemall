/*
 * @description: 
 * @Author: vikingShip
 * @Date: 2021-08-02 15:05:56
 * @LastEditors: vikingShip
 * @LastEditTime: 2021-08-02 15:35:13
 */

/**
 * @LastEditors: vikingShip
 * @description: 组件state类型
 * @param formData
 * @param inlineBox 分隔符边框样式
 * @param inlineStyle 分隔符线条样式
 */
export interface HomeSeparatorType{
    formData:Partial<ISeparator>
    inlineBox:string
    inlineStyle:string
}

/**
 * @LastEditors: vikingShip
 * @description: 分隔符描述
 * @param borderColor  颜色
 * @param hasMargin 边距
 * @param borderStyle 样式
 */

export interface ISeparator {
    borderColor: string;
    hasMargin: boolean;
    borderStyle: string;
  }